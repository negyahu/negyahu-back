package ga.negyahu.music.agency.repository;

import static org.junit.jupiter.api.Assertions.*;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class AgencyMemberRepositoryTest {

    @Autowired
    private AgencyMemberRepository repository;
    @Autowired
    private TestUtils testUtils;
    @Test
    public void test(){
        Account account = TestUtils.createDefaultAccount();
        Account save = this.testUtils.signUpAccount(account);


        AgencyMember byAgencyAndAccountId = repository.findByAgencyAndAccountId(1L, 2L);
    }

}