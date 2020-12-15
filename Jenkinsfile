//this is the scripted method with groovy engine
//https://www.youtube.com/watch?v=7KCS70sCoK0&feature=emb_rel_pause

//3 places of tests skipping command -Dmaven.test.skip=true
//it's necessary to see if a jenkins variable is usefull so to maybe allows conditionnal running and deploying test contexts
//currentBuild.result = hudson.model.Result.SUCCESS.toString()
//currentBuild.result = hudson.model.Result.NOT_BUILT.toString()
//currentBuild.result = hudson.model.Result.UNSTABLE.toString()
//currentBuild.result = hudson.model.Result.FAILURE.toString()
//currentBuild.result = hudson.model.Result.ABORTED.toString()
import hudson.model.Result

node {

    // Variables globales
    def maventool
    def rtMaven
    def buildInfo
    def server
    def ENV
    def executeTests

    // Configuration du job Jenkins
    // On garde les 5 derniers builds par branche
    // On scanne les branches et les tags du Git
    properties([
            buildDiscarder(
                    logRotator(
                            artifactDaysToKeepStr: '',
                            artifactNumToKeepStr: '',
                            daysToKeepStr: '',
                            numToKeepStr: '5')
            ),
            parameters([
                    gitParameter(
                            branch: '',
                            branchFilter: 'origin/(.*)',
                            defaultValue: 'main',
                            description: 'Sélectionner la branche ou le tag à déployer',
                            name: 'BRANCH_TAG',
                            quickFilterEnabled: false,
                            selectedValue: 'NONE',
                            sortMode: 'DESCENDING_SMART',
                            tagFilter: '*',
                            type: 'PT_BRANCH_TAG'),
                    choice(choices: ['DEV', 'TEST', 'PROD'], description: 'Sélectionner l\'environnement cible', name: 'ENV'),
                    booleanParam(defaultValue: false, description: 'Voulez-vous exécuter les tests ?', name: 'executeTests')
            ])
    ])

    stage('Set environnement variables') {
        try {
            env.JAVA_HOME = "${tool 'Open JDK 11'}"
            env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

            maventool = tool 'Maven 3.3.9'
            rtMaven = Artifactory.newMavenBuild()
            server = Artifactory.server '-1137809952@1458918089773'
            rtMaven.tool = 'Maven 3.3.9'
            rtMaven.opts = '-Xms1024m -Xmx4096m'

            if (params.BRANCH_TAG == null) {
                throw new Exception("Variable BRANCH_TAG is null")
            } else {
                echo "Branch to deploy =  ${params.BRANCH_TAG}"
            }

            if (params.ENV == null) {
                throw new Exception("Variable ENV is null")
            } else {
                ENV = params.ENV
                echo "Target environnement =  ${ENV}"
                echo ENV
            }

            if (params.executeTests == null) {
                executeTests = false
            } else {
                executeTests = params.executeTests
            }

            echo "executeTests =  ${executeTests}"

        } catch (e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(e.getLocalizedMessage())
            throw e
        }
    }

    notifySlack("Started")

    stage('SCM checkout') {
        try {
            checkout([
                    $class                           : 'GitSCM',
                    branches                         : [[name: "${params.BRANCH_TAG}"]],
                    doGenerateSubmoduleConfigurations: false,
                    extensions                       : [],
                    submoduleCfg                     : [],
                    userRemoteConfigs                : [[credentialsId: '', url: 'https://github.com/abes-esr/abes-hello-back.git']]
            ])

        } catch (e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(e.getLocalizedMessage())
            throw e
        }
    }

    if ("${executeTests}" == 'true') {
        stage('test') {
            try {

                rtMaven.run pom: 'pom.xml', goals: 'clean test'
                junit allowEmptyResults: true, testResults: '/target/surefire-reports/*.xml'

            } catch (e) {
                currentBuild.result = hudson.model.Result.UNSTABLE.toString()
                notifySlack(e.getLocalizedMessage())
                // Si les tests ne passent pas, on mets le build en UNSTABLE et on continue
                //throw e
            }
        }
    } else {
        echo "Tests are skipped"
    }

    stage('compile-package') {
        try {
            sh 'cd '
            if (ENV == 'DEV') {
                echo 'compile for dev profile'
                sh "'${maventool}/bin/mvn' -Dmaven.test.skip=true clean package -Pdev"
            }

            if (ENV == 'TEST') {
                echo 'compile for test profile'
                sh "'${maventool}/bin/mvn' -Dmaven.test.skip=true clean package -Ptest"
            }

            if (ENV == 'PROD') {
                echo 'compile for prod profile'
                sh "'${maventool}/bin/mvn' -Dmaven.test.skip=true clean package -Pprod"
            }

        } catch(e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(e.getLocalizedMessage())
            throw e
        }
    }

    //stage('sonarqube analysis'){
    //   withSonarQubeEnv('SonarQube Server2'){ cf : jenkins/configuration/sonarQube servers ==> between the quotes put the name we gave to the server
    //      sh "${maventool}/bin/mvn sonar:sonar"
    //  }
    // }

    stage('artifact') {
        try {
            //we have to put the war in the workspace/target directory (see on the server Jacinthe)
            //the path is /var/lib/jenkins/jobs/indexationsolr_test_multibranch_pipeline/branches/develop/workspace/target/indexationsolr.war
            archive 'web/target/*.war'

        } catch(e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(e.getLocalizedMessage())
            throw e
        }
    }

    stage ('deploy to tomcat'){
        try {

            if (ENV == 'DEV') {
                //here we have the choice : we can create the credential in jenkins/configuration/ssh servers
                //or in the space project (so the credential can only be accessed by the project)
                //or in jenkins/identifiants/system/identifiants globaux (so the credential can be accessed by all the projects)

                echo 'deployment on cirse1-dev'
                sshagent(credentials: ['cirse1-dev-ssh-key']) { //one key per tomcat
                    sh 'scp web/target/*.war tomcat@cirse1-dev.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
                }

                echo 'deployment on cirse2-dev'
                sshagent(credentials: ['cirse2-dev-ssh-key']) {
                    sh 'scp web/target/*.war tomcat@cirse2-dev.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
                }
            }
            if (ENV == 'TEST') {
                echo 'deployment on cirse1-test'
                sshagent(credentials: ['cirse1-test-ssh-key']) {
                    sh 'scp web/target/*.war tomcat@cirse1-test.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
                }

                echo 'deployment on cirse2-test'
                sshagent(credentials: ['cirse2-test-ssh-key']) {
                    sh 'scp web/target/*.war tomcat@cirse2-test.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
                }
            }
            if (ENV == 'PROD') {
                echo 'deployment on cirse1-prod'
                sshagent(credentials: ['cirse1-prod-ssh-key']) {
                    sh 'scp web/target/*.war tomcat@cirse1.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
                }

                echo 'deployment on cirse2-prod'
                sshagent(credentials: ['cirse2-prod-ssh-key']) {
                    sh 'scp web/target/*.war tomcat@cirse2.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
                }
            }

        } catch(e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(e.getLocalizedMessage())
            throw e
        }
    }

    stage ('restart tomcat'){

        try {

            if (ENV == 'DEV') {
                echo 'restart tomcat on cirse1-dev'
                sshagent(credentials: ['cirse1-dev-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {

                        try {
                            echo 'get status cirse1 dev (should be running)'
                            sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                        } catch(e) {
                            // Maybe the tomcat is not running
                            echo 'cirse1 dev is not running'

                            echo 'we try to start cirse1 dev'
                            sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'

                            echo 'get status cirse1 dev'
                            sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                        }

                        echo 'stop cirse1 dev'
                        sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'
                        //here, I wanted to get the status but since the status returned is code=exited, status=143 (normal because tomcat has been stopped)
                        //groovy is understanding that the script returned an error 143 and then stop the process
                        //so apart checking the logs, I don't so which manner allows us to get the stopped tomcat status
                        //echo 'get status 2 (should not be running)'
                        //sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-indexationSolr.service"'
                        echo 'start cirse1 dev'
                        sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'

                        echo 'finally we get status cirse1 dev (should be running)'
                        sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    }
                }

                echo 'restart tomcat on cirse2-dev'
                sshagent(credentials: ['cirse2-dev-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {
                        try {
                            echo 'beginning : get status cirse2 dev (should be running)'
                            sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                        } catch(e) {
                            // Maybe the tomcat is not running
                            echo 'cirse2 dev is not running'

                            echo 'we try to start cirse2 dev'
                            sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'

                            echo 'get status cirse2 dev'
                            sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'

                        }

                        echo 'stop cirse2 dev'
                        sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'

                        echo 'start cirse2 dev'
                        sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'

                        echo 'finally we get status cirse2 dev (should be running)'
                        sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    }
                }
            }

            if (ENV == 'TEST') {
                echo 'restart tomcat on cirse1-test'
                sshagent(credentials: ['cirse1-test-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {
                        echo 'beginning : get status cirse1 test (should be running)'
                        sh 'ssh -tt tomcat@cirse1-test.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                        echo 'stop cirse1 test'
                        sh 'ssh -tt tomcat@cirse1-test.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'
                        echo 'start cirse1 test'
                        sh 'ssh -tt tomcat@cirse1-test.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'
                        echo 'then : get status cirse1 test (should be running)'
                        sh 'ssh -tt tomcat@cirse1-test.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    }
                }

                echo 'restart tomcat on cirse2-test'
                sshagent(credentials: ['cirse2-test-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {
                        echo 'beginning : get status cirse2 test (should be running)'
                        sh 'ssh -tt tomcat@cirse2-test.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                        echo 'stop cirse2 test'
                        sh 'ssh -tt tomcat@cirse2-test.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'
                        echo 'start cirse2 test'
                        sh 'ssh -tt tomcat@cirse2-test.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'
                        echo 'then : get status cirse2 test (should be running)'
                        sh 'ssh -tt tomcat@cirse2-test.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    }
                }
            }

            if (ENV == 'PROD') {
                echo 'restart tomcat on cirse1-prod'
                sshagent(credentials: ['cirse1-prod-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {
                        echo 'beginning : get status cirse1 prod (should be running)'
                        sh 'ssh -tt tomcat@cirse1-prod.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                        echo 'stop cirse1 prod'
                        sh 'ssh -tt tomcat@cirse1-prod.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'
                        echo 'start cirse1 prod'
                        sh 'ssh -tt tomcat@cirse1-prod.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'
                        echo 'then : get status cirse1 prod (should be running)'
                        sh 'ssh -tt tomcat@cirse1-prod.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    }
                }

                echo 'restart tomcat on cirse2-prod'
                sshagent(credentials: ['cirse2-prod-ssh-key']) {
                    withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {
                        echo 'beginning : get status cirse2 prod (should be running)'
                        sh 'ssh -tt tomcat@cirse2-prod.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                        echo 'stop cirse2 prod'
                        sh 'ssh -tt tomcat@cirse2-prod.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'
                        echo 'start cirse2 prod'
                        sh 'ssh -tt tomcat@cirse2-prod.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'
                        echo 'then : get status cirse2 prod (should be running)'
                        sh 'ssh -tt tomcat@cirse2-prod.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    }
                }
            }

        } catch(e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(e.getLocalizedMessage())
            throw e
        }
    }

    stage ('Artifactory configuration') {
        try {
            //server = Artifactory.server '-1137809952@1458918089773'
            // create an Artifactory Maven Build instance
            //rtMaven = Artifactory.newMavenBuild()
            //where the Maven build should download its dependencies from ==> we must have the artifactory repo the good version of springframework, the one indicated in the pom
            //if it's not the same version of dependency, it throws an error
            //rtMaven.resolver server: server, releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot'
            //where our build artifacts should be deployed to
            rtMaven.deployer server: server, releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local'
            //to deploy only some artifacts
            //rtMaven.deployer.artifactDeploymentPatterns.addInclude("frog*")
            //exclude artifacts from being deployed
            //rtMaven.deployer.artifactDeploymentPatterns.addExclude("*.zip")
            //combine both methods
            //rtMaven.deployer.artifactDeploymentPatterns.addInclude("frog*").addExclude("*.zip")
            //add custom properties to the deployed artifacts
            //rtMaven.deployer.addProperty("status", "in-qa").addProperty("compatibility", "1", "2", "3")
            //default = 3 ; so to modify the number of threads
            //rtMaven.deployer.threads = 6
            //to disable artifacts deployment set to false
            //rtMaven.deployer.deployArtifacts = false
            //define a Maven Tool through Jenkins Manage

            //the pom is at the root of the workspace (see on the server Jacinthe)
            //the path is /var/lib/jenkins/jobs/indexationsolr_test_multibranch_pipeline/branches/develop/workspace/pom.xml
            buildInfo = Artifactory.newBuildInfo()
            buildInfo = rtMaven.run pom: 'pom.xml', goals: '-U clean install -Dmaven.test.skip=true '

            //'clean install -Dartifactory.publish.artifacts=false -Dartifactory.publish.buildInfo=false'
            //and with jenkins environnement variables :
            //clean install -Dartifactory.publish.artifacts=$PUBLISH_ARTIFACTS -Dartifactory.publish.buildInfo=$PUBLISH_BUILDINFO

            //rtMaven.run pom: 'pom.xml', goals: 'clean install -Dmaven.test.skip=true', buildInfo: buildInfo
            rtMaven.deployer.deployArtifacts buildInfo
            buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install -Dmaven.repo.local=.m2 -Dmaven.test.skip=true'
            buildInfo.env.capture = true
            server.publishBuildInfo buildInfo

        } catch(e) {
            currentBuild.result = hudson.model.Result.FAILURE.toString()
            notifySlack(e.getLocalizedMessage())
            throw e
        }
    }

    currentBuild.result = hudson.model.Result.SUCCESS.toString()
    notifySlack("Congratulation !")
}

def notifySlack(String info = '' ) {
    def colorCode = '#848484' // Gray

    switch (currentBuild.result) {
        case 'NOT_BUILT':
            colorCode = '#36C5F0' // Blue
            break
        case 'SUCCESS':
            colorCode = '#00FF00' // Green
            break
        case 'UNSTABLE':
            colorCode = '#FFFF00' // Yellow
            break
        case 'FAILURE':
            colorCode = '#FF0000' // Red
            break;
    }

    String message = """
        *Jenkins Build*
        Job name: `${env.JOB_NAME}`
        Build number: `#${env.BUILD_NUMBER}`
        Build status: `${currentBuild.result}`
        Branch or tag: `${params.BRANCH_TAG}`
        Target environment: `${params.ENV}`
        Informations: `${info}`
        Build details: <${env.BUILD_URL}/console|See in web console>
    """.stripIndent()

    return slackSend(tokenCredentialId: "slack_token",
            channel: "#notif-helloabes",
            color: colorCode,
            message: message)
}
