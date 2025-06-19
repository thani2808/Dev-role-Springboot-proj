@Library('common-repository-new@feature') _
import org.example.*

pipeline {
  agent any

  parameters {
    string(name: 'REPO_NAME', defaultValue: 'Dev-role-Springboot-proj', description: 'Repository Name to checkout')
    string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout')
  }

  environment {
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

          env.APP_TYPE = envVars.APP_TYPE
          env.IMAGE_NAME = envVars.IMAGE_NAME
          env.CONTAINER_NAME = envVars.CONTAINER_NAME
          env.HOST_PORT = envVars.HOST_PORT
          env.DOCKER_PORT = envVars.DOCKER_PORT
          env.DOCKERHUB_USERNAME = envVars.DOCKERHUB_USERNAME
          env.GIT_CREDENTIALS_ID = envVars.GIT_CREDENTIALS_ID
          env.GIT_URL = envVars.GIT_URL // Optional if used later
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
        echo "✅ Configuration initialized: ${env.CONTAINER_NAME}, ${env.HOST_PORT}, ${env.APP_TYPE}"
      }
    }

    stage('Build Application') {
      steps {
        script {
          echo "Repo: ${params.REPO_NAME}, Branch: ${params.REPO_BRANCH}"
          def appBuilder = new ApplicationBuilder(this)

          if (params.REPO_NAME && params.REPO_BRANCH) {
            appBuilder.build(params.REPO_NAME, params.REPO_BRANCH)
          } else {
            error "❌ Repo or Branch name is missing"
          }
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
