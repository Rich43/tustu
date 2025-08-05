package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/NoServant.class */
public final class NoServant extends UserException {
    public NoServant() {
        super(NoServantHelper.id());
    }

    public NoServant(String str) {
        super(NoServantHelper.id() + Constants.INDENT + str);
    }
}
