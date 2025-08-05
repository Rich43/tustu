package com.sun.org.apache.xalan.internal.xsltc.dom;

import com.sun.org.apache.xalan.internal.xsltc.trax.DOM2SAX;
import com.sun.org.apache.xalan.internal.xsltc.trax.StAXEvent2SAX;
import com.sun.org.apache.xalan.internal.xsltc.trax.StAXStream2SAX;
import com.sun.org.apache.xml.internal.dtm.DTM;
import com.sun.org.apache.xml.internal.dtm.DTMException;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.SystemIDResolver;
import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/dom/XSLTCDTMManager.class */
public class XSLTCDTMManager extends DTMManagerDefault {
    private static final boolean DUMPTREE = false;
    private static final boolean DEBUG = false;

    public static XSLTCDTMManager newInstance() {
        return new XSLTCDTMManager();
    }

    public static XSLTCDTMManager createNewDTMManagerInstance() {
        return newInstance();
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.DTMManagerDefault, com.sun.org.apache.xml.internal.dtm.DTMManager
    public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing) {
        return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, true, false);
    }

    public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean buildIdIndex) {
        return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, buildIdIndex, false);
    }

    public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean buildIdIndex, boolean newNameTable) {
        return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, false, 0, buildIdIndex, newNameTable);
    }

    public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean hasUserReader, int size, boolean buildIdIndex) {
        return getDTM(source, unique, whiteSpaceFilter, incremental, doIndexing, hasUserReader, size, buildIdIndex, false);
    }

    /* JADX WARN: Finally extract failed */
    public DTM getDTM(Source source, boolean unique, DTMWSFilter whiteSpaceFilter, boolean incremental, boolean doIndexing, boolean hasUserReader, int size, boolean buildIdIndex, boolean newNameTable) throws NullPointerException {
        XMLReader reader;
        InputSource xmlSource;
        SAXImpl dtm;
        SAXImpl dtm2;
        SAXImpl dtm3;
        int dtmPos = getFirstFreeDTMID();
        int documentID = dtmPos << 16;
        if (null != source && (source instanceof StAXSource)) {
            StAXSource staxSource = (StAXSource) source;
            StAXEvent2SAX staxevent2sax = null;
            StAXStream2SAX staxStream2SAX = null;
            if (staxSource.getXMLEventReader() != null) {
                XMLEventReader xmlEventReader = staxSource.getXMLEventReader();
                staxevent2sax = new StAXEvent2SAX(xmlEventReader);
            } else if (staxSource.getXMLStreamReader() != null) {
                XMLStreamReader xmlStreamReader = staxSource.getXMLStreamReader();
                staxStream2SAX = new StAXStream2SAX(xmlStreamReader);
            }
            if (size <= 0) {
                dtm3 = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, 512, buildIdIndex, newNameTable);
            } else {
                dtm3 = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, size, buildIdIndex, newNameTable);
            }
            dtm3.setDocumentURI(source.getSystemId());
            addDTM(dtm3, dtmPos, 0);
            try {
                if (staxevent2sax != null) {
                    staxevent2sax.setContentHandler(dtm3);
                    staxevent2sax.parse();
                } else {
                    if (staxStream2SAX != null) {
                        staxStream2SAX.setContentHandler(dtm3);
                        staxStream2SAX.parse();
                    }
                    return dtm3;
                }
                return dtm3;
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e2) {
                throw new WrappedRuntimeException(e2);
            }
        }
        if (null != source && (source instanceof DOMSource)) {
            DOMSource domsrc = (DOMSource) source;
            Node node = domsrc.getNode();
            DOM2SAX dom2sax = new DOM2SAX(node);
            if (size <= 0) {
                dtm2 = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, 512, buildIdIndex, newNameTable);
            } else {
                dtm2 = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, size, buildIdIndex, newNameTable);
            }
            dtm2.setDocumentURI(source.getSystemId());
            addDTM(dtm2, dtmPos, 0);
            dom2sax.setContentHandler(dtm2);
            try {
                dom2sax.parse();
                return dtm2;
            } catch (RuntimeException re2) {
                throw re2;
            } catch (Exception e3) {
                throw new WrappedRuntimeException(e3);
            }
        }
        boolean isSAXSource = null != source ? source instanceof SAXSource : true;
        boolean isStreamSource = null != source ? source instanceof StreamSource : false;
        if (isSAXSource || isStreamSource) {
            if (null == source) {
                xmlSource = null;
                reader = null;
                hasUserReader = false;
            } else {
                reader = getXMLReader(source);
                xmlSource = SAXSource.sourceToInputSource(source);
                String urlOfSource = xmlSource.getSystemId();
                if (null != urlOfSource) {
                    try {
                        urlOfSource = SystemIDResolver.getAbsoluteURI(urlOfSource);
                    } catch (Exception e4) {
                        System.err.println("Can not absolutize URL: " + urlOfSource);
                    }
                    xmlSource.setSystemId(urlOfSource);
                }
            }
            if (size <= 0) {
                dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, 512, buildIdIndex, newNameTable);
            } else {
                dtm = new SAXImpl(this, source, documentID, whiteSpaceFilter, null, doIndexing, size, buildIdIndex, newNameTable);
            }
            addDTM(dtm, dtmPos, 0);
            if (null == reader) {
                return dtm;
            }
            reader.setContentHandler(dtm.getBuilder());
            if (!hasUserReader || null == reader.getDTDHandler()) {
                reader.setDTDHandler(dtm);
            }
            if (!hasUserReader || null == reader.getErrorHandler()) {
                reader.setErrorHandler(dtm);
            }
            try {
                reader.setProperty("http://xml.org/sax/properties/lexical-handler", dtm);
            } catch (SAXNotRecognizedException e5) {
            } catch (SAXNotSupportedException e6) {
            }
            try {
                try {
                    reader.parse(xmlSource);
                    if (!hasUserReader) {
                        releaseXMLReader(reader);
                    }
                    return dtm;
                } catch (RuntimeException re3) {
                    throw re3;
                } catch (Exception e7) {
                    throw new WrappedRuntimeException(e7);
                }
            } catch (Throwable th) {
                if (!hasUserReader) {
                    releaseXMLReader(reader);
                }
                throw th;
            }
        }
        throw new DTMException(XMLMessages.createXMLMessage("ER_NOT_SUPPORTED", new Object[]{source}));
    }
}
