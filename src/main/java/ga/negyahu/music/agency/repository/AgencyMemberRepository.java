package ga.negyahu.music.agency.repository;

import ga.negyahu.music.agency.entity.AgencyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgencyMemberRepository extends JpaRepository<AgencyMember, Long> {

}
