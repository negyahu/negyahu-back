package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.entity.ArtistMember;
import ga.negyahu.music.artist.repository.ArtistMemberRepository;
import ga.negyahu.music.exception.ArtistNotFoundException;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.fileupload.entity.AgencyUpload;
import ga.negyahu.music.fileupload.entity.ArtistMemberUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.fileupload.repository.ArtistMemberUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtils;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("artistMemverUploadService")
public class ArtistMemberUploadService implements FileUploadService<ArtistMemberUpload> {

    private final ArtistMemberUploadRepository uploadRepository;
    private final ArtistMemberRepository artistMemberRepository;
    private final String filePath;
    public static final String TYPE = "members";


    public ArtistMemberUploadService(ArtistMemberUploadRepository uploadRepository,
        ArtistMemberRepository artistMemberRepository,
        @Value("${upload.path:#{null}}") String filePath) throws IOException {
        this.uploadRepository = uploadRepository;
        this.artistMemberRepository = artistMemberRepository;
        this.filePath = createDefaultPath(TYPE, filePath);
    }

    @Override
    public ArtistMemberUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload, Account account) {
        ArtistMember member = (ArtistMember) fileUpload.getEntity();
        ArtistMember artistMember = artistMemberRepository.findFirstById(member.getId())
            .orElseThrow(() -> {
                throw new ArtistNotFoundException();
            });

        File targetFile = null;
        try {
            ArtistMemberUpload file = new ArtistMemberUpload(multipartFile, this.filePath, TYPE);
            targetFile = new File(file.getFullFilePath());
            multipartFile.transferTo(targetFile);
            return uploadRepository.save(file);
        } catch (IOException e) {
            // IOException 이 발생했다면 파일은 저장된 것이 아니기 때문에 예외처리로 넘어간다.
            throw new FileUploadException();
        } catch (RuntimeException e) {
            // save 하는 과정에서 예외가 발생할 경우 저장했던 파일을 삭제한 후, 예외를 던진다.
            targetFile.delete();
            throw e;
        }
    }

    @Override
    public File getFileByFileFullName(String fullFileName) {
        return null;
    }

    @Override
    public File getFileByFileName(String fileName) {
        return null;
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
        return null;
    }

    @Override
    public void deleteImageByOwnerId(Long accountId) {

    }

    @Override
    public ArtistMemberUpload setOwner(Long targetId, FileUpload fileUpload) {
        return null;
    }
}
