package sun.security.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/* loaded from: rt.jar:sun/security/util/Pem.class */
public class Pem {
    public static byte[] decode(String str) throws IOException {
        try {
            return Base64.getDecoder().decode(str.replaceAll("\\s+", "").getBytes(StandardCharsets.ISO_8859_1));
        } catch (IllegalArgumentException e2) {
            throw new IOException(e2);
        }
    }
}
