package ga.negyahu.music.fileupload;

import static ga.negyahu.music.fileupload.controller.AccountFileUploadController.ACCOUNT_FILE_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.fileupload.repository.AccountFileUploadRepository;
import ga.negyahu.music.fileupload.service.AccountFileUploadService;
import ga.negyahu.music.utils.FileUploadTestUtil;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import ga.negyahu.music.utils.annotation.WithTestUser;
import java.io.File;
import java.io.FileInputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@CustomSpringBootTest
public class AccountFileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountFileUploadRepository uploadRepository;
    @AfterEach
    public void destroy(){
        accountRepository.deleteAll();
    }

    @WithTestUser
    @Test
    public void test1() throws Exception {

        File originalFile = FileUploadTestUtil.getTestImageAsFile();
        FileInputStream inputStream = new FileInputStream(originalFile);
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.png",
            MediaType.IMAGE_PNG_VALUE, inputStream);

        this.mockMvc.perform(multipart(ACCOUNT_FILE_URL, 10L).file(multipartFile)
        ).andDo(print());

        long count = uploadRepository.count();
        Assertions.assertEquals(1,count);

    }

    @WithTestUser
    @Test
    public void test2() throws Exception {
        File originalFile = FileUploadTestUtil.getTestImageAsFile();
        FileInputStream inputStream = new FileInputStream(originalFile);
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.png",
            MediaType.IMAGE_PNG_VALUE, inputStream);
        this.mockMvc.perform(multipart(ACCOUNT_FILE_URL, 10L).file(multipartFile)
        ).andDo(print());
    }

}