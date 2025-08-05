package org.omg.PortableServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.ServantObject;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;

/* loaded from: rt.jar:org/omg/PortableServer/_ServantLocatorStub.class */
public class _ServantLocatorStub extends ObjectImpl implements ServantLocator {
    public static final Class _opsClass = ServantLocatorOperations.class;
    private static String[] __ids = {"IDL:omg.org/PortableServer/ServantLocator:1.0", "IDL:omg.org/PortableServer/ServantManager:1.0"};

    @Override // org.omg.PortableServer.ServantLocatorOperations
    public Servant preinvoke(byte[] bArr, POA poa, String str, CookieHolder cookieHolder) throws ForwardRequest {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("preinvoke", _opsClass);
        try {
            Servant servantPreinvoke = ((ServantLocatorOperations) servantObject_servant_preinvoke.servant).preinvoke(bArr, poa, str, cookieHolder);
            _servant_postinvoke(servantObject_servant_preinvoke);
            return servantPreinvoke;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.PortableServer.ServantLocatorOperations
    public void postinvoke(byte[] bArr, POA poa, String str, Object obj, Servant servant) {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("postinvoke", _opsClass);
        try {
            ((ServantLocatorOperations) servantObject_servant_preinvoke.servant).postinvoke(bArr, poa, str, obj, servant);
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
