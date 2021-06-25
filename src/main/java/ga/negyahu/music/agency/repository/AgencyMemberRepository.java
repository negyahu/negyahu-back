package ga.negyahu.music.agency.repository;

import ga.negyahu.music.agency.entity.AgencyMember;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgencyMemberRepository extends JpaRepository<AgencyMember, Long>, AgencyMemberDao {

    @Query("select am from AgencyMember am where am.agency.id = :agencyId and am.account.id = :accountId")
    AgencyMember findByAgencyAndAccountId(Long agencyId, Long accountId);

    @EntityGraph(attributePaths = {"account"})
    List<AgencyMember> findAllByAgencyId(Long agencyId, Pageable pageable);

    boolean existsByAgency_IdAndAccount_Id(Long agencyId, Long accountId);

}
