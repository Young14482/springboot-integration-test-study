package com.metacoding.integrationtest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc // MockMvc를 IoC에 등록해줌
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // 테스트 환경 설정 (mock >> 고정포트로 실행)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    
    private ObjectMapper om = new ObjectMapper(); // 객체를 json으로 변환해주는 라이브러리


    @Test
    @DisplayName("통합 테스트 연습: 회원가입")
    public void join_test() throws Exception {
        // 로그용 출력문에 "mma:" 붙이는이유? >> 테스크 실행 로그에서 빠르게 찾기 위해
        // 필터에서 발생한 예외는 tomcat이 처리 >> 테스트 코드에서는 발생 X
        // given
        UserRequest.JoinDTO dto = new UserRequest.JoinDTO();
        dto.setUsername("haha");
        dto.setPassword("1234");
        dto.setEmail("haha@nate.com");

        String reqBody = om.writeValueAsString(dto);// 객체의 getter를 사용해 json으로 바꿈

        // when
        ResultActions actions = mvc.perform(post("/join").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody);

        //then | $ >> json의 시작 중괄호를 의미 >> 내부의 특정 값이 배열일 경우? >> $.data[0].id
        actions.andExpect(jsonPath("$.success").value(true));
        actions.andExpect(jsonPath("$.message").value("성공"));
        actions.andExpect(jsonPath("$.data.id").value(4));
        actions.andExpect(jsonPath("$.data.username").value("haha"));
        actions.andExpect(jsonPath("$.data.email").value("haha@nate.com"));
        actions.andExpect(jsonPath("$.data.createdAt").isNotEmpty());
    }

    @Test
    @DisplayName("통합 테스트 연습: 회원가입 실패")
    public void join_fail_test() throws Exception {
        // given
        UserRequest.JoinDTO dto = new UserRequest.JoinDTO();
        dto.setUsername("ssar");
        dto.setPassword("1234");
        dto.setEmail("ssar@nate.com");

        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/join").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody);

        actions.andExpect(jsonPath("$.success").value(false));
        actions.andExpect(jsonPath("$.message").value("유저네임 중복"));
        actions.andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @DisplayName("통합 테스트 연습: 로그인")
    public void login_test() throws Exception {
        // given
        UserRequest.LoginDTO dto = new UserRequest.LoginDTO();
        dto.setUsername("ssar");
        dto.setPassword("1234");

        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/login").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody);

        String jwt = actions.andReturn().getResponse().getHeader("Authorization");
        System.out.println("mma: " + jwt);

        actions.andExpect(header().string("Authorization", Matchers.startsWith("Bearer")));
        actions.andExpect(jsonPath("$.success").value(true));
        actions.andExpect(jsonPath("$.message").value("성공"));
        actions.andExpect(jsonPath("$.data.id").value(1));
        actions.andExpect(jsonPath("$.data.username").value("ssar"));

    }

    @Test
    @DisplayName("통합 테스트 연습: 로그인")
    public void login_fail_test() throws Exception {
        // given
        UserRequest.LoginDTO dto = new UserRequest.LoginDTO();
        dto.setUsername("haha");
        dto.setPassword("1234");

        String reqBody = om.writeValueAsString(dto);

        // when
        ResultActions actions = mvc.perform(post("/login").content(reqBody).contentType(MediaType.APPLICATION_JSON));

        // eye
        String resBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println("mma: " + resBody);

//        actions.andExpect(jsonPath("$.success").value(false));
//        actions.andExpect(jsonPath("$.message").value("성공"));
//        actions.andExpect(jsonPath("$.data.id").value(1));
//        actions.andExpect(jsonPath("$.data.username").value("ssar"));
    }
}
