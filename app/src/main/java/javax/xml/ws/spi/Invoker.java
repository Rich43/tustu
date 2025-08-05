package javax.xml.ws.spi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.xml.ws.WebServiceContext;

/* loaded from: rt.jar:javax/xml/ws/spi/Invoker.class */
public abstract class Invoker {
    public abstract void inject(WebServiceContext webServiceContext) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

    public abstract Object invoke(Method method, Object... objArr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;
}
