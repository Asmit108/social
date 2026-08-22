package com.intrakt.social.controller;

import com.intrakt.social.models.Story;
import com.intrakt.social.models.User;
import com.intrakt.social.request.StoryRequest;
import com.intrakt.social.service.StoryService;
import com.intrakt.social.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Story Controller", description = "Endpoints for managing stories")
public class StoryController {

    private final StoryService storyService;
    private final UserService userService;

    @Autowired
    public StoryController(StoryService storyService, UserService userService) {
        this.storyService = storyService;
        this.userService = userService;
    }

    @PostMapping("/story")
    @Operation(summary = "Create a new story", description = "Creates a new story for the authenticated user.")
    public ResponseEntity<Story> createStory(@RequestBody StoryRequest story, @RequestHeader("Authorization") String jwt) {
        User user =userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createStory(story,user));
    }

    @GetMapping("/story/user/{userId}")
    @Operation(summary = "Get stories by user ID", description = "Retrieves all stories created by the user identified by the provided ID.")
    public ResponseEntity<List<Story>> findUserStory(@PathVariable("userId") Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(storyService.findStoryByUserId(userId));
    }

    @DeleteMapping("/admin/story/{storyId}")
    @Operation(summary = "Delete a story", description = "Deletes the story identified by the provided ID.")
    public ResponseEntity<String> deleteStory(@PathVariable("storyId") Integer storyId) {
        storyService.deleteStoryById(storyId);
        return ResponseEntity.status(HttpStatus.OK).body("Story deleted successfully");
    }
}
