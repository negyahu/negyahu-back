package ga.negyahu.music.fileupload.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.entity.FileType;
import ga.negyahu.music.fileupload.service.FileUploadService;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.security.annotation.OnlyAdmin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AgencyUploadController implements InitializingBean {

    @Qualifier("licenseUploadService")
    private final FileUploadService<AgencyUpload> licenseUploadService;
    private final FileUploadUtil uploadUtil;

    public static final String AGENCY_FILE_URL = "/api/agency/{id}/upload";
    public static final String LICENSE_FILE_URL = "/api/licenses";
    private String filePath;

    @Override
    public void afterPropertiesSet() throws Exception {
        filePath = this.licenseUploadService.getFilePath();
    }

    @PostMapping(LICENSE_FILE_URL)
    public ResponseEntity uploadLicense(MultipartFile file, @LoginUser Account user)
        throws IOException {
        AgencyUpload upload = licenseUploadService.saveFile(file, null, user);
        return UploadController.createResponseEntity(upload);
    }

    @OnlyAdmin
    @GetMapping(LICENSE_FILE_URL + "/{id}")
    public ResponseEntity loadLicense(@PathVariable("id") Long id) throws IOException {
        AgencyUpload fileUpload = this.licenseUploadService.getFileUploadByOwnerId(id);
        // 사업자 등록증만 조회할 수 있다.
        if (fileUpload.getFileType() != FileType.BUSINESS) {
            throw new FileNotFoundException();
        }
        // 해당 이미지가 존재하는지 확인
        File license = new File(fileUpload.getFullFilePath());
        if (!license.exists()) {
            return ResponseEntity.notFound()
                .build();
        }
        IOUtils.toByteArray(license.toURI());
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(IOUtils.toByteArray(license.toURI()))
            ;
    }
}
