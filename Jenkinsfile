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

def shouldPublish() {
    def snapshot = readMavenPom().version.contains('-SNAPSHOT')
    if (env.BRANCH_NAME == 'master' && !snapshot)
    {
        return true
    }
    else if (env.BRANCH_NAME != 'master' && snapshot)
    {
        return true
    }
    return false
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
      stage("Run UT/IT - ${getProjectKey(repo)}") {
        withMaven(options: [artifactsPublisher(disabled: true), dependenciesFingerprintPublisher(disabled: true)], maven: 'maven') {
          sh "mvn -T 16C clean install -fae -U -q"
        }
      }
      stage("Publish to nexus - ${getProjectKey(repo)}") {

        if (shouldPublish())
        {
            withMaven(options: [artifactsPublisher(disabled: true), dependenciesFingerprintPublisher(disabled: true)], maven: 'maven') {
                sh "mvn clean deploy -DskipTests -fae -U -q -DnexusUrl=${NEXUS_URL}"
            }
        }
        else
        {
            sh "echo 'Branch ${env.BRANCH_NAME} cannot publish this version of the artifact (only master can publish release version / other branches snapshots)'"
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