package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/ServantAlreadyActive.class */
public final class ServantAlreadyActive extends UserException {
    public ServantAlreadyActive() {
        super(ServantAlreadyActiveHelper.id());
    }

    public ServantAlreadyActive(String str) {
        super(ServantAlreadyActiveHelper.id() + Constants.INDENT + str);
    }
}
