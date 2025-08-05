package javax.management.remote.rmi;

import com.sun.jmx.remote.internal.RMIExporter;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.lang.reflect.Method;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.security.auth.Subject;
import sun.reflect.misc.ReflectUtil;
import sun.rmi.server.DeserializationChecker;
import sun.rmi.server.UnicastServerRef;
import sun.rmi.server.UnicastServerRef2;

/* loaded from: rt.jar:javax/management/remote/rmi/RMIJRMPServerImpl.class */
public class RMIJRMPServerImpl extends RMIServerImpl {
    private final ExportedWrapper exportedWrapper;
    private final int port;
    private final RMIClientSocketFactory csf;
    private final RMIServerSocketFactory ssf;
    private final Map<String, ?> env;

    public RMIJRMPServerImpl(int i2, RMIClientSocketFactory rMIClientSocketFactory, RMIServerSocketFactory rMIServerSocketFactory, Map<String, ?> map) throws IOException {
        super(map);
        if (i2 < 0) {
            throw new IllegalArgumentException("Negative port: " + i2);
        }
        this.port = i2;
        this.csf = rMIClientSocketFactory;
        this.ssf = rMIServerSocketFactory;
        this.env = map == null ? Collections.emptyMap() : map;
        String[] strArr = (String[]) this.env.get(EnvHelp.CREDENTIAL_TYPES);
        ArrayList arrayList = null;
        if (strArr != null) {
            arrayList = new ArrayList();
            for (String str : strArr) {
                if (str == null) {
                    throw new IllegalArgumentException("A credential type is null.");
                }
                ReflectUtil.checkPackageAccess(str);
                arrayList.add(str);
            }
        }
        this.exportedWrapper = arrayList != null ? new ExportedWrapper(this, arrayList) : null;
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected void export() throws IOException {
        if (this.exportedWrapper != null) {
            export(this.exportedWrapper);
        } else {
            export(this);
        }
    }

    private void export(Remote remote) throws RemoteException {
        RMIExporter rMIExporter = (RMIExporter) this.env.get(RMIExporter.EXPORTER_ATTRIBUTE);
        boolean zIsServerDaemon = EnvHelp.isServerDaemon(this.env);
        if (zIsServerDaemon && rMIExporter != null) {
            throw new IllegalArgumentException("If jmx.remote.x.daemon is specified as true, com.sun.jmx.remote.rmi.exporter cannot be used to specify an exporter!");
        }
        if (zIsServerDaemon) {
            if (this.csf == null && this.ssf == null) {
                new UnicastServerRef(this.port).exportObject(remote, null, true);
                return;
            } else {
                new UnicastServerRef2(this.port, this.csf, this.ssf).exportObject(remote, null, true);
                return;
            }
        }
        if (rMIExporter != null) {
            rMIExporter.exportObject(remote, this.port, this.csf, this.ssf);
        } else {
            UnicastRemoteObject.exportObject(remote, this.port, this.csf, this.ssf);
        }
    }

    private void unexport(Remote remote, boolean z2) throws NoSuchObjectException {
        RMIExporter rMIExporter = (RMIExporter) this.env.get(RMIExporter.EXPORTER_ATTRIBUTE);
        if (rMIExporter == null) {
            UnicastRemoteObject.unexportObject(remote, z2);
        } else {
            rMIExporter.unexportObject(remote, z2);
        }
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected String getProtocol() {
        return "rmi";
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    public Remote toStub() throws IOException {
        if (this.exportedWrapper != null) {
            return RemoteObject.toStub(this.exportedWrapper);
        }
        return RemoteObject.toStub(this);
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected RMIConnection makeClient(String str, Subject subject) throws IOException {
        if (str == null) {
            throw new NullPointerException("Null connectionId");
        }
        RMIConnectionImpl rMIConnectionImpl = new RMIConnectionImpl(this, str, getDefaultClassLoader(), subject, this.env);
        export(rMIConnectionImpl);
        return rMIConnectionImpl;
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected void closeClient(RMIConnection rMIConnection) throws IOException {
        unexport(rMIConnection, true);
    }

    @Override // javax.management.remote.rmi.RMIServerImpl
    protected void closeServer() throws IOException {
        if (this.exportedWrapper != null) {
            unexport(this.exportedWrapper, true);
        } else {
            unexport(this, true);
        }
    }

    /* loaded from: rt.jar:javax/management/remote/rmi/RMIJRMPServerImpl$ExportedWrapper.class */
    private static class ExportedWrapper implements RMIServer, DeserializationChecker {
        private final RMIServer impl;
        private final List<String> allowedTypes;

        private ExportedWrapper(RMIServer rMIServer, List<String> list) {
            this.impl = rMIServer;
            this.allowedTypes = list;
        }

        @Override // javax.management.remote.rmi.RMIServer
        public String getVersion() throws RemoteException {
            return this.impl.getVersion();
        }

        @Override // javax.management.remote.rmi.RMIServer
        public RMIConnection newClient(Object obj) throws IOException {
            return this.impl.newClient(obj);
        }

        @Override // sun.rmi.server.DeserializationChecker
        public void check(Method method, ObjectStreamClass objectStreamClass, int i2, int i3) {
            String name = objectStreamClass.getName();
            if (!this.allowedTypes.contains(name)) {
                throw new ClassCastException("Unsupported type: " + name);
            }
        }

        @Override // sun.rmi.server.DeserializationChecker
        public void checkProxyClass(Method method, String[] strArr, int i2, int i3) {
            if (strArr != null && strArr.length > 0) {
                for (String str : strArr) {
                    if (!this.allowedTypes.contains(str)) {
                        throw new ClassCastException("Unsupported type: " + str);
                    }
                }
            }
        }
    }
}
