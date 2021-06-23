package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.AgencyUpload;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AgencyUploadRepository extends JpaRepository<AgencyUpload, Long> {

    Optional<AgencyUpload> findFirstByAgencyId(Long agencyId);

}
