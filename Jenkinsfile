def manualDeploy

pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '30'))
  }

  triggers {
    cron 'H 6 * * *'
  }

  parameters {
    string(name: 'engineSource', defaultValue: 'https://product.ivyteam.io', description: 'Engine page url')
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
      steps {
        script {
          def deployApplicationName = env.BRANCH_NAME.replaceAll("%2F","_").replaceAll("/","_").replaceAll("\\.","_")

          def random = (new Random()).nextInt(10000000)
          def networkName = "build-" + random
          def seleniumName = "selenium-" + random
          def ivyName = "ivy-" + random
          sh "docker network create ${networkName}"
          try {
            docker.image("selenium/standalone-chrome:4").withRun("-e START_XVFB=false -e SE_OPTS=\"--log-level ALL\" --shm-size=2g --name ${seleniumName} --network ${networkName}") {
              docker.build('maven').inside("--name ${ivyName} --network ${networkName}") {
                maven cmd: 'clean verify ' +
                      "-Divy.engine.version='[10.0.0,]' " +
                      '-Dmaven.test.failure.ignore=true ' +
                      "-DdeployApplicationName=dev-workflow-ui-${deployApplicationName} " +
                      "-Dengine.page.url=${params.engineSource} " +
                      "-Dtest.engine.url=http://${ivyName}:8080 " +
                      "-Dselenide.remote=http://${seleniumName}:4444/wd/hub " +
                      "-Dselenide.browser=chrome " +
                      "-Divy.selenide.browser=chrome"
              }
            }
            recordIssues tools: [mavenConsole()], unstableTotalAll: 1, filters: [
              excludeMessage('The system property test.engine.url is configured twice!*'),
              excludeMessage('Can not load credentials from settings.xml*'),
            ]
            junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/*-reports/**/*.xml'
            archiveArtifacts '**/target/*.iar'
            archiveArtifacts '**/target/dev-workflow-ui*.jar'
            archiveArtifacts '**/target/ivyEngine/logs/*'
            archiveArtifacts artifacts: '**/webdriver*.log', allowEmptyArchive: true
            archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
            currentBuild.description = "<a href='${BUILD_URL}artifact/dev-workflow-ui-web-test/target/screenshotsCompare.html'>&raquo; Screenshots</a><br>" +
                                      "<a href='https://nightly.demo.ivyteam.io/dev-workflow-ui-${deployApplicationName}/faces/view/dev-workflow-ui/home.xhtml'>&raquo; Demo</a>"
          } finally {
            sh "docker network rm ${networkName}"
          }
        }
      }
    }

    stage('verify') {
      agent {
        dockerfile {
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
          testFailsCount = (env.BRANCH_NAME == 'master') ? 1 : 2
          recordIssues filters: [includeType('screenshot-html-plugin:compare-images')], tools: [mavenConsole(name: 'Image', id: 'image-warnings')], unstableNewAll: testFailsCount,
          qualityGates: [[threshold: testFailsCount, type: 'TOTAL', unstable: true]]
        }
      }
    }

    stage('verify-manually') {
      when {
        expression { isReleaseOrMasterBranch() }
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
        dockerfile {
          reuseNode true
        }
      }
      when {
        expression { isReleaseOrMasterBranch() }
      }
      steps {
        script {
          maven cmd: 'deploy -Dmaven.test.skip=true'
        }
      }
    }
  }
}

def isReleaseOrMasterBranch() {
  return env.BRANCH_NAME == 'master' || env.BRANCH_NAME.startsWith('release/')
}
