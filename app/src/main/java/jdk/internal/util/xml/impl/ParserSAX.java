package jdk.internal.util.xml.impl;

import java.io.IOException;
import java.io.InputStream;
import jdk.internal.org.xml.sax.ContentHandler;
import jdk.internal.org.xml.sax.DTDHandler;
import jdk.internal.org.xml.sax.EntityResolver;
import jdk.internal.org.xml.sax.ErrorHandler;
import jdk.internal.org.xml.sax.InputSource;
import jdk.internal.org.xml.sax.Locator;
import jdk.internal.org.xml.sax.SAXException;
import jdk.internal.org.xml.sax.SAXParseException;
import jdk.internal.org.xml.sax.XMLReader;
import jdk.internal.org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/ParserSAX.class */
final class ParserSAX extends Parser implements XMLReader, Locator {
    public static final String FEATURE_NS = "http://xml.org/sax/features/namespaces";
    public static final String FEATURE_PREF = "http://xml.org/sax/features/namespace-prefixes";
    private boolean mFNamespaces = true;
    private boolean mFPrefixes = false;
    private DefaultHandler mHand = new DefaultHandler();
    private ContentHandler mHandCont = this.mHand;
    private DTDHandler mHandDtd = this.mHand;
    private ErrorHandler mHandErr = this.mHand;
    private EntityResolver mHandEnt = this.mHand;

