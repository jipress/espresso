# Espresso web framework

This is a Java-based, web framework that stays close, as much as possible, to the APIs in Express.js in terms of its
core abstractions and the corresponding functions and having to only accommodate a few differences where the nuances
of using Java can not sufficiently accommodate the succinctness or brevity of Javascript.

These core abstractions are:

| Express.js  | Espresso jetty |
|-------------|----------------|
| express()   | Espresso       |
| Application | IApplication   |
| Request     | IRequest       |
| Response    | IResponse      |
| Router      | IRouter        | 

## Espresso

> A container for static functions. It cannot be instantiated, and so it cannot hold instance variables.

##### IApplication express()

static function which returns an instance of IApplication (which also extends IRouter) that can be used as an entry
point when starting the server - You can add middleware and HTTP method routes (such as get, put, post, and so on).

##### IBodyParser json()

static function which handles application/json content - Returns middleware that only parses JSON and only looks at
requests where the Content-Type header matches the type option. It is registered with an application using it
__use(IBodyParser)__ function

##### IBodyParser raw()

static function which handles raw bytes, application/octet-stream content - Returns middleware that parses all bodies
as a byte[] array and only looks at requests where the Content-Type header matches the type option. It is registered
with an application using it __use(IBodyParser)__ function

##### IBodyParser text()

static function which handles text/plain content - Returns middleware that parses all bodies as a string and only looks
at requests where the Content-Type header matches the type option. It is registered with an application using it
__use(IBodyParser)__ function

##### IBodyParser urlEncoded()

static function which handles x-www-form-urlencoded encoded content - Returns middleware that only parses urlencoded
bodies and only looks at requests where the Content-Type header matches the type option. It is registered with an
application using it __use(IBodyParser)__ function

##### IBodyParser multipart(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold)

static function which handles multipart/form-data encoded content - Returns middleware that only parses multipart bodies
and only looks at requests where the Content-Type header matches the type option. It is registered with an application
using it __use(IBodyParser)__ function

##### ResourceHandler staticFiles(IStaticOptions options)

static function configures a static resources loader without a context path

##### ContextHandler staticFiles(String context, IStaticOptions options)

static function configures a static resources loader for a given a context path

##### void startServer(String host, int port, Consumer<String> callback, IApplication entryApp)

static function starts the server using a given entry IApplication instance

## IApplication

> The app object conventionally denotes the Espresso application, which is also a router since it extends the _IRouter_
> interface. Create it by calling the top-level express() static function available in the Espresso class:

```
public static void main(String[] args) {
    var app = express();

    app.get("/", (req, res, next) -> res.send("hello world example"));
    app.locals().put("title", "My App");

    app.listen(3000);
}
```

The app object has methods for

1. Routing HTTP requests; see for example, app.METHOD and app.param.
2. Configuring middleware; see app.route.
3. Rendering HTML views; see app.render.
4. Registering a template engine; see app.engine.
5. It also has settings (properties) that affect how the application behave

##### Map<String, Object> locals()

The app.locals() object has properties that are local variables within the application, and will be available in
templates rendered with res.render.

```bash
app.locals().put('title', 'My App')
app.locals().put('email', 'me@myapp.com')

println(app.locals().get('title'))
// => 'My App'

println(app.locals().get('email'))
// => 'me@myapp.com'
```

##### String mountPath();

The app.mountPath() property contains one or more path patterns on which a sub-app was mounted.

```bash
public static void main(String[] args) {
    var app = express();    //entry app
    var admin = express();  //sub-app

    admin.get("/", (req, res, next) -> {
        System.out.println(admin.mountPath());
        res.send("hello sub app example");
    });

    app.use("/admin", admin);
    app.listen(3000);
}
```

##### void on(String event, Consumer<IApplication> subscriber);

The mount event is fired on a sub-app, when it is mounted on a parent app. The parent app is passed to the callback
function.

