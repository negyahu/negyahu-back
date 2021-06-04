package ga.negyahu.music.message;

import ga.negyahu.music.account.Account;
import ga.negyahu.music.mapstruct.MessageMapper;
import ga.negyahu.music.message.dto.MessageDto;
import ga.negyahu.music.message.dto.MessageSearch;
import ga.negyahu.music.message.dto.MessageSendDto;
import ga.negyahu.music.message.dto.MessageUpdateDto;
import ga.negyahu.music.message.service.MessageService;
import ga.negyahu.music.security.annotation.LoginUser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {

    public static final String ROOT_URL = "/api/messages";

    private final MessageService messageService;
    private MessageMapper messageMapper = MessageMapper.INSTANCE;

    @GetMapping(ROOT_URL)
    public ResponseEntity fetchAll(@LoginUser Account account, @PageableDefault Pageable pageable
        ,MessageSearch messageSearch) {
        messageSearch.setAccountId(account.getId());

        Page<MessageDto> page = this.messageService.search(messageSearch, pageable);
        return ResponseEntity.ok().body(page);
    }

    @PostMapping(ROOT_URL)
    public ResponseEntity send(@LoginUser Account sender, @RequestBody MessageSendDto sendDto,
        Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Message message = this.messageMapper.from(sendDto);
        message.setSender(sender);
        Message sentMessage = this.messageService.send(message);

        URI uri = WebMvcLinkBuilder.linkTo(
            WebMvcLinkBuilder.methodOn(MessageController.class).send(sender, sendDto, errors))
            .slash(sentMessage.getId()).toUri();

        MessageDto messageDto = this.messageMapper.toDto(message);

        return ResponseEntity.created(uri).body(messageDto);
    }

    @GetMapping(ROOT_URL + "/{id}")
    public ResponseEntity fetch(@PathVariable Long id, @LoginUser Account account) {
        Message message = messageService.fetch(id, account.getId());
        MessageDto dto = this.messageMapper.toDto(message);
        return ResponseEntity.ok().body(dto);
    }

    @PatchMapping(ROOT_URL + "/{id}")
    public ResponseEntity patch(@PathVariable Long id, @LoginUser Account account, @RequestBody
        MessageUpdateDto updateDto, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        Message message = this.messageMapper.from(updateDto);
        message.setId(id);
        message.setSender(account);

        this.messageService.modify(message);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ROOT_URL + "/{id}")
    public ResponseEntity delete(@PathVariable Long id, @LoginUser Account account) {

        this.messageService.delete(id, account.getId());

        return ResponseEntity.noContent().build();
    }

}
