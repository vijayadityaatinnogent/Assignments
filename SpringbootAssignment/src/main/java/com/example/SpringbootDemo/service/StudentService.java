package com.example.SpringbootDemo.service;

import com.example.SpringbootDemo.Entity.Course;
import com.example.SpringbootDemo.exception.StudentNotFoundException;
import com.example.SpringbootDemo.Entity.Student;
import com.example.SpringbootDemo.repository.CourseRepository;
import com.example.SpringbootDemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService{

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    public void deleteAllStudents(){
        studentRepository.deleteAll();
    }

    public Student updateStudent(int id,Student updatedStudent) throws StudentNotFoundException {
        Student existing = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("student not found in database"));
        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setAddress(updatedStudent.getAddress());
        if (updatedStudent.getCourse() != null && updatedStudent.getCourse().getCourseId() != null) {
            Course course = courseRepository.findById(updatedStudent.getCourse().getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found with ID: " + updatedStudent.getCourse().getCourseId()));
            existing.setCourse(course);
        }

        return studentRepository.save(existing);
    }

    public List<Student> getStudentsByCourseName(String courseName) {
        return studentRepository.findByCourseName(courseName);
    }

    public List<Student> getStudentsWithoutCourse() {
        return studentRepository.findStudentsWithoutCourse();
    }

    public List<Student> searchByCityAndInstructor(String city, String instructor) {
        return studentRepository.findByCityAndInstructor(city, instructor);
    }






}
