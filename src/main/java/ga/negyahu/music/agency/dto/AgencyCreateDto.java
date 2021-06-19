package ga.negyahu.music.agency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema
public class AgencyCreateDto {

    @Schema(description = "우정엔터테이먼트", defaultValue = "소속사 한글이름"
        , example = "우정엔터테이먼트")
    private String name;

    @Schema(description = "YouzhengEntertainment", defaultValue = "소속사 영어이름, URL에 사용된다."
        , example = "YouzhengEntertainment")
    private String nameEN;

    @Schema(description = "000-00-00000", defaultValue = "사업자등록번호"
        , example = "000-00-00000")
    private String businessNumber;


    @Schema(description = "010-1234-5678", defaultValue = "소속사 대표 연락처"
        , example = "010-1234-5678")
    private String mobile;


    @Schema(description = "소속사 대표 이름", defaultValue = "양우정", example = "양우정")
    private String bossName;

    @Schema(defaultValue = "youzheng.ent@gmail.com", description = "소속사 대표이메일, 로그인시 이용"
        , example = "youzheng.ent@gmail.com")
    private String email;

    @Nullable
    @Schema(defaultValue = "10", description = "file upload를 통해 등록한 후 반환된 고유번호"
        , example = "10", type = "long")
    private Long fileId;

}
