package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.rmi.Remote;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.omg.CORBA.Any;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.BoxedValueHelper;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/IDLJavaSerializationOutputStream.class */
final class IDLJavaSerializationOutputStream extends CDROutputStreamBase {
    private ORB orb;
    private byte encodingVersion;
    private ObjectOutputStream os;
    private _ByteArrayOutputStream bos;
    private BufferManagerWrite bufferManager;
    private final int directWriteLength = 16;
    protected ORBUtilSystemException wrapper;

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/IDLJavaSerializationOutputStream$_ByteArrayOutputStream.class */
    class _ByteArrayOutputStream extends ByteArrayOutputStream {
        _ByteArrayOutputStream(int i2) {
            super(i2);
        }

        byte[] getByteArray() {
            return this.buf;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/IDLJavaSerializationOutputStream$MarshalObjectOutputStream.class */
    class MarshalObjectOutputStream extends ObjectOutputStream {
        ORB orb;

        MarshalObjectOutputStream(OutputStream outputStream, ORB orb) throws IOException {
            super(outputStream);
            this.orb = orb;
            AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.encoding.IDLJavaSerializationOutputStream.MarshalObjectOutputStream.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    MarshalObjectOutputStream.this.enableReplaceObject(true);
                    return null;
                }
            });
        }

