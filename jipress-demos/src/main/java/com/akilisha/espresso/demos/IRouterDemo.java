package com.akilisha.espresso.demos;

import com.akilisha.espresso.jett.Espresso;

import java.util.Objects;

public class IRouterDemo {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use(Espresso.text());
        app.use(Espresso.json());

        var router = app.route("/git");
        router.get("^/commits/(\\w+)(?:\\.\\.(\\w+))?$", (req, res, next) -> {
            var from = req.params().get("0");
            var to = Objects.requireNonNullElse(req.params().get("1"), "HEAD");
            res.send("commit range " + from + ".." + to);
        });

        app.listen(3031, args);
    }
}
