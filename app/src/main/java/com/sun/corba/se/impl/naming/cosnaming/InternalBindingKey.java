package com.sun.corba.se.impl.naming.cosnaming;

import org.omg.CosNaming.NameComponent;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/cosnaming/InternalBindingKey.class */
public class InternalBindingKey {
    public NameComponent name;
    private int idLen;
    private int kindLen;
    private int hashVal;

    public InternalBindingKey() {
    }

    public InternalBindingKey(NameComponent nameComponent) {
        this.idLen = 0;
        this.kindLen = 0;
        setup(nameComponent);
    }

    protected void setup(NameComponent nameComponent) {
        this.name = nameComponent;
        if (this.name.id != null) {
            this.idLen = this.name.id.length();
        }
        if (this.name.kind != null) {
            this.kindLen = this.name.kind.length();
        }
        this.hashVal = 0;
        if (this.idLen > 0) {
            this.hashVal += this.name.id.hashCode();
        }
        if (this.kindLen > 0) {
            this.hashVal += this.name.kind.hashCode();
        }
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof InternalBindingKey)) {
            InternalBindingKey internalBindingKey = (InternalBindingKey) obj;
            if (this.idLen != internalBindingKey.idLen || this.kindLen != internalBindingKey.kindLen) {
                return false;
            }
            if (this.idLen > 0 && !this.name.id.equals(internalBindingKey.name.id)) {
                return false;
            }
            if (this.kindLen > 0 && !this.name.kind.equals(internalBindingKey.name.kind)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.hashVal;
    }
}
