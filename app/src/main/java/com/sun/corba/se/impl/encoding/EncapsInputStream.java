package com.sun.corba.se.impl.encoding;

import com.sun.corba.se.impl.encoding.CodeSetConversion;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.org.omg.SendingContext.CodeBase;
import java.nio.ByteBuffer;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import sun.corba.EncapsInputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/EncapsInputStream.class */
public class EncapsInputStream extends CDRInputStream {
    private ORBUtilSystemException wrapper;
    private CodeBase codeBase;

    public EncapsInputStream(ORB orb, byte[] bArr, int i2, boolean z2, GIOPVersion gIOPVersion) {
        super(orb, ByteBuffer.wrap(bArr), i2, z2, gIOPVersion, (byte) 0, BufferManagerFactory.newBufferManagerRead(0, (byte) 0, (com.sun.corba.se.spi.orb.ORB) orb));
        this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB) orb, CORBALogDomains.RPC_ENCODING);
        performORBVersionSpecificInit();
    }

    public EncapsInputStream(ORB orb, ByteBuffer byteBuffer, int i2, boolean z2, GIOPVersion gIOPVersion) {
        super(orb, byteBuffer, i2, z2, gIOPVersion, (byte) 0, BufferManagerFactory.newBufferManagerRead(0, (byte) 0, (com.sun.corba.se.spi.orb.ORB) orb));
        performORBVersionSpecificInit();
    }

    public EncapsInputStream(ORB orb, byte[] bArr, int i2) {
        this(orb, bArr, i2, GIOPVersion.V1_2);
    }

    public EncapsInputStream(EncapsInputStream encapsInputStream) {
        super(encapsInputStream);
        this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB) encapsInputStream.orb(), CORBALogDomains.RPC_ENCODING);
        performORBVersionSpecificInit();
    }

    public EncapsInputStream(ORB orb, byte[] bArr, int i2, GIOPVersion gIOPVersion) {
        this(orb, bArr, i2, false, gIOPVersion);
    }

    public EncapsInputStream(ORB orb, byte[] bArr, int i2, GIOPVersion gIOPVersion, CodeBase codeBase) {
        super(orb, ByteBuffer.wrap(bArr), i2, false, gIOPVersion, (byte) 0, BufferManagerFactory.newBufferManagerRead(0, (byte) 0, (com.sun.corba.se.spi.orb.ORB) orb));
        this.codeBase = codeBase;
        performORBVersionSpecificInit();
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    public CDRInputStream dup() {
        return EncapsInputStreamFactory.newEncapsInputStream(this);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    protected CodeSetConversion.BTCConverter createCharBTCConverter() {
        return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.ISO_8859_1);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    protected CodeSetConversion.BTCConverter createWCharBTCConverter() {
        if (getGIOPVersion().equals(GIOPVersion.V1_0)) {
            throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
        }
        if (getGIOPVersion().equals(GIOPVersion.V1_1)) {
            return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.UTF_16, isLittleEndian());
        }
        return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.UTF_16, false);
    }

    @Override // com.sun.corba.se.impl.encoding.CDRInputStream
    public CodeBase getCodeBase() {
        return this.codeBase;
    }
}
