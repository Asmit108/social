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
public class Post {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String caption;

    private String image;

    private String video;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_create_user_id", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE") // DB-level cascade on delete
    )
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_liked_post_id",
                            foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_liking_id",
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            )
    )
    private List<User> liked = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany(mappedBy = "savedPost")
    private List<User> savedUser = new ArrayList<>();

    private LocalDateTime createdAt;
}
