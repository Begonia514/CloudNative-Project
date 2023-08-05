pipeline{
    agent none

    stages {
        stage('Clone Code') {
            agent {
                label 'master'
            }
            steps {
                echo "1. Starting to clone code from Github (may fail for several times ...)"
                // TODO: add your own username and password here
                sh 'curl "http://p.nju.edu.cn/portal_io/login?' +
                        'username=' + '' +
                        '&' +
                        'password=' + '' + '"'
                git url: 'https://gitee.com/coraxhome/CloudNative-Project.git', branch: 'main'
            }
        }
        stage('Build Code') {
            agent {
                docker {
                    image 'maven:latest'
                    args ' -v /root/.m2:/root/.m2'
                }
            }
            steps {
                echo "2. Clone code has finished, starting to build code with maven"
                sh 'mvn -B clean package'
            }
        }
        stage('Build Image') {
            agent {
                label 'master'
            }
            steps {
                echo "3. Build code has finished, starting to build image"
                sh 'docker build -t hello-server:${BUILD_ID} .'
                sh 'docker tag hello-server:${BUILD_ID} harbor.edu.cn/nju33/hello-server:${BUILD_ID}'
            }
        }
        stage('Push Image') {
            agent {
                label 'master'
            }
            steps {
                // TODO: add your own username and password here
                sh 'docker login harbor.edu.cn ' +
                        '-u ' + '' +
                        ' -p ' + ''

                sh 'docker push harbor.edu.cn/nju33/hello-server:${BUILD_ID}'
            }
        }
    }
}
node('slave'){
    container('jnlp-kubectl'){
        stage('Clone YAML'){
            echo "4. Git Clone YAML to Slave"
            // TODO: add your own username and password here
            sh 'curl "http://p.nju.edu.cn/portal_io/login?' +
                    'username=' + '' +
                    '&' +
                    'password=' + '' + '"'
            git url: 'https://gitee.com/coraxhome/CloudNative-Project.git', branch: 'main'
            sh 'sed -i "s#{VERSION}#${BUILD_ID}#g" hello-deployment.yaml'
        }
        stage('Deploy'){
            echo "5. Build image has finished, starting to deploy"
            // TODO: add your own username and password here
            sh 'docker login harbor.edu.cn ' +
                    '-u ' + '' +
                    ' -p ' + ''
            sh 'docker pull harbor.edu.cn/nju33/hello-server:${BUILD_ID}'
            sh "kubectl apply -f hello-deployment.yaml -n nju33"
        }
        stage('Monitor') {
            echo "6. Deploy has finished, starting to monitor"
            sh "kubectl apply -f hello-monitor.yaml -n monitoring"
        }
    }
}