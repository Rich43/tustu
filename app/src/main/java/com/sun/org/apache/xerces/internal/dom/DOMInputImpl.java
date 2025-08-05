package com.sun.org.apache.xerces.internal.dom;

import java.io.InputStream;
import java.io.Reader;
import org.w3c.dom.ls.LSInput;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMInputImpl.class */
public class DOMInputImpl implements LSInput {
    protected String fPublicId;
    protected String fSystemId;
    protected String fBaseSystemId;
    protected InputStream fByteStream;
    protected Reader fCharStream;
    protected String fData;
    protected String fEncoding;
    protected boolean fCertifiedText;

    public DOMInputImpl() {
        this.fPublicId = null;
        this.fSystemId = null;
        this.fBaseSystemId = null;
        this.fByteStream = null;
        this.fCharStream = null;
        this.fData = null;
        this.fEncoding = null;
        this.fCertifiedText = false;
    }

    public DOMInputImpl(String publicId, String systemId, String baseSystemId) {
        this.fPublicId = null;
        this.fSystemId = null;
        this.fBaseSystemId = null;
        this.fByteStream = null;
        this.fCharStream = null;
        this.fData = null;
        this.fEncoding = null;
        this.fCertifiedText = false;
        this.fPublicId = publicId;
        this.fSystemId = systemId;
        this.fBaseSystemId = baseSystemId;
    }

    public DOMInputImpl(String publicId, String systemId, String baseSystemId, InputStream byteStream, String encoding) {
        this.fPublicId = null;
        this.fSystemId = null;
        this.fBaseSystemId = null;
        this.fByteStream = null;
        this.fCharStream = null;
        this.fData = null;
        this.fEncoding = null;
        this.fCertifiedText = false;
        this.fPublicId = publicId;
        this.fSystemId = systemId;
        this.fBaseSystemId = baseSystemId;
        this.fByteStream = byteStream;
        this.fEncoding = encoding;
    }

    public DOMInputImpl(String publicId, String systemId, String baseSystemId, Reader charStream, String encoding) {
        this.fPublicId = null;
        this.fSystemId = null;
        this.fBaseSystemId = null;
        this.fByteStream = null;
        this.fCharStream = null;
        this.fData = null;
        this.fEncoding = null;
        this.fCertifiedText = false;
        this.fPublicId = publicId;
        this.fSystemId = systemId;
        this.fBaseSystemId = baseSystemId;
        this.fCharStream = charStream;
        this.fEncoding = encoding;
    }

    public DOMInputImpl(String publicId, String systemId, String baseSystemId, String data, String encoding) {
        this.fPublicId = null;
        this.fSystemId = null;
        this.fBaseSystemId = null;
        this.fByteStream = null;
        this.fCharStream = null;
        this.fData = null;
        this.fEncoding = null;
        this.fCertifiedText = false;
        this.fPublicId = publicId;
        this.fSystemId = systemId;
        this.fBaseSystemId = baseSystemId;
        this.fData = data;
        this.fEncoding = encoding;
    }

    @Override // org.w3c.dom.ls.LSInput
    public InputStream getByteStream() {
        return this.fByteStream;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setByteStream(InputStream byteStream) {
        this.fByteStream = byteStream;
    }

    @Override // org.w3c.dom.ls.LSInput
    public Reader getCharacterStream() {
        return this.fCharStream;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setCharacterStream(Reader characterStream) {
        this.fCharStream = characterStream;
    }

    @Override // org.w3c.dom.ls.LSInput
    public String getStringData() {
        return this.fData;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setStringData(String stringData) {
        this.fData = stringData;
    }

    @Override // org.w3c.dom.ls.LSInput
    public String getEncoding() {
        return this.fEncoding;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setEncoding(String encoding) {
        this.fEncoding = encoding;
    }

    @Override // org.w3c.dom.ls.LSInput
    public String getPublicId() {
        return this.fPublicId;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setPublicId(String publicId) {
        this.fPublicId = publicId;
    }

    @Override // org.w3c.dom.ls.LSInput
    public String getSystemId() {
        return this.fSystemId;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setSystemId(String systemId) {
        this.fSystemId = systemId;
    }

    @Override // org.w3c.dom.ls.LSInput
    public String getBaseURI() {
        return this.fBaseSystemId;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setBaseURI(String baseURI) {
        this.fBaseSystemId = baseURI;
    }

    @Override // org.w3c.dom.ls.LSInput
    public boolean getCertifiedText() {
        return this.fCertifiedText;
    }

    @Override // org.w3c.dom.ls.LSInput
    public void setCertifiedText(boolean certifiedText) {
        this.fCertifiedText = certifiedText;
    }
}
