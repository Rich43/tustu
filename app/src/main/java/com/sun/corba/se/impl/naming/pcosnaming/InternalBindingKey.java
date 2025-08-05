package com.sun.corba.se.impl.naming.pcosnaming;

import java.io.Serializable;
import org.omg.CosNaming.NameComponent;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/InternalBindingKey.class */
public class InternalBindingKey implements Serializable {
    private static final long serialVersionUID = -5410796631793704055L;
    public String id;
    public String kind;

    public InternalBindingKey() {
    }

    public InternalBindingKey(NameComponent nameComponent) {
        setup(nameComponent);
    }

    protected void setup(NameComponent nameComponent) {
        this.id = nameComponent.id;
        this.kind = nameComponent.kind;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof InternalBindingKey)) {
            InternalBindingKey internalBindingKey = (InternalBindingKey) obj;
            if (this.id != null && internalBindingKey.id != null) {
                if (this.id.length() != internalBindingKey.id.length()) {
                    return false;
                }
                if (this.id.length() > 0 && !this.id.equals(internalBindingKey.id)) {
                    return false;
                }
            } else {
                if (this.id == null && internalBindingKey.id != null) {
                    return false;
                }
                if (this.id != null && internalBindingKey.id == null) {
                    return false;
                }
            }
            if (this.kind != null && internalBindingKey.kind != null) {
                if (this.kind.length() != internalBindingKey.kind.length()) {
                    return false;
                }
                if (this.kind.length() > 0 && !this.kind.equals(internalBindingKey.kind)) {
                    return false;
                }
                return true;
            }
            if (this.kind == null && internalBindingKey.kind != null) {
                return false;
            }
            if (this.kind != null && internalBindingKey.kind == null) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = 0;
        if (this.id.length() > 0) {
            iHashCode = 0 + this.id.hashCode();
        }
        if (this.kind.length() > 0) {
            iHashCode += this.kind.hashCode();
        }
        return iHashCode;
    }
}
