package ga.negyahu.music.mail;

import javax.mail.MessagingException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

public interface ReceiverContext {

    void setReceiver(MimeMessageHelper helper) throws MessagingException;

    Context getContext();

    /*
    * 메일에 사용될 HTML 파일의 위치를 /resources/templates/${target_path}.html
    * */
    String getTemplatePath();

}
