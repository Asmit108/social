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
            name = "chat_user",
            joinColumns = @JoinColumn(
                    name = "chat_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_user_chat_id",
                            foreignKeyDefinition = "FOREIGN KEY (chat_id) REFERENCES chat(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "user_id",
                    referencedColumnName = "id",
                    foreignKey = @ForeignKey(
                            name = "fk_chat_user_id",
                            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE ON DELETE CASCADE" // DB-level cascade on delete
                    )
            )
    )
    private List<User> users = new ArrayList<>();

    private LocalDateTime timestamp;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages = new ArrayList<>();
}
