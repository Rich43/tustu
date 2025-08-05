package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/ObjectNotActive.class */
public final class ObjectNotActive extends UserException {
    public ObjectNotActive() {
        super(ObjectNotActiveHelper.id());
    }

    public ObjectNotActive(String str) {
        super(ObjectNotActiveHelper.id() + Constants.INDENT + str);
    }
}
