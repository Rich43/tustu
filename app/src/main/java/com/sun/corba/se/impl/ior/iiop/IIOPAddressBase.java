package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/IIOPAddressBase.class */
abstract class IIOPAddressBase implements IIOPAddress {
    IIOPAddressBase() {
    }

    protected short intToShort(int i2) {
        if (i2 > 32767) {
            return (short) (i2 - 65536);
        }
        return (short) i2;
    }

    protected int shortToInt(short s2) {
        if (s2 < 0) {
            return s2 + 65536;
        }
        return s2;
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        outputStream.write_string(getHost());
        outputStream.write_short(intToShort(getPort()));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof IIOPAddress)) {
            return false;
        }
        IIOPAddress iIOPAddress = (IIOPAddress) obj;
        return getHost().equals(iIOPAddress.getHost()) && getPort() == iIOPAddress.getPort();
    }

    public int hashCode() {
        return getHost().hashCode() ^ getPort();
    }

    public String toString() {
        return "IIOPAddress[" + getHost() + "," + getPort() + "]";
    }
}
