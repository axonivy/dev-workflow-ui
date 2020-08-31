pipeline {
  agent {
    docker {
      image 'mstruebing/editorconfig-checker:2.1.0'
    }
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '60', artifactNumToKeepStr: '1'))
  }

  triggers {
    cron '@midnight'
    pollSCM('H/10 * * * *')
  }

  stages {
    stage('check') {
      steps {
        script {
          sh 'ec -no-color'
        }
      }
    }
  }
}
