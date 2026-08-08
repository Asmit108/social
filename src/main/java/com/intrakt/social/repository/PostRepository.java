package com.intrakt.social.repository;

import com.intrakt.social.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findPostByUserId(Integer userId);

    @Query("""
    SELECT p
    FROM Post p
    WHERE NOT EXISTS (
        SELECT p2
        FROM Post p2
        WHERE SIZE(p2.liked) > SIZE(p.liked)
           OR (
               SIZE(p2.liked) = SIZE(p.liked)
               AND SIZE(p2.comments) > SIZE(p.comments)
           )
           OR (
               SIZE(p2.liked) = SIZE(p.liked)
               AND SIZE(p2.comments) = SIZE(p.comments)
               AND p2.createdAt > p.createdAt
           )
    )
""")
    List<Post> getTopPosts();
}
