
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/nhidinh91/AikidoPracticeTracker.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Code Coverage') {
                    steps {
                        bat 'mvn jacoco:report'
                    }
                }
                stage('Publish Test Results') {
                    steps {
                        junit '**/target/surefire-reports/*.xml'
                    }
                }
                stage('Publish Coverage Report') {
                    steps {
                        jacoco()
                    }
                }
    }

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            jacoco execPattern: '**/target/jacoco.exec'
        }
    }
}

