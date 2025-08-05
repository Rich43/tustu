package javax.xml.ws.spi.http;

import java.io.IOException;

/* loaded from: rt.jar:javax/xml/ws/spi/http/HttpHandler.class */
public abstract class HttpHandler {
    public abstract void handle(HttpExchange httpExchange) throws IOException;
}
