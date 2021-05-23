package ga.negyahu.music.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import ga.negyahu.music.security.utils.JwtTokenProvider;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    private final JwtTokenProvider provider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res,
        Authentication auth) throws IOException, ServletException {

        String token = provider.createToken(auth);

        setTokenInHeader(res, token);
        setTokenInBody(res, token);
    }

    private void setTokenInHeader(HttpServletResponse res, String token) {
        res.setStatus(HttpServletResponse.SC_OK);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setHeader(HttpHeaders.AUTHORIZATION, token);
    }

    private void setTokenInBody(HttpServletResponse res, String token) throws IOException {
        PrintWriter writer = res.getWriter();
        Map<String, String> result = Map.of("token", token);
        String tokenAsJson = objectMapper.writeValueAsString(result);

        writer.print(tokenAsJson);
        writer.flush();
    }
}
