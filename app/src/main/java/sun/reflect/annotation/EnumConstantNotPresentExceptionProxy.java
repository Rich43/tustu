package sun.reflect.annotation;

/* loaded from: rt.jar:sun/reflect/annotation/EnumConstantNotPresentExceptionProxy.class */
public class EnumConstantNotPresentExceptionProxy extends ExceptionProxy {
    private static final long serialVersionUID = -604662101303187330L;
    Class<? extends Enum<?>> enumType;
    String constName;

    public EnumConstantNotPresentExceptionProxy(Class<? extends Enum<?>> cls, String str) {
        this.enumType = cls;
        this.constName = str;
    }

    @Override // sun.reflect.annotation.ExceptionProxy
    protected RuntimeException generateException() {
        return new EnumConstantNotPresentException(this.enumType, this.constName);
    }
}
