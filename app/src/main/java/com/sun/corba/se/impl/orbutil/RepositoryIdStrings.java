package com.sun.corba.se.impl.orbutil;

import com.sun.corba.se.impl.io.TypeMismatchException;
import java.io.Serializable;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/RepositoryIdStrings.class */
public interface RepositoryIdStrings {
    String createForAnyType(Class cls);

    String createForJavaType(Serializable serializable) throws TypeMismatchException;

    String createForJavaType(Class cls) throws TypeMismatchException;

    String createSequenceRepID(Object obj);

    String createSequenceRepID(Class cls);

    RepositoryIdInterface getFromString(String str);

    String getClassDescValueRepId();

    String getWStringValueRepId();
}
