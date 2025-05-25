package com.fakepostdetector.model;

import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;

@Data
@Builder
public class Post {
    private Long postId;
    private Long userId;
    private String content;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double credibilityScore;
    private Boolean isFake;
} 