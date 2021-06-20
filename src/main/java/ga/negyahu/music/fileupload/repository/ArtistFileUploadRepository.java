package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.AccountFileUpload;
import ga.negyahu.music.fileupload.entity.ArtistFileUpload;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ArtistFileUploadRepository extends JpaRepository<ArtistFileUpload, Long> {

    @Transactional(readOnly = true)
    ArtistFileUpload findFirstByArtistId(Long artistId);

    List<ArtistFileUpload> findAllByIdIn(Long[] ids);
}
