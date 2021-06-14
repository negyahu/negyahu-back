package ga.negyahu.music.utils;

import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FileUploadTestUtil {

    public static final int HEIGHT = 300;
    public static final int WIDTH = 250;

    public static File getTestImageAsFile() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, Transparency.OPAQUE);
        String desktop = "/Users/youzheng/desktop/test.png";
        File file = new File(desktop);
        ImageIO.write(bufferedImage, "png", file);
        return file;
    }

    public static BufferedImage getTestImage() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(300, 300, Transparency.OPAQUE);
        bufferedImage.setRGB(153, 153, 153);
        return bufferedImage;
    }

}
