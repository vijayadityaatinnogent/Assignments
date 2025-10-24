package com.librarymanagementsystem.librarymanagementSystem.Controller;

import com.librarymanagementsystem.librarymanagementSystem.DTO.AuthorDTO;
import com.librarymanagementsystem.librarymanagementSystem.DTO.BookDTO;
import com.librarymanagementsystem.librarymanagementSystem.DTO.MemberDTO;
import com.librarymanagementsystem.librarymanagementSystem.Entity.AuthorEntity;
import com.librarymanagementsystem.librarymanagementSystem.Entity.BookEntity;
import com.librarymanagementsystem.librarymanagementSystem.Entity.MemberEntity;
import com.librarymanagementsystem.librarymanagementSystem.Exception.ResourceNotFoundException;
import com.librarymanagementsystem.librarymanagementSystem.Service.LibraryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/library/dto")
public class DtoController {

        @Autowired
        private LibraryService libraryService;

        @Autowired
        private ModelMapper modelMapper;

        // Add a new author
        @PostMapping("/authors")
        public AuthorDTO addAuthor(@RequestBody AuthorDTO authorDTO) {
            AuthorEntity authorEntity = modelMapper.map(authorDTO, AuthorEntity.class);
            AuthorEntity savedAuthor = libraryService.addAuthor(authorEntity);
            return modelMapper.map(savedAuthor, AuthorDTO.class);
        }

        // Add a new book (linked with author)
        @PostMapping("/books/{authorId}")
        public BookDTO addBook(@RequestBody BookDTO bookDTO, @PathVariable Long authorId) throws ResourceNotFoundException {
            BookEntity bookEntity = modelMapper.map(bookDTO, BookEntity.class);
            BookEntity savedBook = libraryService.addBook(bookEntity, authorId);
            return modelMapper.map(savedBook, BookDTO.class);
        }

        // Add new member
        @PostMapping("/members")
        public MemberDTO addMember(@RequestBody MemberDTO memberDTO) {
            MemberEntity memberEntity = modelMapper.map(memberDTO, MemberEntity.class);
            MemberEntity savedMember = libraryService.addMember(memberEntity);
            return modelMapper.map(savedMember, MemberDTO.class);
        }

        // Borrow a book
        @PostMapping("/borrow/{memberId}/{bookId}")
        public String borrowBook(@PathVariable Long memberId, @PathVariable Long bookId) throws ResourceNotFoundException {
            libraryService.borrowBook(memberId, bookId);
            return "Book borrowed successfully!";
        }

        // Get all borrowed books for a member
        @GetMapping("/members/{memberId}/borrowed-books")
        public List<BookDTO> getBorrowedBooks(@PathVariable Long memberId) throws ResourceNotFoundException {
            List<BookEntity> books = libraryService.getBorrowedBooksByMember(memberId);
            return books.stream()
                    .map(book -> modelMapper.map(book, BookDTO.class))
                    .collect(Collectors.toList());
        }
    }

