@Library('common-repository-new') _
import org.example.*

pipeline {
  agent any

  parameters {
    string(name: 'REPO_NAME', defaultValue: '', description: 'Repository Name to checkout')
    string(name: 'REPO_BRANCH', defaultValue: 'main', description: 'Branch to checkout')
  }

  environment {
    // These will be populated from EnvLoader in the shared library:
    // git@github.com:thani2808/common-repository-new.git
    DOCKERHUB_USERNAME = ''       // Loaded from shared lib
    GIT_CREDENTIALS_ID = ''       // Loaded from shared lib

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

          // Assign values imported from the shared GitHub repository
          env.DOCKERHUB_USERNAME = envVars.DOCKERHUB_USERNAME
          env.GIT_CREDENTIALS_ID = envVars.GIT_CREDENTIALS_ID
        }
      }
    }

    stage('Clean Workspace') {
      steps {
        script {
          new CleanWorkspace(this).clean()
        }
      }
    }

    stage('Checkout Repo') {
      steps {
        script {
          def checkout = new CheckoutTargetRepoImpl(this)
          def repoInfo = checkout.checkout(params.REPO_NAME, params.REPO_BRANCH)

          env.TARGET_REPO = repoInfo.repo
          env.TARGET_BRANCH = repoInfo.branch
        }
      }
    }

    stage('Initialize Configuration') {
      steps {
        script {
          def config = new InitEnv(this).initialize()

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
          new ApplicationBuilder(this)
            .build(env.APP_TYPE, env.IMAGE_NAME)
        }
      }
    }

    stage('Run Container') {
      steps {
        script {
          new RunContainer(this)
            .run(env.CONTAINER_NAME, env.IMAGE_NAME, env.HOST_PORT, env.DOCKER_PORT, env.APP_TYPE)
        }
      }
    }

    stage('Health Check') {
      steps {
        script {
          new HealthCheck(this)
            .check(env.HOST_PORT, env.CONTAINER_NAME, env.APP_TYPE)
        }
      }
    }

    stage('Success') {
      steps {
        echo "🎉 Deployment successful for ${env.TARGET_REPO} [${env.TARGET_BRANCH}]"
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
