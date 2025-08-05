package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/ObjectAlreadyActive.class */
public final class ObjectAlreadyActive extends UserException {
    public ObjectAlreadyActive() {
        super(ObjectAlreadyActiveHelper.id());
    }

    public ObjectAlreadyActive(String str) {
        super(ObjectAlreadyActiveHelper.id() + Constants.INDENT + str);
    }
}
