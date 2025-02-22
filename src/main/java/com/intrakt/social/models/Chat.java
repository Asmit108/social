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
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String chat_name;

    private String chat_image;

    @ManyToMany
    @JoinTable(
            name = "chat_user", // Name of the join table
            joinColumns = @JoinColumn(
                    name = "chat_id",  // Foreign key column for Post entity
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_chat_id",  // Foreign key constraint name
                            foreignKeyDefinition = "FOREIGN KEY (chat_id) REFERENCES chat(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",  // Foreign key column for User entity
                    referencedColumnName = "id",  // Reference to the primary key of User entity
                    foreignKey = @ForeignKey(
                            name = "fk_chat_user_id",  // Foreign key constraint name
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            )
    )
    private List<User> users = new ArrayList<User>();

    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();

}
