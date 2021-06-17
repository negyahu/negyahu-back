package ga.negyahu.music.account.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(defaultValue = "계정 생성 DTO")
public class AccountCreateDto implements Serializable {

    @Schema(defaultValue = "youzheng@gmail.com", example = "youzheng@gmail.com", description = "사용자 이메일, 로그인시 사용")
    private String email;

    @Schema(defaultValue = "password@@", example = "password@@", description = "패스워드")
    private String password;

    @Schema(defaultValue = "양우정", example = "양우정", description = "사용자 실명")
    private String username;

    @Size(min = 13, max = 14)
    @Schema(defaultValue = "010-1111-2222", example = "010-1111-2222", description = "사용자 연락처")
    private String mobile;

}
