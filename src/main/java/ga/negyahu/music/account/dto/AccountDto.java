package ga.negyahu.music.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ga.negyahu.music.area.Area;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("Account DTO")
public class AccountDto {

    @ApiModelProperty(name = "id", value = "3201", notes = "계정의 고유번호")

    private Long id;

    @ApiModelProperty(name = "email", value = "useremail@email.com", notes = "회원 이메일, 로그인시 사용")
    private String email;

    @ApiModelProperty(name = "username", value = "김창현", notes = "회원실명")
    private String username;

    @ApiModelProperty(name = "country", value = "KO-KR", notes = "회원의 국적")
    private String country;

    @ApiModelProperty(name = "signUpDate", value = "KO-KR", notes = "계정 생성일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDate signUpDate;

    @ApiModelProperty(name = "area", value = "KO-KR", notes = "계정 생성일")
    private Area area;

    @ApiModelProperty(name = "areaCertify", value = "KO-KR", notes = "계정 생성일")
    private boolean areaCertify;
}
