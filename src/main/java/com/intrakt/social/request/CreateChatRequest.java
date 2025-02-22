package com.intrakt.social.request;

import com.intrakt.social.models.User;

public class CreateChatRequest {

    private Integer userId;


    public CreateChatRequest() {

    }

    public CreateChatRequest(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
