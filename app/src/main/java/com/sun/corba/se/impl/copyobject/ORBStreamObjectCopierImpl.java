package com.sun.corba.se.impl.copyobject;

import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.copyobject.ObjectCopier;
import java.io.Serializable;
import java.rmi.Remote;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/copyobject/ORBStreamObjectCopierImpl.class */
public class ORBStreamObjectCopierImpl implements ObjectCopier {
    private ORB orb;

    public ORBStreamObjectCopierImpl(ORB orb) {
        this.orb = orb;
    }

    @Override // com.sun.corba.se.spi.copyobject.ObjectCopier
    public Object copy(Object obj) {
        if (obj instanceof Remote) {
            return Utility.autoConnect(obj, this.orb, true);
        }
        OutputStream outputStream = (OutputStream) this.orb.create_output_stream();
        outputStream.write_value((Serializable) obj);
        return ((InputStream) outputStream.create_input_stream()).read_value();
    }
}
