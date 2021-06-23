package ga.negyahu.music.fileupload.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ga.negyahu.music.fileupload.controller.UploadController;
import java.util.Locale;
import java.util.UUID;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
public class BaseFileUpload {

    private String originalName;

    private String fileName;

    @JsonIgnore
    private String type;

    @JsonIgnore
    private String filePath;

    private String contentType;

    private long size;

    public String getImageUrl() {
        return UploadController.UPLOAD_URL_PREFIX + "/" + this.type + "/" + this.fileName;
    }

    public BaseFileUpload(MultipartFile file, String filePath, String type) {
        this.originalName = file.getOriginalFilename();
        this.filePath = filePath;
        this.size = file.getSize();
        this.contentType = file.getContentType();
        this.type = type;
        createFileName();
    }

    public BaseFileUpload(MultipartFile multipartFile, String filePath) {
        this.originalName = multipartFile.getOriginalFilename();
        this.filePath = filePath;
        this.size = multipartFile.getSize();
        this.contentType = multipartFile.getContentType();
    }

    public void setNewMultipartFile(MultipartFile file) {
        this.originalName = file.getOriginalFilename();
        this.filePath = filePath;
        this.size = file.getSize();
        this.contentType = file.getContentType();
        createFileName();
    }

    @JsonIgnore
    public String getFullFilePath() {
        // upload directory path / filename
        return this.filePath + "/" + this.fileName;
    }

    public void createFileName() {
        // file encoding type
        String[] split = this.originalName.split("\\.");
        String type = split[split.length - 1].toLowerCase(Locale.ROOT);
        // UUID + encoding type
        this.fileName = UUID.randomUUID() + "." + type;
    }

    public void setBaseFileUpload(BaseFileUpload fileUpload) {
        this.fileName = fileUpload.fileName;
        this.filePath = fileUpload.filePath;
        this.originalName = fileUpload.originalName;
        this.contentType = fileUpload.getContentType();
        this.size = fileUpload.getSize();
    }

}
