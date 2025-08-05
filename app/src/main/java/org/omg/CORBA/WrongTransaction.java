package org.omg.CORBA;

import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:org/omg/CORBA/WrongTransaction.class */
public final class WrongTransaction extends UserException {
    public WrongTransaction() {
        super(WrongTransactionHelper.id());
    }

    public WrongTransaction(String str) {
        super(WrongTransactionHelper.id() + Constants.INDENT + str);
    }
}
