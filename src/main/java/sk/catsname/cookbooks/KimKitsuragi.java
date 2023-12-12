package sk.catsname.cookbooks;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class KimKitsuragi {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static Image convertBlobToImage(Blob blob) throws SQLException {
        if (blob == null) { return null; } // if blob itself is null, it returns null, because there is nothing to be converted

        Image image;
        try {
            byte[] imageBytes = blob.getBytes(1, (int) blob.length()); // retrieves bytes from blob

            // creates JavaFX Image from array of bytes
            image = new Image(new ByteArrayInputStream(imageBytes));
        } catch (Exception e) {
            throw new SQLException("Error converting Blob to Image: " + e.getMessage());
        }

        return image;
    }

    public static Blob convertImageToBlob(Image image) throws SQLException {
        if (image == null) { return null; } // if image itself is null, it returns null, because there is nothing to be converted

        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null); // converts the JavaFX Image to Java BufferedImage

        byte[] imageBytes; // array of bytes that the image will be later coded into
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(bufferedImage, "jpg", baos); // writes the BufferedImage into baos
            imageBytes = baos.toByteArray(); // converts the baos into array of bytes
        } catch (Exception e) {
            throw new SQLException("Error converting BufferedImage to Blob: " + e.getMessage());
        }

        return new SerialBlob(imageBytes); // returns the
    }

    //            ⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⡤⠖⠚⠉⠁⠉⠉⠉⠙⠒⢄⠀⠀⠀⠀⠀⠀
    //            ⠀⠀⠀⠀⠀⠀⠀⢀⠔⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  ⠀⠉⢢⡀⠀⠀⠀
    //            ⠀⠀⠀⠀⠀⠀⡰⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⠀⠙⣆⠀⠀
    //            ⠀⠀⠀⠀⠀⢠⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  ⠀ ⠀⠈⢇⠀
    //            ⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀    ⠘⡄
    //            ⠀⠀⠀⠀⢸⠄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⠇
    //            ⠀⠀⠀⠀⠸⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  ⠇
    //            ⠀⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀   ⡘
    //            ⠀⠀⠀⠀⠀⢻⠀⠀⠀⠀⠀⠀⠀⢀⣴⣶⡄⠀⠀⠀⠀⠀⢀⣶⡆⠀ ⢠⠇
    //            ⠀⠀⠀⠀⠀⠀⣣⠀⠀⠀⠀⠀⠀⠀⠙⠛⠁⠀⠀⠀⠀⠀⠈⠛⠁  ⡰⠃⠀
    //            ⠀⠀⠀⠀⢠⠞⠋⢳⢤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀  ⢀⠜⠁⠀⠀
    //            ⠀⠀⠀⣰⠋⠀⠀⠀⢷⠙⠲⢤⣀⡀⠀⠀⠀⠀    ⠴⠚⠁⠀⠀⠀⠀
    //            ⠀⠀⣰⠃⠀⠀⠀⠀⠘⡇⠀⣀⣀⡉⠙⠒⠒⠒⡎⠉⠀⠀⠀⠀⠀⠀⠀⠀
    //            ⠀⢠⠃⠀⠀⢶⠀⠀⠀⢳⠋⠁⠀⠙⢳⡠⠖⠚⠑⠲⡀⠀⠀⠀⠀⠀⠀⠀
    //            ⠀⡎⠀⠀⠀⠘⣆⠀⠀⠈⢧⣀⣠⠔⡺⣧⠀⡴⡖⠦⠟⢣⠀⠀⠀⠀⠀⠀
    //            ⢸⠀⠀⠀⠀⠀⢈⡷⣄⡀⠀⠀⠀⠀⠉⢹⣾⠁⠁⠀⣠⠎⠀⠀⠀⠀⠀⠀
    //            ⠈⠀⠀⠀⠀⠀⡼⠆⠀⠉⢉⡝⠓⠦⠤⢾⠈⠓⠖⠚⢹⠀⠀⠀⠀⠀⠀⠀
    //            ⢰⡀⠀⠀⠀⠀⠀⠀⠀⠀⢸⠁⠀⠀⠀⢸⠀⠀⠀⠀⡏⠀⠀⠀⠀⠀⠀⠀
    //            ⠀⠳⡀⠀⠀⠀⠀⠀⠀⣀⢾⠀⠀⠀⠀⣾⠀⠀⠀⠀⡇⠀⠀⠀⠀⠀⠀⠀
    //            ⠀⠀⠈⠐⠢⠤⠤⠔⠚⠁⠘⣆⠀⠀⢠⠋⢧⣀⣀⡼⠀⠀⠀⠀⠀⠀⠀⠀
}
