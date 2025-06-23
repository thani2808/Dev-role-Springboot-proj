@Library('common-repository-new@feature') _
import org.example.*

pipeline {
    agent any

    parameters {
        string(name: 'REPO_NAME', defaultValue: 'Dev-role-Springboot-proj', description: 'Repository Name to checkout')
        string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout')
        choice(name: 'ENV', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
    }

    environment {
        IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Initialize Environment') {
            steps {
                script {
                    // ✅ Create ApplicationBuilder with `this`
                    def builder = new org.example.ApplicationBuilder(this)
                    builder.initialize()
                }
            }
        }

        stage('Checkout Repository') {
            steps {
                script {
                    def builder = new org.example.ApplicationBuilder(this)
                    builder.checkout()
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    def builder = new org.example.ApplicationBuilder(this)
                    builder.buildImage()
                }
            }
        }

        stage('Run Container') {
            steps {
                script {
                    def builder = new org.example.ApplicationBuilder(this)
                    builder.runContainer()
                }
            }
        }

        stage('Health Check') {
            steps {
                script {
                    def builder = new org.example.ApplicationBuilder(this)
                    builder.healthCheck()
                }
            }
        }

        stage('Post Build Report') {
            steps {
                script {
                    def builder = new org.example.ApplicationBuilder(this)
                    builder.sendBuildReport()
                }
            }
        }
    }

    post {
        always {
            script {
                def builder = new org.example.ApplicationBuilder(this)
                builder.cleanWorkspace()
            }
        }
    }
}
