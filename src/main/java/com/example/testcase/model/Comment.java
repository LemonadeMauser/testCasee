package com.example.testcase.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String text;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User author;
    @ManyToOne
    @JoinColumn(name = "task_id")
    Task task;
}
