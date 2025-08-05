package javax.transaction;

import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/transaction/TransactionRolledbackException.class */
public class TransactionRolledbackException extends RemoteException {
    public TransactionRolledbackException() {
    }

    public TransactionRolledbackException(String str) {
        super(str);
    }
}
