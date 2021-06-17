package ga.negyahu.music.account.repository;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
@Where(clause = "state = active")
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findById(Long id);

    Optional<Account> findFirstByIdAndStateIsNot(Long id, State state);

    Optional<Account> findFirstByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying(clearAutomatically = true)
    @Query("update Account as a set a.state = :state where a.id = :id")
    void modifyState(@Param("id") Long id, @Param("state") State newState);

    List<Account> findAllByEmailIn(String[] emails);

}
