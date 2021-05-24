package ga.negyahu.music.area.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.area.Area;
import ga.negyahu.music.area.AreaRepository;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class AreaServiceImplTest {

    private AreaService areaService;
    private List<Account> accounts;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        this.areaService = new AreaServiceImpl(areaRepository, modelMapper);
        this.accounts = TestUtils.createAccounts(10);
        for (Account account : accounts) {
            testUtils.signUpAccount(account);
        }
    }

    @Test
    public void 지역삭제_테스트() {
        String areaName = "서울";
        Area area = Area.builder().name(areaName).build();
        area.addAccounts(accounts);
        areaRepository.save(area);
        em.flush();
        em.clear();

        Area find = areaRepository.findById(area.getId()).get();

        // when : 등록된 지역을 삭제함
        this.areaService.delete(find.getId());

        // then : 계정에서 기존에 등록되어있던 지역 정보가 모두 null로 바뀜, 검색 불가
        List<Long> ids = this.accounts.stream()
            .map(a -> a.getId())
            .collect(Collectors.toList());

        List findAccounts = em
            .createQuery("select a from Account as a join fetch a.area where a.id in :ids")
            .setParameter("ids", ids).getResultList();
        assertEquals(0, findAccounts.size());
    }

}