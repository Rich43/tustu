package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactory;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.IdentifiableContainerBase;
import com.sun.corba.se.spi.ior.IdentifiableFactoryFinder;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/IORTemplateImpl.class */
public class IORTemplateImpl extends IdentifiableContainerBase implements IORTemplate {
    private ObjectKeyTemplate oktemp;

    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof IORTemplateImpl)) {
            return super.equals(obj) && this.oktemp.equals(((IORTemplateImpl) obj).getObjectKeyTemplate());
        }
        return false;
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        return super.hashCode() ^ this.oktemp.hashCode();
    }

    @Override // com.sun.corba.se.spi.ior.IORTemplate
    public ObjectKeyTemplate getObjectKeyTemplate() {
        return this.oktemp;
    }

    public IORTemplateImpl(ObjectKeyTemplate objectKeyTemplate) {
        this.oktemp = objectKeyTemplate;
    }

    @Override // com.sun.corba.se.spi.ior.IORFactory
    public IOR makeIOR(ORB orb, String str, ObjectId objectId) {
        return new IORImpl(orb, str, this, objectId);
    }

    @Override // com.sun.corba.se.spi.ior.IORFactory
    public boolean isEquivalent(IORFactory iORFactory) {
        if (!(iORFactory instanceof IORTemplate)) {
            return false;
        }
        IORTemplate iORTemplate = (IORTemplate) iORFactory;
        Iterator it = iterator();
        Iterator it2 = iORTemplate.iterator();
        while (it.hasNext() && it2.hasNext()) {
            if (!((TaggedProfileTemplate) it.next()).isEquivalent((TaggedProfileTemplate) it2.next())) {
                return false;
            }
        }
        return it.hasNext() == it2.hasNext() && getObjectKeyTemplate().equals(iORTemplate.getObjectKeyTemplate());
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, com.sun.corba.se.spi.ior.MakeImmutable
    public void makeImmutable() {
        makeElementsImmutable();
        super.makeImmutable();
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        this.oktemp.write(outputStream);
        EncapsulationUtility.writeIdentifiableSequence(this, outputStream);
    }

    public IORTemplateImpl(InputStream inputStream) {
        ORB orb = (ORB) inputStream.orb();
        IdentifiableFactoryFinder taggedProfileTemplateFactoryFinder = orb.getTaggedProfileTemplateFactoryFinder();
        this.oktemp = orb.getObjectKeyFactory().createTemplate(inputStream);
        EncapsulationUtility.readIdentifiableSequence(this, taggedProfileTemplateFactoryFinder, inputStream);
        makeImmutable();
    }
}
