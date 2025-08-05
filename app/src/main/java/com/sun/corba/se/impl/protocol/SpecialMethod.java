package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/SpecialMethod.class */
public abstract class SpecialMethod {
    static SpecialMethod[] methods = {new IsA(), new GetInterface(), new NonExistent(), new NotExistent()};

    public abstract boolean isNonExistentMethod();

    public abstract String getName();

    public abstract CorbaMessageMediator invoke(Object obj, CorbaMessageMediator corbaMessageMediator, byte[] bArr, ObjectAdapter objectAdapter);

    public static final SpecialMethod getSpecialMethod(String str) {
        for (int i2 = 0; i2 < methods.length; i2++) {
            if (methods[i2].getName().equals(str)) {
                return methods[i2];
            }
        }
        return null;
    }
}
