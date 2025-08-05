package org.omg.PortableServer;

import java.util.Hashtable;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ResponseHandler;

/* loaded from: rt.jar:org/omg/PortableServer/ServantLocatorPOA.class */
public abstract class ServantLocatorPOA extends Servant implements ServantLocatorOperations, InvokeHandler {
    private static Hashtable _methods = new Hashtable();
    private static String[] __ids;

    static {
        _methods.put("preinvoke", new Integer(0));
        _methods.put("postinvoke", new Integer(1));
        __ids = new String[]{"IDL:omg.org/PortableServer/ServantLocator:1.0", "IDL:omg.org/PortableServer/ServantManager:1.0"};
    }

    @Override // org.omg.CORBA.portable.InvokeHandler
    public OutputStream _invoke(String str, InputStream inputStream, ResponseHandler responseHandler) {
        throw new BAD_OPERATION();
    }

    @Override // org.omg.PortableServer.Servant
    public String[] _all_interfaces(POA poa, byte[] bArr) {
        return (String[]) __ids.clone();
    }

    public ServantLocator _this() {
        return ServantLocatorHelper.narrow(super._this_object());
    }

    public ServantLocator _this(ORB orb) {
        return ServantLocatorHelper.narrow(super._this_object(orb));
    }
}
