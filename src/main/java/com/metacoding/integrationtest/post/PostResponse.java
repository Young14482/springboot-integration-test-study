package com.metacoding.integrationtest.post;

import lombok.Data;

import java.time.format.DateTimeFormatter;

public class PostResponse {

    @Data
    public static class DTO {
        private Integer id;
        private String title;
        private String content;
        private String createdAt;
        private Integer userId;
        private String username;

        public DTO(Post post) {
            this.id = post.getId();
            this.title = post.getTitle();
            this.content = post.getContent();
            this.createdAt = post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            this.userId = post.getUser().getId();
            this.username = post.getUser().getUsername();
        }
    }
}
