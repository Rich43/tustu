package com.sun.corba.se.impl.copyobject;

import com.sun.corba.se.impl.orbutil.DenseIntMapImpl;
import com.sun.corba.se.spi.copyobject.CopierManager;
import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/copyobject/CopierManagerImpl.class */
public class CopierManagerImpl implements CopierManager {
    private int defaultId = 0;
    private DenseIntMapImpl map = new DenseIntMapImpl();
    private ORB orb;

    public CopierManagerImpl(ORB orb) {
        this.orb = orb;
    }

    @Override // com.sun.corba.se.spi.copyobject.CopierManager
    public void setDefaultId(int i2) {
        this.defaultId = i2;
    }

    @Override // com.sun.corba.se.spi.copyobject.CopierManager
    public int getDefaultId() {
        return this.defaultId;
    }

    @Override // com.sun.corba.se.spi.copyobject.CopierManager
    public ObjectCopierFactory getObjectCopierFactory(int i2) {
        return (ObjectCopierFactory) this.map.get(i2);
    }

    @Override // com.sun.corba.se.spi.copyobject.CopierManager
    public ObjectCopierFactory getDefaultObjectCopierFactory() {
        return (ObjectCopierFactory) this.map.get(this.defaultId);
    }

    @Override // com.sun.corba.se.spi.copyobject.CopierManager
    public void registerObjectCopierFactory(ObjectCopierFactory objectCopierFactory, int i2) {
        this.map.set(i2, objectCopierFactory);
    }
}
