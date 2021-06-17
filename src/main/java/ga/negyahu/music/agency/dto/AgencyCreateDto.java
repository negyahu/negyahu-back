package ga.negyahu.music.agency.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyCreateDto {

    @ApiModelProperty(name = "소속사 한글이름", value = "우정엔터테이먼트", notes = "소속사 한글이름"
        , example = "우정엔터테이먼트")
    private String name;

    @ApiModelProperty(name = "소속사 영어이름", value = "YouzhengEntertainment", notes = "소속사 영어이름, URL에 사용된다."
        , example = "YouzhengEntertainment")
    private String nameEN;

    @ApiModelProperty(name = "사업자등록번호", value = "000-00-00000", notes = "사업자등록번호"
        , example = "000-00-00000")
    private String businessNumber;


    @ApiModelProperty(name = "소속사 대표 연락처", value = "010-1234-5678", notes = "소속사 대표 연락처"
        , example = "010-1234-5678")
    private String mobile;


    @ApiModelProperty(name = "소속사 대표 이름", value = "양우정", notes = "소속사 대표 이름", example = "양우정")
    private String bossName;

    @ApiModelProperty(name = "소속사 대표 이메일", value = "youzheng.ent@gmail.com", notes = "소속사 대표이메일, 로그인시 이용"
        , example = "youzheng.ent@gmail.com")
    private String email;

    @ApiModelProperty(name = "등록한 파일의 고유번호", value = "[3,5,6,3]", notes = "file upload를 통해 등록한 후 반환된 고유번호"
        , example = "[3,5,6,3]")
    private Long[] fileIds;

}
