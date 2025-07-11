@Library('common-10072025@feature') _
import org.example.*

def app = null  // ✅ Global ApplicationBuilder reference

pipeline {
  agent any

  tools {
    git 'Git'  // 👈 Jenkins global tool config
  }

  parameters {
    string(name: 'REPO_NAME', defaultValue: 'Dev-role-Springboot-proj', description: 'Repository Name to checkout')
    string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout')
    choice(name: 'ENV_STAGE', choices: ['dev', 'staging', 'prod'], description: 'Deployment Environment')
  }

  stages {

    stage('Startup Debug') {
      steps {
        echo "⚙️ Startup – params.REPO_NAME = '${params.REPO_NAME}'"
        echo "⚙️ Startup – params.ENV_STAGE = '${params.ENV_STAGE}'"
        echo "⚙️ Startup – env.JOB_NAME     = '${env.JOB_NAME}'"
      }
    }

    stage('Initialize Environment') {
      steps {
        script {
          try {
            app = new ApplicationBuilder(this)
            app.initialize()  // 🚀 Now includes platform-specific init.sh/init.bat execution
          } catch (err) {
            echo "❌ Error during environment initialization: ${err}"
            throw err
          }
        }
      }
    }

    stage('Checkout Repo') {
      steps {
        script {
          app.checkout(params.REPO_BRANCH)
        }
      }
    }

    stage('Build Application') {
      steps {
        script {
          app.build(params.REPO_BRANCH)
        }
      }
    }

    stage('Pre-Run Debug') {
      steps {
        script {
          app.preRunDebug()
        }
      }
    }

    stage('Run Container') {
      steps {
        script {
          app.runContainer()
        }
      }
    }

    stage('Health Check') {
      steps {
        script {
          app.healthCheck()
        }
      }
    }

    stage('Success') {
      steps {
        echo "🎉 Deployment successful for ${params.REPO_NAME} [${params.REPO_BRANCH}] in ${params.ENV_STAGE}"
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
