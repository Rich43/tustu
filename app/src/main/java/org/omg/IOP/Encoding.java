package org.omg.IOP;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/IOP/Encoding.class */
public final class Encoding implements IDLEntity {
    public short format;
    public byte major_version;
    public byte minor_version;

    public Encoding() {
        this.format = (short) 0;
        this.major_version = (byte) 0;
        this.minor_version = (byte) 0;
    }

    public Encoding(short s2, byte b2, byte b3) {
        this.format = (short) 0;
        this.major_version = (byte) 0;
        this.minor_version = (byte) 0;
        this.format = s2;
        this.major_version = b2;
        this.minor_version = b3;
    }
}
