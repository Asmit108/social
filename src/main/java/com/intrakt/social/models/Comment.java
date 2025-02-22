package com.intrakt.social.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;

    @ManyToOne
    @JoinColumn(
            name = "user_id",  // Foreign key column for User entity
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_commented_user_id", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE") // DB-level cascade on delete
    )
    private User user;

    @ManyToMany
    @JoinTable(
            name = "comment_likes", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "comment_id",  // Foreign key column for Post entity
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_liked_comment_id",  // Foreign key constraint name
                            foreignKeyDefinition = "FOREIGN KEY (comment_id) REFERENCES comment(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",  // Foreign key column for User entity
                    referencedColumnName = "id",  // Reference to the primary key of User entity
                    foreignKey = @ForeignKey(
                            name = "fk_liking_comment_user_id",  // Foreign key constraint name
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            )
    )
    private List<User> liked = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "post_id",  // Foreign key column for User entity
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_commented_post_id", foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON UPDATE CASCADE ON DELETE CASCADE") // DB-level cascade on delete
    )
    private Post post;

    private LocalDateTime createdAt;

}
