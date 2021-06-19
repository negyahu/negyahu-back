package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import java.io.File;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
public interface FileUploadService {

    BaseFileUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload);

    File getFileByFileFullName(String fullFileName);

    File getFileByFileName(String fileName);

    File getFileByOwnerId(Long accountId);

    String getFilePath();
}
