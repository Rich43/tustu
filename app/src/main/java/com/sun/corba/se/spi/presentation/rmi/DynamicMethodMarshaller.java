package com.sun.corba.se.spi.presentation.rmi;

import com.sun.corba.se.spi.orb.ORB;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA_2_3.portable.InputStream;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/DynamicMethodMarshaller.class */
public interface DynamicMethodMarshaller {
    Method getMethod();

    Object[] copyArguments(Object[] objArr, ORB orb) throws RemoteException;

    Object[] readArguments(InputStream inputStream);

    void writeArguments(OutputStream outputStream, Object[] objArr);

    Object copyResult(Object obj, ORB orb) throws RemoteException;

    Object readResult(InputStream inputStream);

    void writeResult(OutputStream outputStream, Object obj);

    boolean isDeclaredException(Throwable th);

    void writeException(OutputStream outputStream, Exception exc);

    Exception readException(ApplicationException applicationException);
}
