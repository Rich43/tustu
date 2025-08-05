package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersion;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/OldPOAObjectKeyTemplate.class */
public final class OldPOAObjectKeyTemplate extends OldObjectKeyTemplateBase {
    public OldPOAObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream) {
        this(orb, i2, i3, inputStream.read_long(), inputStream.read_long(), inputStream.read_long());
    }

    public OldPOAObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream, OctetSeqHolder octetSeqHolder) {
        this(orb, i2, i3, inputStream);
        octetSeqHolder.value = readObjectKey(inputStream);
    }

    public OldPOAObjectKeyTemplate(ORB orb, int i2, int i3, int i4, int i5, int i6) {
        super(orb, i2, i3, i4, Integer.toString(i5), new ObjectAdapterIdNumber(i6));
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase
    public void writeTemplate(OutputStream outputStream) {
        outputStream.write_long(getMagic());
        outputStream.write_long(getSubcontractId());
        outputStream.write_long(getServerId());
        outputStream.write_long(Integer.parseInt(getORBId()));
        outputStream.write_long(((ObjectAdapterIdNumber) getObjectAdapterId()).getOldPOAId());
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase, com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public ORBVersion getORBVersion() {
        if (getMagic() == -1347695874) {
            return ORBVersionFactory.getOLD();
        }
        if (getMagic() == -1347695873) {
            return ORBVersionFactory.getNEW();
        }
        throw new INTERNAL();
    }
}
