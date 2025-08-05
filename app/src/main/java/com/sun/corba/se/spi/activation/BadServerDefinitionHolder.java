package com.sun.corba.se.spi.activation;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:com/sun/corba/se/spi/activation/BadServerDefinitionHolder.class */
public final class BadServerDefinitionHolder implements Streamable {
    public BadServerDefinition value;

    public BadServerDefinitionHolder() {
        this.value = null;
    }

    public BadServerDefinitionHolder(BadServerDefinition badServerDefinition) {
        this.value = null;
        this.value = badServerDefinition;
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _read(InputStream inputStream) {
        this.value = BadServerDefinitionHelper.read(inputStream);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public void _write(OutputStream outputStream) {
        BadServerDefinitionHelper.write(outputStream, this.value);
    }

    @Override // org.omg.CORBA.portable.Streamable
    public TypeCode _type() {
        return BadServerDefinitionHelper.type();
    }
}
