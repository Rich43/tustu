package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.impl.logging.IORSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/IIOPAddressImpl.class */
public final class IIOPAddressImpl extends IIOPAddressBase {
    private ORB orb;
    private IORSystemException wrapper;
    private String host;
    private int port;

    @Override // com.sun.corba.se.impl.ior.iiop.IIOPAddressBase
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    @Override // com.sun.corba.se.impl.ior.iiop.IIOPAddressBase
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.sun.corba.se.impl.ior.iiop.IIOPAddressBase
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.sun.corba.se.impl.ior.iiop.IIOPAddressBase, com.sun.corba.se.spi.ior.Writeable
    public /* bridge */ /* synthetic */ void write(OutputStream outputStream) {
        super.write(outputStream);
    }

    public IIOPAddressImpl(ORB orb, String str, int i2) {
        this.orb = orb;
        this.wrapper = IORSystemException.get(orb, CORBALogDomains.OA_IOR);
        if (i2 < 0 || i2 > 65535) {
            throw this.wrapper.badIiopAddressPort(new Integer(i2));
        }
        this.host = str;
        this.port = i2;
    }

    public IIOPAddressImpl(InputStream inputStream) {
        this.host = inputStream.read_string();
        this.port = shortToInt(inputStream.read_short());
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPAddress
    public String getHost() {
        return this.host;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPAddress
    public int getPort() {
        return this.port;
    }
}
