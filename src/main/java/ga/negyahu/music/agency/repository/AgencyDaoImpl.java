package ga.negyahu.music.agency.repository;

import static ga.negyahu.music.account.QAccount.account;
import static ga.negyahu.music.agency.entity.QAgency.agency;
import static java.util.Objects.isNull;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.agency.dto.QAgencyDto;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AgencyDaoImpl implements AgencyDao {

    private final JPAQueryFactory query;

    @Override
    public Page<AgencyDto> searchAgency(AgencySearch search, Pageable pageable) {

        QueryResults<AgencyDto> results = query
            .select(
                new QAgencyDto(agency.id, agency.nameKR, agency.nameEN, agency.businessNumber,
                    agency.bossName, agency.mobile, agency.state, agency.signUpDate))
            .from(agency)
            .join(agency.account, account)
            .where(addSearchQuery(search))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetchResults();
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    private BooleanBuilder addSearchQuery(AgencySearch search) {
        BooleanBuilder builder = new BooleanBuilder();
        if (isNull(search)) {
            return null;
        }

        String type = search.getType();
        String keyword = search.getKeyword() == null ? null : "";
        switch (type) {
            case "nameKR":
                return builder.and(agency.nameKR.contains(keyword));
            case "nameEn":
                return builder.and(agency.nameEN.contains(keyword));
            case "bossName":
                return builder.and(agency.bossName.contains(keyword));
            case "email":
                return builder.and(agency.account.email.contains(keyword));
            case "businessNumber":
                return builder.and(agency.businessNumber.contains(keyword));
            case "state":
                State state = State.getState(keyword);
                return builder.and(agency.state.eq(state));
            case "id":
                return builder.and(agency.id.eq(Long.parseLong(keyword)));
            default:
                return null;
        }
    }

}
