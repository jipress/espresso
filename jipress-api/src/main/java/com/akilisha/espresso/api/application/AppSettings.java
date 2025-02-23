package com.akilisha.espresso.api.application;

import java.nio.file.Path;
import java.util.EnumMap;

public class AppSettings extends EnumMap<AppSettings.Setting, Object> {

    public AppSettings() {
        super(Setting.class);
        // apply default values - most of these values defined have no particular usage in the Espresso framework, and
        // they are here only for reference regarding their usage in the Express web framework for Node.js
        put(Setting.ACCEPTOR_THREADS, 1);
        put(Setting.SELECTOR_THREADS, 1);
        put(Setting.ACCEPT_QUEUE_SIZE, 128);
        put(Setting.SUBDOMAIN_OFFSET, 2);
        put(Setting.ENV, "development");
        put(Setting.ETAG, "weak");
        put(Setting.JSONP_CALLBACK_NAME, "callback");
        put(Setting.QUERY_PARSER, "extended");
        put(Setting.TRUST_PROXY, false);
        put(Setting.VIEWS, Path.of(System.getProperty("user.dir"), "www").toAbsolutePath().toString());
        put(Setting.VIEW_CACHE, false);
        put(Setting.X_POWERED_BY, true);
    }

    public enum Setting {
        CASE_SENSITIVE_ROUTING("case sensitive routing", Boolean.class, "Enable case sensitivity. When enabled, \"/Foo\" and \"/foo\" are different routes. When disabled, \"/Foo\" and \"/foo\" are treated the same."),
        ENV("env", String.class, "Environment mode. Be sure to set to \"production\" in a production environment"),
        ETAG("etag", Object.class, "Set the ETag response header"),
        JSONP_CALLBACK_NAME("jsonp callback name", String.class, "Specifies the default JSONP callback name"),
        JSON_ESCAPE("json escape", Boolean.class, "Enable escaping JSON responses from the res.json, res.jsonp, and res.send APIs. This will escape the characters <, >, and & as Unicode escape sequences in JSON"),
        JSON_REPLACER("json replacer", Object.class, "The 'replacer' argument used by `JSON.stringify`."),
        JSON_SPACES("json spaces", Object.class, "The 'space' argument used by `JSON.stringify`. This is typically set to the number of spaces to use to indent prettified JSON."),
        QUERY_PARSER("query parser", Object.class, "Disable query parsing by setting the value to false, or set the query parser to use either \"simple\" or \"extended\" or a custom query string parsing function."),
        STRICT_ROUTING("string routing", Boolean.class, "Enable strict routing. When enabled, the router treats \"/foo\" and \"/foo/\" as different. Otherwise, the router treats \"/foo\" and \"/foo/\" as the same."),
        SUBDOMAIN_OFFSET("subdomain offset", Number.class, "The number of dot-separated parts of the host to remove to access subdomain."),
        TRUST_PROXY("trust proxy", Boolean.class, "Indicates the app is behind a front-facing proxy, and to use the X-Forwarded-* headers to determine the connection and the IP address of the client."),
        VIEWS("views", String.class, "A directory or an array of directories for the application's views. If an array, the views are looked up in the order they occur in the array"),
        VIEW_CACHE("view cache", Boolean.class, "Enables view template compilation caching. Should be set to true in production, otherwise false"),
        VIEW_ENGINE("view engine", String.class, "The default engine extension to use when omitted."),
        X_POWERED_BY("x-powered-by", String.class, "Enables the \"X-Powered-By: Express\" HTTP header."),
        TEMPLATES_DIR("templates dir", String.class, "The same as \"views\""),
        TEMPLATES_EXT("templates ext", String.class, "The file extension suffix for view files e.g. .mvel (for MVEL), .fmt (for FreeMarker)"),
        ACCEPTOR_THREADS("acceptors", int.class, "Number of threads accepting client connection requests"),
        SELECTOR_THREADS("selectors", String.class, "Number of threads which the acceptor can dispatch network events to"),
        ACCEPT_QUEUE_SIZE("acceptQueueSize", String.class, "Number of requests that can be queued up before they start dropping");

        public final String property;
        public final Class<?> type;
        public final String description;

        Setting(String value, Class<?> type, String description) {
            this.property = value;
            this.type = type;
            this.description = description;
        }

        public static Setting value(String property) {
            for (Setting setting : values()) {
                if (setting.property.equals(property)) {
                    return setting;
                }
            }
            throw new RuntimeException("Unknown application setting");
        }
    }
}
