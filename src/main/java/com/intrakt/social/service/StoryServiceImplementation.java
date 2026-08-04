package com.intrakt.social.service;

import com.intrakt.social.exceptions.UserException;
import com.intrakt.social.models.Story;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.StoryRepository;
import com.intrakt.social.request.StoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StoryServiceImplementation implements StoryService{

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserService userService;

    @Override
    public Story createStory(StoryRequest story, User user) {
        Story createdStory = new Story();
        createdStory.setCaptions((story.getCaptions()));
        createdStory.setImage(story.getImage());
        createdStory.setUser(user);
        createdStory.setTimestamp(LocalDateTime.now());

        return storyRepository.save(createdStory);
    }

    @Override
    public List<Story> findStoryByUserId(Integer userId) {
        userService.findUserById(userId);
        return storyRepository.findByUserId(userId);
    }
}
