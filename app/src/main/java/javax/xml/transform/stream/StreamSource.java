package javax.xml.transform.stream;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.transform.Source;

/* loaded from: rt.jar:javax/xml/transform/stream/StreamSource.class */
public class StreamSource implements Source {
    public static final String FEATURE = "http://javax.xml.transform.stream.StreamSource/feature";
    private String publicId;
    private String systemId;
    private InputStream inputStream;
    private Reader reader;

    public StreamSource() {
    }

    public StreamSource(InputStream inputStream) {
        setInputStream(inputStream);
    }

    public StreamSource(InputStream inputStream, String systemId) {
        setInputStream(inputStream);
        setSystemId(systemId);
    }

    public StreamSource(Reader reader) {
        setReader(reader);
    }

    public StreamSource(Reader reader, String systemId) {
        setReader(reader);
        setSystemId(systemId);
    }

    public StreamSource(String systemId) {
        this.systemId = systemId;
    }

    public StreamSource(File f2) {
        setSystemId(f2.toURI().toASCIIString());
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Reader getReader() {
        return this.reader;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getPublicId() {
        return this.publicId;
    }

    @Override // javax.xml.transform.Source
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    @Override // javax.xml.transform.Source
    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(File f2) {
        this.systemId = f2.toURI().toASCIIString();
    }
}
