# All-Chat Mobile Test Automation Framework

Welcome to the **All-Chat Mobile Test Automation Framework**! This framework is designed to provide robust, maintainable, and scalable automated mobile UI testing for the All-Chat mobile application. It leverages Appium, Cucumber BDD, and TestNG, built with Java and Maven.

## 🌟 Key Features

*   **Behavior-Driven Development (BDD):** Uses Cucumber to write test scenarios in plain English (Gherkin syntax), bridging the gap between technical and non-technical stakeholders.
*   **Screen Object Model (SOM):** Implements the Screen Object Model design pattern to enhance test maintenance and reduce code duplication for mobile screens.
*   **Cross-Platform Testing:** Supports testing across different mobile platforms (Android/iOS) via dynamic parameters.
*   **Appium Integration:** Utilizes the Appium Java Client (v10) to interact with mobile devices and emulators natively.
*   **CI/CD Integration:** Ready for Jenkins integration with a provided `Jenkinsfile`, including automated Appium server startup, Android emulator launch, video recording of test runs, and Slack notifications.
*   **Detailed Reporting:** Generates comprehensive Cucumber HTML and JSON reports.

## 🏗️ Framework Structure

The framework is organized into the following directory structure:

```text
├── src/
│   ├── main/java/
│   │   ├── helpers/       # Helper classes for common Appium interactions (e.g., waits, gestures)
│   │   ├── screens/       # Screen Object Model classes representing mobile app screens
│   │   └── util/          # Utilities like DriverManager, Configuration readers, etc.
│   └── test/
│       ├── java/
│       │   ├── hooks/             # Cucumber hooks (e.g., @Before, @After) for setup and teardown
│       │   ├── runners/           # Cucumber TestNG runners to execute the feature files
│       │   └── stepdefinitions/   # Java code that implements the steps defined in the feature files
│       └── resources/
│           └── features/          # Cucumber .feature files containing Gherkin scenarios
├── suites/                # TestNG XML suite files for grouping and running tests
├── Jenkinsfile            # Jenkins pipeline configuration for automated execution
└── pom.xml                # Maven configuration and dependencies
```

## 🛠️ Prerequisites

Before you begin, ensure you have the following installed on your machine:

1.  **Java Development Kit (JDK):** Version 17 (as specified in `pom.xml`).
2.  **Maven:** Version 3.8+ for dependency management and test execution.
3.  **Git:** For version control.
4.  **Node.js & npm:** Required to install and run the Appium server.
5.  **Appium:** Install globally via `npm install -g appium` and install necessary drivers (e.g., `appium driver install uiautomator2`).
6.  **Android SDK / Android Studio:** For Android emulator setup and `adb` command line tools. Ensure `ANDROID_SDK_DIRECTORY` or `ANDROID_HOME` is configured in your environment.

## 🚀 Installation & Setup

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/SaifAlAbabseh/appium-mobile-automation-framework.git
    cd All-Chat-Mobile-Test-Automation
    ```

2.  **Environment Variables:**
    *   Create a `.env` file in the root directory if your tests require specific environment configurations (e.g., Appium server port, AVD name, credentials).
    *   *Example variables used in Jenkins: `ANDROID_SDK_DIRECTORY`, `AC_ANDROID_AVD`, `AC_ANDROID_SERVER_PORT`.*

3.  **Install Dependencies:**
    Run the following command to download all necessary Maven dependencies:
    ```bash
    mvn clean install -DskipTests
    ```

## 🏃‍♂️ Running the Tests

You can run the tests locally using Maven. The framework supports passing dynamic properties to control the execution environment.

### Ensure Appium and Emulator are Running
Before executing tests locally, make sure your Appium server is running:
```bash
appium --port 4723
```
And ensure your Android Emulator or physical device is connected and accessible via `adb devices`.

### Basic Run

To run the tests using the default settings and a specific TestNG suite file:

```bash
mvn clean test -DsuiteXmlFile=suites/all_chat_project/MainTestSuite.xml
```

### Passing Parameters

You can customize the test execution by passing system properties (as defined in your `Jenkinsfile` and `pom.xml`). Here are common properties:

*   `-Dplatform`: Specify the platform to run tests on (e.g., `Android`, `iOS`).

**Example:**

1.  **Run on Android:**
    ```bash
    mvn clean test -DsuiteXmlFile=suites/all_chat_project/MainTestSuite.xml -Dplatform=Android
    ```

## 📊 Viewing Reports

After the test execution finishes, Cucumber generates reports in the `target` directory:

*   **HTML Report:** Open `target/cucumber-report.html` in your web browser for a detailed, interactive view of the test results.
*   **JSON Report:** `target/cucumber-report.json` is generated for integration with CI/CD tools or custom reporting dashboards.
*   **Screenshots:** If any tests fail, screenshots are typically saved in `src/main/screenshots/`.
*   **Video Recordings:** Videos of test runs are saved in `src/main/recordings/` (e.g., `test.mp4`).

## 🔄 CI/CD (Jenkins)

The project includes a `Jenkinsfile` configured with stages for:
1.  Cleaning the workspace and checking out the code.
2.  Setting environment variables from a `.env` file (`/var/Env/Mobile/.env`).
3.  Starting the ADB server and launching the specified Android Emulator.
4.  Installing Appium and the `uiautomator2` driver via npm, and starting the Appium Server.
5.  Running Maven tests with dynamic platform parameters.
6.  Publishing artifacts (reports, screenshots, videos).
7.  Sending Slack notifications with test summaries and video/report uploads to a designated channel.
