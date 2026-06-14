package helpers;

import io.cucumber.datatable.DataTable;
import jakarta.mail.Message;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import util.Driver;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import util.EmailReader;

public class MainHelpers {

    public static void waitForVisibilityOfElement(int seconds, By elementSelector) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.visibilityOfElementLocated(elementSelector));
    }

    public static void waitForOfElementToBeClickable(int seconds, By elementSelector) {
        WebDriverWait wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.elementToBeClickable(elementSelector));
    }

    public static String getDataTableValueAsString(DataTable table, int row, String header) {
        List<Map<String, String>> data = table.asMaps(String.class, String.class);
        return data.get(row).get(header);
    }

    public static void verifyEmail(String username, String appPassword, String subject, int timeoutSeconds, String expectedBody) {
        try {
            Message message = EmailReader.waitForEmail(
                    "imap.gmail.com",
                    username,
                    appPassword,
                    subject,
                    timeoutSeconds
            );

            String body = EmailReader.getFullEmailBody(message);
            Assert.assertTrue(body.toLowerCase().contains(expectedBody.toLowerCase()), "Expected email body to contain: " + expectedBody + "\n Actual body: " + body);
        }
        catch(Exception e) {
            System.err.println("Could not read emails \n" + e.getMessage());
        }
    }

    public static String getOTP(String username, String appPassword, String subject, int timeoutSeconds) {
        try {
            Message message = EmailReader.waitForEmail(
                    "imap.gmail.com",
                    username,
                    appPassword,
                    subject,
                    timeoutSeconds
            );
            String body = EmailReader.getFullEmailBody(message);
            return extractOTP(body);
        }
        catch(Exception e) {
            System.err.println("Could not read emails \n" + e.getMessage());
        }
        return null;
    }

    private static String extractOTP(String body) {
        String[] splits1 = body.split("code below to login. ");
        String[] splits2 = splits1[1].split(" If you didn't request this");
        return splits2[0];
    }

    public static String generateRandomString(int stringLength) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random RANDOM = new Random();
        StringBuilder result = new StringBuilder(stringLength);
        int charactersLength = CHARACTERS.length();
        for (int i = 0; i < stringLength; i++) {
            int randomIndex = RANDOM.nextInt(charactersLength);
            result.append(CHARACTERS.charAt(randomIndex));
        }

        return result.toString();
    }
}
