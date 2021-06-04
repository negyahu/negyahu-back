package ga.negyahu.music.utils;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.message.Message;
import ga.negyahu.music.message.repository.MessageRepository;
import java.util.ArrayList;
import java.util.List;


public class MessageTestUtils {

    private final MessageRepository messageRepository;

    public MessageTestUtils(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> init(Account account1, Account account2) {

        List<Message> messages = new ArrayList<>();
        // 유저1이 보낸 메세지 10 개 + 유저1이 보내고 삭제한 메세지 5개
        for (int i = 0; i < 10; i++) {
            Message message = TestUtils.createMessage("message" + (i + 1), account1, account2);
            messages.add(message);
        }
        for (int i = 0; i < 5; i++) {
            Message message = TestUtils
                .createMessage("message delete by sender " + (i + 1), account1, account2);
            message.setDeletedBySender(true);
            messages.add(message);
        }
        // 유저2이 보낸 메세지 5 개 + 유저1이 보내고 삭제한 메세지 5개
        for (int i = 0; i < 5; i++) {
            Message message = TestUtils
                .createMessage("message delete by receiver" + (i + 1), account2, account1);
            message.setDeletedByReceiver(true);
            message.setOpened(true);
            messages.add(message);
        }
        for (int i = 0; i < 5; i++) {
            Message message = TestUtils.createMessage("message" + (i + 1), account2, account1);
            messages.add(message);
        }
        return this.messageRepository.saveAll(messages);
    }

}
