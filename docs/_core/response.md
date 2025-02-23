## Response

The res object represents the HTTP response and contains method which give control over the nature of the response
content sent back to a client. In this documentation and by convention, the object is always referred to as res
(and the HTTP request is req) but its actual name is determined by the argument used in the callback function
which you’re working with.

For example:

```bash
app.get("/user/:id", (req, res) {
  res.send("user " + req.params.id)
})
```

But you could just as well have used different argument names:

```bash
app.get("/user/:id", function (request, response) {
  response.send("user " + request.params.id)
})
```

#### IApplication app()

This function returns a reference to the instance of the Express application that is using the middleware.

#### void append(String header, String value)

Appends the specified value to the HTTP response header field. It delegates to the ```.setHeader(header, value)```
method of the underlying request object.

```bash
res.append('Link', ['<http://localhost/>', '<http://localhost:3000/>'])
res.append('Set-Cookie', 'foo=bar; Path=/; HttpOnly')
res.append('Warning', '199 Miscellaneous warning')
```

#### void attachment()

Sets the HTTP response Content-Disposition header field to "attachment".

```bash
res.attachment()
// Content-Disposition: attachment
```

#### void attachment (String fileLocation)

Similar to ```res.attachment()```. If a filename is given, then it sets the Content-Type based on the extension name
via res.type(), and sets the Content-Disposition "filename=" parameter.

```bash
res.attachment('path/to/logo.png')
// Content-Disposition: attachment; filename="logo.png"
// Content-Type: image/png
```

#### void contentType(String contentType)

Convenience method for setting the _Content-Type_ header in the response, since it is used pretty frequently

#### void cookie(String name, String value)

Set cookie name to value. The value parameter may be a string or object converted to JSON.

The _options_ parameter is an object that can have the following properties.

| Property  | Type 	            | Description                                                                                 |
|-----------|-------------------|---------------------------------------------------------------------------------------------|
| domain 	  | String 	          | Domain name for the cookie. Defaults to the domain name of the app.                         |
| encode 	  | Function 	        | A synchronous function used for cookie value encoding. Defaults to encodeURIComponent.      |                     |                                                                                             |
| expires 	 | Date 	            | Expiry date of the cookie in GMT. If not specified or set to 0, creates a session cookie.   |
| httpOnly  | Boolean 	         | Flags the cookie to be accessible only by the web server.                                   |
| maxAge 	  | Number 	          | Convenient option for setting the expiry time relative to the current time in milliseconds. |
| path 	    | String 	          | Path for the cookie. Defaults to "/".                                                       |
| priority  | String 	          | Value of the "Priority" Set-Cookie attribute.                                               |
| secure 	  | Boolean 	         | Marks the cookie to be used with HTTPS only.                                                |
| signed 	  | Boolean 	         | Indicates if the cookie should be signed.                                                   |
| sameSite  | Boolean or String | Value of the "SameSite" Set-Cookie attribute.                                               |

#### void cookie(String name, String value, CookieOptions options)

Similar to ```res.cookie(name, value)``` but now with addition of a cookie options builder to help with using custom
options.

```bash
app.get("/time/:action/:another", (req, res, next) -> {
    String action = req.param("action");
    String another = req.param("another");
    if (action.equals("new") && another.equals("again")) {
        res.cookie("movies", "shaka-zulu", CookieOptionsBuilder.newBuilder()
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
```

#### void clearCookie(String name)

```bash
res.cookie('name', 'tobi', { path: '/admin' })
res.clearCookie('name', { path: '/admin' })
```

Clears the cookie specified by name

#### void clearCookie(String name, CookieOptions options)

Cookies are tied to a specific path. Attention should be paid to ensure that the same path used during the cookie's
removal matches exactly the same path used during the cookie's creation. The path defaults to the currently requested
URL (and would thus only be available in the same resource or its sub-resources).

It's better to explicitly specify the path, otherwise it would be dependent on the currently requested URL. The cookie
path information is like the maxAge in the sense that it is not available in the request cookie header.

#### void download(String filePath, String fileName)

Transfer the file specified in the _filePath_ argument as an "attachment". Typically, browsers will prompt the user
for download. By default, the Content-Disposition header "filename=" parameter is derived from the path argument,
but can be overridden with the filename parameter. If _filePath_ is relative, then it will be based on the current
working directory of the process or the root option, if provided.

This method delegate to ```res.download(path, file, options)``` by passing empty attachment options.

#### void download(String filePath, String fileName, AttachmentOptions options)

The attachment options available can be created with the help of the ```AttachmentOptionsBuilder.newBuilder()```
object. These options are, however, not enforced by the _Jipress_ framework at this time. The underlying _servlet
response_ does the heavy lifting without any additional coercion.

| Property 	     | Description 	                                                                                                                                             | Default  |
|----------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|----------|
| maxAge 	       | Sets the max-age property of the Cache-Control header in milliseconds or a string in ms format 	                                                          | 0        |
| root 	         | Root directory for relative filenames.                                                                                                                    |          |
| lastModified 	 | Sets the Last-Modified header to the last modified date of the file on the OS. Set false to disable it. 	                                                 | Enabled  |
| headers 	      | Object containing HTTP headers to serve with the file. The header Content-Disposition will be overridden by the filename argument.                        |          |
| dotfiles 	     | Option for serving dotfiles. Possible values are "allow", "deny", "ignore". 	                                                                             | "ignore" |
| acceptRanges 	 | Enable or disable accepting ranged requests. 	                                                                                                            | true     |
| cacheControl 	 | Enable or disable setting Cache-Control response header. 	                                                                                                | true     |
| immutable 	    | Enable or disable the immutable directive in the Cache-Control response header. If enabled, the maxAge option should also be specified to enable caching. | false    |

