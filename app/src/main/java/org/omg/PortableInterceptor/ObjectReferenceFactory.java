package org.omg.PortableInterceptor;

import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ValueBase;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ObjectReferenceFactory.class */
public interface ObjectReferenceFactory extends ValueBase {
    Object make_object(String str, byte[] bArr);
}
