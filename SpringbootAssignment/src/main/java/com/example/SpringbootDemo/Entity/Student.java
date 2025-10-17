package com.example.SpringbootDemo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "Student_Table")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 3, max = 50, message = "name length must be minimum 3 length")
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull(message = "Address can not be empty")
    private String address;

    @ManyToOne
    @JoinColumn(name = "courseId")
    @JsonBackReference
//    @JsonIgnoreProperties("students")
    private Course course;

}
