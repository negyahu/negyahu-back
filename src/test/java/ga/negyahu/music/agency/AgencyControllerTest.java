package ga.negyahu.music.agency;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

        this.mockMvc.perform(post("/api/agency")
            .contentType(MediaType.APPLICATION_JSON)
        );

    }

}