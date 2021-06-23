package ga.negyahu.music.agency.repository;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.entity.Agency;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgencyRepository extends JpaRepository<Agency, Long>, AgencyDao {

    @EntityGraph(attributePaths = "account")
    Optional<Agency> findById(Long id);

    @EntityGraph(attributePaths = {"account"})
    Optional<Agency> findByIdAndAccount(Long id, Account account);



}
