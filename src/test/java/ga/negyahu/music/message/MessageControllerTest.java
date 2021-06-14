package ga.negyahu.music.message;

import static ga.negyahu.music.message.controller.MessageController.ROOT_URL;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.account.repository.AccountRepository;
import ga.negyahu.music.exception.MessageNotFoundException;
import ga.negyahu.music.message.dto.MessageSearch;
import ga.negyahu.music.message.dto.MessageSendDto;
import ga.negyahu.music.message.dto.MessageType;
import ga.negyahu.music.message.dto.MessageUpdateDto;
import ga.negyahu.music.message.repository.MessageRepository;
import ga.negyahu.music.message.service.MessageService;
import ga.negyahu.music.utils.MessageTestUtils;
import ga.negyahu.music.utils.TestUtils;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@CustomSpringBootTest
@AutoConfigureMockMvc
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AccountRepository accountRepository;

    private List<Account> accounts;
    private String tokenOfZeroIndex;

    @BeforeEach
    public void init() {
        int accountCnt = 3;
        this.accounts = new ArrayList<>(accountCnt);
        this.accounts = TestUtils.createAccounts(accountCnt);
        for (int i = 1; i < accountCnt; i++) {
            testUtils.signUpAccount(accounts.get(i));
        }
        this.tokenOfZeroIndex = this.testUtils.signSupAndLogin(accounts.get(0));
    }

    @AfterEach
    public void destroy(){
        this.messageRepository.deleteAll();
        this.accountRepository.deleteAll();
        SecurityContextHolder.getContext().setAuthentication(null);
        this.tokenOfZeroIndex = null;
        this.accounts = null;
    }

    @Test
    public void 메세지_전송() throws Exception {
        long count = messageRepository.count();
        assertEquals(0, count, "초기에 메세지 0개");

        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        MessageSendDto sendDto = MessageSendDto.builder().receiverId(receiver.getId())
            .content("안녕하세요~~~~").build();
        String contentAsJson = objectMapper.writeValueAsString(sendDto);

        // when : 메세지 전송
        ResultActions request = postRequest(contentAsJson);

        // then : MessageDto 형태의 JSON을 반환 받는다.
        ResultActions actions = checkSendResponse(sender, receiver, request);

        assertEquals(1, messageRepository.count());

    }

    @Test
    public void 자신에게_메세지를_보낼수_없다() throws Exception {

        Account sendAndReceiver = this.accounts.get(0);
        MessageSendDto sendDto = MessageSendDto.builder().receiverId(sendAndReceiver.getId())
            .content("안녕하세요~~~~").build();

        String contentAsJson = objectMapper.writeValueAsString(sendDto);

        // when : 메세지 전송
        ResultActions request = postRequest(contentAsJson);

        // then : 404
        request.andExpect(status().isBadRequest());
        long count = this.messageRepository.count();
        assertEquals(0, count);
    }

    @Test
    public void 존재하지_않는_유저에게_발송() throws Exception {
        Account sender = this.accounts.get(0);
        MessageSendDto sendDto = MessageSendDto.builder().receiverId(Long.MAX_VALUE)
            .content("안녕하세요~~~~").build();

        String contentAsJson = objectMapper.writeValueAsString(sendDto);

        // when : 메세지 전송
        ResultActions request = postRequest(contentAsJson);
        request.andExpect(status().isBadRequest())
        ;
        long count = this.messageRepository.count();
        assertEquals(0, count);
    }

    // Fetch
    @Test
    public void 발신자_메세지_조회() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        String token = this.testUtils.loginAccount(sender);
        Message sendMessage = sendMessage(sender, receiver, "message");

        ResultActions get = this.mockMvc
            .perform(get(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, token)
            );

        get.andExpect(jsonPath("$.id", is(sendMessage.getId().intValue())))
            .andExpect(jsonPath("$.content", is(sendMessage.getContent())))
            .andExpect(jsonPath("$.sendDateTime").exists())
            .andExpect(jsonPath("$.sender.id", is(sender.getId().intValue())))
            .andExpect(jsonPath("$.sender.nickname", is(sender.getNickname())))
            .andExpect(jsonPath("$.receiver.id", is(receiver.getId().intValue())))
            .andExpect(jsonPath("$.receiver.nickname", is(receiver.getNickname())))
            .andExpect(jsonPath("$.opened", is(false)))
        ;
    }

    @Test
    public void 수신자_메세지_조회() throws Exception {
        long count = this.accountRepository.count();
        System.out.println("count = " + count);
        long messageCnt = this.messageRepository.count();
        System.out.println("messageCnt = " + messageCnt);
        Account receiver = this.accounts.get(0);
        Account sender = this.accounts.get(1);
        Message sendMessage = sendMessage(sender, receiver, "message");

        String token = this.testUtils.loginAccount(receiver);

        ResultActions get = this.mockMvc
            .perform(get(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, token)
            );
        long count2 = this.accountRepository.count();
        System.out.println("count = " + count2);
        get.andExpect(jsonPath("$.id", is(sendMessage.getId().intValue())))
            .andExpect(jsonPath("$.content", is(sendMessage.getContent())))
            .andExpect(jsonPath("$.sendDateTime").exists())
            .andExpect(jsonPath("$.sender.id", is(sender.getId().intValue())))
            .andExpect(jsonPath("$.sender.nickname", is(sender.getNickname())))
            .andExpect(jsonPath("$.receiver.id", is(receiver.getId().intValue())))
            .andExpect(jsonPath("$.receiver.nickname", is(receiver.getNickname())))
            .andExpect(jsonPath("$.opened", is(true)))
        ;
    }

    @Test
    public void 권한이없는계정_메세지_조회() throws Exception {
        Account sender = this.accounts.get(1);
        Account receiver = this.accounts.get(2);
        Message sendMessage = sendMessage(sender, receiver, "message");

        // when : 제3자가 메세지 조회 요청
        ResultActions get = this.mockMvc
            .perform(get(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
            );

        // then : 403
        get.andExpect(status().isForbidden());
    }

    @Test
    public void 존재하지않는_메세지_조회() throws Exception {
        Account sender = this.accounts.get(0);

        // when : 제3자가 메세지 조회 요청
        ResultActions get = this.mockMvc
            .perform(get(ROOT_URL + "/{id}", 10L)
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
            );

        // then : 404
        get.andExpect(status().isNotFound());
    }

    // Patch
    @Test
    public void 발신자_메세지_수정() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        Message sendMessage = sendMessage(sender, receiver, "message");

        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        ResultActions patch = this.mockMvc
            .perform(patch(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        patch.andExpect(status().isNoContent())
        ;

        Message updated = this.messageRepository.findById(sendMessage.getId()).get();
        assertEquals(dto.getContent(), updated.getContent());
        assertEquals(sendMessage.getSendDateTime(), updated.getSendDateTime());
    }

    @Test
    public void 수신지가_읽은메세지_수정불가() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        Message sendMessage = sendMessage(sender, receiver, "message");
        sendMessage.setOpened(true);
        this.messageRepository.save(sendMessage);

        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        ResultActions patch = this.mockMvc
            .perform(patch(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        patch.andExpect(status().isBadRequest())
        ;

        Message updated = this.messageRepository.findById(sendMessage.getId()).get();
        assertNotEquals(dto.getContent(), updated.getContent(), "변경값이 적용되지 않는다.");
    }

    @Test
    public void 수진자_제3자_메세지수정() throws Exception {
        Account sender = this.accounts.get(1);
        Account receiver = this.accounts.get(0); // JWT OWNER

        Message sendMessage = sendMessage(sender, receiver, "message");
        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        // when : 수신자가 메세지 수정
        ResultActions patch = this.mockMvc
            .perform(patch(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        // then : 메세지를 조회하는 과정에서 open = true 로 변경되기 때문에 수정할 수 없다.
        patch.andExpect(status().isBadRequest())
        ;

        Account other = this.accounts.get(2);
        String token2 = this.testUtils.loginAccount(other);
        // when : 제3자 메세지 수정
        ResultActions patch2 = this.mockMvc
            .perform(patch(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, token2)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        // then : 403
        patch2.andExpect(status().isForbidden());

        Message updated = this.messageRepository.findById(sendMessage.getId()).get();
        assertNotEquals(dto.getContent(), updated.getContent(), "변경값이 적용되지 않는다.");
    }

    @Description("수신자가 아직 읽지 않은 메세지를 발신자가 삭제")
    @Test
    public void 발신자_메세지_삭제() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        Message sendMessage = sendMessage(sender, receiver, "message");
        this.messageRepository.save(sendMessage);
        String token = this.testUtils.loginAccount(sender);
        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        ResultActions delete = this.mockMvc
            .perform(MockMvcRequestBuilders.delete(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, token)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        // then : 206, 수신자가 아직 읽지 않았기 때문에 메세지는 완전히 삭제된다.
        delete.andExpect(status().isNoContent());
        assertThrows(MessageNotFoundException.class, () -> {
            Message fetch = this.messageService.fetch(sendMessage.getId(), sender.getId());
        });

        long totalCount = this.messageRepository.count();
        assertEquals(0, totalCount);
    }

    @Description("수신자가 이미 읽은 메세지를 발신자가 삭제, 발신자의 flag만 변경된다.")
    @Test
    public void 이미읽은_메세지_발신자_삭제() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        Message sendMessage = sendMessage(sender, receiver, "message");
        sendMessage.setOpened(true);    // 메세지 읽음으로 설정
        this.messageRepository.save(sendMessage);

        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        ResultActions delete = this.mockMvc
            .perform(MockMvcRequestBuilders.delete(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        // then : 206
        delete.andExpect(status().isNoContent());

        // then : 발신자는 조회할 수 없다.
        assertThrows(MessageNotFoundException.class, () -> {
            Message fetch = this.messageService.fetch(sendMessage.getId(), sender.getId());
        });
        long totalCount = this.messageRepository.count();
        assertEquals(1, totalCount);
        // then : 수신자는 여전히 조회할 수 있다.
        Message fetchByReceiver = this.messageService.fetch(sendMessage.getId(), receiver.getId());
        assertTrue(fetchByReceiver.isDeletedBySender());
    }

    @Description("발신자는 삭제하지 않고, 수신자만 삭제요청")
    @Test
    public void 발신자삭제X_수신자삭제요청() throws Exception {
        Account sender = this.accounts.get(1);
        Account receiver = this.accounts.get(0);
        Message sendMessage = sendMessage(sender, receiver, "message");
        this.messageRepository.save(sendMessage);

        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        ResultActions delete = this.mockMvc
            .perform(MockMvcRequestBuilders.delete(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        // then : 206, 발신
        delete.andExpect(status().isNoContent());
        assertThrows(MessageNotFoundException.class, () -> {
            Message fetch = this.messageService.fetch(sendMessage.getId(), receiver.getId());
        });

        long totalCount = this.messageRepository.count();
        assertEquals(1, totalCount);
        Message fetchBySender = this.messageService.fetch(sendMessage.getId(), sender.getId());
        assertTrue(fetchBySender.isDeletedByReceiver());
    }

    @Description("수신자 메세지 삭제후, 발신자도 삭제요청 -> 완전히 삭제된다.")
    @Test
    public void 수신자삭제후_발신자삭제요청() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        Message sendMessage = sendMessage(sender, receiver, "message");
        // given : 수신자 읽음, 수신자 삭제
        sendMessage.setDeletedByReceiver(true);
        sendMessage.setOpened(true);
        this.messageRepository.save(sendMessage);

        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        // when : 발신자 삭제요청
        ResultActions delete = this.mockMvc
            .perform(MockMvcRequestBuilders.delete(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        // then : 206, 메세지 완전히 삭제, 수신자 발신자 모두 조회불가
        delete.andExpect(status().isNoContent());
        assertThrows(MessageNotFoundException.class, () -> {
            Message fetch = this.messageService.fetch(sendMessage.getId(), receiver.getId());
            Message fetch2 = this.messageService.fetch(sendMessage.getId(), sender.getId());
        });
        // then : DB에서 해당 메세지 삭제
        long totalCount = this.messageRepository.count();
        assertEquals(0, totalCount);

    }

    @Description("발신자 삭제후, 수신자 메세지 삭제요청 -> 완전히 삭제된다.")
    @Test
    public void 발신자삭제후_수신자삭제요청() throws Exception {
        Account sender = this.accounts.get(1);
        Account receiver = this.accounts.get(0);
        Message sendMessage = sendMessage(sender, receiver, "message");
        // given : 수신자 읽음, 수신자 삭제
        sendMessage.setDeletedBySender(true);
//        sendMessage.setOpened(true);
        this.messageRepository.save(sendMessage);

        MessageUpdateDto dto = MessageUpdateDto.builder().content("new message").build();

        // when : 발신자 삭제요청
        ResultActions delete = this.mockMvc
            .perform(MockMvcRequestBuilders.delete(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
                .content(this.objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON)
            );

        // then : 206, 메세지 완전히 삭제, 수신자 발신자 모두 조회불가
        delete.andExpect(status().isNoContent());
        assertThrows(MessageNotFoundException.class, () -> {
            Message fetch = this.messageService.fetch(sendMessage.getId(), receiver.getId());
            Message fetch2 = this.messageService.fetch(sendMessage.getId(), sender.getId());
        });
        // then : DB에서 해당 메세지 삭제
        long totalCount = this.messageRepository.count();
        assertEquals(0, totalCount);
    }

    @Test
    public void 발신한_메세지_검색() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        MessageTestUtils utils = new MessageTestUtils(this.messageRepository);
        utils.init(sender, receiver);

        MessageSearch search = MessageSearch.builder()
            .type(MessageType.SEND)
            .build();

        ResultActions request = this.mockMvc.perform(get(ROOT_URL)
            .header(AUTHORIZATION, this.tokenOfZeroIndex)
            .contentType(MediaType.APPLICATION_JSON)
            .param("type", MessageType.SEND.name())
        );

        request.andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(10)))
            .andExpect(jsonPath("$.totalElements", is(10)))
        ;
    }

    @Test
    public void 수신한_메세지_검색() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        MessageTestUtils utils = new MessageTestUtils(this.messageRepository);
        utils.init(sender, receiver);

        ResultActions request = this.mockMvc.perform(get(ROOT_URL)
            .header(AUTHORIZATION, this.tokenOfZeroIndex)
            .contentType(MediaType.APPLICATION_JSON)
            .param("type", MessageType.RECEIVE.name())
        );

        request.andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(5)))
            .andExpect(jsonPath("$.totalElements", is(5)))
        ;
    }

    @Test
    public void 모든_송수신메세지() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        MessageTestUtils utils = new MessageTestUtils(this.messageRepository);
        utils.init(sender, receiver);

        ResultActions request = this.mockMvc.perform(get(ROOT_URL)
            .header(AUTHORIZATION, this.tokenOfZeroIndex)
            .contentType(MediaType.APPLICATION_JSON)
            .param("type", MessageType.RECEIVE.name())
        );

        request.andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(5)))
            .andExpect(jsonPath("$.totalElements", is(5)))
        ;
    }

    @Test
    public void 모든_송수신메세지_페이징() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        MessageTestUtils utils = new MessageTestUtils(this.messageRepository);
        utils.init(sender, receiver);

        Integer page = 1;
        Integer size = 5;

        ResultActions request = this.mockMvc.perform(get(ROOT_URL)
            .header(AUTHORIZATION, this.tokenOfZeroIndex)
            .contentType(MediaType.APPLICATION_JSON)
            .param("type", MessageType.RECEIVE.name())
            .param("p", page.toString())
            .param("s", size.toString())
        );

        request.andExpect(status().isOk())
            .andExpect(jsonPath("$.content", hasSize(size)))
            .andExpect(jsonPath("$.totalElements", is(5)))
        ;
    }

    @Test
    public void 발신자_메세지삭제() throws Exception {
        Account sender = this.accounts.get(0);
        Account receiver = this.accounts.get(1);
        MessageTestUtils utils = new MessageTestUtils(this.messageRepository);

        List<Message> messages = utils.init(sender, receiver);
        Long messageId = messages.stream()
            .filter(m -> m.getSender().getId().equals(sender.getId()))
            .findFirst().get().getId();

        ResultActions delete = this.mockMvc.perform(delete(ROOT_URL + "/{id}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, this.tokenOfZeroIndex)
        );

        delete.andExpect(status().isNoContent());

        assertThrows(NoSuchElementException.class, () -> {
            this.messageRepository.findById(messageId).get();
        }, "수신자가 아직 연람하지 않았기 때문에 완전히 삭제된다.");
    }

    @Test
    public void 수신자_메세지삭제() throws Exception {
        Account sender = this.accounts.get(1);
        Account receiver = this.accounts.get(0);
        MessageTestUtils utils = new MessageTestUtils(this.messageRepository);
        String token = this.testUtils.loginAccount(receiver);
        List<Message> messages = utils.init(sender, receiver);
        Long messageId = messages.stream()
            .filter(m -> m.getSender().getId().equals(sender.getId()))
            .findFirst().get().getId();

        ResultActions delete = this.mockMvc.perform(delete(ROOT_URL + "/{id}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, token)
        );

        // then : 206
        delete.andExpect(status().isNoContent());

        // then : 수신자 삭제 플레그 -> true
        Message message = this.messageRepository.findById(messageId).get();
        assertEquals(true, message.isDeletedByReceiver());
        assertEquals(false, message.isDeletedBySender());
        // then : 수신자는 조회할 수 없다.
        assertThrows(MessageNotFoundException.class, () -> {
            this.messageService.fetch(messageId, receiver.getId());
        });
    }

    @Test
    public void 권한이_없는_사용자_삭제요청() throws Exception {
        Account sender = this.accounts.get(1);
        Account receiver = this.accounts.get(0);
        Account other = this.accounts.get(2);
        String token = testUtils.loginAccount(other);
        MessageTestUtils utils = new MessageTestUtils(this.messageRepository);

        List<Message> messages = utils.init(sender, receiver);
        Long messageId = messages.stream()
            .filter(m -> m.getSender().getId().equals(sender.getId()))
            .findFirst().get().getId();

        ResultActions delete = this.mockMvc.perform(delete(ROOT_URL + "/{id}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .header(AUTHORIZATION, token)
        );

        // then : 403
        delete.andExpect(status().isForbidden());

        // then : 메세지의 플레그 변경되지 않는다.
        Message message = this.messageRepository.findById(messageId).get();
        assertNotNull(message);
        assertFalse(message.isDeletedByReceiver());
        assertFalse(message.isDeletedBySender());
    }

    private Message sendMessage(Account sender, Account receiver, String message) {
        Message template = TestUtils.createMessage(message, sender, receiver);
        return this.messageRepository.save(template);
    }

    private ResultActions postRequest(String contentAsJson) throws Exception {
        return this.mockMvc.perform(post(ROOT_URL)
            .header(AUTHORIZATION, this.tokenOfZeroIndex)
            .contentType(MediaType.APPLICATION_JSON)
            .content(contentAsJson)
        );
    }

    private ResultActions checkSendResponse(Account sender, Account receiver, ResultActions request)
        throws Exception {
        return request.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.content").exists())
            .andExpect(jsonPath("$.opened", is(false)))
            .andExpect(jsonPath("$.sendDateTime").exists())
            .andExpect(jsonPath("$.sender.id", is(sender.getId().intValue())))
            .andExpect(jsonPath("$.sender.nickname", is(sender.getNickname())))
            .andExpect(jsonPath("$.receiver.id", is(receiver.getId().intValue())))
            .andExpect(jsonPath("$.receiver.nickname", is(receiver.getNickname())));
    }

}