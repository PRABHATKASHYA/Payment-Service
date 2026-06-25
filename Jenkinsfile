pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK 17'
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=$WORKSPACE/.m2/repository'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
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
