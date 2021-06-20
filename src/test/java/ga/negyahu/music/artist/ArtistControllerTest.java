package ga.negyahu.music.artist;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.artist.dto.ArtistCreateDto;
import ga.negyahu.music.artist.dto.ArtistMemberCreateDto;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@CustomSpringBootTest
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test1() throws Exception {

        ArtistCreateDto artistCreateDto = new ArtistCreateDto();
        ArtistMemberCreateDto member1 = ArtistMemberCreateDto.builder()
            .email("email")
            .name("name")
            .build();
        ArtistMemberCreateDto member2 = ArtistMemberCreateDto.builder()
            .email("email")
            .name("name")
            .build();

        ResultActions perform = this.mockMvc.perform(post("/api/artist")
            .content(this.objectMapper.writeValueAsString(artistCreateDto))
        );

        perform.andDo(print());

    }

}