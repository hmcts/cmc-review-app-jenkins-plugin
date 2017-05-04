#!groovy
properties(
  [[$class: 'GithubProjectProperty', projectUrlStr: 'http://git.reform.hmcts.net/cmc/review-app-jenkins-plugin/'],
   pipelineTriggers([[$class: 'GitHubPushTrigger']])]
)

node {
  try {
    stage('Checkout') {
      deleteDir()
      checkout scm
    }

    stage('Build') {
      sh "./mvnw compile"
    }

    stage('Lint') {
      sh "./mvnw checkstyle:check"
    }

    stage('Test (Unit)') {
      sh "./mvnw test"
    }

    stage('Package (JAR)') {
      sh "./mvnw package -DskipTests"
    }
  } catch (err) {
    slackSend(
      channel: '#cmc-tech-notification',
      color: 'danger',
      message: "${env.JOB_NAME}:  <${env.BUILD_URL}console|Build ${env.BUILD_DISPLAY_NAME}> has FAILED")
    throw err
  }
}
