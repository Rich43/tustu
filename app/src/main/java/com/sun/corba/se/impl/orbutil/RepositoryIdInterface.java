package com.sun.corba.se.impl.orbutil;

import java.net.MalformedURLException;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/RepositoryIdInterface.class */
public interface RepositoryIdInterface {
    Class getClassFromType() throws ClassNotFoundException;

    Class getClassFromType(String str) throws MalformedURLException, ClassNotFoundException;

    Class getClassFromType(Class cls, String str) throws MalformedURLException, ClassNotFoundException;

    String getClassName();
}
