package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.exception.FileNotFoundException;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.entity.FileType;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.fileupload.repository.AgencyUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("licenseUploadService")
@Transactional
public class LicenseUploadService implements FileUploadService<AgencyUpload> {

    private final FileUploadUtil uploadUtil;
    private final AgencyUploadRepository uploadRepository;
    private final String filePath;
    public static final String TYPE = "licenses";

    public String getFilePath() {
        return this.filePath;
    }

    public LicenseUploadService(FileUploadUtil uploadUtil,
        AgencyUploadRepository uploadRepository,
        @Value("${upload.path:#{null}}") String filePath) throws IOException {

        this.uploadRepository = uploadRepository;
        this.uploadUtil = uploadUtil;
        this.filePath = createDefaultPath(TYPE, filePath);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public AgencyUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload,
        Account account) {

        AgencyUpload upload = new AgencyUpload(multipartFile, this.filePath, TYPE);
        upload.setNewMultipartFile(multipartFile);

        if (fileUpload == null) {
            upload.setFileType(FileType.BUSINESS);
        } else {
            Agency agency = (Agency) fileUpload.getEntity();
            upload.setAgency(agency);
        }

        try {
            String path = upload.getFullFilePath();
            File file = new File(path);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileUploadException();
        }

        return this.uploadRepository.save(upload);
    }

    @Override
    public File getFileByFileFullName(String fullFileName) {
        return null;
    }

    @Override
    public File getFileByFileName(String fileName) {
        return getFileByFileFullName(this.filePath + fileName);
    }

    @Override
    public File getFileByOwnerId(Long ownerId) {
        AgencyUpload fileUpload = getFileUploadByOwnerId(ownerId);
        File file = new File(fileUpload.getFullFilePath());
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    @Override
    public AgencyUpload getFileUploadByOwnerId(Long ownerId) {
        return this.uploadRepository.findFirstByAgencyId(ownerId)
            .orElseThrow(() -> {
                throw new FileNotFoundException();
            });
    }

    @Override
    public void deleteImageByOwnerId(Long accountId) {

    }

    @Override
    public AgencyUpload setOwner(Long targetId, FileUpload fileUpload) {
        Agency agency = (Agency) fileUpload.getEntity();
        try {
            AgencyUpload upload = uploadRepository.findById(targetId).get();
            upload.setAgency(agency);
            return upload;
        } catch (Exception e) {
            return null;
        }
    }

}
