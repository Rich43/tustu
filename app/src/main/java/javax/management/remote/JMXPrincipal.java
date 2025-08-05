package javax.management.remote;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.security.Principal;

/* loaded from: rt.jar:javax/management/remote/JMXPrincipal.class */
public class JMXPrincipal implements Principal, Serializable {
    private static final long serialVersionUID = -4184480100214577411L;
    private String name;

    public JMXPrincipal(String str) throws NullPointerException {
        validate(str);
        this.name = str;
    }

    @Override // java.security.Principal
    public String getName() {
        return this.name;
    }

    @Override // java.security.Principal
    public String toString() {
        return "JMXPrincipal:  " + this.name;
    }

    @Override // java.security.Principal
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof JMXPrincipal)) {
            return false;
        }
        return getName().equals(((JMXPrincipal) obj).getName());
    }

    @Override // java.security.Principal
    public int hashCode() {
        return this.name.hashCode();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        String str = (String) objectInputStream.readFields().get("name", (Object) null);
        try {
            validate(str);
            this.name = str;
        } catch (NullPointerException e2) {
            throw new InvalidObjectException(e2.getMessage());
        }
    }

    private static void validate(String str) throws NullPointerException {
        if (str == null) {
            throw new NullPointerException("illegal null input");
        }
    }
}
