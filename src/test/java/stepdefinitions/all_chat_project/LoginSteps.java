package stepdefinitions.all_chat_project;

import helpers.MainHelpers;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import static org.testng.Assert.*;
import screens.all_chat_project.LoginScreen;
import screens.all_chat_project.MainScreen;
import util.EnvConfig;
import util.Screen;

public class LoginSteps {

    private final Screen screen = new Screen();
    private final LoginScreen loginScreen = new LoginScreen();
    private MainScreen mainScreen;

    @Given("user enter valid credentials")
    public void enterValidCredentials(DataTable table) {
        String username = EnvConfig.get(MainHelpers.getDataTableValueAsString(table, 0, "username"));
        String password = EnvConfig.get(MainHelpers.getDataTableValueAsString(table, 0, "password"));
        loginScreen.typeUsername(username);
        loginScreen.typePassword(password);
    }

    @And("clicks login")
    public void clickOnLoginButton() {
        mainScreen = loginScreen.clickLoginButton();
    }

    @Then("user should be logged in")
    public void verifyLogin(DataTable table) {
        String actualUsername = mainScreen.returnLoggedInUsername();
        String expectedUsername = EnvConfig.get(MainHelpers.getDataTableValueAsString(table, 0, "username"));

        assertEquals(actualUsername, expectedUsername);
    }

    @And("clicks on the profile button")
    public void clickOnProfileButton() {
        mainScreen.clickOnProfile();
    }

    @Then("clicks on the logout button")
    public void clickOnLogoutButton() {
        mainScreen.clickOnLogoutButton();
    }

    @Then("user should be logged out")
    public void verifyLogout() {
        assertTrue(loginScreen.returnIfLoginButtonDisplayed(), "Could not find the login button.");
    }
}
