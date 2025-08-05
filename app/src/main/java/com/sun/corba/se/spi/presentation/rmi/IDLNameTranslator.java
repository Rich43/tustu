package com.sun.corba.se.spi.presentation.rmi;

import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/IDLNameTranslator.class */
public interface IDLNameTranslator {
    Class[] getInterfaces();

    Method[] getMethods();

    Method getMethod(String str);

    String getIDLName(Method method);
}
