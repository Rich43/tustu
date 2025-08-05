package com.sun.corba.se.impl.io;

import com.sun.corba.se.impl.io.OutputStreamHook;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotActiveException;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.rmi.Remote;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Stack;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ValueOutputStream;
import org.omg.CORBA_2_3.portable.OutputStream;
import sun.corba.Bridge;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/IIOPOutputStream.class */
public class IIOPOutputStream extends OutputStreamHook {
    private static Bridge bridge = (Bridge) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.io.IIOPOutputStream.1
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Object run2() {
            return Bridge.get();
        }
    });
    private OutputStream orbStream;
    private UtilSystemException wrapper = UtilSystemException.get(CORBALogDomains.RPC_ENCODING);
    private Object currentObject = null;
    private ObjectStreamClass currentClassDesc = null;
    private int recursionDepth = 0;
    private int simpleWriteDepth = 0;
    private IOException abortIOException = null;
    private Stack classDescStack = new Stack();
    private Object[] writeObjectArgList = {this};

    @Override // com.sun.corba.se.impl.io.OutputStreamHook
    protected void beginOptionalCustomData() {
        if (this.streamFormatVersion == 2) {
            ((ValueOutputStream) this.orbStream).start_value(this.currentClassDesc.getRMIIIOPOptionalDataRepId());
        }
    }

    final void setOrbStream(OutputStream outputStream) {
        this.orbStream = outputStream;
    }

    @Override // com.sun.corba.se.impl.io.OutputStreamHook
    final OutputStream getOrbStream() {
        return this.orbStream;
    }

    final void increaseRecursionDepth() {
        this.recursionDepth++;
    }

    final int decreaseRecursionDepth() {
        int i2 = this.recursionDepth - 1;
        this.recursionDepth = i2;
        return i2;
    }

    @Override // java.io.ObjectOutputStream
    public final void writeObjectOverride(Object obj) throws IOException {
        this.writeObjectState.writeData(this);
        Util.writeAbstractObject(this.orbStream, obj);
    }

    public final void simpleWriteObject(Object obj, byte b2) {
        byte b3 = this.streamFormatVersion;
        this.streamFormatVersion = b2;
        Object obj2 = this.currentObject;
        ObjectStreamClass objectStreamClass = this.currentClassDesc;
        this.simpleWriteDepth++;
        try {
            try {
                outputObject(obj);
                this.streamFormatVersion = b3;
                this.simpleWriteDepth--;
                this.currentObject = obj2;
                this.currentClassDesc = objectStreamClass;
            } catch (IOException e2) {
                if (this.abortIOException == null) {
                    this.abortIOException = e2;
                }
                this.streamFormatVersion = b3;
                this.simpleWriteDepth--;
                this.currentObject = obj2;
                this.currentClassDesc = objectStreamClass;
            }
            IOException iOException = this.abortIOException;
            if (this.simpleWriteDepth == 0) {
                this.abortIOException = null;
            }
            if (iOException != null) {
                bridge.throwException(iOException);
            }
        } catch (Throwable th) {
            this.streamFormatVersion = b3;
            this.simpleWriteDepth--;
            this.currentObject = obj2;
            this.currentClassDesc = objectStreamClass;
            throw th;
        }
    }

    @Override // com.sun.corba.se.impl.io.OutputStreamHook
    ObjectStreamField[] getFieldsNoCopy() {
        return this.currentClassDesc.getFieldsNoCopy();
    }

    @Override // com.sun.corba.se.impl.io.OutputStreamHook
    public final void defaultWriteObjectDelegate() throws NotActiveException, IllegalArgumentException {
        try {
            if (this.currentObject == null || this.currentClassDesc == null) {
                throw new NotActiveException("defaultWriteObjectDelegate");
            }
            ObjectStreamField[] fieldsNoCopy = this.currentClassDesc.getFieldsNoCopy();
            if (fieldsNoCopy.length > 0) {
                outputClassFields(this.currentObject, this.currentClassDesc.forClass(), fieldsNoCopy);
            }
        } catch (IOException e2) {
            bridge.throwException(e2);
        }
    }

    public final boolean enableReplaceObjectDelegate(boolean z2) {
        return false;
    }

    @Override // java.io.ObjectOutputStream
    protected final void annotateClass(Class<?> cls) throws IOException {
        throw new IOException("Method annotateClass not supported");
    }

    @Override // java.io.ObjectOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
    }

    @Override // java.io.ObjectOutputStream
    protected final void drain() throws IOException {
    }

    @Override // java.io.ObjectOutputStream, java.io.OutputStream, java.io.Flushable
    public final void flush() throws IOException {
        try {
            this.orbStream.flush();
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream
    protected final Object replaceObject(Object obj) throws IOException {
        throw new IOException("Method replaceObject not supported");
    }

    @Override // java.io.ObjectOutputStream
    public final void reset() throws IOException {
        try {
            if (this.currentObject != null || this.currentClassDesc != null) {
                throw new IOException("Illegal call to reset");
            }
            this.abortIOException = null;
            if (this.classDescStack == null) {
                this.classDescStack = new Stack();
            } else {
                this.classDescStack.setSize(0);
            }
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.OutputStream
    public final void write(byte[] bArr) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_octet_array(bArr, 0, bArr.length);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.OutputStream
    public final void write(byte[] bArr, int i2, int i3) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_octet_array(bArr, i2, i3);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.OutputStream
    public final void write(int i2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_octet((byte) (i2 & 255));
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeBoolean(boolean z2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_boolean(z2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeByte(int i2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_octet((byte) i2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeBytes(String str) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            byte[] bytes = str.getBytes();
            this.orbStream.write_octet_array(bytes, 0, bytes.length);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeChar(int i2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_wchar((char) i2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeChars(String str) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            char[] charArray = str.toCharArray();
            this.orbStream.write_wchar_array(charArray, 0, charArray.length);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeDouble(double d2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_double(d2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeFloat(float f2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_float(f2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeInt(int i2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_long(i2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeLong(long j2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_longlong(j2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeShort(int i2) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            this.orbStream.write_short((short) i2);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // java.io.ObjectOutputStream
    protected final void writeStreamHeader() throws IOException {
    }

    protected void internalWriteUTF(org.omg.CORBA.portable.OutputStream outputStream, String str) {
        outputStream.write_wstring(str);
    }

    @Override // java.io.ObjectOutputStream, java.io.DataOutput
    public final void writeUTF(String str) throws IOException {
        try {
            this.writeObjectState.writeData(this);
            internalWriteUTF(this.orbStream, str);
        } catch (Error e2) {
            IOException iOException = new IOException(e2.getMessage());
            iOException.initCause(e2);
            throw iOException;
        }
    }

    private boolean checkSpecialClasses(Object obj) throws IOException {
        if (obj instanceof ObjectStreamClass) {
            throw new IOException("Serialization of ObjectStreamClass not supported");
        }
        return false;
    }

    private boolean checkSubstitutableSpecialClasses(Object obj) throws IOException {
        if (obj instanceof String) {
            this.orbStream.write_value((Serializable) obj);
            return true;
        }
        return false;
    }

    private void outputObject(Object obj) throws IOException {
        ObjectStreamClass objectStreamClass;
        this.currentObject = obj;
        Class<?> cls = obj.getClass();
        this.currentClassDesc = ObjectStreamClass.lookup(cls);
        if (this.currentClassDesc == null) {
            throw new NotSerializableException(cls.getName());
        }
        if (this.currentClassDesc.isExternalizable()) {
            this.orbStream.write_octet(this.streamFormatVersion);
            ((Externalizable) obj).writeExternal(this);
            return;
        }
        if (this.currentClassDesc.forClass().getName().equals("java.lang.String")) {
            writeUTF((String) obj);
            return;
        }
        int size = this.classDescStack.size();
        while (true) {
            try {
                ObjectStreamClass superclass = this.currentClassDesc.getSuperclass();
                if (superclass == null) {
                    break;
                }
                this.classDescStack.push(this.currentClassDesc);
                this.currentClassDesc = superclass;
            } finally {
                this.classDescStack.setSize(size);
            }
        }
        do {
            OutputStreamHook.WriteObjectState writeObjectState = this.writeObjectState;
            try {
                setState(NOT_IN_WRITE_OBJECT);
                if (this.currentClassDesc.hasWriteObject()) {
                    invokeObjectWriter(this.currentClassDesc, obj);
                } else {
                    defaultWriteObjectDelegate();
                }
                setState(writeObjectState);
                if (this.classDescStack.size() <= size) {
                    break;
                }
                objectStreamClass = (ObjectStreamClass) this.classDescStack.pop();
                this.currentClassDesc = objectStreamClass;
            } catch (Throwable th) {
                setState(writeObjectState);
                throw th;
            }
        } while (objectStreamClass != null);
    }

    private void invokeObjectWriter(ObjectStreamClass objectStreamClass, Object obj) throws IOException, IllegalArgumentException {
        objectStreamClass.forClass();
        try {
            this.orbStream.write_octet(this.streamFormatVersion);
            this.writeObjectState.enterWriteObject(this);
            objectStreamClass.writeObjectMethod.invoke(obj, this.writeObjectArgList);
            this.writeObjectState.exitWriteObject(this);
        } catch (IllegalAccessException e2) {
        } catch (InvocationTargetException e3) {
            Throwable targetException = e3.getTargetException();
            if (targetException instanceof IOException) {
                throw ((IOException) targetException);
            }
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            if (targetException instanceof Error) {
                throw ((Error) targetException);
            }
            throw new Error("invokeObjectWriter internal error", e3);
        }
    }

    @Override // com.sun.corba.se.impl.io.OutputStreamHook
    void writeField(ObjectStreamField objectStreamField, Object obj) throws IOException {
        switch (objectStreamField.getTypeCode()) {
            case 'B':
                if (obj == null) {
                    this.orbStream.write_octet((byte) 0);
                    return;
                } else {
                    this.orbStream.write_octet(((Byte) obj).byteValue());
                    return;
                }
            case 'C':
                if (obj == null) {
                    this.orbStream.write_wchar((char) 0);
                    return;
                } else {
                    this.orbStream.write_wchar(((Character) obj).charValue());
                    return;
                }
            case 'D':
                if (obj == null) {
                    this.orbStream.write_double(0.0d);
                    return;
                } else {
                    this.orbStream.write_double(((Double) obj).doubleValue());
                    return;
                }
            case 'E':
            case 'G':
            case 'H':
            case 'K':
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
                if (obj == null) {
                    this.orbStream.write_float(0.0f);
                    return;
                } else {
                    this.orbStream.write_float(((Float) obj).floatValue());
                    return;
                }
            case 'I':
                if (obj == null) {
                    this.orbStream.write_long(0);
                    return;
                } else {
                    this.orbStream.write_long(((Integer) obj).intValue());
                    return;
                }
            case 'J':
                if (obj == null) {
                    this.orbStream.write_longlong(0L);
                    return;
                } else {
                    this.orbStream.write_longlong(((Long) obj).longValue());
                    return;
                }
            case 'L':
            case '[':
                writeObjectField(objectStreamField, obj);
                return;
            case 'S':
                if (obj == null) {
                    this.orbStream.write_short((short) 0);
                    return;
                } else {
                    this.orbStream.write_short(((Short) obj).shortValue());
                    return;
                }
            case 'Z':
                if (obj == null) {
                    this.orbStream.write_boolean(false);
                    return;
                } else {
                    this.orbStream.write_boolean(((Boolean) obj).booleanValue());
                    return;
                }
        }
    }

    private void writeObjectField(ObjectStreamField objectStreamField, Object obj) throws IOException {
        if (ObjectStreamClassCorbaExt.isAny(objectStreamField.getTypeString())) {
            Util.writeAny(this.orbStream, obj);
            return;
        }
        Class type = objectStreamField.getType();
        boolean z2 = 2;
        if (type.isInterface()) {
            type.getName();
            if (Remote.class.isAssignableFrom(type) || Object.class.isAssignableFrom(type)) {
                z2 = false;
            } else if (RepositoryId.isAbstractBase(type) || ObjectStreamClassCorbaExt.isAbstractInterface(type)) {
                z2 = true;
            }
        }
        switch (z2) {
            case false:
                Util.writeRemoteObject(this.orbStream, obj);
                return;
            case true:
                Util.writeAbstractObject(this.orbStream, obj);
                return;
            case true:
                try {
                    this.orbStream.write_value((Serializable) obj, type);
                    return;
                } catch (ClassCastException e2) {
                    if (obj instanceof Serializable) {
                        throw e2;
                    }
                    Utility.throwNotSerializableForCorba(obj.getClass().getName());
                    return;
                }
            default:
                return;
        }
    }

    private void outputClassFields(Object obj, Class cls, ObjectStreamField[] objectStreamFieldArr) throws IOException, IllegalArgumentException {
        for (int i2 = 0; i2 < objectStreamFieldArr.length; i2++) {
            if (objectStreamFieldArr[i2].getField() == null) {
                throw new InvalidClassException(cls.getName(), "Nonexistent field " + objectStreamFieldArr[i2].getName());
            }
            try {
                switch (objectStreamFieldArr[i2].getTypeCode()) {
                    case 'B':
                        this.orbStream.write_octet(objectStreamFieldArr[i2].getField().getByte(obj));
                    case 'C':
                        this.orbStream.write_wchar(objectStreamFieldArr[i2].getField().getChar(obj));
                    case 'D':
                        this.orbStream.write_double(objectStreamFieldArr[i2].getField().getDouble(obj));
                    case 'E':
                    case 'G':
                    case 'H':
                    case 'K':
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
                        this.orbStream.write_float(objectStreamFieldArr[i2].getField().getFloat(obj));
                    case 'I':
                        this.orbStream.write_long(objectStreamFieldArr[i2].getField().getInt(obj));
                    case 'J':
                        this.orbStream.write_longlong(objectStreamFieldArr[i2].getField().getLong(obj));
                    case 'L':
                    case '[':
                        writeObjectField(objectStreamFieldArr[i2], objectStreamFieldArr[i2].getField().get(obj));
                    case 'S':
                        this.orbStream.write_short(objectStreamFieldArr[i2].getField().getShort(obj));
                    case 'Z':
                        this.orbStream.write_boolean(objectStreamFieldArr[i2].getField().getBoolean(obj));
                }
            } catch (IllegalAccessException e2) {
                throw this.wrapper.illegalFieldAccess(e2, objectStreamFieldArr[i2].getName());
            }
        }
    }
}
