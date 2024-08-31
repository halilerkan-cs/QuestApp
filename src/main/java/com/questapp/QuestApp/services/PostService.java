package com.questapp.QuestApp.services;

import com.questapp.QuestApp.entities.Like;
import com.questapp.QuestApp.entities.Post;
import com.questapp.QuestApp.entities.User;
import com.questapp.QuestApp.repos.PostRepository;
import com.questapp.QuestApp.requests.PostCreateRequest;
import com.questapp.QuestApp.requests.PostUpdateRequest;
import com.questapp.QuestApp.responses.LikeResponse;
import com.questapp.QuestApp.responses.PostResponse;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    private PostRepository postRepository;
    private LikeService likeService;
    private UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public void setLikeService(LikeService likeService) {
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId){
        List<Post> list;
        if(userId.isPresent()) {
            list = postRepository.findByUserId(userId.get());
        }
        list = postRepository.findAll();

        return list.stream().map(p -> {
            List<LikeResponse> likes = likeService.getAllLikes(Optional.of(p.getId()), Optional.ofNullable(null));
            return new PostResponse(p, likes);
        }).collect(Collectors.toList());
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public PostResponse getPostByIdWithLikes(Long id){
        Post post = postRepository.findById(id).orElse(null);
        List<LikeResponse> likes = likeService.getAllLikes(Optional.of(post.getId()), Optional.ofNullable(null));
        if(post == null)
            return new PostResponse(null, likes);
        return new PostResponse(post, likes);
    }

    public Post createPost(PostCreateRequest postCreateRequest) {
        User user = userService.getUserById(postCreateRequest.getUserId());
        if(user == null)
            return null;
        Post post = new Post();
        post.setId(postCreateRequest.getId());
        post.setTitle(postCreateRequest.getTitle());
        post.setContent(postCreateRequest.getContent());
        post.setUser(user);
        post.setCreateDate(new Date());

        return postRepository.save(post);
    }

    public Post updatePostById(Long id, PostUpdateRequest postUpdateRequest) {
        Optional<Post> post = postRepository.findById(id);
        if(post.isPresent()) {
            Post postUpdate = post.get();
            postUpdate.setTitle(postUpdateRequest.getTitle());
            postUpdate.setContent(postUpdateRequest.getContent());
            return postRepository.save(postUpdate);
        }
        return null;
    }

    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }
}
