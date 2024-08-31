package com.questapp.QuestApp.services;

import com.questapp.QuestApp.entities.Comment;
import com.questapp.QuestApp.entities.Like;
import com.questapp.QuestApp.entities.User;
import com.questapp.QuestApp.repos.CommentRepository;
import com.questapp.QuestApp.repos.LikeRepository;
import com.questapp.QuestApp.repos.PostRepository;
import com.questapp.QuestApp.repos.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    PostRepository postRepository;

    public UserService(UserRepository userRepository, LikeRepository likeRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long userId, User newUser) {
        Optional<User> oldUser = userRepository.findById(userId);
        if (oldUser.isPresent()) {
            User updatedUser = oldUser.get();
            if (newUser.getUserName() != null) {
                updatedUser.setUserName(newUser.getUserName());
            }
            if (newUser.getPassword() != null) {
                updatedUser.setPassword(newUser.getPassword());
            }
            if (newUser.getAvatar() != 0) {
                updatedUser.setAvatar(newUser.getAvatar());
            }
            return userRepository.save(updatedUser);
        } else {
            // Custom Exception
            return null;
        }
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public List<Object> getUserActivity(Long userId) {
        List<Long> postIds = postRepository.findTopByUserId(userId);
        if (postIds.isEmpty()) {
            return null;
        }
        List<Object> lastComments = commentRepository.findCommentsByPostId(postIds);
        List<Object> lastLikes = likeRepository.findLikesByPostId(postIds);
        List<Object> activity = new ArrayList<>();
        activity.addAll(lastComments);
        activity.addAll(lastLikes);
        return activity;
    }
}
