package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.ArtistMemberUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistMemberUploadRepository extends JpaRepository<ArtistMemberUpload, Long> {

}
