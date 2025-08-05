package org.omg.CORBA.portable;

import org.omg.CORBA.TypeCode;

/* loaded from: rt.jar:org/omg/CORBA/portable/Streamable.class */
public interface Streamable {
    void _read(InputStream inputStream);

    void _write(OutputStream outputStream);

    TypeCode _type();
}
