package com.intrakt.social.controller;

import com.intrakt.social.models.Story;
import com.intrakt.social.models.User;
import com.intrakt.social.request.StoryRequest;
import com.intrakt.social.service.StoryService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Story> createStory(@RequestBody StoryRequest story, @RequestHeader("Authorization") String jwt) {
        User user =userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createStory(story,user));
    }

    @GetMapping("/api/story/user/{userId}")
    public ResponseEntity<List<Story>> findUserStory(@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) {
        userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(storyService.findStoryByUserId(userId));
    }
}
