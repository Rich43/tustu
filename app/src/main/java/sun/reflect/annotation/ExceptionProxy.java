package sun.reflect.annotation;

import java.io.Serializable;

/* loaded from: rt.jar:sun/reflect/annotation/ExceptionProxy.class */
public abstract class ExceptionProxy implements Serializable {
    protected abstract RuntimeException generateException();
}
