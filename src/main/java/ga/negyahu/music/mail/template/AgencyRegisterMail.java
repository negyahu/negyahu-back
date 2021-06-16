package ga.negyahu.music.mail.template;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.agency.entity.Agency;
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
public class AgencyRegisterMail implements ReceiverContext {

    public static final String TEMPLATE_PATH = "agency-register";

    private Agency agency;

    private String rawPassword;

    @Override
    public void setReceiver(final MimeMessageHelper helper) throws MessagingException {
        String email = agency.getAccount().getEmail();
        Validate.notEmpty(email, "[ERROR] 올바르지 못한 수신자 정보입니다.");

        helper.setTo(email);
        helper.setSubject(agency.getName() + "의 소속사 등록신청이 완료되었습니다.");
    }

    @Override
    public Context getContext() {
        Validate.notEmpty(agency.getAccount().getEmail(), "[ERROR] 올바르지 못한 수신자 정보입니다.");

        Context context = new Context();
        context.setVariable("agencyName", this.agency.getName());
        context.setVariable("rawPassword", this.rawPassword);
        return context;
    }

    @Override
    public String getTemplatePath() {
        return TEMPLATE_PATH;
    }

}
