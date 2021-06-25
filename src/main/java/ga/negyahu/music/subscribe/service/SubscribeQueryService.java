package ga.negyahu.music.subscribe.service;

import ga.negyahu.music.subscribe.entity.Subscribe;

public interface SubscribeQueryService {

    Subscribe subscribe(Long artistId, Long accountId);

}
