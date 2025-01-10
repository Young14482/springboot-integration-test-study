package com.metacoding.integrationtest.util;

import com.metacoding.integrationtest._core.util.JwtUtil;
import com.metacoding.integrationtest.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// JwtUtil, application-dev.properties
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void create_Test(){
        User user = User.builder()
                .id(1)
                .username("ssar")
                .build();

        String jwt = jwtUtil.create(user);

        System.out.println(jwt);
    }
}