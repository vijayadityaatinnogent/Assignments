package com.librarymanagementsystem.librarymanagementSystem.Service;

import com.librarymanagementsystem.librarymanagementSystem.DTO.AuthorDTO;
import com.librarymanagementsystem.librarymanagementSystem.DTO.BookDTO;
import com.librarymanagementsystem.librarymanagementSystem.DTO.MemberDTO;
import com.librarymanagementsystem.librarymanagementSystem.Entity.AuthorEntity;
import com.librarymanagementsystem.librarymanagementSystem.Entity.BookEntity;
import com.librarymanagementsystem.librarymanagementSystem.Entity.MemberEntity;
import com.librarymanagementsystem.librarymanagementSystem.Exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface LibraryService {
    BookDTO addBookByDto(BookEntity book, Long authorId) throws ResourceNotFoundException;

    AuthorDTO addAuthorByDto(AuthorEntity author);

    BookEntity addBook(BookEntity book, Long authorId) throws ResourceNotFoundException;
    AuthorEntity addAuthor(AuthorEntity author);
    void deleteAuthor(Long authorId) throws ResourceNotFoundException;
    MemberEntity addMember(MemberEntity member);
    List<BookEntity> getBorrowedBooksByMember(Long memberId) throws ResourceNotFoundException;

    MemberDTO addMemberByDto(MemberEntity member);

    @Transactional
    void borrowBook(Long memberId, Long bookId) throws ResourceNotFoundException;

    List<BookDTO> getBorrowedBooksByMemberDto(Long memberId) throws ResourceNotFoundException;
}
