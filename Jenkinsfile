pipeline {
    agent none   // 🔥 IMPORTANT (we define agent per stage)

    environment {
        SONAR_TOKEN = credentials('sonar-test-token')
        SNYK_TOKEN = credentials('snyk-test-token')
    }

    options {
        timestamps()
    }

    stages {

        // =========================
        // 🔹 CHECKOUT
        // =========================
        stage('Checkout') {
            agent any
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

                // 🔹 BUILD
                stage('Build') {
                    agent {
                        docker {
                            image 'maven:3.9.6-eclipse-temurin-17'
                        }
                    }
                    steps {
                        sh 'mvn clean install -DskipTests'
                    }
                }

                // 🔹 SONAR
                stage('Sonar (SAST)') {
                    agent {
                        docker {
                            image 'maven:3.9.6-eclipse-temurin-17'
                        }
                    }
                    steps {
                        catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        sh """
                        mvn sonar:sonar \
                        -Dsonar.projectKey=bipraraksha1995_calculator \
                        -Dsonar.organization=bipraraksha1995 \
                        -Dsonar.login=$SONAR_TOKEN
                        """
                        }
                    }
                }

                // 🔹 SNYK
                stage('Snyk (SCA)') {
                    agent {
                        docker {
                            image 'node:18'
                        }
                    }
                    steps {
                        withCredentials([string(credentialsId: 'snyk-token', variable: 'SNYK_TOKEN')]) {
                            catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
                        sh '''
                        export SNYK_TOKEN=$SNYK_TOKEN
                        npx snyk auth
                        npx snyk test
                        '''
                            }
                        }
                    }
                }
            }
        }

        // =========================
        // 🔹 QA (ZAP)
        // =========================
        stage('QA - DAST (ZAP)') {
            when {
                anyOf {
                    expression { env.BRANCH_NAME.startsWith('qa-') }
                    changeRequest()
                }
            }
            agent {
                docker {
                    image 'ghcr.io/zaproxy/zaproxy:stable'
                    args '--network=host'
                }
            }
            steps {
                sh '''
                zap-baseline.py -t http://example.com || true
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
            agent any
            steps {
                input message: 'Approve Production Deployment?'
            }
        }

        // =========================
        // 🔹 PROD (TRIVY)
        // =========================
        stage('Prod - Security + Release') {
            when {
                branch 'main'
            }
            agent {
                docker {
                    image 'aquasec/trivy:0.50.0'
                }
            }
            steps {
                sh 'trivy fs . || true'
            }
        }
    }
}
