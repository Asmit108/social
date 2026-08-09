package com.intrakt.social.controller;

import com.intrakt.social.models.Reels;
import com.intrakt.social.models.User;
import com.intrakt.social.request.ReelsRequest;
import com.intrakt.social.service.ReelsService;
import com.intrakt.social.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReelsController {

    private final ReelsService reelsService;
    private final UserService userService;

    @Autowired
    public ReelsController(ReelsService reelsService, UserService userService) {
        this.reelsService = reelsService;
        this.userService = userService;
    }

    @PostMapping("/reels")
    public ResponseEntity<Reels> createReels(@RequestBody ReelsRequest reels, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwt(jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(reelsService.createReel(reels,user));
    }

    @GetMapping("/reels")
    public ResponseEntity<List<Reels>> findAllReels(){
        return ResponseEntity.status(HttpStatus.OK).body(reelsService.findAllReels());
    }

    @GetMapping("/reels/user/{userId}")
    public ResponseEntity<List<Reels>> findUsersReels(@PathVariable("userId") Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(reelsService.findUsersReel(userId));
    }

    @DeleteMapping("/admin/reels/{reelsId}")
    public ResponseEntity<String> deleteReels(@PathVariable("reelsId") Integer reelsId) {
        reelsService.deleteReelsById(reelsId);
        return ResponseEntity.status(HttpStatus.OK).body("Reels deleted successfully");
    }
}
