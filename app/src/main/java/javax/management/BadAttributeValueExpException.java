package javax.management;

import java.io.IOException;
import java.io.ObjectInputStream;

/* loaded from: rt.jar:javax/management/BadAttributeValueExpException.class */
public class BadAttributeValueExpException extends Exception {
    private static final long serialVersionUID = -3105272988410493376L;
    private Object val;

    public BadAttributeValueExpException(Object obj) {
        this.val = obj == null ? null : obj.toString();
    }

    @Override // java.lang.Throwable
    public String toString() {
        return "BadAttributeValueException: " + this.val;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Object obj = objectInputStream.readFields().get("val", (Object) null);
        if (obj == null) {
            this.val = null;
            return;
        }
        if (obj instanceof String) {
            this.val = obj;
            return;
        }
        if (System.getSecurityManager() == null || (obj instanceof Long) || (obj instanceof Integer) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Boolean)) {
            this.val = obj.toString();
        } else {
            this.val = System.identityHashCode(obj) + "@" + obj.getClass().getName();
        }
    }
}
