package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORFactory;
import com.sun.corba.se.spi.ior.IORTemplate;
import com.sun.corba.se.spi.ior.IORTemplateList;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.StreamableValue;
import org.omg.PortableInterceptor.ObjectReferenceTemplate;
import org.omg.PortableInterceptor.ObjectReferenceTemplateHelper;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectReferenceTemplateImpl.class */
public class ObjectReferenceTemplateImpl extends ObjectReferenceProducerBase implements ObjectReferenceTemplate, StreamableValue {
    private transient IORTemplate iorTemplate;
    public static final String repositoryId = "IDL:com/sun/corba/se/impl/ior/ObjectReferenceTemplateImpl:1.0";

    public ObjectReferenceTemplateImpl(InputStream inputStream) {
        super((ORB) inputStream.orb());
        _read(inputStream);
    }

    public ObjectReferenceTemplateImpl(ORB orb, IORTemplate iORTemplate) {
        super(orb);
        this.iorTemplate = iORTemplate;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ObjectReferenceTemplateImpl) {
            return this.iorTemplate != null && this.iorTemplate.equals(((ObjectReferenceTemplateImpl) obj).iorTemplate);
        }
        return false;
    }

    public int hashCode() {
        return this.iorTemplate.hashCode();
    }

    @Override // org.omg.CORBA.portable.ValueBase
    public String[] _truncatable_ids() {
        return new String[]{repositoryId};
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ObjectReferenceTemplateHelper.type();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        org.omg.CORBA_2_3.portable.InputStream inputStream2 = (org.omg.CORBA_2_3.portable.InputStream) inputStream;
        this.iorTemplate = IORFactories.makeIORTemplate(inputStream2);
        this.orb = (ORB) inputStream2.orb();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        this.iorTemplate.write((org.omg.CORBA_2_3.portable.OutputStream) outputStream);
    }

    @Override // org.omg.PortableInterceptor.ObjectReferenceTemplate
    public String server_id() {
        return Integer.toString(this.iorTemplate.getObjectKeyTemplate().getServerId());
    }

    @Override // org.omg.PortableInterceptor.ObjectReferenceTemplate
    public String orb_id() {
        return this.iorTemplate.getObjectKeyTemplate().getORBId();
    }

    @Override // org.omg.PortableInterceptor.ObjectReferenceTemplate
    public String[] adapter_name() {
        return this.iorTemplate.getObjectKeyTemplate().getObjectAdapterId().getAdapterName();
    }

    @Override // com.sun.corba.se.impl.ior.ObjectReferenceProducerBase
    public IORFactory getIORFactory() {
        return this.iorTemplate;
    }

    @Override // com.sun.corba.se.impl.ior.ObjectReferenceProducerBase
    public IORTemplateList getIORTemplateList() {
        IORTemplateList iORTemplateListMakeIORTemplateList = IORFactories.makeIORTemplateList();
        iORTemplateListMakeIORTemplateList.add(this.iorTemplate);
        iORTemplateListMakeIORTemplateList.makeImmutable();
        return iORTemplateListMakeIORTemplateList;
    }
}
