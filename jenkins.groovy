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
                        'username=211250245' +
                        '&' +
                        'password=Archettoforever1"'
                git url: 'https://gitee.com/coraxhome/CloudNative-Project.git', branch: 'main'
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
                script {
                    def version = "1.0.${env.BUILD_NUMBER}"
                    sh "docker build -t my-image:${version} CloudNative-Project"
                }
            }
        }
    }
}
node('slave'){
    container('jnlp-kubectl'){
        stage('Clone YAML'){
            echo "4. Git Clone YAML to Slave"
            sh 'curl "http://p.nju.edu.cn/portal_io/logout"'
            sh 'curl "http://p.nju.edu.cn/portal_io/login?' +
                    'username=211250245' +
                    '&' +
                    'password=Archettoforever1"'
            git url: 'https://gitee.com/coraxhome/CloudNative-Project.git', branch: 'main'
        }
        stage('Deploy'){
            echo "5. Build image has finished, starting to deploy"
            sh "kubectl apply -f CloudNative-Project/hello-deployment.yaml -n nju33"
            sh "kubectl apply -f CloudNative-Project/hello-service.yaml -n nju33"
        }
    }
}