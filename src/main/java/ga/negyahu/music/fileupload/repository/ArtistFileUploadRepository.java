package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.ArtistUpload;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ArtistFileUploadRepository extends JpaRepository<ArtistUpload, Long> {

    @Transactional(readOnly = true)
    ArtistUpload findFirstByArtistId(Long artistId);

    List<ArtistUpload> findAllByIdIn(Long[] ids);
}
