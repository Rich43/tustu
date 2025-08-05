package javax.xml.ws.http;

import javax.xml.ws.ProtocolException;

/* loaded from: rt.jar:javax/xml/ws/http/HTTPException.class */
public class HTTPException extends ProtocolException {
    private int statusCode;

    public HTTPException(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
