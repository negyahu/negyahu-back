package ga.negyahu.music.agency.repository;

import static ga.negyahu.music.account.QAccount.*;
import static ga.negyahu.music.agency.entity.QAgency.*;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.account.QAccount;
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
            .select(new QAgencyDto(agency.id, agency.name, agency.nameEN, agency.businessNumber,
                agency.bossName, agency.mobile, agency.state, agency.signUpDate,
                agency.account.email))
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
        if (search == null || Objects.isNull(search.getType()) || Objects
            .isNull(search.getKeyword())) {
            return null;
        }

        String type = search.getType();
        type = type.toLowerCase(Locale.ROOT);
        switch (type) {
            case "name":
                return builder.and(agency.name.contains(search.getKeyword()));
            case "nameEn":
                return builder.and(agency.nameEN.contains(search.getKeyword()));
            case "bossName":
                return builder.and(agency.bossName.contains(search.getKeyword()));
            case "email":
                return builder.and(agency.account.email.contains(search.getKeyword()));
            case "businessNumber":
                return builder.and(agency.businessNumber.contains(search.getKeyword()));
            case "state":
                String keyword = search.getKeyword();
                State state = State.valueOf(keyword);
                return builder.and(agency.state.eq(state));
            case "id":
                return builder.and(agency.id.eq(Long.parseLong(search.getKeyword())));
            default:
                return null;
        }

    }

}
