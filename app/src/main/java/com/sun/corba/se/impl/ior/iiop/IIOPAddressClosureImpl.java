package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.spi.orbutil.closure.Closure;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/IIOPAddressClosureImpl.class */
public final class IIOPAddressClosureImpl extends IIOPAddressBase {
    private Closure host;
    private Closure port;

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

    public IIOPAddressClosureImpl(Closure closure, Closure closure2) {
        this.host = closure;
        this.port = closure2;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPAddress
    public String getHost() {
        return (String) this.host.evaluate();
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPAddress
    public int getPort() {
        return ((Integer) this.port.evaluate()).intValue();
    }
}
