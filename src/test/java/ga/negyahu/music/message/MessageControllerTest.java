package ga.negyahu.music.message;

import static ga.negyahu.music.message.MessageController.ROOT_URL;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.account.Account;
import ga.negyahu.music.message.dto.MessageSendDto;
import ga.negyahu.music.message.dto.MessageUpdateDto;
import ga.negyahu.music.message.repository.MessageRepository;
import ga.negyahu.music.utils.TestUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TestUtils testUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MessageRepository messageRepository;

    private List<Account> accounts;
    private String tokenOfZeroIndex;

    @BeforeEach
    public void init() {
        int accountCnt = 3;
        this.accounts = new ArrayList<>(accountCnt);
        this.accounts = TestUtils.createAccounts(accountCnt);
        this.tokenOfZeroIndex = this.testUtils.signSupAndLogin(accounts.get(0));
        for (int i = 1; i < accountCnt; i++) {
            testUtils.signUpAccount(accounts.get(i));
        }
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
        request.andDo(print());
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
        Message sendMessage = sendMessage(sender, receiver, "message");

        ResultActions get = this.mockMvc
            .perform(get(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
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
        Account receiver = this.accounts.get(0);
        Account sender = this.accounts.get(1);
        Message sendMessage = sendMessage(sender, receiver, "message");

        ResultActions get = this.mockMvc
            .perform(get(ROOT_URL + "/{id}", sendMessage.getId())
                .header(AUTHORIZATION, this.tokenOfZeroIndex)
            )
            .andDo(print());

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
        assertNotEquals(dto.getContent(), updated.getContent(),"변경값이 적용되지 않는다.");
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
        patch2.andExpect(status().isForbidden())
        .andDo(print())
        ;


        Message updated = this.messageRepository.findById(sendMessage.getId()).get();
        assertNotEquals(dto.getContent(), updated.getContent(),"변경값이 적용되지 않는다.");
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