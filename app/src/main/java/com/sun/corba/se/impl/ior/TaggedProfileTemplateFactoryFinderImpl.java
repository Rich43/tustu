package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.Identifiable;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/TaggedProfileTemplateFactoryFinderImpl.class */
public class TaggedProfileTemplateFactoryFinderImpl extends IdentifiableFactoryFinderBase {
    public TaggedProfileTemplateFactoryFinderImpl(ORB orb) {
        super(orb);
    }

    @Override // com.sun.corba.se.impl.ior.IdentifiableFactoryFinderBase
    public Identifiable handleMissingFactory(int i2, InputStream inputStream) {
        throw this.wrapper.taggedProfileTemplateFactoryNotFound(new Integer(i2));
    }
}
