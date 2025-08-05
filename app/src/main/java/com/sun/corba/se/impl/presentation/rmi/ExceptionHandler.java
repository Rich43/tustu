package com.sun.corba.se.impl.presentation.rmi;

import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/ExceptionHandler.class */
public interface ExceptionHandler {
    boolean isDeclaredException(Class cls);

    void writeException(OutputStream outputStream, Exception exc);

    Exception readException(ApplicationException applicationException);
}
