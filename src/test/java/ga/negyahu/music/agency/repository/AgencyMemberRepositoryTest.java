package ga.negyahu.music.agency.repository;

import static ga.negyahu.music.agency.entity.QAgencyMember.agencyMember;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.entity.AgencyMember;
import ga.negyahu.music.agency.entity.AgencyRole;
import ga.negyahu.music.agency.entity.QAgency;
import ga.negyahu.music.utils.AgencyTestUtils;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class AgencyMemberRepositoryTest {

    @Autowired
    AgencyMemberRepository repository;
    @Autowired
    AgencyRepository agencyRepository;
    @Autowired
    TestUtils testUtils;
    @PersistenceContext
    EntityManager em;
    private Agency agency;
    Account account;

    List<AgencyMember> members;
    @Autowired
    JPAQueryFactory query;

    @BeforeEach
    public void init(){
        System.out.println("========");
        Account account = TestUtils.createDefaultAccount();
        account.setRole(Role.AGENCY);
        this.account = this.testUtils.signUpAccount(account);
        Agency agency = AgencyTestUtils.createDefaultAgency(account);
        this.agency = agencyRepository.save(agency);

        List<Account> accounts = TestUtils.createAccounts(5);
        this.members = new ArrayList<>(accounts.size());
        for (Account a : accounts ){
            Account save = this.testUtils.signUpAccount(a);
            AgencyMember agencyMember = AgencyTestUtils.createAgencyMember(this.agency, save);
            agencyMember.setAgency(this.agency);

            members.add(agencyMember);
        }
        this.repository.saveAll(members);
        em.flush();
        em.clear();
        em.close();
    }

    @Test
    public void test(){

        AgencyMember member = this.repository.findById(this.members.get(0).getId()).get();

    }

}