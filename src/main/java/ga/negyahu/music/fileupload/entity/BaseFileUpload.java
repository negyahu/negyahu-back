package ga.negyahu.music.fileupload.entity;

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

    private String filePath;

    private String contentType;

    private long size;

    public BaseFileUpload(MultipartFile multipartFile, String filePath) {
        this.originalName = multipartFile.getOriginalFilename();
        this.filePath = filePath;
        this.size = multipartFile.getSize();
        this.contentType = multipartFile.getContentType();
    }

    public void setNewMultipartFile(MultipartFile multipartFile) {
        this.originalName = multipartFile.getOriginalFilename();
        this.size = multipartFile.getSize();
        this.contentType = multipartFile.getContentType();
        createFileName();
    }

    public String getFullFilePath() {
        return this.filePath + this.fileName;
    }

    public void createFileName() {
        this.fileName = UUID.randomUUID().toString() + "." + this.originalName;
    }

    public void setBaseFileUpload(BaseFileUpload fileUpload) {
        this.fileName = fileUpload.fileName;
        this.filePath = fileUpload.filePath;
        this.originalName = fileUpload.originalName;
        this.contentType = fileUpload.getContentType();
        this.size = fileUpload.getSize();
    }

}
