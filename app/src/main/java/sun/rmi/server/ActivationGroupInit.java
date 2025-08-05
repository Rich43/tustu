package sun.rmi.server;

import java.rmi.activation.ActivationGroup;
import java.rmi.activation.ActivationGroupDesc;
import java.rmi.activation.ActivationGroupID;

/* loaded from: rt.jar:sun/rmi/server/ActivationGroupInit.class */
public abstract class ActivationGroupInit {
    public static void main(String[] strArr) {
        try {
            try {
                if (System.getSecurityManager() == null) {
                    System.setSecurityManager(new SecurityManager());
                }
                MarshalInputStream marshalInputStream = new MarshalInputStream(System.in);
                ActivationGroup.createGroup((ActivationGroupID) marshalInputStream.readObject(), (ActivationGroupDesc) marshalInputStream.readObject(), marshalInputStream.readLong());
                try {
                    System.in.close();
                } catch (Exception e2) {
                }
            } catch (Exception e3) {
                System.err.println("Exception in starting ActivationGroupInit:");
                e3.printStackTrace();
                try {
                    System.in.close();
                } catch (Exception e4) {
                }
            }
        } catch (Throwable th) {
            try {
                System.in.close();
            } catch (Exception e5) {
            }
            throw th;
        }
    }
}
