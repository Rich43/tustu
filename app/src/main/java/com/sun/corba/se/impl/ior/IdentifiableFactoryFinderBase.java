package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.logging.IORSystemException;
import com.sun.corba.se.spi.ior.Identifiable;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/IdentifiableFactoryFinderBase.class */
public abstract class IdentifiableFactoryFinderBase implements IdentifiableFactoryFinder {
    private ORB orb;
    private Map map = new HashMap();
    protected IORSystemException wrapper;

    public abstract Identifiable handleMissingFactory(int i2, InputStream inputStream);

    protected IdentifiableFactoryFinderBase(ORB orb) {
        this.orb = orb;
        this.wrapper = IORSystemException.get(orb, CORBALogDomains.OA_IOR);
    }

    protected IdentifiableFactory getFactory(int i2) {
        return (IdentifiableFactory) this.map.get(new Integer(i2));
    }

    @Override // com.sun.corba.se.spi.ior.IdentifiableFactoryFinder
    public Identifiable create(int i2, InputStream inputStream) {
        IdentifiableFactory factory = getFactory(i2);
        if (factory != null) {
            return factory.create(inputStream);
        }
        return handleMissingFactory(i2, inputStream);
    }

    @Override // com.sun.corba.se.spi.ior.IdentifiableFactoryFinder
    public void registerFactory(IdentifiableFactory identifiableFactory) {
        this.map.put(new Integer(identifiableFactory.getId()), identifiableFactory);
    }
}
