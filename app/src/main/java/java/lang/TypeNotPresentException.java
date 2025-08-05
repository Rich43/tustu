package java.lang;

/* loaded from: rt.jar:java/lang/TypeNotPresentException.class */
public class TypeNotPresentException extends RuntimeException {
    private static final long serialVersionUID = -5101214195716534496L;
    private String typeName;

    public TypeNotPresentException(String str, Throwable th) {
        super("Type " + str + " not present", th);
        this.typeName = str;
    }

    public String typeName() {
        return this.typeName;
    }
}
