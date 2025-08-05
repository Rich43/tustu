package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedComponentBase;
import com.sun.corba.se.spi.ior.iiop.ORBTypeComponent;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/ORBTypeComponentImpl.class */
public class ORBTypeComponentImpl extends TaggedComponentBase implements ORBTypeComponent {
    private int ORBType;

    public boolean equals(Object obj) {
        return (obj instanceof ORBTypeComponentImpl) && this.ORBType == ((ORBTypeComponentImpl) obj).ORBType;
    }

    public int hashCode() {
        return this.ORBType;
    }

    public String toString() {
        return "ORBTypeComponentImpl[ORBType=" + this.ORBType + "]";
    }

    public ORBTypeComponentImpl(int i2) {
        this.ORBType = i2;
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return 0;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.ORBTypeComponent
    public int getORBType() {
        return this.ORBType;
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        outputStream.write_ulong(this.ORBType);
    }
}
