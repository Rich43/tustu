package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/OctetSeqHolder.class */
public final class OctetSeqHolder implements Streamable {
    public byte[] value;

    public OctetSeqHolder() {
        this.value = null;
    }

    public OctetSeqHolder(byte[] bArr) {
        this.value = null;
        this.value = bArr;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = OctetSeqHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        OctetSeqHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return OctetSeqHelper.type();
    }
}
