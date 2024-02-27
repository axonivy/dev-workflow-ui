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
                def phase = isReleaseOrMasterBranch() ? 'deploy' : 'verify';
                maven cmd: "clean ${phase} " +
                      "-Divy.engine.version.latest.minor=true " +
                      "-Dmaven.test.failure.ignore=true " +
                      "-Dengine.page.url=${params.engineSource} " +
                      "-Dref.screenshot.build='${getScreenshotRefBranch()}' " + 
                      "-Dtest.engine.url=http://${ivyName}:8080 " +
                      "-Dselenide.remote=http://${seleniumName}:4444/wd/hub "
              }
            }
            recordIssues tools: [mavenConsole()], qualityGates: [[threshold: 1, type: 'TOTAL']], filters: [
              excludeMessage('The system property test.engine.url is configured twice!*'),
              excludeMessage('Can not load credentials from settings.xml*'),
            ]
            junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/*-reports/**/*.xml'
            archiveArtifacts '**/target/*.iar'
            archiveArtifacts '**/target/dev-workflow-ui*.jar'
            archiveArtifacts '**/target/ivyEngine/logs/*'
            archiveArtifacts '**/target/docu/**/*'
            archiveArtifacts '**/target/*.html'
            archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
            currentBuild.description = "<a href='${BUILD_URL}artifact/dev-workflow-ui-web-test/target/screenshotsCompare.html'>&raquo; Screenshots</a><br>"
            recordIssues filters: [includeType('screenshot-html-plugin:compare-images')], tools: [mavenConsole(name: 'Image', id: 'image-warnings')], qualityGates: [[threshold: 1, type: 'TOTAL', unstable: true]]
          } finally {
            sh "docker network rm ${networkName}"
          }
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
                    "-Divy.engine.version.latest.minor=true " +
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

def getScreenshotRefBranch() {
  if (isReleaseOrMasterBranch() && currentBuild.previousSuccessfulBuild != null) {
    return env.BRANCH_NAME.replace('/', '%252F')
  }
  return 'master'
}
