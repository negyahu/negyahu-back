package ga.negyahu.music.message;

import ga.negyahu.music.account.Account;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime sendDateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Account sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Account receiver;

    private boolean isOpened;

    private boolean isDeletedBySender;

    private boolean isDeletedByReceiver;

    public boolean isSender(Long accountId) {
        return this.sender.getId().equals(accountId);
    }

    public void open(Long accountId) {
        if (this.isOpened) {
            return;
        }
        if (this.receiver.getId().equals(accountId)) {
            isOpened = true;
            return;
        }
    }

    public boolean canModifyBy(Long accountId) {
        return this.sender.getId().equals(accountId);
    }

    public boolean canFetchBy(Long accountId) {
        if (this.isDeletedBySender && this.sender.getId().equals(accountId)) {
            return false;
        }
        if (this.isDeletedByReceiver && this.receiver.getId().equals(accountId)) {
            return false;
        }
        return true;
    }

    public boolean isOwner(Long accountId) {
        return this.sender.getId().equals(accountId) || this.receiver.getId().equals(accountId);
    }

}
