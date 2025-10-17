package com.example.SpringbootDemo.service;

import com.example.SpringbootDemo.Entity.Course;
import com.example.SpringbootDemo.Entity.Student;
import com.example.SpringbootDemo.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public Course addCourse(Course course){
        return courseRepository.save(course);
    }

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }

    public List<Course> getAllCoursesOnly() {
        List<Course> courses = courseRepository.findAllCoursesOnly();

        // students field ko null kar de (to hide in response)
        for (Course c : courses) {
            c.setStudents(null);
        }

        return courses;
    }

    public List<Course> findAllStudentsByCourses(){
        return courseRepository.finddAllStudentsByCourses();
    }

    public Course getCourseById(int id){
        return courseRepository.findById(id).orElse(null);

    }

    public void deleteCourseById(int id){
        Course course = courseRepository.findById(id).orElse(null);
        courseRepository.delete(course);
    }


    public List<Course> getCoursesWithoutStudents() {
        return courseRepository.findCoursesWithoutStudents();
    }

    public List<Object[]> getCourseStudentCount() {
        return courseRepository.getCourseStudentCount();
    }

    public List<Object[]> getTopCoursesByEnrollment(int n) {
        return courseRepository.getTopCoursesByEnrollment(n);
    }

    public Course updateInstructor(int courseId, Course course) throws Exception {
        Course new_course = courseRepository.findById(courseId)
                .orElseThrow(() -> new Exception("Course not found"));
        new_course.setCourseInstructor(course.getCourseInstructor());
        return courseRepository.save(new_course);
    }
}
