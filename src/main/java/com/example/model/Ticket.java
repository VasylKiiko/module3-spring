package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@NamedQueries({
        @NamedQuery(name = "Ticket.is_already_booked",
                query = "select t from Ticket t where t.event.id=:event_id and t.place=:place"),
        @NamedQuery(name = "Ticket.get_by_user_id",
                query = "select t from Ticket t left join fetch t.user u left join fetch t.event e where u.id=:user_id order by e.date desc "),
        @NamedQuery(name = "Ticket.get_by_event_id",
                query = "select t from Ticket t left join fetch t.user u left join fetch t.event e where e.id=:event_id order by u.email asc ")
})
@Table(name = "Ticket", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"place", "event_id"})
})
public class Ticket {
    public enum Category {STANDARD, PREMIUM, BAR}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "event_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Event event;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "place")
    private int place;

    @Column(name = "paid_up", columnDefinition = "boolean default false")
    private boolean isPaidUp;
}
