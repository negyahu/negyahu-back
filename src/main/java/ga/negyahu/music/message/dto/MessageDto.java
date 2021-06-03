package ga.negyahu.music.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
