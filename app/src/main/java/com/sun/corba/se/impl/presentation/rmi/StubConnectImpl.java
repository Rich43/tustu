package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.corba.CORBAObjectImpl;
import com.sun.corba.se.impl.ior.StubIORImpl;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.rmi.RemoteException;
import javax.rmi.CORBA.Tie;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.ObjectImpl;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubConnectImpl.class */
public abstract class StubConnectImpl {
    static UtilSystemException wrapper = UtilSystemException.get(CORBALogDomains.RMIIIOP);

    public static StubIORImpl connect(StubIORImpl stubIORImpl, Object object, ObjectImpl objectImpl, ORB orb) throws RemoteException {
        Delegate delegate;
        try {
            try {
            } catch (BAD_OPERATION e2) {
                if (stubIORImpl == null) {
                    Tie andForgetTie = Utility.getAndForgetTie(object);
                    if (andForgetTie == null) {
                        throw wrapper.connectNoTie();
                    }
                    ORB orb2 = orb;
                    try {
                        orb2 = andForgetTie.orb();
                    } catch (BAD_INV_ORDER e3) {
                        andForgetTie.orb(orb);
                    } catch (BAD_OPERATION e4) {
                        andForgetTie.orb(orb);
                    }
                    if (orb2 != orb) {
                        throw wrapper.connectTieWrongOrb();
                    }
                    delegate = StubAdapter.getDelegate(andForgetTie);
                    CORBAObjectImpl cORBAObjectImpl = new CORBAObjectImpl();
                    cORBAObjectImpl._set_delegate(delegate);
                    stubIORImpl = new StubIORImpl(cORBAObjectImpl);
                } else {
                    delegate = stubIORImpl.getDelegate(orb);
                }
                StubAdapter.setDelegate(objectImpl, delegate);
            }
            if (StubAdapter.getDelegate(objectImpl).orb(objectImpl) != orb) {
                throw wrapper.connectWrongOrb();
            }
            return stubIORImpl;
        } catch (SystemException e5) {
            throw new RemoteException("CORBA SystemException", e5);
        }
    }
}
