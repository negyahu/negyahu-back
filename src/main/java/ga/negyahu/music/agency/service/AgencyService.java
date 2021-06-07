package ga.negyahu.music.agency.service;

import ga.negyahu.music.agency.Agency;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AgencyService {

    Agency register(Agency agency);

}
