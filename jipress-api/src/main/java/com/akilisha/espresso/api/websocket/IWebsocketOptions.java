package com.akilisha.espresso.api.websocket;

import java.util.List;

public interface IWebsocketOptions {

    List<String> subProtocols();

    /*
     *  The path spec can have these forms:
     *  Servlet syntax, specified with servlet|<path spec>, where the servlet| prefix can be omitted if the path spec begins with / or *. (for example, /ws, /ws/chat or *.ws).
     *  Regex syntax, specified with regex|<path spec>, where the regex| prefix can be omitted if the path spec begins with ^ (for example, ^/ws/[0-9]+).
     *  URI template syntax, specified with uri-template|<path spec> (for example uri-template|/ws/chat/{room}).
     */
    String websocketPath();

    int maxBufferSize();

    int pingInterval();
}
