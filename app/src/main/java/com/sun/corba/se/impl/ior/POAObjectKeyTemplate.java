package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.activation.POANameHelper;
import com.sun.corba.se.spi.ior.ObjectAdapterId;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/POAObjectKeyTemplate.class */
public final class POAObjectKeyTemplate extends NewObjectKeyTemplateBase {
    public POAObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream) {
        super(orb, i2, i3, inputStream.read_long(), inputStream.read_string(), new ObjectAdapterIdArray(POANameHelper.read(inputStream)));
        setORBVersion(inputStream);
    }

    public POAObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream, OctetSeqHolder octetSeqHolder) {
        super(orb, i2, i3, inputStream.read_long(), inputStream.read_string(), new ObjectAdapterIdArray(POANameHelper.read(inputStream)));
        octetSeqHolder.value = readObjectKey(inputStream);
        setORBVersion(inputStream);
    }

    public POAObjectKeyTemplate(ORB orb, int i2, int i3, String str, ObjectAdapterId objectAdapterId) {
        super(orb, -1347695872, i2, i3, str, objectAdapterId);
        setORBVersion(ORBVersionFactory.getORBVersion());
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase
    public void writeTemplate(OutputStream outputStream) {
        outputStream.write_long(getMagic());
        outputStream.write_long(getSubcontractId());
        outputStream.write_long(getServerId());
        outputStream.write_string(getORBId());
        getObjectAdapterId().write(outputStream);
    }
}
