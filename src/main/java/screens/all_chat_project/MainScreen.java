package screens.all_chat_project;

import org.openqa.selenium.By;
import util.Screen;

public class MainScreen extends Screen {

    private final By profileButton = By.id("com.example.allchat:id/profilePicture"),
                    logoutButton = By.id("com.example.allchat:id/button5"),
                    usernameLabel = By.id("com.example.allchat:id/usernameField");

    public void clickOnProfile() {
        findElementBy(profileButton).click();
    }

    public void clickOnLogoutButton() {
        findElementBy(logoutButton).click();
    }

    public String returnLoggedInUsername() {
        try {
            return findElementBy(usernameLabel).getText();
        }
        catch (Exception e) {
            System.err.println("Could not find the username label on the main screen." + e.getMessage());
        }
        return null;
    }
}
