package com.example.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class UserAccount {
    @Id
    @Column(nullable = false, unique = true)
    Long id;

    @Column(nullable = false)
    String password;

    @Column(columnDefinition = "decimal default 0.0")
    Double amount;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    User user;
}
