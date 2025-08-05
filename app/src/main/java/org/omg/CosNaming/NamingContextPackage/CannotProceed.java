package org.omg.CosNaming.NamingContextPackage;

import org.omg.CORBA.UserException;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/CosNaming/NamingContextPackage/CannotProceed.class */
public final class CannotProceed extends UserException {
    public NamingContext cxt;
    public NameComponent[] rest_of_name;

    public CannotProceed() {
        super(CannotProceedHelper.id());
        this.cxt = null;
        this.rest_of_name = null;
    }

    public CannotProceed(NamingContext namingContext, NameComponent[] nameComponentArr) {
        super(CannotProceedHelper.id());
        this.cxt = null;
        this.rest_of_name = null;
        this.cxt = namingContext;
        this.rest_of_name = nameComponentArr;
    }

    public CannotProceed(String str, NamingContext namingContext, NameComponent[] nameComponentArr) {
        super(CannotProceedHelper.id() + Constants.INDENT + str);
        this.cxt = null;
        this.rest_of_name = null;
        this.cxt = namingContext;
        this.rest_of_name = nameComponentArr;
    }
}
