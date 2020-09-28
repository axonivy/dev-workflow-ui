pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '60', artifactNumToKeepStr: '1'))
  }

  triggers {
    cron '@midnight'
    bitbucketPush()
  }

  parameters {
    string(name: 'engineSource', defaultValue: 'https://jenkins.ivyteam.io/job/ivy-core_product/job/master/lastSuccessfulBuild/', description: 'Engine page url')
    booleanParam(name: 'deployScreenshots', defaultValue: false, description: 'Deploy new screenshots')
  }

  environment {
    imgSimilarity = 98
    dockerfileParams = '--shm-size 1g --hostname=ivy'
  }

  stages {
    stage('check') {
      agent {
        docker {
          image 'mstruebing/editorconfig-checker:2.1.0'
          reuseNode true
        }
      }
      steps {
        sh 'ec -no-color'
      }
    }
    stage('build') {
      agent {
        docker {
          image 'axonivy/build-container:web-1.0'
          reuseNode true
        }
      }
      steps {
        script {
          maven cmd: 'clean verify ' +
                '-Dmaven.test.failure.ignore=true ' +
                '-Dengine.page.url=' + params.engineSource

          checkVersions recordIssue: false
          checkVersions cmd: '-f maven/config/pom.xml'
          junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/surefire-reports/**/*.xml'
          junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/failsafe-reports/*.xml'
          archiveArtifacts '**/target/*.iar'
          archiveArtifacts '**/target/ivyEngine/logs/*'
          archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
        }
      }
    }
    stage('verify') {
      agent {
        docker {
          image 'maven:3.6.3-jdk-11'
          reuseNode true
        }
      }
      when {
        expression { return !params.deployScreenshots }
      }
      steps {
        script {
          maven cmd: 'clean verify ' +
                  '-f maven/image-validation/pom.xml ' +
                  '-Dmaven.test.failure.ignore=true ' +
                  '-Dimg.similarity=' + env.imgSimilarity

          archiveArtifacts '**/target/docu/**/*'
          archiveArtifacts '**/target/*.html'
          recordIssues filters: [includeType('screenshot-html-plugin:compare-images')], tools: [mavenConsole(name: 'Image')], unstableNewAll: 1,
          qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]]
        }
      }
    }
    stage('deploy') {
      agent {
        docker {
          image 'axonivy/build-container:web-1.0'
          reuseNode true
        }
      }
      when {
        allOf {
          branch 'master'
          expression { return currentBuild.currentResult == 'SUCCESS' || params.deployScreenshots }
        }
      }
      steps {
        script {
          maven cmd: 'deploy -Dmaven.test.skip=true'
        }
      }
    }
  }
}