    @Override // jdk.internal.org.xml.sax.XMLReader
    public ContentHandler getContentHandler() {
        if (this.mHandCont != this.mHand) {
            return this.mHandCont;
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void setContentHandler(ContentHandler contentHandler) {
        if (contentHandler == null) {
            throw new NullPointerException();
        }
        this.mHandCont = contentHandler;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public DTDHandler getDTDHandler() {
        if (this.mHandDtd != this.mHand) {
            return this.mHandDtd;
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void setDTDHandler(DTDHandler dTDHandler) {
        if (dTDHandler == null) {
            throw new NullPointerException();
        }
        this.mHandDtd = dTDHandler;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public ErrorHandler getErrorHandler() {
        if (this.mHandErr != this.mHand) {
            return this.mHandErr;
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void setErrorHandler(ErrorHandler errorHandler) {
        if (errorHandler == null) {
            throw new NullPointerException();
        }
        this.mHandErr = errorHandler;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public EntityResolver getEntityResolver() {
        if (this.mHandEnt != this.mHand) {
            return this.mHandEnt;
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void setEntityResolver(EntityResolver entityResolver) {
        if (entityResolver == null) {
            throw new NullPointerException();
        }
        this.mHandEnt = entityResolver;
    }

    @Override // jdk.internal.org.xml.sax.Locator
    public String getPublicId() {
        if (this.mInp != null) {
            return this.mInp.pubid;
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.Locator
    public String getSystemId() {
        if (this.mInp != null) {
            return this.mInp.sysid;
        }
        return null;
    }

    @Override // jdk.internal.org.xml.sax.Locator
    public int getLineNumber() {
        return -1;
    }

    @Override // jdk.internal.org.xml.sax.Locator
    public int getColumnNumber() {
        return -1;
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void parse(String str) throws IOException, SAXException {
        parse(new InputSource(str));
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void parse(InputSource inputSource) throws IOException, SAXException {
        if (inputSource == null) {
            throw new IllegalArgumentException("");
        }
        this.mInp = new Input(512);
        this.mPh = -1;
        try {
            setinp(inputSource);
        } catch (IOException e2) {
            throw e2;
        } catch (RuntimeException e3) {
            throw e3;
        } catch (SAXException e4) {
            throw e4;
        } catch (Exception e5) {
            panic(e5.toString());
        }
        parse();
    }

    public void parse(InputStream inputStream, DefaultHandler defaultHandler) throws IOException, SAXException {
        if (inputStream == null || defaultHandler == null) {
            throw new IllegalArgumentException("");
        }
        parse(new InputSource(inputStream), defaultHandler);
    }

    public void parse(InputSource inputSource, DefaultHandler defaultHandler) throws IOException, SAXException {
        if (inputSource == null || defaultHandler == null) {
            throw new IllegalArgumentException("");
        }
        this.mHandCont = defaultHandler;
        this.mHandDtd = defaultHandler;
        this.mHandErr = defaultHandler;
        this.mHandEnt = defaultHandler;
        this.mInp = new Input(512);
        this.mPh = -1;
        try {
            setinp(inputSource);
        } catch (IOException | RuntimeException | SAXException e2) {
            throw e2;
        } catch (Exception e3) {
            panic(e3.toString());
        }
        parse();
    }

    private void parse() throws IOException, SAXException {
        int iStep;
        init();
        try {
            try {
                try {
                    try {
                        this.mHandCont.setDocumentLocator(this);
                        this.mHandCont.startDocument();
                        if (this.mPh != 1) {
                            this.mPh = 1;
                        }
                        do {
                            wsskip();
                            int iStep2 = step();
                            iStep = iStep2;
                            switch (iStep2) {
                                case 1:
                                case 2:
                                    this.mPh = 4;
                                    break;
                                case 3:
                                case 4:
                                case 5:
                                case 7:
                                default:
                                    panic("");
                                    break;
                                case 6:
                                case 8:
                                    break;
                                case 9:
                                    if (this.mPh >= 3) {
                                        panic("");
                                    }
                                    this.mPh = 3;
                                    break;
                            }
                        } while (this.mPh < 4);
                        do {
                            switch (iStep) {
                                case 1:
                                case 2:
                                    if (this.mIsNSAware) {
                                        this.mHandCont.startElement(this.mElm.value, this.mElm.name, "", this.mAttrs);
                                    } else {
                                        this.mHandCont.startElement("", "", this.mElm.name, this.mAttrs);
                                    }
                                    if (iStep == 2) {
                                        iStep = step();
                                    }
                                    break;
                                case 3:
                                    break;
                                case 4:
                                case 5:
                                case 6:
                                case 7:
                                case 8:
                                case 10:
                                    iStep = step();
                                    break;
                                case 9:
                                default:
                                    panic("");
                                    break;
                            }
                            if (this.mIsNSAware) {
                                this.mHandCont.endElement(this.mElm.value, this.mElm.name, "");
                            } else {
                                this.mHandCont.endElement("", "", this.mElm.name);
                            }
                            while (this.mPref.list == this.mElm) {
                                this.mHandCont.endPrefixMapping(this.mPref.name);
                                this.mPref = del(this.mPref);
                            }
                            this.mElm = del(this.mElm);
                            if (this.mElm == null) {
                                this.mPh = 5;
                            } else {
                                iStep = step();
                            }
                        } while (this.mPh == 4);
                        while (wsskip() != 65535) {
                            switch (step()) {
                                case 6:
                                case 8:
                                    break;
                                default:
                                    panic("");
                                    break;
                            }
                            if (this.mPh != 5) {
                                this.mPh = 6;
                                this.mHandCont.endDocument();
                                cleanup();
                            }
                        }
                        this.mPh = 6;
                        this.mHandCont.endDocument();
                        cleanup();
                    } catch (IOException e2) {
                        throw e2;
                    } catch (Exception e3) {
                        panic(e3.toString());
                        this.mHandCont.endDocument();
                        cleanup();
                    }
                } catch (SAXException e4) {
                    throw e4;
                }
            } catch (RuntimeException e5) {
                throw e5;
            }
        } catch (Throwable th) {
            this.mHandCont.endDocument();
            cleanup();
            throw th;
        }
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void docType(String str, String str2, String str3) throws SAXException {
        this.mHandDtd.notationDecl(str, str2, str3);
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void comm(char[] cArr, int i2) {
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void pi(String str, String str2) throws SAXException {
        this.mHandCont.processingInstruction(str, str2);
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void newPrefix() throws SAXException {
        this.mHandCont.startPrefixMapping(this.mPref.name, this.mPref.value);
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void skippedEnt(String str) throws SAXException {
        this.mHandCont.skippedEntity(str);
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected InputSource resolveEnt(String str, String str2, String str3) throws IOException, SAXException {
        return this.mHandEnt.resolveEntity(str2, str3);
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void notDecl(String str, String str2, String str3) throws SAXException {
        this.mHandDtd.notationDecl(str, str2, str3);
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void unparsedEntDecl(String str, String str2, String str3, String str4) throws SAXException {
        this.mHandDtd.unparsedEntityDecl(str, str2, str3, str4);
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void panic(String str) throws SAXException {
        SAXParseException sAXParseException = new SAXParseException(str, this);
        this.mHandErr.fatalError(sAXParseException);
        throw sAXParseException;
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void bflash() throws SAXException {
        if (this.mBuffIdx >= 0) {
            this.mHandCont.characters(this.mBuff, 0, this.mBuffIdx + 1);
            this.mBuffIdx = -1;
        }
    }

    @Override // jdk.internal.util.xml.impl.Parser
    protected void bflash_ws() throws SAXException {
        if (this.mBuffIdx >= 0) {
            this.mHandCont.characters(this.mBuff, 0, this.mBuffIdx + 1);
            this.mBuffIdx = -1;
        }
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public boolean getFeature(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void setFeature(String str, boolean z2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public Object getProperty(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // jdk.internal.org.xml.sax.XMLReader
    public void setProperty(String str, Object obj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
