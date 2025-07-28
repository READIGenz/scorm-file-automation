@SCROMTesting @IMCExpress
Feature: Testing of Scorm Files
  Scenario Outline: As a learner, I would like to be able to open a WBT via the LHG LMS in the course.
    Given The user launches the LMS on "<browser>"
    And The user performs login as a learner
    And The user navigates to the SCORM
    When The user opens the SCORM
    And The user starts the IMCExpress SCORM course
    And The user navigates through the course

    Examples:
      | browser |
      | edge    |
