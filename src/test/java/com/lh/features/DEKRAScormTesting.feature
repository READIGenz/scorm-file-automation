@SCROMTesting
Feature: Testing of Scorm Files
  Scenario: As a learner, I would like to be able to open a WBT via the LHG LMS in the course.
    Given The user launches LHG-LMS url
    And The user enters the credentials
#    And The user enters "<LoginID>" in "IDField"
#    And The user enters "<Password>" in "PasswordField"
    And The user clicks on the "LoginButton"
    And The user clicks on the "MyCourses"
    And The user clicks on the "All courses"
    Then The user clicks on the "Course"
    When The user opens the SCORM
    And The user starts the course
    Then The user verifies that the table of contents and progress bar is visible
    Then The user completes the course partially
    And The user clicks on the "Back"
    When The user opens the SCORM
    And The user starts the course
#    Then The user verifies progress has been updated
    Then The user completes the course

#    Examples:
#    |LoginID|Password|
#    |TF00005|Z29IKmzE|
