package ga.negyahu.music.account.repository;

import ga.negyahu.music.account.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findFirstByEmail(String email);

    boolean existsByEmail(String email);

    Account findFirstByEmailOrNickname(String email, String nickname);

}
