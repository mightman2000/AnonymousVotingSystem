import javax.swing.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Generator {

    public String RandomKeyGenerator() {

        final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

        int length = 7;

        Random random = new Random();
        StringBuilder key = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            key.append(randomChar);

        }

        Instant instant = Instant.now();

        String timestampString = instant.toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        String formattedTimestamp = instant.atZone(java.time.ZoneId.systemDefault()).format(formatter);


        String finalKey = key + formattedTimestamp;

         JOptionPane.showMessageDialog(null, finalKey, "Your voting key", JOptionPane.INFORMATION_MESSAGE);

        return finalKey.toString();

    }
}
