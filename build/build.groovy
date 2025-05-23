def buildIntegration(def mvnArgs = '') {
  withCredentials([string(credentialsId: 'github.ivy-team.token', variable: 'GITHUB_TOKEN')]) {
    def random = (new Random()).nextInt(10000000)
    def networkName = "build-" + random
    def seleniumName = "selenium-" + random
    def ivyName = "ivy-" + random
    def dbName = "db-" + random
    sh "docker network create ${networkName}"
    try {
      docker.image("selenium/standalone-firefox:4").withRun("-e START_XVFB=false --shm-size=2g --name ${seleniumName} --network ${networkName} --shm-size 1g --hostname=ivy") {
        docker.build('maven', '-f build/Dockerfile .').inside("--name ${ivyName} --network ${networkName}") {
          def mvnBuildArgs = "-Dwdm.gitHubTokenName=ivy-team " +
              "-Dwdm.gitHubTokenSecret=${env.GITHUB_TOKEN} " +
              "-Dtest.engine.url=http://${ivyName}:8080 " +
              "-Dselenide.remote=http://${seleniumName}:4444/wd/hub " +
              mvnArgs
          mvnBuild(mvnBuildArgs);

          archiveArtifacts artifacts: '**/ivyEngine/logs/ivy.log', allowEmptyArchive: true
          archiveArtifacts artifacts: '**/target/selenide/reports/**/*', allowEmptyArchive: true
        }
      }
    } finally {
      sh "docker network rm ${networkName}"
    }
  }
}

def build() {
  docker.build('maven', '-f build/Dockerfile .').inside("") {
    mvnBuild('-Pcreate-deploy-zip');
  }
}

def mvnBuild(def mvnArgs = '') {
  def phase = isReleasingBranch() ? 'deploy' : 'verify'
  maven cmd: "clean ${phase} -ntp -Divy.engine.version.latest.minor=true -Dmaven.test.skip=false -Dengine.page.url=${params.engineSource} " + mvnArgs
  
  recordIssues tools: [eclipse()], qualityGates: [[threshold: 1, type: 'TOTAL']]
  recordIssues tools: [mavenConsole()], qualityGates: [[threshold: 1, type: 'TOTAL']], filters: [
    excludeMessage('The system property test.engine.url is configured twice!*'),
    excludeMessage('JAR will be empty*')
  ]
  junit testDataPublishers: [[$class: 'AttachmentPublisher'], [$class: 'StabilityTestDataPublisher']], testResults: '**/target/*-reports/**/*.xml'
}

return this
