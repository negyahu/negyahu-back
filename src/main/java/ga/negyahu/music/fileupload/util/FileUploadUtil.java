package ga.negyahu.music.fileupload.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.Getter;
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
}
