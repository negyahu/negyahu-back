package ga.negyahu.music.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ga.negyahu.music.account.entity.Address;
import ga.negyahu.music.account.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountOwnerDto {

    @Schema(defaultValue = "1", example = "1", description = "사용자 계정 고유번호")
    private Long id;

    @Schema(defaultValue = "youzheng@gmail.com", example = "youzheng@gmail.com", description = "사용자 이메일, 로그인시 사용")
    private String email;

    @Schema(defaultValue = "010-1111-2222", example = "010-1111-2222", description = "사용자 연락처")
    private String mobile;

    @Schema(defaultValue = "양우정", example = "양우정", description = "사용자 실명")
    private String username;

    @Schema(defaultValue = "true", example = "true", description = "멤버십 가입여부")
    private boolean isMemberShip;

    @Schema(defaultValue = "USER", example = "USER", description = "계정등급")
    private Role role;

    @Schema(defaultValue = "USER", example = "USER", description = "계정등급")
    private Address address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(defaultValue = "2021-01-01", example = "2021-01-01", description = "가입일")
    private LocalDate signUpDate;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Area area;
//
//    private boolean areaCertify;
}
