# scorm-file-automation

This is a Proof-of-Concept project for automating the testing of SCORM packages using **Java**, **Playwright**, and **BDD (Cucumber)**. The framework is designed to interact with SCORM content inside iframes, auto-play lessons, handle video interactions, and verify question flows using dynamic test data.

### Running the Tests
#### Update Test Data
Edit testdata/mcq-answers.json to configure answers for the SCORM module under test.

Run Tests
mvn clean test

### Reports
After execution, two reports will be generated:

reports/execution-report.html – BDD test execution report (e.g., Cucumber HTML)

reports/scorm-api-report.json – Captures SCORM API logs and activities during runtime.

### Changing SCORM Modules
#### To test different SCORM files:
Upload the SCORM package to your LMS.
Update the test inputs (MCQs, expected flows) in testdata/mcq-answers.json.
Run the test again.
No code changes are needed.

### Key Technologies Used
🧪 Java Playwright: Fast browser automation

🧩 Cucumber BDD: Readable test scenarios

📦 Maven: Dependency management

🗃️ JSON Test Data: Dynamic MCQ input

🧾 Custom Reporting: Execution + SCORM API logs
