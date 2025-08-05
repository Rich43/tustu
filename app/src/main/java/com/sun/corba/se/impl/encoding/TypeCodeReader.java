package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.corba.TypeCodeImpl;
import org.omg.CORBA_2_3.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/TypeCodeReader.class */
public interface TypeCodeReader extends MarshalInputStream {
    void addTypeCodeAtPosition(TypeCodeImpl typeCodeImpl, int i2);

    TypeCodeImpl getTypeCodeAtPosition(int i2);

    void setEnclosingInputStream(InputStream inputStream);

    TypeCodeReader getTopLevelStream();

    int getTopLevelPosition();

    @Override // com.sun.corba.se.impl.encoding.MarshalInputStream
    int getPosition();

    void printTypeMap();
}
