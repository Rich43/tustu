package org.omg.PortableServer.CurrentPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/CurrentPackage/NoContext.class */
public final class NoContext extends UserException {
    public NoContext() {
        super(NoContextHelper.id());
    }

    public NoContext(String str) {
        super(NoContextHelper.id() + Constants.INDENT + str);
    }
}
