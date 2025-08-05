package java.security;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:java/security/PermissionCollection.class */
public abstract class PermissionCollection implements Serializable {
    private static final long serialVersionUID = -6727011328946861783L;
    private volatile boolean readOnly;

    public abstract void add(Permission permission);

    public abstract boolean implies(Permission permission);

    public abstract Enumeration<Permission> elements();

    public void setReadOnly() {
        this.readOnly = true;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public String toString() {
        Enumeration<Permission> enumerationElements = elements();
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString() + " (\n");
        while (enumerationElements.hasMoreElements()) {
            try {
                sb.append(" ");
                sb.append(enumerationElements.nextElement().toString());
                sb.append("\n");
            } catch (NoSuchElementException e2) {
            }
        }
        sb.append(")\n");
        return sb.toString();
    }
}
