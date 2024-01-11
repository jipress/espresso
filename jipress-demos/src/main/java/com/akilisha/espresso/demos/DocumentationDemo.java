package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.cookie.CookieOptionsBuilder;
import com.akilisha.espresso.jett.Espresso;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DocumentationDemo {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use(Espresso.text());
        app.use(Espresso.json());

        app.get("/time/:action/:another", (req, res, next) -> {
            String action = req.param("action");
            String another = req.param("another");
            if (action.equals("new") && another.equals("again")) {
                res.cookie("movies", "jim-bob", CookieOptionsBuilder.newBuilder()
                        .domain("localhost")
                        .comment("last watched movie")
                        .maxAge(120, TimeUnit.SECONDS)
                        .path("/time")
                        .build());
            } else {
                res.clearCookie("movies", CookieOptionsBuilder.newBuilder().path("/time").build());
            }
            res.send(new Date().toString());
        });

        app.get("/protocol", (req, res, next) -> {
            res.send(req.protocol());
        });

        app.get("/query", (req, res, next) -> {
            res.json(req.query());
        });

        app.get("/query/:name", (req, res, next) -> {
            res.json(req.query(req.param("name"), String::toString));
        });

        app.get("/secure", (req, res, next) -> {
            res.send(Boolean.toString(req.secure()));
        });

        app.get("/hostname", (req, res, next) -> {
            res.send(req.hostname());
        });

        app.get("/ip", (req, res, next) -> {
            res.send(req.ip());
        });

        app.get("/ips", (req, res, next) -> {
            res.json(req.ips());
        });

        app.get("/original", (req, res, next) -> {
            var paths = List.of(
                    req.originalUrl(), // '/admin/new?sort=desc'
                    req.baseUrl(), // '/admin'
                    req.path()); // '/new'
            res.json(paths);
        });

        app.get("/params/:first/then/:next", (req, res, next) -> {
            res.json(List.of(req.param("first"), req.param("next")));
        });

        app.get("/params/:first/then/:next/all", (req, res, next) -> {
            res.json(List.of(req.params()));
        });

        app.get("/nums/:first/then/:second", (req, res, next) -> {
            int first = req.param("first", Integer::parseInt);
            int second = req.param("second", Integer::parseInt);
            res.json(List.of(first, second));
        });

        app.listen(3031);
    }
}
