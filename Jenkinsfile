@Library('common-repository-new@feature') _

import org.example.*

pipeline {
    agent any

    parameters {
        // Dynamically filled REPO_NAME and REPO_BRANCH via Active Choices
        string(name: 'REPO_NAME', defaultValue: 'Dev-role-Springboot-proj', description: 'GitHub Repository Name')
        string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout (populated dynamically)')
        choice(name: 'ENV', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
    }

    environment {
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Initialize Environment') {
            steps {
                script {
                    // ✅ Initialize once and reuse
                    builder = new ApplicationBuilder(this)
                    builder.initialize()
                }
            }
        }

        stage('Checkout Repository') {
            steps {
                script {
                    builder.checkout()
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    builder.buildImage()
                }
            }
        }

        stage('Run Container') {
            steps {
                script {
                    builder.runContainer()
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    builder.healthCheck()
                }
            }
        }

        stage('Post Build Report') {
            steps {
                script {
                    builder.sendBuildReport()
                }
            }
        }
    }

    post {
        always {
            script {
                builder.cleanWorkspace()
            }
        }
    }
}
