package org.omg.PortableInterceptor.ORBInitInfoPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableInterceptor/ORBInitInfoPackage/InvalidName.class */
public final class InvalidName extends UserException {
    public InvalidName() {
        super(InvalidNameHelper.id());
    }

    public InvalidName(String str) {
        super(InvalidNameHelper.id() + Constants.INDENT + str);
    }
}
