package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.AgencyFileUpload;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AgencyFileUploadRepository extends JpaRepository<AgencyFileUpload, Long> {

    Optional<AgencyFileUpload> findFirstByAgencyId(Long agencyId);

}
