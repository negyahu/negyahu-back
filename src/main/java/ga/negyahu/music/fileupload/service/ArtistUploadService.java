package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.artist.repository.ArtistMemberRepository;
import ga.negyahu.music.exception.FileNotFoundException;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.entity.ArtistUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.fileupload.repository.AccountUploadRepository;
import ga.negyahu.music.fileupload.repository.ArtistFileUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtils;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service("artistFileUploadService")
public class ArtistUploadService implements FileUploadService<ArtistUpload> {

    private static final String TYPE = "artists";

    private final ArtistFileUploadRepository artistFileUploadRepository;
    private final String filePath;
    private final AccountUploadRepository accountFileUploadRepository;
    private final AccountRepository accountRepository;
    private final AccountFileUploadService accountFileUploadService;
    private final ArtistMemberRepository artistMemberRepository;

    public ArtistUploadService(ArtistFileUploadRepository uploadRepository,
        @Value("${upload.path:#{null}}") String filePath,
        AccountUploadRepository accountFileUploadRepository,
        AccountRepository accountRepository,
        AccountFileUploadService accountFileUploadService,
        ArtistMemberRepository artistMemberRepository) throws IOException {
        this.artistFileUploadRepository = uploadRepository;
        this.accountFileUploadRepository = accountFileUploadRepository;
        this.accountRepository = accountRepository;
        this.accountFileUploadService = accountFileUploadService;
        this.artistMemberRepository = artistMemberRepository;
        this.filePath = createDefaultPath("artist", filePath);
    }

    @Transactional
    @Override
    public ArtistUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload,
        Account account) {
        try {
            ArtistUpload image = new ArtistUpload(multipartFile, filePath, TYPE);
            ArtistUpload save = this.artistFileUploadRepository.save(image);
            String fullFilePath = save.getFullFilePath();
            multipartFile.transferTo(new File(fullFilePath));
            return save;
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }

    @Override
    public File getFileByFileFullName(String fullFileName) {
        File file = new File(fullFileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    @Override
    public File getFileByFileName(String fileName) {
        File file = new File(this.filePath + "/" + fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }

    @Override
    public File getFileByOwnerId(Long ownerId) {
        return null;
    }

    @Override
    public AgencyUpload getFileUploadByOwnerId(Long ownerId) {
        return null;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void deleteImageByOwnerId(Long accountId) {

    }

    @Override
    public ArtistUpload setOwner(Long targetId, FileUpload fileUpload) {
        ArtistUpload upload = null;
        // 사진이 없더라도 기존의 트랜잭션에 롤백을 시키지 않는다.
        try {
            upload = this.artistFileUploadRepository.findById(targetId).get();
        } catch (Exception e) {

        }
        if (upload == null) {
            return null;
        }
        upload.setArtist((Artist) fileUpload.getEntity());
        return upload;
    }

}
