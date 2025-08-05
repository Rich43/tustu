package com.sun.corba.se.impl.io;

import com.sun.corba.se.impl.io.InputStreamHook;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.EOFException;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputValidation;
import java.io.StreamCorruptedException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javafx.fxml.FXMLLoader;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandler;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.portable.IndirectionException;
import org.omg.CORBA.portable.ValueInputStream;
import org.omg.CORBA_2_3.portable.InputStream;
import sun.corba.Bridge;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/IIOPInputStream.class */
public class IIOPInputStream extends InputStreamHook {
    private InputStream orbStream;
    private CodeBase cbSender;
    private ValueHandlerImpl vhandler;
    private Vector callbacks;
    ObjectStreamClass[] classdesc;
    Class[] classes;
    int spClass;
    private static final String kEmptyStr = "";
    private static final boolean useFVDOnly = false;
    private byte streamFormatVersion;
    private static Bridge bridge = (Bridge) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.IIOPInputStream.1
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            return Bridge.get();
        }
    });
    private static OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.RPC_ENCODING);
    private static UtilSystemException utilWrapper = UtilSystemException.get(CORBALogDomains.RPC_ENCODING);
    public static final TypeCode kRemoteTypeCode = ORB.init().get_primitive_tc(TCKind.tk_objref);
    public static final TypeCode kValueTypeCode = ORB.init().get_primitive_tc(TCKind.tk_value);
    private static final Constructor OPT_DATA_EXCEPTION_CTOR = getOptDataExceptionCtor();
    private ValueMember[] defaultReadObjectFVDMembers = null;
    private Object currentObject = null;
    private ObjectStreamClass currentClassDesc = null;
    private Class currentClass = null;
    private int recursionDepth = 0;
    private int simpleReadDepth = 0;
    ActiveRecursionManager activeRecursionMgr = new ActiveRecursionManager();
    private IOException abortIOException = null;
    private ClassNotFoundException abortClassNotFoundException = null;
    private Object[] readObjectArgList = {this};

    private static Constructor getOptDataExceptionCtor() {
        try {
            Constructor constructor = (Constructor) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.impl.io.IIOPInputStream.2
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws NoSuchMethodException, SecurityException {
                    Constructor declaredConstructor = java.io.OptionalDataException.class.getDeclaredConstructor(Boolean.TYPE);
                    declaredConstructor.setAccessible(true);
                    return declaredConstructor;
                }
            });
            if (constructor == null) {
                throw new Error("Unable to find OptionalDataException constructor");
            }
            return constructor;
        } catch (Exception e2) {
            throw new ExceptionInInitializerError(e2);
        }
    }

    private java.io.OptionalDataException createOptionalDataException() {
        try {
            java.io.OptionalDataException optionalDataException = (java.io.OptionalDataException) OPT_DATA_EXCEPTION_CTOR.newInstance(Boolean.TRUE);
            if (optionalDataException == null) {
                throw new Error("Created null OptionalDataException");
            }
            return optionalDataException;
        } catch (Exception e2) {
            throw new Error("Couldn't create OptionalDataException", e2);
        }
    }

    @Override // com.sun.corba.se.impl.io.InputStreamHook
    protected byte getStreamFormatVersion() {
        return this.streamFormatVersion;
    }

    private void readFormatVersion() throws IOException {
        this.streamFormatVersion = this.orbStream.read_octet();
        if (this.streamFormatVersion < 1 || this.streamFormatVersion > this.vhandler.getMaximumStreamFormatVersion()) {
            MARSHAL marshalUnsupportedFormatVersion = omgWrapper.unsupportedFormatVersion(CompletionStatus.COMPLETED_MAYBE);
            IOException iOException = new IOException("Unsupported format version: " + ((int) this.streamFormatVersion));
            iOException.initCause(marshalUnsupportedFormatVersion);
            throw iOException;
        }
        if (this.streamFormatVersion == 2 && !(this.orbStream instanceof ValueInputStream)) {
            BAD_PARAM bad_paramNotAValueinputstream = omgWrapper.notAValueinputstream(CompletionStatus.COMPLETED_MAYBE);
            IOException iOException2 = new IOException("Not a ValueInputStream");
            iOException2.initCause(bad_paramNotAValueinputstream);
            throw iOException2;
        }
    }

    public static void setTestFVDFlag(boolean z2) {
    }

    public IIOPInputStream() throws IOException {
        resetStream();
    }

    final void setOrbStream(InputStream inputStream) {
        this.orbStream = inputStream;
    }

    @Override // com.sun.corba.se.impl.io.InputStreamHook
    final InputStream getOrbStream() {
        return this.orbStream;
    }

    public final void setSender(CodeBase codeBase) {
        this.cbSender = codeBase;
    }

    public final CodeBase getSender() {
        return this.cbSender;
    }

    public final void setValueHandler(ValueHandler valueHandler) {
        this.vhandler = (ValueHandlerImpl) valueHandler;
    }

    public final ValueHandler getValueHandler() {
        return this.vhandler;
    }

    final void increaseRecursionDepth() {
        this.recursionDepth++;
    }

    final int decreaseRecursionDepth() {
        int i2 = this.recursionDepth - 1;
        this.recursionDepth = i2;
        return i2;
    }

    public final synchronized Object readObjectDelegate() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_abstract_interface();
        } catch (MARSHAL e2) {
            handleOptionalDataMarshalException(e2, true);
            throw e2;
        } catch (IndirectionException e3) {
            return this.activeRecursionMgr.getObject(e3.offset);
        }
    }

    final synchronized Object simpleReadObject(Class cls, String str, CodeBase codeBase, int i2) {
        Object obj = this.currentObject;
        ObjectStreamClass objectStreamClass = this.currentClassDesc;
        Class cls2 = this.currentClass;
        byte b2 = this.streamFormatVersion;
        this.simpleReadDepth++;
        try {
            try {
                Object resolve = this.currentClassDesc.readResolve(this.vhandler.useFullValueDescription(cls, str) ? inputObjectUsingFVD(cls, str, codeBase, i2) : inputObject(cls, str, codeBase, i2));
                this.simpleReadDepth--;
                this.currentObject = obj;
                this.currentClassDesc = objectStreamClass;
                this.currentClass = cls2;
                this.streamFormatVersion = b2;
                IOException iOException = this.abortIOException;
                if (this.simpleReadDepth == 0) {
                    this.abortIOException = null;
                }
                if (iOException != null) {
                    bridge.throwException(iOException);
                    return null;
                }
                ClassNotFoundException classNotFoundException = this.abortClassNotFoundException;
                if (this.simpleReadDepth == 0) {
                    this.abortClassNotFoundException = null;
                }
                if (classNotFoundException == null) {
                    return resolve;
                }
                bridge.throwException(classNotFoundException);
                return null;
            } catch (IOException e2) {
                bridge.throwException(e2);
                this.simpleReadDepth--;
                this.currentObject = obj;
                this.currentClassDesc = objectStreamClass;
                this.currentClass = cls2;
                this.streamFormatVersion = b2;
                return null;
            } catch (ClassNotFoundException e3) {
                bridge.throwException(e3);
                this.simpleReadDepth--;
                this.currentObject = obj;
                this.currentClassDesc = objectStreamClass;
                this.currentClass = cls2;
                this.streamFormatVersion = b2;
                return null;
            }
        } catch (Throwable th) {
            this.simpleReadDepth--;
            this.currentObject = obj;
            this.currentClassDesc = objectStreamClass;
            this.currentClass = cls2;
            this.streamFormatVersion = b2;
            throw th;
        }
    }

    public final synchronized void simpleSkipObject(String str, CodeBase codeBase) {
        Object obj = this.currentObject;
        ObjectStreamClass objectStreamClass = this.currentClassDesc;
        Class cls = this.currentClass;
        byte b2 = this.streamFormatVersion;
        this.simpleReadDepth++;
        try {
            try {
                try {
                    skipObjectUsingFVD(str, codeBase);
                    this.simpleReadDepth--;
                    this.streamFormatVersion = b2;
                    this.currentObject = obj;
                    this.currentClassDesc = objectStreamClass;
                    this.currentClass = cls;
                    IOException iOException = this.abortIOException;
                    if (this.simpleReadDepth == 0) {
                        this.abortIOException = null;
                    }
                    if (iOException != null) {
                        bridge.throwException(iOException);
                        return;
                    }
                    ClassNotFoundException classNotFoundException = this.abortClassNotFoundException;
                    if (this.simpleReadDepth == 0) {
                        this.abortClassNotFoundException = null;
                    }
                    if (classNotFoundException != null) {
                        bridge.throwException(classNotFoundException);
                    }
                } catch (IOException e2) {
                    bridge.throwException(e2);
                    this.simpleReadDepth--;
                    this.streamFormatVersion = b2;
                    this.currentObject = obj;
                    this.currentClassDesc = objectStreamClass;
                    this.currentClass = cls;
                }
            } catch (ClassNotFoundException e3) {
                bridge.throwException(e3);
                this.simpleReadDepth--;
                this.streamFormatVersion = b2;
                this.currentObject = obj;
                this.currentClassDesc = objectStreamClass;
                this.currentClass = cls;
            }
        } catch (Throwable th) {
            this.simpleReadDepth--;
            this.streamFormatVersion = b2;
            this.currentObject = obj;
            this.currentClassDesc = objectStreamClass;
            this.currentClass = cls;
            throw th;
        }
    }

    @Override // java.io.ObjectInputStream
    protected final Object readObjectOverride() throws ClassNotFoundException, IOException {
        return readObjectDelegate();
    }

    @Override // com.sun.corba.se.impl.io.InputStreamHook
    final synchronized void defaultReadObjectDelegate() throws IOException, ClassCastException {
        try {
            if (this.currentObject == null || this.currentClassDesc == null) {
                throw new NotActiveException("defaultReadObjectDelegate");
            }
            if (!this.currentClassDesc.forClass().isAssignableFrom(this.currentObject.getClass())) {
                throw new IOException("Object Type mismatch");
            }
            if (this.defaultReadObjectFVDMembers != null && this.defaultReadObjectFVDMembers.length > 0) {
                inputClassFields(this.currentObject, this.currentClass, this.currentClassDesc, this.defaultReadObjectFVDMembers, this.cbSender);
            } else {
                ObjectStreamField[] fieldsNoCopy = this.currentClassDesc.getFieldsNoCopy();
                if (fieldsNoCopy.length > 0) {
                    inputClassFields(this.currentObject, this.currentClass, fieldsNoCopy, this.cbSender);
                }
            }
        } catch (NotActiveException e2) {
            bridge.throwException(e2);
        } catch (IOException e3) {
            bridge.throwException(e3);
        } catch (ClassNotFoundException e4) {
            bridge.throwException(e4);
        }
    }

    public final boolean enableResolveObjectDelegate(boolean z2) {
        return false;
    }

    @Override // java.io.InputStream
    public final void mark(int i2) {
        this.orbStream.mark(i2);
    }

    @Override // java.io.InputStream
    public final boolean markSupported() {
        return this.orbStream.markSupported();
    }

    @Override // java.io.InputStream
    public final void reset() throws IOException {
        try {
            this.orbStream.reset();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.InputStream
    public final int available() throws IOException {
        return 0;
    }

    @Override // java.io.ObjectInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
    }

    @Override // java.io.ObjectInputStream, java.io.InputStream
    public final int read() throws IOException {
        try {
            this.readObjectState.readData(this);
            return (this.orbStream.read_octet() << 0) & 255;
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            if (e3.minor == 1330446344) {
                setState(IN_READ_OBJECT_NO_MORE_OPT_DATA);
                return -1;
            }
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.InputStream
    public final int read(byte[] bArr, int i2, int i3) throws IOException {
        try {
            this.readObjectState.readData(this);
            this.orbStream.read_octet_array(bArr, i2, i3);
            return i3;
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            if (e3.minor == 1330446344) {
                setState(IN_READ_OBJECT_NO_MORE_OPT_DATA);
                return -1;
            }
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final boolean readBoolean() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_boolean();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final byte readByte() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_octet();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final char readChar() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_wchar();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final double readDouble() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_double();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final float readFloat() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_float();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final void readFully(byte[] bArr) throws Throwable {
        readFully(bArr, 0, bArr.length);
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final void readFully(byte[] bArr, int i2, int i3) throws Throwable {
        try {
            this.readObjectState.readData(this);
            this.orbStream.read_octet_array(bArr, i2, i3);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final int readInt() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_long();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final String readLine() throws IOException {
        throw new IOException("Method readLine not supported");
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final long readLong() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_longlong();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final short readShort() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return this.orbStream.read_short();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream
    protected final void readStreamHeader() throws IOException {
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final int readUnsignedByte() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return (this.orbStream.read_octet() << 0) & 255;
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final int readUnsignedShort() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return (this.orbStream.read_ushort() << 0) & 65535;
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    protected String internalReadUTF(org.omg.CORBA.portable.InputStream inputStream) {
        return inputStream.read_wstring();
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final String readUTF() throws Throwable {
        try {
            this.readObjectState.readData(this);
            return internalReadUTF(this.orbStream);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    private void handleOptionalDataMarshalException(MARSHAL marshal, boolean z2) throws Throwable {
        Throwable thCreateOptionalDataException;
        if (marshal.minor == 1330446344) {
            if (!z2) {
                thCreateOptionalDataException = new EOFException("No more optional data");
            } else {
                thCreateOptionalDataException = createOptionalDataException();
            }
            thCreateOptionalDataException.initCause(marshal);
            setState(IN_READ_OBJECT_NO_MORE_OPT_DATA);
            throw thCreateOptionalDataException;
        }
    }

    @Override // java.io.ObjectInputStream
    public final synchronized void registerValidation(ObjectInputValidation objectInputValidation, int i2) throws InvalidObjectException, NotActiveException {
        throw new Error("Method registerValidation not supported");
    }

    protected final Class resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        throw new IOException("Method resolveClass not supported");
    }

    @Override // java.io.ObjectInputStream
    protected final Object resolveObject(Object obj) throws IOException {
        throw new IOException("Method resolveObject not supported");
    }

    @Override // java.io.ObjectInputStream, java.io.DataInput
    public final int skipBytes(int i2) throws Throwable {
        try {
            this.readObjectState.readData(this);
            this.orbStream.read_octet_array(new byte[i2], 0, i2);
            return i2;
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        } catch (MARSHAL e3) {
            handleOptionalDataMarshalException(e3, false);
            throw e3;
        }
    }

    /* JADX WARN: Finally extract failed */
    private synchronized Object inputObject(Class cls, String str, CodeBase codeBase, int i2) throws ClassNotFoundException, IOException {
        this.currentClassDesc = ObjectStreamClass.lookup(cls);
        this.currentClass = this.currentClassDesc.forClass();
        if (this.currentClass == null) {
            throw new ClassNotFoundException(this.currentClassDesc.getName());
        }
        try {
            if (Enum.class.isAssignableFrom(cls)) {
                this.orbStream.read_long();
                Enum enumValueOf = Enum.valueOf(cls, (String) this.orbStream.read_value(String.class));
                this.activeRecursionMgr.removeObject(i2);
                return enumValueOf;
            }
            if (this.currentClassDesc.isExternalizable()) {
                try {
                    try {
                        try {
                            this.currentObject = this.currentClass == null ? null : this.currentClassDesc.newInstance();
                            if (this.currentObject != null) {
                                this.activeRecursionMgr.addObject(i2, this.currentObject);
                                readFormatVersion();
                                ((Externalizable) this.currentObject).readExternal(this);
                            }
                        } catch (InvocationTargetException e2) {
                            InvalidClassException invalidClassException = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
                            invalidClassException.initCause(e2);
                            throw invalidClassException;
                        }
                    } catch (UnsupportedOperationException e3) {
                        InvalidClassException invalidClassException2 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
                        invalidClassException2.initCause(e3);
                        throw invalidClassException2;
                    }
                } catch (InstantiationException e4) {
                    InvalidClassException invalidClassException3 = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
                    invalidClassException3.initCause(e4);
                    throw invalidClassException3;
                }
            } else {
                ObjectStreamClass objectStreamClass = this.currentClassDesc;
                Class cls2 = this.currentClass;
                int i3 = this.spClass;
                if (this.currentClass.getName().equals("java.lang.String")) {
                    String utf = readUTF();
                    this.activeRecursionMgr.removeObject(i2);
                    return utf;
                }
                Class<? super Object> superclass = this.currentClass;
                for (ObjectStreamClass superclass2 = this.currentClassDesc; superclass2 != null && superclass2.isSerializable(); superclass2 = superclass2.getSuperclass()) {
                    Class<?> clsForClass = superclass2.forClass();
                    Class<? super Object> superclass3 = superclass;
                    while (superclass3 != null && clsForClass != superclass3) {
                        superclass3 = superclass3.getSuperclass();
                    }
                    this.spClass++;
                    if (this.spClass >= this.classes.length) {
                        int length = this.classes.length * 2;
                        Class[] clsArr = new Class[length];
                        ObjectStreamClass[] objectStreamClassArr = new ObjectStreamClass[length];
                        System.arraycopy(this.classes, 0, clsArr, 0, this.classes.length);
                        System.arraycopy(this.classdesc, 0, objectStreamClassArr, 0, this.classes.length);
                        this.classes = clsArr;
                        this.classdesc = objectStreamClassArr;
                    }
                    if (superclass3 == null) {
                        this.classdesc[this.spClass] = superclass2;
                        this.classes[this.spClass] = null;
                    } else {
                        this.classdesc[this.spClass] = superclass2;
                        this.classes[this.spClass] = superclass3;
                        superclass = superclass3.getSuperclass();
                    }
                }
                try {
                    try {
                        try {
                            this.currentObject = this.currentClass == null ? null : this.currentClassDesc.newInstance();
                            this.activeRecursionMgr.addObject(i2, this.currentObject);
                            try {
                                this.spClass = this.spClass;
                                while (this.spClass > i3) {
                                    this.currentClassDesc = this.classdesc[this.spClass];
                                    this.currentClass = this.classes[this.spClass];
                                    if (this.classes[this.spClass] != null) {
                                        InputStreamHook.ReadObjectState readObjectState = this.readObjectState;
                                        setState(DEFAULT_STATE);
                                        try {
                                            if (this.currentClassDesc.hasWriteObject()) {
                                                readFormatVersion();
                                                this.readObjectState.beginUnmarshalCustomValue(this, readBoolean(), this.currentClassDesc.readObjectMethod != null);
                                            } else if (this.currentClassDesc.hasReadObject()) {
                                                setState(IN_READ_OBJECT_REMOTE_NOT_CUSTOM_MARSHALED);
                                            }
                                            if (!invokeObjectReader(this.currentClassDesc, this.currentObject, this.currentClass) || this.readObjectState == IN_READ_OBJECT_DEFAULTS_SENT) {
                                                ObjectStreamField[] fieldsNoCopy = this.currentClassDesc.getFieldsNoCopy();
                                                if (fieldsNoCopy.length > 0) {
                                                    inputClassFields(this.currentObject, this.currentClass, fieldsNoCopy, codeBase);
                                                }
                                            }
                                            if (this.currentClassDesc.hasWriteObject()) {
                                                this.readObjectState.endUnmarshalCustomValue(this);
                                            }
                                            setState(readObjectState);
                                        } finally {
                                        }
                                    } else {
                                        ObjectStreamField[] fieldsNoCopy2 = this.currentClassDesc.getFieldsNoCopy();
                                        if (fieldsNoCopy2.length > 0) {
                                            inputClassFields(null, this.currentClass, fieldsNoCopy2, codeBase);
                                        }
                                    }
                                    this.spClass--;
                                }
                                this.spClass = i3;
                            } catch (Throwable th) {
                                this.spClass = i3;
                                throw th;
                            }
                        } catch (InvocationTargetException e5) {
                            InvalidClassException invalidClassException4 = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
                            invalidClassException4.initCause(e5);
                            throw invalidClassException4;
                        }
                    } catch (UnsupportedOperationException e6) {
                        InvalidClassException invalidClassException5 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
                        invalidClassException5.initCause(e6);
                        throw invalidClassException5;
                    }
                } catch (InstantiationException e7) {
                    InvalidClassException invalidClassException6 = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
                    invalidClassException6.initCause(e7);
                    throw invalidClassException6;
                }
            }
            return this.currentObject;
        } finally {
            this.activeRecursionMgr.removeObject(i2);
        }
    }

    private Vector getOrderedDescriptions(String str, CodeBase codeBase) {
        Vector vector = new Vector();
        if (codeBase == null) {
            return vector;
        }
        FullValueDescription fullValueDescriptionMeta = codeBase.meta(str);
        while (true) {
            FullValueDescription fullValueDescription = fullValueDescriptionMeta;
            if (fullValueDescription != null) {
                vector.insertElementAt(fullValueDescription, 0);
                if (fullValueDescription.base_value == null || "".equals(fullValueDescription.base_value)) {
                    break;
                }
                fullValueDescriptionMeta = codeBase.meta(fullValueDescription.base_value);
            } else {
                return vector;
            }
        }
        return vector;
    }

    /* JADX WARN: Finally extract failed */
    private synchronized Object inputObjectUsingFVD(Class cls, String str, CodeBase codeBase, int i2) throws IOException, ClassNotFoundException {
        int i3 = this.spClass;
        try {
            this.currentClassDesc = ObjectStreamClass.lookup(cls);
            this.currentClass = cls;
            if (this.currentClassDesc.isExternalizable()) {
                try {
                    this.currentObject = this.currentClass == null ? null : this.currentClassDesc.newInstance();
                    if (this.currentObject != null) {
                        this.activeRecursionMgr.addObject(i2, this.currentObject);
                        readFormatVersion();
                        ((Externalizable) this.currentObject).readExternal(this);
                    }
                } catch (InstantiationException e2) {
                    InvalidClassException invalidClassException = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
                    invalidClassException.initCause(e2);
                    throw invalidClassException;
                } catch (UnsupportedOperationException e3) {
                    InvalidClassException invalidClassException2 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
                    invalidClassException2.initCause(e3);
                    throw invalidClassException2;
                } catch (InvocationTargetException e4) {
                    InvalidClassException invalidClassException3 = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
                    invalidClassException3.initCause(e4);
                    throw invalidClassException3;
                }
            } else {
                Class<? super Object> superclass = this.currentClass;
                for (ObjectStreamClass superclass2 = this.currentClassDesc; superclass2 != null && superclass2.isSerializable(); superclass2 = superclass2.getSuperclass()) {
                    Class<?> clsForClass = superclass2.forClass();
                    Class<? super Object> superclass3 = superclass;
                    while (superclass3 != null && clsForClass != superclass3) {
                        superclass3 = superclass3.getSuperclass();
                    }
                    this.spClass++;
                    if (this.spClass >= this.classes.length) {
                        int length = this.classes.length * 2;
                        Class[] clsArr = new Class[length];
                        ObjectStreamClass[] objectStreamClassArr = new ObjectStreamClass[length];
                        System.arraycopy(this.classes, 0, clsArr, 0, this.classes.length);
                        System.arraycopy(this.classdesc, 0, objectStreamClassArr, 0, this.classes.length);
                        this.classes = clsArr;
                        this.classdesc = objectStreamClassArr;
                    }
                    if (superclass3 == null) {
                        this.classdesc[this.spClass] = superclass2;
                        this.classes[this.spClass] = null;
                    } else {
                        this.classdesc[this.spClass] = superclass2;
                        this.classes[this.spClass] = superclass3;
                        superclass = superclass3.getSuperclass();
                    }
                }
                try {
                    this.currentObject = this.currentClass == null ? null : this.currentClassDesc.newInstance();
                    this.activeRecursionMgr.addObject(i2, this.currentObject);
                    Enumeration enumerationElements = getOrderedDescriptions(str, codeBase).elements();
                    while (enumerationElements.hasMoreElements() && this.spClass > i3) {
                        FullValueDescription fullValueDescription = (FullValueDescription) enumerationElements.nextElement2();
                        String className = this.vhandler.getClassName(fullValueDescription.id);
                        String className2 = this.vhandler.getClassName(this.vhandler.getRMIRepositoryID(this.currentClass));
                        while (this.spClass > i3 && !className.equals(className2)) {
                            int iFindNextClass = findNextClass(className, this.classes, this.spClass, i3);
                            if (iFindNextClass != -1) {
                                this.spClass = iFindNextClass;
                                this.currentClass = this.classes[this.spClass];
                                className2 = this.vhandler.getClassName(this.vhandler.getRMIRepositoryID(this.currentClass));
                            } else {
                                if (fullValueDescription.is_custom) {
                                    readFormatVersion();
                                    if (readBoolean()) {
                                        inputClassFields(null, null, null, fullValueDescription.members, codeBase);
                                    }
                                    if (getStreamFormatVersion() == 2) {
                                        ((ValueInputStream) getOrbStream()).start_value();
                                        ((ValueInputStream) getOrbStream()).end_value();
                                    }
                                } else {
                                    inputClassFields(null, this.currentClass, null, fullValueDescription.members, codeBase);
                                }
                                if (enumerationElements.hasMoreElements()) {
                                    fullValueDescription = (FullValueDescription) enumerationElements.nextElement2();
                                    className = this.vhandler.getClassName(fullValueDescription.id);
                                } else {
                                    Object obj = this.currentObject;
                                    this.spClass = i3;
                                    this.activeRecursionMgr.removeObject(i2);
                                    return obj;
                                }
                            }
                        }
                        ObjectStreamClass objectStreamClassLookup = ObjectStreamClass.lookup(this.currentClass);
                        this.currentClassDesc = objectStreamClassLookup;
                        if (!className2.equals(Constants.OBJECT_CLASS)) {
                            InputStreamHook.ReadObjectState readObjectState = this.readObjectState;
                            setState(DEFAULT_STATE);
                            try {
                                if (fullValueDescription.is_custom) {
                                    readFormatVersion();
                                    this.readObjectState.beginUnmarshalCustomValue(this, readBoolean(), this.currentClassDesc.readObjectMethod != null);
                                }
                                try {
                                    if (!fullValueDescription.is_custom && this.currentClassDesc.hasReadObject()) {
                                        setState(IN_READ_OBJECT_REMOTE_NOT_CUSTOM_MARSHALED);
                                    }
                                    this.defaultReadObjectFVDMembers = fullValueDescription.members;
                                    boolean zInvokeObjectReader = invokeObjectReader(this.currentClassDesc, this.currentObject, this.currentClass);
                                    this.defaultReadObjectFVDMembers = null;
                                    if (!zInvokeObjectReader || this.readObjectState == IN_READ_OBJECT_DEFAULTS_SENT) {
                                        inputClassFields(this.currentObject, this.currentClass, objectStreamClassLookup, fullValueDescription.members, codeBase);
                                    }
                                    if (fullValueDescription.is_custom) {
                                        this.readObjectState.endUnmarshalCustomValue(this);
                                    }
                                    setState(readObjectState);
                                    Class[] clsArr2 = this.classes;
                                    int i4 = this.spClass - 1;
                                    this.spClass = i4;
                                    this.currentClass = clsArr2[i4];
                                } finally {
                                }
                            } catch (Throwable th) {
                                setState(readObjectState);
                                throw th;
                            }
                        } else {
                            inputClassFields(null, this.currentClass, null, fullValueDescription.members, codeBase);
                            while (enumerationElements.hasMoreElements()) {
                                FullValueDescription fullValueDescription2 = (FullValueDescription) enumerationElements.nextElement2();
                                if (fullValueDescription2.is_custom) {
                                    skipCustomUsingFVD(fullValueDescription2.members, codeBase);
                                } else {
                                    inputClassFields(null, this.currentClass, null, fullValueDescription2.members, codeBase);
                                }
                            }
                        }
                    }
                    while (enumerationElements.hasMoreElements()) {
                        FullValueDescription fullValueDescription3 = (FullValueDescription) enumerationElements.nextElement2();
                        if (fullValueDescription3.is_custom) {
                            skipCustomUsingFVD(fullValueDescription3.members, codeBase);
                        } else {
                            throwAwayData(fullValueDescription3.members, codeBase);
                        }
                    }
                } catch (InstantiationException e5) {
                    InvalidClassException invalidClassException4 = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
                    invalidClassException4.initCause(e5);
                    throw invalidClassException4;
                } catch (UnsupportedOperationException e6) {
                    InvalidClassException invalidClassException5 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
                    invalidClassException5.initCause(e6);
                    throw invalidClassException5;
                } catch (InvocationTargetException e7) {
                    InvalidClassException invalidClassException6 = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
                    invalidClassException6.initCause(e7);
                    throw invalidClassException6;
                }
            }
            Object obj2 = this.currentObject;
            this.spClass = i3;
            this.activeRecursionMgr.removeObject(i2);
            return obj2;
        } catch (Throwable th2) {
            this.spClass = i3;
            this.activeRecursionMgr.removeObject(i2);
            throw th2;
        }
    }

    private Object skipObjectUsingFVD(String str, CodeBase codeBase) throws ClassNotFoundException, IOException {
        Enumeration enumerationElements = getOrderedDescriptions(str, codeBase).elements();
        while (enumerationElements.hasMoreElements()) {
            FullValueDescription fullValueDescription = (FullValueDescription) enumerationElements.nextElement2();
            if (!this.vhandler.getClassName(fullValueDescription.id).equals(Constants.OBJECT_CLASS)) {
                if (fullValueDescription.is_custom) {
                    readFormatVersion();
                    if (readBoolean()) {
                        inputClassFields(null, null, null, fullValueDescription.members, codeBase);
                    }
                    if (getStreamFormatVersion() == 2) {
                        ((ValueInputStream) getOrbStream()).start_value();
                        ((ValueInputStream) getOrbStream()).end_value();
                    }
                } else {
                    inputClassFields(null, null, null, fullValueDescription.members, codeBase);
                }
            }
        }
        return null;
    }

    private int findNextClass(String str, Class[] clsArr, int i2, int i3) {
        for (int i4 = i2; i4 > i3; i4--) {
            if (str.equals(clsArr[i4].getName())) {
                return i4;
            }
        }
        return -1;
    }

    private boolean invokeObjectReader(ObjectStreamClass objectStreamClass, Object obj, Class cls) throws ClassNotFoundException, IOException, IllegalArgumentException {
        if (objectStreamClass.readObjectMethod == null) {
            return false;
        }
        try {
            objectStreamClass.readObjectMethod.invoke(obj, this.readObjectArgList);
            return true;
        } catch (IllegalAccessException e2) {
            return false;
        } catch (InvocationTargetException e3) {
            Throwable targetException = e3.getTargetException();
            if (targetException instanceof ClassNotFoundException) {
                throw ((ClassNotFoundException) targetException);
            }
            if (targetException instanceof IOException) {
                throw ((IOException) targetException);
            }
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            if (targetException instanceof Error) {
                throw ((Error) targetException);
            }
            throw new Error("internal error");
        }
    }

    private void resetStream() throws IOException {
        if (this.classes == null) {
            this.classes = new Class[20];
        } else {
            for (int i2 = 0; i2 < this.classes.length; i2++) {
                this.classes[i2] = null;
            }
        }
        if (this.classdesc == null) {
            this.classdesc = new ObjectStreamClass[20];
        } else {
            for (int i3 = 0; i3 < this.classdesc.length; i3++) {
                this.classdesc[i3] = null;
            }
        }
        this.spClass = 0;
        if (this.callbacks != null) {
            this.callbacks.setSize(0);
        }
    }

    private void inputPrimitiveField(Object obj, Class cls, ObjectStreamField objectStreamField) throws IOException {
        try {
            switch (objectStreamField.getTypeCode()) {
                case 'B':
                    byte b2 = this.orbStream.read_octet();
                    if (objectStreamField.getField() != null) {
                        bridge.putByte(obj, objectStreamField.getFieldID(), b2);
                        break;
                    }
                    break;
                case 'C':
                    char c2 = this.orbStream.read_wchar();
                    if (objectStreamField.getField() != null) {
                        bridge.putChar(obj, objectStreamField.getFieldID(), c2);
                        break;
                    }
                    break;
                case 'D':
                    double d2 = this.orbStream.read_double();
                    if (objectStreamField.getField() != null) {
                        bridge.putDouble(obj, objectStreamField.getFieldID(), d2);
                        break;
                    }
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new InvalidClassException(cls.getName());
                case 'F':
                    float f2 = this.orbStream.read_float();
                    if (objectStreamField.getField() != null) {
                        bridge.putFloat(obj, objectStreamField.getFieldID(), f2);
                        break;
                    }
                    break;
                case 'I':
                    int i2 = this.orbStream.read_long();
                    if (objectStreamField.getField() != null) {
                        bridge.putInt(obj, objectStreamField.getFieldID(), i2);
                        break;
                    }
                    break;
                case 'J':
                    long j2 = this.orbStream.read_longlong();
                    if (objectStreamField.getField() != null) {
                        bridge.putLong(obj, objectStreamField.getFieldID(), j2);
                        break;
                    }
                    break;
                case 'S':
                    short s2 = this.orbStream.read_short();
                    if (objectStreamField.getField() != null) {
                        bridge.putShort(obj, objectStreamField.getFieldID(), s2);
                        break;
                    }
                    break;
                case 'Z':
                    boolean z2 = this.orbStream.read_boolean();
                    if (objectStreamField.getField() != null) {
                        bridge.putBoolean(obj, objectStreamField.getFieldID(), z2);
                        break;
                    }
                    break;
            }
        } catch (IllegalArgumentException e2) {
            ClassCastException classCastException = new ClassCastException("Assigning instance of class " + objectStreamField.getType().getName() + " to field " + this.currentClassDesc.getName() + '#' + objectStreamField.getField().getName());
            classCastException.initCause(e2);
            throw classCastException;
        }
    }

    private Object inputObjectField(ValueMember valueMember, CodeBase codeBase) throws IndirectionException, ClassNotFoundException, IOException, ClassCastException {
        Class classFromType;
        Object abstractAndNarrow;
        String str = valueMember.id;
        try {
            classFromType = this.vhandler.getClassFromType(str);
        } catch (ClassNotFoundException e2) {
            classFromType = null;
        }
        String signature = null;
        if (classFromType != null) {
            signature = ValueUtility.getSignature(valueMember);
        }
        if (signature != null && (signature.equals(Constants.OBJECT_SIG) || signature.equals("Ljava/io/Serializable;") || signature.equals("Ljava/io/Externalizable;"))) {
            abstractAndNarrow = Util.readAny(this.orbStream);
        } else {
            int i2 = 2;
            if (!this.vhandler.isSequence(str)) {
                if (valueMember.type.kind().value() == kRemoteTypeCode.kind().value()) {
                    i2 = 0;
                } else if (classFromType != null && classFromType.isInterface() && (this.vhandler.isAbstractBase(classFromType) || ObjectStreamClassCorbaExt.isAbstractInterface(classFromType))) {
                    i2 = 1;
                }
            }
            switch (i2) {
                case 0:
                    if (classFromType != null) {
                        abstractAndNarrow = Utility.readObjectAndNarrow(this.orbStream, classFromType);
                        break;
                    } else {
                        abstractAndNarrow = this.orbStream.read_Object();
                        break;
                    }
                case 1:
                    if (classFromType != null) {
                        abstractAndNarrow = Utility.readAbstractAndNarrow(this.orbStream, classFromType);
                        break;
                    } else {
                        abstractAndNarrow = this.orbStream.read_abstract_interface();
                        break;
                    }
                case 2:
                    if (classFromType != null) {
                        abstractAndNarrow = this.orbStream.read_value(classFromType);
                        break;
                    } else {
                        abstractAndNarrow = this.orbStream.read_value();
                        break;
                    }
                default:
                    throw new StreamCorruptedException("Unknown callType: " + i2);
            }
        }
        return abstractAndNarrow;
    }

    private Object inputObjectField(ObjectStreamField objectStreamField) throws IndirectionException, ClassNotFoundException, IOException, ClassCastException {
        Object abstractAndNarrow;
        if (ObjectStreamClassCorbaExt.isAny(objectStreamField.getTypeString())) {
            return Util.readAny(this.orbStream);
        }
        Class type = objectStreamField.getType();
        Class clsLoadStubClass = type;
        int i2 = 2;
        boolean z2 = false;
        if (type.isInterface()) {
            boolean z3 = false;
            if (Remote.class.isAssignableFrom(type)) {
                i2 = 0;
            } else if (Object.class.isAssignableFrom(type)) {
                i2 = 0;
                z3 = true;
            } else if (this.vhandler.isAbstractBase(type)) {
                i2 = 1;
                z3 = true;
            } else if (ObjectStreamClassCorbaExt.isAbstractInterface(type)) {
                i2 = 1;
            }
            if (z3) {
                try {
                    clsLoadStubClass = Utility.loadStubClass(this.vhandler.createForAnyType(type), Util.getCodebase(type), type);
                } catch (ClassNotFoundException e2) {
                    z2 = true;
                }
            } else {
                z2 = true;
            }
        }
        switch (i2) {
            case 0:
                if (!z2) {
                    abstractAndNarrow = this.orbStream.read_Object(clsLoadStubClass);
                    break;
                } else {
                    abstractAndNarrow = Utility.readObjectAndNarrow(this.orbStream, clsLoadStubClass);
                    break;
                }
            case 1:
                if (!z2) {
                    abstractAndNarrow = this.orbStream.read_abstract_interface(clsLoadStubClass);
                    break;
                } else {
                    abstractAndNarrow = Utility.readAbstractAndNarrow(this.orbStream, clsLoadStubClass);
                    break;
                }
            case 2:
                abstractAndNarrow = this.orbStream.read_value(clsLoadStubClass);
                break;
            default:
                throw new StreamCorruptedException("Unknown callType: " + i2);
        }
        return abstractAndNarrow;
    }

    private final boolean mustUseRemoteValueMembers() {
        return this.defaultReadObjectFVDMembers != null;
    }

    @Override // com.sun.corba.se.impl.io.InputStreamHook
    void readFields(Map map) throws ClassNotFoundException, IOException, ClassCastException {
        if (mustUseRemoteValueMembers()) {
            inputRemoteMembersForReadFields(map);
        } else {
            inputCurrentClassFieldsForReadFields(map);
        }
    }

    private final void inputRemoteMembersForReadFields(Map map) throws ClassNotFoundException, IOException {
        Object object;
        ValueMember[] valueMemberArr = this.defaultReadObjectFVDMembers;
        for (int i2 = 0; i2 < valueMemberArr.length; i2++) {
            try {
                switch (valueMemberArr[i2].type.kind().value()) {
                    case 2:
                        map.put(valueMemberArr[i2].name, new Short(this.orbStream.read_short()));
                        continue;
                    case 3:
                        map.put(valueMemberArr[i2].name, new Integer(this.orbStream.read_long()));
                        continue;
                    case 4:
                    case 5:
                    case 11:
                    case 12:
                    case 13:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 24:
                    case 25:
                    case 27:
                    case 28:
                    default:
                        throw new StreamCorruptedException("Unknown kind: " + valueMemberArr[i2].type.kind().value());
                    case 6:
                        map.put(valueMemberArr[i2].name, new Float(this.orbStream.read_float()));
                        continue;
                    case 7:
                        map.put(valueMemberArr[i2].name, new Double(this.orbStream.read_double()));
                        continue;
                    case 8:
                        map.put(valueMemberArr[i2].name, new Boolean(this.orbStream.read_boolean()));
                        continue;
                    case 9:
                    case 26:
                        map.put(valueMemberArr[i2].name, new Character(this.orbStream.read_wchar()));
                        continue;
                    case 10:
                        map.put(valueMemberArr[i2].name, new Byte(this.orbStream.read_octet()));
                        continue;
                    case 14:
                    case 29:
                    case 30:
                        try {
                            object = inputObjectField(valueMemberArr[i2], this.cbSender);
                        } catch (IndirectionException e2) {
                            object = this.activeRecursionMgr.getObject(e2.offset);
                        }
                        map.put(valueMemberArr[i2].name, object);
                    case 23:
                        map.put(valueMemberArr[i2].name, new Long(this.orbStream.read_longlong()));
                        continue;
                }
            } catch (Throwable th) {
                StreamCorruptedException streamCorruptedException = new StreamCorruptedException(th.getMessage());
                streamCorruptedException.initCause(th);
                throw streamCorruptedException;
            }
            StreamCorruptedException streamCorruptedException2 = new StreamCorruptedException(th.getMessage());
            streamCorruptedException2.initCause(th);
            throw streamCorruptedException2;
        }
    }

    private final void inputCurrentClassFieldsForReadFields(Map map) throws ClassNotFoundException, IOException, ClassCastException {
        Object object;
        ObjectStreamField[] fieldsNoCopy = this.currentClassDesc.getFieldsNoCopy();
        int length = fieldsNoCopy.length - this.currentClassDesc.objFields;
        for (int i2 = 0; i2 < length; i2++) {
            switch (fieldsNoCopy[i2].getTypeCode()) {
                case 'B':
                    map.put(fieldsNoCopy[i2].getName(), new Byte(this.orbStream.read_octet()));
                    break;
                case 'C':
                    map.put(fieldsNoCopy[i2].getName(), new Character(this.orbStream.read_wchar()));
                    break;
                case 'D':
                    map.put(fieldsNoCopy[i2].getName(), new Double(this.orbStream.read_double()));
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw new InvalidClassException(this.currentClassDesc.getName());
                case 'F':
                    map.put(fieldsNoCopy[i2].getName(), new Float(this.orbStream.read_float()));
                    break;
                case 'I':
                    map.put(fieldsNoCopy[i2].getName(), new Integer(this.orbStream.read_long()));
                    break;
                case 'J':
                    map.put(fieldsNoCopy[i2].getName(), new Long(this.orbStream.read_longlong()));
                    break;
                case 'S':
                    map.put(fieldsNoCopy[i2].getName(), new Short(this.orbStream.read_short()));
                    break;
                case 'Z':
                    map.put(fieldsNoCopy[i2].getName(), new Boolean(this.orbStream.read_boolean()));
                    break;
            }
        }
        if (this.currentClassDesc.objFields > 0) {
            for (int i3 = length; i3 < fieldsNoCopy.length; i3++) {
                try {
                    object = inputObjectField(fieldsNoCopy[i3]);
                } catch (IndirectionException e2) {
                    object = this.activeRecursionMgr.getObject(e2.offset);
                }
                map.put(fieldsNoCopy[i3].getName(), object);
            }
        }
    }

    private void inputClassFields(Object obj, Class<?> cls, ObjectStreamField[] objectStreamFieldArr, CodeBase codeBase) throws IOException, ClassNotFoundException, ClassCastException {
        Object object;
        int length = objectStreamFieldArr.length - this.currentClassDesc.objFields;
        if (obj != null) {
            for (int i2 = 0; i2 < length; i2++) {
                inputPrimitiveField(obj, cls, objectStreamFieldArr[i2]);
            }
        }
        if (this.currentClassDesc.objFields > 0) {
            for (int i3 = length; i3 < objectStreamFieldArr.length; i3++) {
                try {
                    object = inputObjectField(objectStreamFieldArr[i3]);
                } catch (IndirectionException e2) {
                    object = this.activeRecursionMgr.getObject(e2.offset);
                }
                if (obj != null && objectStreamFieldArr[i3].getField() != null) {
                    try {
                        Class<?> clazz = objectStreamFieldArr[i3].getClazz();
                        if (object != null && !clazz.isAssignableFrom(object.getClass())) {
                            throw new IllegalArgumentException("Field mismatch");
                        }
                        try {
                            try {
                                Field declaredField = getDeclaredField(cls, objectStreamFieldArr[i3].getName());
                                if (declaredField != null) {
                                    if (!declaredField.getType().isAssignableFrom(clazz)) {
                                        throw new IllegalArgumentException("Field Type mismatch");
                                    }
                                    if (object != null && !clazz.isInstance(object)) {
                                        throw new IllegalArgumentException();
                                    }
                                    bridge.putObject(obj, objectStreamFieldArr[i3].getFieldID(), object);
                                }
                            } catch (NoSuchFieldException e3) {
                            } catch (NullPointerException e4) {
                            } catch (PrivilegedActionException e5) {
                                throw new IllegalArgumentException((NoSuchFieldException) e5.getException());
                            }
                        } catch (SecurityException e6) {
                            throw new IllegalArgumentException(e6);
                        }
                    } catch (IllegalArgumentException e7) {
                        String name = FXMLLoader.NULL_KEYWORD;
                        String name2 = FXMLLoader.NULL_KEYWORD;
                        String name3 = FXMLLoader.NULL_KEYWORD;
                        if (object != null) {
                            name = object.getClass().getName();
                        }
                        if (this.currentClassDesc != null) {
                            name2 = this.currentClassDesc.getName();
                        }
                        if (objectStreamFieldArr[i3] != null && objectStreamFieldArr[i3].getField() != null) {
                            name3 = objectStreamFieldArr[i3].getField().getName();
                        }
                        ClassCastException classCastException = new ClassCastException("Assigning instance of class " + name + " to field " + name2 + '#' + name3);
                        classCastException.initCause(e7);
                        throw classCastException;
                    }
                }
            }
        }
    }

    private void inputClassFields(Object obj, Class cls, ObjectStreamClass objectStreamClass, ValueMember[] valueMemberArr, CodeBase codeBase) throws ClassNotFoundException, IOException {
        Object object;
        for (int i2 = 0; i2 < valueMemberArr.length; i2++) {
            try {
                try {
                    switch (valueMemberArr[i2].type.kind().value()) {
                        case 2:
                            short s2 = this.orbStream.read_short();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setShortField(obj, cls, valueMemberArr[i2].name, s2);
                            }
                            break;
                        case 3:
                            int i3 = this.orbStream.read_long();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setIntField(obj, cls, valueMemberArr[i2].name, i3);
                            }
                            break;
                        case 4:
                        case 5:
                        case 11:
                        case 12:
                        case 13:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 24:
                        case 25:
                        case 27:
                        case 28:
                        default:
                            throw new StreamCorruptedException("Unknown kind: " + valueMemberArr[i2].type.kind().value());
                        case 6:
                            float f2 = this.orbStream.read_float();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setFloatField(obj, cls, valueMemberArr[i2].name, f2);
                            }
                            break;
                        case 7:
                            double d2 = this.orbStream.read_double();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setDoubleField(obj, cls, valueMemberArr[i2].name, d2);
                            }
                            break;
                        case 8:
                            boolean z2 = this.orbStream.read_boolean();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setBooleanField(obj, cls, valueMemberArr[i2].name, z2);
                            }
                            break;
                        case 9:
                        case 26:
                            char c2 = this.orbStream.read_wchar();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setCharField(obj, cls, valueMemberArr[i2].name, c2);
                            }
                            break;
                        case 10:
                            byte b2 = this.orbStream.read_octet();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setByteField(obj, cls, valueMemberArr[i2].name, b2);
                            }
                            break;
                        case 14:
                        case 29:
                        case 30:
                            try {
                                object = inputObjectField(valueMemberArr[i2], codeBase);
                            } catch (IndirectionException e2) {
                                object = this.activeRecursionMgr.getObject(e2.offset);
                            }
                            if (obj != null) {
                                try {
                                    if (objectStreamClass.hasField(valueMemberArr[i2])) {
                                        setObjectField(obj, cls, valueMemberArr[i2].name, object);
                                    }
                                } catch (IllegalArgumentException e3) {
                                    ClassCastException classCastException = new ClassCastException("Assigning instance of class " + object.getClass().getName() + " to field " + valueMemberArr[i2].name);
                                    classCastException.initCause(e3);
                                    throw classCastException;
                                }
                            }
                        case 23:
                            long j2 = this.orbStream.read_longlong();
                            if (obj != null && objectStreamClass.hasField(valueMemberArr[i2])) {
                                setLongField(obj, cls, valueMemberArr[i2].name, j2);
                            }
                            break;
                    }
                } catch (IllegalArgumentException e4) {
                    ClassCastException classCastException2 = new ClassCastException("Assigning instance of class " + valueMemberArr[i2].id + " to field " + this.currentClassDesc.getName() + '#' + valueMemberArr[i2].name);
                    classCastException2.initCause(e4);
                    throw classCastException2;
                }
            } catch (Throwable th) {
                StreamCorruptedException streamCorruptedException = new StreamCorruptedException(th.getMessage());
                streamCorruptedException.initCause(th);
                throw streamCorruptedException;
            }
        }
    }

    private void skipCustomUsingFVD(ValueMember[] valueMemberArr, CodeBase codeBase) throws IOException, ClassNotFoundException {
        readFormatVersion();
        if (readBoolean()) {
            throwAwayData(valueMemberArr, codeBase);
        }
        if (getStreamFormatVersion() == 2) {
            ((ValueInputStream) getOrbStream()).start_value();
            ((ValueInputStream) getOrbStream()).end_value();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x014c A[Catch: IndirectionException -> 0x01f6, IllegalArgumentException -> 0x0225, TryCatch #1 {IllegalArgumentException -> 0x0225, blocks: (B:5:0x0008, B:6:0x0014, B:7:0x0098, B:8:0x00a3, B:9:0x00ae, B:10:0x00b9, B:11:0x00c4, B:12:0x00cf, B:13:0x00da, B:14:0x00e5, B:15:0x00f0, B:16:0x00fb, B:21:0x0116, B:24:0x0123, B:26:0x012d, B:28:0x0137, B:30:0x0141, B:31:0x014c, B:33:0x015b, B:36:0x017b, B:40:0x0188, B:41:0x01a4, B:42:0x01af, B:45:0x01bf, B:46:0x01cc, B:47:0x01d7, B:48:0x01f2, B:52:0x01fb, B:53:0x0221), top: B:62:0x0008 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void throwAwayData(org.omg.CORBA.ValueMember[] r7, com.sun.org.omg.SendingContext.CodeBase r8) throws java.lang.ClassNotFoundException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 631
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.io.IIOPInputStream.throwAwayData(org.omg.CORBA.ValueMember[], com.sun.org.omg.SendingContext.CodeBase):void");
    }

    private static void setObjectField(Object obj, Class<?> cls, String str, Object obj2) throws Exception {
        try {
            Field declaredField = getDeclaredField(cls, str);
            Class<?> type = declaredField.getType();
            if (obj2 != null && !type.isInstance(obj2)) {
                throw new Exception();
            }
            bridge.putObject(obj, bridge.objectFieldOffset(declaredField), obj2);
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetObjectField(e2, str, obj.toString(), obj2.toString());
            }
            throw utilWrapper.errorSetObjectField(e2, str, "null " + cls.getName() + " object", obj2.toString());
        }
    }

    private static void setBooleanField(Object obj, Class<?> cls, String str, boolean z2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Boolean.TYPE) {
                bridge.putBoolean(obj, bridge.objectFieldOffset(declaredField), z2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetBooleanField(e2, str, obj.toString(), new Boolean(z2));
            }
            throw utilWrapper.errorSetBooleanField(e2, str, "null " + cls.getName() + " object", new Boolean(z2));
        }
    }

    private static void setByteField(Object obj, Class<?> cls, String str, byte b2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Byte.TYPE) {
                bridge.putByte(obj, bridge.objectFieldOffset(declaredField), b2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetByteField(e2, str, obj.toString(), new Byte(b2));
            }
            throw utilWrapper.errorSetByteField(e2, str, "null " + cls.getName() + " object", new Byte(b2));
        }
    }

    private static void setCharField(Object obj, Class<?> cls, String str, char c2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Character.TYPE) {
                bridge.putChar(obj, bridge.objectFieldOffset(declaredField), c2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetCharField(e2, str, obj.toString(), new Character(c2));
            }
            throw utilWrapper.errorSetCharField(e2, str, "null " + cls.getName() + " object", new Character(c2));
        }
    }

    private static void setShortField(Object obj, Class<?> cls, String str, short s2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Short.TYPE) {
                bridge.putShort(obj, bridge.objectFieldOffset(declaredField), s2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetShortField(e2, str, obj.toString(), new Short(s2));
            }
            throw utilWrapper.errorSetShortField(e2, str, "null " + cls.getName() + " object", new Short(s2));
        }
    }

    private static void setIntField(Object obj, Class<?> cls, String str, int i2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Integer.TYPE) {
                bridge.putInt(obj, bridge.objectFieldOffset(declaredField), i2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetIntField(e2, str, obj.toString(), new Integer(i2));
            }
            throw utilWrapper.errorSetIntField(e2, str, "null " + cls.getName() + " object", new Integer(i2));
        }
    }

    private static void setLongField(Object obj, Class<?> cls, String str, long j2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Long.TYPE) {
                bridge.putLong(obj, bridge.objectFieldOffset(declaredField), j2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetLongField(e2, str, obj.toString(), new Long(j2));
            }
            throw utilWrapper.errorSetLongField(e2, str, "null " + cls.getName() + " object", new Long(j2));
        }
    }

    private static void setFloatField(Object obj, Class<?> cls, String str, float f2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Float.TYPE) {
                bridge.putFloat(obj, bridge.objectFieldOffset(declaredField), f2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetFloatField(e2, str, obj.toString(), new Float(f2));
            }
            throw utilWrapper.errorSetFloatField(e2, str, "null " + cls.getName() + " object", new Float(f2));
        }
    }

    private static void setDoubleField(Object obj, Class<?> cls, String str, double d2) throws InvalidObjectException {
        try {
            Field declaredField = getDeclaredField(cls, str);
            if (declaredField != null && declaredField.getType() == Double.TYPE) {
                bridge.putDouble(obj, bridge.objectFieldOffset(declaredField), d2);
                return;
            }
            throw new InvalidObjectException("Field Type mismatch");
        } catch (Exception e2) {
            if (obj != null) {
                throw utilWrapper.errorSetDoubleField(e2, str, obj.toString(), new Double(d2));
            }
            throw utilWrapper.errorSetDoubleField(e2, str, "null " + cls.getName() + " object", new Double(d2));
        }
    }

    private static Field getDeclaredField(final Class<?> cls, final String str) throws PrivilegedActionException, NoSuchFieldException, SecurityException {
        if (System.getSecurityManager() == null) {
            return cls.getDeclaredField(str);
        }
        return (Field) AccessController.doPrivileged(new PrivilegedExceptionAction<Field>() { // from class: com.sun.corba.se.impl.io.IIOPInputStream.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedExceptionAction
            public Field run() throws NoSuchFieldException {
                return cls.getDeclaredField(str);
            }
        });
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/IIOPInputStream$ActiveRecursionManager.class */
    static class ActiveRecursionManager {
        private Map<Integer, Object> offsetToObjectMap = new HashMap();

        public void addObject(int i2, Object obj) {
            this.offsetToObjectMap.put(new Integer(i2), obj);
        }

        public Object getObject(int i2) throws IOException {
            Integer num = new Integer(i2);
            if (!this.offsetToObjectMap.containsKey(num)) {
                throw new IOException("Invalid indirection to offset " + i2);
            }
            return this.offsetToObjectMap.get(num);
        }

        public void removeObject(int i2) {
            this.offsetToObjectMap.remove(new Integer(i2));
        }

        public boolean containsObject(int i2) {
            return this.offsetToObjectMap.containsKey(new Integer(i2));
        }
    }
}
