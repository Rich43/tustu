package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.CORBAObjectImpl;
import com.sun.corba.se.impl.corba.PrincipalImpl;
import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.CacheTable;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.RepositoryIdFactory;
import com.sun.corba.se.impl.orbutil.RepositoryIdInterface;
import com.sun.corba.se.impl.orbutil.RepositoryIdStrings;
import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.impl.util.Version;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.pept.transport.ByteBufferPool;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.presentation.rmi.PresentationDefaults;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
import com.sun.org.omg.CORBA.portable.ValueHelper;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.ValueHandler;
import org.omg.CORBA.Any;
import org.omg.CORBA.AnySeqHolder;
import org.omg.CORBA.BooleanSeqHolder;
import org.omg.CORBA.CharSeqHolder;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.CustomMarshal;
import org.omg.CORBA.DoubleSeqHolder;
import org.omg.CORBA.FloatSeqHolder;
import org.omg.CORBA.LongLongSeqHolder;
import org.omg.CORBA.LongSeqHolder;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Object;
import org.omg.CORBA.OctetSeqHolder;
import org.omg.CORBA.Principal;
import org.omg.CORBA.ShortSeqHolder;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.ULongLongSeqHolder;
import org.omg.CORBA.ULongSeqHolder;
import org.omg.CORBA.UShortSeqHolder;
import org.omg.CORBA.WCharSeqHolder;
import org.omg.CORBA.portable.BoxedValueHelper;
import org.omg.CORBA.portable.CustomValue;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.IndirectionException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.InvokeHandler;
import org.omg.CORBA.portable.StreamableValue;
import org.omg.CORBA.portable.ValueBase;
import org.omg.CORBA.portable.ValueFactory;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStream_1_0.class */
public class CDRInputStream_1_0 extends CDRInputStreamBase implements RestorableInputStream {
    private static final String kReadMethod = "read";
    private static final int maxBlockLength = 2147483392;
    protected BufferManagerRead bufferManagerRead;
    protected ByteBufferWithInfo bbwi;
    protected boolean littleEndian;
    protected ORB orb;
    protected ORBUtilSystemException wrapper;
    protected OMGSystemException omgWrapper;
    private RepositoryIdUtility repIdUtil;
    private RepositoryIdStrings repIdStrs;
    private CodeSetConversion.BTCConverter charConverter;
    private CodeSetConversion.BTCConverter wcharConverter;
    private static final String _id = "IDL:omg.org/CORBA/DataInputStream:1.0";
    private static final String[] _ids = {_id};
    private boolean debug = false;
    protected ValueHandler valueHandler = null;
    private CacheTable valueCache = null;
    private CacheTable repositoryIdCache = null;
    private CacheTable codebaseCache = null;
    protected int blockLength = 2147483392;
    protected int end_flag = 0;
    private int chunkedValueNestingLevel = 0;
    protected int valueIndirection = 0;
    protected int stringIndirection = 0;
    protected boolean isChunked = false;
    private boolean specialNoOptionalDataState = false;
    protected MarkAndResetHandler markAndResetHandler = null;

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public /* bridge */ /* synthetic */ boolean markSupported() {
        return super.markSupported();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public /* bridge */ /* synthetic */ Context read_Context() {
        return super.read_Context();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public /* bridge */ /* synthetic */ int read() throws IOException {
        return super.read();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public /* bridge */ /* synthetic */ void setParent(CDRInputStream cDRInputStream) {
        super.setParent(cDRInputStream);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public CDRInputStreamBase dup() {
        try {
            CDRInputStreamBase cDRInputStreamBase = (CDRInputStreamBase) getClass().newInstance();
            cDRInputStreamBase.init(this.orb, this.bbwi.byteBuffer, this.bbwi.buflen, this.littleEndian, this.bufferManagerRead);
            ((CDRInputStream_1_0) cDRInputStreamBase).bbwi.position(this.bbwi.position());
            ((CDRInputStream_1_0) cDRInputStreamBase).bbwi.byteBuffer.limit(this.bbwi.buflen);
            return cDRInputStreamBase;
        } catch (Exception e2) {
            throw this.wrapper.couldNotDuplicateCdrInputStream(e2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void init(org.omg.CORBA.ORB orb, ByteBuffer byteBuffer, int i2, boolean z2, BufferManagerRead bufferManagerRead) {
        this.orb = (ORB) orb;
        this.wrapper = ORBUtilSystemException.get((ORB) orb, CORBALogDomains.RPC_ENCODING);
        this.omgWrapper = OMGSystemException.get((ORB) orb, CORBALogDomains.RPC_ENCODING);
        this.littleEndian = z2;
        this.bufferManagerRead = bufferManagerRead;
        this.bbwi = new ByteBufferWithInfo(orb, byteBuffer, 0);
        this.bbwi.buflen = i2;
        this.bbwi.byteBuffer.limit(this.bbwi.buflen);
        this.markAndResetHandler = this.bufferManagerRead.getMarkAndResetHandler();
        this.debug = ((ORB) orb).transportDebugFlag;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void performORBVersionSpecificInit() {
        createRepositoryIdHandlers();
    }

    private final void createRepositoryIdHandlers() {
        this.repIdUtil = RepositoryIdFactory.getRepIdUtility();
        this.repIdStrs = RepositoryIdFactory.getRepIdStringsFactory();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_0;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void setHeaderPadding(boolean z2) {
        throw this.wrapper.giopVersionError();
    }

    protected final int computeAlignment(int i2, int i3) {
        int i4;
        if (i3 > 1 && (i4 = i2 & (i3 - 1)) != 0) {
            return i3 - i4;
        }
        return 0;
    }

    public int getSize() {
        return this.bbwi.position();
    }

    protected void checkBlockLength(int i2, int i3) {
        if (!this.isChunked) {
            return;
        }
        if (this.specialNoOptionalDataState) {
            throw this.omgWrapper.rmiiiopOptionalDataIncompatible1();
        }
        boolean z2 = false;
        if (this.blockLength == get_offset()) {
            this.blockLength = 2147483392;
            start_block();
            if (this.blockLength == 2147483392) {
                z2 = true;
            }
        } else if (this.blockLength < get_offset()) {
            throw this.wrapper.chunkOverflow();
        }
        int iComputeAlignment = computeAlignment(this.bbwi.position(), i2) + i3;
        if (this.blockLength != 2147483392 && this.blockLength < get_offset() + iComputeAlignment) {
            throw this.omgWrapper.rmiiiopOptionalDataIncompatible2();
        }
        if (z2) {
            int i4 = read_long();
            this.bbwi.position(this.bbwi.position() - 4);
            if (i4 < 0) {
                throw this.omgWrapper.rmiiiopOptionalDataIncompatible3();
            }
        }
    }

    protected void alignAndCheck(int i2, int i3) {
        checkBlockLength(i2, i3);
        this.bbwi.position(this.bbwi.position() + computeAlignment(this.bbwi.position(), i2));
        if (this.bbwi.position() + i3 > this.bbwi.buflen) {
            grow(i2, i3);
        }
    }

    protected void grow(int i2, int i3) {
        this.bbwi.needed = i3;
        this.bbwi = this.bufferManagerRead.underflow(this.bbwi);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void consumeEndian() {
        this.littleEndian = read_boolean();
    }

    public final double read_longdouble() {
        throw this.wrapper.longDoubleNotImplemented(CompletionStatus.COMPLETED_MAYBE);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final boolean read_boolean() {
        return read_octet() != 0;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final char read_char() {
        alignAndCheck(1, 1);
        return getConvertedChars(1, getCharConverter())[0];
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public char read_wchar() {
        int i2;
        int i3;
        if (ORBUtility.isForeignORB(this.orb)) {
            throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
        }
        alignAndCheck(2, 2);
        if (this.littleEndian) {
            i3 = this.bbwi.byteBuffer.get(this.bbwi.position()) & 255;
            this.bbwi.position(this.bbwi.position() + 1);
            i2 = this.bbwi.byteBuffer.get(this.bbwi.position()) & 255;
            this.bbwi.position(this.bbwi.position() + 1);
        } else {
            i2 = this.bbwi.byteBuffer.get(this.bbwi.position()) & 255;
            this.bbwi.position(this.bbwi.position() + 1);
            i3 = this.bbwi.byteBuffer.get(this.bbwi.position()) & 255;
            this.bbwi.position(this.bbwi.position() + 1);
        }
        return (char) ((i2 << 8) + (i3 << 0));
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final byte read_octet() {
        alignAndCheck(1, 1);
        byte b2 = this.bbwi.byteBuffer.get(this.bbwi.position());
        this.bbwi.position(this.bbwi.position() + 1);
        return b2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final short read_short() {
        int i2;
        int i3;
        alignAndCheck(2, 2);
        if (this.littleEndian) {
            i3 = (this.bbwi.byteBuffer.get(this.bbwi.position()) << 0) & 255;
            this.bbwi.position(this.bbwi.position() + 1);
            i2 = (this.bbwi.byteBuffer.get(this.bbwi.position()) << 8) & NormalizerImpl.CC_MASK;
            this.bbwi.position(this.bbwi.position() + 1);
        } else {
            i2 = (this.bbwi.byteBuffer.get(this.bbwi.position()) << 8) & NormalizerImpl.CC_MASK;
            this.bbwi.position(this.bbwi.position() + 1);
            i3 = (this.bbwi.byteBuffer.get(this.bbwi.position()) << 0) & 255;
            this.bbwi.position(this.bbwi.position() + 1);
        }
        return (short) (i2 | i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final short read_ushort() {
        return read_short();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final int read_long() {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        alignAndCheck(4, 4);
        int iPosition = this.bbwi.position();
        if (this.littleEndian) {
            int i7 = iPosition + 1;
            i6 = this.bbwi.byteBuffer.get(iPosition) & 255;
            int i8 = i7 + 1;
            i4 = this.bbwi.byteBuffer.get(i7) & 255;
            int i9 = i8 + 1;
            i3 = this.bbwi.byteBuffer.get(i8) & 255;
            i5 = i9 + 1;
            i2 = this.bbwi.byteBuffer.get(i9) & 255;
        } else {
            int i10 = iPosition + 1;
            i2 = this.bbwi.byteBuffer.get(iPosition) & 255;
            int i11 = i10 + 1;
            i3 = this.bbwi.byteBuffer.get(i10) & 255;
            int i12 = i11 + 1;
            i4 = this.bbwi.byteBuffer.get(i11) & 255;
            i5 = i12 + 1;
            i6 = this.bbwi.byteBuffer.get(i12) & 255;
        }
        this.bbwi.position(i5);
        return (i2 << 24) | (i3 << 16) | (i4 << 8) | i6;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final int read_ulong() {
        return read_long();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final long read_longlong() {
        long j2;
        long j3;
        alignAndCheck(8, 8);
        if (this.littleEndian) {
            j3 = read_long() & 4294967295L;
            j2 = read_long() << 32;
        } else {
            j2 = read_long() << 32;
            j3 = read_long() & 4294967295L;
        }
        return j2 | j3;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final long read_ulonglong() {
        return read_longlong();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final float read_float() {
        return Float.intBitsToFloat(read_long());
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final double read_double() {
        return Double.longBitsToDouble(read_longlong());
    }

    protected final void checkForNegativeLength(int i2) {
        if (i2 < 0) {
            throw this.wrapper.negativeStringLength(CompletionStatus.COMPLETED_MAYBE, new Integer(i2));
        }
    }

    protected final String readStringOrIndirection(boolean z2) {
        int i2 = read_long();
        if (z2) {
            if (i2 == -1) {
                return null;
            }
            this.stringIndirection = get_offset() - 4;
        }
        checkForNegativeLength(i2);
        return internalReadString(i2);
    }

    private final String internalReadString(int i2) {
        if (i2 == 0) {
            return new String("");
        }
        char[] convertedChars = getConvertedChars(i2 - 1, getCharConverter());
        read_octet();
        return new String(convertedChars, 0, getCharConverter().getNumChars());
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final String read_string() {
        return readStringOrIndirection(false);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public String read_wstring() {
        if (ORBUtility.isForeignORB(this.orb)) {
            throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
        }
        int i2 = read_long();
        if (i2 == 0) {
            return new String("");
        }
        checkForNegativeLength(i2);
        int i3 = i2 - 1;
        char[] cArr = new char[i3];
        for (int i4 = 0; i4 < i3; i4++) {
            cArr[i4] = read_wchar();
        }
        read_wchar();
        return new String(cArr);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_octet_array(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            throw this.wrapper.nullParam();
        }
        if (i3 == 0) {
            return;
        }
        alignAndCheck(1, 1);
        int i4 = i2;
        while (true) {
            int i5 = i4;
            if (i5 < i3 + i2) {
                int iPosition = this.bbwi.buflen - this.bbwi.position();
                if (iPosition <= 0) {
                    grow(1, 1);
                    iPosition = this.bbwi.buflen - this.bbwi.position();
                }
                int i6 = (i3 + i2) - i5;
                int i7 = i6 < iPosition ? i6 : iPosition;
                for (int i8 = 0; i8 < i7; i8++) {
                    bArr[i5 + i8] = this.bbwi.byteBuffer.get(this.bbwi.position() + i8);
                }
                this.bbwi.position(this.bbwi.position() + i7);
                i4 = i5 + i7;
            } else {
                return;
            }
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Principal read_Principal() {
        int i2 = read_long();
        byte[] bArr = new byte[i2];
        read_octet_array(bArr, 0, i2);
        PrincipalImpl principalImpl = new PrincipalImpl();
        principalImpl.name(bArr);
        return principalImpl;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public TypeCode read_TypeCode() {
        TypeCodeImpl typeCodeImpl = new TypeCodeImpl(this.orb);
        typeCodeImpl.read_value(this.parent);
        return typeCodeImpl;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Any read_any() throws MARSHAL {
        Any anyCreate_any = this.orb.create_any();
        TypeCodeImpl typeCodeImpl = new TypeCodeImpl(this.orb);
        try {
            typeCodeImpl.read_value(this.parent);
        } catch (MARSHAL e2) {
            if (typeCodeImpl.kind().value() != 29) {
                throw e2;
            }
            dprintThrowable(e2);
        }
        anyCreate_any.read_value(this.parent, typeCodeImpl);
        return anyCreate_any;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Object read_Object() {
        return read_Object(null);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Object read_Object(Class cls) {
        PresentationManager.StubFactory stubFactoryCreateStubFactory;
        IOR iorMakeIOR = IORFactories.makeIOR(this.parent);
        if (iorMakeIOR.isNil()) {
            return null;
        }
        PresentationManager.StubFactoryFactory stubFactoryFactory = ORB.getStubFactoryFactory();
        String codebase = iorMakeIOR.getProfile().getCodebase();
        if (cls == null) {
            RepositoryId id = RepositoryId.cache.getId(iorMakeIOR.getTypeId());
            String className = id.getClassName();
            this.orb.validateIORClass(className);
            boolean zIsIDLType = id.isIDLType();
            if (className == null || className.equals("")) {
                stubFactoryCreateStubFactory = null;
            } else {
                try {
                    stubFactoryCreateStubFactory = stubFactoryFactory.createStubFactory(className, zIsIDLType, codebase, (Class) null, (ClassLoader) null);
                } catch (Exception e2) {
                    stubFactoryCreateStubFactory = null;
                }
            }
        } else if (StubAdapter.isStubClass(cls)) {
            stubFactoryCreateStubFactory = PresentationDefaults.makeStaticStubFactory(cls);
        } else {
            stubFactoryCreateStubFactory = stubFactoryFactory.createStubFactory(cls.getName(), IDLEntity.class.isAssignableFrom(cls), codebase, cls, cls.getClassLoader());
        }
        return internalIORToObject(iorMakeIOR, stubFactoryCreateStubFactory, this.orb);
    }

    public static Object internalIORToObject(IOR ior, PresentationManager.StubFactory stubFactory, ORB orb) {
        Object cORBAObjectImpl;
        ORBUtilSystemException oRBUtilSystemException = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_ENCODING);
        Object servant = ior.getProfile().getServant();
        if (servant != null) {
            if (servant instanceof Tie) {
                Object object = (Object) Utility.loadStub((Tie) servant, stubFactory, ior.getProfile().getCodebase(), false);
                if (object != null) {
                    return object;
                }
                throw oRBUtilSystemException.readObjectException();
            }
            if (servant instanceof Object) {
                if (!(servant instanceof InvokeHandler)) {
                    return (Object) servant;
                }
            } else {
                throw oRBUtilSystemException.badServantReadObject();
            }
        }
        CorbaClientDelegate corbaClientDelegateMakeClientDelegate = ORBUtility.makeClientDelegate(ior);
        try {
            cORBAObjectImpl = stubFactory.makeStub();
        } catch (Throwable th) {
            oRBUtilSystemException.stubCreateError(th);
            if (th instanceof ThreadDeath) {
                throw ((ThreadDeath) th);
            }
            cORBAObjectImpl = new CORBAObjectImpl();
        }
        StubAdapter.setDelegate(cORBAObjectImpl, corbaClientDelegateMakeClientDelegate);
        return cORBAObjectImpl;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Object read_abstract_interface() {
        return read_abstract_interface(null);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Object read_abstract_interface(Class cls) {
        if (read_boolean()) {
            return read_Object(cls);
        }
        return read_value();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value() {
        return read_value((Class) null);
    }

    private Serializable handleIndirection() {
        int i2 = (read_long() + get_offset()) - 4;
        if (this.valueCache != null && this.valueCache.containsVal(i2)) {
            return (Serializable) this.valueCache.getKey(i2);
        }
        throw new IndirectionException(i2);
    }

    private String readRepositoryIds(int i2, Class cls, String str) {
        return readRepositoryIds(i2, cls, str, null);
    }

    private String readRepositoryIds(int i2, Class cls, String str, BoxedValueHelper boxedValueHelper) {
        switch (this.repIdUtil.getTypeInfo(i2)) {
            case 0:
                if (cls == null) {
                    if (str != null) {
                        return str;
                    }
                    if (boxedValueHelper != null) {
                        return boxedValueHelper.get_id();
                    }
                    throw this.wrapper.expectedTypeNullAndNoRepId(CompletionStatus.COMPLETED_MAYBE);
                }
                return this.repIdStrs.createForAnyType(cls);
            case 2:
                return read_repositoryId();
            case 6:
                return read_repositoryIds();
            default:
                throw this.wrapper.badValueTag(CompletionStatus.COMPLETED_MAYBE, Integer.toHexString(i2));
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(Class cls) {
        Object value;
        int valueTag = readValueTag();
        if (valueTag == 0) {
            return null;
        }
        if (valueTag == -1) {
            return handleIndirection();
        }
        int i2 = get_offset() - 4;
        boolean z2 = this.isChunked;
        this.isChunked = this.repIdUtil.isChunkedEncoding(valueTag);
        String str = null;
        if (this.repIdUtil.isCodeBasePresent(valueTag)) {
            str = read_codebase_URL();
        }
        String repositoryIds = readRepositoryIds(valueTag, cls, null);
        start_block();
        this.end_flag--;
        if (this.isChunked) {
            this.chunkedValueNestingLevel--;
        }
        if (repositoryIds.equals(this.repIdStrs.getWStringValueRepId())) {
            value = read_wstring();
        } else if (repositoryIds.equals(this.repIdStrs.getClassDescValueRepId())) {
            value = readClass();
        } else {
            Class classFromString = cls;
            if (cls == null || !repositoryIds.equals(this.repIdStrs.createForAnyType(cls))) {
                classFromString = getClassFromString(repositoryIds, str, cls);
            }
            if (classFromString == null) {
                throw this.wrapper.couldNotFindClass(CompletionStatus.COMPLETED_MAYBE, new ClassNotFoundException());
            }
            if (classFromString != null && IDLEntity.class.isAssignableFrom(classFromString)) {
                value = readIDLValue(i2, repositoryIds, classFromString, str);
            } else {
                try {
                    if (this.valueHandler == null) {
                        this.valueHandler = ORBUtility.createValueHandler();
                    }
                    value = this.valueHandler.readValue(this.parent, i2, classFromString, repositoryIds, getCodeBase());
                } catch (Error e2) {
                    throw this.wrapper.valuehandlerReadError(CompletionStatus.COMPLETED_MAYBE, e2);
                } catch (SystemException e3) {
                    throw e3;
                } catch (Exception e4) {
                    throw this.wrapper.valuehandlerReadException(CompletionStatus.COMPLETED_MAYBE, e4);
                }
            }
        }
        handleEndOfValue();
        readEndTag();
        if (this.valueCache == null) {
            this.valueCache = new CacheTable(this.orb, false);
        }
        this.valueCache.put(value, i2);
        this.isChunked = z2;
        start_block();
        return (Serializable) value;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(BoxedValueHelper boxedValueHelper) throws SecurityException, IllegalArgumentException {
        Object iDLValueWithHelper;
        int valueTag = readValueTag();
        if (valueTag == 0) {
            return null;
        }
        if (valueTag == -1) {
            int i2 = (read_long() + get_offset()) - 4;
            if (this.valueCache != null && this.valueCache.containsVal(i2)) {
                return (Serializable) this.valueCache.getKey(i2);
            }
            throw new IndirectionException(i2);
        }
        int i3 = get_offset() - 4;
        boolean z2 = this.isChunked;
        this.isChunked = this.repIdUtil.isChunkedEncoding(valueTag);
        String str = null;
        if (this.repIdUtil.isCodeBasePresent(valueTag)) {
            str = read_codebase_URL();
        }
        String repositoryIds = readRepositoryIds(valueTag, null, null, boxedValueHelper);
        if (!repositoryIds.equals(boxedValueHelper.get_id())) {
            boxedValueHelper = Utility.getHelper(null, str, repositoryIds);
        }
        start_block();
        this.end_flag--;
        if (this.isChunked) {
            this.chunkedValueNestingLevel--;
        }
        if (boxedValueHelper instanceof ValueHelper) {
            iDLValueWithHelper = readIDLValueWithHelper((ValueHelper) boxedValueHelper, i3);
        } else {
            this.valueIndirection = i3;
            iDLValueWithHelper = boxedValueHelper.read_value(this.parent);
        }
        handleEndOfValue();
        readEndTag();
        if (this.valueCache == null) {
            this.valueCache = new CacheTable(this.orb, false);
        }
        this.valueCache.put(iDLValueWithHelper, i3);
        this.isChunked = z2;
        start_block();
        return (Serializable) iDLValueWithHelper;
    }

    private boolean isCustomType(ValueHelper valueHelper) {
        try {
            TypeCode typeCode = valueHelper.get_type();
            if (typeCode.kind().value() == 29) {
                return typeCode.type_modifier() == 1;
            }
            return false;
        } catch (BadKind e2) {
            throw this.wrapper.badKind(e2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(Serializable serializable) {
        if (this.valueCache == null) {
            this.valueCache = new CacheTable(this.orb, false);
        }
        this.valueCache.put(serializable, this.valueIndirection);
        if (serializable instanceof StreamableValue) {
            ((StreamableValue) serializable)._read(this.parent);
        } else if (serializable instanceof CustomValue) {
            ((CustomValue) serializable).unmarshal(this.parent);
        }
        return serializable;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(String str) {
        int valueTag = readValueTag();
        if (valueTag == 0) {
            return null;
        }
        if (valueTag == -1) {
            int i2 = (read_long() + get_offset()) - 4;
            if (this.valueCache != null && this.valueCache.containsVal(i2)) {
                return (Serializable) this.valueCache.getKey(i2);
            }
            throw new IndirectionException(i2);
        }
        int i3 = get_offset() - 4;
        boolean z2 = this.isChunked;
        this.isChunked = this.repIdUtil.isChunkedEncoding(valueTag);
        String str2 = null;
        if (this.repIdUtil.isCodeBasePresent(valueTag)) {
            str2 = read_codebase_URL();
        }
        ValueFactory factory = Utility.getFactory(null, str2, this.orb, readRepositoryIds(valueTag, null, str));
        start_block();
        this.end_flag--;
        if (this.isChunked) {
            this.chunkedValueNestingLevel--;
        }
        this.valueIndirection = i3;
        Serializable serializable = factory.read_value(this.parent);
        handleEndOfValue();
        readEndTag();
        if (this.valueCache == null) {
            this.valueCache = new CacheTable(this.orb, false);
        }
        this.valueCache.put(serializable, i3);
        this.isChunked = z2;
        start_block();
        return serializable;
    }

    private Class readClass() {
        String str;
        String str2;
        if (this.orb == null || ORBVersionFactory.getFOREIGN().equals(this.orb.getORBVersion()) || ORBVersionFactory.getNEWER().compareTo(this.orb.getORBVersion()) <= 0) {
            str = (String) read_value(String.class);
            str2 = (String) read_value(String.class);
        } else {
            str2 = (String) read_value(String.class);
            str = (String) read_value(String.class);
        }
        if (this.debug) {
            dprint("readClass codebases: " + str + " rep Id: " + str2);
        }
        RepositoryIdInterface fromString = this.repIdStrs.getFromString(str2);
        try {
            return fromString.getClassFromType(str);
        } catch (ClassNotFoundException e2) {
            throw this.wrapper.cnfeReadClass(CompletionStatus.COMPLETED_MAYBE, e2, fromString.getClassName());
        } catch (MalformedURLException e3) {
            throw this.wrapper.malformedUrl(CompletionStatus.COMPLETED_MAYBE, e3, fromString.getClassName(), str);
        }
    }

    private Object readIDLValueWithHelper(ValueHelper valueHelper, int i2) throws SecurityException, IllegalArgumentException {
        try {
            Method declaredMethod = valueHelper.getClass().getDeclaredMethod("read", InputStream.class, valueHelper.get_class());
            try {
                Object objNewInstance = valueHelper.get_class().newInstance();
                if (this.valueCache == null) {
                    this.valueCache = new CacheTable(this.orb, false);
                }
                this.valueCache.put(objNewInstance, i2);
                if ((objNewInstance instanceof CustomMarshal) && isCustomType(valueHelper)) {
                    ((CustomMarshal) objNewInstance).unmarshal(this.parent);
                    return objNewInstance;
                }
                try {
                    declaredMethod.invoke(valueHelper, this.parent, objNewInstance);
                    return objNewInstance;
                } catch (IllegalAccessException e2) {
                    throw this.wrapper.couldNotInvokeHelperReadMethod(e2, valueHelper.get_class());
                } catch (InvocationTargetException e3) {
                    throw this.wrapper.couldNotInvokeHelperReadMethod(e3, valueHelper.get_class());
                }
            } catch (IllegalAccessException e4) {
                return valueHelper.read_value(this.parent);
            } catch (InstantiationException e5) {
                throw this.wrapper.couldNotInstantiateHelper(e5, valueHelper.get_class());
            }
        } catch (NoSuchMethodException e6) {
            return valueHelper.read_value(this.parent);
        }
    }

    private Object readBoxedIDLEntity(Class cls, String str) throws NoSuchMethodException {
        ClassLoader classLoader;
        if (cls == null) {
            classLoader = null;
        } else {
            try {
                classLoader = cls.getClassLoader();
            } catch (ClassNotFoundException e2) {
                throw this.wrapper.couldNotInvokeHelperReadMethod(e2, (Object) null);
            } catch (IllegalAccessException e3) {
                throw this.wrapper.couldNotInvokeHelperReadMethod(e3, (Object) null);
            } catch (NoSuchMethodException e4) {
                throw this.wrapper.couldNotInvokeHelperReadMethod(e4, (Object) null);
            } catch (InvocationTargetException e5) {
                throw this.wrapper.couldNotInvokeHelperReadMethod(e5, (Object) null);
            }
        }
        ClassLoader classLoader2 = classLoader;
        final Class clsLoadClassForClass = Utility.loadClassForClass(cls.getName() + "Helper", str, classLoader2, cls, classLoader2);
        final Class[] clsArr = {InputStream.class};
        try {
            return ((Method) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.impl.encoding.CDRInputStream_1_0.1
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws NoSuchMethodException {
                    return clsLoadClassForClass.getDeclaredMethod("read", clsArr);
                }
            })).invoke(null, this.parent);
        } catch (PrivilegedActionException e6) {
            throw ((NoSuchMethodException) e6.getException());
        }
    }

    private Object readIDLValue(int i2, String str, Class cls, String str2) {
        try {
            ValueFactory factory = Utility.getFactory(cls, str2, this.orb, str);
            this.valueIndirection = i2;
            return factory.read_value(this.parent);
        } catch (MARSHAL e2) {
            if (!StreamableValue.class.isAssignableFrom(cls) && !CustomValue.class.isAssignableFrom(cls) && ValueBase.class.isAssignableFrom(cls)) {
                BoxedValueHelper helper = Utility.getHelper(cls, str2, str);
                if (helper instanceof ValueHelper) {
                    return readIDLValueWithHelper((ValueHelper) helper, i2);
                }
                return helper.read_value(this.parent);
            }
            return readBoxedIDLEntity(cls, str2);
        }
    }

    private void readEndTag() {
        if (this.isChunked) {
            int i2 = read_long();
            if (i2 >= 0) {
                throw this.wrapper.positiveEndTag(CompletionStatus.COMPLETED_MAYBE, new Integer(i2), new Integer(get_offset() - 4));
            }
            if (this.orb == null || ORBVersionFactory.getFOREIGN().equals(this.orb.getORBVersion()) || ORBVersionFactory.getNEWER().compareTo(this.orb.getORBVersion()) <= 0) {
                if (i2 < this.chunkedValueNestingLevel) {
                    throw this.wrapper.unexpectedEnclosingValuetype(CompletionStatus.COMPLETED_MAYBE, new Integer(i2), new Integer(this.chunkedValueNestingLevel));
                }
                if (i2 != this.chunkedValueNestingLevel) {
                    this.bbwi.position(this.bbwi.position() - 4);
                }
            } else if (i2 != this.end_flag) {
                this.bbwi.position(this.bbwi.position() - 4);
            }
            this.chunkedValueNestingLevel++;
        }
        this.end_flag++;
    }

    protected int get_offset() {
        return this.bbwi.position();
    }

    private void start_block() {
        if (!this.isChunked) {
            return;
        }
        this.blockLength = 2147483392;
        this.blockLength = read_long();
        if (this.blockLength > 0 && this.blockLength < 2147483392) {
            this.blockLength += get_offset();
        } else {
            this.blockLength = 2147483392;
            this.bbwi.position(this.bbwi.position() - 4);
        }
    }

    private void handleEndOfValue() {
        if (!this.isChunked) {
            return;
        }
        while (this.blockLength != 2147483392) {
            end_block();
            start_block();
        }
        int i2 = read_long();
        this.bbwi.position(this.bbwi.position() - 4);
        if (i2 < 0) {
            return;
        }
        if (i2 == 0 || i2 >= 2147483392) {
            read_value();
            handleEndOfValue();
            return;
        }
        throw this.wrapper.couldNotSkipBytes(CompletionStatus.COMPLETED_MAYBE, new Integer(i2), new Integer(get_offset()));
    }

    private void end_block() {
        if (this.blockLength != 2147483392) {
            if (this.blockLength == get_offset()) {
                this.blockLength = 2147483392;
            } else {
                if (this.blockLength > get_offset()) {
                    skipToOffset(this.blockLength);
                    return;
                }
                throw this.wrapper.badChunkLength(new Integer(this.blockLength), new Integer(get_offset()));
            }
        }
    }

    private int readValueTag() {
        return read_long();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public org.omg.CORBA.ORB orb() {
        return this.orb;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_boolean_array(boolean[] zArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            zArr[i4 + i2] = read_boolean();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_char_array(char[] cArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            cArr[i4 + i2] = read_char();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_wchar_array(char[] cArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            cArr[i4 + i2] = read_wchar();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_short_array(short[] sArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            sArr[i4 + i2] = read_short();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_ushort_array(short[] sArr, int i2, int i3) {
        read_short_array(sArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_long_array(int[] iArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            iArr[i4 + i2] = read_long();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_ulong_array(int[] iArr, int i2, int i3) {
        read_long_array(iArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_longlong_array(long[] jArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            jArr[i4 + i2] = read_longlong();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_ulonglong_array(long[] jArr, int i2, int i3) {
        read_longlong_array(jArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_float_array(float[] fArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            fArr[i4 + i2] = read_float();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public final void read_double_array(double[] dArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            dArr[i4 + i2] = read_double();
        }
    }

    public final void read_any_array(Any[] anyArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            anyArr[i4 + i2] = read_any();
        }
    }

    private String read_repositoryIds() {
        int i2 = read_long();
        if (i2 == -1) {
            int i3 = (read_long() + get_offset()) - 4;
            if (this.repositoryIdCache != null && this.repositoryIdCache.containsOrderedVal(i3)) {
                return (String) this.repositoryIdCache.getKey(i3);
            }
            throw this.wrapper.unableToLocateRepIdArray(new Integer(i3));
        }
        int i4 = get_offset();
        String str = read_repositoryId();
        if (this.repositoryIdCache == null) {
            this.repositoryIdCache = new CacheTable(this.orb, false);
        }
        this.repositoryIdCache.put(str, i4);
        for (int i5 = 1; i5 < i2; i5++) {
            read_repositoryId();
        }
        return str;
    }

    private final String read_repositoryId() {
        String stringOrIndirection = readStringOrIndirection(true);
        if (stringOrIndirection == null) {
            int i2 = (read_long() + get_offset()) - 4;
            if (this.repositoryIdCache != null && this.repositoryIdCache.containsOrderedVal(i2)) {
                return (String) this.repositoryIdCache.getKey(i2);
            }
            throw this.wrapper.badRepIdIndirection(CompletionStatus.COMPLETED_MAYBE, new Integer(this.bbwi.position()));
        }
        if (this.repositoryIdCache == null) {
            this.repositoryIdCache = new CacheTable(this.orb, false);
        }
        this.repositoryIdCache.put(stringOrIndirection, this.stringIndirection);
        return stringOrIndirection;
    }

    private final String read_codebase_URL() {
        String stringOrIndirection = readStringOrIndirection(true);
        if (stringOrIndirection == null) {
            int i2 = (read_long() + get_offset()) - 4;
            if (this.codebaseCache != null && this.codebaseCache.containsVal(i2)) {
                return (String) this.codebaseCache.getKey(i2);
            }
            throw this.wrapper.badCodebaseIndirection(CompletionStatus.COMPLETED_MAYBE, new Integer(this.bbwi.position()));
        }
        if (this.codebaseCache == null) {
            this.codebaseCache = new CacheTable(this.orb, false);
        }
        this.codebaseCache.put(stringOrIndirection, this.stringIndirection);
        return stringOrIndirection;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Object read_Abstract() {
        return read_abstract_interface();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_Value() {
        return read_value();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_any_array(AnySeqHolder anySeqHolder, int i2, int i3) {
        read_any_array(anySeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_boolean_array(BooleanSeqHolder booleanSeqHolder, int i2, int i3) {
        read_boolean_array(booleanSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_char_array(CharSeqHolder charSeqHolder, int i2, int i3) {
        read_char_array(charSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_wchar_array(WCharSeqHolder wCharSeqHolder, int i2, int i3) {
        read_wchar_array(wCharSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_octet_array(OctetSeqHolder octetSeqHolder, int i2, int i3) {
        read_octet_array(octetSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_short_array(ShortSeqHolder shortSeqHolder, int i2, int i3) {
        read_short_array(shortSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_ushort_array(UShortSeqHolder uShortSeqHolder, int i2, int i3) {
        read_ushort_array(uShortSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_long_array(LongSeqHolder longSeqHolder, int i2, int i3) {
        read_long_array(longSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_ulong_array(ULongSeqHolder uLongSeqHolder, int i2, int i3) {
        read_ulong_array(uLongSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_ulonglong_array(ULongLongSeqHolder uLongLongSeqHolder, int i2, int i3) {
        read_ulonglong_array(uLongLongSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_longlong_array(LongLongSeqHolder longLongSeqHolder, int i2, int i3) {
        read_longlong_array(longLongSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_float_array(FloatSeqHolder floatSeqHolder, int i2, int i3) {
        read_float_array(floatSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_double_array(DoubleSeqHolder doubleSeqHolder, int i2, int i3) {
        read_double_array(doubleSeqHolder.value, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public BigDecimal read_fixed(short s2, short s3) {
        StringBuffer stringBuffer = read_fixed_buffer();
        if (s2 != stringBuffer.length()) {
            throw this.wrapper.badFixed(new Integer(s2), new Integer(stringBuffer.length()));
        }
        stringBuffer.insert(s2 - s3, '.');
        return new BigDecimal(stringBuffer.toString());
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public BigDecimal read_fixed() {
        return new BigDecimal(read_fixed_buffer().toString());
    }

    private StringBuffer read_fixed_buffer() {
        StringBuffer stringBuffer = new StringBuffer(64);
        boolean z2 = false;
        boolean z3 = true;
        while (z3) {
            byte b2 = read_octet();
            int i2 = (b2 & 240) >> 4;
            int i3 = b2 & 15;
            if (z2 || i2 != 0) {
                stringBuffer.append(Character.forDigit(i2, 10));
                z2 = true;
            }
            if (i3 == 12) {
                if (!z2) {
                    return new StringBuffer(Version.BUILD);
                }
                z3 = false;
            } else if (i3 == 13) {
                stringBuffer.insert(0, '-');
                z3 = false;
            } else {
                stringBuffer.append(Character.forDigit(i3, 10));
                z2 = true;
            }
        }
        return stringBuffer;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public String[] _truncatable_ids() {
        if (_ids == null) {
            return null;
        }
        return (String[]) _ids.clone();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void printBuffer() {
        printBuffer(this.bbwi);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v43, types: [int] */
    public static void printBuffer(ByteBufferWithInfo byteBufferWithInfo) {
        System.out.println("----- Input Buffer -----");
        System.out.println();
        System.out.println("Current position: " + byteBufferWithInfo.position());
        System.out.println("Total length : " + byteBufferWithInfo.buflen);
        System.out.println();
        try {
            char[] cArr = new char[16];
            for (int i2 = 0; i2 < byteBufferWithInfo.buflen; i2 += 16) {
                int i3 = 0;
                while (i3 < 16 && i3 + i2 < byteBufferWithInfo.buflen) {
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
                while (i4 < 16 && i4 + i2 < byteBufferWithInfo.buflen) {
                    if (ORBUtility.isPrintable((char) byteBufferWithInfo.byteBuffer.get(i2 + i4))) {
                        cArr[i4] = (char) byteBufferWithInfo.byteBuffer.get(i2 + i4);
                    } else {
                        cArr[i4] = '.';
                    }
                    i4++;
                }
                System.out.println(new String(cArr, 0, i4));
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        System.out.println("------------------------");
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public ByteBuffer getByteBuffer() {
        ByteBuffer byteBuffer = null;
        if (this.bbwi != null) {
            byteBuffer = this.bbwi.byteBuffer;
        }
        return byteBuffer;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int getBufferLength() {
        return this.bbwi.buflen;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setBufferLength(int i2) {
        this.bbwi.buflen = i2;
        this.bbwi.byteBuffer.limit(this.bbwi.buflen);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        this.bbwi = byteBufferWithInfo;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.bbwi.byteBuffer = byteBuffer;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int getIndex() {
        return this.bbwi.position();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setIndex(int i2) {
        this.bbwi.position(i2);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public boolean isLittleEndian() {
        return this.littleEndian;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void orb(org.omg.CORBA.ORB orb) {
        this.orb = (ORB) orb;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public BufferManagerRead getBufferManager() {
        return this.bufferManagerRead;
    }

    private void skipToOffset(int i2) {
        int i3 = i2 - get_offset();
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < i3) {
                int iPosition = this.bbwi.buflen - this.bbwi.position();
                if (iPosition <= 0) {
                    grow(1, 1);
                    iPosition = this.bbwi.buflen - this.bbwi.position();
                }
                int i6 = i3 - i5;
                int i7 = i6 < iPosition ? i6 : iPosition;
                this.bbwi.position(this.bbwi.position() + i7);
                i4 = i5 + i7;
            } else {
                return;
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/CDRInputStream_1_0$StreamMemento.class */
    protected class StreamMemento {
        private int blockLength_;
        private int end_flag_;
        private int chunkedValueNestingLevel_;
        private int valueIndirection_;
        private int stringIndirection_;
        private boolean isChunked_;
        private ValueHandler valueHandler_;
        private ByteBufferWithInfo bbwi_;
        private boolean specialNoOptionalDataState_;

        public StreamMemento() {
            this.blockLength_ = CDRInputStream_1_0.this.blockLength;
            this.end_flag_ = CDRInputStream_1_0.this.end_flag;
            this.chunkedValueNestingLevel_ = CDRInputStream_1_0.this.chunkedValueNestingLevel;
            this.valueIndirection_ = CDRInputStream_1_0.this.valueIndirection;
            this.stringIndirection_ = CDRInputStream_1_0.this.stringIndirection;
            this.isChunked_ = CDRInputStream_1_0.this.isChunked;
            this.valueHandler_ = CDRInputStream_1_0.this.valueHandler;
            this.specialNoOptionalDataState_ = CDRInputStream_1_0.this.specialNoOptionalDataState;
            this.bbwi_ = new ByteBufferWithInfo(CDRInputStream_1_0.this.bbwi);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.RestorableInputStream
    public Object createStreamMemento() {
        return new StreamMemento();
    }

    @Override // com.sun.corba.se.impl.encoding.RestorableInputStream
    public void restoreInternalState(Object obj) {
        StreamMemento streamMemento = (StreamMemento) obj;
        this.blockLength = streamMemento.blockLength_;
        this.end_flag = streamMemento.end_flag_;
        this.chunkedValueNestingLevel = streamMemento.chunkedValueNestingLevel_;
        this.valueIndirection = streamMemento.valueIndirection_;
        this.stringIndirection = streamMemento.stringIndirection_;
        this.isChunked = streamMemento.isChunked_;
        this.valueHandler = streamMemento.valueHandler_;
        this.specialNoOptionalDataState = streamMemento.specialNoOptionalDataState_;
        this.bbwi = streamMemento.bbwi_;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int getPosition() {
        return get_offset();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public void mark(int i2) {
        this.markAndResetHandler.mark(this);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public void reset() {
        this.markAndResetHandler.reset();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    CodeBase getCodeBase() {
        return this.parent.getCodeBase();
    }

    private Class getClassFromString(String str, String str2, Class cls) {
        RepositoryIdInterface fromString = this.repIdStrs.getFromString(str);
        try {
            try {
                return fromString.getClassFromType(cls, str2);
            } catch (ClassNotFoundException e2) {
                try {
                    if (getCodeBase() == null) {
                        return null;
                    }
                    str2 = getCodeBase().implementation(str);
                    if (str2 == null) {
                        return null;
                    }
                    return fromString.getClassFromType(cls, str2);
                } catch (ClassNotFoundException e3) {
                    dprintThrowable(e3);
                    return null;
                }
            }
        } catch (MalformedURLException e4) {
            throw this.wrapper.malformedUrl(CompletionStatus.COMPLETED_MAYBE, e4, str, str2);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0049  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x004c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.Class getClassFromString(java.lang.String r7, java.lang.String r8) {
        /*
            r6 = this;
            r0 = r6
            com.sun.corba.se.impl.orbutil.RepositoryIdStrings r0 = r0.repIdStrs
            r1 = r7
            com.sun.corba.se.impl.orbutil.RepositoryIdInterface r0 = r0.getFromString(r1)
            r9 = r0
            r0 = 0
            r10 = r0
        Le:
            r0 = r10
            r1 = 3
            if (r0 >= r1) goto L70
            r0 = r10
            switch(r0) {
                case 0: goto L30;
                case 1: goto L37;
                case 2: goto L3a;
                default: goto L45;
            }     // Catch: java.lang.ClassNotFoundException -> L54 java.net.MalformedURLException -> L59
        L30:
            r0 = r9
            java.lang.Class r0 = r0.getClassFromType()     // Catch: java.lang.ClassNotFoundException -> L54 java.net.MalformedURLException -> L59
            return r0
        L37:
            goto L45
        L3a:
            r0 = r6
            com.sun.org.omg.SendingContext.CodeBase r0 = r0.getCodeBase()     // Catch: java.lang.ClassNotFoundException -> L54 java.net.MalformedURLException -> L59
            r1 = r7
            java.lang.String r0 = r0.implementation(r1)     // Catch: java.lang.ClassNotFoundException -> L54 java.net.MalformedURLException -> L59
            r8 = r0
        L45:
            r0 = r8
            if (r0 != 0) goto L4c
            goto L6a
        L4c:
            r0 = r9
            r1 = r8
            java.lang.Class r0 = r0.getClassFromType(r1)     // Catch: java.lang.ClassNotFoundException -> L54 java.net.MalformedURLException -> L59
            return r0
        L54:
            r11 = move-exception
            goto L6a
        L59:
            r11 = move-exception
            r0 = r6
            com.sun.corba.se.impl.logging.ORBUtilSystemException r0 = r0.wrapper
            org.omg.CORBA.CompletionStatus r1 = org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE
            r2 = r11
            r3 = r7
            r4 = r8
            org.omg.CORBA.MARSHAL r0 = r0.malformedUrl(r1, r2, r3, r4)
            throw r0
        L6a:
            int r10 = r10 + 1
            goto Le
        L70:
            r0 = r6
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = r1
            r2.<init>()
            java.lang.String r2 = "getClassFromString failed with rep id "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r7
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = " and codebase "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r8
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.dprint(r1)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.encoding.CDRInputStream_1_0.getClassFromString(java.lang.String, java.lang.String):java.lang.Class");
    }

    char[] getConvertedChars(int i2, CodeSetConversion.BTCConverter bTCConverter) {
        byte[] bArrArray;
        if (this.bbwi.buflen - this.bbwi.position() >= i2) {
            if (this.bbwi.byteBuffer.hasArray()) {
                bArrArray = this.bbwi.byteBuffer.array();
            } else {
                bArrArray = new byte[this.bbwi.buflen];
                for (int i3 = 0; i3 < this.bbwi.buflen; i3++) {
                    bArrArray[i3] = this.bbwi.byteBuffer.get(i3);
                }
            }
            char[] chars = bTCConverter.getChars(bArrArray, this.bbwi.position(), i2);
            this.bbwi.position(this.bbwi.position() + i2);
            return chars;
        }
        byte[] bArr = new byte[i2];
        read_octet_array(bArr, 0, bArr.length);
        return bTCConverter.getChars(bArr, 0, i2);
    }

    protected CodeSetConversion.BTCConverter getCharConverter() {
        if (this.charConverter == null) {
            this.charConverter = this.parent.createCharBTCConverter();
        }
        return this.charConverter;
    }

    protected CodeSetConversion.BTCConverter getWCharConverter() {
        if (this.wcharConverter == null) {
            this.wcharConverter = this.parent.createWCharBTCConverter();
        }
        return this.wcharConverter;
    }

    protected void dprintThrowable(Throwable th) {
        if (this.debug && th != null) {
            th.printStackTrace();
        }
    }

    protected void dprint(String str) {
        if (this.debug) {
            ORBUtility.dprint(this, str);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void alignOnBoundary(int i2) {
        int iComputeAlignment = computeAlignment(this.bbwi.position(), i2);
        if (this.bbwi.position() + iComputeAlignment <= this.bbwi.buflen) {
            this.bbwi.position(this.bbwi.position() + iComputeAlignment);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void resetCodeSetConverters() {
        this.charConverter = null;
        this.wcharConverter = null;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void start_value() {
        int valueTag = readValueTag();
        if (valueTag == 0) {
            this.specialNoOptionalDataState = true;
            return;
        }
        if (valueTag == -1) {
            throw this.wrapper.customWrapperIndirection(CompletionStatus.COMPLETED_MAYBE);
        }
        if (this.repIdUtil.isCodeBasePresent(valueTag)) {
            throw this.wrapper.customWrapperWithCodebase(CompletionStatus.COMPLETED_MAYBE);
        }
        if (this.repIdUtil.getTypeInfo(valueTag) != 2) {
            throw this.wrapper.customWrapperNotSingleRepid(CompletionStatus.COMPLETED_MAYBE);
        }
        read_repositoryId();
        start_block();
        this.end_flag--;
        this.chunkedValueNestingLevel--;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void end_value() {
        if (this.specialNoOptionalDataState) {
            this.specialNoOptionalDataState = false;
            return;
        }
        handleEndOfValue();
        readEndTag();
        start_block();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        CDROutputObject cDROutputObject;
        getBufferManager().close(this.bbwi);
        if (this.bbwi != null && getByteBuffer() != null) {
            MessageMediator messageMediator = this.parent.getMessageMediator();
            if (messageMediator != null && (cDROutputObject = (CDROutputObject) messageMediator.getOutputObject()) != null && cDROutputObject.isSharing(getByteBuffer())) {
                cDROutputObject.setByteBuffer(null);
                cDROutputObject.setByteBufferWithInfo(null);
            }
            ByteBufferPool byteBufferPool = this.orb.getByteBufferPool();
            if (this.debug) {
                int iIdentityHashCode = System.identityHashCode(this.bbwi.byteBuffer);
                StringBuffer stringBuffer = new StringBuffer(80);
                stringBuffer.append(".close - releasing ByteBuffer id (");
                stringBuffer.append(iIdentityHashCode).append(") to ByteBufferPool.");
                dprint(stringBuffer.toString());
            }
            byteBufferPool.releaseByteBuffer(this.bbwi.byteBuffer);
            this.bbwi.byteBuffer = null;
            this.bbwi = null;
        }
    }
}