```bash
public static void main(String[] args) {
    var app = express();
    var admin = express();
    
    admin.on("mount", (parent) -> {
        System.out.println("Admin Mounted");
        System.out.printf("parent mountPath - /%s\n", parent.mountPath()); // refers to the parent app
    });

    admin.get("/", (req, res, next) -> {
        res.send("Admin Homepage");
    });

    app.use("/admin", admin);

    app.listen(3000);
}
```

#### void all(String path, IMiddleware... middlewares);

This method is like the standard app.METHOD() methods, except it matches all HTTP verbs.

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of combinations of any of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

```bash
public static void main(String[] args) {
    var requireAuthentication = new IMiddleware() {

        @Override
        public void handle(IRequest req, IResponse res, INext next) {
            System.out.println("Authenticating user"); // Authenticating user
            next.ok();
        }
    };

    var loadUser = new IMiddleware() {

        @Override
        public void handle(IRequest req, IResponse res, INext next) {
            System.out.println("Loading user"); // Loading user
            next.ok();
        }
    };

    var app = express();

    app.all("/*", requireAuthentication, loadUser);

    app.get("/", (req, res, next) -> {
        res.send("All handler Homepage");
    });

    app.listen(3000);
}
```

Or the could equivalently apply the middleware this way:

```bash
app.all("/*", requireAuthentication)
app.all("/*", loadUser)
```

Another example is white-listed “global” functionality. The example is similar to the ones above, but it only restricts
paths that start with “/api”:

app.all("/api/*", requireAuthentication)

#### void delete(String path, IMiddleware... middlewares);

Routes HTTP DELETE requests to the specified path with the specified middleware functions.

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of combinations of any of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

```bash
app.delete("/", (req, res, next) {
  res.send("DELETE request to homepage")
})
```

#### void disable(String setting)

Sets the Boolean setting name to false, where name is one of the properties from the app settings

```bash
app.disable("my-property")
app.get("my-property")
// => false
```

#### boolean disabled(String setting)

Returns true if the Boolean setting name is disabled (false), where name is one of the properties from the app settings

```bash
app.disabled("my-property")
// => true

app.enable("my-property")
app.disabled("my-property")
// => false
```

#### void enable(String setting)

Sets the Boolean setting name to true, where name is one of the properties from the app settings

```bash
app.enable("my-property")
app.get("my-property")
// => true
```

#### boolean enabled(String setting)

Returns true if the setting name is enabled (true), where name is one of the properties from the app settings

```bash
app.enabled("my-property")
// => false

app.enable("my-property")
app.enabled("my-property")
// => true
```

#### void engine(String fileExt, IViewEngine engine)

Registers the given template engine callback for a given file type. You can do it in one of two ways

```bash
    app.set(AppSettings.Setting.TEMPLATES_DIR.property, "<view dir>");
    app.set(AppSettings.Setting.VIEW_ENGINE.property, "mvel");
    app.set(AppSettings.Setting.TEMPLATES_EXT.property, ".mvel");
}
```

or simply

```bash
    app.engine(new MvelViewEngine("<view dir>"), ".mvel");
```

Currently, Espresso only ships with two default view libraries - MVEL and PEBBLE

#### Object get(String setting)

Returns the value of name app setting, where name is one of the strings in the app settings

```bash
app.get("title")
// => undefined

app.set("title", "My Site")
app.get("title")
// => "My Site"
```

#### void get(String path, IMiddleware... middlewares);

Routes HTTP GET requests to the specified path with the specified callback functions.

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of combinations of any of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

```bash
app.get("/",  (req, res, next) {
  res.send("GET request to homepage")
})
```

#### void listen()

#### void listen(int port)

#### void listen(String host, int port, Consumer<String> callback)

Binds and listens for connections on the specified host and port.

If port is omitted or is 0, the operating system will assign an arbitrary unused port, which is useful for cases like
automated tasks. Please beware that any code using the port number returned by this method is subject to a race
condition - a different process/thread may bind to the same port immediately after the ServerSocket instance is closed.

