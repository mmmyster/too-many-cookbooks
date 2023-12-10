package sk.catsname.cookbooks;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class KimKitsuragi {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");

    public static Image convertBlobToImage(Blob blob) throws SQLException {
        if (blob == null) { return null; }

        Image image;
        try (InputStream bninaryStream = blob.getBinaryStream()) {
            image = ImageIO.read(bninaryStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return image;
    }

    public static Blob convertImageToBlob(Image image) throws SQLException {
        if (image == null) { return null; }

        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageData;
        try {
            ImageIO.write(bufferedImage, "jpg", baos);
            imageData = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new SerialBlob(imageData);
    }
}
