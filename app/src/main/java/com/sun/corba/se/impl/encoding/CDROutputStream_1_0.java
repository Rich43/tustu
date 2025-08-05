package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.io.TypeMismatchException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.CacheTable;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.RepositoryIdFactory;
import com.sun.corba.se.impl.orbutil.RepositoryIdStrings;
import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.org.omg.CORBA.portable.ValueHelper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandler;
import javax.rmi.CORBA.ValueHandlerMultiFormat;
import org.omg.CORBA.Any;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.CustomMarshal;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.portable.BoxedValueHelper;
import org.omg.CORBA.portable.CustomValue;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.StreamableValue;
import org.omg.CORBA.portable.ValueBase;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDROutputStream_1_0.class */
public class CDROutputStream_1_0 extends CDROutputStreamBase {
    private static final int INDIRECTION_TAG = -1;
    protected boolean littleEndian;
    protected BufferManagerWrite bufferManagerWrite;
    ByteBufferWithInfo bbwi;
    protected ORB orb;
    protected ORBUtilSystemException wrapper;
    protected byte streamFormatVersion;
    private static final int DEFAULT_BUFFER_SIZE = 1024;
    private static final String kWriteMethod = "write";
    private RepositoryIdUtility repIdUtil;
    private RepositoryIdStrings repIdStrs;
    private CodeSetConversion.CTBConverter charConverter;
    private CodeSetConversion.CTBConverter wcharConverter;
    private static final String _id = "IDL:omg.org/CORBA/DataOutputStream:1.0";
    private static final String[] _ids = {_id};
    protected boolean debug = false;
    protected int blockSizeIndex = -1;
    protected int blockSizePosition = 0;
    private CacheTable codebaseCache = null;
    private CacheTable valueCache = null;
    private CacheTable repositoryIdCache = null;
    private int end_flag = 0;
    private int chunkedValueNestingLevel = 0;
    private boolean mustChunk = false;
    protected boolean inBlock = false;
    private int end_flag_position = 0;
    private int end_flag_index = 0;
    private ValueHandler valueHandler = null;

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public /* bridge */ /* synthetic */ void write_Context(Context context, ContextList contextList) {
        super.write_Context(context, contextList);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase, java.io.OutputStream
    public /* bridge */ /* synthetic */ void write(int i2) throws IOException {
        super.write(i2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public /* bridge */ /* synthetic */ void init(org.omg.CORBA.ORB orb, BufferManagerWrite bufferManagerWrite, byte b2) {
        super.init(orb, bufferManagerWrite, b2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public /* bridge */ /* synthetic */ void setParent(CDROutputStream cDROutputStream) {
        super.setParent(cDROutputStream);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void init(org.omg.CORBA.ORB orb, boolean z2, BufferManagerWrite bufferManagerWrite, byte b2, boolean z3) {
        this.orb = (ORB) orb;
        this.wrapper = ORBUtilSystemException.get(this.orb, CORBALogDomains.RPC_ENCODING);
        this.debug = this.orb.transportDebugFlag;
        this.littleEndian = z2;
        this.bufferManagerWrite = bufferManagerWrite;
        this.bbwi = new ByteBufferWithInfo(orb, bufferManagerWrite, z3);
        this.streamFormatVersion = b2;
        createRepositoryIdHandlers();
    }

    public void init(org.omg.CORBA.ORB orb, boolean z2, BufferManagerWrite bufferManagerWrite, byte b2) {
        init(orb, z2, bufferManagerWrite, b2, true);
    }

    private final void createRepositoryIdHandlers() {
        this.repIdUtil = RepositoryIdFactory.getRepIdUtility();
        this.repIdStrs = RepositoryIdFactory.getRepIdStringsFactory();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public BufferManagerWrite getBufferManager() {
        return this.bufferManagerWrite;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public byte[] toByteArray() {
        byte[] bArr = new byte[this.bbwi.position()];
        for (int i2 = 0; i2 < this.bbwi.position(); i2++) {
            bArr[i2] = this.bbwi.byteBuffer.get(i2);
        }
        return bArr;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_0;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    void setHeaderPadding(boolean z2) {
        throw this.wrapper.giopVersionError();
    }

    protected void handleSpecialChunkBegin(int i2) {
    }

    protected void handleSpecialChunkEnd() {
    }

    protected final int computeAlignment(int i2) {
        int iPosition;
        if (i2 > 1 && (iPosition = this.bbwi.position() & (i2 - 1)) != 0) {
            return i2 - iPosition;
        }
        return 0;
    }

    protected void alignAndReserve(int i2, int i3) {
        this.bbwi.position(this.bbwi.position() + computeAlignment(i2));
        if (this.bbwi.position() + i3 > this.bbwi.buflen) {
            grow(i2, i3);
        }
    }

    protected void grow(int i2, int i3) {
        this.bbwi.needed = i3;
        this.bufferManagerWrite.overflow(this.bbwi);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void putEndian() throws SystemException {
        write_boolean(this.littleEndian);
    }

    public final boolean littleEndian() {
        return this.littleEndian;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    void freeInternalCaches() {
        if (this.codebaseCache != null) {
            this.codebaseCache.done();
        }
        if (this.valueCache != null) {
            this.valueCache.done();
        }
        if (this.repositoryIdCache != null) {
            this.repositoryIdCache.done();
        }
    }

    public final void write_longdouble(double d2) {
        throw this.wrapper.longDoubleNotImplemented(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_octet(byte b2) {
        alignAndReserve(1, 1);
        this.bbwi.byteBuffer.put(this.bbwi.position(), b2);
        this.bbwi.position(this.bbwi.position() + 1);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_boolean(boolean z2) {
        write_octet(z2 ? (byte) 1 : (byte) 0);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_char(char c2) {
        CodeSetConversion.CTBConverter charConverter = getCharConverter();
        charConverter.convert(c2);
        if (charConverter.getNumBytes() > 1) {
            throw this.wrapper.invalidSingleCharCtb(CompletionStatus.COMPLETED_MAYBE);
        }
        write_octet(charConverter.getBytes()[0]);
    }

    private final void writeLittleEndianWchar(char c2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) (c2 & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) ((c2 >>> '\b') & 255));
        this.bbwi.position(this.bbwi.position() + 2);
    }

    private final void writeBigEndianWchar(char c2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) ((c2 >>> '\b') & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) (c2 & 255));
        this.bbwi.position(this.bbwi.position() + 2);
    }

    private final void writeLittleEndianShort(short s2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) (s2 & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) ((s2 >>> 8) & 255));
        this.bbwi.position(this.bbwi.position() + 2);
    }

    private final void writeBigEndianShort(short s2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) ((s2 >>> 8) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) (s2 & 255));
        this.bbwi.position(this.bbwi.position() + 2);
    }

    private final void writeLittleEndianLong(int i2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) (i2 & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) ((i2 >>> 8) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 2, (byte) ((i2 >>> 16) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 3, (byte) ((i2 >>> 24) & 255));
        this.bbwi.position(this.bbwi.position() + 4);
    }

    private final void writeBigEndianLong(int i2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) ((i2 >>> 24) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) ((i2 >>> 16) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 2, (byte) ((i2 >>> 8) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 3, (byte) (i2 & 255));
        this.bbwi.position(this.bbwi.position() + 4);
    }

    private final void writeLittleEndianLongLong(long j2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) (j2 & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) ((j2 >>> 8) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 2, (byte) ((j2 >>> 16) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 3, (byte) ((j2 >>> 24) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 4, (byte) ((j2 >>> 32) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 5, (byte) ((j2 >>> 40) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 6, (byte) ((j2 >>> 48) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 7, (byte) ((j2 >>> 56) & 255));
        this.bbwi.position(this.bbwi.position() + 8);
    }

    private final void writeBigEndianLongLong(long j2) {
        this.bbwi.byteBuffer.put(this.bbwi.position(), (byte) ((j2 >>> 56) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 1, (byte) ((j2 >>> 48) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 2, (byte) ((j2 >>> 40) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 3, (byte) ((j2 >>> 32) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 4, (byte) ((j2 >>> 24) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 5, (byte) ((j2 >>> 16) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 6, (byte) ((j2 >>> 8) & 255));
        this.bbwi.byteBuffer.put(this.bbwi.position() + 7, (byte) (j2 & 255));
        this.bbwi.position(this.bbwi.position() + 8);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wchar(char c2) {
        if (ORBUtility.isForeignORB(this.orb)) {
            throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
        }
        alignAndReserve(2, 2);
        if (this.littleEndian) {
            writeLittleEndianWchar(c2);
        } else {
            writeBigEndianWchar(c2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_short(short s2) {
        alignAndReserve(2, 2);
        if (this.littleEndian) {
            writeLittleEndianShort(s2);
        } else {
            writeBigEndianShort(s2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ushort(short s2) {
        write_short(s2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_long(int i2) {
        alignAndReserve(4, 4);
        if (this.littleEndian) {
            writeLittleEndianLong(i2);
        } else {
            writeBigEndianLong(i2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulong(int i2) {
        write_long(i2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_longlong(long j2) {
        alignAndReserve(8, 8);
        if (this.littleEndian) {
            writeLittleEndianLongLong(j2);
        } else {
            writeBigEndianLongLong(j2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulonglong(long j2) {
        write_longlong(j2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_float(float f2) {
        write_long(Float.floatToIntBits(f2));
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_double(double d2) {
        write_longlong(Double.doubleToLongBits(d2));
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_string(String str) {
        writeString(str);
    }

    protected int writeString(String str) {
        if (str == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        CodeSetConversion.CTBConverter charConverter = getCharConverter();
        charConverter.convert(str);
        int numBytes = charConverter.getNumBytes() + 1;
        handleSpecialChunkBegin(computeAlignment(4) + 4 + numBytes);
        write_long(numBytes);
        int i2 = get_offset() - 4;
        internalWriteOctetArray(charConverter.getBytes(), 0, charConverter.getNumBytes());
        write_octet((byte) 0);
        handleSpecialChunkEnd();
        return i2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wstring(String str) {
        if (str == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        if (ORBUtility.isForeignORB(this.orb)) {
            throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
        }
        int length = str.length() + 1;
        handleSpecialChunkBegin(4 + (length * 2) + computeAlignment(4));
        write_long(length);
        for (int i2 = 0; i2 < length - 1; i2++) {
            write_wchar(str.charAt(i2));
        }
        write_short((short) 0);
        handleSpecialChunkEnd();
    }

    void internalWriteOctetArray(byte[] bArr, int i2, int i3) {
        int i4 = i2;
        boolean z2 = true;
        while (i4 < i3 + i2) {
            if (this.bbwi.position() + 1 > this.bbwi.buflen || z2) {
                z2 = false;
                alignAndReserve(1, 1);
            }
            int iPosition = this.bbwi.buflen - this.bbwi.position();
            int i5 = (i3 + i2) - i4;
            int i6 = i5 < iPosition ? i5 : iPosition;
            for (int i7 = 0; i7 < i6; i7++) {
                this.bbwi.byteBuffer.put(this.bbwi.position() + i7, bArr[i4 + i7]);
            }
            this.bbwi.position(this.bbwi.position() + i6);
            i4 += i6;
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_octet_array(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(i3);
        internalWriteOctetArray(bArr, i2, i3);
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_Principal(Principal principal) {
        write_long(principal.name().length);
        write_octet_array(principal.name(), 0, principal.name().length);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_any(Any any) {
        if (any == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        write_TypeCode(any.type());
        any.write_value(this.parent);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_TypeCode(TypeCode typeCode) {
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
    public void write_Object(Object object) {
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
    public void write_abstract_interface(Object obj) throws NoSuchMethodException, TypeMismatchException, IllegalArgumentException {
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
    public void write_value(Serializable serializable, Class cls) throws NoSuchMethodException, TypeMismatchException, IllegalArgumentException {
        write_value(serializable);
    }

    private void writeWStringValue(String str) {
        int iWriteValueTag = writeValueTag(this.mustChunk, true, null);
        write_repositoryId(this.repIdStrs.getWStringValueRepId());
        updateIndirectionTable(iWriteValueTag, str, str);
        if (this.mustChunk) {
            start_block();
            this.end_flag--;
            this.chunkedValueNestingLevel--;
        } else {
            this.end_flag--;
        }
        write_wstring(str);
        if (this.mustChunk) {
            end_block();
        }
        writeEndTag(this.mustChunk);
    }

    private void writeArray(Serializable serializable, Class cls) {
        if (this.valueHandler == null) {
            this.valueHandler = ORBUtility.createValueHandler();
        }
        int iWriteValueTag = writeValueTag(this.mustChunk, true, Util.getCodebase(cls));
        write_repositoryId(this.repIdStrs.createSequenceRepID(cls));
        updateIndirectionTable(iWriteValueTag, serializable, serializable);
        if (this.mustChunk) {
            start_block();
            this.end_flag--;
            this.chunkedValueNestingLevel--;
        } else {
            this.end_flag--;
        }
        if (this.valueHandler instanceof ValueHandlerMultiFormat) {
            ((ValueHandlerMultiFormat) this.valueHandler).writeValue(this.parent, serializable, this.streamFormatVersion);
        } else {
            this.valueHandler.writeValue(this.parent, serializable);
        }
        if (this.mustChunk) {
            end_block();
        }
        writeEndTag(this.mustChunk);
    }

    private void writeValueBase(ValueBase valueBase, Class cls) {
        this.mustChunk = true;
        int iWriteValueTag = writeValueTag(true, true, Util.getCodebase(cls));
        String str = valueBase._truncatable_ids()[0];
        write_repositoryId(str);
        updateIndirectionTable(iWriteValueTag, valueBase, valueBase);
        start_block();
        this.end_flag--;
        this.chunkedValueNestingLevel--;
        writeIDLValue(valueBase, str);
        end_block();
        writeEndTag(true);
    }

    private void writeRMIIIOPValueType(Serializable serializable, Class cls) {
        if (this.valueHandler == null) {
            this.valueHandler = ORBUtility.createValueHandler();
        }
        Serializable serializableWriteReplace = this.valueHandler.writeReplace(serializable);
        if (serializableWriteReplace == null) {
            write_long(0);
            return;
        }
        if (serializableWriteReplace != serializable) {
            if (this.valueCache != null && this.valueCache.containsKey(serializableWriteReplace)) {
                writeIndirection(-1, this.valueCache.getVal(serializableWriteReplace));
                return;
            }
            cls = serializableWriteReplace.getClass();
        }
        if (this.mustChunk || this.valueHandler.isCustomMarshaled(cls)) {
            this.mustChunk = true;
        }
        int iWriteValueTag = writeValueTag(this.mustChunk, true, Util.getCodebase(cls));
        write_repositoryId(this.repIdStrs.createForJavaType(cls));
        updateIndirectionTable(iWriteValueTag, serializableWriteReplace, serializable);
        if (this.mustChunk) {
            this.end_flag--;
            this.chunkedValueNestingLevel--;
            start_block();
        } else {
            this.end_flag--;
        }
        if (this.valueHandler instanceof ValueHandlerMultiFormat) {
            ((ValueHandlerMultiFormat) this.valueHandler).writeValue(this.parent, serializableWriteReplace, this.streamFormatVersion);
        } else {
            this.valueHandler.writeValue(this.parent, serializableWriteReplace);
        }
        if (this.mustChunk) {
            end_block();
        }
        writeEndTag(this.mustChunk);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_value(Serializable serializable, String str) throws NoSuchMethodException, TypeMismatchException, IllegalArgumentException {
        if (serializable == null) {
            write_long(0);
            return;
        }
        if (this.valueCache != null && this.valueCache.containsKey(serializable)) {
            writeIndirection(-1, this.valueCache.getVal(serializable));
            return;
        }
        Class<?> cls = serializable.getClass();
        boolean z2 = this.mustChunk;
        if (this.mustChunk) {
            this.mustChunk = true;
        }
        if (this.inBlock) {
            end_block();
        }
        if (cls.isArray()) {
            writeArray(serializable, cls);
        } else if (serializable instanceof ValueBase) {
            writeValueBase((ValueBase) serializable, cls);
        } else if (shouldWriteAsIDLEntity(serializable)) {
            writeIDLEntity((IDLEntity) serializable);
        } else if (serializable instanceof String) {
            writeWStringValue((String) serializable);
        } else if (serializable instanceof Class) {
            writeClass(str, (Class) serializable);
        } else {
            writeRMIIIOPValueType(serializable, cls);
        }
        this.mustChunk = z2;
        if (this.mustChunk) {
            start_block();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_value(Serializable serializable) throws NoSuchMethodException, TypeMismatchException, IllegalArgumentException {
        write_value(serializable, (String) null);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_value(Serializable serializable, BoxedValueHelper boxedValueHelper) {
        short sType_modifier;
        if (serializable == null) {
            write_long(0);
            return;
        }
        if (this.valueCache != null && this.valueCache.containsKey(serializable)) {
            writeIndirection(-1, this.valueCache.getVal(serializable));
            return;
        }
        boolean z2 = this.mustChunk;
        boolean z3 = false;
        if (boxedValueHelper instanceof ValueHelper) {
            try {
                sType_modifier = ((ValueHelper) boxedValueHelper).get_type().type_modifier();
            } catch (BadKind e2) {
                sType_modifier = 0;
            }
            if ((serializable instanceof CustomMarshal) && sType_modifier == 1) {
                z3 = true;
                this.mustChunk = true;
            }
            if (sType_modifier == 3) {
                this.mustChunk = true;
            }
        }
        if (this.mustChunk) {
            if (this.inBlock) {
                end_block();
            }
            int iWriteValueTag = writeValueTag(true, this.orb.getORBData().useRepId(), Util.getCodebase(serializable.getClass()));
            if (this.orb.getORBData().useRepId()) {
                write_repositoryId(boxedValueHelper.get_id());
            }
            updateIndirectionTable(iWriteValueTag, serializable, serializable);
            start_block();
            this.end_flag--;
            this.chunkedValueNestingLevel--;
            if (z3) {
                ((CustomMarshal) serializable).marshal(this.parent);
            } else {
                boxedValueHelper.write_value(this.parent, serializable);
            }
            end_block();
            writeEndTag(true);
        } else {
            int iWriteValueTag2 = writeValueTag(false, this.orb.getORBData().useRepId(), Util.getCodebase(serializable.getClass()));
            if (this.orb.getORBData().useRepId()) {
                write_repositoryId(boxedValueHelper.get_id());
            }
            updateIndirectionTable(iWriteValueTag2, serializable, serializable);
            this.end_flag--;
            boxedValueHelper.write_value(this.parent, serializable);
            writeEndTag(false);
        }
        this.mustChunk = z2;
        if (this.mustChunk) {
            start_block();
        }
    }

    public int get_offset() {
        return this.bbwi.position();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void start_block() {
        if (this.debug) {
            dprint("CDROutputStream_1_0 start_block, position" + this.bbwi.position());
        }
        write_long(0);
        this.inBlock = true;
        this.blockSizePosition = get_offset();
        this.blockSizeIndex = this.bbwi.position();
        if (this.debug) {
            dprint("CDROutputStream_1_0 start_block, blockSizeIndex " + this.blockSizeIndex);
        }
    }

    protected void writeLongWithoutAlign(int i2) {
        if (this.littleEndian) {
            writeLittleEndianLong(i2);
        } else {
            writeBigEndianLong(i2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void end_block() {
        if (this.debug) {
            dprint("CDROutputStream_1_0.java end_block");
        }
        if (!this.inBlock) {
            return;
        }
        if (this.debug) {
            dprint("CDROutputStream_1_0.java end_block, in a block");
        }
        this.inBlock = false;
        if (get_offset() == this.blockSizePosition) {
            this.bbwi.position(this.bbwi.position() - 4);
            this.blockSizeIndex = -1;
            this.blockSizePosition = -1;
        } else {
            int iPosition = this.bbwi.position();
            this.bbwi.position(this.blockSizeIndex - 4);
            writeLongWithoutAlign(iPosition - this.blockSizeIndex);
            this.bbwi.position(iPosition);
            this.blockSizeIndex = -1;
            this.blockSizePosition = -1;
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public org.omg.CORBA.ORB orb() {
        return this.orb;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_boolean_array(boolean[] zArr, int i2, int i3) {
        if (zArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(i3);
        for (int i4 = 0; i4 < i3; i4++) {
            write_boolean(zArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_char_array(char[] cArr, int i2, int i3) {
        if (cArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(i3);
        for (int i4 = 0; i4 < i3; i4++) {
            write_char(cArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_wchar_array(char[] cArr, int i2, int i3) {
        if (cArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(computeAlignment(2) + (i3 * 2));
        for (int i4 = 0; i4 < i3; i4++) {
            write_wchar(cArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_short_array(short[] sArr, int i2, int i3) {
        if (sArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(computeAlignment(2) + (i3 * 2));
        for (int i4 = 0; i4 < i3; i4++) {
            write_short(sArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ushort_array(short[] sArr, int i2, int i3) {
        write_short_array(sArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_long_array(int[] iArr, int i2, int i3) {
        if (iArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(computeAlignment(4) + (i3 * 4));
        for (int i4 = 0; i4 < i3; i4++) {
            write_long(iArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulong_array(int[] iArr, int i2, int i3) {
        write_long_array(iArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_longlong_array(long[] jArr, int i2, int i3) {
        if (jArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(computeAlignment(8) + (i3 * 8));
        for (int i4 = 0; i4 < i3; i4++) {
            write_longlong(jArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_ulonglong_array(long[] jArr, int i2, int i3) {
        write_longlong_array(jArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_float_array(float[] fArr, int i2, int i3) {
        if (fArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(computeAlignment(4) + (i3 * 4));
        for (int i4 = 0; i4 < i3; i4++) {
            write_float(fArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_double_array(double[] dArr, int i2, int i3) {
        if (dArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        handleSpecialChunkBegin(computeAlignment(8) + (i3 * 8));
        for (int i4 = 0; i4 < i3; i4++) {
            write_double(dArr[i2 + i4]);
        }
        handleSpecialChunkEnd();
    }

    public void write_string_array(String[] strArr, int i2, int i3) {
        if (strArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        for (int i4 = 0; i4 < i3; i4++) {
            write_string(strArr[i2 + i4]);
        }
    }

    public void write_wstring_array(String[] strArr, int i2, int i3) {
        if (strArr == null) {
            throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
        }
        for (int i4 = 0; i4 < i3; i4++) {
            write_wstring(strArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final void write_any_array(Any[] anyArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            write_any(anyArr[i2 + i4]);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void writeTo(OutputStream outputStream) throws IOException {
        byte[] bArrArray;
        if (this.bbwi.byteBuffer.hasArray()) {
            bArrArray = this.bbwi.byteBuffer.array();
        } else {
            int iPosition = this.bbwi.position();
            bArrArray = new byte[iPosition];
            for (int i2 = 0; i2 < iPosition; i2++) {
                bArrArray[i2] = this.bbwi.byteBuffer.get(i2);
            }
        }
        outputStream.write(bArrArray, 0, this.bbwi.position());
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void writeOctetSequenceTo(org.omg.CORBA.portable.OutputStream outputStream) {
        byte[] bArrArray;
        if (this.bbwi.byteBuffer.hasArray()) {
            bArrArray = this.bbwi.byteBuffer.array();
        } else {
            int iPosition = this.bbwi.position();
            bArrArray = new byte[iPosition];
            for (int i2 = 0; i2 < iPosition; i2++) {
                bArrArray[i2] = this.bbwi.byteBuffer.get(i2);
            }
        }
        outputStream.write_long(this.bbwi.position());
        outputStream.write_octet_array(bArrArray, 0, this.bbwi.position());
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public final int getSize() {
        return this.bbwi.position();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public int getIndex() {
        return this.bbwi.position();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public boolean isLittleEndian() {
        return this.littleEndian;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void setIndex(int i2) {
        this.bbwi.position(i2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public ByteBufferWithInfo getByteBufferWithInfo() {
        return this.bbwi;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        this.bbwi = byteBufferWithInfo;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = null;
        if (this.bbwi != null) {
            byteBuffer = this.bbwi.byteBuffer;
        }
        return byteBuffer;
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.bbwi.byteBuffer = byteBuffer;
    }

    private final void updateIndirectionTable(int i2, Object obj, Object obj2) {
        if (this.valueCache == null) {
            this.valueCache = new CacheTable(this.orb, true);
        }
        this.valueCache.put(obj, i2);
        if (obj2 != obj) {
            this.valueCache.put(obj2, i2);
        }
    }

    private final void write_repositoryId(String str) {
        if (this.repositoryIdCache != null && this.repositoryIdCache.containsKey(str)) {
            writeIndirection(-1, this.repositoryIdCache.getVal(str));
            return;
        }
        int iWriteString = writeString(str);
        if (this.repositoryIdCache == null) {
            this.repositoryIdCache = new CacheTable(this.orb, true);
        }
        this.repositoryIdCache.put(str, iWriteString);
    }

    private void write_codebase(String str, int i2) {
        if (this.codebaseCache != null && this.codebaseCache.containsKey(str)) {
            writeIndirection(-1, this.codebaseCache.getVal(str));
            return;
        }
        write_string(str);
        if (this.codebaseCache == null) {
            this.codebaseCache = new CacheTable(this.orb, true);
        }
        this.codebaseCache.put(str, i2);
    }

    private final int writeValueTag(boolean z2, boolean z3, String str) {
        int i2 = 0;
        if (z2 && !z3) {
            if (str == null) {
                write_long(this.repIdUtil.getStandardRMIChunkedNoRepStrId());
                i2 = get_offset() - 4;
            } else {
                write_long(this.repIdUtil.getCodeBaseRMIChunkedNoRepStrId());
                i2 = get_offset() - 4;
                write_codebase(str, get_offset());
            }
        } else if (z2 && z3) {
            if (str == null) {
                write_long(this.repIdUtil.getStandardRMIChunkedId());
                i2 = get_offset() - 4;
            } else {
                write_long(this.repIdUtil.getCodeBaseRMIChunkedId());
                i2 = get_offset() - 4;
                write_codebase(str, get_offset());
            }
        } else if (!z2 && !z3) {
            if (str == null) {
                write_long(this.repIdUtil.getStandardRMIUnchunkedNoRepStrId());
                i2 = get_offset() - 4;
            } else {
                write_long(this.repIdUtil.getCodeBaseRMIUnchunkedNoRepStrId());
                i2 = get_offset() - 4;
                write_codebase(str, get_offset());
            }
        } else if (!z2 && z3) {
            if (str == null) {
                write_long(this.repIdUtil.getStandardRMIUnchunkedId());
                i2 = get_offset() - 4;
            } else {
                write_long(this.repIdUtil.getCodeBaseRMIUnchunkedId());
                i2 = get_offset() - 4;
                write_codebase(str, get_offset());
            }
        }
        return i2;
    }

    private void writeIDLValue(Serializable serializable, String str) {
        if (serializable instanceof StreamableValue) {
            ((StreamableValue) serializable)._write(this.parent);
            return;
        }
        if (serializable instanceof CustomValue) {
            ((CustomValue) serializable).marshal(this.parent);
            return;
        }
        BoxedValueHelper helper = Utility.getHelper(serializable.getClass(), null, str);
        boolean z2 = false;
        if ((helper instanceof ValueHelper) && (serializable instanceof CustomMarshal)) {
            try {
                if (((ValueHelper) helper).get_type().type_modifier() == 1) {
                    z2 = true;
                }
            } catch (BadKind e2) {
                throw this.wrapper.badTypecodeForCustomValue(CompletionStatus.COMPLETED_MAYBE, e2);
            }
        }
        if (z2) {
            ((CustomMarshal) serializable).marshal(this.parent);
        } else {
            helper.write_value(this.parent, serializable);
        }
    }

    private void writeEndTag(boolean z2) {
        if (z2) {
            if (get_offset() == this.end_flag_position && this.bbwi.position() == this.end_flag_index) {
                this.bbwi.position(this.bbwi.position() - 4);
            }
            writeNestingLevel();
            this.end_flag_index = this.bbwi.position();
            this.end_flag_position = get_offset();
            this.chunkedValueNestingLevel++;
        }
        this.end_flag++;
    }

    private void writeNestingLevel() {
        if (this.orb == null || ORBVersionFactory.getFOREIGN().equals(this.orb.getORBVersion()) || ORBVersionFactory.getNEWER().compareTo(this.orb.getORBVersion()) <= 0) {
            write_long(this.chunkedValueNestingLevel);
        } else {
            write_long(this.end_flag);
        }
    }

    private void writeClass(String str, Class cls) {
        if (str == null) {
            str = this.repIdStrs.getClassDescValueRepId();
        }
        updateIndirectionTable(writeValueTag(this.mustChunk, true, null), cls, cls);
        write_repositoryId(str);
        if (this.mustChunk) {
            start_block();
            this.end_flag--;
            this.chunkedValueNestingLevel--;
        } else {
            this.end_flag--;
        }
        writeClassBody(cls);
        if (this.mustChunk) {
            end_block();
        }
        writeEndTag(this.mustChunk);
    }

    private void writeClassBody(Class cls) {
        if (this.orb == null || ORBVersionFactory.getFOREIGN().equals(this.orb.getORBVersion()) || ORBVersionFactory.getNEWER().compareTo(this.orb.getORBVersion()) <= 0) {
            write_value(Util.getCodebase(cls));
            write_value(this.repIdStrs.createForAnyType(cls));
        } else {
            write_value(this.repIdStrs.createForAnyType(cls));
            write_value(Util.getCodebase(cls));
        }
    }

    private boolean shouldWriteAsIDLEntity(Serializable serializable) {
        return (!(serializable instanceof IDLEntity) || (serializable instanceof ValueBase) || (serializable instanceof Object)) ? false : true;
    }

    private void writeIDLEntity(IDLEntity iDLEntity) throws NoSuchMethodException, TypeMismatchException, IllegalArgumentException {
        ClassLoader classLoader;
        this.mustChunk = true;
        String strCreateForJavaType = this.repIdStrs.createForJavaType(iDLEntity);
        Class<?> cls = iDLEntity.getClass();
        String codebase = Util.getCodebase(cls);
        updateIndirectionTable(writeValueTag(true, true, codebase), iDLEntity, iDLEntity);
        write_repositoryId(strCreateForJavaType);
        this.end_flag--;
        this.chunkedValueNestingLevel--;
        start_block();
        if (cls == null) {
            classLoader = null;
        } else {
            try {
                classLoader = cls.getClassLoader();
            } catch (ClassNotFoundException e2) {
                throw this.wrapper.errorInvokingHelperWrite(CompletionStatus.COMPLETED_MAYBE, e2);
            } catch (IllegalAccessException e3) {
                throw this.wrapper.errorInvokingHelperWrite(CompletionStatus.COMPLETED_MAYBE, e3);
            } catch (NoSuchMethodException e4) {
                throw this.wrapper.errorInvokingHelperWrite(CompletionStatus.COMPLETED_MAYBE, e4);
            } catch (InvocationTargetException e5) {
                throw this.wrapper.errorInvokingHelperWrite(CompletionStatus.COMPLETED_MAYBE, e5);
            }
        }
        ClassLoader classLoader2 = classLoader;
        final Class clsLoadClassForClass = Utility.loadClassForClass(cls.getName() + "Helper", codebase, classLoader2, cls, classLoader2);
        final Class[] clsArr = {org.omg.CORBA.portable.OutputStream.class, cls};
        try {
            ((Method) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.impl.encoding.CDROutputStream_1_0.1
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws NoSuchMethodException {
                    return clsLoadClassForClass.getDeclaredMethod("write", clsArr);
                }
            })).invoke(null, this.parent, iDLEntity);
            end_block();
            writeEndTag(true);
        } catch (PrivilegedActionException e6) {
            throw ((NoSuchMethodException) e6.getException());
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_Abstract(Object obj) throws NoSuchMethodException, TypeMismatchException, IllegalArgumentException {
        write_abstract_interface(obj);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_Value(Serializable serializable) throws NoSuchMethodException, TypeMismatchException, IllegalArgumentException {
        write_value(serializable);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void write_fixed(BigDecimal bigDecimal, short s2, short s3) {
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
    public void write_fixed(BigDecimal bigDecimal) {
        write_fixed(bigDecimal.toString(), bigDecimal.signum());
    }

    public void write_fixed(String str, int i2) {
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
    public String[] _truncatable_ids() {
        if (_ids == null) {
            return null;
        }
        return (String[]) _ids.clone();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void printBuffer() {
        printBuffer(this.bbwi);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v43, types: [int] */
    public static void printBuffer(ByteBufferWithInfo byteBufferWithInfo) {
        System.out.println("+++++++ Output Buffer ++++++++");
        System.out.println();
        System.out.println("Current position: " + byteBufferWithInfo.position());
        System.out.println("Total length : " + byteBufferWithInfo.buflen);
        System.out.println();
        char[] cArr = new char[16];
        for (int i2 = 0; i2 < byteBufferWithInfo.position(); i2 += 16) {
            try {
                int i3 = 0;
                while (i3 < 16 && i3 + i2 < byteBufferWithInfo.position()) {
                    byte b2 = byteBufferWithInfo.byteBuffer.get(i2 + i3);
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
                while (i4 < 16 && i4 + i2 < byteBufferWithInfo.position()) {
                    if (ORBUtility.isPrintable((char) byteBufferWithInfo.byteBuffer.get(i2 + i4))) {
                        cArr[i4] = (char) byteBufferWithInfo.byteBuffer.get(i2 + i4);
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
    public void writeIndirection(int i2, int i3) {
        handleSpecialChunkBegin(computeAlignment(4) + 8);
        write_long(i2);
        write_long(i3 - this.parent.getRealIndex(get_offset()));
        handleSpecialChunkEnd();
    }

    protected CodeSetConversion.CTBConverter getCharConverter() {
        if (this.charConverter == null) {
            this.charConverter = this.parent.createCharCTBConverter();
        }
        return this.charConverter;
    }

    protected CodeSetConversion.CTBConverter getWCharConverter() {
        if (this.wcharConverter == null) {
            this.wcharConverter = this.parent.createWCharCTBConverter();
        }
        return this.wcharConverter;
    }

    protected void dprint(String str) {
        if (this.debug) {
            ORBUtility.dprint(this, str);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    void alignOnBoundary(int i2) {
        alignAndReserve(i2, 0);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void start_value(String str) {
        if (this.debug) {
            dprint("start_value w/ rep id " + str + " called at pos " + get_offset() + " position " + this.bbwi.position());
        }
        if (this.inBlock) {
            end_block();
        }
        writeValueTag(true, true, null);
        write_repositoryId(str);
        this.end_flag--;
        this.chunkedValueNestingLevel--;
        start_block();
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStreamBase
    public void end_value() {
        if (this.debug) {
            dprint("end_value called at pos " + get_offset() + " position " + this.bbwi.position());
        }
        end_block();
        writeEndTag(true);
        if (this.debug) {
            dprint("mustChunk is " + this.mustChunk);
        }
        if (this.mustChunk) {
            start_block();
        }
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        CDRInputObject cDRInputObject;
        getBufferManager().close();
        if (getByteBufferWithInfo() != null && getByteBuffer() != null) {
            MessageMediator messageMediator = this.parent.getMessageMediator();
            if (messageMediator != null && (cDRInputObject = (CDRInputObject) messageMediator.getInputObject()) != null && cDRInputObject.isSharing(getByteBuffer())) {
                cDRInputObject.setByteBuffer(null);
                cDRInputObject.setByteBufferWithInfo(null);
            }
            ByteBufferPool byteBufferPool = this.orb.getByteBufferPool();
            if (this.debug) {
                int iIdentityHashCode = System.identityHashCode(this.bbwi.byteBuffer);
                StringBuffer stringBuffer = new StringBuffer(80);
                stringBuffer.append(".close - releasing ByteBuffer id (");
                stringBuffer.append(iIdentityHashCode).append(") to ByteBufferPool.");
                dprint(stringBuffer.toString());
            }
            byteBufferPool.releaseByteBuffer(getByteBuffer());
            this.bbwi.byteBuffer = null;
            this.bbwi = null;
        }
    }
}
