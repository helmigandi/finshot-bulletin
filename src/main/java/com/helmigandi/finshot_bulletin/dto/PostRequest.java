package com.helmigandi.finshot_bulletin.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @NotNull
    @Size(min = 2, max = 100)
    private String title;

    @NotNull
    @Size(min = 2, max = 10)
    private String authorName;

    @NotNull
    @Size(min = 2)
    private String content;

    @NotNull
    @Size(min = 2, max = 32)
    private String postPassword;
}
