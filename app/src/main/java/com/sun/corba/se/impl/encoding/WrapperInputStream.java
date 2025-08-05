package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.TypeCodeImpl;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/WrapperInputStream.class */
public class WrapperInputStream extends InputStream implements TypeCodeReader {
    private CDRInputStream stream;
    private Map typeMap = null;
    private int startPos;

    public WrapperInputStream(CDRInputStream cDRInputStream) {
        this.startPos = 0;
        this.stream = cDRInputStream;
        this.startPos = this.stream.getPosition();
    }

    @Override // org.omg.CORBA.portable.InputStream, java.io.InputStream
    public int read() throws IOException {
        return this.stream.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        return this.stream.read(bArr);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        return this.stream.read(bArr, i2, i3);
    }

    @Override // java.io.InputStream
    public long skip(long j2) throws IOException {
        return this.stream.skip(j2);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.stream.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.stream.close();
    }

    @Override // java.io.InputStream
    public void mark(int i2) {
        this.stream.mark(i2);
    }

    @Override // java.io.InputStream
    public void reset() {
        this.stream.reset();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.stream.markSupported();
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader, com.sun.corba.se.impl.encoding.MarshalInputStream
    public int getPosition() {
        return this.stream.getPosition();
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    public void consumeEndian() {
        this.stream.consumeEndian();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public boolean read_boolean() {
        return this.stream.read_boolean();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public char read_char() {
        return this.stream.read_char();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public char read_wchar() {
        return this.stream.read_wchar();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public byte read_octet() {
        return this.stream.read_octet();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public short read_short() {
        return this.stream.read_short();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public short read_ushort() {
        return this.stream.read_ushort();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public int read_long() {
        return this.stream.read_long();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public int read_ulong() {
        return this.stream.read_ulong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public long read_longlong() {
        return this.stream.read_longlong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public long read_ulonglong() {
        return this.stream.read_ulonglong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public float read_float() {
        return this.stream.read_float();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public double read_double() {
        return this.stream.read_double();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public String read_string() {
        return this.stream.read_string();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public String read_wstring() {
        return this.stream.read_wstring();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_boolean_array(boolean[] zArr, int i2, int i3) {
        this.stream.read_boolean_array(zArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_char_array(char[] cArr, int i2, int i3) {
        this.stream.read_char_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_wchar_array(char[] cArr, int i2, int i3) {
        this.stream.read_wchar_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_octet_array(byte[] bArr, int i2, int i3) {
        this.stream.read_octet_array(bArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_short_array(short[] sArr, int i2, int i3) {
        this.stream.read_short_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_ushort_array(short[] sArr, int i2, int i3) {
        this.stream.read_ushort_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_long_array(int[] iArr, int i2, int i3) {
        this.stream.read_long_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_ulong_array(int[] iArr, int i2, int i3) {
        this.stream.read_ulong_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_longlong_array(long[] jArr, int i2, int i3) {
        this.stream.read_longlong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_ulonglong_array(long[] jArr, int i2, int i3) {
        this.stream.read_ulonglong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_float_array(float[] fArr, int i2, int i3) {
        this.stream.read_float_array(fArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_double_array(double[] dArr, int i2, int i3) {
        this.stream.read_double_array(dArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public Object read_Object() {
        return this.stream.read_Object();
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public Serializable read_value() {
        return this.stream.read_value();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public TypeCode read_TypeCode() {
        return this.stream.read_TypeCode();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public Any read_any() {
        return this.stream.read_any();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public Principal read_Principal() {
        return this.stream.read_Principal();
    }

    @Override // org.omg.CORBA.portable.InputStream
    public BigDecimal read_fixed() {
        return this.stream.read_fixed();
    }

    @Override // org.omg.CORBA.portable.InputStream
    public Context read_Context() {
        return this.stream.read_Context();
    }

    @Override // org.omg.CORBA.portable.InputStream
    public ORB orb() {
        return this.stream.orb();
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
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public TypeCodeReader getTopLevelStream() {
        return this;
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public int getTopLevelPosition() {
        return getPosition() - this.startPos;
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    public void performORBVersionSpecificInit() {
        this.stream.performORBVersionSpecificInit();
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    public void resetCodeSetConverters() {
        this.stream.resetCodeSetConverters();
    }

    @Override // com.sun.corba.se.impl.encoding.TypeCodeReader
    public void printTypeMap() {
        System.out.println("typeMap = {");
        ArrayList<Integer> arrayList = new ArrayList(this.typeMap.keySet());
        Collections.sort(arrayList);
        for (Integer num : arrayList) {
            System.out.println("  key = " + num.intValue() + ", value = " + ((TypeCodeImpl) this.typeMap.get(num)).description());
        }
        System.out.println("}");
    }
}
