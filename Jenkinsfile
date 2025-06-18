@Library('common-repository-new') _
import org.example.*

pipeline {
  agent any

  environment {
    // Will be set in 'Load Shared Logic & Env'
    DOCKERHUB_USERNAME = ''
    GIT_CREDENTIALS_ID = ''
    TARGET_REPO = ''
    TARGET_BRANCH = ''
    APP_TYPE = ''
    IMAGE_NAME = ''
    CONTAINER_NAME = ''
    HOST_PORT = ''
    DOCKER_PORT = ''
  }

  stages {

    stage('Load Shared Logic & Env') {
      steps {
        script {
          def envLoader = new EnvLoader(this)
          def envVars = envLoader.load()

          env.DOCKERHUB_USERNAME = envVars.DOCKERHUB_USERNAME
          env.GIT_CREDENTIALS_ID = envVars.GIT_CREDENTIALS_ID
        }
      }
    }

    stage('Clean Workspace') {
      steps {
        script {
          new WorkspaceCleaner(this).clean()
        }
      }
    }

    stage('Checkout Application Repo') {
      steps {
        script {
          def checkout = new RepoCheckout(this)
          def repoInfo = checkout.checkout(env.GIT_CREDENTIALS_ID)

          env.TARGET_REPO = repoInfo.repo
          env.TARGET_BRANCH = repoInfo.branch
        }
      }
    }

    stage('Initialize Configuration') {
      steps {
        script {
          def config = new EnvLoader(this).loadAppConfig(env.TARGET_REPO)

          env.APP_TYPE = config.APP_TYPE
          env.IMAGE_NAME = config.IMAGE_NAME
          env.CONTAINER_NAME = config.CONTAINER_NAME
          env.HOST_PORT = config.HOST_PORT
          env.DOCKER_PORT = config.DOCKER_PORT
        }
      }
    }

    stage('Build Application') {
      steps {
        script {
          new AppBuilder(this)
            .build(env.APP_TYPE, env.IMAGE_NAME)
        }
      }
    }

    stage('Run Docker Container') {
      steps {
        script {
          new ContainerRunner(this)
            .run(env.CONTAINER_NAME, env.IMAGE_NAME, env.HOST_PORT, env.DOCKER_PORT, env.APP_TYPE)
        }
      }
    }

    stage('Health Check') {
      steps {
        script {
          new HealthCheckRunner(this)
            .check(env.HOST_PORT, env.CONTAINER_NAME, env.APP_TYPE)
        }
      }
    }

    stage('Deployment Success') {
      steps {
        echo "✅ Deployment successful for ${env.TARGET_REPO} [${env.TARGET_BRANCH}]"
      }
    }
  }

  post {
    failure {
      echo '❌ Deployment failed. Check logs.'
    }
    always {
      echo '🎯 Pipeline execution completed.'
    }
  }
}
