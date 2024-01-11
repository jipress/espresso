## Response

#### IApplication app()

#### void append(String header, String value)

#### void attachment()

#### void attachment (String fileLocation)

#### void contentType(String contentType)

#### void cookie(String name, String value)

#### void cookie(String name, String value, CookieOptions options)

#### void clearCookie(String name)

Cookies are tied to a specific path. Attention should be paid to ensure that the same path used during the cookie's
removal matches exactly the same path used during the cookie's creation. The path defaults to the currently requested
folder in the URL (and would thus only be available in the same folder or all its subfolders). It's better to
explicitly specify the path, otherwise it would be dependent on the currently requested folder in the URL. The cookie
path information is like the maxAge in the sense that it is not available in the request cookie header.

#### void clearCookie(String name, CookieOptions options)

#### void download(String filePath, String fileName)

#### void download(String filePath, String fileName, AttachmentOptions options)

#### void download(String filePath, String fileName, AttachmentOptions options, Consumer<Exception> callback)

#### void encoding(Charset charset)

#### void end()

#### void end(Object data, String encoding)

#### String get(String headerName)

#### void json(Object json)

#### void jsonp(Object jsonp)

#### void links(Map<String, String> links)

#### Map<String, Object> locals()

#### void location(String path)

#### void redirect(int status, String path)

#### void render(String viewName)

#### void render(String viewName, Map<String, Object> context)

#### void render(String viewName, BiConsumer<Exception, String> consumer)

#### void render(String viewName, Map<String, Object> context, BiConsumer<Exception, String> consumer)

#### void send(String content)

#### void send(Exception err, String content)

#### void sendFile(String filePath)

#### void sendFile(String filePath, Consumer<Exception> callback)

#### void sendFile(String filePath, AttachmentOptions options, Consumer<Exception> callback)

#### void sendStatus(int status)

#### void set(String headerName, String value)

#### void status(int status)

#### void type(String attachmentType)

#### void writeSync(Object content)