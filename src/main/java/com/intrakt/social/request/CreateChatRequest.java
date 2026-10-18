package com.intrakt.social.request;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRequest {

    @Positive(message = "ReceiverId must be a positive number")
    private Integer userId;
}
