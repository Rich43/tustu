package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/ServantNotActive.class */
public final class ServantNotActive extends UserException {
    public ServantNotActive() {
        super(ServantNotActiveHelper.id());
    }

    public ServantNotActive(String str) {
        super(ServantNotActiveHelper.id() + Constants.INDENT + str);
    }
}
