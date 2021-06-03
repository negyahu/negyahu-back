package ga.negyahu.music.message.repository;

import ga.negyahu.music.message.Message;
import ga.negyahu.music.message.MessageResult;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface MessageRepository extends JpaRepository<Message, Long>, MessageDao {

    @Query("select m from Message m where m.sender.id = :senderId and m.isDeletedBySender = false")
    List<Message> findAllSentMessage(Long senderId);

    @Query("select m from Message m where m.receiver.id = :receiverId and m.isDeletedByReceiver = false")
    List<Message> findAllReceivedMessage(Long receiverId);

    @Modifying
    @Query("delete from Message m where m.sendDateTime <= :localDateTime")
    int deleteAllBySendDateTimeBefore(LocalDateTime localDateTime);

    @EntityGraph(attributePaths = {"sender","receiver"})
    Optional<Message> findWithAccountsById(Long id);

    <T> T findFirstById(Long id,Class<T> type);

}