This method delegate to ```res.download(path, file, options, callback)``` by passing error handler which prints the
error out to the console.

#### void download(String filePath, String fileName, AttachmentOptions options, Consumer<Exception> callback)

Performs the actual _write_ operation to the output stream. As indicated in the previous section, the
_AttachmentOptions_ values are currently not used to influence the directions of the underlying response writer.

#### void encoding(Charset charset)

Convenience method for setting the _character encoding_ header in the response

#### void end()

Ends the response process. This method actually informs the _Jipress_ framework at what point to use the underlying
```setHandled(boolean)``` from Jetty core, because this is used to drive other actions regarding on the success or
failure of the request handling. Typically, this method is not used directly because it will eventually be called by
other higher-level functions in the _Response_ object, but it is sometimes good to know that it exists.

```bash
res.end()
res.status(404).end()
```

#### void end(Object data, String encoding)

A higher level function that will write the content passed via the _data_ argument to the underlying output stream,
and also call ```res.end()``` upon completion

#### String get(String headerName)

Returns the HTTP response header specified by field. The match is case-insensitive.

```bash
res.get("Content-Type")
// => "text/plain"
```

#### void json(Object json)

Sends a JSON response.

```bash
// curl http://localhost:3031/headers -H "Content-Type: text/plain"
app.get("/headers", (req, res, next) -> {
    res.json(List.of(req.get("Content-Type"), req.get("content-type"), req.get("Something")));
});
// => ["text/plain","text/plain","undefined"]
```

#### void jsonp(Object jsonp)

Currently delegates to ```res.json(data)```. No further enhancement is made to the data or special treatment given to
the underlying process

#### void links(Map<String, String> links)

Currently delegates to ```res.json(data)```. No further enhancement is made to the data or special treatment given to
the underlying process

#### Map<String, Object> locals()

Returns a mutable map of values that are only available for the duration to the response object. Typically, middleware
will use this (in the same way as _req.setAttr(attr, value)_ and _req.getAttr(attr)_) to pass values around during the
lifetime of handling the request.

```bash
app.use(function (req, res, next) {
  // Make `user` and `authenticated` available in the next handler
  res.locals().put("user", req.getAttr("user"))
  res.locals().put("authenticated", !req.getAttr("user.anonymous"))
  next.ok()
})
```

#### void location(String path)

Delegate to ```res.redirect(status, path)``` using HTTP status code 301.

#### void redirect(String path)

Delegate to ```res.redirect(status, path)``` using HTTP status code 302.

#### void redirect(int status, String path)

Redirects to the URL derived from the specified path, with specified status, a positive integer that corresponds to an
HTTP status code.

#### void render(String viewName)

Delegate to the ```app.render(view, model, callback)``` function which will find the configured _ViewEngine_ and use
it to write content back to the client

#### void render(String viewName, Map<String, Object> context)

Delegate to the ```app.render(view, model, callback)``` function which will find the configured _ViewEngine_ and use
it to write content back to the client

#### void render(String viewName, BiConsumer<Exception, String> consumer)

Delegate to the ```app.render(view, model, callback)``` function which will find the configured _ViewEngine_ and use
it to write content back to the client

#### void render(String viewName, Map<String, Object> context, BiConsumer<Exception, String> consumer)

Delegate to the ```app.render(view, model, callback)``` function which will find the configured _ViewEngine_ and use
it to write content back to the client

#### void send(String content)

Writes the content to the output stream through the ```writeSync(content)``` method, and then calls ```res.end()```

#### void send(Exception err, String content)

If _err_ is not missing, it will Writes the error message to the output stream through the ```writeSync(content)```
method, and then calls ```res.end()```. If err is missing however, it will write the provided content instead

#### void sendFile(String filePath)

Delegates to ```res.sendFile(filePath, options, callback)``` with empty _AttachmentOptions_ and an error callback
which prints the error output to the console.

#### void sendFile(String filePath, Consumer<Exception> callback)

Delegates to ```res.sendFile(filePath, options, callback)``` with empty _AttachmentOptions_ and a custom error callback
handler.

#### void sendFile(String filePath, AttachmentOptions options, Consumer<Exception> callback)

Write the content from the specified file to the underlying output stream and also in addition, makes use of an error
callback to customize the error response in the event that an error happens

#### void sendStatus(int status)

Sets the response _HTTP status code_ to statusCode and sends the registered status message as the text response body.

#### void set(String headerName, String value)

Sets the response’s HTTP header field to value.

```bash
res.set('Content-Type', 'text/plain')
```

#### void status(int status)

A convenience method to set the _HTTP status code_ in the response object

#### void type(String attachmentType)

Sets the Content-Type HTTP header to the MIME type as determined by the specified type.

#### void writeSync(Object content)

Write the actual content to the underlying _PrintWriter_ instance. This method is not used directly, but will instead
be used by other higher level output functions defined in _IResponse_