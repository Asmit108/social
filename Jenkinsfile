pipeline {
    agent any
    environment {
        SPRING_DATASOURCE_USERNAME = credentials('db-username')
        SPRING_DATASOURCE_PASSWORD = credentials('db-password')
        JWT_SECRET = credentials('jwt-secret')
        SPRING_SSL_KEY_STORE_PASSWORD = credentials('keystore-password')
        SPRING_SSL_KEY_STORE_FILE = credentials('KeyStore')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }
        stage('Deploy to Production') {
            steps {
                withCredentials([
                        sshUserPrivateKey(
                                credentialsId: 'social-ec2-ssh-key',
                                keyFileVariable: 'SSH_KEY'
                        ),
                        file(
                                credentialsId: 'KeyStore',
                                variable: 'SPRING_SSL_KEY_STORE_FILE'
                        ),
                        string(
                                credentialsId: 'jwt-secret',
                                variable: 'JWT_SECRET'
                        ),
                        string(
                                credentialsId: 'keystore-password',
                                variable: 'SPRING_SSL_KEY_STORE_PASSWORD'
                        ),
                        string(
                                credentialsId: 'db-username',
                                variable: 'SPRING_DATASOURCE_USERNAME'
                        ),
                        string(
                                credentialsId: 'db-password',
                                variable: 'SPRING_DATASOURCE_PASSWORD'
                        )
                ]) {
                    writeFile file: '.env', text: """
                    SPRING_DATASOURCE_URL=${env.SPRING_DATASOURCE_URL}
                    SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
                    SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
                    JWT_SECRET=${JWT_SECRET}
                    SPRING_SSL_KEY_STORE_PASSWORD=${SPRING_SSL_KEY_STORE_PASSWORD}
                    SPRING_SSL_KEY_STORE_FILE=/keystore.p12
                    """
                    bat '''
                    scp -i "%SSH_KEY%" -o StrictHostKeyChecking=no target/social-0.0.1-SNAPSHOT.jar ubuntu@3.108.215.224:~/
                
                    scp -i "%SSH_KEY%" -o StrictHostKeyChecking=no .env ubuntu@3.108.215.224:~/social/.env
                    
                    scp -i "%SSH_KEY%" -o StrictHostKeyChecking=no "%SPRING_SSL_KEY_STORE_FILE%" ubuntu@3.108.215.224:~/social/keystore.p12
                
                    ssh -i "%SSH_KEY%" -o StrictHostKeyChecking=no ubuntu@3.108.215.224 "cd ~/social && mkdir -p target && mv -f ~/social-0.0.1-SNAPSHOT.jar target/ && git pull && docker compose down && docker compose up -d --build"
                    '''
                }
            }
        }
    }
}