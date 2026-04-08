pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-test-token')
        SNYK_TOKEN = credentials('snyk-test-token')
    }

    options {
        skipDefaultCheckout(true)
        timestamps()
    }

    stages {

        // =========================
        // 🔹 CHECKOUT
        // =========================
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // =========================
        // 🔹 DEV (PARALLEL)
        // =========================
        stage('Dev - Build & Security') {
            when {
                anyOf {
                    branch 'dev'
                    branch 'main'
                    expression { env.BRANCH_NAME.startsWith('qa-') }
                    changeRequest()
                }
            }

            parallel {

                stage('Build') {
                    steps {
                        sh 'mvn clean install -DskipTests'
                    }
                }

                stage('Sonar (SAST)') {
                    steps {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=bipraraksha1995_calculator \
                        -Dsonar.organization=bipraraksha1995 \
                        -Dsonar.login=$SONAR_TOKEN
                        """
                    }
                }

                stage('Snyk (SCA)') {
                    steps {
                        sh """
                        snyk auth $SNYK_TOKEN
                        snyk test || true
                        """
                    }
                }
            }
        }

        // =========================
        // 🔹 QA (DAST)
        // =========================
        stage('QA - DAST (ZAP)') {
            when {
                anyOf {
                    expression { env.BRANCH_NAME.startsWith('qa-') }
                    changeRequest()
                }
            }
            steps {
                sh '''
                python3 -m http.server 8080 &
                sleep 10
                '''

                sh '''
                docker run --rm \
                --network="host" \
                ghcr.io/zaproxy/zaproxy:stable \
                zap-baseline.py -t http://127.0.0.1:8080 || true
                '''
            }
        }

        // =========================
        // 🔹 PROD APPROVAL
        // =========================
        stage('Approval for PROD') {
            when {
                branch 'main'
            }
            steps {
                input message: 'Approve Production Deployment?'
            }
        }

        // =========================
        // 🔹 PROD
        // =========================
        stage('Prod - Security + Release') {
            when {
                branch 'main'
            }
            steps {
                sh 'mvn clean package'

                sh '''
                docker run --rm \
                -v $PWD:/app \
                aquasec/trivy fs /app
                '''
            }
        }
    }
}
