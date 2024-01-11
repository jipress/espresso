## Request

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
    app.use("/upload", StaticOptionsBuilder.newBuilder().baseDirectory("jipress-jetty/view").welcomeFiles("upload.html").build());
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
        res.send("Use at the browser's developer tools to view the cookies");
    });

    app.listen(3000);
}
```

#### String hostname()

Contains the hostname derived from the Host HTTP header.

```bash
// curl "http://localhost:3031/hostname"
app.get("/hostname", (req, res, next) -> {
    res.send(req.hostname());
});
// => '127.0.0.1' -> this will definately change depending on the host
```

#### String ip()

Contains the remote IP address of the request.

```bash
// curl "http://localhost:3031/ip"
app.get("/ip", (req, res, next) -> {
    res.send(req.ip());
});
// => '127.0.0.1' -> this will also definately change depending on the host
```

#### List<String> ips()

When the trust proxy setting does not evaluate to false, this property contains an array of IP addresses specified in 
the X-Forwarded-For request header. Otherwise, it contains an empty array. This header can be set by the client or by 
the proxy.

```bash
// curl "http://localhost:3031/ips"
app.get("/ips", (req, res, next) -> {
    res.json(req.ips());
});
// => []
```

#### String method()

Contains a string corresponding to the HTTP method of the request: GET, POST, PUT, and so on.

#### String originalUrl()

Contains the entire request url which is received from the client

```bash
// "GET 'http://www.example.com/admin/new?sort=desc'"
app.get("/original", (req, res, next) -> {
    var paths = List.of(
            req.originalUrl(),    // '/admin/new?sort=desc'
            req.baseUrl(),        // '/admin'
            req.path());          // '/new'
    res.json(paths);
});
```

#### String param(String name)

Returns the named path parameter value by drawing from the map of extracted path parameters which is created with every
request

```bash
// curl "http://localhost:3031/params/one/then/two"
app.get("/params/:first/then/:next", (req, res, next) -> {
    res.json(List.of(req.param("first"), req.param("next")));
});
// => ["one","two"]
```

#### Map<String, String> params()

This property is an object containing properties mapped to the named route “parameters”. For example, if you have the
route /user/:name, then the “name” property is available as req.params.name. This object defaults to {}.

```bash
// // curl "http://localhost:3031/params/one/then/two/all"
app.get("/params/:first/then/:next/all", (req, res, next) -> {
    res.json(List.of(req.params()));
});
// => [{"first":"one","next":"two"}]
```

#### <T> T param(String name, Function<String, T> converter)

Similar to __param(name)__ but with the addition of a converter function that will return a static type

```bash
// curl "http://localhost:3031/nums/230/then/2000"
app.get("/nums/:first/then/:second", (req, res, next) -> {
    int first = req.param("first", Integer::parseInt);
    int second = req.param("second", Integer::parseInt);
    res.json(List.of(first, req.param("second")));
});
// => [230,2000]
```

#### String path()

Contains the path part of the request URL. Similar to the _req.originalUrl()__

```bash
// example.com/users?sort=desc
res.send(req.path())
// => '/users'
```

#### String protocol()

Contains the request protocol string: either http or (for TLS requests) https.

```bash
// curl http://localhost:3031/protocol
app.get("/protocol", (req, res, next) -> {
    res.send(req.protocol());
});
// HTTP/1.1
```

#### <T> List<T> query(String name, Function<String, T> converter)

This will get the values of the named query parameter if it is available, or else it will simply return an empty list

```bash
// curl "http://localhost:3031/query/one?one=1&one=uno&two=2&three=3"
app.get("/query/:name", (req, res, next) -> {
    res.json(req.query(req.param("name"), String::toString));
});
// ["1","one"]
```

#### Map<String, List<String>> query()

This property is an object containing a property for each query string parameter in the route.

```bash
// curl "http://localhost:3031/query?one=1&one=uno&two=2&three=3"
app.get("/query", (req, res, next) -> {
    res.json(req.query());
});
// {"one":["1","one"],"two":["2"],"three":["3"]}
```

Attention should be paid to make sure the _url_ used with curl is in quotes so that _curl_ will not choke, since in
this case, the query string contains special characters. Without the quotes, the shell sees the ampersand (&) and
thinks that's the end of the command (and puts it into the background), and hence the wierd output that may jump out
of the shell otherwise.

#### Boolean secure()

A Boolean property that is true if a TLS connection is established.

```bash
// curl "http://localhost:3031/secure"
app.get("/secure", (req, res, next) -> {
    res.send(Boolean.toString(req.secure()));
});
// false
```

#### Boolean signedCookies()

#### String[] subdomains()

#### boolean xhr()

#### Boolean accepts(String... types)

#### String get(String headerName)

#### Boolean is(String contentType)

#### Object getAttr(String name)

#### void setAttr(String name, Object attr)

#### byte[] readSync()

#### <R> R rawRequest(Class<R> type)