        @Override // java.io.ObjectOutputStream
        protected final Object replaceObject(Object obj) throws IOException {
            try {
                if ((obj instanceof Remote) && !StubAdapter.isStub(obj)) {
                    return Utility.autoConnect(obj, this.orb, true);
                }
                return obj;
            } catch (Exception e2) {
                IOException iOException = new IOException("replaceObject failed");
                iOException.initCause(e2);
                throw iOException;
            }
        }
    }

    public IDLJavaSerializationOutputStream(byte b2) {
        this.encodingVersion = b2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void init(org.omg.CORBA.ORB orb, boolean z2, BufferManagerWrite bufferManagerWrite, byte b2, boolean z3) {
        this.orb = (ORB) orb;
        this.bufferManager = bufferManagerWrite;
        this.wrapper = ORBUtilSystemException.get((ORB) orb, CORBALogDomains.RPC_ENCODING);
        this.bos = new _ByteArrayOutputStream(1024);
    }

    private void initObjectOutputStream() {
        if (this.os != null) {
            throw this.wrapper.javaStreamInitFailed();
        }
        try {
            this.os = new MarshalObjectOutputStream(this.bos, this.orb);
        } catch (Exception e2) {
            throw this.wrapper.javaStreamInitFailed(e2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_boolean(boolean z2) {
        try {
            this.os.writeBoolean(z2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_boolean");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_char(char c2) {
        try {
            this.os.writeChar(c2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_char");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_wchar(char c2) {
        write_char(c2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_octet(byte b2) {
        if (this.bos.size() < 16) {
            this.bos.write(b2);
            if (this.bos.size() == 16) {
                initObjectOutputStream();
                return;
            }
            return;
        }
        try {
            this.os.writeByte(b2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_octet");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_short(short s2) {
        try {
            this.os.writeShort(s2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_short");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ushort(short s2) {
        write_short(s2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_long(int i2) {
        if (this.bos.size() < 16) {
            this.bos.write((byte) ((i2 >>> 24) & 255));
            this.bos.write((byte) ((i2 >>> 16) & 255));
            this.bos.write((byte) ((i2 >>> 8) & 255));
            this.bos.write((byte) ((i2 >>> 0) & 255));
            if (this.bos.size() == 16) {
                initObjectOutputStream();
                return;
            } else {
                if (this.bos.size() > 16) {
                    this.wrapper.javaSerializationException("write_long");
                    return;
                }
                return;
            }
        }
        try {
            this.os.writeInt(i2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_long");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulong(int i2) {
        write_long(i2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_longlong(long j2) {
        try {
            this.os.writeLong(j2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_longlong");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulonglong(long j2) {
        write_longlong(j2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_float(float f2) {
        try {
            this.os.writeFloat(f2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_float");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_double(double d2) {
        try {
            this.os.writeDouble(d2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_double");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_string(String str) {
        try {
            this.os.writeUTF(str);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_string");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_wstring(String str) {
        try {
            this.os.writeObject(str);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_wstring");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_boolean_array(boolean[] zArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_boolean(zArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_char_array(char[] cArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_char(cArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_wchar_array(char[] cArr, int i2, int i3) {
        write_char_array(cArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_octet_array(byte[] bArr, int i2, int i3) {
        try {
            this.os.write(bArr, i2, i3);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_octet_array");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_short_array(short[] sArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_short(sArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ushort_array(short[] sArr, int i2, int i3) {
        write_short_array(sArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_long_array(int[] iArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_long(iArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulong_array(int[] iArr, int i2, int i3) {
        write_long_array(iArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_longlong_array(long[] jArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_longlong(jArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulonglong_array(long[] jArr, int i2, int i3) {
        write_longlong_array(jArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_float_array(float[] fArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_float(fArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_double_array(double[] dArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_double(dArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_Object(Object object) {
        if (object == null) {
            IORFactories.makeIOR(this.orb).write(this.parent);
        } else {
            if (object instanceof LocalObject) {
                throw this.wrapper.writeLocalObject(CompletionStatus.COMPLETED_MAYBE);
            }
            ORBUtility.connectAndGetIOR(this.orb, object).write(this.parent);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_TypeCode(TypeCode typeCode) {
        TypeCodeImpl typeCodeImpl;
        if (typeCode == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        if (typeCode instanceof TypeCodeImpl) {
            typeCodeImpl = (TypeCodeImpl) typeCode;
        } else {
            typeCodeImpl = new TypeCodeImpl(this.orb, typeCode);
        }
        typeCodeImpl.write_value(this.parent);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_any(Any any) {
        if (any == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        write_TypeCode(any.type());
        any.write_value(this.parent);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_Principal(Principal principal) {
        write_long(principal.name().length);
        write_octet_array(principal.name(), 0, principal.name().length);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_fixed(BigDecimal bigDecimal) {
        write_fixed(bigDecimal.toString(), bigDecimal.signum());
    }

    private void write_fixed(String str, int i2) {
        byte b2;
        int length = str.length();
        byte b3 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            char cCharAt = str.charAt(i4);
            if (cCharAt != '-' && cCharAt != '+' && cCharAt != '.') {
                i3++;
            }
        }
        for (int i5 = 0; i5 < length; i5++) {
            char cCharAt2 = str.charAt(i5);
            if (cCharAt2 != '-' && cCharAt2 != '+' && cCharAt2 != '.') {
                byte bDigit = (byte) Character.digit(cCharAt2, 10);
                if (bDigit == -1) {
                    throw this.wrapper.badDigitInFixed(CompletionStatus.COMPLETED_MAYBE);
                }
                if (i3 % 2 == 0) {
                    write_octet((byte) (b3 | bDigit));
                    b3 = 0;
                } else {
                    b3 = (byte) (b3 | (bDigit << 4));
                }
                i3--;
            }
        }
        if (i2 == -1) {
            b2 = (byte) (b3 | 13);
        } else {
            b2 = (byte) (b3 | 12);
        }
        write_octet(b2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final org.omg.CORBA.ORB orb() {
        return this.orb;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_value(Serializable serializable) {
        write_value(serializable, (String) null);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_value(Serializable serializable, Class cls) {
        write_value(serializable);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_value(Serializable serializable, String str) {
        try {
            this.os.writeObject(serializable);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_value");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_value(Serializable serializable, BoxedValueHelper boxedValueHelper) {
        write_value(serializable, (String) null);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_abstract_interface(Object obj) {
        boolean z2 = false;
        Object object = null;
        if (obj != null && (obj instanceof Object)) {
            object = (Object) obj;
            z2 = true;
        }
        write_boolean(z2);
        if (z2) {
            write_Object(object);
            return;
        }
        try {
            write_value((Serializable) obj);
        } catch (ClassCastException e2) {
            if (obj instanceof Serializable) {
                throw e2;
            }
            ORBUtility.throwNotSerializableForCorba(obj.getClass().getName());
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void start_block() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void end_block() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void putEndian() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void writeTo(OutputStream outputStream) throws IOException {
        try {
            this.os.flush();
            this.bos.writeTo(outputStream);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "writeTo");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final byte[] toByteArray() {
        try {
            this.os.flush();
            return this.bos.toByteArray();
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "toByteArray");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_Abstract(Object obj) {
        write_abstract_interface(obj);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_Value(Serializable serializable) {
        write_value(serializable);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_any_array(Any[] anyArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_any(anyArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final String[] _truncatable_ids() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final int getSize() {
        try {
            this.os.flush();
            return this.bos.size();
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "write_boolean");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final int getIndex() {
        return getSize();
    }

    protected int getRealIndex(int i2) {
        return getSize();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void setIndex(int i2) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final ByteBuffer getByteBuffer() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void setByteBuffer(ByteBuffer byteBuffer) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final boolean isLittleEndian() {
        return false;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public ByteBufferWithInfo getByteBufferWithInfo() {
        try {
            this.os.flush();
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(this.bos.getByteArray());
            byteBufferWrap.limit(this.bos.size());
            return new ByteBufferWithInfo(this.orb, byteBufferWrap, this.bos.size());
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "getByteBufferWithInfo");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final BufferManagerWrite getBufferManager() {
        return this.bufferManager;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_fixed(BigDecimal bigDecimal, short s2, short s3) {
        String strSubstring;
        String strSubstring2;
        String string = bigDecimal.toString();
        if (string.charAt(0) == '-' || string.charAt(0) == '+') {
            string = string.substring(1);
        }
        int iIndexOf = string.indexOf(46);
        if (iIndexOf == -1) {
            strSubstring = string;
            strSubstring2 = null;
        } else if (iIndexOf == 0) {
            strSubstring = null;
            strSubstring2 = string;
        } else {
            strSubstring = string.substring(0, iIndexOf);
            strSubstring2 = string.substring(iIndexOf + 1);
        }
        StringBuffer stringBuffer = new StringBuffer(s2);
        if (strSubstring2 != null) {
            stringBuffer.append(strSubstring2);
        }
        while (stringBuffer.length() < s3) {
            stringBuffer.append('0');
        }
        if (strSubstring != null) {
            stringBuffer.insert(0, strSubstring);
        }
        while (stringBuffer.length() < s2) {
            stringBuffer.insert(0, '0');
        }
        write_fixed(stringBuffer.toString(), bigDecimal.signum());
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void writeOctetSequenceTo(org.omg.CORBA.portable.OutputStream outputStream) {
        byte[] byteArray = toByteArray();
        outputStream.write_long(byteArray.length);
        outputStream.write_octet_array(byteArray, 0, byteArray.length);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void writeIndirection(int i2, int i3) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    void freeInternalCaches() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v42, types: [int] */
    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    void printBuffer() {
        byte[] byteArray = toByteArray();
        System.out.println("+++++++ Output Buffer ++++++++");
        System.out.println();
        System.out.println("Current position: " + byteArray.length);
        System.out.println();
        char[] cArr = new char[16];
        for (int i2 = 0; i2 < byteArray.length; i2 += 16) {
            try {
                int i3 = 0;
                while (i3 < 16 && i3 + i2 < byteArray.length) {
                    byte b2 = byteArray[i2 + i3];
                    if (b2 < 0) {
                        b2 = 256 + b2;
                    }
                    String hexString = Integer.toHexString(b2);
                    if (hexString.length() == 1) {
                        hexString = "0" + hexString;
                    }
                    System.out.print(hexString + " ");
                    i3++;
                }
                while (i3 < 16) {
                    System.out.print("   ");
                    i3++;
                }
                int i4 = 0;
                while (i4 < 16 && i4 + i2 < byteArray.length) {
                    if (ORBUtility.isPrintable((char) byteArray[i2 + i4])) {
                        cArr[i4] = (char) byteArray[i2 + i4];
                    } else {
                        cArr[i4] = '.';
                    }
                    i4++;
                }
                System.out.println(new String(cArr, 0, i4));
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        System.out.println("++++++++++++++++++++++++++++++");
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void alignOnBoundary(int i2) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void setHeaderPadding(boolean z2) {
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void start_value(String str) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void end_value() {
        throw this.wrapper.giopVersionError();
    }
}
