package ga.negyahu.music.message.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.message.Message;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MessageService {

    Message send(Message message);

    List<Message> send(Iterable<Message> message);

    List<Message> sendToAccounts(Message message,Long... ids);

    Message fetch(Long messageId,Long accountId);

    List<Message> fetchAllSent(Long accountId);

    List<Message> fetchAllReceived(Long accountId);

    void delete(Long messageId,Long accountId);

    int deleteAllPast();

    void modify(Message newMessage);

    boolean recall(Long messageId,Long accountId);



}
