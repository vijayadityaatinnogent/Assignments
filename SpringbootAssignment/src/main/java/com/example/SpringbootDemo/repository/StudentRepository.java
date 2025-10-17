package com.example.SpringbootDemo.repository;

import com.example.SpringbootDemo.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;


@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
// here Student represents the name of Entity class (model) in this case
// and Integer represents ki primary key ka return type kya hoga.

    // students by course name
    @Query("SELECT s FROM Student s WHERE s.course.courseName = :courseName")
    List<Student> findByCourseName(String courseName);

    // students not enrolled in any course
    @Query("SELECT s FROM Student s WHERE s.course IS NULL")
    List<Student> findStudentsWithoutCourse();

    // Search students by city (address) and course instructor
    @Query("SELECT s FROM Student s WHERE s.address = :city AND s.course.courseInstructor = :instructor")
    List<Student> findByCityAndInstructor(@Param("city") String city, @Param("instructor") String instructor);

//    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
//    List<Student> finddAllStudentsByCourses();
}