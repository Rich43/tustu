package com.sun.corba.se.impl.ior;

import com.sun.corba.se.spi.ior.Identifiable;
import java.util.Arrays;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/GenericIdentifiable.class */
public abstract class GenericIdentifiable implements Identifiable {
    private int id;
    private byte[] data;

    public GenericIdentifiable(int i2, InputStream inputStream) {
        this.id = i2;
        this.data = EncapsulationUtility.readOctets(inputStream);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return this.id;
    }

    @Override // com.sun.corba.se.spi.ior.Writeable
    public void write(OutputStream outputStream) {
        outputStream.write_ulong(this.data.length);
        outputStream.write_octet_array(this.data, 0, this.data.length);
    }

    public String toString() {
        return "GenericIdentifiable[id=" + getId() + "]";
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GenericIdentifiable)) {
            return false;
        }
        GenericIdentifiable genericIdentifiable = (GenericIdentifiable) obj;
        return getId() == genericIdentifiable.getId() && Arrays.equals(getData(), genericIdentifiable.getData());
    }

    public int hashCode() {
        int i2 = 17;
        for (int i3 = 0; i3 < this.data.length; i3++) {
            i2 = (37 * i2) + this.data[i3];
        }
        return i2;
    }

    public GenericIdentifiable(int i2, byte[] bArr) {
        this.id = i2;
        this.data = (byte[]) bArr.clone();
    }

    public byte[] getData() {
        return this.data;
    }
}
