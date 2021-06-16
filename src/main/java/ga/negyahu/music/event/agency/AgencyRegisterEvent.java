package ga.negyahu.music.event.agency;

import ga.negyahu.music.agency.entity.Agency;
import ga.negyahu.music.mail.template.AgencyRegisterMail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AgencyRegisterEvent extends ApplicationEvent {

    private AgencyRegisterMail mail;

    public AgencyRegisterEvent(Agency agency, String password) {
        super(agency);
        this.mail = new AgencyRegisterMail(agency, password);
    }
}
