pipeline {
    agent any

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=$WORKSPACE/.m2/repository'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'chmod +x mvnw'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -DskipTests'
            }
        }
    }

    post {
        success {
            echo 'Build and test completed successfully!'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
        }
        failure {
            echo 'Build or test failed!'
        }
        always {
            cleanWs()
        }
    }
}
