package com.helmigandi.finshot_bulletin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSummary {
    private Integer id;

    private String title;

    private String authorName;

    private Integer totalViews;

    private OffsetDateTime createdAt;
}
