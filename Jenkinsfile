pipeline {
    agent {
        kubernetes {
            yaml '''
            apiVersion: v1
            kind: Pod
            metadata: 
              name: jenkins-agent
            spec:
              containers:
              - name: node
                image: node:18.20.4-alpine3.20
                command:
                - cat
                tty: true
              - name: gradle
                image: gradle:8.10.0-jdk21
                command:
                - cat
                tty: true
              - name: docker
                image: docker:27.2.0-alpine3.20
                command:
                - cat
                tty: true
                volumeMounts:
                - mountPath: "/var/run/docker.sock"
                  name: docker-socket
              - name: kubectl
                image: gcr.io/cloud-builders/kubectl
                command:
                - cat
                tty: true
              volumes:
              - name: docker-socket
                hostPath:
                  path: "/var/run/docker.sock"
            '''
        }
    }

    environment {
        DISCORD_WEBHOOK = credentials('samdasu-discord-webhook')
        DOCKER_IMAGE_TAG = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Npm install & Build Frontend') {
            when {
                changeset "devopsFront/**"
            }
            steps {
                container('node') {
                    dir('devopsFront') {
                        sh 'npm install'
                        sh 'npm run build'
                    }
                }
            }
        }

        stage('Build Backend') {
            when {
                changeset "devopsBackend/**"
            }
            steps {
                container('gradle') {
                    dir('devopsBackend') {
                        sh 'chmod +x ./gradlew'
                        sh './gradlew clean build -x test'
                    }
                }
            }
        }

        stage('Docker Image Build Frontend') {
            when {
                changeset "devopsFront/**"
            }
            steps {
                container('docker') {
                    script {
                        sh 'docker logout'
                        withCredentials([usernamePassword(credentialsId: 'samdasu-dockerhub-access',
                            usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD' )]){
                            sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                        }

                        dir('devopsFront') {
                            withEnv(["DOCKER_IMAGE_TAG=${DOCKER_IMAGE_TAG}"]){
                                sh "docker build --no-cache -t uzz99/samdasu_repo:fourstit-front-$DOCKER_IMAGE_TAG ./"
                                sh "docker push uzz99/samdasu_repo:fourstit-front-$DOCKER_IMAGE_TAG"
                            }
                        }
                        sh 'docker logout'
                    }
                }
            }
        }

        stage('Docker Image Build Backend') {
            when {
                changeset "devopsBackend/**"
            }
            steps {
                container('docker') {
                    script {
                        sh 'docker logout'
                        withCredentials([usernamePassword(credentialsId: 'samdasu-dockerhub-access',
                            usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD' )]){
                            sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                        }

                        dir('devopsBackend') {
                            withEnv(["DOCKER_IMAGE_TAG=${DOCKER_IMAGE_TAG}"]){
                                sh "docker build --no-cache -t uzz99/samdasu_repo:fourstit-back-$DOCKER_IMAGE_TAG ./"
                                sh "docker push uzz99/samdasu_repo:fourstit-back-$DOCKER_IMAGE_TAG"
                            }
                        }
                        sh 'docker logout'
                    }
                }
            }
        }

        stage('Deployment Frontend') {
            when {
                changeset "devopsFront/**"
            }
            steps {
                container('kubectl') {
                    script {
                        sh "kubectl set image deploy fourstit-front-deploy nginx=uzz99/samdasu_repo:fourstit-front-$DOCKER_IMAGE_TAG -n default"
                    }
                }
            }
        }

        stage('Deployment Backend') {
            when {
                changeset "devopsBackend/**"
            }
            steps {
                container('kubectl') {
                    script {
                        sh "kubectl set image deploy fourstit-back-deploy fourstit-back=uzz99/samdasu_repo:fourstit-back-$DOCKER_IMAGE_TAG  -n default"
                    }
                }
            }
        }
    }

    post {
        success {
            withCredentials([string(credentialsId: 'samdasu-discord-webhook', variable: 'DISCORD')]) {
                discordSend description: """
                제목 : ${currentBuild.displayName}
                결과 : ${currentBuild.result}
                실행 시간 : ${currentBuild.duration / 1000}s
                """,
                result: currentBuild.currentResult,
                title: "${env.JOB_NAME} : ${currentBuild.displayName} 성공", 
                webhookURL: "${DISCORD}"
            }
        }

        failure {
            withCredentials([string(credentialsId: 'samdasu-discord-webhook', variable: 'DISCORD')]) {
                discordSend description: """
                제목 : ${currentBuild.displayName}
                결과 : ${currentBuild.result}
                실행 시간 : ${currentBuild.duration / 1000}s
                """,
                result: currentBuild.currentResult,
                title: "${env.JOB_NAME} : ${currentBuild.displayName} 실패", 
                webhookURL: "${DISCORD}"
            }
        }
    }
}
