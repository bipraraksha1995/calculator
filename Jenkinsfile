pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonar-test-token')
        SNYK_TOKEN = credentials('snyk-test-token')
    }

    stages {

        // =========================
        // 🔹 DEV STAGE
        // =========================
        stage('Dev - Build + SAST + SCA') {
            when {
                anyOf {
                    branch 'dev'
                    branch 'main'
                    expression { env.BRANCH_NAME.startsWith('qa-') }
                }
            }
            steps {
                git branch: "${env.BRANCH_NAME}", url: 'https://github.com/your-repo.git'

                sh 'mvn clean install -DskipTests'

                // Sonar Scan
                sh """
                mvn sonar:sonar \
                -Dsonar.projectKey=bipraraksha1995_calculator \
                -Dsonar.organization=bipraraksha1995 \
                -Dsonar.login=$SONAR_TOKEN
                """

                // Snyk Scan
                sh """
                snyk auth $SNYK_TOKEN
                snyk test || true
                """
            }
        }

        // =========================
        // 🔹 QA STAGE
        // =========================
        stage('QA - DAST (ZAP)') {
            when {
                expression { env.BRANCH_NAME.startsWith('qa-') }
            }
            steps {
                // Start test server
                sh '''
                python3 -m http.server 8080 &
                sleep 10
                '''

                // ZAP Scan (Docker)
                sh '''
                docker run --rm \
                --network="host" \
                ghcr.io/zaproxy/zaproxy:stable \
                zap-baseline.py -t http://127.0.0.1:8080 || true
                '''
            }
        }

        // =========================
        // 🔹 PROD STAGE
        // =========================
        stage('Approval for PROD') {
            when {
                branch 'main'
            }
            steps {
                input message: 'Approve Production Deployment?'
            }
        }

        stage('Prod - Security + Release') {
            when {
                branch 'main'
            }
            steps {
                sh 'mvn clean package'

                // Trivy Scan
                sh '''
                docker run --rm \
                -v $PWD:/app \
                aquasec/trivy fs /app
                '''

                // Nessus (optional trigger)
                sh '''
                curl -X POST "https://nessus-server:8834/scans" \
                -H "X-ApiKeys: accessKey=xxx; secretKey=xxx"
                '''
            }
        }
    }
}
