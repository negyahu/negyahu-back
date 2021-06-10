package ga.negyahu.music.mail;

import javax.mail.MessagingException;

public interface MailService {

    void send(ReceiverContext context) throws MessagingException;

}
