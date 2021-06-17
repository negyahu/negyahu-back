package ga.negyahu.music.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ga.negyahu.music.area.Area;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "사용자 DTO")
public class AccountDto {

    @Schema(defaultValue = "1", example = "1", description = "사용자 계정 고유번호")
    private Long id;

    @Schema(defaultValue = "youzheng@gmail.com", example = "youzheng@gmail.com", description = "사용자 이메일, 로그인시 사용")
    private String email;

    @Schema(defaultValue = "양우정", example = "양우정", description = "사용자 실명")
    private String username;

    @Schema( defaultValue = "KO-KR",example = "KO-KR" ,description = "사용자 국가")
    private String country;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @Schema( defaultValue = "2021-01-01",example = "2021-01-01" ,description = "가입일")
    private LocalDate signUpDate;

//    @ApiModelProperty(name = "area", value = "KO-KR", notes = "계정 생성일")
//    private Area area;

//    @ApiModelProperty(name = "areaCertify", value = "KO-KR", notes = "계정 생성일")
//    private boolean areaCertify;
}
