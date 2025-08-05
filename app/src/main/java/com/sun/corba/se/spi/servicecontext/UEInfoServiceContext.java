package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/UEInfoServiceContext.class */
public class UEInfoServiceContext extends ServiceContext {
    public static final int SERVICE_CONTEXT_ID = 9;
    private Throwable unknown;

    public UEInfoServiceContext(Throwable th) {
        this.unknown = null;
        this.unknown = th;
    }

    public UEInfoServiceContext(InputStream inputStream, GIOPVersion gIOPVersion) {
        super(inputStream, gIOPVersion);
        this.unknown = null;
        try {
            this.unknown = (Throwable) this.in.read_value();
        } catch (ThreadDeath e2) {
            throw e2;
        } catch (Throwable th) {
            this.unknown = new UNKNOWN(0, CompletionStatus.COMPLETED_MAYBE);
        }
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public int getId() {
        return 9;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public void writeData(OutputStream outputStream) throws SystemException {
        outputStream.write_value(this.unknown);
    }

    public Throwable getUE() {
        return this.unknown;
    }

    @Override // com.sun.corba.se.spi.servicecontext.ServiceContext
    public String toString() {
        return "UEInfoServiceContext[ unknown=" + this.unknown.toString() + " ]";
    }
}
