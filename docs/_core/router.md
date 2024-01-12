## Router

A _Router_ object is an instance of _Routable_. Both _Router_ and _Routable_ classes implement the same _IRouter_
interface. Similarly, the _Application_ class also implements the _IRouter_ interface, so _Application_ and _Router_
can both be accessed through same super interface, _IRouter_. In this manner therefore, you can think of the _Router_
as a "mini-application," but only capable of performing middleware and routing functions.

The _Routable_ object container ```store()``` and ```select()``` methods which allows it to organize handlers in a
manner that is easy to store and retrieve later on by matching against an incoming request URL. A _Router_ therefore
also behaves like a repository of middleware functions itself.

The top-level _Jipress_ object, _Espresso_ has an ```express()``` method that creates a new _IApplication_ instance.
The _IApplication_ interface has a ```router()``` methods which create a new _IRouter_ instance which then becomes its
child. To be very precise, the ```router()``` method in _IApplication_ returns in actual Application instance which
has simply been upcast to an _IRouter_.

Once you’ve created a router object, you can add middleware and HTTP method routes (such as get, put, post, and so on)
to it just like an application. For example:

```bash
// invoked for any requests passed to this router
router.use((req, res, next) -> {
  // .. some logic here .. like any other middleware
  next()
})

// will handle any request that ends in /events
// depends on where the router is used
router.get("/events", (req, res, next) -> {
  // ..
})
```

#### void all(String path, IMiddleware... middlewares)

This method is just like the router.METHOD() methods, except that it matches all HTTP methods (verbs).

This method is extremely useful for mapping "global" logic for specific path prefixes or arbitrary matches. For example,
if you placed the following route at the top of all other route definitions, it would require that all routes from that
point on would require authentication, and automatically load a user. Keep in mind that these callbacks do not have to
act as end points; loadUser can perform a task, then call next() to continue matching subsequent routes.

```bash
router.all("*", requireAuthentication, loadUser)
```

Another example of this is passlisted "global" functionality. Here the example is much like before, but it only
restricts paths prefixed with "/api":

```bash
router.all("/api/*", requireAuthentication)
```

#### void method(String name, String path, IMiddleware... middlewares)

The router.METHOD() methods provide the routing functionality in Espresso, where METHOD is one of the HTTP methods,
such as GET, PUT, POST, and so on, in lowercase. Thus, the actual methods are router.get(), router.post(), router.put(),
and so on.

```bash
router.get("/", (req, res, next) -> {
  res.send("hello world")
})
```

You can also use regular expressions — useful if you have very specific constraints, for example, the following would
match "GET /commits/71dbb9c" as well as "GET /commits/71dbb9c..4c084f9".

> Note that if you want the entire path to be treated as a 'regular expression literal', then it needs to begin with
> '^' and end with '$'. In this scenario, you cannot therefore have path parameters and so the matched path values can
> now only be accessible using numeric keys.

```bash
// curl http://localhost:3031/git/commits/71dbb9c..4c084f9
// curl http://localhost:3031/git/commits/71dbb9c
public static void main(String[] args) {
    var app = Espresso.express();
    app.use(Espresso.text());
    app.use(Espresso.json());

    var router = app.route("/git");   // creating mini-app router
    router.get("^/commits/(\\w+)(?:\\.\\.(\\w+))?$", (req, res, next) -> {
        var from = req.params().get("0");
        var to = Objects.requireNonNullElse(req.params().get("1"), "HEAD");
        res.send("commit range " + from + ".." + to);
    });

    app.listen(3031, args);
}
// => commit range 71dbb9c..4c084f9
// => commit range 71dbb9c..HEAD
```

### void param(String param, IParamCallback callback);

#### void get(String path, IMiddleware... middlewares)

Refer to ```router.METHOD()``` section

#### void post(String path, IMiddleware... middlewares)

Refer to ```router.METHOD()``` section

#### void put(String path, IMiddleware... middlewares)

Refer to ```router.METHOD()``` section

#### void delete(String path, IMiddleware... middlewares)

Refer to ```router.METHOD()``` section

> Some methods like router.param(name, callback) and router.use([path], [function, ...] function) found in the
> Express.js Router are not in the Jipress IRouter. They are found in IApplication where their functionality maps over
> better.