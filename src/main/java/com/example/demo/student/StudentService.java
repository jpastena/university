package com.example.demo.student;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAll() {
        return this.studentRepository.findAll();
    }

    public Student getById(Long id) {
        return this.studentRepository.getOne(id);
    }

    public Student add(Student student) {
        return this.studentRepository.save(student);
    }

    public Student updateById(Long id, Student student) {
        if (!this.studentRepository.existsById(id)) {
            throw new RuntimeException("Student Not Found");
        }
        student.setId(id);
        return this.studentRepository.save(student);
    }

    public void deleteById(Long id) {
        if (!this.studentRepository.existsById(id)) {
            throw new RuntimeException("Student Not Found");
        }
        this.studentRepository.deleteById(id);
    }
}
