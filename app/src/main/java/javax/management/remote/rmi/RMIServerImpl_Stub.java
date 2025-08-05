package javax.management.remote.rmi;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import java.rmi.server.RemoteRef;
import java.rmi.server.RemoteStub;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIServerImpl_Stub.class */
public final class RMIServerImpl_Stub extends RemoteStub implements RMIServer {
    private static final long serialVersionUID = 2;
    private static Method $method_getVersion_0;
    private static Method $method_newClient_1;
    static Class class$javax$management$remote$rmi$RMIServer;
    static Class class$java$lang$Object;

    static {
        Class clsClass$;
        Class clsClass$2;
        Class<?> clsClass$3;
        try {
            if (class$javax$management$remote$rmi$RMIServer != null) {
                clsClass$ = class$javax$management$remote$rmi$RMIServer;
            } else {
                clsClass$ = class$("javax.management.remote.rmi.RMIServer");
                class$javax$management$remote$rmi$RMIServer = clsClass$;
            }
            $method_getVersion_0 = clsClass$.getMethod("getVersion", new Class[0]);
            if (class$javax$management$remote$rmi$RMIServer != null) {
                clsClass$2 = class$javax$management$remote$rmi$RMIServer;
            } else {
                clsClass$2 = class$("javax.management.remote.rmi.RMIServer");
                class$javax$management$remote$rmi$RMIServer = clsClass$2;
            }
            Class<?>[] clsArr = new Class[1];
            if (class$java$lang$Object != null) {
                clsClass$3 = class$java$lang$Object;
            } else {
                clsClass$3 = class$(Constants.OBJECT_CLASS);
                class$java$lang$Object = clsClass$3;
            }
            clsArr[0] = clsClass$3;
            $method_newClient_1 = clsClass$2.getMethod("newClient", clsArr);
        } catch (NoSuchMethodException unused) {
            throw new NoSuchMethodError("stub class initialization failed");
        }
    }

    public RMIServerImpl_Stub(RemoteRef remoteRef) {
        super(remoteRef);
    }

    static Class class$(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError(e2.getMessage());
        }
    }

    @Override // javax.management.remote.rmi.RMIServer
    public String getVersion() throws RemoteException {
        try {
            return (String) this.ref.invoke(this, $method_getVersion_0, null, -8081107751519807347L);
        } catch (RuntimeException e2) {
            throw e2;
        } catch (RemoteException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }

    @Override // javax.management.remote.rmi.RMIServer
    public RMIConnection newClient(Object obj) throws IOException {
        try {
            return (RMIConnection) this.ref.invoke(this, $method_newClient_1, new Object[]{obj}, -1089742558549201240L);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (Exception e4) {
            throw new UnexpectedException("undeclared checked exception", e4);
        }
    }
}
