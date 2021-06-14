package ga.negyahu.music.fileupload.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.fileupload.entity.AccountFileUpload;
import ga.negyahu.music.fileupload.repository.AccountFileUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import ga.negyahu.music.utils.FileUploadTestUtil;
import ga.negyahu.music.utils.TestUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(value = {SpringExtension.class})
@ContextConfiguration(classes = {
    FileUploadUtil.class,
    AccountFileUploadService.class,
},
    initializers = ConfigDataApplicationContextInitializer.class
)
public class AccountFileUploadServiceTest {

    @Autowired
    public FileUploadUtil fileUploadUtil;
    @Autowired
    private AccountFileUploadService fileUploadService;
    @MockBean
    private AccountFileUploadRepository uploadRepository;

    @AfterEach
    public void destroy() {
        String filePath = fileUploadService.getFilePath();
        File file = new File(filePath);
        String[] list = file.list();
        System.out.println("file = " + filePath);
        for (String name : list) {
            File target = new File(filePath + name);
            target.delete();
        }
    }

    @Test
    public void 이미지_저장_테스트() throws IOException {
        // given : 테스트용 랜덤 이미지 생성, MockMultipartFile 생성
        File originalFile = FileUploadTestUtil.getTestImageAsFile();
        FileInputStream inputStream = new FileInputStream(originalFile);
        MockMultipartFile multipartFile = new MockMultipartFile("uploadImage", "test.png",
            MediaType.IMAGE_PNG_VALUE, inputStream);
        Account defaultAccount = TestUtils.createDefaultAccount();

        // when : 해당 파일 저장
        AccountFileUpload baseFileUpload = (AccountFileUpload) fileUploadService
            .saveFile(multipartFile, defaultAccount);

        // then : 테스트용 파일과 BaseFileUpload 의 파일패스를 이용해 찾은 파일을 비교
        assertNotNull(baseFileUpload.getFullFilePath());
        File uploadedFile = new File(baseFileUpload.getFullFilePath());
        BufferedImage image = ImageIO.read(uploadedFile);
        assertEquals(FileUploadTestUtil.WIDTH, image.getWidth());
        assertEquals(FileUploadTestUtil.HEIGHT, image.getHeight());
    }

    @Test
    public void 이미지_찾기_테스트() throws Exception {
        File originalFile = FileUploadTestUtil.getTestImageAsFile();
        FileInputStream inputStream = new FileInputStream(originalFile);
        MockMultipartFile multipartFile = new MockMultipartFile("uploadImage", "test.png",
            MediaType.IMAGE_PNG_VALUE, inputStream);
        Account defaultAccount = TestUtils.createDefaultAccount();

        // when : 해당 파일 저장
        AccountFileUpload baseFileUpload = (AccountFileUpload) fileUploadService
            .saveFile(multipartFile, defaultAccount);

        File file = this.fileUploadService.getFileByFileFullName(baseFileUpload.getFullFilePath());
        BufferedImage originalImage = ImageIO.read(originalFile);
        BufferedImage image = ImageIO.read(file);
        assertEquals(originalImage.getWidth(), image.getWidth());
        assertEquals(originalImage.getHeight(), image.getHeight());
    }

    @Test
    public void 존재하지않는_파일() throws FileNotFoundException {

        // given : 존재하지 않는 파일이
        String invalidFilePath = "ddfadf";

        // when : 파일 요쳥
        assertThrows(RuntimeException.class, () -> {
            File file = this.fileUploadService.getFileByFileFullName(invalidFilePath);
        },"예외가 발생 : Exception 미지정");
    }

}