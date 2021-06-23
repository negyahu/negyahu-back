package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.fileupload.entity.AccountUpload;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.fileupload.repository.AccountUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("accountFileUploadService")
@Transactional
public class AccountFileUploadService implements FileUploadService<AccountUpload>,
    InitializingBean {

    private final FileUploadUtil uploadUtil;
    private final AccountUploadRepository uploadRepository;
    private final String filePath;
    public static final String TYPE = "accounts";

    public String getFilePath() {
        return this.filePath;
    }

    public AccountFileUploadService(FileUploadUtil uploadUtil,
        AccountUploadRepository uploadRepository,
        @Value("${upload.path:#{null}}") String filePath) throws IOException {
        this.uploadRepository = uploadRepository;
        if (filePath == null) {
            String targetDirectory = "upload/accounts/";
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

    // 파일을 등록할 때, IOException 이 발생하면 save한 AccountFileUpload 도 취소해야한다.
    // Spring Data 는 RuntimeException 만 RollBack하기 때문에 따로 Exception 에 대한 롤백을 지정해야한다.
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public AccountUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload,
        Account account) {
        Account entity = (Account) fileUpload.getEntity();
        boolean deleteFlag = false;
        File newFile = null;
        File beforeFile = null;
        if (!entity.getId().equals(account.getId())) {
            throw new AccessDeniedException("[ERROR] 권한이 없습니다.");
        }
        try {
            AccountUpload upload = existImage(account.getId());
            if (upload != null) {
                deleteFlag = true;
                beforeFile = new File(upload.getFullFilePath());
                upload.setNewMultipartFile(multipartFile);
            } else {
                upload = new AccountUpload(multipartFile, this.filePath, TYPE);
                upload.setAccount(entity);
            }
            AccountUpload save = uploadRepository.saveAndFlush(upload);
            newFile = new File(upload.getFullFilePath());
            uploadRepository.updateFKOfAccount(save.getId(), entity.getId());
            multipartFile.transferTo(newFile);
            if (deleteFlag) {
                beforeFile.delete();
            }
            return upload;
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }

    private AccountUpload existImage(Long accountId) {
        return this.uploadRepository.findFirstByAccountId(accountId);
    }

    @Override
    public File getFileByFileFullName(String fullFileName) {
        File file = new File(fullFileName);
        if (!file.exists()) {
            throw new RuntimeException();
        }
        return file;
    }

    @Override
    public File getFileByFileName(String fileName) {
        return getFileByFileFullName(this.filePath + fileName);
    }

    @Override
    public File getFileByOwnerId(Long ownerId) {
        AccountUpload file = this.uploadRepository
            .findFirstByAccountId(ownerId);
        String fullFilePath = file.getFullFilePath();
        return getFileByFileFullName(fullFilePath);
    }

    @Override
    public AgencyUpload getFileUploadByOwnerId(Long ownerId) {
        return null;
    }

    @Override
    public void deleteImageByOwnerId(Long accountId) {
        AccountUpload file = this.uploadRepository.findFirstByAccountId(accountId);
        File target = new File(file.getFullFilePath());
        boolean result = target.exists();
        if (result) {
            this.uploadRepository.delete(file);
        }
    }

    @Override
    public AccountUpload setOwner(Long targetId, FileUpload fileUpload) {
        return null;
    }

}
