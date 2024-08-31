package com.questapp.QuestApp.services;

import com.questapp.QuestApp.entities.Like;
import com.questapp.QuestApp.entities.Post;
import com.questapp.QuestApp.entities.User;
import com.questapp.QuestApp.repos.LikeRepository;
import com.questapp.QuestApp.requests.LikeCreateRequest;
import com.questapp.QuestApp.responses.LikeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {

    private LikeRepository likeRepository;
    private PostService postService;
    private UserService userService;

    public LikeService(LikeRepository likeRepository, PostService postService, UserService userService) {
        this.likeRepository = likeRepository;
        this.postService = postService;
        this.userService = userService;
        postService.setLikeService(this);
    }

    public List<LikeResponse> getAllLikes(Optional<Long> postId, Optional<Long> userId) {
        Post post = null;
        User user = null;
        if (postId.isPresent()) {
            post = postService.getPostById(postId.get());
        }
        if (userId.isPresent()) {
            user = userService.getUserById(userId.get());
        }

        List<Like> likes;
        if(post == null && user == null) {
            likes = likeRepository.findAll();
        }
        else if(post != null && user == null) {
            likes = likeRepository.findByPostId(post.getId());
        }
        else if(post == null && user != null) {
            likes = likeRepository.findByUserId(user.getId());
        }
        else{
            likes = likeRepository.findByUserIdAndPostId(user.getId(), post.getId());
        }

        return likes.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
    }

    public Like getLikeById(Long likeId) {
        return likeRepository.findById(likeId).orElse(null);
    }

    public Like createLike(LikeCreateRequest likeCreateRequest) {
        User user = userService.getUserById(likeCreateRequest.getUserId());
        Post post = postService.getPostById(likeCreateRequest.getPostId());
        if (post == null || user == null) {
            return null;
        }
        Like like = new Like();
        like.setId(likeCreateRequest.getId());
        like.setUser(user);
        like.setPost(post);

        return likeRepository.save(like);
    }

    public void deleteLike(Long likeId) {
        likeRepository.deleteById(likeId);
    }
}