If host is omitted, then localhost will be implied and used

```bash
var app = express()
app.listen(3000)
```

#### void method(String name, String path, IMiddleware... middlewares)

Routes an HTTP request, where METHOD is the HTTP method of the request, such as GET, PUT, POST, and DELETE, in
lowercase.
Thus, the actual methods are app.get(), app.post(), app.put(), and app.delete().

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of combinations of any of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

#### void param(String param, IParamCallback callback)

#### void param(String[] params, IParamCallback callback)

Add callback triggers to route parameters, where name is the name of the parameter or an array of them, and callback is
the callback function. The parameters of the callback function are the request object, the response object, the next
middleware, the value of the parameter and the name of the parameter, in that order.

```bash
public static void main(String[] args) {
    var app = Espresso.express();

    app.get("/user/:id", (req, res, next) -> res.send("hello world with path params"));
    app.param("id", (req, res, next, id) -> {
        System.out.println("CALLED ONLY ONCE");
        next.ok();
    });

    app.listen(3000);
}
```

#### void properties(String file)

Loads properties externalized in a file into the app's default config for use by different parts of the application.

#### void post(String path, IMiddleware... middlewares)

Routes HTTP POST requests to the specified path with the specified callback functions.

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of combinations of any of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

#### void put(String path, IMiddleware... middlewares)

Routes HTTP PUT requests to the specified path with the specified callback functions.

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of combinations of any of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

#### void render(String viewName, BiConsumer<Exception, String> callback)

Returns the rendered HTML of a view via the callback function. It accepts an optional parameter that is an object
containing local variables for the view. It is like res.render(), except it cannot send the rendered view to the client
on its own.

It's actually used by the IResponse instance to generate content, which then the IResponse instance send to the client

```bash
 @Override
 // IResponse instance
 
public void render(String viewName, Map<String, Object> context, BiConsumer<Exception, String> consumer) {
    this.app.render(viewName, context, consumer);
}
```

#### IRouter route(String contextPath)

Returns an instance of a single route, which you can then use to handle HTTP verbs with optional middleware.
Use app.route() to avoid duplicate route names (and thus typo errors).

When using app.route(), do NOT mount it manually as a sub-app. It will mount itself during creation to the parent app
used to create it

```bash
var app = express()

app.route("/events")
  .all((req, res, next) -> {
    // runs for all HTTP verbs first
    // think of it as route specific middleware!
  })
  .get((req, res, next) -> {
    res.json({})
  })
  .post((req, res, next) -> {
    // maybe add a new event...
  })
```

#### void set(String setting, Object value)

Assigns setting name to value. You may store any value that you want, but certain names can be used to configure the
behavior of the server.

```bash
app.set("title", "My Site")
app.get("title") // "My Site"
```

#### void use(IMiddleware... handlers)

#### void use(String path, IMiddleware... handlers)

Mounts the specified middleware function or functions at the specified path: the middleware function is executed when
the base of the requested path matches path.

```bash
public static void main(String[] args) {
    var app = express();

    app.use((req, res, next) -> {
        res.locals().put("name", "Jimmy");
        next.ok();
    });

    app.get("/home/1", (req, res, next) -> {
        String name = (String) res.locals().get("name");
        res.render("home", Map.of("name", name), res::send);
    });

    app.listen(3000);
}
```

#### void use(CorsOptions options);

Configure CORS options when using cross domain clients

```bash
    var app = express();
    app.use(new CorsOptions());
```

#### void use(String usePath, IApplication subApp)

#### void use(String[] usePaths, IApplication subApp)

Mounts the specified applications at the specified mount paths: the sub-application is used to routes a request when
the base of the requested path matches path.

