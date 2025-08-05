package com.sun.glass.ui.win;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinHTMLCodec.class */
final class WinHTMLCodec {
    public static final String defaultCharset = "UTF-8";

    WinHTMLCodec() {
    }

    public static byte[] encode(byte[] html) {
        return HTMLCodec.convertToHTMLFormat(html);
    }

    public static byte[] decode(byte[] data) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            InputStream is = new HTMLCodec(bais, EHTMLReadMode.HTML_READ_SELECTION);
            ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
            while (true) {
                int b2 = is.read();
                if (b2 != -1) {
                    baos.write(b2);
                } else {
                    return baos.toByteArray();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException("Unexpected IOException caught", ex);
        }
    }
}
