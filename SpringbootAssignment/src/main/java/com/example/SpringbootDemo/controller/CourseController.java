package com.example.SpringbootDemo.controller;

import com.example.SpringbootDemo.Entity.Course;
import com.example.SpringbootDemo.Entity.Student;
import com.example.SpringbootDemo.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    public CourseService courseService;

    @GetMapping("/getCourses")
    public ResponseEntity<List<Course>> getCourses(){
        return ResponseEntity.ok(courseService.getAllCoursesOnly());
    }

    @PostMapping("/addCourse")
    public ResponseEntity<Course> addCourse(@RequestBody @Valid Course course){
        Course course1 = courseService.addCourse(course);
        return ResponseEntity.ok(course1);
    }

    @GetMapping("/courseById")
    public ResponseEntity<Course>  getCourseById(@PathVariable int id){
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @DeleteMapping("/deleteCourseById")
    public ResponseEntity<String> deleteCourseById(@PathVariable int id){
        courseService.deleteCourseById(id);
        return ResponseEntity.ok("Course deleted successfully");
    }


    @GetMapping("/noStudents")
    public ResponseEntity<List<Course>> getCoursesWithoutStudents() {
        return ResponseEntity.ok(courseService.getCoursesWithoutStudents());
    }

    @GetMapping("/count")
    public ResponseEntity<List<Object[]>> getCourseStudentCount() {
        return ResponseEntity.ok(courseService.getCourseStudentCount());
    }

    @GetMapping("/topN/{n}")
    public ResponseEntity<List<Object[]>> getTopCourses(@PathVariable int n) {
        return ResponseEntity.ok(courseService.getTopCoursesByEnrollment(n));
    }

    @PutMapping("/updateInstructor/{id}")
    public ResponseEntity<Course> updateInstructor(
            @PathVariable int id, @RequestBody Course course) throws Exception {
        return ResponseEntity.ok(courseService.updateInstructor(id, course));
    }

    @GetMapping("/fetchAllStudentsByCourses")
    public ResponseEntity<List<Course>> getAllStudentsByCourses(){
        return ResponseEntity.ok(courseService.findAllStudentsByCourses());
    }
}
