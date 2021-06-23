package ga.negyahu.music.account.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TestUtils testUtils;
    @PersistenceContext
    private EntityManager em;

    @Description("Where절 추가, Account의 State가 Delete라면 조회하지 않는다.")
    @Test
    public void 계정의상태_조건추가() {
        // given
        Account account = TestUtils.createDefaultAccount();
        Account save = this.accountRepository.save(account);

        // when : 조회하는 계정 상태는 WAIT (이메일 인증 대기)
        assertThrows(NoSuchElementException.class, () -> {
            this.accountRepository.findFirstByIdAndStateIsNot(save.getId(), State.WAIT).get();
        }, "현재 활성화인 계정을 제외하고 조회하기 때문에, 아무것도 찾지 못한다.");

        // then
        Account find = this.accountRepository
            .findFirstByIdAndStateIsNot(save.getId(), State.DELETED)
            .get();

        assertNotNull(find, "조회성공");
    }

    @Test
    public void 계정상태_변경() {
        // given
        Account account = TestUtils.createDefaultAccount();
        Account save = this.accountRepository.save(account);

        assertEquals(State.WAIT, save.getState());
        // when : 상태를 변경
        this.accountRepository.modifyState(save.getId(), State.DELETED);

        // then : 상태 활성 -> 삭제로 변경
        Account find = this.accountRepository.findFirstByEmail(account.getEmail()).get();
        assertEquals(State.DELETED, find.getState(), "State active -> delete");
    }

    @Description("query 확인")
    @Disabled
    @Test
    public void 해당되는_모든_이메일의_계정검색() {
        String[] emails = new String[]{"aaaa", "bbbb"};
        List<Account> allByEmailIn = this.accountRepository.findAllByEmailIn(emails);
    }

}