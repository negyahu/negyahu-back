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
public class AccountOwnerDto {

    private Long id;

    private String email;

    private String mobile;

    private String username;

    private String nickname;

    private boolean isMemberShip;

    private Role role;

    private Address address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDate signUpDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Area area;

    private boolean areaCertify;
}
