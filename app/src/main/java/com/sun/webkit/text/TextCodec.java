package com.sun.webkit.text;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/webkit/text/TextCodec.class */
final class TextCodec {
    private final Charset charset;
    private static final Map<String, String> reMap = new HashMap();

    static {
        reMap.put("ISO-10646-UCS-2", "UTF-16");
    }

    private TextCodec(String encoding) {
        this.charset = Charset.forName(encoding);
    }

    private byte[] encode(char[] data) {
        ByteBuffer bb2 = this.charset.encode(CharBuffer.wrap(data));
        byte[] encoded = new byte[bb2.remaining()];
        bb2.get(encoded);
        return encoded;
    }

    private String decode(byte[] data) {
        CharBuffer cb = this.charset.decode(ByteBuffer.wrap(data));
        char[] decoded = new char[cb.remaining()];
        cb.get(decoded);
        return new String(decoded);
    }

    private static String[] getEncodings() {
        List<String> encodings = new ArrayList<>();
        Map<String, Charset> ac2 = Charset.availableCharsets();
        for (Map.Entry<String, Charset> entry : ac2.entrySet()) {
            String e2 = entry.getKey();
            encodings.add(e2);
            encodings.add(e2);
            Charset c2 = entry.getValue();
            for (String a2 : c2.aliases()) {
                if (!a2.equals("8859_1")) {
                    encodings.add(a2);
                    String r2 = reMap.get(a2);
                    encodings.add(r2 == null ? e2 : r2);
                }
            }
        }
        return (String[]) encodings.toArray(new String[0]);
    }
}
