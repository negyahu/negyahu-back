package ga.negyahu.music.agency.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(defaultValue = "소속사 검색조건")
public class AgencySearch {

    @Schema(description = "검색 기준, ex)소속사 이름, 소속사 영어이름, 전화번호, 소속사 상태", defaultValue = "name", example = "name")
    private String type;
    // 검색어
    @Schema(description = "검색 키워드", defaultValue = "빅히트", example = "빅히")
    private String keyword;

}
