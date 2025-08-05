package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORFactory;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.IORTemplateList;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.orb.ORB;
import java.util.ArrayList;
import java.util.Iterator;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/IORTemplateListImpl.class */
public class IORTemplateListImpl extends FreezableList implements IORTemplateList {
    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.List
    public Object set(int i2, Object obj) {
        if (obj instanceof IORTemplate) {
            return super.set(i2, obj);
        }
        if (obj instanceof IORTemplateList) {
            Object objRemove = remove(i2);
            add(i2, obj);
            return objRemove;
        }
        throw new IllegalArgumentException();
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.List
    public void add(int i2, Object obj) {
        if (obj instanceof IORTemplate) {
            super.add(i2, obj);
        } else {
            if (obj instanceof IORTemplateList) {
                addAll(i2, (IORTemplateList) obj);
                return;
            }
            throw new IllegalArgumentException();
        }
    }

    public IORTemplateListImpl() {
        super(new ArrayList());
    }

    public IORTemplateListImpl(InputStream inputStream) {
        this();
        int i2 = inputStream.read_long();
        for (int i3 = 0; i3 < i2; i3++) {
            add(IORFactories.makeIORTemplate(inputStream));
        }
        makeImmutable();
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, com.sun.corba.se.spi.ior.MakeImmutable
    public void makeImmutable() {
        makeElementsImmutable();
        super.makeImmutable();
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        outputStream.write_long(size());
        Iterator it = iterator();
        while (it.hasNext()) {
            ((IORTemplate) it.next()).write(outputStream);
        }
    }

    @Override // com.sun.corba.se.spi.ior.IORFactory
    public IOR makeIOR(ORB orb, String str, ObjectId objectId) {
        return new IORImpl(orb, str, this, objectId);
    }

    @Override // com.sun.corba.se.spi.ior.IORFactory
    public boolean isEquivalent(IORFactory iORFactory) {
        if (!(iORFactory instanceof IORTemplateList)) {
            return false;
        }
        Iterator it = iterator();
        Iterator it2 = ((IORTemplateList) iORFactory).iterator();
        while (it.hasNext() && it2.hasNext()) {
            if (!((IORTemplate) it.next()).isEquivalent((IORTemplate) it2.next())) {
                return false;
            }
        }
        return it.hasNext() == it2.hasNext();
    }
}
