package ga.negyahu.music.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import ga.negyahu.music.account.Account;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private Long id;

    private String content;

    private LocalDateTime sendDateTime;

    private WriterDto sender;

    private WriterDto receiver;

    private boolean isOpened;

    @QueryProjection
    public MessageDto(Long id, String content, LocalDateTime sendDateTime,
        Long senderId, String senderNickname, Long receiverId, String receiverNickname,
        boolean isOpened) {
        this.id = id;
        this.content = content;
        this.sendDateTime = sendDateTime;
        this.isOpened = isOpened;

        this.sender = new WriterDto();
        this.sender.setId(senderId);
        this.sender.setNickname(senderNickname);

        this.receiver = new WriterDto();
        this.receiver.setId(receiverId);
        this.receiver.setNickname(receiverNickname);
    }
}
