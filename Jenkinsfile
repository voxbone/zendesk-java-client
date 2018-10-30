#!groovy
def repo = 'zendesk-java-client'
properties(
        [
                [
                        $class  : 'BuildDiscarderProperty',
                        strategy: [$class: 'LogRotator', numToKeepStr: '3']
                ],
                pipelineTriggers([pollSCM('H/5 * * * *')]),
                disableConcurrentBuilds(),
        ]
)

@NonCPS
def getProjectKey(repo) {
    return repo + '_' + env.BRANCH_NAME.replaceAll("/", "_")
}

node('regular') {
  stage("Initial clean-up") {
    deleteDir()
  }

  ws("workspace/${getProjectKey(repo)}")
  {
    try
    {
      stage("Checkout") {
        checkout scm
      }

      stage("Run UT - ${getProjectKey(repo)}") {
        withMaven(options: [artifactsPublisher(disabled: true), dependenciesFingerprintPublisher(disabled: true)], maven: 'maven') {
          sh "mvn -T 16C clean install -fae -U -q"
        }
      }
    }
    catch(e){
      throw e
    }
    finally {
      stage("Cleanup") {
        deleteDir()
      }
    }
  }
}