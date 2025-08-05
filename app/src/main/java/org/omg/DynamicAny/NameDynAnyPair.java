package org.omg.DynamicAny;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/DynamicAny/NameDynAnyPair.class */
public final class NameDynAnyPair implements IDLEntity {
    public String id;
    public DynAny value;

    public NameDynAnyPair() {
        this.id = null;
        this.value = null;
    }

    public NameDynAnyPair(String str, DynAny dynAny) {
        this.id = null;
        this.value = null;
        this.id = str;
        this.value = dynAny;
    }
}
