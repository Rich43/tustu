package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.BoxedValueHelper;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDROutputStreamBase.class */
abstract class CDROutputStreamBase extends OutputStream {
    protected CDROutputStream parent;

    protected abstract void init(ORB orb, boolean z2, BufferManagerWrite bufferManagerWrite, byte b2, boolean z3);

    public abstract void write_boolean(boolean z2);

    public abstract void write_char(char c2);

    public abstract void write_wchar(char c2);

    public abstract void write_octet(byte b2);

    public abstract void write_short(short s2);

    public abstract void write_ushort(short s2);

    public abstract void write_long(int i2);

    public abstract void write_ulong(int i2);

    public abstract void write_longlong(long j2);

    public abstract void write_ulonglong(long j2);

    public abstract void write_float(float f2);

    public abstract void write_double(double d2);

    public abstract void write_string(String str);

    public abstract void write_wstring(String str);

    public abstract void write_boolean_array(boolean[] zArr, int i2, int i3);

    public abstract void write_char_array(char[] cArr, int i2, int i3);

    public abstract void write_wchar_array(char[] cArr, int i2, int i3);

    public abstract void write_octet_array(byte[] bArr, int i2, int i3);

    public abstract void write_short_array(short[] sArr, int i2, int i3);

    public abstract void write_ushort_array(short[] sArr, int i2, int i3);

    public abstract void write_long_array(int[] iArr, int i2, int i3);

    public abstract void write_ulong_array(int[] iArr, int i2, int i3);

    public abstract void write_longlong_array(long[] jArr, int i2, int i3);

    public abstract void write_ulonglong_array(long[] jArr, int i2, int i3);

    public abstract void write_float_array(float[] fArr, int i2, int i3);

    public abstract void write_double_array(double[] dArr, int i2, int i3);

    public abstract void write_Object(Object object);

    public abstract void write_TypeCode(TypeCode typeCode);

    public abstract void write_any(Any any);

    public abstract void write_Principal(Principal principal);

    public abstract void write_fixed(BigDecimal bigDecimal);

    public abstract ORB orb();

    public abstract void write_value(Serializable serializable);

    public abstract void write_value(Serializable serializable, Class cls);

    public abstract void write_value(Serializable serializable, String str);

    public abstract void write_value(Serializable serializable, BoxedValueHelper boxedValueHelper);

    public abstract void write_abstract_interface(Object obj);

    public abstract void start_block();

    public abstract void end_block();

    public abstract void putEndian();

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    public abstract byte[] toByteArray();

    public abstract void write_Abstract(Object obj);

    public abstract void write_Value(Serializable serializable);

    public abstract void write_any_array(Any[] anyArr, int i2, int i3);

    public abstract String[] _truncatable_ids();

    abstract void setHeaderPadding(boolean z2);

    public abstract int getSize();

    public abstract int getIndex();

    public abstract void setIndex(int i2);

    public abstract ByteBuffer getByteBuffer();

    public abstract void setByteBuffer(ByteBuffer byteBuffer);

    public abstract boolean isLittleEndian();

    public abstract ByteBufferWithInfo getByteBufferWithInfo();

    public abstract void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo);

    public abstract BufferManagerWrite getBufferManager();

    public abstract void write_fixed(BigDecimal bigDecimal, short s2, short s3);

    public abstract void writeOctetSequenceTo(org.omg.CORBA.portable.OutputStream outputStream);

    public abstract GIOPVersion getGIOPVersion();

    public abstract void writeIndirection(int i2, int i3);

    abstract void freeInternalCaches();

    abstract void printBuffer();

    abstract void alignOnBoundary(int i2);

    public abstract void start_value(String str);

    public abstract void end_value();

    CDROutputStreamBase() {
    }

    public void setParent(CDROutputStream cDROutputStream) {
        this.parent = cDROutputStream;
    }

    public void init(ORB orb, BufferManagerWrite bufferManagerWrite, byte b2) {
        init(orb, false, bufferManagerWrite, b2, true);
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        throw new NO_IMPLEMENT();
    }

    public void write_Context(Context context, ContextList contextList) {
        throw new NO_IMPLEMENT();
    }
}
