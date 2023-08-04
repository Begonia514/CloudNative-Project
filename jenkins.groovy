pipeline{
    agent none
    stages {
        stage('Clone Code') {
            agent {
                label 'master'
            }
            steps {
                echo "1. Starting to clone code from Github (may fail for several times ...)"
                sh 'curl "http://p.nju.edu.cn/portal_io/logout"'
                sh 'curl "http://p.nju.edu.cn/portal_io/login?' +
                        'username=' +
                        '&' +
                        'password="'
                sh 'if [ -d CloudNative-Project ]; then rm -rf CloudNative-Project; fi'
                sh 'git clone https://github.com/Begonia514/CloudNative-Project.git'
            }
        }
        stage('Build Code') {
            agent {
                docker {
                    image 'maven:latest'
                    args ' -v /home/nju33:/home/nju33'
                }
            }
            steps {
                sh 'sh version.sh > version.txt'
                def JENKINS_BUILD_VERSION = readFile('version.txt').trim()
                sh 'cat version.txt'

                echo "2.1 Clone code has finished, starting to build code with maven"
                sh 'mvn -f CloudNative-Project/pom.xml compile'
                echo "2.2 Build code has finished, starting to run unit tests"
                sh 'mvn -f CloudNative-Project/pom.xml test'
                echo "2.3 Unit tests have finished, starting to package code"
                sh 'mvn -f CloudNative-Project/pom.xml clean package'
            }
        }
        stage('Build Image') {
            agent {
                label 'master'
            }
            steps {
                echo "3. Build code has finished, starting to build image"
                sh 'sh version.sh > version.txt'
                def JENKINS_BUILD_VERSION = readFile('version.txt').trim()
                sh "docker build -t hello-server:${JENKINS_BUILD_VERSION} ."
            }
        }
    }
}
node('slave'){
    container('jnlp-kubectl'){
        stage('Deploy'){
            echo "4. Build image has finished, starting to deploy"
            sh "kubectl apply -f hello-deployment.yaml -n nju33"
            sh "kubectl apply -f hello-service.yaml -n nju33"
        }
    }
}