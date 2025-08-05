package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import org.omg.CORBA.Any;
import org.omg.CORBA.AnySeqHolder;
import org.omg.CORBA.BooleanSeqHolder;
import org.omg.CORBA.CharSeqHolder;
import org.omg.CORBA.Context;
import org.omg.CORBA.DoubleSeqHolder;
import org.omg.CORBA.FloatSeqHolder;
import org.omg.CORBA.LongLongSeqHolder;
import org.omg.CORBA.LongSeqHolder;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA.Principal;
import org.omg.CORBA.ShortSeqHolder;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ULongLongSeqHolder;
import org.omg.CORBA.ULongSeqHolder;
import org.omg.CORBA.UShortSeqHolder;
import org.omg.CORBA.WCharSeqHolder;
import org.omg.CORBA.portable.BoxedValueHelper;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStreamBase.class */
abstract class CDRInputStreamBase extends InputStream {
    protected CDRInputStream parent;

    public abstract void init(ORB orb, ByteBuffer byteBuffer, int i2, boolean z2, BufferManagerRead bufferManagerRead);

    public abstract boolean read_boolean();

    public abstract char read_char();

    public abstract char read_wchar();

    public abstract byte read_octet();

    public abstract short read_short();

    public abstract short read_ushort();

    public abstract int read_long();

    public abstract int read_ulong();

    public abstract long read_longlong();

    public abstract long read_ulonglong();

    public abstract float read_float();

    public abstract double read_double();

    public abstract String read_string();

    public abstract String read_wstring();

    public abstract void read_boolean_array(boolean[] zArr, int i2, int i3);

    public abstract void read_char_array(char[] cArr, int i2, int i3);

    public abstract void read_wchar_array(char[] cArr, int i2, int i3);

    public abstract void read_octet_array(byte[] bArr, int i2, int i3);

    public abstract void read_short_array(short[] sArr, int i2, int i3);

    public abstract void read_ushort_array(short[] sArr, int i2, int i3);

    public abstract void read_long_array(int[] iArr, int i2, int i3);

    public abstract void read_ulong_array(int[] iArr, int i2, int i3);

    public abstract void read_longlong_array(long[] jArr, int i2, int i3);

    public abstract void read_ulonglong_array(long[] jArr, int i2, int i3);

    public abstract void read_float_array(float[] fArr, int i2, int i3);

    public abstract void read_double_array(double[] dArr, int i2, int i3);

    public abstract Object read_Object();

    public abstract TypeCode read_TypeCode();

    public abstract Any read_any();

    public abstract Principal read_Principal();

    public abstract BigDecimal read_fixed();

    public abstract Object read_Object(Class cls);

    public abstract ORB orb();

    public abstract Serializable read_value();

    public abstract Serializable read_value(Class cls);

    public abstract Serializable read_value(BoxedValueHelper boxedValueHelper);

    public abstract Serializable read_value(String str);

    public abstract Serializable read_value(Serializable serializable);

    public abstract Object read_abstract_interface();

    public abstract Object read_abstract_interface(Class cls);

    public abstract void consumeEndian();

    public abstract int getPosition();

    public abstract Object read_Abstract();

    public abstract Serializable read_Value();

    public abstract void read_any_array(AnySeqHolder anySeqHolder, int i2, int i3);

    public abstract void read_boolean_array(BooleanSeqHolder booleanSeqHolder, int i2, int i3);

    public abstract void read_char_array(CharSeqHolder charSeqHolder, int i2, int i3);

    public abstract void read_wchar_array(WCharSeqHolder wCharSeqHolder, int i2, int i3);

    public abstract void read_octet_array(OctetSeqHolder octetSeqHolder, int i2, int i3);

    public abstract void read_short_array(ShortSeqHolder shortSeqHolder, int i2, int i3);

    public abstract void read_ushort_array(UShortSeqHolder uShortSeqHolder, int i2, int i3);

    public abstract void read_long_array(LongSeqHolder longSeqHolder, int i2, int i3);

    public abstract void read_ulong_array(ULongSeqHolder uLongSeqHolder, int i2, int i3);

    public abstract void read_ulonglong_array(ULongLongSeqHolder uLongLongSeqHolder, int i2, int i3);

    public abstract void read_longlong_array(LongLongSeqHolder longLongSeqHolder, int i2, int i3);

    public abstract void read_float_array(FloatSeqHolder floatSeqHolder, int i2, int i3);

    public abstract void read_double_array(DoubleSeqHolder doubleSeqHolder, int i2, int i3);

    public abstract String[] _truncatable_ids();

    @Override // java.io.InputStream
    public abstract void mark(int i2);

    @Override // java.io.InputStream
    public abstract void reset();

    public abstract CDRInputStreamBase dup();

    public abstract BigDecimal read_fixed(short s2, short s3);

    public abstract boolean isLittleEndian();

    abstract void setHeaderPadding(boolean z2);

    public abstract ByteBuffer getByteBuffer();

    public abstract void setByteBuffer(ByteBuffer byteBuffer);

    public abstract void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo);

    public abstract int getBufferLength();

    public abstract void setBufferLength(int i2);

    public abstract int getIndex();

    public abstract void setIndex(int i2);

    public abstract void orb(ORB orb);

    public abstract BufferManagerRead getBufferManager();

    public abstract GIOPVersion getGIOPVersion();

    abstract CodeBase getCodeBase();

    abstract void printBuffer();

    abstract void alignOnBoundary(int i2);

    abstract void performORBVersionSpecificInit();

    public abstract void resetCodeSetConverters();

    public abstract void start_value();

    public abstract void end_value();

    CDRInputStreamBase() {
    }

    public void setParent(CDRInputStream cDRInputStream) {
        this.parent = cDRInputStream;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        throw new NO_IMPLEMENT();
    }

    public Context read_Context() {
        throw new NO_IMPLEMENT();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }
}
