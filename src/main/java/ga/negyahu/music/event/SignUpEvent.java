package ga.negyahu.music.event;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.mail.template.SignUpMail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpEvent {

    private SignUpMail signUpMail;

    public SignUpEvent(Account account) {
        this.signUpMail = new SignUpMail(account,"");
    }
}
