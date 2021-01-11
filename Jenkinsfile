def manualDeploy

pipeline {
  agent any

  options {
    buildDiscarder(logRotator(numToKeepStr: '30'))
  }

  triggers {
    cron 'H 6 * * *'
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
      steps {
        script {
          def deployApplicationName = env.BRANCH_NAME.replaceAll("%2F","_").replaceAll("/","_")

          def random = (new Random()).nextInt(10000000)
          def networkName = "build-" + random
          def seleniumName = "selenium-" + random
          def ivyName = "ivy-" + random
          sh "docker network create ${networkName}"
          try {
            docker.image("selenium/standalone-firefox:3").withRun("-e START_XVFB=false --shm-size=2g --name ${seleniumName} --network ${networkName}") {
              docker.build('maven').inside("--name ${ivyName} --network ${networkName}") {
                maven cmd: 'clean verify ' +
                      '-Dmaven.test.failure.ignore=true ' +
                      "-DdeployApplicationName=workflow-ui-${deployApplicationName} " +
                      "-Dengine.page.url=${params.engineSource} " +
                      "-Dtest.engine.url=http://${ivyName}:8080 " +
                      "-Dselenide.remote=http://${seleniumName}:4444/wd/hub "

                checkVersions recordIssue: false
                checkVersions cmd: '-f maven/config/pom.xml'
              }
            }

            junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/surefire-reports/**/*.xml'
            junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/failsafe-reports/*.xml'
            archiveArtifacts '**/target/*.iar'
            archiveArtifacts '**/target/ivyEngine/logs/*'
            archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
            currentBuild.description = "<a href='${BUILD_URL}artifact/workflow-ui-web-test/target/screenshotsCompare.html'>&raquo; Screenshots</a><br>" +
                                      "<a href='https://nightly.demo.ivyteam.io/workflow-ui-${deployApplicationName}/faces/view/workflow-ui/home.xhtml'>&raquo; Demo</a>"
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
          recordIssues filters: [includeType('screenshot-html-plugin:compare-images')], tools: [mavenConsole(name: 'Image')], unstableNewAll: testFailsCount,
          qualityGates: [[threshold: testFailsCount, type: 'TOTAL', unstable: true]]
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
        dockerfile {
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
