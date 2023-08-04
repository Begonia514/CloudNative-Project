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
                // TODO: add your own username and password here
                sh 'curl "http://p.nju.edu.cn/portal_io/login?' +
                        'username=' +
                        '&' +
                        'password="'
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
                sh 'mvn compile'
                echo "2.2 Build code has finished, starting to run unit tests"
                sh 'mvn test'
                echo "2.3 Unit tests have finished, starting to package code"
                sh 'mvn clean package'
            }
        }
        stage('Build Image') {
            agent {
                label 'master'
            }
            steps {
                echo "3. Build code has finished, starting to build image"
                sh 'docker build -t hello-server:v1 .'
                sh 'docker logout harbor.edu.cn'
                // TODO: add your own username and password here
                sh 'docker login harbor.edu.cn ' +
                        '-u ' +
                        '-p '
                sh 'docker tag hello-server:v1 harbor.edu.cn/nju33/hello-server:v1'
                sh 'docker push harbor.edu.cn/nju33/hello-server:v1'
            }
        }
    }
}
node('slave'){
    container('jnlp-kubectl'){
        stage('Clone YAML'){
            echo "4. Git Clone YAML to Slave"
            sh 'curl "http://p.nju.edu.cn/portal_io/logout"'
            // TODO: add your own username and password here
            sh 'curl "http://p.nju.edu.cn/portal_io/login?' +
                    'username=' +
                    '&' +
                    'password="'
            git url: 'https://gitee.com/coraxhome/CloudNative-Project.git', branch: 'main'
        }
        stage('Deploy'){
            echo "5. Build image has finished, starting to deploy"
            sh "kubectl apply -f hello-deployment.yaml -n nju33"
            sh "kubectl apply -f hello-service.yaml -n nju33"
        }
    }
}