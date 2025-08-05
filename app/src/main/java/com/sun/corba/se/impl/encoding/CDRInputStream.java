package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import org.omg.CORBA.Any;
import org.omg.CORBA.AnySeqHolder;
import org.omg.CORBA.BooleanSeqHolder;
import org.omg.CORBA.CharSeqHolder;
import org.omg.CORBA.Context;
import org.omg.CORBA.DataInputStream;
import org.omg.CORBA.DoubleSeqHolder;
import org.omg.CORBA.FloatSeqHolder;
import org.omg.CORBA.LongLongSeqHolder;
import org.omg.CORBA.LongSeqHolder;
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
import org.omg.CORBA.portable.ValueInputStream;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStream.class */
public abstract class CDRInputStream extends InputStream implements MarshalInputStream, DataInputStream, ValueInputStream {
    protected CorbaMessageMediator messageMediator;
    private CDRInputStreamBase impl;

    public abstract CDRInputStream dup();

    protected abstract CodeSetConversion.BTCConverter createWCharBTCConverter();

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStream$InputStreamFactory.class */
    private static class InputStreamFactory {
        private InputStreamFactory() {
        }

        public static CDRInputStreamBase newInputStream(ORB orb, GIOPVersion gIOPVersion, byte b2) {
            switch (gIOPVersion.intValue()) {
                case 256:
                    return new CDRInputStream_1_0();
                case 257:
                    return new CDRInputStream_1_1();
                case 258:
                    if (b2 != 0) {
                        return new IDLJavaSerializationInputStream(b2);
                    }
                    return new CDRInputStream_1_2();
                default:
                    throw ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING).unsupportedGiopVersion(gIOPVersion);
            }
        }
    }

    public CDRInputStream() {
    }

    public CDRInputStream(CDRInputStream cDRInputStream) {
        this.impl = cDRInputStream.impl.dup();
        this.impl.setParent(this);
    }

    public CDRInputStream(org.omg.CORBA.ORB orb, ByteBuffer byteBuffer, int i2, boolean z2, GIOPVersion gIOPVersion, byte b2, BufferManagerRead bufferManagerRead) {
        this.impl = InputStreamFactory.newInputStream((ORB) orb, gIOPVersion, b2);
        this.impl.init(orb, byteBuffer, i2, z2, bufferManagerRead);
        this.impl.setParent(this);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final boolean read_boolean() {
        return this.impl.read_boolean();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final char read_char() {
        return this.impl.read_char();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final char read_wchar() {
        return this.impl.read_wchar();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final byte read_octet() {
        return this.impl.read_octet();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final short read_short() {
        return this.impl.read_short();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final short read_ushort() {
        return this.impl.read_ushort();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final int read_long() {
        return this.impl.read_long();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final int read_ulong() {
        return this.impl.read_ulong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final long read_longlong() {
        return this.impl.read_longlong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final long read_ulonglong() {
        return this.impl.read_ulonglong();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final float read_float() {
        return this.impl.read_float();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final double read_double() {
        return this.impl.read_double();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final String read_string() {
        return this.impl.read_string();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final String read_wstring() {
        return this.impl.read_wstring();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_boolean_array(boolean[] zArr, int i2, int i3) {
        this.impl.read_boolean_array(zArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_char_array(char[] cArr, int i2, int i3) {
        this.impl.read_char_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_wchar_array(char[] cArr, int i2, int i3) {
        this.impl.read_wchar_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_octet_array(byte[] bArr, int i2, int i3) {
        this.impl.read_octet_array(bArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_short_array(short[] sArr, int i2, int i3) {
        this.impl.read_short_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_ushort_array(short[] sArr, int i2, int i3) {
        this.impl.read_ushort_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_long_array(int[] iArr, int i2, int i3) {
        this.impl.read_long_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_ulong_array(int[] iArr, int i2, int i3) {
        this.impl.read_ulong_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_longlong_array(long[] jArr, int i2, int i3) {
        this.impl.read_longlong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_ulonglong_array(long[] jArr, int i2, int i3) {
        this.impl.read_ulonglong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_float_array(float[] fArr, int i2, int i3) {
        this.impl.read_float_array(fArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void read_double_array(double[] dArr, int i2, int i3) {
        this.impl.read_double_array(dArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final Object read_Object() {
        return this.impl.read_Object();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final TypeCode read_TypeCode() {
        return this.impl.read_TypeCode();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream, org.omg.CORBA.DataInputStream
    public final Any read_any() {
        return this.impl.read_any();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final Principal read_Principal() {
        return this.impl.read_Principal();
    }

    @Override // org.omg.CORBA.portable.InputStream, java.io.InputStream
    public final int read() throws IOException {
        return this.impl.read();
    }

    @Override // org.omg.CORBA.portable.InputStream
    public final BigDecimal read_fixed() {
        return this.impl.read_fixed();
    }

    @Override // org.omg.CORBA.portable.InputStream
    public final Context read_Context() {
        return this.impl.read_Context();
    }

    @Override // org.omg.CORBA.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final Object read_Object(Class cls) {
        return this.impl.read_Object(cls);
    }

    @Override // org.omg.CORBA.portable.InputStream
    public final org.omg.CORBA.ORB orb() {
        return this.impl.orb();
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream, com.sun.corba.se.impl.encoding.MarshalInputStream
    public final Serializable read_value() {
        return this.impl.read_value();
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public final Serializable read_value(Class cls) {
        return this.impl.read_value(cls);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public final Serializable read_value(BoxedValueHelper boxedValueHelper) {
        return this.impl.read_value(boxedValueHelper);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public final Serializable read_value(String str) {
        return this.impl.read_value(str);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public final Serializable read_value(Serializable serializable) {
        return this.impl.read_value(serializable);
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public final Object read_abstract_interface() {
        return this.impl.read_abstract_interface();
    }

    @Override // org.omg.CORBA_2_3.portable.InputStream
    public final Object read_abstract_interface(Class cls) {
        return this.impl.read_abstract_interface(cls);
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    public final void consumeEndian() {
        this.impl.consumeEndian();
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    public final int getPosition() {
        return this.impl.getPosition();
    }

    @Override // org.omg.CORBA.DataInputStream
    public final Object read_Abstract() {
        return this.impl.read_Abstract();
    }

    @Override // org.omg.CORBA.DataInputStream
    public final Serializable read_Value() {
        return this.impl.read_Value();
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_any_array(AnySeqHolder anySeqHolder, int i2, int i3) {
        this.impl.read_any_array(anySeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_boolean_array(BooleanSeqHolder booleanSeqHolder, int i2, int i3) {
        this.impl.read_boolean_array(booleanSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_char_array(CharSeqHolder charSeqHolder, int i2, int i3) {
        this.impl.read_char_array(charSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_wchar_array(WCharSeqHolder wCharSeqHolder, int i2, int i3) {
        this.impl.read_wchar_array(wCharSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_octet_array(OctetSeqHolder octetSeqHolder, int i2, int i3) {
        this.impl.read_octet_array(octetSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_short_array(ShortSeqHolder shortSeqHolder, int i2, int i3) {
        this.impl.read_short_array(shortSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_ushort_array(UShortSeqHolder uShortSeqHolder, int i2, int i3) {
        this.impl.read_ushort_array(uShortSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_long_array(LongSeqHolder longSeqHolder, int i2, int i3) {
        this.impl.read_long_array(longSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_ulong_array(ULongSeqHolder uLongSeqHolder, int i2, int i3) {
        this.impl.read_ulong_array(uLongSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_ulonglong_array(ULongLongSeqHolder uLongLongSeqHolder, int i2, int i3) {
        this.impl.read_ulonglong_array(uLongLongSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_longlong_array(LongLongSeqHolder longLongSeqHolder, int i2, int i3) {
        this.impl.read_longlong_array(longLongSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_float_array(FloatSeqHolder floatSeqHolder, int i2, int i3) {
        this.impl.read_float_array(floatSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.DataInputStream
    public final void read_double_array(DoubleSeqHolder doubleSeqHolder, int i2, int i3) {
        this.impl.read_double_array(doubleSeqHolder, i2, i3);
    }

    @Override // org.omg.CORBA.portable.ValueBase
    public final String[] _truncatable_ids() {
        return this.impl._truncatable_ids();
    }

    @Override // java.io.InputStream
    public final int read(byte[] bArr) throws IOException {
        return this.impl.read(bArr);
    }

    @Override // java.io.InputStream
    public final int read(byte[] bArr, int i2, int i3) throws IOException {
        return this.impl.read(bArr, i2, i3);
    }

    @Override // java.io.InputStream
    public final long skip(long j2) throws IOException {
        return this.impl.skip(j2);
    }

    @Override // java.io.InputStream
    public final int available() throws IOException {
        return this.impl.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.impl.close();
    }

    @Override // java.io.InputStream
    public final void mark(int i2) {
        this.impl.mark(i2);
    }

    @Override // java.io.InputStream
    public final void reset() {
        this.impl.reset();
    }

    @Override // java.io.InputStream
    public final boolean markSupported() {
        return this.impl.markSupported();
    }

    public final BigDecimal read_fixed(short s2, short s3) {
        return this.impl.read_fixed(s2, s3);
    }

    public final boolean isLittleEndian() {
        return this.impl.isLittleEndian();
    }

    protected final ByteBuffer getByteBuffer() {
        return this.impl.getByteBuffer();
    }

    protected final void setByteBuffer(ByteBuffer byteBuffer) {
        this.impl.setByteBuffer(byteBuffer);
    }

    protected final void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        this.impl.setByteBufferWithInfo(byteBufferWithInfo);
    }

    protected final boolean isSharing(ByteBuffer byteBuffer) {
        return getByteBuffer() == byteBuffer;
    }

    public final int getBufferLength() {
        return this.impl.getBufferLength();
    }

    protected final void setBufferLength(int i2) {
        this.impl.setBufferLength(i2);
    }

    protected final int getIndex() {
        return this.impl.getIndex();
    }

    protected final void setIndex(int i2) {
        this.impl.setIndex(i2);
    }

    public final void orb(org.omg.CORBA.ORB orb) {
        this.impl.orb(orb);
    }

    public final GIOPVersion getGIOPVersion() {
        return this.impl.getGIOPVersion();
    }

    public final BufferManagerRead getBufferManager() {
        return this.impl.getBufferManager();
    }

    public CodeBase getCodeBase() {
        return null;
    }

    protected CodeSetConversion.BTCConverter createCharBTCConverter() {
        return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.ISO_8859_1, this.impl.isLittleEndian());
    }

    void printBuffer() {
        this.impl.printBuffer();
    }

    public void alignOnBoundary(int i2) {
        this.impl.alignOnBoundary(i2);
    }

    public void setHeaderPadding(boolean z2) {
        this.impl.setHeaderPadding(z2);
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    public void performORBVersionSpecificInit() {
        if (this.impl != null) {
            this.impl.performORBVersionSpecificInit();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    public void resetCodeSetConverters() {
        this.impl.resetCodeSetConverters();
    }

    public void setMessageMediator(MessageMediator messageMediator) {
        this.messageMediator = (CorbaMessageMediator) messageMediator;
    }

    public MessageMediator getMessageMediator() {
        return this.messageMediator;
    }

    @Override // org.omg.CORBA.portable.ValueInputStream
    public void start_value() {
        this.impl.start_value();
    }

    @Override // org.omg.CORBA.portable.ValueInputStream
    public void end_value() {
        this.impl.end_value();
    }
}
