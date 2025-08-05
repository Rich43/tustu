package java.security;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Deprecated
/* loaded from: rt.jar:java/security/Certificate.class */
public interface Certificate {
    Principal getGuarantor();

    Principal getPrincipal();

    PublicKey getPublicKey();

    void encode(OutputStream outputStream) throws KeyException, IOException;

    void decode(InputStream inputStream) throws KeyException, IOException;

    String getFormat();

    String toString(boolean z2);
}
