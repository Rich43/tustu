package java.security.cert;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:java/security/cert/Extension.class */
public interface Extension {
    String getId();

    boolean isCritical();

    byte[] getValue();

    void encode(OutputStream outputStream) throws IOException;
}
