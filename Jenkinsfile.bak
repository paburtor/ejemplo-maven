node {
    def responseStatus = ''        
    stage('INFO'){
        echo 'Iniciando....'
        //slackSend color: "warning", message: "INFO: Prueba Taller 3 - Modulo 4 Branch: " + env.BRANCH_NAME
         // slackSend color: "good", message: "Info Success. hash commit : " + e
    }
    
    try 
    {
        stage('Build')
        {
            echo '(Pre) Building..........'
            sh 'mvn clean compile -e'
            echo '(Post) Building'
        }
        
        //slackSend color: "good", message: "Build Success"
        stage('Test')
        {
            echo "(Pre) Testing"
            sh 'mvn test -e'
            echo "(Post) Testing"
        }
        //slackSend color: "good", message: "Test Success"
        stage('Package')
        {
            echo "(Pre) Packaging"
            sh 'mvn package -e'
            echo "(Post) Packaging"
        }
        
        //slackSend color: "good", message: "Packaging Success"
        stage('Sonar')
        {
            echo '(Pre) Sonar'
            withSonarQubeEnv('MySonarQubeServer') { // If you have configured more than one global server connection, you can specify its name
                sh 'mvn clean package sonar:sonar'
            }
            echo '(Post) Sonar'
        }
        
        //slackSend color: "good", message: "Sonar Success"
        stage('Upload Nexus')
        {
            echo '(Pre) Upload Nexus'
            sh 'mvn clean install -e'
            //nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.danilovidal', packaging: 'jar', version: '0.0.1']]]
             nexusPublisher nexusInstanceId: 'nexusserverid', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.pedroaburto', packaging: 'jar', version: '0.0.1']]]
            // nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.oscarreyes', packaging: 'jar', version: '0.0.1']]]
            // nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.jesusandrade', packaging: 'jar', version: '0.0.1']]]
            // nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './build/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.angelopimentel', packaging: 'jar', version: '0.0.1']]]
            echo '(Post) Upload Nexus'
        }
        
        //slackSend color: "good", message: "Upload Nexus Success"
        stage('Download Nexus Artefact')
        {
            echo '(Pre) Download Nexus Artefact'
            //sh 'curl -X GET -u admin:1qazxsw2 https://nexus.danilovidalm.com/repository/devops-usach-nexus/com/danilovidal/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
             sh 'curl -X GET -u admin:ff3ec427-7756-4053-b132-5733db6f9edc1234jUyreqw http://nexus:8081/repository/devops-usach-nexus/com/pedroaburto/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            // sh 'curl -X GET -u admin:1qazxsw2 https://nexus.danilovidalm.com/repository/devops-usach-nexus/com/oscarreyes/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            // sh 'curl -X GET -u admin:1qazxsw2 https://nexus.danilovidalm.com/repository/devops-usach-nexus/com/jesusandrade/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            // sh 'curl -X GET -u admin:1qazxsw2 https://nexus.danilovidalm.com/repository/devops-usach-nexus/com/angelopimentel/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O'
            echo '(Post) Download Nexus Artefact'
        }
        //slackSend color: "good", message: "Download Nexus Artefact Success"
        
        stage('Run Nexus Artefact')
        {
            echo '(Pre) Run Nexus Artefact'
            sh 'nohup java -jar ./DevOpsUsach2020-0.0.1.jar &'
            echo '(Post) Run Nexus Artefact'           
        }
        //slackSend color: "good", message: "Run Nexus Artefact Success"
        
        stage('Test Artefact')
        {
            echo '(Pre) Test Artefact'
            sh 'curl -I GET http://localhost:8081/rest/mscovid/test?msg=testing > response.txt'
            responseStatus = sh(script: 'cat response.txt | grep HTTP/1.1 | cut -d " " -f2', returnStdout: true).trim()
            echo '(Post) Test Artifact'
        }
    } 
    catch (e) 
    {
        echo "Failure. Error : " + e
        //slackSend color: "danger", message: "Failure. Error : " + e 
        throw e
    } 
    finally 
    {
        if(responseStatus != '')
        {
            echo 'Test Status Response : ' + responseStatus
            //slackSend color: "danger", message: "Test Status Response : " + responseStatus
        }
    }
    
    if(responseStatus == '200')
    {
        try 
        {
            stage('Upload jar to Nexus')
            {
                echo '(Pre) Upload jar to Nexus'
                //nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.danilovidal', packaging: 'jar', version: '1.0.0']]]
                nexusPublisher nexusInstanceId: 'nexusserverid', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.pedroaburto', packaging: 'jar', version: '1.0.0']]]
                // nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.oscarreyes', packaging: 'jar', version: '1.0.0']]]
                 //nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.jesusandrade', packaging: 'jar', version: '1.0.0']]]
                // nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.angelopimentel', packaging: 'jar', version: '1.0.0']]]
                // nexusPublisher nexusInstanceId: 'nexus01', nexusRepositoryId: 'devops-usach-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: '', filePath: './DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '1.0.0']]]
                echo '(Post) Upload jar to Nexus'
            }            
            //slackSend color: "good", message: "Upload jar to Nexus Success"
        }
        catch (e) 
        {
            echo 'Upload jar to Nexus Failure. Error : ' + e 
            //slackSend color: "danger", message: "Upload jar to Nexus Failure. Error : " + e 
            throw e
        } 
        finally { }
    }
    else 
    {
        echo 'Failure upload release artefact, Status Response : ' + responseStatus
        //slackSend color: "danger", message: "Failure upload release artefact, Status Response : " + responseStatus
        throw e
    }
}
