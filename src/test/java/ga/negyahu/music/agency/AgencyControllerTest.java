package ga.negyahu.music.agency;

import static ga.negyahu.music.agency.AgencyController.ROOT_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.agency.dto.AgencyCreateDto;
import ga.negyahu.music.utils.AgencyTestUtils;
import ga.negyahu.music.utils.TestUtils;
import ga.negyahu.music.utils.annotation.CustomSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@CustomSpringBootTest
@AutoConfigureMockMvc
public class AgencyControllerTest {

    @Autowired
    private TestUtils testUtils;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 소속사_등록신청_테스트() throws Exception {

        AgencyCreateDto createDto = AgencyTestUtils.agencyCreateDto();

        String content = objectMapper.writeValueAsString(createDto);

        ResultActions perform = this.mockMvc.perform(post(ROOT_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
        );

        perform.andDo(print());


    }

}