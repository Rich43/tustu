package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.spi.orb.ORB;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.EncapsInputStreamFactory;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/TypeCodeOutputStream.class */
public final class TypeCodeOutputStream extends EncapsOutputStream {
    private OutputStream enclosure;
    private Map typeMap;
    private boolean isEncapsulation;

    public TypeCodeOutputStream(ORB orb) {
        super(orb, false);
        this.enclosure = null;
        this.typeMap = null;
        this.isEncapsulation = false;
    }

    public TypeCodeOutputStream(ORB orb, boolean z2) {
        super(orb, z2);
        this.enclosure = null;
        this.typeMap = null;
        this.isEncapsulation = false;
    }

    @Override // com.sun.corba.se.impl.encoding.EncapsOutputStream, com.sun.corba.se.impl.encoding.CDROutputStream, org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream
    public InputStream create_input_stream() {
        return EncapsInputStreamFactory.newTypeCodeInputStream((ORB) orb(), getByteBuffer(), getIndex(), isLittleEndian(), getGIOPVersion());
    }

    public void setEnclosingOutputStream(OutputStream outputStream) {
        this.enclosure = outputStream;
    }

    public TypeCodeOutputStream getTopLevelStream() {
        if (this.enclosure == null) {
            return this;
        }
        if (this.enclosure instanceof TypeCodeOutputStream) {
            return ((TypeCodeOutputStream) this.enclosure).getTopLevelStream();
        }
        return this;
    }

    public int getTopLevelPosition() {
        if (this.enclosure != null && (this.enclosure instanceof TypeCodeOutputStream)) {
            int topLevelPosition = ((TypeCodeOutputStream) this.enclosure).getTopLevelPosition() + getPosition();
            if (this.isEncapsulation) {
                topLevelPosition += 4;
            }
            return topLevelPosition;
        }
        return getPosition();
    }

    public void addIDAtPosition(String str, int i2) {
        if (this.typeMap == null) {
            this.typeMap = new HashMap(16);
        }
        this.typeMap.put(str, new Integer(i2));
    }

    public int getPositionForID(String str) {
        if (this.typeMap == null) {
            throw this.wrapper.refTypeIndirType(CompletionStatus.COMPLETED_NO);
        }
        return ((Integer) this.typeMap.get(str)).intValue();
    }

    public void writeRawBuffer(org.omg.CORBA.portable.OutputStream outputStream, int i2) {
        outputStream.write_long(i2);
        ByteBuffer byteBuffer = getByteBuffer();
        if (byteBuffer.hasArray()) {
            outputStream.write_octet_array(byteBuffer.array(), 4, getIndex() - 4);
            return;
        }
        byte[] bArr = new byte[byteBuffer.limit()];
        for (int i3 = 0; i3 < bArr.length; i3++) {
            bArr[i3] = byteBuffer.get(i3);
        }
        outputStream.write_octet_array(bArr, 4, getIndex() - 4);
    }

    public TypeCodeOutputStream createEncapsulation(org.omg.CORBA.ORB orb) {
        TypeCodeOutputStream typeCodeOutputStreamNewTypeCodeOutputStream = OutputStreamFactory.newTypeCodeOutputStream((ORB) orb, isLittleEndian());
        typeCodeOutputStreamNewTypeCodeOutputStream.setEnclosingOutputStream(this);
        typeCodeOutputStreamNewTypeCodeOutputStream.makeEncapsulation();
        return typeCodeOutputStreamNewTypeCodeOutputStream;
    }

    protected void makeEncapsulation() {
        putEndian();
        this.isEncapsulation = true;
    }

    public static TypeCodeOutputStream wrapOutputStream(OutputStream outputStream) {
        TypeCodeOutputStream typeCodeOutputStreamNewTypeCodeOutputStream = OutputStreamFactory.newTypeCodeOutputStream((ORB) outputStream.orb(), outputStream instanceof CDROutputStream ? ((CDROutputStream) outputStream).isLittleEndian() : false);
        typeCodeOutputStreamNewTypeCodeOutputStream.setEnclosingOutputStream(outputStream);
        return typeCodeOutputStreamNewTypeCodeOutputStream;
    }

    public int getPosition() {
        return getIndex();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream
    public int getRealIndex(int i2) {
        return getTopLevelPosition();
    }

    public byte[] getTypeCodeBuffer() {
        ByteBuffer byteBuffer = getByteBuffer();
        byte[] bArr = new byte[getIndex() - 4];
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = byteBuffer.get(i2 + 4);
        }
        return bArr;
    }

    public void printTypeMap() {
        System.out.println("typeMap = {");
        Iterator it = this.typeMap.keySet().iterator();
        while (it.hasNext()) {
            System.out.println("  key = " + ((String) it.next()) + ", value = " + this.typeMap.get(r0));
        }
        System.out.println("}");
    }
}
