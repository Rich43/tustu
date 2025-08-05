package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.spi.ior.TaggedComponentBase;
import com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent;
import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/AlternateIIOPAddressComponentImpl.class */
public class AlternateIIOPAddressComponentImpl extends TaggedComponentBase implements AlternateIIOPAddressComponent {
    private IIOPAddress addr;

    public boolean equals(Object obj) {
        if (!(obj instanceof AlternateIIOPAddressComponentImpl)) {
            return false;
        }
        return this.addr.equals(((AlternateIIOPAddressComponentImpl) obj).addr);
    }

    public int hashCode() {
        return this.addr.hashCode();
    }

    public String toString() {
        return "AlternateIIOPAddressComponentImpl[addr=" + ((Object) this.addr) + "]";
    }

    public AlternateIIOPAddressComponentImpl(IIOPAddress iIOPAddress) {
        this.addr = iIOPAddress;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent
    public IIOPAddress getAddress() {
        return this.addr;
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        this.addr.write(outputStream);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return 3;
    }
}
