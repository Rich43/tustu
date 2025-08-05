package javax.xml.ws;

/* loaded from: rt.jar:javax/xml/ws/AsyncHandler.class */
public interface AsyncHandler<T> {
    void handleResponse(Response<T> response);
}
