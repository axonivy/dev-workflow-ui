pipeline {
  agent none
  stages {
    stage('check') {
      agent {
        docker { image 'mstruebing/editorconfig-checker:2.1.0' }
      }
      steps {
        sh 'ec -no-color'
      }
    }
    stage('build') {
      agent {
        docker { image 'axonivy/build-container:web-1.0' }
      }
      steps {
        script {
          maven cmd: 'clean verify ' +
                '-Dmaven.test.failure.ignore=true ' +
                '-Dengine.page.url=' + params.engineSource

          checkVersions recordIssue: false
          checkVersions cmd: '-f maven-config/pom.xml'
          junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/surefire-reports/**/*.xml'
          archiveArtifacts '**/target/*.iar'
          archiveArtifacts '**/target/ivyEngine/logs/*'
          archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
        }
      }
    }
  }

  options {
    buildDiscarder(logRotator(numToKeepStr: '60', artifactNumToKeepStr: '1'))
  }

  triggers {
    cron '@midnight'
    pollSCM('H/10 * * * *')
  }

  parameters {
    string(name: 'engineSource', defaultValue: 'https://jenkins.ivyteam.io/job/ivy-core_product/job/master/lastSuccessfulBuild/', description: 'Engine page url')
  }
}
