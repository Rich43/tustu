package org.omg.PortableServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:org/omg/PortableServer/_ServantActivatorStub.class */
public class _ServantActivatorStub extends ObjectImpl implements ServantActivator {
    public static final Class _opsClass = ServantActivatorOperations.class;
    private static String[] __ids = {"IDL:omg.org/PortableServer/ServantActivator:2.3", "IDL:omg.org/PortableServer/ServantManager:1.0"};

    @Override // org.omg.PortableServer.ServantActivatorOperations
    public Servant incarnate(byte[] bArr, POA poa) throws ForwardRequest {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("incarnate", _opsClass);
        try {
            Servant servantIncarnate = ((ServantActivatorOperations) servantObject_servant_preinvoke.servant).incarnate(bArr, poa);
            _servant_postinvoke(servantObject_servant_preinvoke);
            return servantIncarnate;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.PortableServer.ServantActivatorOperations
    public void etherealize(byte[] bArr, POA poa, Servant servant, boolean z2, boolean z3) {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("etherealize", _opsClass);
        try {
            ((ServantActivatorOperations) servantObject_servant_preinvoke.servant).etherealize(bArr, poa, servant, z2, z3);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        String utf = objectInputStream.readUTF();
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            _set_delegate(((ObjectImpl) orbInit.string_to_object(utf))._get_delegate());
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            objectOutputStream.writeUTF(orbInit.object_to_string(this));
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }
}
