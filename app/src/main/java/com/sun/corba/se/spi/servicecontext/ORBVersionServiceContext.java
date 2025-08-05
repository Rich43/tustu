package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/ORBVersionServiceContext.class */
public class ORBVersionServiceContext extends ServiceContext {
    public static final int SERVICE_CONTEXT_ID = 1313165056;
    private ORBVersion version;

    public ORBVersionServiceContext() {
        this.version = ORBVersionFactory.getORBVersion();
        this.version = ORBVersionFactory.getORBVersion();
    }

    public ORBVersionServiceContext(ORBVersion oRBVersion) {
        this.version = ORBVersionFactory.getORBVersion();
        this.version = oRBVersion;
    }

    public ORBVersionServiceContext(InputStream inputStream, GIOPVersion gIOPVersion) {
        super(inputStream, gIOPVersion);
        this.version = ORBVersionFactory.getORBVersion();
        this.version = ORBVersionFactory.create(this.in);
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public int getId() {
        return 1313165056;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public void writeData(OutputStream outputStream) throws SystemException {
        this.version.write(outputStream);
    }

    public ORBVersion getVersion() {
        return this.version;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public String toString() {
        return "ORBVersionServiceContext[ version=" + ((Object) this.version) + " ]";
    }
}
