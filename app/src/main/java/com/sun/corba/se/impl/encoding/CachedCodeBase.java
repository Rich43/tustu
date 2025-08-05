package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.org.omg.CORBA.Repository;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import com.sun.org.omg.SendingContext.CodeBase;
import com.sun.org.omg.SendingContext.CodeBaseHelper;
import com.sun.org.omg.SendingContext._CodeBaseImplBase;
import java.util.Hashtable;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CachedCodeBase.class */
public class CachedCodeBase extends _CodeBaseImplBase {
    private Hashtable implementations;
    private Hashtable fvds;
    private Hashtable bases;
    private volatile CodeBase delegate;
    private CorbaConnection conn;
    private static Object iorMapLock = new Object();
    private static Hashtable<IOR, CodeBase> iorMap = new Hashtable<>();

    public static synchronized void cleanCache(ORB orb) {
        synchronized (iorMapLock) {
            for (IOR ior : iorMap.keySet()) {
                if (ior.getORB() == orb) {
                    iorMap.remove(ior);
                }
            }
        }
    }

    public CachedCodeBase(CorbaConnection corbaConnection) {
        this.conn = corbaConnection;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public Repository get_ir() {
        return null;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public synchronized String implementation(String str) {
        String strImplementation = null;
        if (this.implementations == null) {
            this.implementations = new Hashtable();
        } else {
            strImplementation = (String) this.implementations.get(str);
        }
        if (strImplementation == null && connectedCodeBase()) {
            strImplementation = this.delegate.implementation(str);
            if (strImplementation != null) {
                this.implementations.put(str, strImplementation);
            }
        }
        return strImplementation;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public synchronized String[] implementations(String[] strArr) {
        String[] strArr2 = new String[strArr.length];
        for (int i2 = 0; i2 < strArr2.length; i2++) {
            strArr2[i2] = implementation(strArr[i2]);
        }
        return strArr2;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public synchronized FullValueDescription meta(String str) {
        FullValueDescription fullValueDescriptionMeta = null;
        if (this.fvds == null) {
            this.fvds = new Hashtable();
        } else {
            fullValueDescriptionMeta = (FullValueDescription) this.fvds.get(str);
        }
        if (fullValueDescriptionMeta == null && connectedCodeBase()) {
            fullValueDescriptionMeta = this.delegate.meta(str);
            if (fullValueDescriptionMeta != null) {
                this.fvds.put(str, fullValueDescriptionMeta);
            }
        }
        return fullValueDescriptionMeta;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public synchronized FullValueDescription[] metas(String[] strArr) {
        FullValueDescription[] fullValueDescriptionArr = new FullValueDescription[strArr.length];
        for (int i2 = 0; i2 < fullValueDescriptionArr.length; i2++) {
            fullValueDescriptionArr[i2] = meta(strArr[i2]);
        }
        return fullValueDescriptionArr;
    }

    @Override // com.sun.org.omg.SendingContext.CodeBaseOperations
    public synchronized String[] bases(String str) {
        String[] strArrBases = null;
        if (this.bases == null) {
            this.bases = new Hashtable();
        } else {
            strArrBases = (String[]) this.bases.get(str);
        }
        if (strArrBases == null && connectedCodeBase()) {
            strArrBases = this.delegate.bases(str);
            if (strArrBases != null) {
                this.bases.put(str, strArrBases);
            }
        }
        return strArrBases;
    }

    private synchronized boolean connectedCodeBase() {
        if (this.delegate != null) {
            return true;
        }
        if (this.conn.getCodeBaseIOR() == null) {
            if (this.conn.getBroker().transportDebugFlag) {
                this.conn.dprint("CodeBase unavailable on connection: " + ((Object) this.conn));
                return false;
            }
            return false;
        }
        synchronized (iorMapLock) {
            if (this.delegate != null) {
                return true;
            }
            this.delegate = iorMap.get(this.conn.getCodeBaseIOR());
            if (this.delegate != null) {
                return true;
            }
            this.delegate = CodeBaseHelper.narrow(getObjectFromIOR());
            iorMap.put(this.conn.getCodeBaseIOR(), this.delegate);
            return true;
        }
    }

    private final Object getObjectFromIOR() {
        return CDRInputStream_1_0.internalIORToObject(this.conn.getCodeBaseIOR(), null, this.conn.getBroker());
    }
}
