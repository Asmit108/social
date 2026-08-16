package com.intrakt.social.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReelsRequest {

    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Video is required")
    private String video;
}
