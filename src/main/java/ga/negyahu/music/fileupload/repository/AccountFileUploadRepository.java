package ga.negyahu.music.fileupload.repository;

import ga.negyahu.music.fileupload.entity.AccountFileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AccountFileUploadRepository extends JpaRepository<AccountFileUpload, Long> {

    @Transactional(readOnly = true)
    AccountFileUpload findFirstByAccount_Id(Long acountId);

}
