package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.PrincipalImpl;
import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.impl.util.Version;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.IORFactories;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.PresentationDefaults;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import org.omg.CORBA.Any;
import org.omg.CORBA.AnySeqHolder;
import org.omg.CORBA.BooleanSeqHolder;
import org.omg.CORBA.CharSeqHolder;
import org.omg.CORBA.Context;
import org.omg.CORBA.DoubleSeqHolder;
import org.omg.CORBA.FloatSeqHolder;
import org.omg.CORBA.LongLongSeqHolder;
import org.omg.CORBA.LongSeqHolder;
import org.omg.CORBA.MARSHAL;
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
import org.omg.CORBA.portable.IDLEntity;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/IDLJavaSerializationInputStream.class */
public class IDLJavaSerializationInputStream extends CDRInputStreamBase {
    private ORB orb;
    private int bufSize;
    private ByteBuffer buffer;
    private byte encodingVersion;
    private ObjectInputStream is;
    private _ByteArrayInputStream bis;
    private BufferManagerRead bufferManager;
    private boolean markOn;
    private int peekIndex;
    private int peekCount;
    protected ORBUtilSystemException wrapper;
    private final int directReadLength = 16;
    private LinkedList markedItemQ = new LinkedList();

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

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/IDLJavaSerializationInputStream$_ByteArrayInputStream.class */
    class _ByteArrayInputStream extends ByteArrayInputStream {
        _ByteArrayInputStream(byte[] bArr) {
            super(bArr);
        }

        int getPosition() {
            return this.pos;
        }

        void setPosition(int i2) {
            if (i2 < 0 || i2 > this.count) {
                throw new IndexOutOfBoundsException();
            }
            this.pos = i2;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/encoding/IDLJavaSerializationInputStream$MarshalObjectInputStream.class */
    class MarshalObjectInputStream extends ObjectInputStream {
        ORB orb;

        MarshalObjectInputStream(InputStream inputStream, ORB orb) throws IOException {
            super(inputStream);
            this.orb = orb;
            AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream.MarshalObjectInputStream.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    MarshalObjectInputStream.this.enableResolveObject(true);
                    return null;
                }
            });
        }

