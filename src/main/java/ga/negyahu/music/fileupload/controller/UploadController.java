package ga.negyahu.music.fileupload.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import ga.negyahu.music.exception.FileNotFoundException;
import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.service.ArtistMemberUploadService;
import io.swagger.models.Response;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UploadController {

    public static final String UPLOAD_URL_PREFIX = "/api/upload";

    @Value("${upload.account.path:#{uploadController.getDefaultPath()}}")
    public String fileDirectoryPath;

    public static String getDefaultPath() throws IOException {
        return new ClassPathResource("upload").getURI().getPath();
    }

    @GetMapping(UPLOAD_URL_PREFIX + "/{type}" + "/{finePath}")
    public ResponseEntity loadImage(@PathVariable("finePath") String finePath,
        @PathVariable("type") String type) {
        try {
            File file = new File(fileDirectoryPath + "/" + type + "/" + finePath);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            IOUtils.toByteArray(file.toURI());
            return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(IOUtils.toByteArray(file.toURI()))
                ;
        } catch (Exception e) {
            throw new FileNotFoundException();
        }
    }

    public static final URI createUri(String path) {
        return createUri("", path);
    }

    public static final URI createUri(String type, String path) {
        return linkTo(methodOn(UploadController.class).loadImage(type, path)).toUri();
    }

    public static final ResponseEntity createResponseEntity(String type, String path,
        Object content) {
        URI uri = createUri(type, path);
        return ResponseEntity.created(uri).body(content);
    }

    public static final ResponseEntity createResponseEntity(String path, Object content) {
        URI uri = createUri(path);
        return ResponseEntity.created(uri).body(content);
    }

    public static final ResponseEntity createResponseEntity(BaseFileUpload fileUpload) {
        URI uri = createUri(fileUpload.getImageUrl());
        return ResponseEntity.created(uri).body(fileUpload);
    }


}
