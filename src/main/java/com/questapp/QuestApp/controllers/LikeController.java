package com.questapp.QuestApp.controllers;

import com.questapp.QuestApp.entities.Like;
import com.questapp.QuestApp.requests.LikeCreateRequest;
import com.questapp.QuestApp.responses.LikeResponse;
import com.questapp.QuestApp.services.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/likes")
public class LikeController {

    private LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @GetMapping
    public List<LikeResponse> getAllLikes(@RequestParam Optional<Long> postId,
                                          @RequestParam Optional<Long> userId) {
        return likeService.getAllLikes(postId, userId);
    }

    @GetMapping("/{likeId}")
    public Like getLike(@PathVariable Long likeId) {
        return likeService.getLikeById(likeId);
    }

    @PostMapping
    public Like addLike(@RequestBody LikeCreateRequest likeCreateRequest) {
        return likeService.createLike(likeCreateRequest);
    }

    @DeleteMapping("/{likeId}")
    public void deleteLike(@PathVariable Long likeId) {
        likeService.deleteLike(likeId);
    }

}
