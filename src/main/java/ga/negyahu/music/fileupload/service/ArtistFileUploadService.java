package ga.negyahu.music.fileupload.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.artist.entity.Artist;
import ga.negyahu.music.exception.FileNotFoundException;
import ga.negyahu.music.exception.FileUploadException;
import ga.negyahu.music.fileupload.entity.AccountFileUpload;
import ga.negyahu.music.fileupload.entity.AgencyFileUpload;
import ga.negyahu.music.fileupload.entity.ArtistFileUpload;
import ga.negyahu.music.fileupload.entity.BaseFileUpload;
import ga.negyahu.music.fileupload.entity.FileUpload;
import ga.negyahu.music.fileupload.repository.ArtistFileUploadRepository;
import ga.negyahu.music.fileupload.util.FileUploadUtil;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service("artistFileUploadService")
public class ArtistFileUploadService implements FileUploadService<ArtistFileUpload> {

    private final ArtistFileUploadRepository artistFileUploadRepository;
    private final FileUploadUtil uploadUtil;
    private final String filePath;

    public ArtistFileUploadService(FileUploadUtil uploadUtil,
        ArtistFileUploadRepository uploadRepository,
        @Value("${upload.artist.path:#{null}}") String filePath) throws IOException {
        this.artistFileUploadRepository = uploadRepository;
        if (filePath == null) {
            String targetDirectory = "upload/artists/";
            ClassPathResource target = new ClassPathResource("");
            String resourcePath = target.getURI().getPath();
            filePath = resourcePath + targetDirectory;
        }
        this.uploadUtil = uploadUtil;
        this.filePath = filePath;
    }

    @Override
    public ArtistFileUpload saveFile(MultipartFile multipartFile, FileUpload fileUpload) {
        try {
            ArtistFileUpload image = new ArtistFileUpload();
            BaseFileUpload base = new BaseFileUpload(multipartFile, this.filePath);
            image.setBaseFileUpload(base);
            image.createFileName();
            ArtistFileUpload save = this.artistFileUploadRepository.save(image);
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
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        return file;
    }

    @Override
    public File getFileByOwnerId(Long ownerId) {
        return null;
    }

    @Override
    public AgencyFileUpload getFileUploadByOwnerId(Long ownerId) {
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
    public ArtistFileUpload setOwner(Long targetId, FileUpload fileUpload) {
        ArtistFileUpload upload = this.artistFileUploadRepository.findById(targetId).get();
        if (upload == null) {
            return null;
        }
        upload.setArtist((Artist) fileUpload.getEntity());
        return upload;
    }

}
