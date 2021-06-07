package ga.negyahu.music.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ga.negyahu.music.account.entity.Address;
import ga.negyahu.music.account.entity.Role;
import ga.negyahu.music.area.Area;
import java.time.LocalDate;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountUpdateDto {

    private String password;

    private String username;

    private String mobile;

    private String nickname;

}
