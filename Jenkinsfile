@Library('common-repository-new@feature') _
import org.example.*

pipeline {
  agent any

  parameters {
    string(name: 'REPO_NAME', defaultValue: 'Dev-role-Springboot-proj', description: 'Repository Name to checkout')
    string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout')
  }

  environment {
    ENV_LIST = ''
  }

  stages {

    stage('Startup Debug') {
      steps {
        echo "⚙️ Startup – params.REPO_NAME = '${params.REPO_NAME}'"
        echo "⚙️ Startup – env.JOB_NAME     = '${env.JOB_NAME}'"
      }
    }

    stage('Load Shared Logic & Env') {
      steps {
        script {
          def envList = new EnvironmentInitializer(this).initialize()
          if (!envList || envList.isEmpty()) {
            error "❌ Failed to load environment variables from EnvironmentInitializer"
          }
          env.ENV_LIST = envList.join('\n')
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
          if (!env.ENV_LIST) {
            error "❌ ENV_LIST is not set. Cannot proceed with configuration."
          }
          withEnv(env.ENV_LIST.split('\n')) {
            echo "✅ Configuration initialized:"
            echo "➡️ CONTAINER_NAME = '${env.CONTAINER_NAME}'"
            echo "➡️ HOST_PORT      = '${env.HOST_PORT}'"
            echo "➡️ APP_TYPE       = '${env.APP_TYPE}'"
          }
        }
      }
    }

    stage('Build Application') {
      steps {
        script {
          echo "🔨 Building Repo: ${params.REPO_NAME}, Branch: ${params.REPO_BRANCH}"
          def appBuilder = new ApplicationBuilder(this)

          if (params.REPO_NAME && params.REPO_BRANCH) {
            appBuilder.build(params.REPO_NAME, params.REPO_BRANCH)
          } else {
            error "❌ Repo or Branch name is missing"
          }
        }
      }
    }

    stage('Pre-Run Debug') {
      steps {
        script {
          if (!env.ENV_LIST) {
            error "❌ ENV_LIST is not set. Cannot proceed with Pre-Run Debug."
          }
          withEnv(env.ENV_LIST.split('\n')) {
            echo "🔧 Pre-Run – env.APP_TYPE       = '${env.APP_TYPE}'"
            echo "🔧 Pre-Run – env.IMAGE_NAME     = '${env.IMAGE_NAME}'"
            echo "🔧 Pre-Run – env.CONTAINER_NAME = '${env.CONTAINER_NAME}'"

            if (!env.APP_TYPE) {
              error "❌ Pre-Run check failed: APP_TYPE still null!"
            }
          }
        }
      }
    }

    stage('Run Container') {
      steps {
        script {
          if (!env.ENV_LIST) {
            error "❌ ENV_LIST is not set. Cannot run container."
          }
          withEnv(env.ENV_LIST.split('\n')) {
            new RunContainer(this)
              .run(env.CONTAINER_NAME, env.IMAGE_NAME, env.HOST_PORT, env.DOCKER_PORT, env.APP_TYPE)
          }
        }
      }
    }

    stage('Health Check') {
      steps {
        script {
          if (!env.ENV_LIST) {
            error "❌ ENV_LIST is not set. Cannot run health check."
          }
          withEnv(env.ENV_LIST.split('\n')) {
            new HealthCheck(this)
              .check(env.HOST_PORT, env.CONTAINER_NAME, env.APP_TYPE)
          }
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
