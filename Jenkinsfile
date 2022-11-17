node {
    stage('INFO'){
        // echo env
        slackSend color: "warning", message: "INFO: Prueba Taller 3 - Modulo 4 Branch: " + env.BRANCH_NAME
        // slackSend color: "good", message: "Info Success. hash commit : " + e
    }
    try {
        stage('Build'){
            echo "Building"
            sh './mvnw clean compile -e'
        }
        slackSend color: "good", message: "Build Success"
    } catch (e) {
        slackSend color: "danger", message: "Build Failure. Error : " + e 
        throw e
    } finally {}
    try {
        stage('Test'){
            echo "Testing"
            sh './mvnw test -e'
        }
        slackSend color: "good", message: "Test Success"
    } catch (e) {
        slackSend color: "danger", message: "Test Failure. Error : " + e 
        throw e
    } finally {}
    try {
        stage('Package'){
            echo "Packaging"
            sh './mvnw package -e'
        }
        slackSend color: "good", message: "Packaging Success"
    } catch (e) {
        slackSend color: "danger", message: "Packaging Failure. Error : " + e 
        throw e
    } finally { }
    try {
        stage('Sonar'){
            echo 'Sonar...'
            withSonarQubeEnv('sonar-public') { // If you have configured more than one global server connection, you can specify its name
                sh './mvnw clean package sonar:sonar'
            }
        }
        slackSend color: "good", message: "Sonar Success"
    }catch (e) {
        slackSend color: "danger", message: "Sonar Failure. Error : " + e 
        throw e
    } finally { }
    try {
        stage('Upload Nexus'){
            echo 'Upload Nexus...'
            sh './mvnw clean install -e'
            nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
        slackSend color: "good", message: "Upload Nexus Success"
    }catch (e) {
        slackSend color: "danger", message: "Upload Nexus Failure. Error : " + e 
        throw e
    } finally { }
    try {
        stage('Download Nexus Artefact'){
            echo 'Download Nexus Artefact...'
            sh 'curl -X GET -u admin:1qazxsw2 https://nexus.danilovidalm.com/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
        }
        slackSend color: "good", message: "Download Nexus Artefact Success"
    }catch (e) {
        slackSend color: "danger", message: "Download Nexus Artefact Failure. Error : " + e 
        throw e
    } finally { }
    try {
        stage('Run Nexus Artefact'){
            echo 'Run Nexus Artefact...'
            sh 'nohup java -jar ./DevOpsUsach2020-0.0.1.jar &'
        }
        slackSend color: "good", message: "Run Nexus Artefact Success"
    }catch (e) {
        slackSend color: "danger", message: "Run Nexus Artefact Failure. Error : " + e 
        throw e
    } finally { }
    def responseStatus = ''
    try {
        stage('Test Artefact'){
            echo 'Test Artefact...'
            sh 'curl -I GET http://localhost:8081/rest/mscovid/test?msg=testing > response.txt'
            responseStatus = sh(script: 'cat response.txt | grep HTTP/1.1 | cut -d " " -f2', returnStdout: true).trim()
        }
    } catch (e) {
        slackSend color: "danger", message: "Test Artefact Failure. Error : " + e 
        throw e
    } finally { }
    if(responseStatus == '200'){
        try {
            stage('Upload jar to Nexus'){
                echo 'Upload jar to Nexus...'
                nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
            }
            slackSend color: "good", message: "Upload jar to Nexus Success"
        }catch (e) {
            slackSend color: "danger", message: "Upload jar to Nexus Failure. Error : " + e 
            throw e
        } finally { }
    } else {
        slackSend color: "danger", message: "Status Response : " + responseStatus
        throw e
    }
}
