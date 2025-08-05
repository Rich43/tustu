package java.security;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/* loaded from: rt.jar:java/security/GuardedObject.class */
public class GuardedObject implements Serializable {
    private static final long serialVersionUID = -5240450096227834308L;
    private Object object;
    private Guard guard;

    public GuardedObject(Object obj, Guard guard) {
        this.guard = guard;
        this.object = obj;
    }

    public Object getObject() throws SecurityException {
        if (this.guard != null) {
            this.guard.checkGuard(this.object);
        }
        return this.object;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws SecurityException, IOException {
        if (this.guard != null) {
            this.guard.checkGuard(this.object);
        }
        objectOutputStream.defaultWriteObject();
    }
}
