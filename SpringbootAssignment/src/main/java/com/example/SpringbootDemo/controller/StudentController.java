package com.example.SpringbootDemo.controller;

import com.example.SpringbootDemo.exception.StudentNotFoundException;
import com.example.SpringbootDemo.model.Student;
import com.example.SpringbootDemo.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getAllStudents")
    public ResponseEntity<List<Student>> getAllStudents(){
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @PostMapping("/addStudent")
    public ResponseEntity<Student> addStudent(@RequestBody @Valid Student student){
       Student student1 = studentService.addStudent(student);
       return ResponseEntity.ok(student1);
    }

    @GetMapping("/getStudentById/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) throws StudentNotFoundException {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/deleteStudentById/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable int id) throws StudentNotFoundException {
        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Student delete successfully with Id : " + id);
    }

    @PutMapping("/updateStudentById/{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable int id, @RequestBody @Valid Student student) throws StudentNotFoundException {
        Student student1 = studentService.updateStudent(id, student);
        return ResponseEntity.ok(student1);
    }

}
