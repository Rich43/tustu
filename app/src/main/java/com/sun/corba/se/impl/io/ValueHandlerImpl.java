package com.sun.corba.se.impl.io;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.org.omg.SendingContext.CodeBase;
import com.sun.org.omg.SendingContext.CodeBaseHelper;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.rmi.Remote;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Hashtable;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandlerMultiFormat;
import org.omg.CORBA.Object;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.portable.IndirectionException;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.ValueOutputStream;
import org.omg.SendingContext.RunTime;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/ValueHandlerImpl.class */
public final class ValueHandlerImpl implements ValueHandlerMultiFormat {
    public static final String FORMAT_VERSION_PROPERTY = "com.sun.CORBA.MaxStreamFormatVersion";
    private static final byte MAX_SUPPORTED_FORMAT_VERSION = 2;
    private static final byte STREAM_FORMAT_VERSION_1 = 1;
    private static final byte MAX_STREAM_FORMAT_VERSION = getMaxStreamFormatVersion();
    public static final short kRemoteType = 0;
    public static final short kAbstractType = 1;
    public static final short kValueType = 2;
    private Hashtable inputStreamPairs;
    private Hashtable outputStreamPairs;
    private CodeBase codeBase;
    private boolean useHashtables;
    private boolean isInputStream;
    private IIOPOutputStream outputStreamBridge;
    private IIOPInputStream inputStreamBridge;
    private OMGSystemException omgWrapper;
    private UtilSystemException utilWrapper;

