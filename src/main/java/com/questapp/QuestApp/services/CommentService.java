package com.questapp.QuestApp.services;

import com.questapp.QuestApp.entities.Comment;
import com.questapp.QuestApp.entities.Post;
import com.questapp.QuestApp.entities.User;
import com.questapp.QuestApp.repos.CommentRepository;
import com.questapp.QuestApp.requests.CommentCreateRequest;
import com.questapp.QuestApp.requests.CommentUpdateRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostService postService;
    private UserService userService;

    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    public List<Comment> getAllComments(Optional<Long> postId, Optional<Long> userId) {
        Post post = null;
        User user = null;
        if (postId.isPresent()) {
            post = postService.getPostById(postId.get());
        }
        if (userId.isPresent()) {
            user = userService.getUserById(userId.get());
        }

        if(post != null && user != null) {
            return commentRepository.findByUserIdAndPostId(user.getId(), post.getId());
        }
        if(post != null) {
            return commentRepository.findByPostId(post.getId());
        }
        if(user != null) {
            return commentRepository.findByUserId(user.getId());
        }
        return commentRepository.findAll();
   }

    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElse(null);
    }

    public Comment createComment(CommentCreateRequest commentCreateRequest) {
        User user = userService.getUserById(commentCreateRequest.getUserId());
        Post post = postService.getPostById(commentCreateRequest.getPostId());
        if (post == null || user == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(commentCreateRequest.getId());
        comment.setUser(user);
        comment.setPost(post);
        comment.setComment(commentCreateRequest.getComment());
        comment.setCreateDate(new Date());

        return commentRepository.save(comment);
    }

    public Comment updateComment(Long commentId, CommentUpdateRequest commentUpdateRequest) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            Comment commentToUpdate = comment.get();
            commentToUpdate.setComment(commentUpdateRequest.getComment());
            return commentRepository.save(commentToUpdate);
        }
        return null;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
