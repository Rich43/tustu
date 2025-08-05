package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/QDecoderStream.class */
public class QDecoderStream extends QPDecoderStream {
    public QDecoderStream(InputStream in) {
        super(in);
    }

    @Override // com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPDecoderStream, java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int c2 = this.in.read();
        if (c2 == 95) {
            return 32;
        }
        if (c2 == 61) {
            this.f12078ba[0] = (byte) this.in.read();
            this.f12078ba[1] = (byte) this.in.read();
            try {
                return ASCIIUtility.parseInt(this.f12078ba, 0, 2, 16);
            } catch (NumberFormatException nex) {
                throw new IOException("Error in QP stream " + nex.getMessage());
            }
        }
        return c2;
    }
}
