package org.omg.DynamicAny;

import org.omg.CORBA.Any;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/DynamicAny/NameValuePair.class */
public final class NameValuePair implements IDLEntity {
    public String id;
    public Any value;

    public NameValuePair() {
        this.id = null;
        this.value = null;
    }

    public NameValuePair(String str, Any any) {
        this.id = null;
        this.value = null;
        this.id = str;
        this.value = any;
    }
}
