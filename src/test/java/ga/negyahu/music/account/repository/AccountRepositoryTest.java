package ga.negyahu.music.account.repository;

import static org.junit.jupiter.api.Assertions.*;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;

@DataJpaTest
//@Import(DataJpaTestConfig.class)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TestUtils testUtils;

    @Description("Where절 추가, Account의 State가 Delete라면 조회하지 않는다.")
    @Test
    public void 계정의상태_조건추가(){
        // given
        Account account = TestUtils.createDefaultAccount();
        Account save = this.accountRepository.save(account);

        // when : 조회하는 계정 상태는 활성화
        assertThrows(NoSuchElementException.class, () -> {
            this.accountRepository.findFirstByIdAndStateIsNot(save.getId(), State.ACTIVE).get();
        },"현재 활성화인 계정을 제외하고 조회하기 때문에, 아무것도 찾지 못한다.");

        // then
        Account find = this.accountRepository.findFirstByIdAndStateIsNot(save.getId(), State.DELETED)
            .get();

        assertNotNull(find,"조회성공");
    }

    @Test
    public void 계정_삭제(){
        Account account = TestUtils.createDefaultAccount();
        this.re
    }

}