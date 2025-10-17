package com.example.SpringbootDemo.repository;

import com.example.SpringbootDemo.Entity.Course;
import com.example.SpringbootDemo.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    // Get courses without students
    @Query("SELECT c FROM Course c LEFT JOIN c.students s WHERE s IS NULL")
    List<Course> findCoursesWithoutStudents();

    // course details along with total student count
    @Query("SELECT c.courseName, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c.courseName")
    List<Object[]> getCourseStudentCount();

    // top N courses by student enrollment
    @Query("SELECT c.courseName, COUNT(s) AS studentCount FROM Course c LEFT JOIN c.students s GROUP BY c.courseName ORDER BY studentCount DESC LIMIT :n")
    List<Object[]> getTopCoursesByEnrollment(int n);

    @Query("SELECT DISTINCT c FROM Course c LEFT JOIN FETCH c.students")
    List<Course> finddAllStudentsByCourses();

    @Query("SELECT c FROM Course c")
    List<Course> findAllCoursesOnly();
}
