package com.sun.corba.se.spi.presentation.rmi;

import java.rmi.RemoteException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/DynamicStub.class */
public interface DynamicStub extends Object {
    void setDelegate(Delegate delegate);

    Delegate getDelegate();

    ORB getORB();

    String[] getTypeIds();

    void connect(ORB orb) throws RemoteException;

    boolean isLocal();

    OutputStream request(String str, boolean z2);
}
