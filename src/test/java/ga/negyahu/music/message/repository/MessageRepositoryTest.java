package ga.negyahu.music.message.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.message.Message;
import ga.negyahu.music.message.MessageResult;
import ga.negyahu.music.message.dto.MessageOnlyIds;
import ga.negyahu.music.utils.DataJpaTestConfig;
import ga.negyahu.music.utils.TestUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(DataJpaTestConfig.class)
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private TestUtils testUtils;
    @PersistenceContext
    private EntityManager em;

    int totalMessageCnt = 15;
    int boundaryCnt = 10;
    int pastMessageCnt = totalMessageCnt - boundaryCnt;

    private List<Account> accounts;
    private List<Message> messages;

    @BeforeEach
    public void init() {
        // 0 ~ boundaryCnt 0,1번 계정이 주고 받은 메세지
        // boundaryCnt ~ totalMessageCnt 1,2번 계정이 주고 받은 메세지
        // 계정 생성
        this.accounts = TestUtils.createAccounts(3);
        int loopSize = accounts.size();
        for (int i = 0; i < loopSize; i++) {
            testUtils.signUpAccount(this.accounts.get(i));
        }

        // 메세지 저장 : 0인덱스 -> 1인덱스유저 0~boundaryCnt 까지 발송
        this.messages = new ArrayList<>(totalMessageCnt);
        for (int i = 0; i < boundaryCnt; i++) {
            Message message = TestUtils
                .createMessage(UUID.randomUUID().toString(), accounts.get(0), accounts.get(1));
            message.setSendDateTime(LocalDateTime.now());
            this.messages.add(message);
        }
        // 메세지 저장 : 1인덱스 -> 2인덱스유저 boundaryCnt~totalCnt 까지 발송
        for (int i = boundaryCnt; i < totalMessageCnt; i++) {
            Message message = TestUtils
                .createMessage(UUID.randomUUID().toString(), accounts.get(1), accounts.get(2));
            this.messages.add(message);
            message.setSendDateTime(LocalDateTime.now().minusDays(91));
        }
        this.messageRepository.saveAll(this.messages);
    }

    @Test
    public void 수신_발신_메세지_조회() {
        // given
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        int expectedSize = boundaryCnt;
        // when
        List<Message> sentMessages = this.messageRepository.findAllSentMessage(sender.getId());
        System.out.println("======");
        sentMessages.stream()
            .forEach(m -> {
                System.out.println(m.getSender().getId());
            });
        // then
        assertEquals(expectedSize, sentMessages.size()
            , "0인인덱스 유저는 0~boundaryCnt의 메세지의 메세지를 발송했다.");

        // when2
        List<Message> receivedMessage = this.messageRepository
            .findAllReceivedMessage(receiver.getId());

        // then2
        assertEquals(expectedSize, receivedMessage.size()
            , "1인덱스 유저는 0번 인덱스 유저에게 0~boundaryCnt개의 메세지를 전송 받았다.");
    }

    @Test
    public void 논리_삭제된_메세지_조회불가() {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        this.messages.stream()
            .filter(m -> m.getSender().getId().equals(sender.getId()))
            .forEach(m -> m.setDeletedBySender(true));

        List<Message> sentMessages = this.messageRepository.findAllSentMessage(sender.getId());
        assertEquals(0, sentMessages.size(), "전부 논리삭제되었기 때문에 0개를 반환한다.");

        List<Message> receivedMessages = this.messageRepository
            .findAllReceivedMessage(receiver.getId());
        assertEquals(boundaryCnt, receivedMessages.size(), "수신자는 메세지를 조회할 수 있다.");

        List<Message> allMessages = this.messageRepository.findAll();
        assertEquals(totalMessageCnt, allMessages.size(), "총메세지 개수는 변화하지 않는다.");
    }

    @Test
    public void 발송N일_지난_메세지삭제() {
        // given : boundaryCnt개의 메세지가 targetDate 이전에 생성
        int expected = totalMessageCnt - boundaryCnt;
        int numberOfDays = 89;
        LocalDateTime targetDate = LocalDateTime.now().minusDays(numberOfDays);

        for (int i = 0; i < this.boundaryCnt; i++) {
            this.messages.get(i).setSendDateTime(targetDate);
        }

        // when : targetDate 이전에 작성된 댓글을 물리삭제
        int cnt = this.messageRepository.deleteAllBySendDateTimeBefore(targetDate);

        long totalCnt = this.messageRepository.count();
        assertEquals(expected, totalCnt, "boundaryCnt 만큼이 삭제된다. ");
    }
}