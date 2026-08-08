package com.intrakt.social.service;

import com.intrakt.social.models.Story;
import com.intrakt.social.models.User;
import com.intrakt.social.repository.StoryRepository;
import com.intrakt.social.request.StoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StoryServiceImplementation implements StoryService{

    private final StoryRepository storyRepository;
    private final UserService userService;

    @Autowired
    public StoryServiceImplementation(StoryRepository storyRepository, UserService userService) {
        this.storyRepository = storyRepository;
        this.userService = userService;
    }

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

    @Override
    public void deleteStoryById(Integer storyId) {
        Story story = storyRepository.findStoryById(storyId);
        if (story == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Story not found");
        }
        storyRepository.deleteById(storyId);
    }
}
