package ga.negyahu.music.message.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.service.AccountService;
import ga.negyahu.music.exception.AccountNotFoundException;
import ga.negyahu.music.exception.BadMessageRequestException;
import ga.negyahu.music.exception.MessageAlreadyOpenException;
import ga.negyahu.music.exception.MessageNotFoundException;
import ga.negyahu.music.message.Message;
import ga.negyahu.music.message.dto.MessageDto;
import ga.negyahu.music.message.dto.MessageSearch;
import ga.negyahu.music.message.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final AccountService accountService;

    @Override
    public Message send(Message message) {
        if (isSameAccount(message)) {
            throw new BadMessageRequestException("[ERROR] 자신에게 메세지를 보낼 수 없습니다.");
        }

        try {
            Account receiver = accountService.fetch(message.getReceiver().getId());
            message.setReceiver(receiver);
            return this.messageRepository.save(message);
        } catch (AccountNotFoundException e) {
            throw new BadMessageRequestException("[ERROR] 존재하지 않는 회원에게는 메세지를 보낼 수 없습니다.");
        }
    }

    @Override
    public List<Message> send(Iterable<Message> messages) {
        List<Message> temp = new ArrayList<>();
        for (Message message : messages) {
            if (!isSameAccount(message)) {
                temp.add(message);
            }
        }
        return this.messageRepository.saveAll(temp);
    }

    @Override
    public List<Message> sendToAccounts(Message message, Long... ids) {
        ArrayList<Message> messages = new ArrayList<>(ids.length);
        for (Long id : ids) {
            Message msg = new Message();
            BeanUtils.copyProperties(message, msg);
            msg.setReceiver(Account.builder().id(id).build());
            messages.add(msg);
        }
        return this.messageRepository.saveAll(messages);
    }

    @Transactional(readOnly = true)
    @Override
    public Message fetch(Long messageId, Long accountId) {
        Message message = this.messageRepository.findWithAccountsById(messageId)
            .orElseThrow(() -> {
                throw new MessageNotFoundException();
            });
        // 제3자가 메세지를 조회하는지 확인
        boolean isOwner = !message.isOwner(accountId);
        if (!message.isOwner(accountId)) {
            throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
        }
        // 요청자가 이미 메세지를 삭제했다면 조회할수 없다.
        if (!message.canFetchBy(accountId)) {
            throw new MessageNotFoundException();
        }
        // 수신자가 메세지를 조회한다면 '열람' 으로 변경
        if (message.getReceiver().getId().equals(accountId)) {
            message.open(accountId);
        }
        return message;
    }

    @Override
    public Page<MessageDto> search(MessageSearch search, Pageable pageable) {
        return this.messageRepository.search(search, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> fetchAllSent(Long accountId) {
        return this.messageRepository.findAllSentMessage(accountId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Message> fetchAllReceived(Long accountId) {
        return this.messageRepository.findAllReceivedMessage(accountId);
    }

    /* 실제 삭제는 스케쥴링을 통해서 이뤄진다.*/
    @Override
    public void delete(Long messageId, Long accountId) {
        Message message = this.messageRepository.findWithAccountsById(messageId)
            .orElseThrow(() -> {
                throw new MessageNotFoundException();
            });
        // 삭제요청을 발신자가 했을 경우
        if (message.getSender().getId().equals(accountId)) {

            // 수신자가 아직 메세지를 읽지 않았거나, 수신자가 메세지를 삭제했다면 삭제
            if (!message.isOpened() || message.isDeletedByReceiver()) {
                this.messageRepository.delete(message);
                return;
            }
            // 수신자가 읽었다면 발신자만 flag 변경
            message.setDeletedBySender(true);
            return;
        }
        // 삭제요청을 수신자가 했을 경우 수신자만 삭제
        if (message.getReceiver().getId().equals(accountId)) {
            // 발신자도 이미 삭제했다면 메세지를 삭제
            if (message.isDeletedBySender()) {
                this.messageRepository.delete(message);
            }
            // 발신자는 아직 삭제하지 않았다면 수신자의 flag만 변경
            message.setDeletedByReceiver(true);
            return;
        }
        throw new AccessDeniedException("[ERROR] 권한이 없습니다.");
    }

    @Override
    public int deleteAllPast() {
        LocalDateTime targetTime = LocalDateTime.now().minusDays(90);
        return this.messageRepository.deleteAllBySendDateTimeBefore(targetTime);
    }

    /* 수신자가 이미 읽었다면 수정할 수 없다.*/
    @Override
    public void modify(Message newMessage) {
        Message message = this.fetch(newMessage.getId(), newMessage.getSender().getId());
        if (message.isOpened()) {
            throw new MessageAlreadyOpenException("[ERROR] 수신자가 열람한 메세지는 수정할 수 없습니다.");
        }
        if (!message.canModifyBy(newMessage.getSender().getId())) {
            throw new AccessDeniedException("[ERROR] 접근할 수 없습니다.");
        }
        modelMapper.map(newMessage, message);
        messageRepository.save(message);
    }

    /* 수신자가 메세지를 읽지 않았다면 리콜할 수 있다.*/
    @Override
    public boolean recall(Long messageId, Long accountId) {
        Message message = this.messageRepository.findWithAccountsById(messageId)
            .orElseThrow(() -> {
                throw new MessageNotFoundException();
            });
        if (!message.isOpened()) {
            this.messageRepository.delete(message);
            return true;
        }
        return false;
    }

    /* 수신자와 발신자가 동일한지 체크*/
    private boolean isSameAccount(Message message) {
        return message.getSender().getId().equals(message.getReceiver().getId());
    }

}
