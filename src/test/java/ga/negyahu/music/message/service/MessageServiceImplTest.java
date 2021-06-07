package ga.negyahu.music.message.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.message.Message;
import ga.negyahu.music.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class MessageServiceImplTest {

    private List<Account> accounts;
    private List<Message> messages;

    @BeforeEach
    public void init() {
        Long seq = 1L;
        this.accounts = TestUtils.createAccounts(2);

        int loopSize = accounts.size();
        for (int i = 0; i < loopSize; i++) {
            accounts.get(i).setId(seq);
            seq++;
        }

        this.messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = TestUtils
                .createMessage(UUID.randomUUID().toString(), accounts.get(0), accounts.get(1));
            message.setId(seq);
            seq++;
            this.messages.add(message);
        }

        for (int i = 0; i < 5; i++) {
            Message message = TestUtils
                .createMessage(UUID.randomUUID().toString(), accounts.get(1), accounts.get(0));
            message.setId(seq);
            seq++;
            this.messages.add(message);
        }
    }

    @Test
    public void test1(){

    }

}