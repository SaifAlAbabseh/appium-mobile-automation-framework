@login @smoke @regression
Feature: Login

  Scenario: User logs into the app
    Given user enter valid credentials
      | username    | password    |
      | AC_USERNAME | AC_PASSWORD |
    And clicks login
    Then user should be logged in
      | username    |
      | AC_USERNAME |

  Scenario: User logs in and logs out from the app
    Given user enter valid credentials
      | username    | password    |
      | AC_USERNAME | AC_PASSWORD |
    And clicks login
    Then user should be logged in
      | username    |
      | AC_USERNAME |
    And clicks on the profile button
    Then clicks on the logout button
    Then user should be logged out