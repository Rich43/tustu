package com.sun.jmx.remote.protocol.iiop;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.BoxedValueHelper;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/jmx/remote/protocol/iiop/ProxyInputStream.class */
public class ProxyInputStream extends InputStream {
    protected final org.omg.CORBA.portable.InputStream in;

    public ProxyInputStream(org.omg.CORBA.portable.InputStream inputStream) {
        this.in = inputStream;
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public boolean read_boolean() {
        return this.in.read_boolean();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public char read_char() {
        return this.in.read_char();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public char read_wchar() {
        return this.in.read_wchar();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public byte read_octet() {
        return this.in.read_octet();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public short read_short() {
        return this.in.read_short();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public short read_ushort() {
        return this.in.read_ushort();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public int read_long() {
        return this.in.read_long();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public int read_ulong() {
        return this.in.read_ulong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public long read_longlong() {
        return this.in.read_longlong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public long read_ulonglong() {
        return this.in.read_ulonglong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public float read_float() {
        return this.in.read_float();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public double read_double() {
        return this.in.read_double();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public String read_string() {
        return this.in.read_string();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public String read_wstring() {
        return this.in.read_wstring();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_boolean_array(boolean[] zArr, int i2, int i3) {
        this.in.read_boolean_array(zArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_char_array(char[] cArr, int i2, int i3) {
        this.in.read_char_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_wchar_array(char[] cArr, int i2, int i3) {
        this.in.read_wchar_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_octet_array(byte[] bArr, int i2, int i3) {
        this.in.read_octet_array(bArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_short_array(short[] sArr, int i2, int i3) {
        this.in.read_short_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_ushort_array(short[] sArr, int i2, int i3) {
        this.in.read_ushort_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_long_array(int[] iArr, int i2, int i3) {
        this.in.read_long_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_ulong_array(int[] iArr, int i2, int i3) {
        this.in.read_ulong_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_longlong_array(long[] jArr, int i2, int i3) {
        this.in.read_longlong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_ulonglong_array(long[] jArr, int i2, int i3) {
        this.in.read_ulonglong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_float_array(float[] fArr, int i2, int i3) {
        this.in.read_float_array(fArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public void read_double_array(double[] dArr, int i2, int i3) {
        this.in.read_double_array(dArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public Object read_Object() {
        return this.in.read_Object();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public TypeCode read_TypeCode() {
        return this.in.read_TypeCode();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public Any read_any() {
        return this.in.read_any();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    @Deprecated
    public Principal read_Principal() {
        return this.in.read_Principal();
    }

    @Override // org.omg.CORBA.portable.InputStream, java.io.InputStream
    public int read() throws IOException {
        return this.in.read();
    }

    @Override // org.omg.CORBA.portable.InputStream
    public BigDecimal read_fixed() {
        return this.in.read_fixed();
    }

    @Override // org.omg.CORBA.portable.InputStream
    public Context read_Context() {
        return this.in.read_Context();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public Object read_Object(Class cls) {
        return this.in.read_Object(cls);
    }

    @Override // org.omg.CORBA.portable.InputStream
    public ORB orb() {
        return this.in.orb();
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public Serializable read_value() {
        return narrow().read_value();
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public Serializable read_value(Class cls) {
        return narrow().read_value(cls);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public Serializable read_value(BoxedValueHelper boxedValueHelper) {
        return narrow().read_value(boxedValueHelper);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public Serializable read_value(String str) {
        return narrow().read_value(str);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public Serializable read_value(Serializable serializable) {
        return narrow().read_value(serializable);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public Object read_abstract_interface() {
        return narrow().read_abstract_interface();
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public Object read_abstract_interface(Class cls) {
        return narrow().read_abstract_interface(cls);
    }

    protected InputStream narrow() {
        if (this.in instanceof InputStream) {
            return (InputStream) this.in;
        }
        throw new NO_IMPLEMENT();
    }

    public org.omg.CORBA.portable.InputStream getProxiedInputStream() {
        return this.in;
    }
}
