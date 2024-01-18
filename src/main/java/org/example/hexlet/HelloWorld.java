package org.example.hexlet;

import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.example.hexlet.model.Course;
import org.example.hexlet.dto.courses.CoursesPage;

public class HelloWorld {
    public static void main(String[] args) {
        var course1 = new Course("Java", "Course Java");
        var course2 = new Course("Python", "Course Python");
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        app.get("/courses", ctx -> {
            var header = "Programming courses";
            var page = new CoursesPage(courses, header);
            ctx.render("courses/index.jte", Collections.singletonMap("page", page));
        });

        app.start(7070);
    }
}
