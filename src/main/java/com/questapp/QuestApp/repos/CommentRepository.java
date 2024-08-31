package com.questapp.QuestApp.repos;

import com.questapp.QuestApp.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long id);
    List<Comment> findByUserId(Long id);

    List<Comment> findByUserIdAndPostId(Long id, Long id1);

    @Query(value = "SELECT 'commented on', C.post_id, U.avatar, U.user_name FROM "
            + "comment C LEFT JOIN user U ON U.id = C.user_id "
            + "WHERE C.post_id IN (:postIds) LIMIT 5", nativeQuery = true)
    List<Object> findCommentsByPostId(@Param("postIds") List<Long> postIds);
}
