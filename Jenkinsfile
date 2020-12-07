def manualDeploy

pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '30'))
  }

  triggers {
    cron('H 4 * * *')
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
          def deployApplicationName = env.BRANCH_NAME.replaceAll("%2F","_")
          deployApplicationName = deployApplicationName.replaceAll("/","_")
          maven cmd: 'clean verify ' +
                '-Dmaven.test.failure.ignore=true ' +
                '-DdeployApplicationName=workflow-ui-' + deployApplicationName + ' ' +
                '-Dengine.page.url=' + params.engineSource

          checkVersions recordIssue: false
          checkVersions cmd: '-f maven/config/pom.xml'
          junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/surefire-reports/**/*.xml'
          junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/failsafe-reports/*.xml'
          archiveArtifacts '**/target/*.iar'
          archiveArtifacts '**/target/ivyEngine/logs/*'
          archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
          currentBuild.description = "<a href='${BUILD_URL}artifact/workflow-ui-web-test/target/screenshotsCompare.html'>&raquo; Screenshots</a><br>" +
                                     "<a href='https://nightly.demo.ivyteam.io/workflow-ui-${deployApplicationName}/faces/view/workflow-ui/home.xhtml'>&raquo; Demo</a>"
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
          isMaster = (env.BRANCH_NAME == 'master')
          recordIssues filters: [includeType('screenshot-html-plugin:compare-images')], tools: [mavenConsole(name: 'Image')], unstableNewAll: 1,
          qualityGates: [[threshold: 1, type: 'TOTAL', unstable: !isMaster]]
        }
      }
    }
    stage('verify-manually') {
      when {
        branch 'master'
        not {
          triggeredBy 'TimerTrigger'
        }
      }
      steps {
        script{
          if (currentBuild.currentResult == 'FAILURE') {
            timeout(time: 10, unit: 'MINUTES')
            {
              manualDeploy = input(
                message: 'Screenshot comparison failed. Please check them manually.', parameters: [
                [$class: 'BooleanParameterDefinition', defaultValue: false, description: '', name: 'Deploy screenshots?']])
            }
          }
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
          expression { return currentBuild.currentResult == 'SUCCESS' || params.deployScreenshots || manualDeploy }
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
