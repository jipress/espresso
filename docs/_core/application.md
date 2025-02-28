## Application

> The app object conventionally denotes the Espresso instance, which is also a router since it extends the _IRouter_
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
An array of any combinations of the above.

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

Or one could similarly apply the middleware this way:

```bash
app.all("/*", requireAuthentication)
app.all("/*", loadUser)
```

Another example is whitelisted "global" functionality. The example is similar to the ones above, but it only restricts
paths that start with "/api":

app.all("/api/*", requireAuthentication)

#### void delete(String path, IMiddleware... middlewares);

Routes HTTP DELETE requests to the specified path with the specified middleware functions.

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of any combinations of the above.

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

### <T>T get(String setting, Function<Object, T> converter)

Performs the exact same action as ```app.get(setting)``` and in addition, it provides the opportunity to coerce the
setting value to a designated type through a converter function.

```java
int subDomainOffset = app.get(AppSettings.Setting.SUBDOMAIN_OFFSET.property, o -> Integer.parseInt(o.toString()));
```

#### void get(String path, IMiddleware... middlewares);

Routes HTTP GET requests to the specified path with the specified callback functions.

- path

The path for which the middleware function is invoked; can be any of:
A string representing a path.
A path pattern.
A regular expression pattern to match paths.
An array of any combinations of the above.

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

#### void listen(int port, String[] args)

#### void listen(String host, int port, Consumer<String> callback)

### void listen(String host, int port, String[] args, Consumer<String> callback)

Binds and listens for connections on the specified host and port.

If port is omitted or is 0, the operating system will assign an arbitrary unused port, which is useful for cases like
automated tasks. Please beware that any code using the port number returned by this method is subject to a race
condition - a different process/thread may bind to the same port immediately after the ServerSocket instance is closed.

If host is omitted, then localhost will be implied and used

```bash
var app = express()
app.listen(3000)
```

When the _args_ parameter is included, the command line arguments are passed along and applied to the options available
for starting the server up.

```bash
var app = express()
app.listen(3000, args)
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
An array of any combinations of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

#### void param(String param, IParamCallback callback)

#### void param(String[] params, IParamCallback callback)

Add callback triggers to route parameters, where name is the name of the parameter or an array of them, and callback is
the callback function.

The parameters of the callback function are:

- req, the request object.
- res, the response object.
- next, indicating the next middleware function.
- The value of the name parameter.

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
An array of any combinations of the above.

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
An array of any combinations of the above.

- middlewares

IMiddleware functions; can be:

- A middleware function.
- A series of middleware functions (separated by commas).

#### void render(String viewName, BiConsumer<Exception, String> callback)

Returns the rendered HTML of a view via the callback function. It accepts an optional parameter that is an object
containing local variables for the view. It is like res.render(), except it cannot send the rendered view to the client
on its own.

It's actually used by the IResponse instance to generate content, which then the IResponse instance sends to the client

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

Configure CORS options when using cross-domain clients

```bash
    var app = express();
    app.use(CorsOptions.wideOpen());
```

Using the ```wideOpen``` function as shown above, should _ONLY_ be considered for testing purposes only, since it is
the most permissive configuration that can be achieved. A more controlled configuration should be created by using the
_CorsBuilder_.

To illustrate CORS further, consider having two apps:

- The data source app

> The data source makes available data to be consumed by interested clients

```java
public static void main(String[] args) {
    var app = Espresso.express();
    app.use(Espresso.text());

    app.get("/time", (req, res, next) -> res.send(new Date().toString()));

    app.listen(3031);
}
```

- The data client app

> Remember to start the second one with command line argument ```-securePort 3444``` to override any matching defaults

```java
public static void main(String[] args) {
    var app = Espresso.express();
    app.use("/", IStaticOptionsBuilder.newBuilder()
            .baseDirectory("jipress-demos/www")
            .welcomeFiles("cors-handlers.html")
            .build());

    app.listen(3030, args);
}
```

The data client contains the ```cors-handlers.html``` page which is used to initiate http requests from the browser.

```html

<body>
<a href="http://localhost:3031/time">Click to view time now</a>
<h1></h1>
<script>
    document.querySelector("a").addEventListener('click', ev => {
        ev.preventDefault();
        fetch(ev.target.href, res => {
            document.querySelector("h1").innerHTML = res.text;
        })
    })
</script>
</body>
```

When the link on the page is clicked, a ```GET http://localhost:3031/time``` request is made, but this is not in the
same domain as the one which is hosting the web page (hence cross-site). An error is returned, and can be view through
the browser's developer tools

![cor missing allow origin header](../_media/cors-missing-allow-origin.png)

To overcome this, the server needs to be configured to return the ```Access-Control-Allow-Origin ``` and the value
could be either specific to the requesting server, in this case like ```http://localhost:3030``` or much more loose,
like using ```*```

```bash
var app = Espresso.express();
app.use(Espresso.text());
app.use(CorsBuilder.newBuilder()
       .response(bld -> bld.allowOrigin("http://localhost:3030").build())
       .build());

app.get("/time", (req, res, next) -> {
   res.send(new Date().toString());
});

app.listen(3031);
```

![Cors with Origin allowed](../_media/cors-with-origin-allowed.png)

Upon inspecting the response in the browser further, the configured header is now in the response body.

![allowed origin header](../_media/access-control-allow-origin-header.png)

With CORS, there is another concept called _preflight requests_, and this is what the _OPTIONS_ http method
accomplishes.
It is an additional check which is not applied to _GET_ but it is applied to the other non-idempotent http methods.

In the _data source_ app, add a new request handler for _PUT_ requests

```bash
app.put("/time", (req, res, next) -> {
   res.send(new Date().toString());
});
```

In the _data_client_ app, change the browser script to use _PUT_ instead of _GET_.

