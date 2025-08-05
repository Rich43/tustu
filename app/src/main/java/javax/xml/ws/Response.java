package javax.xml.ws;

import java.util.Map;
import java.util.concurrent.Future;

/* loaded from: rt.jar:javax/xml/ws/Response.class */
public interface Response<T> extends Future<T> {
    Map<String, Object> getContext();
}
