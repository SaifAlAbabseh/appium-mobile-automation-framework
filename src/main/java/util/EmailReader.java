package util;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.SubjectTerm;
import org.jsoup.Jsoup;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailReader {

    public static Message waitForEmail(String host, String username, String appPassword, String subject, int timeoutSeconds) throws Exception {

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imaps");
        Thread.sleep(5000);
        store.connect(host, username, appPassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        for (int i = 0; i < timeoutSeconds; i++) {
            Message[] messages = inbox.search(new SubjectTerm(subject));
            if (messages.length > 0) {
                return messages[messages.length - 1];
            }
            Thread.sleep(1000);
        }

        throw new RuntimeException("Email not received within timeout");
    }

    public static String getFullEmailBody(Message message) throws Exception {
        Object content = message.getContent();

        if (content instanceof String) {
            // Single-part plain text email
            return content.toString();
        }

        if (content instanceof MimeMultipart) {
            return extractMultipart((MimeMultipart) content);
        }

        // Fallback: InputStream content (sometimes occurs in headless/minimal environments)
        if (content instanceof InputStream) {
            InputStream is = (InputStream) content;
            return Jsoup.parse(new String(is.readAllBytes(), StandardCharsets.UTF_8)).text();
        }

        return "";
    }

    /**
     * Recursively traverses all multipart contents and extracts full text.
     */
    private static String extractMultipart(MimeMultipart multipart) throws Exception {
        StringBuilder bodyBuilder = new StringBuilder();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);

            // Skip attachments
            if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                continue;
            }

            Object partContent = part.getContent();

            // Handle nested multipart recursively
            if (partContent instanceof MimeMultipart) {
                bodyBuilder.append(extractMultipart((MimeMultipart) partContent));
            }
            // text/plain → include
            else if (part.isMimeType("text/plain")) {
                bodyBuilder.append(getContentAsString(partContent)).append("\n");
            }
            // text/html → convert HTML to readable text
            else if (part.isMimeType("text/html")) {
                String html = getContentAsString(partContent);
                bodyBuilder.append(Jsoup.parse(html).text()).append("\n");
            }
        }

        return bodyBuilder.toString().trim();
    }

    /**
     * Converts part content (String or InputStream) to String
     */
    private static String getContentAsString(Object content) throws Exception {
        if (content instanceof String) {
            return (String) content;
        }
        if (content instanceof InputStream) {
            InputStream is = (InputStream) content;
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
        return "";
    }
}
