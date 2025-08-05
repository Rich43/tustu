package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.ORBVersion;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ORBVersionImpl.class */
public class ORBVersionImpl implements ORBVersion {
    private byte orbType;
    public static final ORBVersion FOREIGN = new ORBVersionImpl((byte) 0);
    public static final ORBVersion OLD = new ORBVersionImpl((byte) 1);
    public static final ORBVersion NEW = new ORBVersionImpl((byte) 2);
    public static final ORBVersion JDK1_3_1_01 = new ORBVersionImpl((byte) 3);
    public static final ORBVersion NEWER = new ORBVersionImpl((byte) 10);
    public static final ORBVersion PEORB = new ORBVersionImpl((byte) 20);

    public ORBVersionImpl(byte b2) {
        this.orbType = b2;
    }

    @Override // com.sun.corba.se.spi.orb.ORBVersion
    public byte getORBType() {
        return this.orbType;
    }

    @Override // com.sun.corba.se.spi.orb.ORBVersion
    public void write(OutputStream outputStream) {
        outputStream.write_octet(this.orbType);
    }

    public String toString() {
        return "ORBVersionImpl[" + Byte.toString(this.orbType) + "]";
    }

    public boolean equals(Object obj) {
        return (obj instanceof ORBVersion) && ((ORBVersion) obj).getORBType() == this.orbType;
    }

    public int hashCode() {
        return this.orbType;
    }

    @Override // com.sun.corba.se.spi.orb.ORBVersion
    public boolean lessThan(ORBVersion oRBVersion) {
        return this.orbType < oRBVersion.getORBType();
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        return getORBType() - ((ORBVersion) obj).getORBType();
    }
}
