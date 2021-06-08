package ga.negyahu.music.agency.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyCreateDto {

    private String agentName;

    private String agentNameEN;

    private String businessNumber;

    private String mobile;

    private String ceoName;

    private String adminEmail;

    private Long[] fileIds;

}
