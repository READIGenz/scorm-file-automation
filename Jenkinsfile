pipeline {
    agent any

    environment {
        TAG_NAME = "@Test"
    }

    triggers {
        cron('0 14 * * *')  // Runs daily at 2PM
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                git branch: 'main', url: 'https://github.com/READIGenz/web-playwright-new'
            }
        }

        stage('Build Project') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Run Tests') {
            steps {
                bat "test_on_jenkins.bat ${env.TAG_NAME}"
            }
        }

        stage('Publish Report') {
            steps {
                script {
                    def latestReportDir = bat(
                        script: '''
                        @echo off
                        setlocal enabledelayedexpansion
                        set "latest="

                        for /f "delims=" %%f in ('dir /b /ad /o-d Reports\\genZ_\\genZ_* 2^>nul') do (
                            set "latest=%%f"
                            goto :found
                        )
                        :found
                        echo !latest!
                        ''',
                        returnStdout: true
                    ).trim()

                    if (latestReportDir) {
                        echo "Publishing report from: Reports/genZ_/${latestReportDir}"

                        publishHTML([
                            allowMissing: false,
                            alwaysLinkToLastBuild: true,
                            keepAll: true,
                            reportDir: "Reports/genZ_/${latestReportDir}",
                            reportFiles: "Overview*.html",
                            reportName: "Automation Test Report"
                        ])
                    } else {
                        echo "No report folder found to publish."
                    }
                }
            }
        }
    }
}
