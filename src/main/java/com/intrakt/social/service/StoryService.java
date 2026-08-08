package com.intrakt.social.service;

import com.intrakt.social.models.Story;
import com.intrakt.social.models.User;
import com.intrakt.social.request.StoryRequest;

import java.util.List;

public interface StoryService {

    Story createStory(StoryRequest story, User user);

    List<Story> findStoryByUserId(Integer userId);

    void deleteStoryById(Integer storyId);
}
