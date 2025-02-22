package com.intrakt.social.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String content;

    private String image;

    @ManyToOne
    @JoinColumn(
            name = "user_id",  // Foreign key column for User entity
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_create_message_user_id", foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE") // DB-level cascade on delete
    )
    private User user;

    @ManyToOne
    @JoinColumn(
            name = "chat_id",  // Foreign key column for User entity
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_create_chat_id", foreignKeyDefinition = "FOREIGN KEY (chat_id) REFERENCES chat(id) ON DELETE CASCADE ON UPDATE CASCADE") // DB-level cascade on delete
    )
    private Chat chat;

    private LocalDateTime timestamp;

}
