package com.example.demo.student;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StrudentController {

    private final StudentService studentService;

    public StrudentController(StudentService userService) {
        this.studentService = userService;
    }

    @GetMapping
    public List<Student> getAll() {
        return this.studentService.getAll();
    }
    
    @GetMapping("/{id}")
    public Student getById(@PathVariable("id") Long id) {
        return this.studentService.getById(id);
    }
    
    @PostMapping
    public Student add(@RequestBody Student student) {
        return this.studentService.add(student);
    }
    
    @PutMapping("/{id}")
    public Student update(@PathVariable("id") Long id, @RequestBody Student student) {
        return this.studentService.updateById(id, student);
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        this.studentService.deleteById(id);
    }
}
