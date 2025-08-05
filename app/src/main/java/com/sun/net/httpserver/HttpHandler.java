package com.sun.net.httpserver;

import java.io.IOException;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpHandler.class */
public interface HttpHandler {
    void handle(HttpExchange httpExchange) throws IOException;
}
