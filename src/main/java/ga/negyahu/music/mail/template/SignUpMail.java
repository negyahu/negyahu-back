package ga.negyahu.music.mail.template;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.mail.ReceiverContext;
import javax.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.util.Validate;

@Data
@Builder
@AllArgsConstructor
public class SignUpMail implements ReceiverContext {

    public static final String TEMPLATE_PATH = "sign-up";

    private Account account;

    private String message;

    @Override
    public void setReceiver(final MimeMessageHelper helper) throws MessagingException {
        Validate.notEmpty(account.getEmail(),"[ERROR] 올바르지 못한 수신자 정보입니다.");
        Validate.notEmpty(account.getUsername(),"[ERROR] 올바르지 못한 수신자 정보입니다.");

        helper.setTo(account.getEmail());
        helper.setSubject(account.getUsername()+"님 회원가입을 축하드립니다!!");
    }

    @Override
    public Context getContext(){
        Validate.notEmpty(account.getEmail(),"[ERROR] 올바르지 못한 수신자 정보입니다.");

        Context context = new Context();
        context.setVariable("email",this.account.getEmail());
        context.setVariable("username",this.account.getUsername());
        context.setVariable("message", this.message);
        return context;
    }

    @Override
    public String getTemplatePath() {
        return TEMPLATE_PATH;
    }

}
