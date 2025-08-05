package javax.xml.crypto;

import java.io.InputStream;

/* loaded from: rt.jar:javax/xml/crypto/OctetStreamData.class */
public class OctetStreamData implements Data {
    private InputStream octetStream;
    private String uri;
    private String mimeType;

    public OctetStreamData(InputStream inputStream) {
        if (inputStream == null) {
            throw new NullPointerException("octetStream is null");
        }
        this.octetStream = inputStream;
    }

    public OctetStreamData(InputStream inputStream, String str, String str2) {
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
