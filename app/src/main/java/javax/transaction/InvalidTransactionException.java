package javax.transaction;

import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/transaction/InvalidTransactionException.class */
public class InvalidTransactionException extends RemoteException {
    public InvalidTransactionException() {
    }

    public InvalidTransactionException(String str) {
        super(str);
    }
}
