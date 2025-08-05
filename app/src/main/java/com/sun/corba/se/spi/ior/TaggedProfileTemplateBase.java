package com.sun.corba.se.spi.ior;

import com.sun.corba.se.impl.ior.EncapsulationUtility;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/TaggedProfileTemplateBase.class */
public abstract class TaggedProfileTemplateBase extends IdentifiableContainerBase implements TaggedProfileTemplate {
    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        EncapsulationUtility.writeEncapsulation(this, outputStream);
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfileTemplate
    public org.omg.IOP.TaggedComponent[] getIOPComponents(ORB orb, int i2) {
        int i3 = 0;
        Iterator itIteratorById = iteratorById(i2);
        while (itIteratorById.hasNext()) {
            itIteratorById.next();
            i3++;
        }
        org.omg.IOP.TaggedComponent[] taggedComponentArr = new org.omg.IOP.TaggedComponent[i3];
        int i4 = 0;
        Iterator itIteratorById2 = iteratorById(i2);
        while (itIteratorById2.hasNext()) {
            int i5 = i4;
            i4++;
            taggedComponentArr[i5] = ((TaggedComponent) itIteratorById2.next()).getIOPComponent(orb);
        }
        return taggedComponentArr;
    }
}
