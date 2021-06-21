package ga.negyahu.music.agency.repository;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.dto.AgencyDto;
import ga.negyahu.music.agency.dto.AgencySearch;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.utils.AgencyTestUtils;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class AgencyRepositoryTest {

    @Autowired
    private AgencyRepository repository;
    @Autowired
    private TestUtils testUtils;

    private Account account;
    private List<Agency> agencies;
    @BeforeEach
    public void init() {

        Account account = TestUtils.createDefaultAccount();
        this.account = this.testUtils.signUpAccount(account);

        Agency agency = AgencyTestUtils.createDefaultAgency(this.account);
        Agency save = this.repository.save(agency);
        this.agencies = new ArrayList<>();
        this.agencies.add(save);

    }

    @Test
    public void 소속사_검색_테스트() {

        Page<AgencyDto> agencyDtos = repository
            .searchAgency(AgencySearch.builder().type("email").keyword(this.account.getEmail()).build(),
                PageRequest.of(0, 10));

        List<AgencyDto> content = agencyDtos.getContent();
        AgencyDto dto = content.get(0);

        Agency agency = this.agencies.get(0);
        Assertions.assertEquals(agency.getId(),dto.getId());
    }

}