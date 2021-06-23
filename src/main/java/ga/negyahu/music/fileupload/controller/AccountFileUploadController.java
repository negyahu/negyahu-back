package ga.negyahu.music.fileupload.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.exception.ResultMessage;
import ga.negyahu.music.fileupload.entity.AccountUpload;
import ga.negyahu.music.fileupload.service.FileUploadService;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import ga.negyahu.music.security.annotation.LoginUser;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AccountFileUploadController implements InitializingBean {

    @Qualifier("accountFileUploadService")
    private final FileUploadService<AccountUpload> accountFileUploadService;
    private final FileUploadUtil uploadUtil;

    public static final String ACCOUNT_FILE_URL = "/api/accounts/{id}/image";
    public static final String DEFAULT_FORMAT = "png";
    private String filePath;


    @Override
    public void afterPropertiesSet() throws Exception {
        filePath = this.accountFileUploadService.getFilePath();
    }

    @PostMapping(value = ACCOUNT_FILE_URL)
    public ResponseEntity uploadImage(@RequestParam("file") MultipartFile multipartFile, @LoginUser
        Account user, @PathVariable("id") Long accountId) throws IOException {
        if (!isImageType(multipartFile)) {
            ResultMessage message = ResultMessage
                .createFailMessage("[ERROR] 이미지 파일만 업로드할 수 있습니다.");
            return ResponseEntity.badRequest().body(message);
        }

        AccountUpload upload = this.accountFileUploadService
            .saveFile(multipartFile, Account.builder().id(accountId).build(), user);
        return UploadController.createResponseEntity(upload);
    }

    public boolean isImageType(MultipartFile file) {
        String type = file.getContentType();
        return type.equals(MediaType.IMAGE_PNG_VALUE) || type.equals(MediaType.IMAGE_JPEG_VALUE);
    }

    /*
     * account 개인이미지 로드
     * */
    @GetMapping(value = ACCOUNT_FILE_URL)
    public ResponseEntity loadImage(@PathVariable("id") Long accountId)
        throws IOException {

        File file = new File(this.filePath + accountId + "." + DEFAULT_FORMAT);
        if (!file.exists()) {
            return ResponseEntity.badRequest()
                .body(ResultMessage.createFailMessage("[ERROR] 존재하지 않는 이미지입니다."));
        }

        IOUtils.toByteArray(file.toURI());
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(IOUtils.toByteArray(file.toURI()))
            ;
    }

    @GetMapping(value = {ACCOUNT_FILE_URL + "/{width}/{height}", ACCOUNT_FILE_URL + "/{width}"})
    public ResponseEntity loadImageResizing(@PathVariable("id") Long accountId,
        @PathVariable("width") Integer width,
        @PathVariable(value = "height", required = false) Integer height)
        throws IOException {
        if (height == null || height.equals(0)) {
            height = width;
        }

        File file = new File(this.filePath + accountId + "." + DEFAULT_FORMAT);
        if (!file.exists()) {
            return ResponseEntity.badRequest()
                .body(ResultMessage.createFailMessage("[ERROR] 존재하지 않는 이미지입니다."));
        }

        ByteArrayOutputStream byteArrayOutputStream = this.uploadUtil.resizingImage(width, height,
            file);
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(byteArrayOutputStream.toByteArray())
            ;
    }

    @DeleteMapping(ACCOUNT_FILE_URL)
    public ResponseEntity deleteImage(@PathVariable("id") Long accountId, @LoginUser Account user) {
        if (!user.getId().equals(accountId)) {
            throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
        }
        this.accountFileUploadService.deleteImageByOwnerId(accountId);

        return null;
    }


}
