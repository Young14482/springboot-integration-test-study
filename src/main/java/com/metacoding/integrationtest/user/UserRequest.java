package com.metacoding.integrationtest.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

public class UserRequest {

    @Data
    public static class LoginDTO {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
    }

    @Data
    public static class JoinDTO {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String email;

        User toEntity() {
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .build();
        }
    }
}
