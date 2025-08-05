package com.sun.org.apache.xml.internal.security.signature;

import java.util.Collections;
import java.util.List;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/VerifiedReference.class */
public class VerifiedReference {
    private final boolean valid;
    private final String uri;
    private final List<VerifiedReference> manifestReferences;

    public VerifiedReference(boolean z2, String str, List<VerifiedReference> list) {
        this.valid = z2;
        this.uri = str;
        if (list != null) {
            this.manifestReferences = list;
        } else {
            this.manifestReferences = Collections.emptyList();
        }
    }

    public boolean isValid() {
        return this.valid;
    }

    public String getUri() {
        return this.uri;
    }

    public List<VerifiedReference> getManifestReferences() {
        return Collections.unmodifiableList(this.manifestReferences);
    }
}
