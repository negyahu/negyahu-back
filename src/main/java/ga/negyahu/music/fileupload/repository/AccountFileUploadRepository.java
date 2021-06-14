package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.AccountFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountFileUploadRepository extends JpaRepository<AccountFileUpload, Long> {

}
