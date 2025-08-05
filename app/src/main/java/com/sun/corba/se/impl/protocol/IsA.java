package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.NullServant;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;

/* compiled from: SpecialMethod.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/IsA.class */
class IsA extends SpecialMethod {
    IsA() {
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public boolean isNonExistentMethod() {
        return false;
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public String getName() {
        return "_is_a";
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public CorbaMessageMediator invoke(Object obj, CorbaMessageMediator corbaMessageMediator, byte[] bArr, ObjectAdapter objectAdapter) {
        if (obj == null || (obj instanceof NullServant)) {
            return corbaMessageMediator.getProtocolHandler().createSystemExceptionResponse(corbaMessageMediator, ORBUtilSystemException.get((ORB) corbaMessageMediator.getBroker(), CORBALogDomains.OA_INVOCATION).badSkeleton(), null);
        }
        String[] interfaces = objectAdapter.getInterfaces(obj, bArr);
        String str = ((InputStream) corbaMessageMediator.getInputObject()).read_string();
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= interfaces.length) {
                break;
            }
            if (!interfaces[i2].equals(str)) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        CorbaMessageMediator corbaMessageMediatorCreateResponse = corbaMessageMediator.getProtocolHandler().createResponse(corbaMessageMediator, null);
        ((OutputStream) corbaMessageMediatorCreateResponse.getOutputObject()).write_boolean(z2);
        return corbaMessageMediatorCreateResponse;
    }
}
