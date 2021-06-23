package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.AccountUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AccountUploadRepository extends JpaRepository<AccountUpload, Long>, AccountUploadDao {

    AccountUpload findFirstByAccountId(Long accountId);

}
