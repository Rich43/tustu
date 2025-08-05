package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/MaxStreamFormatVersionServiceContext.class */
public class MaxStreamFormatVersionServiceContext extends ServiceContext {
    private byte maxStreamFormatVersion;
    public static final MaxStreamFormatVersionServiceContext singleton = new MaxStreamFormatVersionServiceContext();
    public static final int SERVICE_CONTEXT_ID = 17;

    public MaxStreamFormatVersionServiceContext() {
        this.maxStreamFormatVersion = ORBUtility.getMaxStreamFormatVersion();
    }

    public MaxStreamFormatVersionServiceContext(byte b2) {
        this.maxStreamFormatVersion = b2;
    }

    public MaxStreamFormatVersionServiceContext(InputStream inputStream, GIOPVersion gIOPVersion) {
        super(inputStream, gIOPVersion);
        this.maxStreamFormatVersion = inputStream.read_octet();
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public int getId() {
        return 17;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public void writeData(OutputStream outputStream) throws SystemException {
        outputStream.write_octet(this.maxStreamFormatVersion);
    }

    public byte getMaximumStreamFormatVersion() {
        return this.maxStreamFormatVersion;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public String toString() {
        return "MaxStreamFormatVersionServiceContext[" + ((int) this.maxStreamFormatVersion) + "]";
    }
}
