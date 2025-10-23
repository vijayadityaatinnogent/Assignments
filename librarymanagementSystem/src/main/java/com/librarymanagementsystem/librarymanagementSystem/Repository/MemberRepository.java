package com.librarymanagementsystem.librarymanagementSystem.Repository;

import com.librarymanagementsystem.librarymanagementSystem.Entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    MemberEntity findByMemberName(String name);
}
