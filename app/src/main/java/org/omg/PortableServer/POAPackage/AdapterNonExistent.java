package org.omg.PortableServer.POAPackage;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/PortableServer/POAPackage/AdapterNonExistent.class */
public final class AdapterNonExistent extends UserException {
    public AdapterNonExistent() {
        super(AdapterNonExistentHelper.id());
    }

    public AdapterNonExistent(String str) {
        super(AdapterNonExistentHelper.id() + Constants.INDENT + str);
    }
}
