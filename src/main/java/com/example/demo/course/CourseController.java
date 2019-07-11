package com.example.demo.course;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }
    
    @GetMapping
    public List<Course> getAll() {
        return this.courseService.getAll();
    }
    
    @GetMapping("/{id}")
    public Course getById(@PathVariable("id") String id) {
        return this.courseService.getById(id);
    }
    
    @PostMapping
    public Course add(@RequestBody Course couse) {
        return this.courseService.add(couse);
    }
    
    @PutMapping("/{id}")
    public Course update(@PathVariable("id") String id, @RequestBody Course course) {
        return this.courseService.updateById(id, course);
    }
    
    @PutMapping("/{id}/students/{studentId}")
    public Course update(@PathVariable("id") String id, @PathVariable("studentId") Long studentId) {
        return this.courseService.addStudent(id, studentId);
    }
    
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") String id) {
        this.courseService.deleteById(id);
    }
    
}
