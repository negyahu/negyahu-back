package ga.negyahu.music.agency.repository;

import static ga.negyahu.music.agency.entity.QAgencyMember.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.agency.entity.QAgencyMember;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgencyMemberDaoImpl implements AgencyMemberDao {

    private final JPAQueryFactory query;

    @Override
    public List<AgencyMember> saveAllWithHandling(Set<AgencyMember> agencyMembers) {

        return null;
    }
}
