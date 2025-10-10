package com.example.SpringbootDemo.service;

import com.example.SpringbootDemo.exception.StudentNotFoundException;
import com.example.SpringbootDemo.model.Student;
import com.example.SpringbootDemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService{

    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(Student student){
        return studentRepository.save(student);
    }

    public Student getStudentById(int id) throws StudentNotFoundException {
        return studentRepository.findById(id).orElseThrow( () -> new StudentNotFoundException("Student not found in database"));
    }

    public List<Student> getAllStudents(){
        return studentRepository.findAll();
    }

    public void deleteStudentById(int id) throws StudentNotFoundException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("student not found!"));
        studentRepository.delete(student);
    }

    public Student updateStudent(int id,Student updateStudent) throws StudentNotFoundException {
        Student existing = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("studnet not found in database"));
        existing.setName(updateStudent.getName());
        existing.setEmail(updateStudent.getEmail());
        existing.setAddress(updateStudent.getAddress());
        return studentRepository.save(existing);
    }






}
