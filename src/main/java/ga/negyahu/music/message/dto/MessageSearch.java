package ga.negyahu.music.message.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageSearch {

    private Long accountId;

    private MessageType type = MessageType.ALL;

}
