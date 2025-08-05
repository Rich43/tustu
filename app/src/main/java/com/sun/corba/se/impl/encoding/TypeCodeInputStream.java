package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.ORB;
import org.omg.CORBA_2_3.portable.InputStream;
import sun.corba.EncapsInputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/TypeCodeInputStream.class */
public class TypeCodeInputStream extends EncapsInputStream implements TypeCodeReader {
    private Map typeMap;
    private InputStream enclosure;
    private boolean isEncapsulation;

    public TypeCodeInputStream(ORB orb, byte[] bArr, int i2) {
        super(orb, bArr, i2);
        this.typeMap = null;
        this.enclosure = null;
        this.isEncapsulation = false;
    }

    public TypeCodeInputStream(ORB orb, byte[] bArr, int i2, boolean z2, GIOPVersion gIOPVersion) {
        super(orb, bArr, i2, z2, gIOPVersion);
        this.typeMap = null;
        this.enclosure = null;
        this.isEncapsulation = false;
    }

    public TypeCodeInputStream(ORB orb, ByteBuffer byteBuffer, int i2, boolean z2, GIOPVersion gIOPVersion) {
        super(orb, byteBuffer, i2, z2, gIOPVersion);
        this.typeMap = null;
        this.enclosure = null;
        this.isEncapsulation = false;
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public void addTypeCodeAtPosition(TypeCodeImpl typeCodeImpl, int i2) {
        if (this.typeMap == null) {
            this.typeMap = new HashMap(16);
        }
        this.typeMap.put(new Integer(i2), typeCodeImpl);
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public TypeCodeImpl getTypeCodeAtPosition(int i2) {
        if (this.typeMap == null) {
            return null;
        }
        return (TypeCodeImpl) this.typeMap.get(new Integer(i2));
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public void setEnclosingInputStream(InputStream inputStream) {
        this.enclosure = inputStream;
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public TypeCodeReader getTopLevelStream() {
        if (this.enclosure == null) {
            return this;
        }
        if (this.enclosure instanceof TypeCodeReader) {
            return ((TypeCodeReader) this.enclosure).getTopLevelStream();
        }
        return this;
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public int getTopLevelPosition() {
        if (this.enclosure != null && (this.enclosure instanceof TypeCodeReader)) {
            return (((TypeCodeReader) this.enclosure).getTopLevelPosition() - getBufferLength()) + getPosition();
        }
        return getPosition();
    }

    public static TypeCodeInputStream readEncapsulation(InputStream inputStream, ORB orb) {
        TypeCodeInputStream typeCodeInputStreamNewTypeCodeInputStream;
        byte[] bArr = new byte[inputStream.read_long()];
        inputStream.read_octet_array(bArr, 0, bArr.length);
        if (inputStream instanceof CDRInputStream) {
            typeCodeInputStreamNewTypeCodeInputStream = EncapsInputStreamFactory.newTypeCodeInputStream((com.sun.corba.se.spi.orb.ORB) orb, bArr, bArr.length, ((CDRInputStream) inputStream).isLittleEndian(), ((CDRInputStream) inputStream).getGIOPVersion());
        } else {
            typeCodeInputStreamNewTypeCodeInputStream = EncapsInputStreamFactory.newTypeCodeInputStream((com.sun.corba.se.spi.orb.ORB) orb, bArr, bArr.length);
        }
        typeCodeInputStreamNewTypeCodeInputStream.setEnclosingInputStream(inputStream);
        typeCodeInputStreamNewTypeCodeInputStream.makeEncapsulation();
        return typeCodeInputStreamNewTypeCodeInputStream;
    }

    protected void makeEncapsulation() {
        consumeEndian();
        this.isEncapsulation = true;
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public void printTypeMap() {
        System.out.println("typeMap = {");
        for (Integer num : this.typeMap.keySet()) {
            System.out.println("  key = " + num.intValue() + ", value = " + ((TypeCodeImpl) this.typeMap.get(num)).description());
        }
        System.out.println("}");
    }
}
