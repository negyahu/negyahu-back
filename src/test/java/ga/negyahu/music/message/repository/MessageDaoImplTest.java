package ga.negyahu.music.message.repository;

import static org.junit.jupiter.api.Assertions.*;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.message.Message;
import ga.negyahu.music.message.dto.MessageDto;
import ga.negyahu.music.message.dto.MessageSearch;
import ga.negyahu.music.message.dto.MessageType;
import ga.negyahu.music.message.service.MessageService;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class MessageDaoImplTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private TestUtils testUtils;
    private List<Account> accounts;
    private List<Message> messages;

    @BeforeEach
    public void init() {
        accounts = TestUtils.createAccounts(2);
        for (Account account : accounts) {
            this.testUtils.signUpAccount(account);
        }
        Account account1 = accounts.get(0);
        Account account2 = accounts.get(1);
        this.messages = new ArrayList<>();
        // 유저1이 보낸 메세지 10 개 + 유저1이 보내고 삭제한 메세지 5개
        for (int i = 0; i < 10; i++) {
            Message message = TestUtils.createMessage("message" + (i + 1), account1, account2);
            this.messages.add(message);
        }
        for (int i = 0; i < 5; i++) {
            Message message = TestUtils
                .createMessage("message delete by sender " + (i + 1), account1, account2);
            message.setDeletedBySender(true);
            this.messages.add(message);
        }
        // 유저2이 보낸 메세지 5 개 + 유저1이 보내고 삭제한 메세지 5개
        for (int i = 0; i < 5; i++) {
            Message message = TestUtils
                .createMessage("message delete by receiver" + (i + 1), account2, account1);
            message.setDeletedByReceiver(true);
            message.setOpened(true);
            this.messages.add(message);
        }
        for (int i = 0; i < 5; i++) {
            Message message = TestUtils.createMessage("message" + (i + 1), account2, account1);
            this.messages.add(message);
        }
        this.messageRepository.saveAll(this.messages);
    }

    @Test
    public void 테스트환경_검사(){
        //1. 모든 메세지는 25개 : 유저1 -> 유저2 = 15(5개삭제), 유저2 -> 유저1 = 10(5개삭제)
        long totalCount = this.messageRepository.count();
        assertEquals(25, totalCount);
    }

    @Test
    public void 보낸메시지_검색_테스트() {
        MessageSearch search = MessageSearch.builder()
            .accountId(this.accounts.get(0).getId())
            .type(MessageType.SEND)
            .build();
        Page<MessageDto> resultPage = messageRepository.search(search, PageRequest.of(0, 20));
        List<MessageDto> content = resultPage.getContent();
        for (MessageDto dto : content) {
            System.out.println(dto.getContent());
        }
        assertEquals(10, resultPage.getTotalElements());
    }

    @Test
    public void 받은메세지_검색_테스트() {
        int size = 10;
        int pageNumber = 0;

        MessageSearch search = MessageSearch.builder()
            .accountId(this.accounts.get(0).getId())
            .type(MessageType.RECEIVE)
            .build();
        Page<MessageDto> resultPage = messageRepository.search(search, PageRequest.of(pageNumber, size));
        List<MessageDto> content = resultPage.getContent();

        assertEquals(5, resultPage.getTotalElements());
    }

    @Test
    public void 모든메세지_검색_테스트() {
        int size = 10;
        int pageNumber = 0;

        MessageSearch search = MessageSearch.builder()
            .accountId(this.accounts.get(0).getId())
            .type(MessageType.ALL)
            .build();
        Page<MessageDto> resultPage = messageRepository.search(search, PageRequest.of(pageNumber, size));
        List<MessageDto> content = resultPage.getContent();

        // 유저1가 발수신 메세지는 총 15개
       assertEquals(15, resultPage.getTotalElements());
       assertEquals(size, content.size());
    }

}