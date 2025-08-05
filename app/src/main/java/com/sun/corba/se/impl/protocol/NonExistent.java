package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.spi.oa.NullServant;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import org.omg.CORBA.portable.OutputStream;

/* compiled from: SpecialMethod.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/NonExistent.class */
class NonExistent extends SpecialMethod {
    NonExistent() {
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public boolean isNonExistentMethod() {
        return true;
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public String getName() {
        return "_non_existent";
    }

    @Override // com.sun.corba.se.impl.protocol.SpecialMethod
    public CorbaMessageMediator invoke(Object obj, CorbaMessageMediator corbaMessageMediator, byte[] bArr, ObjectAdapter objectAdapter) {
        boolean z2 = obj == null || (obj instanceof NullServant);
        CorbaMessageMediator corbaMessageMediatorCreateResponse = corbaMessageMediator.getProtocolHandler().createResponse(corbaMessageMediator, null);
        ((OutputStream) corbaMessageMediatorCreateResponse.getOutputObject()).write_boolean(z2);
        return corbaMessageMediatorCreateResponse;
    }
}
