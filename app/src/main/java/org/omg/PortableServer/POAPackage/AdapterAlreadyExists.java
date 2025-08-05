package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/AdapterAlreadyExists.class */
public final class AdapterAlreadyExists extends UserException {
    public AdapterAlreadyExists() {
        super(AdapterAlreadyExistsHelper.id());
    }

    public AdapterAlreadyExists(String str) {
        super(AdapterAlreadyExistsHelper.id() + Constants.INDENT + str);
    }
}
