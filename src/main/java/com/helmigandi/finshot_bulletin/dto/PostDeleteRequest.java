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
public class PostDeleteRequest {
    @NotNull
    @Size(min = 2, max = 32)
    private String postPassword;
}
