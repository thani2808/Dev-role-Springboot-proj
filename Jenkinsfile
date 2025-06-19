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

    stage('Startup Debug') {
      steps {
        echo "⚙️ Startup – params.REPO_NAME = '${params.REPO_NAME}'"
        echo "⚙️ Startup – env.JOB_NAME     = '${env.JOB_NAME}'"
        echo "⚙️ Startup – env.APP_TYPE     = '${env.APP_TYPE}'"
      }
    }

    stage('Load Shared Logic & Env') {
      steps {
        script {
          new EnvironmentInitializer(this).initialize()
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
          echo "🔧 Pre-Run – env.APP_TYPE       = '${env.APP_TYPE}'"
          echo "🔧 Pre-Run – env.IMAGE_NAME     = '${env.IMAGE_NAME}'"
          echo "🔧 Pre-Run – env.CONTAINER_NAME = '${env.CONTAINER_NAME}'"

          if (!env.APP_TYPE) {
            error "❌ Pre-Run check failed: APP_TYPE still null!"
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
