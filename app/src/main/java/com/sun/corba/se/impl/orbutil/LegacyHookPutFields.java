package com.sun.corba.se.impl.orbutil;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Hashtable;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/LegacyHookPutFields.class */
class LegacyHookPutFields extends ObjectOutputStream.PutField {
    private Hashtable fields = new Hashtable();

    LegacyHookPutFields() {
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, boolean z2) {
        this.fields.put(str, new Boolean(z2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, char c2) {
        this.fields.put(str, new Character(c2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, byte b2) {
        this.fields.put(str, new Byte(b2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, short s2) {
        this.fields.put(str, new Short(s2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, int i2) {
        this.fields.put(str, new Integer(i2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, long j2) {
        this.fields.put(str, new Long(j2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, float f2) {
        this.fields.put(str, new Float(f2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, double d2) {
        this.fields.put(str, new Double(d2));
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void put(String str, Object obj) {
        this.fields.put(str, obj);
    }

    @Override // java.io.ObjectOutputStream.PutField
    public void write(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeObject(this.fields);
    }
}
