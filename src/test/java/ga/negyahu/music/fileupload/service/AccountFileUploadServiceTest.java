package ga.negyahu.music.fileupload.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.fileupload.entity.AccountUpload;
import ga.negyahu.music.fileupload.repository.AccountUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtils;
import ga.negyahu.music.utils.FileUploadTestUtil;
import ga.negyahu.music.utils.TestUtils;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
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
    FileUploadUtils.class,
    AccountFileUploadService.class,
},
    initializers = ConfigDataApplicationContextInitializer.class
)
public class AccountFileUploadServiceTest {

    @Autowired
    private AccountFileUploadService fileUploadService;
    @MockBean
    private AccountUploadRepository uploadRepository;

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

    @Disabled
    @Test
    public void ?????????_??????_?????????() throws IOException {
        // given : ???????????? ?????? ????????? ??????, MockMultipartFile ??????
        File originalFile = FileUploadTestUtil.getTestImageAsFile();
        FileInputStream inputStream = new FileInputStream(originalFile);
        MockMultipartFile multipartFile = new MockMultipartFile("uploadImage", "test.png",
            MediaType.IMAGE_PNG_VALUE, inputStream);
        Account defaultAccount = TestUtils.createDefaultAccount();

        // when : ?????? ?????? ??????
        AccountUpload baseFileUpload = (AccountUpload) fileUploadService
            .saveFile(multipartFile, defaultAccount, null);

        // then : ???????????? ????????? BaseFileUpload ??? ??????????????? ????????? ?????? ????????? ??????
        assertNotNull(baseFileUpload.getFullFilePath());
        File uploadedFile = new File(baseFileUpload.getFullFilePath());
        BufferedImage image = ImageIO.read(uploadedFile);
        assertEquals(FileUploadTestUtil.WIDTH, image.getWidth());
        assertEquals(FileUploadTestUtil.HEIGHT, image.getHeight());
    }

    @Disabled
    @Test
    public void ?????????_??????_?????????() throws Exception {
        File originalFile = FileUploadTestUtil.getTestImageAsFile();
        FileInputStream inputStream = new FileInputStream(originalFile);
        MockMultipartFile multipartFile = new MockMultipartFile("uploadImage", "test.png",
            MediaType.IMAGE_PNG_VALUE, inputStream);
        Account defaultAccount = TestUtils.createDefaultAccount();

        // when : ?????? ?????? ??????
        AccountUpload baseFileUpload = (AccountUpload) fileUploadService
            .saveFile(multipartFile, defaultAccount, null);

        File file = this.fileUploadService.getFileByFileFullName(baseFileUpload.getFullFilePath());
        BufferedImage originalImage = ImageIO.read(originalFile);
        BufferedImage image = ImageIO.read(file);
        assertEquals(originalImage.getWidth(), image.getWidth());
        assertEquals(originalImage.getHeight(), image.getHeight());
    }

    @Disabled
    @Test
    public void ??????????????????_??????() throws FileNotFoundException {

        // given : ???????????? ?????? ?????????
        String invalidFilePath = "ddfadf";

        // when : ?????? ??????
        assertThrows(RuntimeException.class, () -> {
            File file = this.fileUploadService.getFileByFileFullName(invalidFilePath);
        }, "????????? ?????? : Exception ?????????");
    }

}