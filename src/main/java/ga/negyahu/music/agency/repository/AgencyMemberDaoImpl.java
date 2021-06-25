package ga.negyahu.music.agency.repository;

import static ga.negyahu.music.agency.entity.QAgency.*;
import static ga.negyahu.music.agency.entity.QAgencyMember.*;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.agency.entity.AgencyMember;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgencyMemberDaoImpl implements AgencyMemberDao {

    private final JPAQueryFactory query;

    @Override
    public List<AgencyMember> saveAllWithHandling(Set<AgencyMember> agencyMembers) {

        return null;
    }

    @Override
    public Page<AgencyMember> findAllByBossIdAsPage(Long bossId, Pageable pageable) {

        QueryResults<AgencyMember> results = query
            .selectFrom(agencyMember)
            .join(agencyMember.agency, agency).fetchJoin()
            .where(agencyMember.agency.account.id.eq(bossId))
            .fetchResults();

        return new PageImpl<>(results.getResults(),pageable,results.getTotal());
    }
}
