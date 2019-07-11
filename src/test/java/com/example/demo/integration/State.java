package com.example.demo.integration;

import com.example.demo.course.Course;
import com.example.demo.student.Student;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class State {

    public Map<Student, Student> students = new HashMap<>();
    public Map<Course, Course> courses = new HashMap<>();

    public State() {
        this.setDefaultStudents(3);
        this.setDefaultCourses(2);
    }

    private void setDefaultStudents(int size) {
        for (int i = 0; i < size; i++) {
            Student student = this.getMockStudent(i + 1);
            this.students.put(student, student);
        }
    }
    
    private void setDefaultCourses(int size) {
        for (int i = 0; i < size; i++) {
            Course course = this.getMockCourse(i + 1);
            this.courses.put(course, course);
        }
    }
    
    // todo: move to separate factory
    public Student getMockStudent(int id) {
        String firstName = "mock-firstName-" + id;
        String lastName = "mock-lastName-" + id;
        String email = "mock-email-" + id;
        Student student = new Student(firstName, lastName, email);
        student.setId(new Long(id));
        return student;
    }
    
    // todo: move to separate factory
    public Course getMockCourse(int id) {
        String name = "mock-name-" + id;
        return new Course(name);
    }
}
