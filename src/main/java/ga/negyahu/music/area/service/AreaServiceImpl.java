package ga.negyahu.music.area.service;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.area.Area;
import ga.negyahu.music.area.AreaRepository;
import ga.negyahu.music.exception.AreaNotFountException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService{

    private final AreaRepository areaRepository;
    private final ModelMapper modelMapper;

    @Override
    public Area register(Area area) {
        return areaRepository.save(area);
    }

    @Override
    public Area update(Area area) {
        Area original = fetch(area.getId());
        modelMapper.map(area,original);
        return original;
    }

    @Override
    public void delete(Integer id) {
        Area target = areaRepository.findAreaWithAccountsById(id);
        for(Account account : target.getAccounts()){
            account.setArea(null);
        }
        areaRepository.delete(target);
    }

    @Transactional(readOnly = true)
    @Override
    public Area fetch(Integer id) {
        return areaRepository.findById(id)
            .orElseThrow(() -> {
                throw new AreaNotFountException();
            });
    }

    @Transactional(readOnly = true)
    @Override
    public List<Area> fetchAll() {
        return areaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Account> fetchAccountInArea(Area area) {
        Area findArea = areaRepository.findAreaWithAccountsById(area.getId());
        return findArea.getAccounts();
    }
}
