@Library('common-repository-new@feature') _
import org.example.*

pipeline {
    agent any

    parameters {
        string(name: 'REPO_NAME', defaultValue: 'Dev-role-Springboot-proj', description: 'GitHub Repo Name (auto-populated)')
        string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout (auto-populated)')
        choice(name: 'ENV', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
    }

    environment {
        // Optional: If you want to use these vars elsewhere in the pipeline
        PROJECT_REPO = "${params.REPO_NAME}"
        PROJECT_BRANCH = "${params.REPO_BRANCH}"
        DEPLOY_ENV = "${params.ENV}"
    }

    stages {
        stage('Initialize Environment') {
            steps {
                script {
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
                    builder.build()
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
                    echo "📄 Build report generated:"
                    echo readFile("build-report.txt")
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
