package com.sun.corba.se.impl.orbutil;

import com.sun.corba.se.impl.io.TypeMismatchException;
import com.sun.corba.se.impl.util.RepositoryId;
import java.io.Serializable;
import java.net.MalformedURLException;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/RepIdDelegator.class */
public final class RepIdDelegator implements RepositoryIdStrings, RepositoryIdUtility, RepositoryIdInterface {
    private final RepositoryId delegate;

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public String createForAnyType(Class cls) {
        return RepositoryId.createForAnyType(cls);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public String createForJavaType(Serializable serializable) throws TypeMismatchException {
        return RepositoryId.createForJavaType(serializable);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public String createForJavaType(Class cls) throws TypeMismatchException {
        return RepositoryId.createForJavaType(cls);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public String createSequenceRepID(Object obj) {
        return RepositoryId.createSequenceRepID(obj);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public String createSequenceRepID(Class cls) {
        return RepositoryId.createSequenceRepID(cls);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public RepositoryIdInterface getFromString(String str) {
        return new RepIdDelegator(RepositoryId.cache.getId(str));
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public boolean isChunkedEncoding(int i2) {
        return RepositoryId.isChunkedEncoding(i2);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public boolean isCodeBasePresent(int i2) {
        return RepositoryId.isCodeBasePresent(i2);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public String getClassDescValueRepId() {
        return RepositoryId.kClassDescValueRepID;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdStrings
    public String getWStringValueRepId() {
        return RepositoryId.kWStringValueRepID;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getTypeInfo(int i2) {
        return RepositoryId.getTypeInfo(i2);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getStandardRMIChunkedNoRepStrId() {
        return RepositoryId.kPreComputed_StandardRMIChunked_NoRep;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getCodeBaseRMIChunkedNoRepStrId() {
        return RepositoryId.kPreComputed_CodeBaseRMIChunked_NoRep;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getStandardRMIChunkedId() {
        return RepositoryId.kPreComputed_StandardRMIChunked;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getCodeBaseRMIChunkedId() {
        return RepositoryId.kPreComputed_CodeBaseRMIChunked;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getStandardRMIUnchunkedId() {
        return RepositoryId.kPreComputed_StandardRMIUnchunked;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getCodeBaseRMIUnchunkedId() {
        return RepositoryId.kPreComputed_CodeBaseRMIUnchunked;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getStandardRMIUnchunkedNoRepStrId() {
        return RepositoryId.kPreComputed_StandardRMIUnchunked_NoRep;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdUtility
    public int getCodeBaseRMIUnchunkedNoRepStrId() {
        return RepositoryId.kPreComputed_CodeBaseRMIUnchunked_NoRep;
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdInterface
    public Class getClassFromType() throws ClassNotFoundException {
        return this.delegate.getClassFromType();
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdInterface
    public Class getClassFromType(String str) throws MalformedURLException, ClassNotFoundException {
        return this.delegate.getClassFromType(str);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdInterface
    public Class getClassFromType(Class cls, String str) throws MalformedURLException, ClassNotFoundException {
        return this.delegate.getClassFromType(cls, str);
    }

    @Override // com.sun.corba.se.impl.orbutil.RepositoryIdInterface
    public String getClassName() {
        return this.delegate.getClassName();
    }

    public RepIdDelegator() {
        this(null);
    }

    private RepIdDelegator(RepositoryId repositoryId) {
        this.delegate = repositoryId;
    }

    public String toString() {
        if (this.delegate != null) {
            return this.delegate.toString();
        }
        return getClass().getName();
    }

    public boolean equals(Object obj) {
        if (this.delegate != null) {
            return this.delegate.equals(obj);
        }
        return super.equals(obj);
    }

    public int hashCode() {
        if (this.delegate != null) {
            return this.delegate.hashCode();
        }
        return super.hashCode();
    }
}
