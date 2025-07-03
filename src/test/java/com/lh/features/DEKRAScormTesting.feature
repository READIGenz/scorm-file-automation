@SCROMTesting
Feature: Testing of Scorm Files
  Scenario: As a learner, I would like to be able to open a WBT via the LHG LMS in the course.
    Given The user launches LHG-LMS url
    And The user performs login as a learner
    And The user navigates to the SCORM
    When The user opens the SCORM
    And The user starts the course
    Then The user verifies that the table of contents and progress bar is visible
    Then The user completes the course partially
    When The user opens the SCORM
    And The user starts the course
#    Then The user verifies progress has been updated
    Then The user completes the course
