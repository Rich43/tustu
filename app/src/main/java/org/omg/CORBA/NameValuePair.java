package org.omg.CORBA;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CORBA/NameValuePair.class */
public final class NameValuePair implements IDLEntity {
    public String id;
    public Any value;

    public NameValuePair() {
    }

    public NameValuePair(String str, Any any) {
        this.id = str;
        this.value = any;
    }
}
