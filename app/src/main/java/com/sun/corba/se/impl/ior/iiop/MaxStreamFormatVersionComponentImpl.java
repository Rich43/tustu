package com.sun.corba.se.impl.ior.iiop;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.ior.TaggedComponentBase;
import com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent;
import org.omg.CORBA_2_3.portable.OutputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/iiop/MaxStreamFormatVersionComponentImpl.class */
public class MaxStreamFormatVersionComponentImpl extends TaggedComponentBase implements MaxStreamFormatVersionComponent {
    private byte version;
    public static final MaxStreamFormatVersionComponentImpl singleton = new MaxStreamFormatVersionComponentImpl();

    public boolean equals(Object obj) {
        return (obj instanceof MaxStreamFormatVersionComponentImpl) && this.version == ((MaxStreamFormatVersionComponentImpl) obj).version;
    }

    public int hashCode() {
        return this.version;
    }

    public String toString() {
        return "MaxStreamFormatVersionComponentImpl[version=" + ((int) this.version) + "]";
    }

    public MaxStreamFormatVersionComponentImpl() {
        this.version = ORBUtility.getMaxStreamFormatVersion();
    }

    public MaxStreamFormatVersionComponentImpl(byte b2) {
        this.version = b2;
    }

    @Override // com.sun.corba.se.spi.ior.iiop.MaxStreamFormatVersionComponent
    public byte getMaxStreamFormatVersion() {
        return this.version;
    }

    @Override // com.sun.corba.se.spi.ior.WriteContents
    public void writeContents(OutputStream outputStream) {
        outputStream.write_octet(this.version);
    }

    @Override // com.sun.corba.se.spi.ior.Identifiable
    public int getId() {
        return 38;
    }
}
