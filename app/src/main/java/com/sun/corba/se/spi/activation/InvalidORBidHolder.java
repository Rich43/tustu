package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/InvalidORBidHolder.class */
public final class InvalidORBidHolder implements Streamable {
    public InvalidORBid value;

    public InvalidORBidHolder() {
        this.value = null;
    }

    public InvalidORBidHolder(InvalidORBid invalidORBid) {
        this.value = null;
        this.value = invalidORBid;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = InvalidORBidHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        InvalidORBidHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return InvalidORBidHelper.type();
    }
}
