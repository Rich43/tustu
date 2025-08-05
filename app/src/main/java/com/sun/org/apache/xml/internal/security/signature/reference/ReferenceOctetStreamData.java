package com.sun.org.apache.xml.internal.security.signature.reference;

import java.io.InputStream;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/signature/reference/ReferenceOctetStreamData.class */
public class ReferenceOctetStreamData implements ReferenceData {
    private InputStream octetStream;
    private String uri;
    private String mimeType;

    public ReferenceOctetStreamData(InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("octetStream is null");
        }
        this.octetStream = inputStream;
    }

    public ReferenceOctetStreamData(InputStream inputStream, String str, String str2) {
        if (inputStream == null) {
            throw new NullPointerException("octetStream is null");
        }
        this.octetStream = inputStream;
        this.uri = str;
        this.mimeType = str2;
    }

    public InputStream getOctetStream() {
        return this.octetStream;
    }

    public String getURI() {
        return this.uri;
    }

    public String getMimeType() {
        return this.mimeType;
    }
}
