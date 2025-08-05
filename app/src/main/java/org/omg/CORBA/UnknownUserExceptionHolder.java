package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/UnknownUserExceptionHolder.class */
public final class UnknownUserExceptionHolder implements Streamable {
    public UnknownUserException value;

    public UnknownUserExceptionHolder() {
        this.value = null;
    }

    public UnknownUserExceptionHolder(UnknownUserException unknownUserException) {
        this.value = null;
        this.value = unknownUserException;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = UnknownUserExceptionHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        UnknownUserExceptionHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return UnknownUserExceptionHelper.type();
    }
}
