package com.questapp.QuestApp.repos;

import com.questapp.QuestApp.entities.Comment;
import com.questapp.QuestApp.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserIdAndPostId(Long id, Long id1);

    List<Like> findByPostId(Long id);

    List<Like> findByUserId(Long id);

    @Query(value =  "SELECT 'liked', C.post_id, U.avatar, U.user_name FROM "
            + "comment C LEFT JOIN user U ON U.id = C.user_id "
            + "WHERE C.post_id IN (:postIds) LIMIT 5"
            , nativeQuery = true)
    List<Object> findLikesByPostId(@Param("postIds") List<Long> postIds);
}
