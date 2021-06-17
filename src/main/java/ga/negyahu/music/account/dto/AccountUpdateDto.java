package ga.negyahu.music.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "사용자 정보 변경")
public class AccountUpdateDto {

    @Schema(example = "newPassword@@", defaultValue = "newPassword@@", description = "새로운 패스워드")
    private String password;

    @Schema(example = "01033334444", defaultValue = "01033334444", description = "새로운 연락처")
    private String mobile;

}
