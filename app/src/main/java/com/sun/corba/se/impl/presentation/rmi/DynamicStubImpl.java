package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.ior.StubIORImpl;
import com.sun.corba.se.impl.util.JDKBridge;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.spi.presentation.rmi.DynamicStub;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA_2_3.portable.ObjectImpl;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/DynamicStubImpl.class */
public class DynamicStubImpl extends ObjectImpl implements DynamicStub, Serializable {
    private static final long serialVersionUID = 4852612040012087675L;
    private String[] typeIds;
    private DynamicStub self = null;
    private StubIORImpl ior = null;

    public void setSelf(DynamicStub dynamicStub) {
        this.self = dynamicStub;
    }

    public DynamicStub getSelf() {
        return this.self;
    }

    public DynamicStubImpl(String[] strArr) {
        this.typeIds = strArr;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicStub
    public void setDelegate(Delegate delegate) {
        _set_delegate(delegate);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicStub
    public Delegate getDelegate() {
        return _get_delegate();
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicStub
    public ORB getORB() {
        return _orb();
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return this.typeIds;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicStub
    public String[] getTypeIds() {
        return _ids();
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicStub
    public void connect(ORB orb) throws RemoteException {
        this.ior = StubConnectImpl.connect(this.ior, this.self, this, orb);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicStub
    public boolean isLocal() {
        return _is_local();
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicStub
    public OutputStream request(String str, boolean z2) {
        return _request(str, z2);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.ior = new StubIORImpl();
        this.ior.doRead(objectInputStream);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (this.ior == null) {
            this.ior = new StubIORImpl(this);
        }
        this.ior.doWrite(objectOutputStream);
    }

    public Object readResolve() {
        Class clsLoadClass = null;
        try {
            clsLoadClass = JDKBridge.loadClass(RepositoryId.cache.getId(this.ior.getRepositoryId()).getClassName(), null, null);
        } catch (ClassNotFoundException e2) {
        }
        return ((InvocationHandlerFactoryImpl) com.sun.corba.se.spi.orb.ORB.getPresentationManager().getClassData(clsLoadClass).getInvocationHandlerFactory()).getInvocationHandler(this);
    }
}
