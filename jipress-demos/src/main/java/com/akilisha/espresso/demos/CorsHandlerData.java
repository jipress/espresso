package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.application.CorsBuilder;
import com.akilisha.espresso.jett.Espresso;

import java.util.Date;

public class CorsHandlerData {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use(Espresso.text());
        app.use(CorsBuilder.newBuilder()
                .response(bld -> bld
                        .allowedOrigins("http://localhost:3030")
                        .allowedMethods("PUT")
                        .allowedCredentials(true)
                        .build())
                .build());

        app.get("/time", (req, res, next) -> {
            res.send(new Date().toString());
        });

        app.put("/time", (req, res, next) -> {
            res.send(new Date().toString());
        });

        app.listen(3031);
    }
}
