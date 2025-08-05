package com.sun.jndi.ldap;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import net.lingala.zip4j.util.InternalZipConstants;
import sun.misc.HexDumpEncoder;

/* loaded from: rt.jar:com/sun/jndi/ldap/Ber.class */
public abstract class Ber {
    protected byte[] buf;
    protected int offset;
    protected int bufsize;
    public static final int ASN_BOOLEAN = 1;
    public static final int ASN_INTEGER = 2;
    public static final int ASN_BIT_STRING = 3;
    public static final int ASN_SIMPLE_STRING = 4;
    public static final int ASN_OCTET_STR = 4;
    public static final int ASN_NULL = 5;
    public static final int ASN_OBJECT_ID = 6;
    public static final int ASN_SEQUENCE = 16;
    public static final int ASN_SET = 17;
    public static final int ASN_PRIMITIVE = 0;
    public static final int ASN_UNIVERSAL = 0;
    public static final int ASN_CONSTRUCTOR = 32;
    public static final int ASN_APPLICATION = 64;
    public static final int ASN_CONTEXT = 128;
    public static final int ASN_PRIVATE = 192;
    public static final int ASN_ENUMERATED = 10;

    protected Ber() {
    }

    public static void dumpBER(OutputStream outputStream, String str, byte[] bArr, int i2, int i3) {
        try {
            outputStream.write(10);
            outputStream.write(str.getBytes(InternalZipConstants.CHARSET_UTF8));
            new HexDumpEncoder().encodeBuffer(new ByteArrayInputStream(bArr, i2, i3), outputStream);
            outputStream.write(10);
        } catch (IOException e2) {
            try {
                outputStream.write("Ber.dumpBER(): error encountered\n".getBytes(InternalZipConstants.CHARSET_UTF8));
            } catch (IOException e3) {
            }
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/ldap/Ber$EncodeException.class */
    static final class EncodeException extends IOException {
        private static final long serialVersionUID = -5247359637775781768L;

        EncodeException(String str) {
            super(str);
        }
    }

    /* loaded from: rt.jar:com/sun/jndi/ldap/Ber$DecodeException.class */
    static final class DecodeException extends IOException {
        private static final long serialVersionUID = 8735036969244425583L;

        DecodeException(String str) {
            super(str);
        }
    }
}
