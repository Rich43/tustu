package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORFactory;
import com.sun.corba.se.spi.ior.IORTemplateList;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectReferenceProducerBase.class */
public abstract class ObjectReferenceProducerBase {
    protected transient ORB orb;

    public abstract IORFactory getIORFactory();

    public abstract IORTemplateList getIORTemplateList();

    public ObjectReferenceProducerBase(ORB orb) {
        this.orb = orb;
    }

    public Object make_object(String str, byte[] bArr) {
        return ORBUtility.makeObjectReference(getIORFactory().makeIOR(this.orb, str, IORFactories.makeObjectId(bArr)));
    }
}
