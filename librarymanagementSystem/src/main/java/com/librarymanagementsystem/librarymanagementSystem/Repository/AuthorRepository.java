package com.librarymanagementsystem.librarymanagementSystem.Repository;

import com.librarymanagementsystem.librarymanagementSystem.Entity.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity,Long> {
    AuthorEntity findByAuthorName(String name);
}
