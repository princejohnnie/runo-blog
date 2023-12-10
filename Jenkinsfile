@Library('jenkins-build-helpers') _
setupEnvironment(['business_unit': 'corp'])

def createTestingEnvironment() {
    return setupContainers([
        [
            'name': 'main',
            // WARNING: rename this to follow $username-testing-image
            'image': 'ike-docker-local.artifactory.internetbrands.com/corp/levelup-academy:juzodinma-testing-image',
            'imagePullPolicy': 'Always',
            'env': [
                ['name': 'DB_HOST',     'value': 'localhost'],
                ['name': 'PGPASSWORD',  'value': 'password'],
                ['name': 'DB_DATABASE', 'value': 'levelup'],
                ['name': 'DB_USERNAME', 'value': 'levelup'],
                ['name': 'DB_PASSWORD', 'value': 'password'],
                ['name': 'LOG_CHANNEL', 'value': 'single'],
                ['name': 'LOG_LEVEL',   'value': 'debug'],
            ],
        ],[
           'name': 'pgsql',
           'image': 'postgres:14',
           'env': [
                ['name': 'PGPASSWORD',        'value': 'password'],
                ['name': 'POSTGRES_DB',       'value': 'levelup'],
                ['name': 'POSTGRES_USER',     'value': 'levelup'],
                ['name': 'POSTGRES_PASSWORD', 'value': 'password'],
          ]
        ]
   ])
}

pipeline {
    agent none

    options {
        gitLabConnection('IB Gitlab')
    }

    stages {
        stage('Build pipeline testing image') {
            agent {
                kubernetes {
                    yaml dockerContainerImageBuildAndPushPodManifest()
                }
            }
            steps {
                container('builder') {
                    dockerContainerImageBuildAndPush([
                        'docker_repo_host': 'ike-docker-local.artifactory.internetbrands.com',
                        'docker_repo_credential_id': 'artifactory-ike',
                        'dockerfile': './ci/Dockerfile',
                        'docker_image_name': 'levelup-academy',
                        'docker_image_tag': 'juzodinma-testing-image' // WARNING: rename this to follow $username-testing-image
                    ])
                }
            }
        }

        stage('Check MR Branch name and Author Email') {
            when {
                expression { env.CHANGE_ID != null }

            }
            agent {
                kubernetes {
                    yaml createTestingEnvironment()
                }
            }
            steps {
                script {
                    checkMRBranchName()
                }
            }
        }

        stage('Run backend tests') {
            agent {
                kubernetes {
                    yaml createTestingEnvironment()
                }
            }
            post {
                success {
                    updateGitlabCommitStatus name: 'backend-tests', state: 'success'
                }
                failure {
                    updateGitlabCommitStatus name: 'backend-tests', state: 'failed'
                }
            }
            steps {
                container('main') {
                    sh './ci/do-checks.sh'
                }
            }
        }
    }

}

def checkMRBranchName() {
    echo 'Hello World!'
    echo "Branch $BRANCH_NAME"

    echo "CHANGE_AUTHOR ${env.CHANGE_AUTHOR}"
    echo "CHANGE_TARGET ${env.CHANGE_TARGET}"
   
    return {
        print("MR Branch Name -> ${BRANCH_NAME}")
        print("MR Author Email -> ${CHANGE_AUTHOR}")
    }
}