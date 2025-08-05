package jdk.internal.org.xml.sax;

import java.io.InputStream;
import java.io.Reader;

/* loaded from: rt.jar:jdk/internal/org/xml/sax/InputSource.class */
public class InputSource {
    private String publicId;
    private String systemId;
    private InputStream byteStream;
    private String encoding;
    private Reader characterStream;

    public InputSource() {
    }

    public InputSource(String str) {
        setSystemId(str);
    }

    public InputSource(InputStream inputStream) {
        setByteStream(inputStream);
    }

    public InputSource(Reader reader) {
        setCharacterStream(reader);
    }

    public void setPublicId(String str) {
        this.publicId = str;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public void setSystemId(String str) {
        this.systemId = str;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setByteStream(InputStream inputStream) {
        this.byteStream = inputStream;
    }

    public InputStream getByteStream() {
        return this.byteStream;
    }

    public void setEncoding(String str) {
        this.encoding = str;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setCharacterStream(Reader reader) {
        this.characterStream = reader;
    }

    public Reader getCharacterStream() {
        return this.characterStream;
    }
}
