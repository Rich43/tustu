package java.rmi.server;

import java.rmi.RemoteException;

/* loaded from: rt.jar:java/rmi/server/ExportException.class */
public class ExportException extends RemoteException {
    private static final long serialVersionUID = -9155485338494060170L;

    public ExportException(String str) {
        super(str);
    }

    public ExportException(String str, Exception exc) {
        super(str, exc);
    }
}
