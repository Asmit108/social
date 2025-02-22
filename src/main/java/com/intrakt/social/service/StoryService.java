package com.intrakt.social.service;

import com.intrakt.social.models.Story;
import com.intrakt.social.models.User;

import java.util.List;

public interface StoryService {

    public Story createStory(Story story, User user);

    public List<Story> findStoryByUserId(Integer userId) throws Exception;

}
