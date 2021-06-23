package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import java.io.File;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public interface FileUploadService<T extends BaseFileUpload> {

    static final String targetDirectory = "upload/";

    T saveFile(MultipartFile multipartFile, FileUpload fileUpload, Account account);

    File getFileByFileFullName(String fullFileName);

    File getFileByFileName(String fileName);

    File getFileByOwnerId(Long ownerId);

    AgencyUpload getFileUploadByOwnerId(Long ownerId);

    String getFilePath();

    void deleteImageByOwnerId(Long accountId);

    T setOwner(Long targetId, FileUpload fileUpload);

    default String createDefaultPath(String suffix, String filePath) throws IOException {
        if (filePath == null) {
            filePath = new ClassPathResource("").getURI().getPath();
        }
        return filePath + targetDirectory + suffix;
    }

}
