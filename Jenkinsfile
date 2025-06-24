@Library('common-repository-new@feature') _
import org.example.*

pipeline {
  agent any

  parameters {
    string(name: 'REPO_NAME', defaultValue: 'eureka-discovery-server', description: 'Repository Name to checkout')
    string(name: 'REPO_BRANCH', defaultValue: 'feature', description: 'Branch to checkout')
  }

  stages {

    stage('Startup Debug') {
      steps {
        echo "⚙️ Startup – params.REPO_NAME = '${params.REPO_NAME}'"
        echo "⚙️ Startup – env.JOB_NAME     = '${env.JOB_NAME}'"
      }
    }

    stage('Initialize Environment') {
      steps {
        script {
          app = new ApplicationBuilder(this)
          app.initialize()
        }
      }
    }

    stage('Clean Workspace') {
      steps {
        script {
          app.cleanWorkspace()
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
        echo "🎉 Deployment successful for ${params.REPO_NAME} [${params.REPO_BRANCH}]"
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
