package com.librarymanagementsystem.librarymanagementSystem.Service;

import com.librarymanagementsystem.librarymanagementSystem.DTO.AuthorDTO;
import com.librarymanagementsystem.librarymanagementSystem.DTO.BookDTO;
import com.librarymanagementsystem.librarymanagementSystem.DTO.MemberDTO;
import com.librarymanagementsystem.librarymanagementSystem.Entity.AuthorEntity;
import com.librarymanagementsystem.librarymanagementSystem.Entity.BookEntity;
import com.librarymanagementsystem.librarymanagementSystem.Entity.MemberEntity;
import com.librarymanagementsystem.librarymanagementSystem.Exception.ResourceNotFoundException;
import com.librarymanagementsystem.librarymanagementSystem.Repository.AuthorRepository;
import com.librarymanagementsystem.librarymanagementSystem.Repository.BookRepository;
import com.librarymanagementsystem.librarymanagementSystem.Repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryServiceImpl implements LibraryService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AuthorEntity addAuthor(AuthorEntity author) {
        return authorRepository.save(author);
    }
    @Override
    @Transactional
    public void deleteAuthor(Long authorId) throws ResourceNotFoundException {
        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));

        // Optional: agar author ke books bhi delete karne ho
        if (author.getBooks() != null && !author.getBooks().isEmpty()) {
            bookRepository.deleteAll(author.getBooks());
        }

        authorRepository.delete(author);
    }

    @Override
    public AuthorDTO addAuthorByDto(AuthorEntity author) {
        AuthorEntity saved = authorRepository.save(author);
        return modelMapper.map(saved, AuthorDTO.class);
    }


    @Override
    public BookEntity addBook(BookEntity book, Long authorId) throws ResourceNotFoundException {
        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));

        book.setAuthor(author);
        return bookRepository.save(book);
    }

    @Override
    public BookDTO addBookByDto(BookEntity book, Long authorId) throws ResourceNotFoundException {
        AuthorEntity author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId));

        book.setAuthor(author);
        BookEntity savedBook = bookRepository.save(book);

        // ✅ Convert Entity → DTO using ModelMapper
        BookDTO bookDTO = modelMapper.map(savedBook, BookDTO.class);



        return bookDTO;
    }


    @Override
    public MemberEntity addMember(MemberEntity member) {
        return memberRepository.save(member);
    }

    @Override
    public MemberDTO addMemberByDto(MemberEntity member) {
        MemberEntity saved = memberRepository.save(member);
        return modelMapper.map(saved, MemberDTO.class);
    }


    @Transactional
    @Override
    public void borrowBook(Long memberId, Long bookId) throws ResourceNotFoundException {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        BookEntity book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));

        if (book.getStock() < 1) {
            throw new ResourceNotFoundException("Book out of stock!");
        }

        // reduce stock
        book.setStock(book.getStock() - 1);

        // add book to member's borrowed list
        member.getBorrowedBooks().add(book);

        // save updated data
        bookRepository.save(book);
        memberRepository.save(member);
    }

    @Override
    public List<BookEntity> getBorrowedBooksByMember(Long memberId) throws ResourceNotFoundException {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));
        return member.getBorrowedBooks();
    }

    @Override
    public List<BookDTO> getBorrowedBooksByMemberDto(Long memberId) throws ResourceNotFoundException {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with id: " + memberId));

        return member.getBorrowedBooks()
                .stream()
                .map(book -> modelMapper.map(book, BookDTO.class))
                .collect(Collectors.toList());
    }

    // Optional: full list of authors
    public List<AuthorEntity> getAllAuthors() {
        return authorRepository.findAll();
    }

    // Optional: full list of books
    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

}