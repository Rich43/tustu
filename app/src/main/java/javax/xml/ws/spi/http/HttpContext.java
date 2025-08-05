package javax.xml.ws.spi.http;

import java.util.Set;

/* loaded from: rt.jar:javax/xml/ws/spi/http/HttpContext.class */
public abstract class HttpContext {
    protected HttpHandler handler;

    public abstract String getPath();

    public abstract Object getAttribute(String str);

    public abstract Set<String> getAttributeNames();

    public void setHandler(HttpHandler handler) {
        this.handler = handler;
    }
}
