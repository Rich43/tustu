package com.sun.corba.se.impl.orbutil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.Hashtable;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/LegacyHookGetFields.class */
class LegacyHookGetFields extends ObjectInputStream.GetField {
    private Hashtable fields;

    LegacyHookGetFields(Hashtable hashtable) {
        this.fields = null;
        this.fields = hashtable;
    }

    @Override // java.io.ObjectInputStream.GetField
    public ObjectStreamClass getObjectStreamClass() {
        return null;
    }

    @Override // java.io.ObjectInputStream.GetField
    public boolean defaulted(String str) throws IOException, IllegalArgumentException {
        return !this.fields.containsKey(str);
    }

    @Override // java.io.ObjectInputStream.GetField
    public boolean get(String str, boolean z2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return z2;
        }
        return ((Boolean) this.fields.get(str)).booleanValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public char get(String str, char c2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return c2;
        }
        return ((Character) this.fields.get(str)).charValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public byte get(String str, byte b2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return b2;
        }
        return ((Byte) this.fields.get(str)).byteValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public short get(String str, short s2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return s2;
        }
        return ((Short) this.fields.get(str)).shortValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public int get(String str, int i2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return i2;
        }
        return ((Integer) this.fields.get(str)).intValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public long get(String str, long j2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return j2;
        }
        return ((Long) this.fields.get(str)).longValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public float get(String str, float f2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return f2;
        }
        return ((Float) this.fields.get(str)).floatValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public double get(String str, double d2) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return d2;
        }
        return ((Double) this.fields.get(str)).doubleValue();
    }

    @Override // java.io.ObjectInputStream.GetField
    public Object get(String str, Object obj) throws IOException, IllegalArgumentException {
        if (defaulted(str)) {
            return obj;
        }
        return this.fields.get(str);
    }

    public String toString() {
        return this.fields.toString();
    }
}
