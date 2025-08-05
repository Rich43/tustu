package com.sun.corba.se.spi.ior;

import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/ObjectKeyFactory.class */
public interface ObjectKeyFactory {
    ObjectKey create(byte[] bArr);

    ObjectKeyTemplate createTemplate(InputStream inputStream);
}
