package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.InputStream;
import sun.corba.EncapsInputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/EncapsOutputStream.class */
public class EncapsOutputStream extends CDROutputStream {
    static final boolean usePooledByteBuffers = false;

    public EncapsOutputStream(ORB orb) {
        this(orb, GIOPVersion.V1_2);
    }

    public EncapsOutputStream(ORB orb, GIOPVersion gIOPVersion) {
        this(orb, gIOPVersion, false);
    }

    public EncapsOutputStream(ORB orb, boolean z2) {
        this(orb, GIOPVersion.V1_2, z2);
    }

    public EncapsOutputStream(ORB orb, GIOPVersion gIOPVersion, boolean z2) {
        super(orb, gIOPVersion, (byte) 0, z2, BufferManagerFactory.newBufferManagerWrite(0, (byte) 0, orb), (byte) 1, false);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream, org.omg.CORBA.portable.OutputStream, com.sun.corba.se.impl.encoding.MarshalOutputStream
    public InputStream create_input_stream() {
        freeInternalCaches();
        return EncapsInputStreamFactory.newEncapsInputStream(orb(), getByteBuffer(), getSize(), isLittleEndian(), getGIOPVersion());
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream
    protected CodeSetConversion.CTBConverter createCharCTBConverter() {
        return CodeSetConversion.impl().getCTBConverter(OSFCodeSetRegistry.ISO_8859_1);
    }

    @Override // com.sun.corba.se.impl.encoding.CDROutputStream
    protected CodeSetConversion.CTBConverter createWCharCTBConverter() {
        if (getGIOPVersion().equals(GIOPVersion.V1_0)) {
            throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
        }
        if (getGIOPVersion().equals(GIOPVersion.V1_1)) {
            return CodeSetConversion.impl().getCTBConverter(OSFCodeSetRegistry.UTF_16, isLittleEndian(), false);
        }
        return CodeSetConversion.impl().getCTBConverter(OSFCodeSetRegistry.UTF_16, false, ((ORB) orb()).getORBData().useByteOrderMarkersInEncapsulations());
    }
}
