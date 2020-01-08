pipeline {
    agent {
        docker {
            image 'maven:3.6.3-jdk-11' 
            args '-v germes-maven-repo:/root/.m2'
        }
    }
    stages {
        stage('Build') { 
            steps {
                sh 'mvn -e -B -DskipTests clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                 junit '**/target/surefire-reports/*.xml'
                }
            }
        }
    }
}