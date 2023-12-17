package org.example.hexlet;

import io.javalin.Javalin;

public class HelloWorld {
    public static void main(String[] args) {
        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });
        app.get("/hello", ctx -> {
            var name = ctx.queryParam("name");
            ctx.contentType("text/html");
            if (name.isEmpty()) {
                name = "WORLD";
            }
            ctx.result("<h1>Hello, " + name + "!</h1>");
        });
        app.get("/", ctx -> {
            var name = ctx.queryParam("name");
            ctx.contentType("text/html");
            ctx.result("<h1>Hello, World!</h1>");
        });
        app.start(7070);
    }
}
