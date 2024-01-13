package com.akilisha.espresso.demos;

import com.akilisha.espresso.api.servable.IStaticOptionsBuilder;
import com.akilisha.espresso.jett.Espresso;

public class CorsHandlerClient {

    public static void main(String[] args) {
        var app = Espresso.express();
        app.use("/", IStaticOptionsBuilder.newBuilder()
                .baseDirectory("jipress-demos/www")
                .welcomeFiles("cors-handlers.html")
                .build());

        app.listen(3030, args);
    }
}
