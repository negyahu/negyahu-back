package ga.negyahu.music.agency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(defaultValue = "소속사에 등록할 사용자계정의 고유번호")
public class ManagerDto {

    @Schema(defaultValue = "계정 이메일 (배열)", example = "[1,2,3]", description = "소속사에 등록할 계정의 이메일")
    private String[] emails;
}
