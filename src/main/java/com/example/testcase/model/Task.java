package com.example.testcase.model;

import com.example.testcase.model.enums.Priority;
import com.example.testcase.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String message;
    String header;
    Status status;
    Priority priority;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User author;
    @ManyToOne
    @JoinColumn(name = "user_id")
    User executor;
    @OneToMany
    List<Comment> comments;
}
