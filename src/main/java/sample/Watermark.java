package sample;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Watermark {

    public static void addWatermark(File imageFile, String str) {
        try {



            File watermarkFile = new File("C:\\Users\\MYN\\Desktop\\tranbg.png");

            BufferedImage toteWM = ImageIO.read(new File("C:\\Users\\MYN\\Desktop\\tote.png"));
            BufferedImage watermark = ImageIO.read(watermarkFile);
            BufferedImage image = ImageIO.read(imageFile);

            Graphics graphics = image.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.setPaintMode();
            graphics.drawImage(watermark, 0, 0, null );
            graphics.setFont(new Font("Arial", Font.PLAIN, 30));

            graphics.drawImage(toteWM, (image.getWidth() - toteWM.getWidth())/2, (image.getHeight() - toteWM.getHeight())/2, null );

            graphics.drawString(str, 40,40);

            ImageIO.write(image, "jpg", imageFile);
        } catch (Exception  e) {
            e.printStackTrace();
        }
    }

    public static void mass(String imageFolder)
    {
        File[] allImages = new File(imageFolder).listFiles();

        for (File f: allImages){
            if (!f.getAbsolutePath().endsWith("jpg") || f.getAbsolutePath().endsWith("gif") || f.getAbsolutePath().endsWith("png"))
                continue;

            String watermark = f.getName().replace(".gif", "").replace(".png", "").replace(".jpg", "")
                    .split(" ")[0];

            System.out.println(f.getAbsolutePath());
            addWatermark(f, watermark);
        }
    }

    public static void main(String[] args) {
        mass("C:\\Users\\MYN\\Google Drive\\Tote Babe\\Hei\\NEW-DRESS");
        mass("C:\\Users\\MYN\\Google Drive\\Tote Babe\\Hei\\NEW-SUIT");
    }

}
