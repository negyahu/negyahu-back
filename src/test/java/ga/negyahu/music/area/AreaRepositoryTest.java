package ga.negyahu.music.area;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class AreaRepositoryTest {

    private List<Account> accounts;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private AreaRepository areaRepository;
    @PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void init(){
        // 테스트용 계정 10개 생성
        this.accounts = TestUtils.createAccounts(10);
        for (Account account : this.accounts) {
            testUtils.signUpAccount(account);
        }
    }

    @Test
    public void findAreaWithAccountsByIdTest(){
        // given : Area 한개 생성, 10개의 개정에 Area 등록
        Area area = Area.builder().name("서울").build();
        Area save = areaRepository.save(area);
        save.addAccounts(this.accounts);

        em.flush();
        em.clear();

        // when : Area의 정보로 Account를 조회
        Area findArea = areaRepository.findAreaWithAccountsById(save.getId());
        em.close();

        List<Account> findAccounts = findArea.getAccounts();

        // then : 등록된 5개의 계정이 모두 조회되어야 한다.
        Assertions.assertEquals(this.accounts.size(), findAccounts.size());
    }

}