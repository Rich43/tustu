package sun.reflect.annotation;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.lang.reflect.Method;

/* loaded from: rt.jar:sun/reflect/annotation/AnnotationTypeMismatchExceptionProxy.class */
class AnnotationTypeMismatchExceptionProxy extends ExceptionProxy {
    private static final long serialVersionUID = 7844069490309503934L;
    private Method member;
    private String foundType;

    AnnotationTypeMismatchExceptionProxy(String str) {
        this.foundType = str;
    }

    AnnotationTypeMismatchExceptionProxy setMember(Method method) {
        this.member = method;
        return this;
    }

    @Override // sun.reflect.annotation.ExceptionProxy
    protected RuntimeException generateException() {
        return new AnnotationTypeMismatchException(this.member, this.foundType);
    }
}
