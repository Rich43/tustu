package com.sun.corba.se.spi.ior;

import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import java.util.List;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/ior/TaggedProfileTemplate.class */
public interface TaggedProfileTemplate extends List, Identifiable, WriteContents, MakeImmutable {
    Iterator iteratorById(int i2);

    TaggedProfile create(ObjectKeyTemplate objectKeyTemplate, ObjectId objectId);

    void write(ObjectKeyTemplate objectKeyTemplate, ObjectId objectId, OutputStream outputStream);

    boolean isEquivalent(TaggedProfileTemplate taggedProfileTemplate);

    org.omg.IOP.TaggedComponent[] getIOPComponents(ORB orb, int i2);
}
