package com.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@NamedQueries({
        @NamedQuery(name = "get_average_rating_for_event",
                query = "select avg(comm.rating) from Comment comm where comm.event.id=:event_id"),
        @NamedQuery(name = "get_comments_for_event",
                query = "select comm from Comment comm where comm.event.id=:event_id order by comm.creationTime desc"),
        @NamedQuery(name = "get_comments_for_user",
                query = "select comm from Comment comm where comm.user.id=:user_id order by comm.creationTime desc")
})
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    LocalDateTime creationTime;

    String comment;

    int rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;

}
