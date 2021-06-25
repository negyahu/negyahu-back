package ga.negyahu.music.subscribe.service;

import static org.junit.jupiter.api.Assertions.*;

import ga.negyahu.music.subscribe.repository.SubscribeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubscribeQueryServiceImplTest {

    SubscribeQueryService queryService;
    @Mock
    SubscribeRepository subscribeRepository;


    @BeforeEach
    public void init() {
        this.queryService = new SubscribeQueryServiceImpl(subscribeRepository);
    }

    @Test
    public void 구독신청테스트() {
        
    }

}