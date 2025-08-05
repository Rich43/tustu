package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/QEncoderStream.class */
public class QEncoderStream extends QPEncoderStream {
    private String specials;
    private static String WORD_SPECIALS = "=_?\"#$%&'(),.:;<>@[\\]^`{|}~";
    private static String TEXT_SPECIALS = "=_?";

    public QEncoderStream(OutputStream out, boolean encodingWord) {
        super(out, Integer.MAX_VALUE);
        this.specials = encodingWord ? WORD_SPECIALS : TEXT_SPECIALS;
    }

    @Override // com.sun.xml.internal.messaging.saaj.packaging.mime.util.QPEncoderStream, java.io.FilterOutputStream, java.io.OutputStream
    public void write(int c2) throws IOException {
        int c3 = c2 & 255;
        if (c3 == 32) {
            output(95, false);
        } else if (c3 < 32 || c3 >= 127 || this.specials.indexOf(c3) >= 0) {
            output(c3, true);
        } else {
            output(c3, false);
        }
    }

    public static int encodedLength(byte[] b2, boolean encodingWord) {
        int len = 0;
        String specials = encodingWord ? WORD_SPECIALS : TEXT_SPECIALS;
        for (byte b3 : b2) {
            int c2 = b3 & 255;
            if (c2 < 32 || c2 >= 127 || specials.indexOf(c2) >= 0) {
                len += 3;
            } else {
                len++;
            }
        }
        return len;
    }
}
