package ga.negyahu.music.fileupload.util;

import static org.springframework.http.ResponseEntity.ok;

import ga.negyahu.music.exception.FileNotFoundException;
import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

public class FileUploadUtils {

    public static ByteArrayOutputStream resizingImage(int width, int height,
        File targetImage) throws IOException {
        return resizingImage(width,height,targetImage,"png");
    }

    public static ByteArrayOutputStream resizingImage(int width, int height,
        File targetImage, String encodeType) throws IOException {
        BufferedImage bufferedImage = Thumbnails.of(targetImage.toString())
            .size(width, height)
            .outputFormat(encodeType)
            .keepAspectRatio(false)
            .asBufferedImage();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        IOUtils.toByteArray(targetImage.toURI());
        return byteArrayOutputStream;
    }

    public static File getFile(BaseFileUpload upload) {
        return getFile(upload.getFullFilePath());
    }

    public static File getFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    public static final ResponseEntity<byte[]> transToByteRes(File file) throws IOException {
        return ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(IOUtils.toByteArray(file.toURI()))
            ;
    }

}
