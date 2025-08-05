package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.ior.TaggedComponentBase;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/JavaSerializationComponent.class */
public class JavaSerializationComponent extends TaggedComponentBase {
    private byte version;
    private static JavaSerializationComponent singleton;

    public static JavaSerializationComponent singleton() {
        if (singleton == null) {
            synchronized (JavaSerializationComponent.class) {
                singleton = new JavaSerializationComponent((byte) 1);
            }
        }
        return singleton;
    }

    public JavaSerializationComponent(byte b2) {
        this.version = b2;
    }

    public byte javaSerializationVersion() {
        return this.version;
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        outputStream.write_octet(this.version);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return ORBConstants.TAG_JAVA_SERIALIZATION_ID;
    }

    public boolean equals(Object obj) {
        return (obj instanceof JavaSerializationComponent) && this.version == ((JavaSerializationComponent) obj).version;
    }

    public int hashCode() {
        return this.version;
    }
}
