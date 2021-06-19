package ga.negyahu.music.fileupload.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.Getter;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadUtil {

    public static final String SEPARATOR = ".";

    public static String createFileName(String originalName) {
        String nanoTimeAsString = String.valueOf(System.nanoTime());
        return nanoTimeAsString + SEPARATOR + originalName;
    }

    public ByteArrayOutputStream resizingImage(int width, int height,
        File targetImage) throws IOException {
        BufferedImage bufferedImage = Thumbnails.of(targetImage.toString())
            .size(width, height)
            .outputFormat("png")
            .keepAspectRatio(false)
            .asBufferedImage();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        IOUtils.toByteArray(targetImage.toURI());
        return byteArrayOutputStream;
    }
}
