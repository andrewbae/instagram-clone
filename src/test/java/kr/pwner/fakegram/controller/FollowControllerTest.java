package kr.pwner.fakegram.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.pwner.fakegram.dto.account.CreateAccountDto;
import kr.pwner.fakegram.dto.follow.FollowDto;
import kr.pwner.fakegram.service.AccountService;
import kr.pwner.fakegram.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class FollowControllerTest {
    @Autowired
    AccountService accountService;
    @Autowired
    JwtService jwtService;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;

    private final String OTHER_ID = "IDIOT";
    private final String TESTER_ID = "TeSteR";
    private final String TESTER_PW = "password123";
    private final String TESTER_EMAIL = "testtest@test.com";
    private final String TESTER_NAME = "tester!";

    private void CreateTemporaryAccount() {
        CreateAccountDto.Request request = new CreateAccountDto.Request()
                .setId(TESTER_ID)
                .setPassword(TESTER_PW)
                .setEmail(TESTER_EMAIL)
                .setName(TESTER_NAME);
        accountService.CreateAccount(request);

        request = new CreateAccountDto.Request()
                .setId(OTHER_ID)
                .setPassword(TESTER_PW)
                .setEmail(TESTER_EMAIL)
                .setName(TESTER_NAME);
        accountService.CreateAccount(request);
    }

    @Transactional
    @Test
    public void Follow() throws Exception {
        CreateTemporaryAccount();
        String accessToken = jwtService.GenerateAccessToken(TESTER_ID);
        FollowDto.Request request = new FollowDto.Request().setId(OTHER_ID);
        mvc.perform(post("/api/v1/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}