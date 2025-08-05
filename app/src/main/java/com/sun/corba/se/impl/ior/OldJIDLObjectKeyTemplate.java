package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.encoding.CDRInputStream;
import com.sun.corba.se.spi.ior.ObjectId;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/OldJIDLObjectKeyTemplate.class */
public final class OldJIDLObjectKeyTemplate extends OldObjectKeyTemplateBase {
    public static final byte NULL_PATCH_VERSION = 0;
    byte patchVersion;

    public OldJIDLObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream, OctetSeqHolder octetSeqHolder) {
        this(orb, i2, i3, inputStream);
        octetSeqHolder.value = readObjectKey(inputStream);
        if (i2 == -1347695873 && octetSeqHolder.value.length > ((CDRInputStream) inputStream).getPosition()) {
            this.patchVersion = inputStream.read_octet();
            if (this.patchVersion == 1) {
                setORBVersion(ORBVersionFactory.getJDK1_3_1_01());
            } else {
                if (this.patchVersion > 1) {
                    setORBVersion(ORBVersionFactory.getORBVersion());
                    return;
                }
                throw this.wrapper.invalidJdk131PatchLevel(new Integer(this.patchVersion));
            }
        }
    }

    public OldJIDLObjectKeyTemplate(ORB orb, int i2, int i3, int i4) {
        super(orb, i2, i3, i4, "", JIDL_OAID);
        this.patchVersion = (byte) 0;
    }

    public OldJIDLObjectKeyTemplate(ORB orb, int i2, int i3, InputStream inputStream) {
        this(orb, i2, i3, inputStream.read_long());
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase
    protected void writeTemplate(OutputStream outputStream) {
        outputStream.write_long(getMagic());
        outputStream.write_long(getSubcontractId());
        outputStream.write_long(getServerId());
    }

    @Override // com.sun.corba.se.impl.ior.ObjectKeyTemplateBase, com.sun.corba.se.spi.ior.ObjectKeyTemplate
    public void write(ObjectId objectId, OutputStream outputStream) {
        super.write(objectId, outputStream);
        if (this.patchVersion != 0) {
            outputStream.write_octet(this.patchVersion);
        }
    }
}
