package ga.negyahu.music.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class AccountTest {

    @Autowired
    private AccountRepository accountRepository;

    @Description("")
    @Test
    public void 계정_생성_테스트(){

        Account account = TestUtils.createDefaultAccount();

        Account save = this.accountRepository.save(account);

        assertNotNull(save.getId());
        assertEquals(Role.USER, save.getRole());
        assertEquals(null,save.getArea());
        assertEquals(account.getAddress(),save.getAddress());
        assertEquals(account.getAddress(),save.getAddress());
        assertEquals(false,save.isMemberShip());
    }

}