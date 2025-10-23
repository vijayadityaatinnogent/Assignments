package com.librarymanagementsystem.librarymanagementSystem.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class BookDTO {
    private Long id;
    private String title;
    private int stock;
    private String authorName; // just name instead of whole AuthorEntity
}
