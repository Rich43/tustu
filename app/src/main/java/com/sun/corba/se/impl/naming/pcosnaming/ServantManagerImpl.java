package com.sun.corba.se.impl.naming.pcosnaming;

import com.sun.corba.se.spi.orb.ORB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Hashtable;
import org.omg.CORBA.LocalObject;
import org.omg.PortableServer.ForwardRequest;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantLocator;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/pcosnaming/ServantManagerImpl.class */
public class ServantManagerImpl extends LocalObject implements ServantLocator {
    private static final long serialVersionUID = 4028710359865748280L;
    private ORB orb;
    private NameService theNameService;
    private File logDir;
    private Hashtable contexts = new Hashtable();
    private CounterDB counterDb;
    private int counter;
    private static final String objKeyPrefix = "NC";

    ServantManagerImpl(ORB orb, File file, NameService nameService) {
        this.logDir = file;
        this.orb = orb;
        this.counterDb = new CounterDB(file);
        this.theNameService = nameService;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [org.omg.PortableServer.Servant] */
    @Override // org.omg.PortableServer.ServantLocatorOperations
    public Servant preinvoke(byte[] bArr, POA poa, String str, CookieHolder cookieHolder) throws ForwardRequest {
        String str2 = new String(bArr);
        NamingContextImpl inContext = (Servant) this.contexts.get(str2);
        if (inContext == null) {
            inContext = readInContext(str2);
        }
        return inContext;
    }

    @Override // org.omg.PortableServer.ServantLocatorOperations
    public void postinvoke(byte[] bArr, POA poa, String str, Object obj, Servant servant) {
    }

    public NamingContextImpl readInContext(String str) {
        NamingContextImpl namingContextImpl = (NamingContextImpl) this.contexts.get(str);
        if (namingContextImpl != null) {
            return namingContextImpl;
        }
        File file = new File(this.logDir, str);
        if (file.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
                namingContextImpl = (NamingContextImpl) objectInputStream.readObject();
                namingContextImpl.setORB(this.orb);
                namingContextImpl.setServantManagerImpl(this);
                namingContextImpl.setRootNameService(this.theNameService);
                objectInputStream.close();
            } catch (Exception e2) {
            }
        }
        if (namingContextImpl != null) {
            this.contexts.put(str, namingContextImpl);
        }
        return namingContextImpl;
    }

    public NamingContextImpl addContext(String str, NamingContextImpl namingContextImpl) {
        File file = new File(this.logDir, str);
        if (file.exists()) {
            namingContextImpl = readInContext(str);
        } else {
            try {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(namingContextImpl);
                objectOutputStream.close();
            } catch (Exception e2) {
            }
        }
        try {
            this.contexts.remove(str);
        } catch (Exception e3) {
        }
        this.contexts.put(str, namingContextImpl);
        return namingContextImpl;
    }

    public void updateContext(String str, NamingContextImpl namingContextImpl) {
        File file = new File(this.logDir, str);
        if (file.exists()) {
            file.delete();
            file = new File(this.logDir, str);
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(namingContextImpl);
            objectOutputStream.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static String getRootObjectKey() {
        return "NC0";
    }

    public String getNewObjectKey() {
        return objKeyPrefix + this.counterDb.getNextCounter();
    }
}
