package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.exception.FileNotFoundException;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.fileupload.entity.AgencyFileUpload;
import ga.negyahu.music.fileupload.entity.FileType;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.fileupload.repository.AgencyFileUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("agencyFileUploadService")
@Transactional
public class AgencyFileUploadService implements FileUploadService<AgencyFileUpload>,
    InitializingBean {

    private final FileUploadUtil uploadUtil;
    private final AgencyFileUploadRepository uploadRepository;
    private final String filePath;

    public String getFilePath() {
        return this.filePath;
    }

    public AgencyFileUploadService(FileUploadUtil uploadUtil,
        AgencyFileUploadRepository uploadRepository,
        @Value("${upload.account.path:#{null}}") String filePath) throws IOException {
        this.uploadRepository = uploadRepository;
        if (filePath == null) {
            String targetDirectory = "upload/agency/";
            ClassPathResource target = new ClassPathResource("");
            String resourcePath = target.getURI().getPath();
            filePath = resourcePath + targetDirectory;
        }
        this.uploadUtil = uploadUtil;
        this.filePath = filePath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        File targetDirectory = new File(this.filePath);
        if (!targetDirectory.exists()) {
            targetDirectory.mkdirs();
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public AgencyFileUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload) {

        AgencyFileUpload upload = new AgencyFileUpload();
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
        AgencyFileUpload fileUpload = getFileUploadByOwnerId(ownerId);
        File file = new File(fileUpload.getFullFilePath());
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    @Override
    public AgencyFileUpload getFileUploadByOwnerId(Long ownerId) {
        return this.uploadRepository.findFirstByAgencyId(ownerId)
            .orElseThrow(() -> {
                throw new FileNotFoundException();
            });
    }

    @Override
    public void deleteImageByOwnerId(Long accountId) {

    }

    @Override
    public AgencyFileUpload setOwner(Long targetId, FileUpload fileUpload) {
        Agency agency = (Agency) fileUpload.getEntity();
        try {
            AgencyFileUpload upload = uploadRepository.findById(targetId).get();
            upload.setAgency(agency);
            return upload;
        } catch (Exception e) {
            return null;
        }
    }

}
