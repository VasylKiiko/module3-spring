package com.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NamedQueries({
        @NamedQuery(name = "Event.get_event_by_title",
                query = "select e from Event e where lower(e.title) like lower(concat('%', :title ,'%')) "),
        @NamedQuery(name = "Event.get_events_for_day",
                query = "select e from Event e where e.date = :date")
})

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"title", "date"})
})
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, include = "non-lazy")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    Double ticketPrice;

    @Column(name = "title", nullable = false)
    String title;

    @Column(name = "date", nullable = false)
    LocalDate date;

    @Column(columnDefinition = "double precision default 0")
    double rating;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    @ToString.Exclude
    List<Ticket> eventTickets;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true)
    @ToString.Exclude
    List<Comment> comments;

}
