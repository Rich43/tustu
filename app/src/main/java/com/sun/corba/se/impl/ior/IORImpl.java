package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.logging.IORSystemException;
import com.sun.corba.se.impl.orbutil.HexOutputStream;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.IORTemplateList;
import com.sun.corba.se.spi.ior.IdentifiableContainerBase;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.ior.TaggedProfile;
import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import org.omg.IOP.IORHelper;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/IORImpl.class */
public class IORImpl extends IdentifiableContainerBase implements IOR {
    private String typeId;
    private ORB factory;
    private boolean isCachedHashValue;
    private int cachedHashValue;
    IORSystemException wrapper;
    private IORTemplateList iortemps;

    @Override // com.sun.corba.se.spi.ior.IOR
    public ORB getORB() {
        return this.factory;
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof IOR)) {
            return super.equals(obj) && this.typeId.equals(((IOR) obj).getTypeId());
        }
        return false;
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.Collection, java.util.List
    public synchronized int hashCode() {
        if (!this.isCachedHashValue) {
            this.cachedHashValue = super.hashCode() ^ this.typeId.hashCode();
            this.isCachedHashValue = true;
        }
        return this.cachedHashValue;
    }

    public IORImpl(ORB orb) {
        this(orb, "");
    }

    public IORImpl(ORB orb, String str) {
        this.factory = null;
        this.isCachedHashValue = false;
        this.iortemps = null;
        this.factory = orb;
        this.wrapper = IORSystemException.get(orb, CORBALogDomains.OA_IOR);
        this.typeId = str;
    }

    public IORImpl(ORB orb, String str, IORTemplate iORTemplate, ObjectId objectId) {
        this(orb, str);
        this.iortemps = IORFactories.makeIORTemplateList();
        this.iortemps.add(iORTemplate);
        addTaggedProfiles(iORTemplate, objectId);
        makeImmutable();
    }

    private void addTaggedProfiles(IORTemplate iORTemplate, ObjectId objectId) {
        ObjectKeyTemplate objectKeyTemplate = iORTemplate.getObjectKeyTemplate();
        Iterator it = iORTemplate.iterator();
        while (it.hasNext()) {
            add(((TaggedProfileTemplate) it.next()).create(objectKeyTemplate, objectId));
        }
    }

    public IORImpl(ORB orb, String str, IORTemplateList iORTemplateList, ObjectId objectId) {
        this(orb, str);
        this.iortemps = iORTemplateList;
        Iterator it = iORTemplateList.iterator();
        while (it.hasNext()) {
            addTaggedProfiles((IORTemplate) it.next(), objectId);
        }
        makeImmutable();
    }

    public IORImpl(InputStream inputStream) {
        this((ORB) inputStream.orb(), inputStream.read_string());
        EncapsulationUtility.readIdentifiableSequence(this, this.factory.getTaggedProfileFactoryFinder(), inputStream);
        makeImmutable();
    }

    @Override // com.sun.corba.se.spi.ior.IOR
    public String getTypeId() {
        return this.typeId;
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        outputStream.write_string(this.typeId);
        EncapsulationUtility.writeIdentifiableSequence(this, outputStream);
    }

    @Override // com.sun.corba.se.spi.ior.IOR
    public String stringify() {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.factory);
        encapsOutputStreamNewEncapsOutputStream.putEndian();
        write(encapsOutputStreamNewEncapsOutputStream);
        StringWriter stringWriter = new StringWriter();
        try {
            encapsOutputStreamNewEncapsOutputStream.writeTo(new HexOutputStream(stringWriter));
            return ORBConstants.STRINGIFY_PREFIX + ((Object) stringWriter);
        } catch (IOException e2) {
            throw this.wrapper.stringifyWriteError(e2);
        }
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, com.sun.corba.se.spi.ior.MakeImmutable
    public synchronized void makeImmutable() {
        makeElementsImmutable();
        if (this.iortemps != null) {
            this.iortemps.makeImmutable();
        }
        super.makeImmutable();
    }

    @Override // com.sun.corba.se.spi.ior.IOR
    public org.omg.IOP.IOR getIOPIOR() {
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream(this.factory);
        write(encapsOutputStreamNewEncapsOutputStream);
        return IORHelper.read((InputStream) encapsOutputStreamNewEncapsOutputStream.create_input_stream());
    }

    @Override // com.sun.corba.se.spi.ior.IOR
    public boolean isNil() {
        return size() == 0;
    }

    @Override // com.sun.corba.se.spi.ior.IOR
    public boolean isEquivalent(IOR ior) {
        Iterator it = iterator();
        Iterator it2 = ior.iterator();
        while (it.hasNext() && it2.hasNext()) {
            if (!((TaggedProfile) it.next()).isEquivalent((TaggedProfile) it2.next())) {
                return false;
            }
        }
        return it.hasNext() == it2.hasNext();
    }

    private void initializeIORTemplateList() {
        HashMap map = new HashMap();
        this.iortemps = IORFactories.makeIORTemplateList();
        Iterator it = iterator();
        ObjectId objectId = null;
        while (it.hasNext()) {
            TaggedProfile taggedProfile = (TaggedProfile) it.next();
            TaggedProfileTemplate taggedProfileTemplate = taggedProfile.getTaggedProfileTemplate();
            ObjectKeyTemplate objectKeyTemplate = taggedProfile.getObjectKeyTemplate();
            if (objectId == null) {
                objectId = taggedProfile.getObjectId();
            } else if (!objectId.equals(taggedProfile.getObjectId())) {
                throw this.wrapper.badOidInIorTemplateList();
            }
            IORTemplate iORTemplateMakeIORTemplate = (IORTemplate) map.get(objectKeyTemplate);
            if (iORTemplateMakeIORTemplate == null) {
                iORTemplateMakeIORTemplate = IORFactories.makeIORTemplate(objectKeyTemplate);
                map.put(objectKeyTemplate, iORTemplateMakeIORTemplate);
                this.iortemps.add(iORTemplateMakeIORTemplate);
            }
            iORTemplateMakeIORTemplate.add(taggedProfileTemplate);
        }
        this.iortemps.makeImmutable();
    }

    @Override // com.sun.corba.se.spi.ior.IOR
    public synchronized IORTemplateList getIORTemplates() {
        if (this.iortemps == null) {
            initializeIORTemplateList();
        }
        return this.iortemps;
    }

    @Override // com.sun.corba.se.spi.ior.IOR
    public IIOPProfile getProfile() {
        IIOPProfile iIOPProfile = null;
        Iterator itIteratorById = iteratorById(0);
        if (itIteratorById.hasNext()) {
            iIOPProfile = (IIOPProfile) itIteratorById.next();
        }
        if (iIOPProfile != null) {
            return iIOPProfile;
        }
        throw this.wrapper.iorMustHaveIiopProfile();
    }
}