    private static byte getMaxStreamFormatVersion() {
        try {
            String str = (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.ValueHandlerImpl.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    return System.getProperty(ValueHandlerImpl.FORMAT_VERSION_PROPERTY);
                }
            });
            if (str == null) {
                return (byte) 2;
            }
            byte b2 = Byte.parseByte(str);
            if (b2 < 1 || b2 > 2) {
                throw new ExceptionInInitializerError("Invalid stream format version: " + ((int) b2) + ".  Valid range is 1 through 2");
            }
            return b2;
        } catch (Exception e2) {
            ExceptionInInitializerError exceptionInInitializerError = new ExceptionInInitializerError(e2);
            exceptionInInitializerError.initCause(e2);
            throw exceptionInInitializerError;
        }
    }

    @Override // javax.rmi.CORBA.ValueHandlerMultiFormat
    public byte getMaximumStreamFormatVersion() {
        return MAX_STREAM_FORMAT_VERSION;
    }

    @Override // javax.rmi.CORBA.ValueHandlerMultiFormat
    public void writeValue(OutputStream outputStream, Serializable serializable, byte b2) {
        if (b2 == 2) {
            if (!(outputStream instanceof ValueOutputStream)) {
                throw this.omgWrapper.notAValueoutputstream();
            }
        } else if (b2 != 1) {
            throw this.omgWrapper.invalidStreamFormatVersion(new Integer(b2));
        }
        writeValueWithVersion(outputStream, serializable, b2);
    }

    private ValueHandlerImpl() {
        this.inputStreamPairs = null;
        this.outputStreamPairs = null;
        this.codeBase = null;
        this.useHashtables = true;
        this.isInputStream = true;
        this.outputStreamBridge = null;
        this.inputStreamBridge = null;
        this.omgWrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);
        this.utilWrapper = UtilSystemException.get(CORBALogDomains.RPC_ENCODING);
    }

    private ValueHandlerImpl(boolean z2) {
        this();
        this.useHashtables = false;
        this.isInputStream = z2;
    }

    static ValueHandlerImpl getInstance() {
        return new ValueHandlerImpl();
    }

    static ValueHandlerImpl getInstance(boolean z2) {
        return new ValueHandlerImpl(z2);
    }

    @Override // javax.rmi.CORBA.ValueHandler
    public void writeValue(OutputStream outputStream, Serializable serializable) {
        writeValueWithVersion(outputStream, serializable, (byte) 1);
    }

    private void writeValueWithVersion(OutputStream outputStream, Serializable serializable, byte b2) {
        org.omg.CORBA_2_3.portable.OutputStream outputStream2 = (org.omg.CORBA_2_3.portable.OutputStream) outputStream;
        if (!this.useHashtables) {
            if (this.outputStreamBridge == null) {
                this.outputStreamBridge = createOutputStream();
                this.outputStreamBridge.setOrbStream(outputStream2);
            }
            try {
                this.outputStreamBridge.increaseRecursionDepth();
                writeValueInternal(this.outputStreamBridge, outputStream2, serializable, b2);
                this.outputStreamBridge.decreaseRecursionDepth();
                return;
            } catch (Throwable th) {
                this.outputStreamBridge.decreaseRecursionDepth();
                throw th;
            }
        }
        if (this.outputStreamPairs == null) {
            this.outputStreamPairs = new Hashtable();
        }
        IIOPOutputStream iIOPOutputStreamCreateOutputStream = (IIOPOutputStream) this.outputStreamPairs.get(outputStream);
        if (iIOPOutputStreamCreateOutputStream == null) {
            iIOPOutputStreamCreateOutputStream = createOutputStream();
            iIOPOutputStreamCreateOutputStream.setOrbStream(outputStream2);
            this.outputStreamPairs.put(outputStream, iIOPOutputStreamCreateOutputStream);
        }
        try {
            iIOPOutputStreamCreateOutputStream.increaseRecursionDepth();
            writeValueInternal(iIOPOutputStreamCreateOutputStream, outputStream2, serializable, b2);
            if (iIOPOutputStreamCreateOutputStream.decreaseRecursionDepth() == 0) {
                this.outputStreamPairs.remove(outputStream);
            }
        } catch (Throwable th2) {
            if (iIOPOutputStreamCreateOutputStream.decreaseRecursionDepth() == 0) {
                this.outputStreamPairs.remove(outputStream);
            }
            throw th2;
        }
    }

    private void writeValueInternal(IIOPOutputStream iIOPOutputStream, org.omg.CORBA_2_3.portable.OutputStream outputStream, Serializable serializable, byte b2) {
        Class<?> cls = serializable.getClass();
        if (cls.isArray()) {
            write_Array(outputStream, serializable, cls.getComponentType());
        } else {
            iIOPOutputStream.simpleWriteObject(serializable, b2);
        }
    }

    @Override // javax.rmi.CORBA.ValueHandler
    public Serializable readValue(InputStream inputStream, int i2, Class cls, String str, RunTime runTime) {
        CodeBase codeBaseNarrow = CodeBaseHelper.narrow(runTime);
        org.omg.CORBA_2_3.portable.InputStream inputStream2 = (org.omg.CORBA_2_3.portable.InputStream) inputStream;
        if (!this.useHashtables) {
            if (this.inputStreamBridge == null) {
                this.inputStreamBridge = createInputStream();
                this.inputStreamBridge.setOrbStream(inputStream2);
                this.inputStreamBridge.setSender(codeBaseNarrow);
                this.inputStreamBridge.setValueHandler(this);
            }
            try {
                this.inputStreamBridge.increaseRecursionDepth();
                Serializable valueInternal = readValueInternal(this.inputStreamBridge, inputStream2, i2, cls, str, codeBaseNarrow);
                if (this.inputStreamBridge.decreaseRecursionDepth() == 0) {
                }
                return valueInternal;
            } catch (Throwable th) {
                if (this.inputStreamBridge.decreaseRecursionDepth() == 0) {
                }
                throw th;
            }
        }
        if (this.inputStreamPairs == null) {
            this.inputStreamPairs = new Hashtable();
        }
        IIOPInputStream iIOPInputStreamCreateInputStream = (IIOPInputStream) this.inputStreamPairs.get(inputStream);
        if (iIOPInputStreamCreateInputStream == null) {
            iIOPInputStreamCreateInputStream = createInputStream();
            iIOPInputStreamCreateInputStream.setOrbStream(inputStream2);
            iIOPInputStreamCreateInputStream.setSender(codeBaseNarrow);
            iIOPInputStreamCreateInputStream.setValueHandler(this);
            this.inputStreamPairs.put(inputStream, iIOPInputStreamCreateInputStream);
        }
        try {
            iIOPInputStreamCreateInputStream.increaseRecursionDepth();
            Serializable valueInternal2 = readValueInternal(iIOPInputStreamCreateInputStream, inputStream2, i2, cls, str, codeBaseNarrow);
            if (iIOPInputStreamCreateInputStream.decreaseRecursionDepth() == 0) {
                this.inputStreamPairs.remove(inputStream);
            }
            return valueInternal2;
        } catch (Throwable th2) {
            if (iIOPInputStreamCreateInputStream.decreaseRecursionDepth() == 0) {
                this.inputStreamPairs.remove(inputStream);
            }
            throw th2;
        }
    }

    private Serializable readValueInternal(IIOPInputStream iIOPInputStream, org.omg.CORBA_2_3.portable.InputStream inputStream, int i2, Class cls, String str, CodeBase codeBase) {
        Serializable serializable;
        if (cls == null) {
            if (isArray(str)) {
                read_Array(iIOPInputStream, inputStream, null, codeBase, i2);
            } else {
                iIOPInputStream.simpleSkipObject(str, codeBase);
            }
            return null;
        }
        if (cls.isArray()) {
            serializable = (Serializable) read_Array(iIOPInputStream, inputStream, cls, codeBase, i2);
        } else {
            serializable = (Serializable) iIOPInputStream.simpleReadObject(cls, str, codeBase, i2);
        }
        return serializable;
    }

    @Override // javax.rmi.CORBA.ValueHandler
    public String getRMIRepositoryID(Class cls) {
        return RepositoryId.createForJavaType(cls);
    }

    @Override // javax.rmi.CORBA.ValueHandler
    public boolean isCustomMarshaled(Class cls) {
        return ObjectStreamClass.lookup(cls).isCustomMarshaled();
    }

    @Override // javax.rmi.CORBA.ValueHandler
    public RunTime getRunTimeCodeBase() {
        if (this.codeBase != null) {
            return this.codeBase;
        }
        this.codeBase = new FVDCodeBaseImpl();
        ((FVDCodeBaseImpl) this.codeBase).setValueHandler(this);
        return this.codeBase;
    }

    public boolean useFullValueDescription(Class cls, String str) throws IOException {
        return RepositoryId.useFullValueDescription(cls, str);
    }

    public String getClassName(String str) {
        return RepositoryId.cache.getId(str).getClassName();
    }

    public Class getClassFromType(String str) throws ClassNotFoundException {
        return RepositoryId.cache.getId(str).getClassFromType();
    }

    public Class getAnyClassFromType(String str) throws ClassNotFoundException {
        return RepositoryId.cache.getId(str).getAnyClassFromType();
    }

    public String createForAnyType(Class cls) {
        return RepositoryId.createForAnyType(cls);
    }

    public String getDefinedInId(String str) {
        return RepositoryId.cache.getId(str).getDefinedInId();
    }

    public String getUnqualifiedName(String str) {
        return RepositoryId.cache.getId(str).getUnqualifiedName();
    }

    public String getSerialVersionUID(String str) {
        return RepositoryId.cache.getId(str).getSerialVersionUID();
    }

    public boolean isAbstractBase(Class cls) {
        return RepositoryId.isAbstractBase(cls);
    }

    public boolean isSequence(String str) {
        return RepositoryId.cache.getId(str).isSequence();
    }

    @Override // javax.rmi.CORBA.ValueHandler
    public Serializable writeReplace(Serializable serializable) {
        return ObjectStreamClass.lookup(serializable.getClass()).writeReplace(serializable);
    }

    private void writeCharArray(org.omg.CORBA_2_3.portable.OutputStream outputStream, char[] cArr, int i2, int i3) {
        outputStream.write_wchar_array(cArr, i2, i3);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void write_Array(org.omg.CORBA_2_3.portable.OutputStream outputStream, Serializable serializable, Class cls) {
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                int[] iArr = (int[]) serializable;
                int length = iArr.length;
                outputStream.write_ulong(length);
                outputStream.write_long_array(iArr, 0, length);
                return;
            }
            if (cls == Byte.TYPE) {
                byte[] bArr = (byte[]) serializable;
                int length2 = bArr.length;
                outputStream.write_ulong(length2);
                outputStream.write_octet_array(bArr, 0, length2);
                return;
            }
            if (cls == Long.TYPE) {
                long[] jArr = (long[]) serializable;
                int length3 = jArr.length;
                outputStream.write_ulong(length3);
                outputStream.write_longlong_array(jArr, 0, length3);
                return;
            }
            if (cls == Float.TYPE) {
                float[] fArr = (float[]) serializable;
                int length4 = fArr.length;
                outputStream.write_ulong(length4);
                outputStream.write_float_array(fArr, 0, length4);
                return;
            }
            if (cls == Double.TYPE) {
                double[] dArr = (double[]) serializable;
                int length5 = dArr.length;
                outputStream.write_ulong(length5);
                outputStream.write_double_array(dArr, 0, length5);
                return;
            }
            if (cls == Short.TYPE) {
                short[] sArr = (short[]) serializable;
                int length6 = sArr.length;
                outputStream.write_ulong(length6);
                outputStream.write_short_array(sArr, 0, length6);
                return;
            }
            if (cls == Character.TYPE) {
                char[] cArr = (char[]) serializable;
                int length7 = cArr.length;
                outputStream.write_ulong(length7);
                writeCharArray(outputStream, cArr, 0, length7);
                return;
            }
            if (cls == Boolean.TYPE) {
                boolean[] zArr = (boolean[]) serializable;
                int length8 = zArr.length;
                outputStream.write_ulong(length8);
                outputStream.write_boolean_array(zArr, 0, length8);
                return;
            }
            throw new Error("Invalid primitive type : " + serializable.getClass().getName());
        }
        if (cls == Object.class) {
            Object[] objArr = (Object[]) serializable;
            outputStream.write_ulong(objArr.length);
            for (Object obj : objArr) {
                Util.writeAny(outputStream, obj);
            }
            return;
        }
        Object[] objArr2 = (Object[]) serializable;
        int length9 = objArr2.length;
        outputStream.write_ulong(length9);
        boolean z2 = 2;
        if (cls.isInterface()) {
            cls.getName();
            if (Remote.class.isAssignableFrom(cls) || Object.class.isAssignableFrom(cls)) {
                z2 = false;
            } else if (RepositoryId.isAbstractBase(cls) || ObjectStreamClassCorbaExt.isAbstractInterface(cls)) {
                z2 = true;
            }
        }
        for (int i2 = 0; i2 < length9; i2++) {
            switch (z2) {
                case false:
                    Util.writeRemoteObject(outputStream, objArr2[i2]);
                    break;
                case true:
                    Util.writeAbstractObject(outputStream, objArr2[i2]);
                    break;
                case true:
                    try {
                        outputStream.write_value((Serializable) objArr2[i2]);
                        break;
                    } catch (ClassCastException e2) {
                        if (objArr2[i2] instanceof Serializable) {
                            throw e2;
                        }
                        Utility.throwNotSerializableForCorba(objArr2[i2].getClass().getName());
                        break;
                    }
            }
        }
    }

    private void readCharArray(org.omg.CORBA_2_3.portable.InputStream inputStream, char[] cArr, int i2, int i3) {
        inputStream.read_wchar_array(cArr, i2, i3);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Multi-variable type inference failed */
    private Object read_Array(IIOPInputStream iIOPInputStream, org.omg.CORBA_2_3.portable.InputStream inputStream, Class cls, CodeBase codeBase, int i2) {
        int i3;
        Object object;
        try {
            i3 = inputStream.read_ulong();
        } finally {
        }
        if (cls == null) {
            for (int i4 = 0; i4 < i3; i4++) {
                inputStream.read_value();
            }
            return null;
        }
        Class componentType = cls.getComponentType();
        Class clsLoadStubClass = componentType;
        if (componentType.isPrimitive()) {
            if (componentType == Integer.TYPE) {
                int[] iArr = new int[i3];
                inputStream.read_long_array(iArr, 0, i3);
                Serializable serializable = (Serializable) iArr;
                iIOPInputStream.activeRecursionMgr.removeObject(i2);
                return serializable;
            }
            if (componentType == Byte.TYPE) {
                byte[] bArr = new byte[i3];
                inputStream.read_octet_array(bArr, 0, i3);
                Serializable serializable2 = (Serializable) bArr;
                iIOPInputStream.activeRecursionMgr.removeObject(i2);
                return serializable2;
            }
            if (componentType == Long.TYPE) {
                long[] jArr = new long[i3];
                inputStream.read_longlong_array(jArr, 0, i3);
                Serializable serializable3 = (Serializable) jArr;
                iIOPInputStream.activeRecursionMgr.removeObject(i2);
                return serializable3;
            }
            if (componentType == Float.TYPE) {
                float[] fArr = new float[i3];
                inputStream.read_float_array(fArr, 0, i3);
                Serializable serializable4 = (Serializable) fArr;
                iIOPInputStream.activeRecursionMgr.removeObject(i2);
                return serializable4;
            }
            if (componentType == Double.TYPE) {
                double[] dArr = new double[i3];
                inputStream.read_double_array(dArr, 0, i3);
                Serializable serializable5 = (Serializable) dArr;
                iIOPInputStream.activeRecursionMgr.removeObject(i2);
                return serializable5;
            }
            if (componentType == Short.TYPE) {
                short[] sArr = new short[i3];
                inputStream.read_short_array(sArr, 0, i3);
                Serializable serializable6 = (Serializable) sArr;
                iIOPInputStream.activeRecursionMgr.removeObject(i2);
                return serializable6;
            }
            if (componentType == Character.TYPE) {
                char[] cArr = new char[i3];
                readCharArray(inputStream, cArr, 0, i3);
                Serializable serializable7 = (Serializable) cArr;
                iIOPInputStream.activeRecursionMgr.removeObject(i2);
                return serializable7;
            }
            if (componentType != Boolean.TYPE) {
                throw new Error("Invalid primitive componentType : " + cls.getName());
            }
            boolean[] zArr = new boolean[i3];
            inputStream.read_boolean_array(zArr, 0, i3);
            Serializable serializable8 = (Serializable) zArr;
            iIOPInputStream.activeRecursionMgr.removeObject(i2);
            return serializable8;
        }
        if (componentType == Object.class) {
            Object[] objArr = (Object[]) Array.newInstance((Class<?>) componentType, i3);
            iIOPInputStream.activeRecursionMgr.addObject(i2, objArr);
            for (int i5 = 0; i5 < i3; i5++) {
                try {
                    object = Util.readAny(inputStream);
                } catch (IndirectionException e2) {
                    try {
                        object = iIOPInputStream.activeRecursionMgr.getObject(e2.offset);
                    } catch (IOException e3) {
                        throw this.utilWrapper.invalidIndirection(e3, new Integer(e2.offset));
                    }
                }
                objArr[i5] = object;
            }
            Serializable serializable9 = (Serializable) objArr;
            iIOPInputStream.activeRecursionMgr.removeObject(i2);
            return serializable9;
        }
        Object[] objArr2 = (Object[]) Array.newInstance((Class<?>) componentType, i3);
        iIOPInputStream.activeRecursionMgr.addObject(i2, objArr2);
        boolean z2 = 2;
        boolean z3 = false;
        if (componentType.isInterface()) {
            boolean z4 = false;
            if (Remote.class.isAssignableFrom(componentType) || Object.class.isAssignableFrom(componentType)) {
                z2 = false;
                z4 = true;
            } else if (RepositoryId.isAbstractBase(componentType)) {
                z2 = true;
                z4 = true;
            } else if (ObjectStreamClassCorbaExt.isAbstractInterface(componentType)) {
                z2 = true;
            }
            if (z4) {
                try {
                    clsLoadStubClass = Utility.loadStubClass(RepositoryId.createForAnyType(componentType), Util.getCodebase(componentType), componentType);
                } catch (ClassNotFoundException e4) {
                    z3 = true;
                }
            } else {
                z3 = true;
            }
        }
        for (int i6 = 0; i6 < i3; i6++) {
            try {
            } catch (IndirectionException e5) {
                try {
                    objArr2[i6] = iIOPInputStream.activeRecursionMgr.getObject(e5.offset);
                } catch (IOException e6) {
                    throw this.utilWrapper.invalidIndirection(e6, new Integer(e5.offset));
                }
            }
            switch (z2) {
                case false:
                    if (z3) {
                        objArr2[i6] = Utility.readObjectAndNarrow(inputStream, clsLoadStubClass);
                    } else {
                        objArr2[i6] = inputStream.read_Object(clsLoadStubClass);
                    }
                case true:
                    if (z3) {
                        objArr2[i6] = Utility.readAbstractAndNarrow(inputStream, clsLoadStubClass);
                    } else {
                        objArr2[i6] = inputStream.read_abstract_interface(clsLoadStubClass);
                    }
                case true:
                    objArr2[i6] = inputStream.read_value(clsLoadStubClass);
                default:
            }
        }
        Serializable serializable10 = (Serializable) objArr2;
        iIOPInputStream.activeRecursionMgr.removeObject(i2);
        return serializable10;
        iIOPInputStream.activeRecursionMgr.removeObject(i2);
    }

    private boolean isArray(String str) {
        return RepositoryId.cache.getId(str).isSequence();
    }

    private String getOutputStreamClassName() {
        return "com.sun.corba.se.impl.io.IIOPOutputStream";
    }

    private IIOPOutputStream createOutputStream() {
        String outputStreamClassName = getOutputStreamClassName();
        try {
            IIOPOutputStream iIOPOutputStreamCreateOutputStreamBuiltIn = createOutputStreamBuiltIn(outputStreamClassName);
            if (iIOPOutputStreamCreateOutputStreamBuiltIn != null) {
                return iIOPOutputStreamCreateOutputStreamBuiltIn;
            }
            return (IIOPOutputStream) createCustom(IIOPOutputStream.class, outputStreamClassName);
        } catch (Throwable th) {
            InternalError internalError = new InternalError("Error loading " + outputStreamClassName);
            internalError.initCause(th);
            throw internalError;
        }
    }

    private IIOPOutputStream createOutputStreamBuiltIn(final String str) throws Throwable {
        try {
            return (IIOPOutputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<IIOPOutputStream>() { // from class: com.sun.corba.se.impl.io.ValueHandlerImpl.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public IIOPOutputStream run() throws IOException {
                    return ValueHandlerImpl.this.createOutputStreamBuiltInNoPriv(str);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw e2.getCause();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IIOPOutputStream createOutputStreamBuiltInNoPriv(String str) throws IOException {
        if (str.equals(IIOPOutputStream.class.getName())) {
            return new IIOPOutputStream();
        }
        return null;
    }

    private String getInputStreamClassName() {
        return "com.sun.corba.se.impl.io.IIOPInputStream";
    }

    private IIOPInputStream createInputStream() {
        String inputStreamClassName = getInputStreamClassName();
        try {
            IIOPInputStream iIOPInputStreamCreateInputStreamBuiltIn = createInputStreamBuiltIn(inputStreamClassName);
            if (iIOPInputStreamCreateInputStreamBuiltIn != null) {
                return iIOPInputStreamCreateInputStreamBuiltIn;
            }
            return (IIOPInputStream) createCustom(IIOPInputStream.class, inputStreamClassName);
        } catch (Throwable th) {
            InternalError internalError = new InternalError("Error loading " + inputStreamClassName);
            internalError.initCause(th);
            throw internalError;
        }
    }

    private IIOPInputStream createInputStreamBuiltIn(final String str) throws Throwable {
        try {
            return (IIOPInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<IIOPInputStream>() { // from class: com.sun.corba.se.impl.io.ValueHandlerImpl.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public IIOPInputStream run() throws IOException {
                    return ValueHandlerImpl.this.createInputStreamBuiltInNoPriv(str);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw e2.getCause();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IIOPInputStream createInputStreamBuiltInNoPriv(String str) throws IOException {
        if (str.equals(IIOPInputStream.class.getName())) {
            return new IIOPInputStream();
        }
        return null;
    }

    private <T> T createCustom(Class<T> cls, String str) throws Throwable {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            contextClassLoader = ClassLoader.getSystemClassLoader();
        }
        return (T) contextClassLoader.loadClass(str).asSubclass(cls).newInstance();
    }

    TCKind getJavaCharTCKind() {
        return TCKind.tk_wchar;
    }
}
