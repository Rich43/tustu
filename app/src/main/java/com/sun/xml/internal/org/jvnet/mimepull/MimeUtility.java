package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MimeUtility.class */
final class MimeUtility {
    private static final boolean ignoreUnknownEncoding = PropUtil.getBooleanSystemProperty("mail.mime.ignoreunknownencoding", false);

    private MimeUtility() {
    }

    public static InputStream decode(InputStream is, String encoding) throws DecodingException {
        if (encoding.equalsIgnoreCase("base64")) {
            return new BASE64DecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("quoted-printable")) {
            return new QPDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("uuencode") || encoding.equalsIgnoreCase("x-uuencode") || encoding.equalsIgnoreCase("x-uue")) {
            return new UUDecoderStream(is);
        }
        if (encoding.equalsIgnoreCase("binary") || encoding.equalsIgnoreCase("7bit") || encoding.equalsIgnoreCase("8bit")) {
            return is;
        }
        if (!ignoreUnknownEncoding) {
            throw new DecodingException("Unknown encoding: " + encoding);
        }
        return is;
    }
}
