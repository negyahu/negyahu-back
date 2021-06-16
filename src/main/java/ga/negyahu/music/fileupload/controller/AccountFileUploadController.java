package ga.negyahu.music.fileupload.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.fileupload.entity.AccountFileUpload;
import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.service.FileUploadService;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import ga.negyahu.music.security.annotation.LoginUser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AccountFileUploadController {

    @Qualifier("accountFileUploadService")
    private final FileUploadService fileUploadService;

    public static final String ACCOUNT_FILE_URL = "/api/accounts/{id}/upload";


    /*
     * account 개인이미지 업로
     * */
    @PostMapping(value = ACCOUNT_FILE_URL
        , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile multipartFile, @LoginUser
        Account user, @PathVariable("id") Long accountId) throws IOException {
        if (!user.getId().equals(accountId)) {
            throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
        }
        BaseFileUpload baseFileUpload = this.fileUploadService.saveFile(multipartFile, user);

        URI uri = linkTo(methodOn(AccountFileUploadController.class).loadImage(accountId)).toUri();
        return ResponseEntity.created(uri).build();
    }

    /*
     * account 개인이미지 로드
     * */
    @GetMapping(value = ACCOUNT_FILE_URL
        , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity loadImage(@PathVariable("accountId") Long accountId)
        throws IOException {
        File accountImageFile = this.fileUploadService.getFileByAccountId(accountId);
        IOUtils.toByteArray(accountImageFile.toURI());
        return ResponseEntity.ok(IOUtils.toByteArray(accountImageFile.toURI()));
    }

}
