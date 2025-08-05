package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.corba.AnyImpl;
import com.sun.corba.se.impl.encoding.EncapsInputStream;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import org.omg.CORBA.Any;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TypeCode;
import org.omg.IOP.Codec;
import org.omg.IOP.CodecPackage.FormatMismatch;
import org.omg.IOP.CodecPackage.InvalidTypeForEncoding;
import org.omg.IOP.CodecPackage.TypeMismatch;
import sun.corba.EncapsInputStreamFactory;
import sun.corba.OutputStreamFactory;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/CDREncapsCodec.class */
public final class CDREncapsCodec extends LocalObject implements Codec {
    private ORB orb;
    ORBUtilSystemException wrapper;
    private GIOPVersion giopVersion;

    public CDREncapsCodec(ORB orb, int i2, int i3) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB) orb, CORBALogDomains.RPC_PROTOCOL);
        this.giopVersion = GIOPVersion.getInstance((byte) i2, (byte) i3);
    }

    @Override // org.omg.IOP.CodecOperations
    public byte[] encode(Any any) throws InvalidTypeForEncoding {
        if (any == null) {
            throw this.wrapper.nullParam();
        }
        return encodeImpl(any, true);
    }

    @Override // org.omg.IOP.CodecOperations
    public Any decode(byte[] bArr) throws FormatMismatch {
        if (bArr == null) {
            throw this.wrapper.nullParam();
        }
        return decodeImpl(bArr, null);
    }

    @Override // org.omg.IOP.CodecOperations
    public byte[] encode_value(Any any) throws InvalidTypeForEncoding {
        if (any == null) {
            throw this.wrapper.nullParam();
        }
        return encodeImpl(any, false);
    }

    @Override // org.omg.IOP.CodecOperations
    public Any decode_value(byte[] bArr, TypeCode typeCode) throws FormatMismatch, TypeMismatch {
        if (bArr == null) {
            throw this.wrapper.nullParam();
        }
        if (typeCode == null) {
            throw this.wrapper.nullParam();
        }
        return decodeImpl(bArr, typeCode);
    }

    private byte[] encodeImpl(Any any, boolean z2) throws InvalidTypeForEncoding {
        if (any == null) {
            throw this.wrapper.nullParam();
        }
        EncapsOutputStream encapsOutputStreamNewEncapsOutputStream = OutputStreamFactory.newEncapsOutputStream((com.sun.corba.se.spi.orb.ORB) this.orb, this.giopVersion);
        encapsOutputStreamNewEncapsOutputStream.putEndian();
        if (z2) {
            encapsOutputStreamNewEncapsOutputStream.write_TypeCode(any.type());
        }
        any.write_value(encapsOutputStreamNewEncapsOutputStream);
        return encapsOutputStreamNewEncapsOutputStream.toByteArray();
    }

    private Any decodeImpl(byte[] bArr, TypeCode typeCode) throws FormatMismatch {
        if (bArr == null) {
            throw this.wrapper.nullParam();
        }
        try {
            EncapsInputStream encapsInputStreamNewEncapsInputStream = EncapsInputStreamFactory.newEncapsInputStream(this.orb, bArr, bArr.length, this.giopVersion);
            encapsInputStreamNewEncapsInputStream.consumeEndian();
            if (typeCode == null) {
                typeCode = encapsInputStreamNewEncapsInputStream.read_TypeCode();
            }
            AnyImpl anyImpl = new AnyImpl((com.sun.corba.se.spi.orb.ORB) this.orb);
            anyImpl.read_value(encapsInputStreamNewEncapsInputStream, typeCode);
            return anyImpl;
        } catch (RuntimeException e2) {
            throw new FormatMismatch();
        }
    }
}
