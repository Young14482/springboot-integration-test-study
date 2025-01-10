package com.metacoding.integrationtest.user;

import lombok.Data;

import java.time.format.DateTimeFormatter;

public class UserResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String username;
        private String email;
        private String createdAt;

        public DTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        }
    }
}
