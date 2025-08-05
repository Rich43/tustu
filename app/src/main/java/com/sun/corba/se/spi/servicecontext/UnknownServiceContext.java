package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/UnknownServiceContext.class */
public class UnknownServiceContext extends ServiceContext {
    private int id;
    private byte[] data;

    public UnknownServiceContext(int i2, byte[] bArr) {
        this.id = -1;
        this.data = null;
        this.id = i2;
        this.data = bArr;
    }

    public UnknownServiceContext(int i2, InputStream inputStream) {
        this.id = -1;
        this.data = null;
        this.id = i2;
        int i3 = inputStream.read_long();
        this.data = new byte[i3];
        inputStream.read_octet_array(this.data, 0, i3);
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public int getId() {
        return this.id;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public void writeData(OutputStream outputStream) throws SystemException {
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public void write(OutputStream outputStream, GIOPVersion gIOPVersion) throws SystemException {
        outputStream.write_long(this.id);
        outputStream.write_long(this.data.length);
        outputStream.write_octet_array(this.data, 0, this.data.length);
    }

    public byte[] getData() {
        return this.data;
    }
}
