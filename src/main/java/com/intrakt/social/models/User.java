package com.intrakt.social.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
//note:@JsonIgnore- this is used to avoid infinite ciruclar reference in api response.it directly ignores
//the particular field to be shown in response
//https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion
//@Json Managed reference and @Json back reference



//in user---> we have savedposts, in post----> we have users who saved posts
// so, we use jointable user_savedpost for list<post> in user model,,,,--->owning side
//now, we don;t need to create anther jointable savedpost_user in the post model for users
//so,in post model,,,,we use:----->inverse side
//@ManyToMany(mappedBy = "savedposts")
//private List<User> users;


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
    private String role;
    private String gender;

    //there is another methods: manually create a join table model, or we can use @ElementCollection
//    The @ElementCollection annotation is used for storing collections of basic types
//            (like Long, String, etc.) or embeddable objects.
//    although extra table will be created same like this, but There is no foreign key constraint
//    in the database to ensure the validity of followerIds
//    @ElementCollection
//    private List<Long> followerIds = new ArrayList<>();
// insertion integrity is by default
// for deletion integrity, we need to use either ON DELETE CASCADE (DB feature)
// or CASCADETYPE.REMOVE (JPA feature)
// for updation, use ON UPDATE CASCADE(DB feature)
//Foreign key constraints must have unique names globally within database schema,
//regardless of whether they refer to different tables or columns.
    @ManyToMany
    @JoinTable(
            name = "user_followers", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_follower_id",  // Name of the foreign key
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "follower_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_follower_id",
                            foreignKeyDefinition = "FOREIGN KEY (follower_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE"
                    )
            )
    )
    private List<User> followers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_followings", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_follwing_id",  // Name of the foreign key
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "following_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_following_id",
                            foreignKeyDefinition = "FOREIGN KEY (following_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE"
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
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE"
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "post_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_saved_post_id",
                            foreignKeyDefinition = "FOREIGN KEY (post_id) REFERENCES post(id) ON UPDATE CASCADE ON DELETE CASCADE"
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


//    public User(){
//
//    }
//
//    public User(Integer id, String firstName, String lastName, String email, String password, String gender, List<Message> createdMessage, List<Reels> createdReel, List<Story> createdStory, List<User> followers, List<Post> createdPost,List<User> followings, List<Post> savedPost) {
//        this.id = id;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.email = email;
//        this.password = password;
//        this.gender = gender;
//        this.followers = followers;
//        this.followings = followings;
//        this.createdPost = createdPost;
//        this.savedPost = savedPost;
//        this.createdStory = createdStory;
//        this.createdReel = createdReel;
//        this.createdMessage = createdMessage;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public List<User> getFollowers() {
//        return followers;
//    }
//
//    public void setFollowers(List<User> followers) {
//        this.followers = followers;
//    }
//
//    public List<User> getFollowings() {
//        return followings;
//    }
//
//    public void setFollowings(List<User> followings) {
//        this.followings = followings;
//    }
//
//    public List<Post> getSavedPost() {
//        return savedPost;
//    }
//
//    public void setSavedPost(List<Post> savedPost) {
//        this.savedPost = savedPost;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public List<Post> getCreatedPost() {
//        return createdPost;
//    }
//
//    public void setCreatedPost(List<Post> createdPost) {
//        this.createdPost = createdPost;
//    }
//
//    public List<Story> getCreatedStory() {
//        return createdStory;
//    }
//
//    public void setCreatedStory(List<Story> createdStory) {
//        this.createdStory = createdStory;
//    }

    public List<Reels> getCreatedReel() {
        return createdReel;
    }

    public void setCreatedReel(List<Reels> createdReel) {
        this.createdReel = createdReel;
    }

    public List<Message> getCreatedMessage() {
        return createdMessage;
    }

    public void setCreatedMessage(List<Message> createdMessage) {
        this.createdMessage = createdMessage;
    }
}
