package ga.negyahu.music.event;

import ga.negyahu.music.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpEvent {

    private Account account;

}
