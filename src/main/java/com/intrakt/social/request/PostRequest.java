package com.intrakt.social.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

    @NotBlank(message = "Caption is required")
    private String caption;
    @NotBlank(message = "Image is required")
    private String image;
    private String video;
}
