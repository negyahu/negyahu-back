package ga.negyahu.music.agency.repository;

import ga.negyahu.music.agency.entity.AgencyMember;
import java.util.List;
import java.util.Set;

public interface AgencyMemberDao {

    List<AgencyMember> saveAllWithHandling(Set<AgencyMember> agencyMembers);

}
