package java.rmi.activation;

import java.lang.reflect.Method;
import java.rmi.MarshalledObject;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

/* loaded from: rt.jar:java/rmi/activation/ActivationGroup_Stub.class */
public final class ActivationGroup_Stub extends RemoteStub implements ActivationInstantiator, Remote {
    private static final long serialVersionUID = 2;
    private static Method $method_newInstance_0;
    static Class class$java$rmi$activation$ActivationInstantiator;
    static Class class$java$rmi$activation$ActivationID;
    static Class class$java$rmi$activation$ActivationDesc;

    static {
        Class clsClass$;
        Class<?> clsClass$2;
        Class<?> clsClass$3;
        try {
            if (class$java$rmi$activation$ActivationInstantiator != null) {
                clsClass$ = class$java$rmi$activation$ActivationInstantiator;
            } else {
                clsClass$ = class$("java.rmi.activation.ActivationInstantiator");
                class$java$rmi$activation$ActivationInstantiator = clsClass$;
            }
            Class<?>[] clsArr = new Class[2];
            if (class$java$rmi$activation$ActivationID != null) {
                clsClass$2 = class$java$rmi$activation$ActivationID;
            } else {
                clsClass$2 = class$("java.rmi.activation.ActivationID");
                class$java$rmi$activation$ActivationID = clsClass$2;
            }
            clsArr[0] = clsClass$2;
            if (class$java$rmi$activation$ActivationDesc != null) {
                clsClass$3 = class$java$rmi$activation$ActivationDesc;
            } else {
                clsClass$3 = class$("java.rmi.activation.ActivationDesc");
                class$java$rmi$activation$ActivationDesc = clsClass$3;
            }
            clsArr[1] = clsClass$3;
            $method_newInstance_0 = clsClass$.getMethod("newInstance", clsArr);
        } catch (NoSuchMethodException unused) {
            throw new NoSuchMethodError("stub class initialization failed");
        }
    }

    public ActivationGroup_Stub(RemoteRef remoteRef) {
        super(remoteRef);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError(e2.getMessage());
        }
    }

    @Override // java.rmi.activation.ActivationInstantiator
    public MarshalledObject newInstance(ActivationID activationID, ActivationDesc activationDesc) throws ActivationException, RemoteException {
        try {
            return (MarshalledObject) this.ref.invoke(this, $method_newInstance_0, new Object[]{activationID, activationDesc}, -5274445189091581345L);
        } catch (RuntimeException e2) {
            throw e2;
        } catch (RemoteException e3) {
            throw e3;
        } catch (ActivationException e4) {
            throw e4;
        } catch (Exception e5) {
            throw new UnexpectedException("undeclared checked exception", e5);
        }
    }
}
