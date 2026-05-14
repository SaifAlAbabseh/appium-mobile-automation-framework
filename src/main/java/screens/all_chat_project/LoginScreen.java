package screens.all_chat_project;

import org.openqa.selenium.By;
import util.Screen;

public class LoginScreen extends Screen {

    private final By usernameField = By.id("com.example.allchat:id/username_field"),
                    passwordField = By.id("com.example.allchat:id/password_field"),
                    loginButton = By.id("com.example.allchat:id/button");

    public void typeUsername(String username) {
        findElementBy(usernameField).sendKeys(username);
    }

    public void typePassword(String password) {
        findElementBy(passwordField).sendKeys(password);
    }

    public MainScreen clickLoginButton() {
        findElementBy(loginButton).click();
        return new MainScreen();
    }

    public boolean returnIfLoginButtonDisplayed() {
        return findElementBy(loginButton).isDisplayed();
    }
}
