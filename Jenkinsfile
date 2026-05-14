import groovy.json.JsonSlurper

pipeline {
    agent any

    environment {
        SLACK_CHANNEL = '#automation-jobs'
        SLACK_CHANNEL_ID = 'C05R1CXD2Q4'
    }

    tools {
        maven 'Maven 3.9.11'
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                git branch: 'main',
                        url: 'https://github.com/SaifAlAbabseh/All-Chat-Mobile-Test-Automation'
            }
        }

        stage('Set Env') {
            steps {
                script {
                    // Read the .env file as UTF-8
                    def lines = readFile(file: '/var/Env/Mobile/.env', encoding: 'UTF-8').readLines()
                    lines.each { line ->
                        line = line.trim()
                        if (!line || line.startsWith("#")) return  // skip empty lines and comments
                        def (key, value) = line.split("=", 2)
                        env."$key" = value
                    }
                }
            }
        }

        stage("Execute ADB Server") {
            steps {
                echo "Executing ADB Server"
                script {
                    try {
                        sh """
                            #!/bin/bash
                            
                            cd ${env.ANDROID_SDK_DIRECTORY}/platform-tools
                            ./adb start-server&
                        """
                    } catch (Exception e) {
                        echo "The ADB Server is not running"
                        echo e.toString()
                    }
                }
            }
        }

        stage("Launch Android Emulator") {
            steps {
                echo "Starting Emulator"
                script {
                    try {
                        sh """
                            #!/bin/bash
                            
                            
                           ${env.ANDROID_SDK_DIRECTORY}/cmdline-tools/latest/bin/sdkmanager "system-images;android-33;google_apis;x86_64"
                                            ${env.ANDROID_SDK_DIRECTORY}/cmdline-tools/latest/bin/avdmanager create avd \
                                            -n ${env.AC_ANDROID_AVD} \
                                            -k "system-images;android-33;google_apis;x86_64" \
                                            --force \
                                            -d "pixel"
                            
                            ${env.ANDROID_SDK_DIRECTORY}/emulator/emulator -avd ${env.AC_ANDROID_AVD} -no-window -no-snapshot-load
                            sleep 5s
                        """
                    } catch (Exception e) {
                        echo "The emulator is not open"
                        echo e.toString()
                    }
                }
            }
        }

        stage('Install Appium & UIAutomator2') {
            steps {
                sh '''
                #!/bin/bash
                npm init -y
                npm install appium
                npx appium driver install uiautomator2
                '''
            }
        }

        stage('Start Appium Server') {
            steps {
                sh """
                npx appium --port ${env.AC_ANDROID_SERVER_PORT} &
                """
            }
        }

        stage('Start Maven Tests') {
            steps {
                sh '''
                #!/bin/bash
                mvn clean test -DsuiteXmlFile=suites/all_chat_project/MainTestSuite.xml -Dplatform=${platform}
                '''
            }
        }
    }

    post {
        always {

            archiveArtifacts artifacts: 'src/main/recordings/test.mp4, src/main/screenshots/*.png, target/cucumber-report.html, target/cucumber-report.json', allowEmptyArchive: true
            script {
                def cucumberJsonReport = readFile('target/cucumber-report.json')
                def jsonRaw = new JsonSlurper().parseText(cucumberJsonReport)
                def scenarios = jsonRaw.collectMany { feature ->
                    feature.elements.findAll { it.type == 'scenario' }
                }
                def TOTAL_TESTS = scenarios.size()
                def PASSED_TESTS = scenarios.count { s -> s.steps.every { it.result.status == 'passed' } }
                def FAILED_TESTS = scenarios.count { s -> s.steps.any { it.result.status == 'failed' } }
                def SKIPPED_TESTS = scenarios.count { s -> s.steps.any { it.result.status == 'skipped' } }
                def IGNORED_TESTS = scenarios.count { s -> s.steps.any { it.result.status == 'ignored' } }

                def isSuccess = (currentBuild.result == null || currentBuild.result == 'SUCCESS')
                def failedScreenshots = isSuccess ? "" : "* 📸 Screenshots: <${env.BUILD_URL}artifact/src/main/screenshots|Click here>\n"

                def jobStatusOverall = isSuccess ? '✅  PASSED JOB ✅' : '❌ FAILED JOB ❌'
                def platformTestedOn = params.platform.toUpperCase()
                def slackMessage = """
************************************************************
                        ${jobStatusOverall}
************************************************************
* 💼 Job: ${env.JOB_NAME}
* 🔨 Build #: ${env.BUILD_NUMBER}
* 🔨 Build Link: <${env.BUILD_URL}|Click here>
************************************************************
                        📊 Total Tests = ${TOTAL_TESTS}
************************************************************
* ✅ PASSED: ${PASSED_TESTS}
* ❌ FAILED: ${FAILED_TESTS}
* ⏩ SKIPPED: ${SKIPPED_TESTS}
* ⏩ IGNORED: ${IGNORED_TESTS}
************************************************************
* ⚙️ System: 📱 Mobile
* ⚙️ Platform: ${platformTestedOn}
${failedScreenshots}
* ⬇️⬇️ 📹 Test Video Recording  & 📋 Test Report can be found below ⬇️⬇️
"""

                cucumberJsonReport = null
                jsonRaw = null
                scenarios = null
                def testVideoRecordingPath = "recordings/test.mp4"
                def testReportPath = "target/cucumber-report.html"
                def resultColor = isSuccess ? "good" : "danger"

                // Send Slack message
                slackSend(
                        channel:"${env.SLACK_CHANNEL}",
                        color: resultColor,
                        message: slackMessage
                )

                // Send Slack test video file
                slackUploadFile(
                        channel: "${env.SLACK_CHANNEL_ID}",
                        filePath: "${testVideoRecordingPath}, ${testReportPath}"
                )
            }
        }
    }
}