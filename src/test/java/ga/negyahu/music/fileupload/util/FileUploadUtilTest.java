package ga.negyahu.music.fileupload.util;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

public class FileUploadUtilTest {

    @Test
    public void test1() throws Exception{
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("file:/Users/youzheng/workspace/negyahu/back/build/resources/test/upload");
        String path = resource.getURI().getPath();
        System.out.println("path = " + path);
    }

}