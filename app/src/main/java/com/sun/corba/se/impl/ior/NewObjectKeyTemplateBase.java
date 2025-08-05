package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/NewObjectKeyTemplateBase.class */
public abstract class NewObjectKeyTemplateBase extends ObjectKeyTemplateBase {
    public NewObjectKeyTemplateBase(ORB orb, int i2, int i3, int i4, String str, ObjectAdapterId objectAdapterId) {
        super(orb, i2, i3, i4, str, objectAdapterId);
        if (i2 != -1347695872) {
            throw this.wrapper.badMagic(new Integer(i2));
        }
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase, com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public void write(ObjectId objectId, OutputStream outputStream) {
        super.write(objectId, outputStream);
        getORBVersion().write(outputStream);
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase, com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        super.write(outputStream);
        getORBVersion().write(outputStream);
    }

    protected void setORBVersion(InputStream inputStream) {
        setORBVersion(ORBVersionFactory.create(inputStream));
    }
}
