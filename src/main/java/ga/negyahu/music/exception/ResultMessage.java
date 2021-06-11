package ga.negyahu.music.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultMessage {

    private Result result;

    private String message;

    public static final ResultMessage createFailMessage(String message) {
        return ResultMessage.builder()
            .result(Result.FAIL)
            .message(message)
            .build();
    }

    public static final ResultMessage createSuccessMessage(String message) {
        return ResultMessage.builder()
            .result(Result.SUCCESS)
            .message(message)
            .build();
    }

}
