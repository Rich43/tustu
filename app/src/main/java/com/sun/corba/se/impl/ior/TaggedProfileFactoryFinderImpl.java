package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.Identifiable;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/TaggedProfileFactoryFinderImpl.class */
public class TaggedProfileFactoryFinderImpl extends IdentifiableFactoryFinderBase {
    public TaggedProfileFactoryFinderImpl(ORB orb) {
        super(orb);
    }

    @Override // com.sun.corba.se.impl.ior.IdentifiableFactoryFinderBase
    public Identifiable handleMissingFactory(int i2, InputStream inputStream) {
        return new GenericTaggedProfile(i2, inputStream);
    }
}
