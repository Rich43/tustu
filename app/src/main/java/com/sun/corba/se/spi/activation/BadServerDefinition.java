package com.sun.corba.se.spi.activation;

import org.omg.CORBA.UserException;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/BadServerDefinition.class */
public final class BadServerDefinition extends UserException {
    public String reason;

    public BadServerDefinition() {
        super(BadServerDefinitionHelper.id());
        this.reason = null;
    }

    public BadServerDefinition(String str) {
        super(BadServerDefinitionHelper.id());
        this.reason = null;
        this.reason = str;
    }

    public BadServerDefinition(String str, String str2) {
        super(BadServerDefinitionHelper.id() + Constants.INDENT + str);
        this.reason = null;
        this.reason = str2;
    }
}
