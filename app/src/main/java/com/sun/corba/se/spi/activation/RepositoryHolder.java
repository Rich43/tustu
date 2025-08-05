package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/RepositoryHolder.class */
public final class RepositoryHolder implements Streamable {
    public Repository value;

    public RepositoryHolder() {
        this.value = null;
    }

    public RepositoryHolder(Repository repository) {
        this.value = null;
        this.value = repository;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = RepositoryHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        RepositoryHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return RepositoryHelper.type();
    }
}
