pipeline {
  agent any

  stages {

    stage('Clone Shared Library') {
      steps {
        dir('shared-lib') {
          git branch: 'feature', url: 'git@github.com:thani2808/common-repository-new.git', credentialsId: 'private-key-jenkins'
        }
      }
    }

    stage('Load Shared Logic & Env') {
      steps {
        script {
          def envLoader = new org.example.EnvLoader(this)
          def envVars = envLoader.load()
          env.DOCKERHUB_USERNAME = envVars.DOCKERHUB_USERNAME
          env.GIT_CREDENTIALS_ID = envVars.GIT_CREDENTIALS_ID
        }
      }
    }

    stage('Clean Workspace') {
      steps {
        script {
          new org.example.WorkspaceCleaner(this).clean()
        }
      }
    }

    stage('Checkout Application Repo') {
      steps {
        script {
          def checkout = new org.example.RepoCheckout(this)
          def repoInfo = checkout.checkout(env.GIT_CREDENTIALS_ID)
          env.TARGET_REPO = repoInfo.repo
          env.TARGET_BRANCH = repoInfo.branch
        }
      }
    }

    stage('Initialize Configuration') {
      steps {
        script {
          def config = new org.example.EnvLoader(this).loadAppConfig(env.TARGET_REPO)
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
          new org.example.AppBuilder(this)
            .build(env.APP_TYPE, env.IMAGE_NAME)
        }
      }
    }

    stage('Run Docker Container') {
      steps {
        script {
          new org.example.ContainerRunner(this)
            .run(env.CONTAINER_NAME, env.IMAGE_NAME, env.HOST_PORT, env.DOCKER_PORT, env.APP_TYPE)
        }
      }
    }

    stage('Health Check') {
      steps {
        script {
          new org.example.HealthCheckRunner(this)
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
