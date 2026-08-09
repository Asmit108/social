package com.intrakt.social.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")//  Uses a property (e.g., id) as the identifier to avoid infinite circular references
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name="firstName")
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    public enum Role {
        USER,
        ADMIN
    }
    private Role role;
    public enum Gender {
        MALE,
        FEMALE
    }
    private Gender gender;

    @ManyToMany
    @JoinTable(
            name = "user_followers",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_follower_id",
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "follower_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_follower_id",
                            foreignKeyDefinition = "FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE"
                    )
            )
    )
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_followings",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_follwing_id",
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "following_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_following_id",
                            foreignKeyDefinition = "FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE"
                    )
            )
    )
    private List<User> followings = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_saved_posts",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_saved_id",
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_saved_post_id",
                            foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE"
                    )
            )
    )
    private List<Post> savedPost = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Post> createdPost = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Story> createdStory = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reels> createdReel = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Message> createdMessage = new ArrayList<>();
}
