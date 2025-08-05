package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.impl.ior.IORImpl;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/SendingContextServiceContext.class */
public class SendingContextServiceContext extends ServiceContext {
    public static final int SERVICE_CONTEXT_ID = 6;
    private IOR ior;

    public SendingContextServiceContext(IOR ior) {
        this.ior = null;
        this.ior = ior;
    }

    public SendingContextServiceContext(InputStream inputStream, GIOPVersion gIOPVersion) {
        super(inputStream, gIOPVersion);
        this.ior = null;
        this.ior = new IORImpl(this.in);
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public int getId() {
        return 6;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public void writeData(OutputStream outputStream) throws SystemException {
        this.ior.write(outputStream);
    }

    public IOR getIOR() {
        return this.ior;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public String toString() {
        return "SendingContexServiceContext[ ior=" + ((Object) this.ior) + " ]";
    }
}
