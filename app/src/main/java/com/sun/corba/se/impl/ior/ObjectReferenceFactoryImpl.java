package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.IORFactory;
import com.sun.corba.se.spi.ior.IORTemplateList;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.StreamableValue;
import org.omg.PortableInterceptor.ObjectReferenceFactory;
import org.omg.PortableInterceptor.ObjectReferenceFactoryHelper;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectReferenceFactoryImpl.class */
public class ObjectReferenceFactoryImpl extends ObjectReferenceProducerBase implements ObjectReferenceFactory, StreamableValue {
    private transient IORTemplateList iorTemplates;
    public static final String repositoryId = "IDL:com/sun/corba/se/impl/ior/ObjectReferenceFactoryImpl:1.0";

    public ObjectReferenceFactoryImpl(InputStream inputStream) {
        super((ORB) inputStream.orb());
        _read(inputStream);
    }

    public ObjectReferenceFactoryImpl(ORB orb, IORTemplateList iORTemplateList) {
        super(orb);
        this.iorTemplates = iORTemplateList;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ObjectReferenceFactoryImpl) {
            return this.iorTemplates != null && this.iorTemplates.equals(((ObjectReferenceFactoryImpl) obj).iorTemplates);
        }
        return false;
    }

    public int hashCode() {
        return this.iorTemplates.hashCode();
    }

    @Override // org.omg.CORBA.portable.ValueBase
    public String[] _truncatable_ids() {
        return new String[]{repositoryId};
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return ObjectReferenceFactoryHelper.type();
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.iorTemplates = IORFactories.makeIORTemplateList((org.omg.CORBA_2_3.portable.InputStream) inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        this.iorTemplates.write((org.omg.CORBA_2_3.portable.OutputStream) outputStream);
    }

    @Override // com.sun.corba.se.impl.ior.ObjectReferenceProducerBase
    public IORFactory getIORFactory() {
        return this.iorTemplates;
    }

    @Override // com.sun.corba.se.impl.ior.ObjectReferenceProducerBase
    public IORTemplateList getIORTemplateList() {
        return this.iorTemplates;
    }
}
