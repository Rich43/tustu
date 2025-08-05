package sun.security.util;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:sun/security/util/DerEncoder.class */
public interface DerEncoder {
    void derEncode(OutputStream outputStream) throws IOException;
}
