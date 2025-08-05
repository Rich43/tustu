package com.sun.corba.se.spi.ior;

import com.sun.corba.se.impl.ior.EncapsulationUtility;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/IdentifiableBase.class */
public abstract class IdentifiableBase implements Identifiable, WriteContents {
    @Override // com.sun.corba.se.spi.ior.Writeable
    public final void write(OutputStream outputStream) {
        EncapsulationUtility.writeEncapsulation(this, outputStream);
    }
}
