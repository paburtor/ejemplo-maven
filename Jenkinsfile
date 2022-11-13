pipeline {
    agent any
    stages {
        stage('SonarQube analysis'){
            steps{
                echo 'SonarQube analysis ${GIT_BRANCH}'
                withSonarQubeEnv() { sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.7.0.2747:sonar'}
            }
            post {
                success {
                    echo 'Build Success'
                }
                failure {
                    echo 'Build Failed'
                }
            }
        }
        stage('Build'){
            steps{
                echo 'Building [${GIT_BRANCH}]'
                sh './mvnw clean compile -e'
            }
            post {
                success {
                    echo 'Build Success'
                }
                failure {
                    echo 'Build Failed'
                }
            }
        }
        stage('Test'){
            steps{
                echo 'Testing [${GIT_BRANCH}]'
                sh './mvnw test -e'
            }
            post {
                success {
                    echo 'Test Success'
                }
                failure {
                    echo 'Test Failed'
                }
            }
        }
        stage('Package'){
            steps{
                echo 'Packaging [${GIT_BRANCH}]'
                sh './mvnw package -e'
            }
            post {
                success {
                    echo 'Package Success'
                }
                failure {
                    echo 'Package Failed'
                }
            }
        }
        stage('Run'){
            steps{
                echo 'Running [${GIT_BRANCH}]'
                sh '#./mvnw spring-boot:run -e'
            }
            post {
                success {
                    echo 'Run Success'
                    cleanWs()                 
                }
                failure {
                    echo 'Run Failed'
                }
            }
        }
    }
}