```js
document.querySelector("a").addEventListener('click', ev => {
    ev.preventDefault();
    fetch(ev.target.href, {method: 'PUT'}, res => {
        document.querySelector("h1").innerHTML = res.text;
    })
})
```

Restart both apps and make the same request as before.

When the server is not handling pre-flight requests, the request will fail as shown below.

![no pre-flight checks failure](../_media/pre-flight-check-failure.png)

When the server is handling pre-flight requests, but the request method is not configured, the request will fail as
shown below.

![pre-flight check without method](../_media/pre-flight-no-method-failure.png)

To correct this, the request method should be configured, and the configured methods should contain the method specified
in the _Allow-Control-Request-Method_ header (if using a browser, this header is typically inserted in the request
automatically).

```bash
app.use(CorsBuilder.newBuilder()
.response(bld -> bld
      .allowOrigin("http://localhost:3030")
      .allowHeaders("PUT")
      .build())
.build());
```

After configuring and restarting the _data source_, the response should now look good.

![pre-flight successful](../_media/pre-flight-method-check-ok.png)

Another dimension of CORS is when using credentials from the client. In the client, make this update to see the effect.

```js
document.querySelector("a").addEventListener('click', ev => {
    ev.preventDefault();
    fetch(ev.target.href, {
        method: 'PUT', credentials: "include",
    }, res => {
        document.querySelector("h1").innerHTML = res.text;
    })
})
```

Upon making the same request in the browser and inspecting the response, CORS once again find away to crash the party.

![credentials conft configured failure](../_media/allow-credentials-header-not-configured.png)

To resolve this, an additional configuration needs to be done in the _data server_ app.

```bash
app.use(CorsBuilder.newBuilder()
 .response(bld -> bld
         .allowOrigin("http://localhost:3030")
         .allowMethods("PUT")
         .allowCredentials(true)
         .build())
 .build());
```

And the result now is back to looking good

![access-control-allow-credentials_ok](../_media/access-control-allow-credentials_ok.png)

The _CorsBuilder_ has additional methods for more CORS configuration options.

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

Configure the respective static content request handlers with the respective application, at the application's mount
path or relative to the mount path

```bash
public static void main(String[] args) {
        var app = express();
        
        app.use(StaticOptionsBuilder.newBuilder().baseDirectory("jipress-jetty/view").build());
        app.use("/home", StaticOptionsBuilder.newBuilder().baseDirectory("jipress-jetty/view").build());

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

Register custom error handling middleware with the application. A default error handler is used to suppress the stack
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

Register a web socket handler which will connect and communicate with web socket clients. You __MUST__ provide an
implementation for the method you want to be handled. The available handler methods are in the __IIWebsocketHandler__
interface

```bash
interface IWebsocketHandler<S> {

    void onConnect(S session); //S is a generic type, which represents a Session in the servlet-based implemnetation
    
    void onError(Throwable cause);
    
    void onClose(int statusCode, String reason);
    
    void onMessage(String message);
    
    void onBinary(byte[] payload, int offset, int length);
}
```

There are also some other convenience methods which are available in the creator that can be used while registering the
handler methods. These methods are:

```bash
RemoteEndpoint getRemote();

Session getSession();

boolean isConnected();

boolean isNotConnected();
```

An illustration for registering method handlers is shown below. Point worth noting:

1. When registering a *subProtocol*, make sure it is matched in the client. __protocolOne__ is the default value
2. When registering a *pulseInterval*, make sure it is less than the http connection timeout. __20000__ millis is the
   default value, while __30000__ millis is the default http connection timeout.

```bash
public static void main(String[] args) {
    var app = express();
    app.use(StaticOptionsBuilder.newBuilder().baseDirectory("jipress-jetty/view")..welcomeFiles("websocket.html").build());
    app.websocket("/ws/", WebsocketOptionsBuilder.newBuilder().subProtocols(List.of("protocolOne"))
                .pulseInterval(20000).websocketPath("/events/*").build(), (ws) -> {
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
                ((Session) ws.getSession()).close();
            }
        });

        ws.onBinary((bytes, offset, length) -> {
            System.out.println("Received BYTES message: " + new String(bytes, offset, length));
        });

        ws.onError(cause -> cause.printStackTrace(System.err));
    });

    app.listen(8090);
}
```

On the client side (e.g. browser), a sample implementation is also illustrated:

```html:title='websocket.html'
<form>
    <label for="message">Message</label>
    <textarea id="message"></textarea>
    <input onclick="sendMessage()" type="button" value="Send">
    <input onclick="closeConnection()" type="button" value="Close">
</form>

<script src="js/ws-events.js" type="text/javascript"></script>
```

And the corresponding javascript would look like this:

```js:title='js/ws-events.js'
const ws = new WebSocket(
    "ws://localhost:8090/ws/events/", ["protocolOne"]
);

ws.addEventListener('open', (connection) => {
    console.log('connection opened', connection)
    ws.send("Ok, let kick off this thing!");
    const blob = new Blob(['the client is now connected'], {type: 'text/plain'});
    ws.send(blob);
});

ws.addEventListener('error', (cause) => {
    console.log('connection encountered an error', cause);
});

ws.addEventListener('message', (message) => {
    console.log('received message from server', message.data);
});

ws.addEventListener('close', (status, reason) => {
    console.log('server closed connection', status, reason);
});

function sendMessage() {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    const msg = {
        type: "message",
        text: document.getElementById("message").value,
        id: 'testing',
        date: Date.now(),
    };

    // Send the msg object as a JSON-formatted string.
    ws.send(JSON.stringify(msg));

    // Blank the text input element, ready to receive the next line of text from the user.
    document.getElementById("message").value = "";
}

function closeConnection() {
    ws.close(3000, 'client has disconnected')
}
```