package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.fileupload.entity.AccountFileUpload;
import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.fileupload.repository.AccountFileUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("accountFileUploadService")
@Transactional
public class AccountFileUploadService implements FileUploadService, InitializingBean {

    private final FileUploadUtil uploadUtil;
    private final AccountFileUploadRepository uploadRepository;
    private final String filePath;

    public String getFilePath() {
        return this.filePath;
    }

    public AccountFileUploadService(FileUploadUtil uploadUtil,
        AccountFileUploadRepository uploadRepository,
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
    public BaseFileUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload) {

        try {
            BaseFileUpload baseFileUpload = new BaseFileUpload(multipartFile, this.filePath);
            Account account = (Account) fileUpload.getEntity();
            AccountFileUpload accountFileUpload = new AccountFileUpload();
            accountFileUpload.setAccount(account);
            accountFileUpload.setBaseFileUpload(baseFileUpload);
            uploadRepository.save(accountFileUpload);

            File file = new File(accountFileUpload.getFullFilePath());
            multipartFile.transferTo(file);
            return accountFileUpload;
        } catch (IOException e) {
            throw new FileUploadException("[ERROR] 파일 업로드에 실패했습니다.");
        }
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
    public File getFileByAccountId(Long accountId) {
        AccountFileUpload file = this.uploadRepository
            .findFirstByAccount_Id(accountId);
        String fullFilePath = file.getFullFilePath();
        return getFileByFileFullName(fullFilePath);
    }

}
