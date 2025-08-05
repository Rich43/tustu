package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.ObjectId;
import java.util.Arrays;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/ObjectIdImpl.class */
public final class ObjectIdImpl implements ObjectId {
    private byte[] id;

    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectIdImpl)) {
            return false;
        }
        return Arrays.equals(this.id, ((ObjectIdImpl) obj).id);
    }

    public int hashCode() {
        int i2 = 17;
        for (int i3 = 0; i3 < this.id.length; i3++) {
            i2 = (37 * i2) + this.id[i3];
        }
        return i2;
    }

    public ObjectIdImpl(byte[] bArr) {
        this.id = bArr;
    }

    @Override // com.sun.corba.se.spi.ior.ObjectId
    public byte[] getId() {
        return this.id;
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        outputStream.write_long(this.id.length);
        outputStream.write_octet_array(this.id, 0, this.id.length);
    }
}
