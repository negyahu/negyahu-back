package ga.negyahu.music.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgencySearch {

    // 검색기준 : 소속사 이름, 영어이름, 전화번호, 상태
    private String type;
    // 검색어
    private String keyword;

}
