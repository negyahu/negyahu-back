package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.fileupload.entity.AgencyFileUpload;
import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import java.io.File;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional(readOnly = true)
public interface FileUploadService<T extends BaseFileUpload> {

    T saveFile(MultipartFile multipartFile, FileUpload fileUpload);

    File getFileByFileFullName(String fullFileName);

    File getFileByFileName(String fileName);

    File getFileByOwnerId(Long ownerId);

    AgencyFileUpload getFileUploadByOwnerId(Long ownerId);

    String getFilePath();

    void deleteImageByOwnerId(Long accountId);

    T setOwner(Long targetId,FileUpload fileUpload);

}
