package org.omg.CosNaming;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CosNaming/NameComponent.class */
public final class NameComponent implements IDLEntity {
    public String id;
    public String kind;

    public NameComponent() {
        this.id = null;
        this.kind = null;
    }

    public NameComponent(String str, String str2) {
        this.id = null;
        this.kind = null;
        this.id = str;
        this.kind = str2;
    }
}
