package sun.reflect.annotation;

/* loaded from: rt.jar:sun/reflect/annotation/TypeNotPresentExceptionProxy.class */
public class TypeNotPresentExceptionProxy extends ExceptionProxy {
    private static final long serialVersionUID = 5565925172427947573L;
    String typeName;
    Throwable cause;

    public TypeNotPresentExceptionProxy(String str, Throwable th) {
        this.typeName = str;
        this.cause = th;
    }

    @Override // sun.reflect.annotation.ExceptionProxy
    protected RuntimeException generateException() {
        return new TypeNotPresentException(this.typeName, this.cause);
    }
}
