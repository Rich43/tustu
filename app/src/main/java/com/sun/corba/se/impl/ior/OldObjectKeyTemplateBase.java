package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/OldObjectKeyTemplateBase.class */
public abstract class OldObjectKeyTemplateBase extends ObjectKeyTemplateBase {
    public OldObjectKeyTemplateBase(ORB orb, int i2, int i3, int i4, String str, ObjectAdapterId objectAdapterId) {
        super(orb, i2, i3, i4, str, objectAdapterId);
        if (i2 == -1347695874) {
            setORBVersion(ORBVersionFactory.getOLD());
        } else {
            if (i2 == -1347695873) {
                setORBVersion(ORBVersionFactory.getNEW());
                return;
            }
            throw this.wrapper.badMagic(new Integer(i2));
        }
    }
}
