package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.UserException;
import org.omg.CosNaming.NameComponent;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/NotFound.class */
public final class NotFound extends UserException {
    public NotFoundReason why;
    public NameComponent[] rest_of_name;

    public NotFound() {
        super(NotFoundHelper.id());
        this.why = null;
        this.rest_of_name = null;
    }

    public NotFound(NotFoundReason notFoundReason, NameComponent[] nameComponentArr) {
        super(NotFoundHelper.id());
        this.why = null;
        this.rest_of_name = null;
        this.why = notFoundReason;
        this.rest_of_name = nameComponentArr;
    }

    public NotFound(String str, NotFoundReason notFoundReason, NameComponent[] nameComponentArr) {
        super(NotFoundHelper.id() + Constants.INDENT + str);
        this.why = null;
        this.rest_of_name = null;
        this.why = notFoundReason;
        this.rest_of_name = nameComponentArr;
    }
}
