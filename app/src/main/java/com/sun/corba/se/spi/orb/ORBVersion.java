package com.sun.corba.se.spi.orb;

import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/orb/ORBVersion.class */
public interface ORBVersion extends Comparable {
    public static final byte FOREIGN = 0;
    public static final byte OLD = 1;
    public static final byte NEW = 2;
    public static final byte JDK1_3_1_01 = 3;
    public static final byte NEWER = 10;
    public static final byte PEORB = 20;

    byte getORBType();

    void write(OutputStream outputStream);

    boolean lessThan(ORBVersion oRBVersion);
}
