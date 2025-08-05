package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.UnexpectedException;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/ExceptionHandlerImpl.class */
public class ExceptionHandlerImpl implements ExceptionHandler {
    private ExceptionRW[] rws;
    private final ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PRESENTATION);

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/ExceptionHandlerImpl$ExceptionRW.class */
    public interface ExceptionRW {
        Class getExceptionClass();

        String getId();

        void write(OutputStream outputStream, Exception exc);

        Exception read(InputStream inputStream);
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/ExceptionHandlerImpl$ExceptionRWBase.class */
    public abstract class ExceptionRWBase implements ExceptionRW {
        private Class cls;
        private String id;

        public ExceptionRWBase(Class cls) {
            this.cls = cls;
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl.ExceptionRW
        public Class getExceptionClass() {
            return this.cls;
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl.ExceptionRW
        public String getId() {
            return this.id;
        }

        void setId(String str) {
            this.id = str;
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/ExceptionHandlerImpl$ExceptionRWIDLImpl.class */
    public class ExceptionRWIDLImpl extends ExceptionRWBase {
        private Method readMethod;
        private Method writeMethod;

        /* JADX WARN: Multi-variable type inference failed */
        public ExceptionRWIDLImpl(Class cls) {
            super(cls);
            String str = cls.getName() + "Helper";
            try {
                Class<?> cls2 = Class.forName(str, true, cls.getClassLoader());
                setId((String) cls2.getDeclaredMethod("id", (Class[]) null).invoke(null, (Object[]) null));
                try {
                    this.writeMethod = cls2.getDeclaredMethod("write", org.omg.CORBA.portable.OutputStream.class, cls);
                    try {
                        this.readMethod = cls2.getDeclaredMethod("read", org.omg.CORBA.portable.InputStream.class);
                    } catch (Exception e2) {
                        throw ExceptionHandlerImpl.this.wrapper.badHelperReadMethod(e2, str);
                    }
                } catch (Exception e3) {
                    throw ExceptionHandlerImpl.this.wrapper.badHelperWriteMethod(e3, str);
                }
            } catch (Exception e4) {
                throw ExceptionHandlerImpl.this.wrapper.badHelperIdMethod(e4, str);
            }
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl.ExceptionRW
        public void write(OutputStream outputStream, Exception exc) {
            try {
                this.writeMethod.invoke(null, outputStream, exc);
            } catch (Exception e2) {
                throw ExceptionHandlerImpl.this.wrapper.badHelperWriteMethod(e2, this.writeMethod.getDeclaringClass().getName());
            }
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl.ExceptionRW
        public Exception read(InputStream inputStream) {
            try {
                return (Exception) this.readMethod.invoke(null, inputStream);
            } catch (Exception e2) {
                throw ExceptionHandlerImpl.this.wrapper.badHelperReadMethod(e2, this.readMethod.getDeclaringClass().getName());
            }
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/ExceptionHandlerImpl$ExceptionRWRMIImpl.class */
    public class ExceptionRWRMIImpl extends ExceptionRWBase {
        public ExceptionRWRMIImpl(Class cls) {
            super(cls);
            setId(IDLNameTranslatorImpl.getExceptionId(cls));
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl.ExceptionRW
        public void write(OutputStream outputStream, Exception exc) {
            outputStream.write_string(getId());
            outputStream.write_value(exc, getExceptionClass());
        }

        @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandlerImpl.ExceptionRW
        public Exception read(InputStream inputStream) {
            inputStream.read_string();
            return (Exception) inputStream.read_value(getExceptionClass());
        }
    }

    public ExceptionHandlerImpl(Class[] clsArr) {
        Object exceptionRWRMIImpl;
        int i2 = 0;
        for (Class cls : clsArr) {
            if (!RemoteException.class.isAssignableFrom(cls)) {
                i2++;
            }
        }
        this.rws = new ExceptionRW[i2];
        int i3 = 0;
        for (Class cls2 : clsArr) {
            if (!RemoteException.class.isAssignableFrom(cls2)) {
                if (UserException.class.isAssignableFrom(cls2)) {
                    exceptionRWRMIImpl = new ExceptionRWIDLImpl(cls2);
                } else {
                    exceptionRWRMIImpl = new ExceptionRWRMIImpl(cls2);
                }
                int i4 = i3;
                i3++;
                this.rws[i4] = exceptionRWRMIImpl;
            }
        }
    }

    private int findDeclaredException(Class cls) {
        for (int i2 = 0; i2 < this.rws.length; i2++) {
            if (this.rws[i2].getExceptionClass().isAssignableFrom(cls)) {
                return i2;
            }
        }
        return -1;
    }

    private int findDeclaredException(String str) {
        for (int i2 = 0; i2 < this.rws.length && this.rws[i2] != null; i2++) {
            if (str.equals(this.rws[i2].getId())) {
                return i2;
            }
        }
        return -1;
    }

    @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandler
    public boolean isDeclaredException(Class cls) {
        return findDeclaredException(cls) >= 0;
    }

    @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandler
    public void writeException(OutputStream outputStream, Exception exc) {
        int iFindDeclaredException = findDeclaredException(exc.getClass());
        if (iFindDeclaredException < 0) {
            throw this.wrapper.writeUndeclaredException(exc, exc.getClass().getName());
        }
        this.rws[iFindDeclaredException].write(outputStream, exc);
    }

    @Override // com.sun.corba.se.impl.presentation.rmi.ExceptionHandler
    public Exception readException(ApplicationException applicationException) {
        InputStream inputStream = (InputStream) applicationException.getInputStream();
        int iFindDeclaredException = findDeclaredException(applicationException.getId());
        if (iFindDeclaredException < 0) {
            UnexpectedException unexpectedException = new UnexpectedException(inputStream.read_string());
            unexpectedException.initCause(applicationException);
            return unexpectedException;
        }
        return this.rws[iFindDeclaredException].read(inputStream);
    }

    public ExceptionRW getRMIExceptionRW(Class cls) {
        return new ExceptionRWRMIImpl(cls);
    }
}
