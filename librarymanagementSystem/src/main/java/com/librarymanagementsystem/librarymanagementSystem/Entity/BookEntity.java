package com.librarymanagementsystem.librarymanagementSystem.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    private String title;

    private int stock;

    //Many books can have one author
    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private AuthorEntity author;

    //Many to Many with members
    @ManyToMany(mappedBy = "borrowedBooks")
    @JsonBackReference
    List<MemberEntity> members;

}
