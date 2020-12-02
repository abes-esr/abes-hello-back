//this is the scripted method with groovy engine
//https://www.youtube.com/watch?v=7KCS70sCoK0&feature=emb_rel_pause

//3 places of tests skipping command -Dmaven.test.skip=true
//it's necessary to see if a jenkins variable is usefull so to maybe allows conditionnal running and deploying test contexts

node {
    // 1. On charge les variables d'environnement (Java, Maven,...)
    stage ("Loading environement variables") {

        try {
            env.JAVA_HOME = "${tool 'Open JDK 11'}"
            env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"

            def maventool = tool 'Maven 3.3.9'
            //def serverArtifactory = Artifactory.newServer url: 'https://artifactory-test.abes.fr', credentialsId: 'artifactorykey'
            def rtMaven = Artifactory.newMavenBuild()
            def buildInfo
            def server = Artifactory.server '-1137809952@1458918089773'
            rtMaven.tool = 'Maven 3.3.9'
            rtMaven.opts = '-Xms1024m -Xmx4096m'

            //rtMaven
            def ENV = params.ENV
            def executeTests = params.executeTests
            if (params.ENV != null) {
                echo "env =  ${ENV}"
                echo ENV
            }
            if (params.executeTests != null) {
                echo "executeTests =  ${executeTests}"
            }

        } catch (err) {
            throw err
        }
    }

    stage('SCM checkout') {
        checkout([$class: 'GitSCM',
                  branches: [[name: '*/master'],
                             [name: '*/develop']],
                  doGenerateSubmoduleConfigurations: false,
                  extensions: [],
                  submoduleCfg: [],
                  userRemoteConfigs: [[credentialsId: 'Github',
                                       url: 'https://github.com/abes-esr/abes-hello-back.git']]
        ])
    }

    // 2. On configure les paramètres d'utilisation
    stage ("Setting parameters") {
        try {
            sh (script: 'git tag -l > tags_list.txt',returnStdout: true)
            def tags = readFile('tags_list.txt').trim()
            echo "tags = ${tags}"
            sh 'rm -f tags_list.txt'

            properties(
                    [parameters([
                            choice(choices: ['RELEASE', 'LATEST', '0.0.1-SNAPSHOT'], description: '', name: 'maven-repository-artifact'),
                            choice(choices: ['DEV', 'TEST', 'PROD'], description: '', name: 'ENV'),
                            choice(choices: ['CURRENT', ${tags}], description: '', name: 'VERSION'),
                            booleanParam(defaultValue: false, description: '', name: 'executeTests')
                    ])])

        }
        catch (err) {
            throw err
        }

    }

    //if the checkbox (params.executeTests) is checked
    stage ('test') {
        if("${executeTests}" == 'true'){
            rtMaven.run pom: 'pom.xml', goals: 'clean test'
            junit allowEmptyResults: true, testResults: '/target/surefire-reports/*.xml'
        }
        else{
            echo "tests = false"
        }
    }


    stage('compile-package') {
        sh 'cd '
        //sh "'${maventool}/bin/mvn' -Dmaven.test.failure.ignore clean package"
        sh "'${maventool}/bin/mvn' -Dmaven.test.skip=true clean package"
    }



    //stage('sonarqube analysis'){
    //   withSonarQubeEnv('SonarQube Server2'){ cf : jenkins/configuration/sonarQube servers ==> between the quotes put the name we gave to the server
    //      sh "${maventool}/bin/mvn sonar:sonar"
    //  }
    // }


    stage('artifact') {
        //we have to put the war in the workspace/target directory (see on the server Jacinthe)
        //the path is /var/lib/jenkins/jobs/indexationsolr_test_multibranch_pipeline/branches/develop/workspace/target/indexationsolr.war
        archive 'web/target/*.war'
    }



    stage ('deploy to tomcat'){
        echo 'deployment started'

        if (ENV == 'DEV') {
            //here we have the choice : we can create the credential in jenkins/configuration/ssh servers
            //or in the space project (so the credential can only be accessed by the project)
            //or in jenkins/identifiants/system/identifiants globaux (so the credential can be accessed by all the projects)

            //here it's a project credential
            sshagent(credentials: ['cirse1-dev-ssh-key']) { //one key per tomcat
                echo 'key ok'
                sh 'scp web/target/*.war tomcat@cirse1-dev.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
            }
            //here it's a global projects credential
            sshagent(credentials: ['cirse2-dev-ssh-key']) {
                sh 'scp web/target/*.war tomcat@cirse2-dev.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
            }
        }
        if (ENV == 'TEST') {
            sshagent(credentials: ['cirse1-test-ssh-key']) {//so need to generate new keys (not already done)
                sh 'scp web/target/*.war tomcat@cirse1-test.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
            }
            sshagent(credentials: ['cirse2-test-ssh-key']) {//so need to generate new keys (not already done)
                sh 'scp web/target/*.war tomcat@cirse2-test.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
            }
        }
        if (ENV == 'PROD') {
            sshagent(credentials: ['cirse1-prod-ssh-key']) {//so need to generate new keys (not already done)
                sh 'scp web/target/*.war tomcat@cirse1.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
            }
            sshagent(credentials: ['cirse2-prod-ssh-key']) {//so need to generate new keys (not already done)
                sh 'scp web/target/*.war tomcat@cirse2.v3.abes.fr:/usr/local/tomcat9-abes-hello/webapps/'
            }
        }
    }



    stage ('restart tomcat'){
        echo 'restart tomcat started'

        if (ENV == 'DEV') {
            sshagent(credentials: ['cirse1-dev-ssh-key']) {
                withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {
                    //echo 'test1'
                    //sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-indexationSolr.service && sudo systemctl start tomcat9-indexationSolr.service && sudo systemctl status tomcat9-indexationSolr.service"'
                    //echo 'test1 fini'  ==> works fine

                    echo 'beginning : get status cirse1 dev (should be running)'
                    sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    echo 'stop cirse1 dev'
                    sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'
                    //here, I wanted to get the status but since the status returned is code=exited, status=143 (normal because tomcat has been stopped)
                    //groovy is understanding that the script returned an error 143 and then stop the process
                    //so apart checking the logs, I don't so which manner allows us to get the stopped tomcat status
                    //echo 'get status 2 (should not be running)'
                    //sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-indexationSolr.service"'
                    echo 'start cirse1 dev'
                    sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'
                    echo 'then : get status cirse1 dev (should be running)'
                    sh 'ssh -tt tomcat@cirse1-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                }
            }
            sshagent(credentials: ['cirse2-dev-ssh-key']) {
                withCredentials([usernamePassword(credentialsId: 'tomcatuser', passwordVariable: 'pass', usernameVariable: 'username')]) {
                    echo 'beginning : get status cirse2 dev (should be running)'
                    sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                    echo 'stop cirse2 dev'
                    sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl stop tomcat9-abes-hello.service"'
                    echo 'start cirse2 dev'
                    sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && sudo systemctl start tomcat9-abes-hello.service"'
                    echo 'then : get status cirse2 dev (should be running)'
                    sh 'ssh -tt tomcat@cirse2-dev.v3.abes.fr  "cd /usr/local/ && systemctl status tomcat9-abes-hello.service"'
                }
            }
        }

        if (ENV == 'TEST') {
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
    }

    stage ('Artifactory configuration') {
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

        rtMaven.run pom: 'pom.xml', goals: 'clean install', buildInfo: buildInfo
        rtMaven.deployer.deployArtifacts buildInfo
        buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install -Dmaven.repo.local=.m2 -Dmaven.test.skip=true'
        buildInfo.env.capture = true
        server.publishBuildInfo buildInfo
    }


}
