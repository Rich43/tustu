package java.rmi.server;

import java.rmi.RemoteException;

@Deprecated
/* loaded from: rt.jar:java/rmi/server/SkeletonNotFoundException.class */
public class SkeletonNotFoundException extends RemoteException {
    private static final long serialVersionUID = -7860299673822761231L;

    public SkeletonNotFoundException(String str) {
        super(str);
    }

    public SkeletonNotFoundException(String str, Exception exc) {
        super(str, exc);
    }
}
