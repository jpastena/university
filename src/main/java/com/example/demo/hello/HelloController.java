package com.example.demo.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping()
    public String helloWorld() {
        return "<h1>Hello World</h1>";
    }

    @GetMapping("/{name}")
    public String helloName(@PathVariable("name") String name) {
        return "<h1>Hello " + name + "</h1>";
    }

}
