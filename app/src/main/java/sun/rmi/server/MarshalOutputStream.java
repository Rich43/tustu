package sun.rmi.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.RemoteStub;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.rmi.transport.ObjectTable;
import sun.rmi.transport.Target;

/* loaded from: rt.jar:sun/rmi/server/MarshalOutputStream.class */
public class MarshalOutputStream extends ObjectOutputStream {
    public MarshalOutputStream(OutputStream outputStream) throws IOException {
        this(outputStream, 1);
    }

    public MarshalOutputStream(OutputStream outputStream, int i2) throws IOException {
        super(outputStream);
        useProtocolVersion(i2);
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.server.MarshalOutputStream.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                MarshalOutputStream.this.enableReplaceObject(true);
                return null;
            }
        });
    }

    @Override // java.io.ObjectOutputStream
    protected final Object replaceObject(Object obj) throws IOException {
        Target target;
        if ((obj instanceof Remote) && !(obj instanceof RemoteStub) && (target = ObjectTable.getTarget((Remote) obj)) != null) {
            return target.getStub();
        }
        return obj;
    }

    @Override // java.io.ObjectOutputStream
    protected void annotateClass(Class<?> cls) throws IOException {
        writeLocation(RMIClassLoader.getClassAnnotation(cls));
    }

    @Override // java.io.ObjectOutputStream
    protected void annotateProxyClass(Class<?> cls) throws IOException {
        annotateClass(cls);
    }

    protected void writeLocation(String str) throws IOException {
        writeObject(str);
    }
}
