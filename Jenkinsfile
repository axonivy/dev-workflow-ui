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
    string(name: 'deployToEngineUrl', defaultValue: 'https://nightly.demo.ivyteam.io', description: 'Deploy to engine (e.g. see: https://demo.ivyteam.io)')
  }

  environment {
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
          def random = (new Random()).nextInt(10000000)
          def networkName = "build-" + random
          def seleniumName = "selenium-" + random
          def ivyName = "ivy-" + random
          sh "docker network create ${networkName}"
          try {
            docker.image("selenium/standalone-firefox:4").withRun("-e START_XVFB=false --shm-size=2g --name ${seleniumName} --network ${networkName}") {
              docker.build('maven').inside("--name ${ivyName} --network ${networkName}") {
                maven cmd: 'clean verify ' +
                      "-Divy.engine.version.latest.minor=true " +
                      "-Dmaven.test.failure.ignore=true " +
                      "-Dengine.page.url=${params.engineSource} " +
                      "-Dtest.engine.url=http://${ivyName}:8080 " +
                      "-Dselenide.remote=http://${seleniumName}:4444/wd/hub "
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
            archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
            currentBuild.description = "<a href='${BUILD_URL}artifact/dev-workflow-ui-web-test/target/screenshotsCompare.html'>&raquo; Screenshots</a><br>"
          } finally {
            sh "docker network rm ${networkName}"
          }
        }
      }
    }

    stage('verify screenshots') {
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
                  '-Dimg.similarity=92'

          archiveArtifacts '**/target/docu/**/*'
          archiveArtifacts '**/target/*.html'
          testFailsCount = (env.BRANCH_NAME == 'master') ? 1 : 2
          recordIssues filters: [includeType('screenshot-html-plugin:compare-images')], tools: [mavenConsole(name: 'Image', id: 'image-warnings')], unstableNewAll: testFailsCount,
          qualityGates: [[threshold: testFailsCount, type: 'TOTAL', unstable: true]]
        }
      }
    }

    stage('verify screenshots manually') {
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

    stage('deploy maven') {
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

    stage('deploy to engine') {
      agent {
        dockerfile {
          reuseNode true
        }
      }
      when {
        expression { !params.deployToEngineUrl.isEmpty()}
      }
      steps {
        script {
          def deployToEngineUrl = params.deployToEngineUrl
          def deployApplicationName = ("dev-workflow-ui_" + env.BRANCH_NAME.replaceAll("%2F","_").replaceAll("/","_").replaceAll("\\.","_")).take(40)

          catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
            maven cmd: 'install -P deploy-to-engine ' +
                    "-DskipDeployToEngine=false " +
                    "-DdeployToEngineUrl=${deployToEngineUrl} " +
                    "-DdeployApplicationName=${deployApplicationName} " +
                    "-Dmaven.test.skip=true -Dmaven.deploy.skip=true "
            currentBuild.description += "<a href='${deployToEngineUrl}/${deployApplicationName}/faces/view/dev-workflow-ui/home.xhtml'>&raquo; Demo</a>"
          }
        }
      }
    }
  }
}

def isReleaseOrMasterBranch() {
  return env.BRANCH_NAME == 'master' || env.BRANCH_NAME.startsWith('release/')
}
