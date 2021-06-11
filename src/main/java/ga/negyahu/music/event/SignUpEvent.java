package ga.negyahu.music.event;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.mail.template.SignUpMail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class SignUpEvent extends ApplicationEvent {

    private SignUpMail signUpMail;

    public SignUpEvent(Account account) {
        super(account);
        this.signUpMail = new SignUpMail(account,"");
    }

    public SignUpEvent(Account account,String message) {
        super(account);
        this.signUpMail = new SignUpMail(account,message);
    }
}
