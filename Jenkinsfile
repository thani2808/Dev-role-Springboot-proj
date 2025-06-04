pipeline {
    agent any

    environment {
        IMAGE_NAME = "role-app"
        CONTAINER_NAME = "role-container"      // corrected to match usage below
        APP_PORT = "8085"        // Container app port
        HOST_PORT = "8081"       // Mapped host port for app
        EUREKA_PORT = "8761"     // Eureka server port
    }

    stages {
        stage('Clone the Repo') {
            steps {
                echo 'Cloning the repository...'
                deleteDir()
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/feature']],
                    userRemoteConfigs: [[
                        url: 'git@github.com:thani2808/Dev-role-Springboot-proj.git',
                        credentialsId: 'private-key-jenkins'
                    ]]
                ])
            }
        }

        stage('Build JAR') {
            steps {
                dir('role/role') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('role/role') {
                    script {
                        if (!fileExists('Dockerfile')) {
                            error "Dockerfile not found!"
                        }
                    }
                    bat "docker build -t ${IMAGE_NAME} ."
                }
            }
        }

        stage('Stop and Remove Old Container') {
            steps {
                script {
                    bat """
                        docker stop ${CONTAINER_NAME} || exit 0
                        docker rm ${CONTAINER_NAME} || exit 0
                    """
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                bat "docker run -d --name ${CONTAINER_NAME} -p ${HOST_PORT}:${APP_PORT} ${IMAGE_NAME}"
            }
        }

        stage('Success Confirmation') {
            steps {
                echo '✅ Application has been deployed successfully via Docker!'
            }
        }
    }

    post {
        failure {
            echo '❌ Pipeline failed. Check the logs for details.'
        }
        always {
            echo 'Pipeline execution finished.'
        }
    }
}
