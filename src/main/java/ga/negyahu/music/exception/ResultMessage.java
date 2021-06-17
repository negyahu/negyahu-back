package ga.negyahu.music.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "요청 결과 메세지")
public class ResultMessage {

    @Schema(description = "요청에 대한 결과", defaultValue = "FAIL")
    private Result result;

    @Schema(description = "메세지", defaultValue = "메세지")
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
