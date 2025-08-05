package javax.transaction;

import java.rmi.RemoteException;

/* loaded from: rt.jar:javax/transaction/TransactionRequiredException.class */
public class TransactionRequiredException extends RemoteException {
    public TransactionRequiredException() {
    }

    public TransactionRequiredException(String str) {
        super(str);
    }
}
