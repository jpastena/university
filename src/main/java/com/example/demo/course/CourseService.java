package com.example.demo.course;

import com.example.demo.student.Student;
import com.example.demo.student.StudentService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;

    public CourseService(CourseRepository courseRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    public List<Course> getAll() {
        return this.courseRepository.findAll();
    }

    public Course getById(String id) {
        return this.courseRepository.getOne(id);
    }

    public Course add(Course course) {
        return this.courseRepository.save(course);
    }

    public Course updateById(String id, Course course) {
        if (!this.courseRepository.existsById(id)) {
            throw new RuntimeException("Course Not Found");
        }
        course.setName(id);
        return this.courseRepository.save(course);
    }

    public void deleteById(String id) {
        if (!this.courseRepository.existsById(id)) {
            throw new RuntimeException("Course Not Found");
        }
        this.courseRepository.deleteById(id);
    }

    public Course addStudent(String id, Long studentId) {
        Course course = this.courseRepository.getOne(id);
        Student student = this.studentService.getById(studentId);
        course.addStudent(student);
        return this.courseRepository.save(course);
    }
}
