package javax.xml.ws;

import java.util.concurrent.Future;

/* loaded from: rt.jar:javax/xml/ws/Dispatch.class */
public interface Dispatch<T> extends BindingProvider {
    T invoke(T t2);

    Response<T> invokeAsync(T t2);

    Future<?> invokeAsync(T t2, AsyncHandler<T> asyncHandler);

    void invokeOneWay(T t2);
}
