package ga.negyahu.music.area.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.area.Area;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AreaService {

    Area register(Area area);

    Area update(Area area);

    void delete(Integer id);

    Area fetch(Integer id);

    List<Area> fetchAll();

    List<Account> fetchAccountInArea(Area area);

}
