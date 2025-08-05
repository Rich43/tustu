package java.security.cert;

import java.util.Set;

/* loaded from: rt.jar:java/security/cert/X509Extension.class */
public interface X509Extension {
    boolean hasUnsupportedCriticalExtension();

    Set<String> getCriticalExtensionOIDs();

    Set<String> getNonCriticalExtensionOIDs();

    byte[] getExtensionValue(String str);
}
