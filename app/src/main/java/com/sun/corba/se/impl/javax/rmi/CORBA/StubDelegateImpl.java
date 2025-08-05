package com.sun.corba.se.impl.javax.rmi.CORBA;

import com.sun.corba.se.impl.ior.StubIORImpl;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.impl.presentation.rmi.StubConnectImpl;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import javax.rmi.CORBA.Stub;
import javax.rmi.CORBA.StubDelegate;
import org.omg.CORBA.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/javax/rmi/CORBA/StubDelegateImpl.class */
public class StubDelegateImpl implements StubDelegate {
    static UtilSystemException wrapper = UtilSystemException.get(CORBALogDomains.RMIIIOP);
    private StubIORImpl ior = null;

    public StubIORImpl getIOR() {
        return this.ior;
    }

    private void init(Stub stub) {
        if (this.ior == null) {
            this.ior = new StubIORImpl(stub);
        }
    }

    @Override // javax.rmi.CORBA.StubDelegate
    public int hashCode(Stub stub) {
        init(stub);
        return this.ior.hashCode();
    }

    @Override // javax.rmi.CORBA.StubDelegate
    public boolean equals(Stub stub, Object obj) {
        if (stub == obj) {
            return true;
        }
        if (!(obj instanceof Stub)) {
            return false;
        }
        Stub stub2 = (Stub) obj;
        if (stub2.hashCode() != stub.hashCode()) {
            return false;
        }
        return stub.toString().equals(stub2.toString());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StubDelegateImpl)) {
            return false;
        }
        StubDelegateImpl stubDelegateImpl = (StubDelegateImpl) obj;
        if (this.ior == null) {
            return this.ior == stubDelegateImpl.ior;
        }
        return this.ior.equals(stubDelegateImpl.ior);
    }

    public int hashCode() {
        if (this.ior == null) {
            return 0;
        }
        return this.ior.hashCode();
    }

    @Override // javax.rmi.CORBA.StubDelegate
    public String toString(Stub stub) {
        if (this.ior == null) {
            return null;
        }
        return this.ior.toString();
    }

    @Override // javax.rmi.CORBA.StubDelegate
    public void connect(Stub stub, ORB orb) throws RemoteException {
        this.ior = StubConnectImpl.connect(this.ior, stub, stub, orb);
    }

    @Override // javax.rmi.CORBA.StubDelegate
    public void readObject(Stub stub, ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        if (this.ior == null) {
            this.ior = new StubIORImpl();
        }
        this.ior.doRead(objectInputStream);
    }

    @Override // javax.rmi.CORBA.StubDelegate
    public void writeObject(Stub stub, ObjectOutputStream objectOutputStream) throws IOException {
        init(stub);
        this.ior.doWrite(objectOutputStream);
    }
}
