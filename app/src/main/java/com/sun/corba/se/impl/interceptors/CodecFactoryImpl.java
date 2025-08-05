package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.ORB;
import org.omg.IOP.Codec;
import org.omg.IOP.CodecFactory;
import org.omg.IOP.CodecFactoryPackage.UnknownEncoding;
import org.omg.IOP.Encoding;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/CodecFactoryImpl.class */
public final class CodecFactoryImpl extends LocalObject implements CodecFactory {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private static final int MAX_MINOR_VERSION_SUPPORTED = 2;
    private Codec[] codecs = new Codec[3];

    public CodecFactoryImpl(ORB orb) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB) orb, CORBALogDomains.RPC_PROTOCOL);
        for (int i2 = 0; i2 <= 2; i2++) {
            this.codecs[i2] = new CDREncapsCodec(orb, 1, i2);
        }
    }

    @Override // org.omg.IOP.CodecFactoryOperations
    public Codec create_codec(Encoding encoding) throws UnknownEncoding {
        if (encoding == null) {
            nullParam();
        }
        Codec codec = null;
        if (encoding.format == 0 && encoding.major_version == 1 && encoding.minor_version >= 0 && encoding.minor_version <= 2) {
            codec = this.codecs[encoding.minor_version];
        }
        if (codec == null) {
            throw new UnknownEncoding();
        }
        return codec;
    }

    private void nullParam() {
        throw this.wrapper.nullParam();
    }
}
