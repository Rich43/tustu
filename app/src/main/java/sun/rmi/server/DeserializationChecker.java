package sun.rmi.server;

import java.io.ObjectStreamClass;
import java.lang.reflect.Method;

/* loaded from: rt.jar:sun/rmi/server/DeserializationChecker.class */
public interface DeserializationChecker {
    void check(Method method, ObjectStreamClass objectStreamClass, int i2, int i3);

    void checkProxyClass(Method method, String[] strArr, int i2, int i3);

    default void end(int i2) {
    }
}
