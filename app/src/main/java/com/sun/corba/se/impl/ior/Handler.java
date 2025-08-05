package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA_2_3.portable.InputStream;

/* compiled from: ObjectKeyFactoryImpl.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/ior/Handler.class */
interface Handler {
    ObjectKeyTemplate handle(int i2, int i3, InputStream inputStream, OctetSeqHolder octetSeqHolder);
}
