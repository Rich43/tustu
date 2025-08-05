package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
import java.io.InputStream;
import java.io.Reader;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SAXInputSource.class */
public final class SAXInputSource extends XMLInputSource {
    private XMLReader fXMLReader;
    private InputSource fInputSource;

    public SAXInputSource() {
        this(null);
    }

    public SAXInputSource(InputSource inputSource) {
        this(null, inputSource);
    }

    public SAXInputSource(XMLReader reader, InputSource inputSource) {
        super(inputSource != null ? inputSource.getPublicId() : null, inputSource != null ? inputSource.getSystemId() : null, null);
        if (inputSource != null) {
            setByteStream(inputSource.getByteStream());
            setCharacterStream(inputSource.getCharacterStream());
            setEncoding(inputSource.getEncoding());
        }
        this.fInputSource = inputSource;
        this.fXMLReader = reader;
    }

    public void setXMLReader(XMLReader reader) {
        this.fXMLReader = reader;
    }

    public XMLReader getXMLReader() {
        return this.fXMLReader;
    }

    public void setInputSource(InputSource inputSource) {
        if (inputSource != null) {
            setPublicId(inputSource.getPublicId());
            setSystemId(inputSource.getSystemId());
            setByteStream(inputSource.getByteStream());
            setCharacterStream(inputSource.getCharacterStream());
            setEncoding(inputSource.getEncoding());
        } else {
            setPublicId(null);
            setSystemId(null);
            setByteStream(null);
            setCharacterStream(null);
            setEncoding(null);
        }
        this.fInputSource = inputSource;
    }

    public InputSource getInputSource() {
        return this.fInputSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource
    public void setPublicId(String publicId) {
        super.setPublicId(publicId);
        if (this.fInputSource == null) {
            this.fInputSource = new InputSource();
        }
        this.fInputSource.setPublicId(publicId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource
    public void setSystemId(String systemId) {
        super.setSystemId(systemId);
        if (this.fInputSource == null) {
            this.fInputSource = new InputSource();
        }
        this.fInputSource.setSystemId(systemId);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource
    public void setByteStream(InputStream byteStream) {
        super.setByteStream(byteStream);
        if (this.fInputSource == null) {
            this.fInputSource = new InputSource();
        }
        this.fInputSource.setByteStream(byteStream);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource
    public void setCharacterStream(Reader charStream) {
        super.setCharacterStream(charStream);
        if (this.fInputSource == null) {
            this.fInputSource = new InputSource();
        }
        this.fInputSource.setCharacterStream(charStream);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource
    public void setEncoding(String encoding) {
        super.setEncoding(encoding);
        if (this.fInputSource == null) {
            this.fInputSource = new InputSource();
        }
        this.fInputSource.setEncoding(encoding);
    }
}
