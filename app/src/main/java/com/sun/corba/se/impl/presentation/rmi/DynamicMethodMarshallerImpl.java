package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.Externalizable;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.rmi.CORBA.Util;
import javax.rmi.PortableRemoteObject;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/DynamicMethodMarshallerImpl.class */
public class DynamicMethodMarshallerImpl implements DynamicMethodMarshaller {
    Method method;
    ExceptionHandler ehandler;
    boolean hasArguments;
    boolean hasVoidResult;
    boolean needsArgumentCopy;
    boolean needsResultCopy;
    ReaderWriter[] argRWs;
    ReaderWriter resultRW;
    private static ReaderWriter booleanRW = new ReaderWriterBase("boolean") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.1
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Boolean(inputStream.read_boolean());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_boolean(((Boolean) obj).booleanValue());
        }
    };
    private static ReaderWriter byteRW = new ReaderWriterBase(SchemaSymbols.ATTVAL_BYTE) { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.2
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Byte(inputStream.read_octet());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_octet(((Byte) obj).byteValue());
        }
    };
    private static ReaderWriter charRW = new ReaderWriterBase("char") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.3
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Character(inputStream.read_wchar());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_wchar(((Character) obj).charValue());
        }
    };
    private static ReaderWriter shortRW = new ReaderWriterBase(SchemaSymbols.ATTVAL_SHORT) { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.4
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Short(inputStream.read_short());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_short(((Short) obj).shortValue());
        }
    };
    private static ReaderWriter intRW = new ReaderWriterBase("int") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.5
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Integer(inputStream.read_long());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_long(((Integer) obj).intValue());
        }
    };
    private static ReaderWriter longRW = new ReaderWriterBase(SchemaSymbols.ATTVAL_LONG) { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.6
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Long(inputStream.read_longlong());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_longlong(((Long) obj).longValue());
        }
    };
    private static ReaderWriter floatRW = new ReaderWriterBase(SchemaSymbols.ATTVAL_FLOAT) { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.7
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Float(inputStream.read_float());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_float(((Float) obj).floatValue());
        }
    };
    private static ReaderWriter doubleRW = new ReaderWriterBase(SchemaSymbols.ATTVAL_DOUBLE) { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.8
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return new Double(inputStream.read_double());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_double(((Double) obj).doubleValue());
        }
    };
    private static ReaderWriter corbaObjectRW = new ReaderWriterBase("org.omg.CORBA.Object") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.9
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return inputStream.read_Object();
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            outputStream.write_Object((Object) obj);
        }
    };
    private static ReaderWriter anyRW = new ReaderWriterBase("any") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.10
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return Util.readAny(inputStream);
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            Util.writeAny(outputStream, obj);
        }
    };
    private static ReaderWriter abstractInterfaceRW = new ReaderWriterBase("abstract_interface") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.11
        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public Object read(InputStream inputStream) {
            return inputStream.read_abstract_interface();
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
        public void write(OutputStream outputStream, Object obj) {
            Util.writeAbstractObject(outputStream, obj);
        }
    };

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/DynamicMethodMarshallerImpl$ReaderWriter.class */
    public interface ReaderWriter {
        Object read(InputStream inputStream);

        void write(OutputStream outputStream, Object obj);
    }

    private static boolean isAnyClass(Class cls) {
        return cls.equals(Object.class) || cls.equals(Serializable.class) || cls.equals(Externalizable.class);
    }

    private static boolean isAbstractInterface(Class cls) {
        if (IDLEntity.class.isAssignableFrom(cls)) {
            return cls.isInterface();
        }
        return cls.isInterface() && allMethodsThrowRemoteException(cls);
    }

    private static boolean allMethodsThrowRemoteException(Class cls) throws SecurityException {
        for (Method method : cls.getMethods()) {
            if (method.getDeclaringClass() != Object.class && !throwsRemote(method)) {
                return false;
            }
        }
        return true;
    }

    private static boolean throwsRemote(Method method) {
        for (Class<?> cls : method.getExceptionTypes()) {
            if (RemoteException.class.isAssignableFrom(cls)) {
                return true;
            }
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/DynamicMethodMarshallerImpl$ReaderWriterBase.class */
    static abstract class ReaderWriterBase implements ReaderWriter {
        String name;

        public ReaderWriterBase(String str) {
            this.name = str;
        }

        public String toString() {
            return "ReaderWriter[" + this.name + "]";
        }
    }

    public static ReaderWriter makeReaderWriter(final Class cls) {
        if (cls.equals(Boolean.TYPE)) {
            return booleanRW;
        }
        if (cls.equals(Byte.TYPE)) {
            return byteRW;
        }
        if (cls.equals(Character.TYPE)) {
            return charRW;
        }
        if (cls.equals(Short.TYPE)) {
            return shortRW;
        }
        if (cls.equals(Integer.TYPE)) {
            return intRW;
        }
        if (cls.equals(Long.TYPE)) {
            return longRW;
        }
        if (cls.equals(Float.TYPE)) {
            return floatRW;
        }
        if (cls.equals(Double.TYPE)) {
            return doubleRW;
        }
        if (Remote.class.isAssignableFrom(cls)) {
            return new ReaderWriterBase("remote(" + cls.getName() + ")") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.12
                @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
                public Object read(InputStream inputStream) {
                    return PortableRemoteObject.narrow(inputStream.read_Object(), cls);
                }

                @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
                public void write(OutputStream outputStream, Object obj) {
                    Util.writeRemoteObject(outputStream, obj);
                }
            };
        }
        if (cls.equals(Object.class)) {
            return corbaObjectRW;
        }
        if (Object.class.isAssignableFrom(cls)) {
            return new ReaderWriterBase("org.omg.CORBA.Object(" + cls.getName() + ")") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.13
                @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
                public Object read(InputStream inputStream) {
                    return inputStream.read_Object(cls);
                }

                @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
                public void write(OutputStream outputStream, Object obj) {
                    outputStream.write_Object((Object) obj);
                }
            };
        }
        if (isAnyClass(cls)) {
            return anyRW;
        }
        if (isAbstractInterface(cls)) {
            return abstractInterfaceRW;
        }
        return new ReaderWriterBase("value(" + cls.getName() + ")") { // from class: com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.14
            @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
            public Object read(InputStream inputStream) {
                return inputStream.read_value(cls);
            }

            @Override // com.sun.corba.se.impl.presentation.rmi.DynamicMethodMarshallerImpl.ReaderWriter
            public void write(OutputStream outputStream, Object obj) {
                outputStream.write_value((Serializable) obj, cls);
            }
        };
    }

    public DynamicMethodMarshallerImpl(Method method) {
        this.hasArguments = true;
        this.hasVoidResult = true;
        this.argRWs = null;
        this.resultRW = null;
        this.method = method;
        this.ehandler = new ExceptionHandlerImpl(method.getExceptionTypes());
        this.needsArgumentCopy = false;
        Class<?>[] parameterTypes = method.getParameterTypes();
        this.hasArguments = parameterTypes.length > 0;
        if (this.hasArguments) {
            this.argRWs = new ReaderWriter[parameterTypes.length];
            for (int i2 = 0; i2 < parameterTypes.length; i2++) {
                if (!parameterTypes[i2].isPrimitive()) {
                    this.needsArgumentCopy = true;
                }
                this.argRWs[i2] = makeReaderWriter(parameterTypes[i2]);
            }
        }
        Class<?> returnType = method.getReturnType();
        this.needsResultCopy = false;
        this.hasVoidResult = returnType.equals(Void.TYPE);
        if (!this.hasVoidResult) {
            this.needsResultCopy = !returnType.isPrimitive();
            this.resultRW = makeReaderWriter(returnType);
        }
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public Method getMethod() {
        return this.method;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public Object[] copyArguments(Object[] objArr, ORB orb) throws RemoteException {
        if (this.needsArgumentCopy) {
            return Util.copyObjects(objArr, orb);
        }
        return objArr;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public Object[] readArguments(InputStream inputStream) {
        Object[] objArr = null;
        if (this.hasArguments) {
            objArr = new Object[this.argRWs.length];
            for (int i2 = 0; i2 < this.argRWs.length; i2++) {
                objArr[i2] = this.argRWs[i2].read(inputStream);
            }
        }
        return objArr;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public void writeArguments(OutputStream outputStream, Object[] objArr) {
        if (this.hasArguments) {
            if (objArr.length != this.argRWs.length) {
                throw new IllegalArgumentException("Expected " + this.argRWs.length + " arguments, but got " + objArr.length + " arguments.");
            }
            for (int i2 = 0; i2 < this.argRWs.length; i2++) {
                this.argRWs[i2].write(outputStream, objArr[i2]);
            }
        }
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public Object copyResult(Object obj, ORB orb) throws RemoteException {
        if (this.needsResultCopy) {
            return Util.copyObject(obj, orb);
        }
        return obj;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public Object readResult(InputStream inputStream) {
        if (this.hasVoidResult) {
            return null;
        }
        return this.resultRW.read(inputStream);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public void writeResult(OutputStream outputStream, Object obj) {
        if (!this.hasVoidResult) {
            this.resultRW.write(outputStream, obj);
        }
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public boolean isDeclaredException(Throwable th) {
        return this.ehandler.isDeclaredException(th.getClass());
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public void writeException(OutputStream outputStream, Exception exc) {
        this.ehandler.writeException(outputStream, exc);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller
    public Exception readException(ApplicationException applicationException) {
        return this.ehandler.readException(applicationException);
    }
}
