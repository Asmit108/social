package com.intrakt.social.controller;

import com.intrakt.social.exceptions.UserException;
import com.intrakt.social.models.Story;
import com.intrakt.social.models.User;
import com.intrakt.social.service.StoryService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StoryController {

    private final StoryService storyService;
    private final UserService userService;

    @Autowired
    public StoryController(StoryService storyService, UserService userService) {
        this.storyService = storyService;
        this.userService = userService;
    }

    @PostMapping("/api/story")
    public Story createStory(@RequestBody Story story, @RequestHeader("Authorization") String jwt) throws UserException {
        User user =userService.findUserByJwt(jwt);
        return storyService.createStory(story,user);
    }

    @GetMapping("/api/story/user/{userId}")
    public List<Story> findUserStory(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) throws Exception {
        userService.findUserByJwt(jwt);
        return storyService.findStoryByUserId(userId);
    }
}
