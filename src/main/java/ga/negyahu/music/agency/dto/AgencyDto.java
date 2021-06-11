package ga.negyahu.music.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyDto {

    private Long id;

    private String name;

    private String nameEN;

    private String ceoName;

    private String mobile;

//    private Account account; // 대표계정의 상세정보 대신, 이메일만 노출
    private String adminEmail;

}
