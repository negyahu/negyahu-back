package ga.negyahu.music.subscribe.service;

import ga.negyahu.music.subscribe.entity.Subscribe;
import ga.negyahu.music.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscribeQueryServiceImpl implements SubscribeQueryService {

    private final SubscribeRepository subscribeRepository;

    @Override
    public Subscribe subscribe(Long artistId, Long accountId){

        return null;
    };

}
