package org.example.hexlet;

import io.javalin.Javalin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.javalin.validation.ValidationException;
import org.example.hexlet.controller.SessionsController;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.dto.users.BuildUserPage;
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
        app.get("/", ctx -> {
            var page = new MainPage(ctx.sessionAttribute("currentUser"));
            ctx.render("index.jte", Collections.singletonMap("page", page));
        });

        app.get("/sessions/build", SessionsController::build);
        app.post("/sessions", SessionsController::create);
        app.delete("/sessions", SessionsController::destroy);

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
        app.get(NamedRoutes.buildUserPath(), ctx -> {
            var page = new BuildUserPage();
            ctx.render("users/build.jte", Collections.singletonMap("page", page));
        });
        app.post(NamedRoutes.usersPath(), ctx -> {
            var name = ctx.formParam("name");
            var email = ctx.formParam("email");

            try {
                var passwordConfirmation = ctx.formParam("passwordConfirmation");
                var password = ctx.formParamAsClass("password", String.class)
                        .check(value -> value.equals(passwordConfirmation), "Passwords do not match")
                        .get();
                var user = new User(name, email, password);
                UserRepository.save(user);
                ctx.redirect("/users");
            } catch (ValidationException e) {
                var page = new BuildUserPage(name, email, e.getErrors());
                ctx.render("users/build.jte", Collections.singletonMap("page", page));
            }
        });
        app.get("/users", UsersController::index);

//                ctx -> {
//            List<User> users = UserRepository.getEntities();
//            var page = new UsersPage(users);
//            ctx.render("users/index.jte", Collections.singletonMap("page", page));
//        });
        app.start(7070);
    }
}
