package javax.management;

/* loaded from: rt.jar:javax/management/BadBinaryOpValueExpException.class */
public class BadBinaryOpValueExpException extends Exception {
    private static final long serialVersionUID = 5068475589449021227L;
    private ValueExp exp;

    public BadBinaryOpValueExpException(ValueExp valueExp) {
        this.exp = valueExp;
    }

    public ValueExp getExp() {
        return this.exp;
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "BadBinaryOpValueExpException: " + ((Object) this.exp);
    }
}
