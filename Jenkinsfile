@Library('common-repository-new@feature') _
import org.example.*

pipeline {
    agent any

    parameters {
        string(name: 'REPO_NAME', defaultValue: '', description: 'GitHub Repo Name (auto-populated)')
        string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout (auto-populated)')
        choice(name: 'ENV', choices: ['dev', 'staging', 'prod'], description: 'Deployment environment')
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
                    builder.checkout(params.REPO_BRANCH)
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    builder.build(params.REPO_BRANCH)
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
                    echo "📄 Build report generated: build-report.txt"
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
