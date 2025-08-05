package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.TaggedComponent;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/GenericTaggedComponent.class */
public class GenericTaggedComponent extends GenericIdentifiable implements TaggedComponent {
    public GenericTaggedComponent(int i2, InputStream inputStream) {
        super(i2, inputStream);
    }

    public GenericTaggedComponent(int i2, byte[] bArr) {
        super(i2, bArr);
    }

    @Override // com.sun.corba.se.spi.ior.TaggedComponent
    public org.omg.IOP.TaggedComponent getIOPComponent(ORB orb) {
        return new org.omg.IOP.TaggedComponent(getId(), getData());
    }
}
