node {
    def responseStatus = ''
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
        stage('Test'){
            echo "Testing"
            sh './mvnw test -e'
        }
        slackSend color: "good", message: "Test Success"
        stage('Package'){
            echo "Packaging"
            sh './mvnw package -e'
        }
        slackSend color: "good", message: "Packaging Success"
        stage('Sonar'){
            echo 'Sonar...'
            withSonarQubeEnv('sonar-public') { // If you have configured more than one global server connection, you can specify its name
                sh './mvnw clean package sonar:sonar'
            }
        }
        slackSend color: "good", message: "Sonar Success"
        stage('Upload Nexus'){
            echo 'Upload Nexus...'
            sh './mvnw clean install -e'
            nexusPublisher nexusInstanceId: 'nexusserverid', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
        }
        slackSend color: "good", message: "Upload Nexus Success"
        stage('Download Nexus Artefact'){
            echo 'Download Nexus Artefact...'
            sh 'curl -X GET -u admin:ff3ec427-7756-4053-b132-5733db6f9edc1234jUyreqw http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
        }
        slackSend color: "good", message: "Download Nexus Artefact Success"
        stage('Run Nexus Artefact'){
            echo 'Run Nexus Artefact...'
            sh 'nohup java -jar ./DevOpsUsach2020-0.0.1.jar &'
        }
        slackSend color: "good", message: "Run Nexus Artefact Success"
        stage('Test Artefact'){
            echo 'Test Artefact...'
            sh 'curl -I GET http://localhost:8081/rest/mscovid/test?msg=testing > response.txt'
            responseStatus = sh(script: 'cat response.txt | grep HTTP/1.1 | cut -d " " -f2', returnStdout: true).trim()
        }
    } catch (e) {
        slackSend color: "danger", message: "Failure. Error : " + e 
        throw e
    } finally {
        if(responseStatus != ''){
            slackSend color: "danger", message: "Test Status Response : " + responseStatus
        }
    }
    if(responseStatus == '200'){
        try {
            stage('Upload jar to Nexus'){
                echo 'Upload jar to Nexus...'
                nexusPublisher nexusInstanceId: 'nexusserverid', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
            }
            slackSend color: "good", message: "Upload jar to Nexus Success"
        }catch (e) {
            slackSend color: "danger", message: "Upload jar to Nexus Failure. Error : " + e 
            throw e
        } finally { }
    } else {
        slackSend color: "danger", message: "Failure upload release artefact, Status Response : " + responseStatus
        throw e
    }
}
