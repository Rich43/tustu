package org.omg.CORBA.portable;

import java.io.Serializable;

/* loaded from: rt.jar:org/omg/CORBA/portable/BoxedValueHelper.class */
public interface BoxedValueHelper {
    Serializable read_value(InputStream inputStream);

    void write_value(OutputStream outputStream, Serializable serializable);

    String get_id();
}
