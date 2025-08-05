package com.sun.corba.se.impl.orbutil;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/RepositoryIdUtility.class */
public interface RepositoryIdUtility {
    public static final int NO_TYPE_INFO = 0;
    public static final int SINGLE_REP_TYPE_INFO = 2;
    public static final int PARTIAL_LIST_TYPE_INFO = 6;

    boolean isChunkedEncoding(int i2);

    boolean isCodeBasePresent(int i2);

    int getTypeInfo(int i2);

    int getStandardRMIChunkedNoRepStrId();

    int getCodeBaseRMIChunkedNoRepStrId();

    int getStandardRMIChunkedId();

    int getCodeBaseRMIChunkedId();

    int getStandardRMIUnchunkedId();

    int getCodeBaseRMIUnchunkedId();

    int getStandardRMIUnchunkedNoRepStrId();

    int getCodeBaseRMIUnchunkedNoRepStrId();
}
