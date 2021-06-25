package ga.negyahu.music.subscribe;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.mapstruct.SubscribeMapper;
import ga.negyahu.music.security.annotation.LoginUser;
import ga.negyahu.music.subscribe.dto.SubscribeDto;
import ga.negyahu.music.subscribe.dto.SubscribeRequest;
import ga.negyahu.music.subscribe.entity.Subscribe;
import ga.negyahu.music.subscribe.service.SubscribeQueryService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubscribeController {

    private final SubscribeQueryService queryService;
    private SubscribeMapper mapper = SubscribeMapper.INSTANCE;
    @PostMapping("/subscribes}")
    public ResponseEntity<SubscribeDto> subscribe(@RequestBody SubscribeRequest request
        , @LoginUser Account user) {
        Subscribe subscribe = this.queryService.subscribe(request.getArtistId(), user.getId());

        SubscribeDto dto = mapper.toDto(subscribe);

        URI uri = linkTo(methodOn(SubscribeController.class).subscribe(request, user)).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    /*
    * 단일 구독정보 조회
    * */
    @GetMapping("/subscribes/{subscribeId}")
    public ResponseEntity<SubscribeDto> fetch(@PathVariable String subscribeId){

        return null;
    }

    /*
    * 구독자의 구독 리스트 조회
    * */
    @GetMapping("/accounts/{artistId}/subscribes")
    public ResponseEntity<Page<SubscribeDto>> artistFetchList(@PathVariable("artistId") Long artistId){

        return null;
    }

    /*
    * 아티스트 구독자 조회
    * */
    @GetMapping("/artists/{artistId}/subscribes")
    public ResponseEntity<Page<SubscribeDto>> artistFetchList(@PathVariable String artistId){

        return null;
    }

}
