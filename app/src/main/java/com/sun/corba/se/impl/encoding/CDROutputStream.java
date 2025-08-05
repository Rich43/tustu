package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.DataOutputStream;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.BoxedValueHelper;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ValueOutputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDROutputStream.class */
public abstract class CDROutputStream extends OutputStream implements MarshalOutputStream, DataOutputStream, ValueOutputStream {
    private CDROutputStreamBase impl;
    protected ORB orb;
    protected ORBUtilSystemException wrapper;
    protected CorbaMessageMediator corbaMessageMediator;

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream
    public abstract InputStream create_input_stream();

    protected abstract CodeSetConversion.CTBConverter createWCharCTBConverter();

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDROutputStream$OutputStreamFactory.class */
    private static class OutputStreamFactory {
        private OutputStreamFactory() {
        }

        public static CDROutputStreamBase newOutputStream(ORB orb, GIOPVersion gIOPVersion, byte b2) {
            switch (gIOPVersion.intValue()) {
                case 256:
                    return new CDROutputStream_1_0();
                case 257:
                    return new CDROutputStream_1_1();
                case 258:
                    if (b2 != 0) {
                        return new IDLJavaSerializationOutputStream(b2);
                    }
                    return new CDROutputStream_1_2();
                default:
                    throw ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING).unsupportedGiopVersion(gIOPVersion);
            }
        }
    }

    public CDROutputStream(ORB orb, GIOPVersion gIOPVersion, byte b2, boolean z2, BufferManagerWrite bufferManagerWrite, byte b3, boolean z3) {
        this.impl = OutputStreamFactory.newOutputStream(orb, gIOPVersion, b2);
        this.impl.init(orb, z2, bufferManagerWrite, b3, z3);
        this.impl.setParent(this);
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
    }

    public CDROutputStream(ORB orb, GIOPVersion gIOPVersion, byte b2, boolean z2, BufferManagerWrite bufferManagerWrite, byte b3) {
        this(orb, gIOPVersion, b2, z2, bufferManagerWrite, b3, true);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_boolean(boolean z2) {
        this.impl.write_boolean(z2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_char(char c2) {
        this.impl.write_char(c2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_wchar(char c2) {
        this.impl.write_wchar(c2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_octet(byte b2) {
        this.impl.write_octet(b2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_short(short s2) {
        this.impl.write_short(s2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_ushort(short s2) {
        this.impl.write_ushort(s2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_long(int i2) {
        this.impl.write_long(i2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_ulong(int i2) {
        this.impl.write_ulong(i2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_longlong(long j2) {
        this.impl.write_longlong(j2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_ulonglong(long j2) {
        this.impl.write_ulonglong(j2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_float(float f2) {
        this.impl.write_float(f2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_double(double d2) {
        this.impl.write_double(d2);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_string(String str) {
        this.impl.write_string(str);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_wstring(String str) {
        this.impl.write_wstring(str);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_boolean_array(boolean[] zArr, int i2, int i3) {
        this.impl.write_boolean_array(zArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_char_array(char[] cArr, int i2, int i3) {
        this.impl.write_char_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_wchar_array(char[] cArr, int i2, int i3) {
        this.impl.write_wchar_array(cArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_octet_array(byte[] bArr, int i2, int i3) {
        this.impl.write_octet_array(bArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_short_array(short[] sArr, int i2, int i3) {
        this.impl.write_short_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_ushort_array(short[] sArr, int i2, int i3) {
        this.impl.write_ushort_array(sArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_long_array(int[] iArr, int i2, int i3) {
        this.impl.write_long_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_ulong_array(int[] iArr, int i2, int i3) {
        this.impl.write_ulong_array(iArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_longlong_array(long[] jArr, int i2, int i3) {
        this.impl.write_longlong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_ulonglong_array(long[] jArr, int i2, int i3) {
        this.impl.write_ulonglong_array(jArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_float_array(float[] fArr, int i2, int i3) {
        this.impl.write_float_array(fArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_double_array(double[] dArr, int i2, int i3) {
        this.impl.write_double_array(dArr, i2, i3);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_Object(Object object) {
        this.impl.write_Object(object);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_TypeCode(TypeCode typeCode) {
        this.impl.write_TypeCode(typeCode);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream, org.omg.CORBA.DataOutputStream
    public final void write_any(Any any) {
        this.impl.write_any(any);
    }

    @Override // org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream
    public final void write_Principal(Principal principal) {
        this.impl.write_Principal(principal);
    }

    @Override // org.omg.CORBA.portable.OutputStream, java.io.OutputStream
    public final void write(int i2) throws IOException {
        this.impl.write(i2);
    }

    @Override // org.omg.CORBA.portable.OutputStream
    public final void write_fixed(BigDecimal bigDecimal) {
        this.impl.write_fixed(bigDecimal);
    }

    @Override // org.omg.CORBA.portable.OutputStream
    public final void write_Context(Context context, ContextList contextList) {
        this.impl.write_Context(context, contextList);
    }

    @Override // org.omg.CORBA.portable.OutputStream
    public final org.omg.CORBA.ORB orb() {
        return this.impl.orb();
    }

    @Override // org.omg.CORBA_2_3.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream
    public final void write_value(Serializable serializable) {
        this.impl.write_value(serializable);
    }

    @Override // org.omg.CORBA_2_3.portable.OutputStream
    public final void write_value(Serializable serializable, Class cls) {
        this.impl.write_value(serializable, cls);
    }

    @Override // org.omg.CORBA_2_3.portable.OutputStream
    public final void write_value(Serializable serializable, String str) {
        this.impl.write_value(serializable, str);
    }

    @Override // org.omg.CORBA_2_3.portable.OutputStream
    public final void write_value(Serializable serializable, BoxedValueHelper boxedValueHelper) {
        this.impl.write_value(serializable, boxedValueHelper);
    }

    @Override // org.omg.CORBA_2_3.portable.OutputStream
    public final void write_abstract_interface(Object obj) {
        this.impl.write_abstract_interface(obj);
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr) throws IOException {
        this.impl.write(bArr);
    }

    @Override // java.io.OutputStream
    public final void write(byte[] bArr, int i2, int i3) throws IOException {
        this.impl.write(bArr, i2, i3);
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public final void flush() throws IOException {
        this.impl.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.impl.close();
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalOutputStream
    public final void start_block() {
        this.impl.start_block();
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalOutputStream
    public final void end_block() {
        this.impl.end_block();
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalOutputStream
    public final void putEndian() {
        this.impl.putEndian();
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalOutputStream
    public void writeTo(java.io.OutputStream outputStream) throws IOException {
        this.impl.writeTo(outputStream);
    }

    @Override // com.sun.corba.se.impl.encoding.MarshalOutputStream
    public final byte[] toByteArray() {
        return this.impl.toByteArray();
    }

    @Override // org.omg.CORBA.DataOutputStream
    public final void write_Abstract(Object obj) {
        this.impl.write_Abstract(obj);
    }

    @Override // org.omg.CORBA.DataOutputStream
    public final void write_Value(Serializable serializable) {
        this.impl.write_Value(serializable);
    }

    @Override // org.omg.CORBA.DataOutputStream
    public final void write_any_array(Any[] anyArr, int i2, int i3) {
        this.impl.write_any_array(anyArr, i2, i3);
    }

    public void setMessageMediator(MessageMediator messageMediator) {
        this.corbaMessageMediator = (CorbaMessageMediator) messageMediator;
    }

    public MessageMediator getMessageMediator() {
        return this.corbaMessageMediator;
    }

    @Override // org.omg.CORBA.portable.ValueBase
    public final String[] _truncatable_ids() {
        return this.impl._truncatable_ids();
    }

    protected final int getSize() {
        return this.impl.getSize();
    }

    protected final int getIndex() {
        return this.impl.getIndex();
    }

    protected int getRealIndex(int i2) {
        return i2;
    }

    protected final void setIndex(int i2) {
        this.impl.setIndex(i2);
    }

    protected final ByteBuffer getByteBuffer() {
        return this.impl.getByteBuffer();
    }

    protected final void setByteBuffer(ByteBuffer byteBuffer) {
        this.impl.setByteBuffer(byteBuffer);
    }

    protected final boolean isSharing(ByteBuffer byteBuffer) {
        return getByteBuffer() == byteBuffer;
    }

    public final boolean isLittleEndian() {
        return this.impl.isLittleEndian();
    }

    public ByteBufferWithInfo getByteBufferWithInfo() {
        return this.impl.getByteBufferWithInfo();
    }

    protected void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        this.impl.setByteBufferWithInfo(byteBufferWithInfo);
    }

    public final BufferManagerWrite getBufferManager() {
        return this.impl.getBufferManager();
    }

    public final void write_fixed(BigDecimal bigDecimal, short s2, short s3) {
        this.impl.write_fixed(bigDecimal, s2, s3);
    }

    public final void writeOctetSequenceTo(org.omg.CORBA.portable.OutputStream outputStream) {
        this.impl.writeOctetSequenceTo(outputStream);
    }

    public final GIOPVersion getGIOPVersion() {
        return this.impl.getGIOPVersion();
    }

    public final void writeIndirection(int i2, int i3) {
        this.impl.writeIndirection(i2, i3);
    }

    protected CodeSetConversion.CTBConverter createCharCTBConverter() {
        return CodeSetConversion.impl().getCTBConverter(OSFCodeSetRegistry.ISO_8859_1);
    }

    protected final void freeInternalCaches() {
        this.impl.freeInternalCaches();
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

    @Override // org.omg.CORBA.portable.ValueOutputStream
    public void start_value(String str) {
        this.impl.start_value(str);
    }

    @Override // org.omg.CORBA.portable.ValueOutputStream
    public void end_value() {
        this.impl.end_value();
    }
}
