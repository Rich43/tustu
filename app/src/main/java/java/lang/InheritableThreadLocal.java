package java.lang;

import java.lang.ThreadLocal;

/* loaded from: rt.jar:java/lang/InheritableThreadLocal.class */
public class InheritableThreadLocal<T> extends ThreadLocal<T> {
    @Override // java.lang.ThreadLocal
    protected T childValue(T t2) {
        return t2;
    }

    @Override // java.lang.ThreadLocal
    ThreadLocal.ThreadLocalMap getMap(Thread thread) {
        return thread.inheritableThreadLocals;
    }

    @Override // java.lang.ThreadLocal
    void createMap(Thread thread, T t2) {
        thread.inheritableThreadLocals = new ThreadLocal.ThreadLocalMap(this, t2);
    }
}
