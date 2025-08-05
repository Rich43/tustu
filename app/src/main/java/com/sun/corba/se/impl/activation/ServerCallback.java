package com.sun.corba.se.impl.activation;

import com.sun.corba.se.spi.activation._ServerImplBase;
import java.lang.reflect.Method;
import org.omg.CORBA.ORB;

/* compiled from: ServerMain.java */
/* loaded from: rt.jar:com/sun/corba/se/impl/activation/ServerCallback.class */
class ServerCallback extends _ServerImplBase {
    private ORB orb;
    private transient Method installMethod;
    private transient Method uninstallMethod;
    private transient Method shutdownMethod;
    private Object[] methodArgs;

    ServerCallback(ORB orb, Method method, Method method2, Method method3) {
        this.orb = orb;
        this.installMethod = method;
        this.uninstallMethod = method2;
        this.shutdownMethod = method3;
        orb.connect(this);
        this.methodArgs = new Object[]{orb};
    }

    private void invokeMethod(Method method) {
        if (method != null) {
            try {
                method.invoke(null, this.methodArgs);
            } catch (Exception e2) {
                ServerMain.logError("could not invoke " + method.getName() + " method: " + e2.getMessage());
            }
        }
    }

    @Override // com.sun.corba.se.spi.activation.ServerOperations
    public void shutdown() {
        ServerMain.logInformation("Shutdown starting");
        invokeMethod(this.shutdownMethod);
        this.orb.shutdown(true);
        ServerMain.logTerminal("Shutdown completed", 0);
    }

    @Override // com.sun.corba.se.spi.activation.ServerOperations
    public void install() {
        ServerMain.logInformation("Install starting");
        invokeMethod(this.installMethod);
        ServerMain.logInformation("Install completed");
    }

    @Override // com.sun.corba.se.spi.activation.ServerOperations
    public void uninstall() {
        ServerMain.logInformation("uninstall starting");
        invokeMethod(this.uninstallMethod);
        ServerMain.logInformation("uninstall completed");
    }
}
