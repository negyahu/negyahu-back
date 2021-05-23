package ga.negyahu.music.account.dto;

import ga.negyahu.music.account.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreateDto {

    private String email;

    private String password;

    private String username;

    private String nickname;

    private String country;

    private String zipcode;

    private String roadAddress;

    private String detailAddress;
}