        @Override // java.io.ObjectInputStream
        protected final Object resolveObject(Object obj) throws IOException {
            try {
                if (StubAdapter.isStub(obj)) {
                    StubAdapter.connect(obj, this.orb);
                }
                return obj;
            } catch (RemoteException e2) {
                IOException iOException = new IOException("resolveObject failed");
                iOException.initCause(e2);
                throw iOException;
            }
        }
    }

    public IDLJavaSerializationInputStream(byte b2) {
        this.encodingVersion = b2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void init(org.omg.CORBA.ORB orb, ByteBuffer byteBuffer, int i2, boolean z2, BufferManagerRead bufferManagerRead) {
        byte[] bArrArray;
        this.orb = (ORB) orb;
        this.bufSize = i2;
        this.bufferManager = bufferManagerRead;
        this.buffer = byteBuffer;
        this.wrapper = ORBUtilSystemException.get((ORB) orb, CORBALogDomains.RPC_ENCODING);
        if (this.buffer.hasArray()) {
            bArrArray = this.buffer.array();
        } else {
            bArrArray = new byte[i2];
            this.buffer.get(bArrArray);
        }
        this.bis = new _ByteArrayInputStream(bArrArray);
    }

    private void initObjectInputStream() {
        if (this.is != null) {
            throw this.wrapper.javaStreamInitFailed();
        }
        try {
            this.is = new MarshalObjectInputStream(this.bis, this.orb);
        } catch (Exception e2) {
            throw this.wrapper.javaStreamInitFailed(e2);
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public boolean read_boolean() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Boolean) this.markedItemQ.removeFirst()).booleanValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return ((Boolean) linkedList.get(i2)).booleanValue();
        }
        try {
            boolean z2 = this.is.readBoolean();
            if (this.markOn) {
                this.markedItemQ.addLast(Boolean.valueOf(z2));
            }
            return z2;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_boolean");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public char read_char() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Character) this.markedItemQ.removeFirst()).charValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return ((Character) linkedList.get(i2)).charValue();
        }
        try {
            char c2 = this.is.readChar();
            if (this.markOn) {
                this.markedItemQ.addLast(new Character(c2));
            }
            return c2;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_char");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public char read_wchar() {
        return read_char();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public byte read_octet() {
        if (this.bis.getPosition() < 16) {
            byte b2 = (byte) this.bis.read();
            if (this.bis.getPosition() == 16) {
                initObjectInputStream();
            }
            return b2;
        }
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Byte) this.markedItemQ.removeFirst()).byteValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return ((Byte) linkedList.get(i2)).byteValue();
        }
        try {
            byte b3 = this.is.readByte();
            if (this.markOn) {
                this.markedItemQ.addLast(new Byte(b3));
            }
            return b3;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_octet");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public short read_short() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Short) this.markedItemQ.removeFirst()).shortValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return ((Short) linkedList.get(i2)).shortValue();
        }
        try {
            short s2 = this.is.readShort();
            if (this.markOn) {
                this.markedItemQ.addLast(new Short(s2));
            }
            return s2;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_short");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public short read_ushort() {
        return read_short();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int read_long() {
        if (this.bis.getPosition() < 16) {
            int i2 = (this.bis.read() << 24) & (-16777216);
            int i3 = (this.bis.read() << 16) & 16711680;
            int i4 = (this.bis.read() << 8) & NormalizerImpl.CC_MASK;
            int i5 = (this.bis.read() << 0) & 255;
            if (this.bis.getPosition() == 16) {
                initObjectInputStream();
            } else if (this.bis.getPosition() > 16) {
                this.wrapper.javaSerializationException("read_long");
            }
            return i2 | i3 | i4 | i5;
        }
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Integer) this.markedItemQ.removeFirst()).intValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i6 = this.peekIndex;
            this.peekIndex = i6 + 1;
            return ((Integer) linkedList.get(i6)).intValue();
        }
        try {
            int i7 = this.is.readInt();
            if (this.markOn) {
                this.markedItemQ.addLast(new Integer(i7));
            }
            return i7;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_long");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int read_ulong() {
        return read_long();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public long read_longlong() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Long) this.markedItemQ.removeFirst()).longValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return ((Long) linkedList.get(i2)).longValue();
        }
        try {
            long j2 = this.is.readLong();
            if (this.markOn) {
                this.markedItemQ.addLast(new Long(j2));
            }
            return j2;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_longlong");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public long read_ulonglong() {
        return read_longlong();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public float read_float() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Float) this.markedItemQ.removeFirst()).floatValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return ((Float) linkedList.get(i2)).floatValue();
        }
        try {
            float f2 = this.is.readFloat();
            if (this.markOn) {
                this.markedItemQ.addLast(new Float(f2));
            }
            return f2;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_float");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public double read_double() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return ((Double) this.markedItemQ.removeFirst()).doubleValue();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return ((Double) linkedList.get(i2)).doubleValue();
        }
        try {
            double d2 = this.is.readDouble();
            if (this.markOn) {
                this.markedItemQ.addLast(new Double(d2));
            }
            return d2;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_double");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public String read_string() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return (String) this.markedItemQ.removeFirst();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return (String) linkedList.get(i2);
        }
        try {
            String utf = this.is.readUTF();
            if (this.markOn) {
                this.markedItemQ.addLast(utf);
            }
            return utf;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_string");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public String read_wstring() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return (String) this.markedItemQ.removeFirst();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return (String) linkedList.get(i2);
        }
        try {
            String str = (String) this.is.readObject();
            if (this.markOn) {
                this.markedItemQ.addLast(str);
            }
            return str;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_wstring");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_boolean_array(boolean[] zArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            zArr[i4 + i2] = read_boolean();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_char_array(char[] cArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            cArr[i4 + i2] = read_char();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_wchar_array(char[] cArr, int i2, int i3) {
        read_char_array(cArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_octet_array(byte[] bArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            bArr[i4 + i2] = read_octet();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_short_array(short[] sArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            sArr[i4 + i2] = read_short();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_ushort_array(short[] sArr, int i2, int i3) {
        read_short_array(sArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_long_array(int[] iArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            iArr[i4 + i2] = read_long();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_ulong_array(int[] iArr, int i2, int i3) {
        read_long_array(iArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_longlong_array(long[] jArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            jArr[i4 + i2] = read_longlong();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_ulonglong_array(long[] jArr, int i2, int i3) {
        read_longlong_array(jArr, i2, i3);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_float_array(float[] fArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            fArr[i4 + i2] = read_float();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void read_double_array(double[] dArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            dArr[i4 + i2] = read_double();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Object read_Object() {
        return read_Object(null);
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
            e2.printStackTrace();
        }
        anyCreate_any.read_value(this.parent, typeCodeImpl);
        return anyCreate_any;
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
        return CDRInputStream_1_0.internalIORToObject(iorMakeIOR, stubFactoryCreateStubFactory, this.orb);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public org.omg.CORBA.ORB orb() {
        return this.orb;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value() {
        if (!this.markOn && !this.markedItemQ.isEmpty()) {
            return (Serializable) this.markedItemQ.removeFirst();
        }
        if (this.markOn && !this.markedItemQ.isEmpty() && this.peekIndex < this.peekCount) {
            LinkedList linkedList = this.markedItemQ;
            int i2 = this.peekIndex;
            this.peekIndex = i2 + 1;
            return (Serializable) linkedList.get(i2);
        }
        try {
            Serializable serializable = (Serializable) this.is.readObject();
            if (this.markOn) {
                this.markedItemQ.addLast(serializable);
            }
            return serializable;
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "read_value");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(Class cls) {
        return read_value();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(BoxedValueHelper boxedValueHelper) {
        return read_value();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(String str) {
        return read_value();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public Serializable read_value(Serializable serializable) {
        return read_value();
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
    public void consumeEndian() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int getPosition() {
        try {
            return this.bis.getPosition();
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "getPosition");
        }
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

    private final void read_any_array(Any[] anyArr, int i2, int i3) {
        for (int i4 = 0; i4 < i3; i4++) {
            anyArr[i4 + i2] = read_any();
        }
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
    public String[] _truncatable_ids() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public void mark(int i2) {
        if (this.markOn || this.is == null) {
            throw this.wrapper.javaSerializationException("mark");
        }
        this.markOn = true;
        if (!this.markedItemQ.isEmpty()) {
            this.peekIndex = 0;
            this.peekCount = this.markedItemQ.size();
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public void reset() {
        this.markOn = false;
        this.peekIndex = 0;
        this.peekCount = 0;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase, java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public CDRInputStreamBase dup() {
        try {
            CDRInputStreamBase cDRInputStreamBase = (CDRInputStreamBase) getClass().newInstance();
            cDRInputStreamBase.init(this.orb, this.buffer, this.bufSize, false, null);
            ((IDLJavaSerializationInputStream) cDRInputStreamBase).skipBytes(getPosition());
            ((IDLJavaSerializationInputStream) cDRInputStreamBase).setMarkData(this.markOn, this.peekIndex, this.peekCount, (LinkedList) this.markedItemQ.clone());
            return cDRInputStreamBase;
        } catch (Exception e2) {
            throw this.wrapper.couldNotDuplicateCdrInputStream(e2);
        }
    }

    void skipBytes(int i2) {
        try {
            this.is.skipBytes(i2);
        } catch (Exception e2) {
            throw this.wrapper.javaSerializationException(e2, "skipBytes");
        }
    }

    void setMarkData(boolean z2, int i2, int i3, LinkedList linkedList) {
        this.markOn = z2;
        this.peekIndex = i2;
        this.peekCount = i3;
        this.markedItemQ = linkedList;
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
    public boolean isLittleEndian() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void setHeaderPadding(boolean z2) {
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public ByteBuffer getByteBuffer() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setByteBuffer(ByteBuffer byteBuffer) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setByteBufferWithInfo(ByteBufferWithInfo byteBufferWithInfo) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int getBufferLength() {
        return this.bufSize;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setBufferLength(int i2) {
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public int getIndex() {
        return this.bis.getPosition();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void setIndex(int i2) {
        try {
            this.bis.setPosition(i2);
        } catch (IndexOutOfBoundsException e2) {
            throw this.wrapper.javaSerializationException(e2, "setIndex");
        }
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void orb(org.omg.CORBA.ORB orb) {
        this.orb = (ORB) orb;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public BufferManagerRead getBufferManager() {
        return this.bufferManager;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public GIOPVersion getGIOPVersion() {
        return GIOPVersion.V1_2;
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    CodeBase getCodeBase() {
        return this.parent.getCodeBase();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v44, types: [int] */
    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void printBuffer() {
        byte[] bArrArray = this.buffer.array();
        System.out.println("+++++++ Input Buffer ++++++++");
        System.out.println();
        System.out.println("Current position: " + getPosition());
        System.out.println("Total length : " + this.bufSize);
        System.out.println();
        char[] cArr = new char[16];
        for (int i2 = 0; i2 < bArrArray.length; i2 += 16) {
            try {
                int i3 = 0;
                while (i3 < 16 && i3 + i2 < bArrArray.length) {
                    byte b2 = bArrArray[i2 + i3];
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
                while (i4 < 16 && i4 + i2 < bArrArray.length) {
                    if (ORBUtility.isPrintable((char) bArrArray[i2 + i4])) {
                        cArr[i4] = (char) bArrArray[i2 + i4];
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

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void alignOnBoundary(int i2) {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    void performORBVersionSpecificInit() {
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void resetCodeSetConverters() {
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void start_value() {
        throw this.wrapper.giopVersionError();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStreamBase
    public void end_value() {
        throw this.wrapper.giopVersionError();
    }
}
