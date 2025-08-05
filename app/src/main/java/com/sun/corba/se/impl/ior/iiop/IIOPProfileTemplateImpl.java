package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.impl.encoding.CDROutputStream;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.ior.EncapsulationUtility;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
import com.sun.corba.se.spi.ior.TaggedProfile;
import com.sun.corba.se.spi.ior.TaggedProfileTemplate;
import com.sun.corba.se.spi.ior.TaggedProfileTemplateBase;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/IIOPProfileTemplateImpl.class */
public class IIOPProfileTemplateImpl extends TaggedProfileTemplateBase implements IIOPProfileTemplate {
    private ORB orb;
    private GIOPVersion giopVersion;
    private IIOPAddress primary;

    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (!(obj instanceof IIOPProfileTemplateImpl)) {
            return false;
        }
        IIOPProfileTemplateImpl iIOPProfileTemplateImpl = (IIOPProfileTemplateImpl) obj;
        return super.equals(obj) && this.giopVersion.equals(iIOPProfileTemplateImpl.giopVersion) && this.primary.equals(iIOPProfileTemplateImpl.primary);
    }

    @Override // com.sun.corba.se.impl.ior.FreezableList, java.util.AbstractList, java.util.Collection, java.util.List
    public int hashCode() {
        return (super.hashCode() ^ this.giopVersion.hashCode()) ^ this.primary.hashCode();
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfileTemplate
    public TaggedProfile create(ObjectKeyTemplate objectKeyTemplate, ObjectId objectId) {
        return IIOPFactories.makeIIOPProfile(this.orb, objectKeyTemplate, objectId, this);
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate
    public GIOPVersion getGIOPVersion() {
        return this.giopVersion;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate
    public IIOPAddress getPrimaryAddress() {
        return this.primary;
    }

    public IIOPProfileTemplateImpl(ORB orb, GIOPVersion gIOPVersion, IIOPAddress iIOPAddress) {
        this.orb = orb;
        this.giopVersion = gIOPVersion;
        this.primary = iIOPAddress;
        if (this.giopVersion.getMinor() == 0) {
            makeImmutable();
        }
    }

    public IIOPProfileTemplateImpl(InputStream inputStream) {
        byte b2 = inputStream.read_octet();
        byte b3 = inputStream.read_octet();
        this.giopVersion = GIOPVersion.getInstance(b2, b3);
        this.primary = new IIOPAddressImpl(inputStream);
        this.orb = (ORB) inputStream.orb();
        if (b3 > 0) {
            EncapsulationUtility.readIdentifiableSequence(this, this.orb.getTaggedComponentFactoryFinder(), inputStream);
        }
        makeImmutable();
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfileTemplate
    public void write(ObjectKeyTemplate objectKeyTemplate, ObjectId objectId, OutputStream outputStream) {
        this.giopVersion.write(outputStream);
        this.primary.write(outputStream);
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((ORB) outputStream.orb(), ((CDROutputStream) outputStream).isLittleEndian());
        objectKeyTemplate.write(objectId, encapsOutputStreamNewEncapsOutputStream);
        EncapsulationUtility.writeOutputStream(encapsOutputStreamNewEncapsOutputStream, outputStream);
        if (this.giopVersion.getMinor() > 0) {
            EncapsulationUtility.writeIdentifiableSequence(this, outputStream);
        }
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        this.giopVersion.write(outputStream);
        this.primary.write(outputStream);
        if (this.giopVersion.getMinor() > 0) {
            EncapsulationUtility.writeIdentifiableSequence(this, outputStream);
        }
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return 0;
    }

    @Override // com.sun.corba.se.spi.ior.TaggedProfileTemplate
    public boolean isEquivalent(TaggedProfileTemplate taggedProfileTemplate) {
        if (!(taggedProfileTemplate instanceof IIOPProfileTemplateImpl)) {
            return false;
        }
        return this.primary.equals(((IIOPProfileTemplateImpl) taggedProfileTemplate).primary);
    }
}
