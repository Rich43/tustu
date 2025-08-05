package java.lang;

/* loaded from: rt.jar:java/lang/EnumConstantNotPresentException.class */
public class EnumConstantNotPresentException extends RuntimeException {
    private static final long serialVersionUID = -6046998521960521108L;
    private Class<? extends Enum> enumType;
    private String constantName;

    public EnumConstantNotPresentException(Class<? extends Enum> cls, String str) {
        super(cls.getName() + "." + str);
        this.enumType = cls;
        this.constantName = str;
    }

    public Class<? extends Enum> enumType() {
        return this.enumType;
    }

    public String constantName() {
        return this.constantName;
    }
}
