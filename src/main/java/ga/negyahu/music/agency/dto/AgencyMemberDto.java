package ga.negyahu.music.agency.dto;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.dto.AccountDto;
import ga.negyahu.music.account.entity.State;
import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.agency.entity.AgencyRole;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyMemberDto {

    private Long id;

    private Agency agency;

    private String nickname;

    private AccountDto account;

    private AgencyRole agencyRole;

    private State state;

    private LocalDate registerDate;


}
