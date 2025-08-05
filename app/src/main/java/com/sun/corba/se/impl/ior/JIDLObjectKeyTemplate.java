package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/JIDLObjectKeyTemplate.class */
public final class JIDLObjectKeyTemplate extends NewObjectKeyTemplateBase {
    public JIDLObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream) {
        super(orb, i2, i3, inputStream.read_long(), "", JIDL_OAID);
        setORBVersion(inputStream);
    }

    public JIDLObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream, OctetSeqHolder octetSeqHolder) {
        super(orb, i2, i3, inputStream.read_long(), "", JIDL_OAID);
        octetSeqHolder.value = readObjectKey(inputStream);
        setORBVersion(inputStream);
    }

    public JIDLObjectKeyTemplate(ORB orb, int i2, int i3) {
        super(orb, -1347695872, i2, i3, "", JIDL_OAID);
        setORBVersion(ORBVersionFactory.getORBVersion());
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase
    protected void writeTemplate(OutputStream outputStream) {
        outputStream.write_long(getMagic());
        outputStream.write_long(getSubcontractId());
        outputStream.write_long(getServerId());
    }
}
