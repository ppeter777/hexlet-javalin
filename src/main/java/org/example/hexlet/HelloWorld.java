package org.example.hexlet;

import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.example.hexlet.model.Course;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.dto.users.UsersPage;
import org.example.hexlet.model.User;
import org.example.hexlet.model.UserRepository;

public class HelloWorld {
    public static void main(String[] args) {
        var course1 = new Course("Java", "Course Java");
        var course2 = new Course("Python", "Course Python");
        var course3 = new Course("C++", "Course C++");
        var course4 = new Course("JS", "Course JS");
        var course5 = new Course("PHP", "Course PHP");
        var user1 = new User("John", "lys@mail.ru", "qua34");
        var user2 = new User("Alex", "gid123@yandex.ru", "sot2999");
        var user3 = new User("Jane", "sinal@gmail.com", "g_I983");
        UserRepository.save(user1);
        UserRepository.save(user2);
        UserRepository.save(user3);
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        courses.add(course4);
        courses.add(course5);
        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        app.get("/", ctx -> ctx.render("index.jte"));
        app.get("/courses", ctx -> {

            var term = ctx.queryParam("term");
            var coursesFiltered = courses;
            if (term != null) {
                coursesFiltered = courses.stream()
                        .filter(course -> course.getDescription().contains(term))
                        .collect(Collectors.toList());
            }
            var header = "Programming courses";
            var page = new CoursesPage(coursesFiltered, header, term);
            ctx.render("courses/index.jte", Collections.singletonMap("page", page));
        });
        app.get("/users/build", ctx -> {
            ctx.render("users/build.jte");
        });
        app.post("/users", ctx -> {
            var name = ctx.formParam("name");
            var email = ctx.formParam("email");
            var password = ctx.formParam("password");
            var passwordConfirmation = ctx.formParam("passwordConfirmation");

            var user = new User(name, email, password);
            UserRepository.save(user);
            ctx.redirect("/users");
        });
        app.get("/users", ctx -> {
            List<User> users = UserRepository.getEntities();
            var page = new UsersPage(users);
            ctx.render("users/index.jte", Collections.singletonMap("page", page));
        });
        app.start(7070);
    }
}
