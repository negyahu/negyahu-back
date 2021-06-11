package ga.negyahu.music.account.dto;

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