```bash
public static void main(String[] args) {
    var app = express();

    var admin = express();
    admin.get("/", (req, res, next) -> {
        System.out.println(admin.mountPath());  // [ '/adm*n', '/manager' ]
        res.send("Admin Homepage");
    });

    var secret = express();
    secret.get("/", (req, res, next) -> {
        System.out.println(secret.mountPath()); // '/secr*t'
        res.send("Admin Secret");
    });

    admin.use("/secr*t", secret);
    app.use(new String[]{"/adm*n", "/manager"}, admin);

    app.get("/", (req, res, next) -> {
        System.out.println(app.mountPath()); // '/'
        res.send("App Home");
    });

    app.listen(3000);
}
```

#### void use(IBodyParser bodyParser)

Registers the specified body parse implementation with the corresponding factory for use in the application. The
respective parse is used when the content type header type is matched in the handler middleware

```bash
public static void main(String[] args) {
        var app = Espresso.express();
        app.use(Espresso.json());
        app.use(Espresso.text());

        var manage = app.route("/manage");

        <other handlers>

        app.listen(3030);
    }
```

#### void use(IStaticOptions options)

#### void use(String path, IStaticOptions options)

Configures the respective static content request handlers with the respective application, at the application's mount
path or relative to the mount path

```bash
public static void main(String[] args) {
        var app = express();
        
        app.use(StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").build());
        app.use("/home", StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").build());

        app.use((req, res, next) -> {
            res.locals().put("name", "Jimmy");
            next.ok();
        });

        app.get("/home/1", (req, res, next) -> {
            String name = (String) res.locals().get("name");
            res.render("home", Map.of("name", name), res::send);
        });

        app.listen(3000);
    }
```

#### void use(IErrorHandler... handlers)

Registers custom error handling middleware with the application. A default error handler is used to suppress the stack
trace generated by application errors, and simply prints the message.

A use is encouraged to register their custom error handlers, which can be targeted by using an error code

```bash
public static void main(String[] args) {
        var app = express();

    app.use((error, req, res, next) -> {
        if (next.errorCode() != null && next.errorCode().equals("404")) {
            res.end(error.getMessage(), Charset.defaultCharset().name());
            next.setHandled(true);
        }
    });

    app.get("/", (req, res, next) -> {
        throw new RuntimeException("Throwing generic exceptions like you don't care");
    });

    app.get("/404", (req, res, next) -> {
        next.error("404", new RuntimeException("Throwing 404 exceptions like you don't care"));
    });

    app.listen(3000);
}
```

##### void websocket(String contextPath, IWebsocketOptions options, Consumer<WebsocketHandlerCreator<?>> creator)

Register a web socket handler which will connect and communicate with web socket clients

```bash
public static void main(String[] args) {
    var app = express();
    app.use(StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").welcomeFiles("websocket.html").build());
    app.websocket("/ws/", WebsocketOptionsBuilder.newBuilder().websocketPath("/events/*").build(), (ws) -> {

        ws.onConnect(session -> {
            Session sess = (Session) session;
            System.out.println("Socket connected: " + session);
            try {
                sess.getRemote().sendString("Connection accepted");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ws.onClose((status, reason) -> System.out.printf("Socket closing: %d, %s\n", status, reason));

        ws.onMessage(message -> {
            System.out.println("Received TXT message: " + message);
            if (message.toLowerCase(Locale.ENGLISH).contains("bye")) {
                ((Session) session).close();
            }
        });

        ws.onError(cause -> cause.printStackTrace(System.err));
    });

    app.listen(8090);
}
```

## IRequest

The req object represents the HTTP request and has properties for the request query string, parameters, body,
HTTP headers, and so on. In this documentation and by convention, the object is always referred to as req (and the HTTP
response is res) but its actual name is determined by the parameters to the callback function in which you’re working.

For example:

```bash
app.get("/user/:id", (req, res) {
  res.send("user " + req.params.id)
})
```

But you could just as well have:

```bash
app.get("/user/:id", function (request, response) {
  response.send("user " + request.params.id)
})
```

#### IApplication app()

This function returns a reference to the instance of the Express application that is using the middleware.

