package ga.negyahu.music.agency.repository;

import ga.negyahu.music.agency.entity.AgencyMember;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgencyMemberDao {

    List<AgencyMember> saveAllWithHandling(Set<AgencyMember> agencyMembers);

    Page<AgencyMember> findAllByBossIdAsPage(Long bossId, Pageable pageable);


}
