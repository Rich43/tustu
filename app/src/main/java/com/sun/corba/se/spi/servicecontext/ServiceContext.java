package com.sun.corba.se.spi.servicecontext;

import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.SystemException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/spi/servicecontext/ServiceContext.class */
public abstract class ServiceContext {
    protected InputStream in;

    public abstract int getId();

    protected abstract void writeData(OutputStream outputStream);

    protected ServiceContext() {
        this.in = null;
    }

    private void dprint(String str) {
        ORBUtility.dprint(this, str);
    }

    protected ServiceContext(InputStream inputStream, GIOPVersion gIOPVersion) throws SystemException {
        this.in = null;
        this.in = inputStream;
    }

    public void write(OutputStream outputStream, GIOPVersion gIOPVersion) throws SystemException {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB) outputStream.orb(), gIOPVersion);
        encapsOutputStreamNewEncapsOutputStream.putEndian();
        writeData(encapsOutputStreamNewEncapsOutputStream);
        byte[] byteArray = encapsOutputStreamNewEncapsOutputStream.toByteArray();
        outputStream.write_long(getId());
        outputStream.write_long(byteArray.length);
        outputStream.write_octet_array(byteArray, 0, byteArray.length);
    }

    public String toString() {
        return "ServiceContext[ id=" + getId() + " ]";
    }
}
