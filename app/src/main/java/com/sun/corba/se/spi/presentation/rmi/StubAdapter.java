package com.sun.corba.se.spi.presentation.rmi;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.oa.poa.POAManagerImpl;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.rmi.RemoteException;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.Tie;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.OutputStream;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAManager;
import org.omg.PortableServer.POAPackage.ServantNotActive;
import org.omg.PortableServer.POAPackage.WrongPolicy;
import org.omg.PortableServer.Servant;

/* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/StubAdapter.class */
public abstract class StubAdapter {
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PRESENTATION);

    private StubAdapter() {
    }

    public static boolean isStubClass(Class cls) {
        return ObjectImpl.class.isAssignableFrom(cls) || DynamicStub.class.isAssignableFrom(cls);
    }

    public static boolean isStub(Object obj) {
        return (obj instanceof DynamicStub) || (obj instanceof ObjectImpl);
    }

    public static void setDelegate(Object obj, Delegate delegate) {
        if (obj instanceof DynamicStub) {
            ((DynamicStub) obj).setDelegate(delegate);
        } else {
            if (obj instanceof ObjectImpl) {
                ((ObjectImpl) obj)._set_delegate(delegate);
                return;
            }
            throw wrapper.setDelegateRequiresStub();
        }
    }

    public static Object activateServant(Servant servant) {
        POA poa_default_POA = servant._default_POA();
        try {
            Object objectServant_to_reference = poa_default_POA.servant_to_reference(servant);
            POAManager pOAManagerThe_POAManager = poa_default_POA.the_POAManager();
            if (pOAManagerThe_POAManager instanceof POAManagerImpl) {
                ((POAManagerImpl) pOAManagerThe_POAManager).implicitActivation();
            }
            return objectServant_to_reference;
        } catch (ServantNotActive e2) {
            throw wrapper.getDelegateServantNotActive(e2);
        } catch (WrongPolicy e3) {
            throw wrapper.getDelegateWrongPolicy(e3);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static Object activateTie(Tie tie) {
        if (tie instanceof ObjectImpl) {
            return tie.thisObject();
        }
        if (tie instanceof Servant) {
            return activateServant((Servant) tie);
        }
        throw wrapper.badActivateTieCall();
    }

    public static Delegate getDelegate(Object obj) {
        if (obj instanceof DynamicStub) {
            return ((DynamicStub) obj).getDelegate();
        }
        if (obj instanceof ObjectImpl) {
            return ((ObjectImpl) obj)._get_delegate();
        }
        if (obj instanceof Tie) {
            return getDelegate(activateTie((Tie) obj));
        }
        throw wrapper.getDelegateRequiresStub();
    }

    public static ORB getORB(Object obj) {
        if (obj instanceof DynamicStub) {
            return ((DynamicStub) obj).getORB();
        }
        if (obj instanceof ObjectImpl) {
            return ((ObjectImpl) obj)._orb();
        }
        throw wrapper.getOrbRequiresStub();
    }

    public static String[] getTypeIds(Object obj) {
        if (obj instanceof DynamicStub) {
            return ((DynamicStub) obj).getTypeIds();
        }
        if (obj instanceof ObjectImpl) {
            return ((ObjectImpl) obj)._ids();
        }
        throw wrapper.getTypeIdsRequiresStub();
    }

    public static void connect(Object obj, ORB orb) throws RemoteException {
        if (obj instanceof DynamicStub) {
            ((DynamicStub) obj).connect((com.sun.corba.se.spi.orb.ORB) orb);
        } else if (obj instanceof Stub) {
            ((Stub) obj).connect(orb);
        } else {
            if (obj instanceof ObjectImpl) {
                orb.connect((Object) obj);
                return;
            }
            throw wrapper.connectRequiresStub();
        }
    }

    public static boolean isLocal(Object obj) {
        if (obj instanceof DynamicStub) {
            return ((DynamicStub) obj).isLocal();
        }
        if (obj instanceof ObjectImpl) {
            return ((ObjectImpl) obj)._is_local();
        }
        throw wrapper.isLocalRequiresStub();
    }

    public static OutputStream request(Object obj, String str, boolean z2) {
        if (obj instanceof DynamicStub) {
            return ((DynamicStub) obj).request(str, z2);
        }
        if (obj instanceof ObjectImpl) {
            return ((ObjectImpl) obj)._request(str, z2);
        }
        throw wrapper.requestRequiresStub();
    }
}
