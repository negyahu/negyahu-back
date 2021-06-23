package ga.negyahu.music.fileupload.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.entity.FileType;
import ga.negyahu.music.fileupload.service.FileUploadService;
import ga.negyahu.music.fileupload.util.FileUploadUtils;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.security.annotation.OnlyAdmin;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AgencyUploadController {

    @Qualifier("licenseUploadService")
    private final FileUploadService<AgencyUpload> licenseUploadService;

    public static final String LICENSE_FILE_URL = "/api/licenses";

    @PostMapping(LICENSE_FILE_URL)
    public ResponseEntity uploadLicense(MultipartFile file, @LoginUser Account user)  {
        AgencyUpload upload = licenseUploadService.saveFile(file, null, user);
        return UploadController.createResponseEntity(upload);
    }

}