```bash
static IMiddleware middleware = (req, res, next) -> {
    res.send("The views directory is " + req.app().get("templates dir"));
    //expect - The views directory is my-view-folder
};

public static void main(String[] args) {
    var app = express();

    app.set(AppSettings.Setting.TEMPLATES_DIR.property, "my-view-folder");
    app.get("/", middleware);

    app.listen(3000);
}
```

#### String baseUrl()

The context path on which a application instance was mounted.

```bash
public static void main(String[] args) {
    var app = express();

    var greet = app.route("/greet");

    greet.get("/jp", (req, res, next) -> {
        System.out.println(req.baseUrl()); // /greet
        res.send("Konichiwa!");
    });
    
    var greet2 = express();

    greet2.get("/swa", (req, res, next) -> {
        System.out.println(req.baseUrl()); // /greet2
        res.send("Shikamoo!");
    });

    app.use("/greet2", greet2);

    app.listen(3000);
}
```

#### Map<String, Object> body()

Contains key-value pairs of data submitted in the request body. By default, it is undefined, and is populated when you
use body-parsing middleware such as express.json() or express.urlencoded().

```bash
public static void main(String[] args) {
    var app = Espresso.express();
    app.use(json());
    app.use(urlEncoded());

    app.get("/json", (req, res, next) -> {
        Map<String, Object> json = Map.of("name", "Janie", "age", 23);
        System.out.println(json);
        res.json(json);
    });

    app.get("/json", (req, res, next) -> {
        Map<String, Object> json = Map.of("name", "Janie", "age", 23);
        System.out.println(json);
        res.json(json);
    });

    app.post("/json", (req, res, next) -> {
        Map<String, Object> json = req.body();
        System.out.println(json);
        res.json(json);
    });

    app.post("/form", (req, res, next) -> {
        Map<String, Object> json = req.body();
        System.out.println(json);
        res.json(json);
    });

    app.listen(3000);
}
```

#### <T> T body(Function<byte[], T> converter)

Turns over request bytes to custom converter for user-defined conversion to desired type

> curl -X POST http://localhost:3000/string/ -H "Content-Type: text/plain" -d "Some cool stuff"

```bash
public static void main(String[] args) {
    var app = express();
    app.use(new PlainTextBodyParser());
    
    var greet = app.route("/");
    
    greet.post("/string", (req, res, next) -> {
        String plainText = req.body(String::new);
        res.send(plainText); // expect: Some cool stuff
    });
    
    app.listen(3000);
}
```

#### void upload()

Upload content to a designated directory configured through application properties

```bash
public static void main(String[] args) {
    var app = express();
    app.properties("app-default.yaml"); // load application configuration properties made available in Config Map
    app.use("/upload", StaticOptionsBuilder.newBuilder().baseDirectory("presso-jetty/view").welcomeFiles("upload.html").build());
    app.use(multipart(
            System.getProperty("java.io.tmpdir"),
            ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getMaxFileSize(),
            ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getMaxRequestSize(),
            ((Application) app).getAppConfig().get(ConfigMap.MULTIPART_CONFIG, MultipartConfig.class).getFileSizeThreshold()));

    app.get("/", (req, res, next) -> {
        res.send("Hello, trying to upload");
    });

    app.post("/some", (req, res, next) -> {
        req.upload(); //this should upload content
        res.sendStatus(201);
    });

    app.listen(3000);
}
```

#### ReqCookies cookies()

When using cookie-parser middleware, this property is an object that contains cookies sent by the request. If the
request contains no cookies, it defaults to {}.

```bash
public static void main(String[] args) {
    var app = express();

    app.get("/", (req, res, next) -> {
        CookieOptions cookieOptions = CookieOptionsBuilder.newBuilder().build();
        res.cookie("monster", "frankenstein", cookieOptions);
        res.send("hello cookies example");
    });
    
    app.get("/cookie", (req, res, next) -> {
        res.
        res.send("hello cookies example");
    });

    app.listen(3000);
}
```