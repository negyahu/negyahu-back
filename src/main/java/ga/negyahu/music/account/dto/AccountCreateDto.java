package ga.negyahu.music.account.dto;

import ga.negyahu.music.account.entity.Address;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("Account Create DTO")
public class AccountCreateDto implements Serializable {

    @ApiModelProperty(notes = "사용자 이메일, 로그인시 사용됩니다.",name = "email", value = "useremail@email.com"
        ,example = "youzheng@gmail.com")
    private String email;

    @ApiModelProperty(notes = "사용자 패스워드",name = "password", value = "우정007@@",example = "우정007@@")
    private String password;

    @ApiModelProperty(notes = "회원 실명",name = "username", value = "양우정",example = "양우정")
    private String username;

    @ApiModelProperty(notes = "회원 닉네임",name = "nickname", value = "킹우정",example = "킹우정")
    private String nickname;

    @Size(min = 13,max = 14)
    @ApiModelProperty(notes = "회원 연락처",name = "mobile", value = "010-1111-2222",example = "010-1111-2222")
    private String mobile;

}
