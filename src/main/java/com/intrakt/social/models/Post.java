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


//https://www.baeldung.com/jpa-cascade-types
//Cascade Type PERSIST propagates the persist operation from a parent to a child entity.
//        When we save the person entity, the address entity will also get saved.
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

//    Correct! The foreign key column (user_id) in the Post table, which is defined using
//    @ManyToOne and @JoinColumn, is independent of the join table (if one exists).
//    Deleting a row in the join table created by onetomany side in user table will not affect this
//    column in the Post table, and
//    vice versa,.........generally we not use join table
    @ManyToOne  // Cascade persist operations to User
    @JoinColumn(
            name = "user_id",  // Foreign key column for User entity
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_create_user_id", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE") // DB-level cascade on delete
    )
    private User user;

    @ManyToMany
    @JoinTable(
            name = "post_likes", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "post_id",  // Foreign key column for Post entity
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_liked_post_id",  // Foreign key constraint name
                            foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",  // Foreign key column for User entity
                    referencedColumnName = "id",  // Reference to the primary key of User entity
                    foreignKey = @ForeignKey(
                            name = "fk_user_liking_id",  // Foreign key constraint name
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            )
    )
    private List<User> liked = new ArrayList<>();

   @OneToMany(mappedBy = "post")
//    @JoinTable(
//            name = "post_comments", // Name of the join table
//            joinColumns = @JoinColumn(
//                    name = "post_id",  // Foreign key column for Post entity
//                    referencedColumnName = "id",
//                    foreignKey = @ForeignKey(
//                            name = "fk_comment_post_id",  // Foreign key constraint name
//                            foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
//                    )
//            ),
//            inverseJoinColumns = @JoinColumn(
//                    name = "comment_id",  // Foreign key column for User entity
//                    referencedColumnName = "id",  // Reference to the primary key of User entity
//                    foreignKey = @ForeignKey(
//                            name = "fk_post_comment_id",  // Foreign key constraint name
//                            foreignKeyDefinition = "FOREIGN KEY (comment_id) REFERENCES comment(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
//                    )
//            )
//    )
//orphanRemoval=true means if post is deleted its child here:comments housld also be deleted
   //but it is already managed other side,,,,so we don;t need this
    private List<Comment> comments = new ArrayList<>();

    //though this fiedl is only rwquird to access user who saved,,,,,to avoid multiple join tables to
    //formed,,,linked to already formed join table
    @ManyToMany(mappedBy = "savedPost")//this is reverse side
    //in user model,corresponding field is owning side as that created join table
//    If you attempt to apply cascade on the inverse side (mappedBy), it typically doesn’t have an
//    effect because cascading operations are only relevant to the owning side. The inverse side
//    only reflects the relationship; it does not manage it.
    private List<User> savedUser = new ArrayList<>();

    private LocalDateTime createdAt;
}
