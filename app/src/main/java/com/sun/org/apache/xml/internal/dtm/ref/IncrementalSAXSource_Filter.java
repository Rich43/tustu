package com.sun.org.apache.xml.internal.dtm.ref;

import com.sun.org.apache.xml.internal.res.XMLMessages;
import com.sun.org.apache.xml.internal.utils.ThreadControllerWrapper;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/IncrementalSAXSource_Filter.class */
final class IncrementalSAXSource_Filter implements IncrementalSAXSource, ContentHandler, DTDHandler, LexicalHandler, ErrorHandler, Runnable {
    private int eventcounter;
    boolean DEBUG = false;
    private CoroutineManager fCoroutineManager = null;
    private int fControllerCoroutineID = -1;
    private int fSourceCoroutineID = -1;
    private ContentHandler clientContentHandler = null;
    private LexicalHandler clientLexicalHandler = null;
    private DTDHandler clientDTDHandler = null;
    private ErrorHandler clientErrorHandler = null;
    private int frequency = 5;
    private boolean fNoMoreEvents = false;
    private XMLReader fXMLReader = null;
    private InputSource fXMLReaderInputSource = null;

    public IncrementalSAXSource_Filter() {
        init(new CoroutineManager(), -1, -1);
    }

    public IncrementalSAXSource_Filter(CoroutineManager co, int controllerCoroutineID) {
        init(co, controllerCoroutineID, -1);
    }

    public static IncrementalSAXSource createIncrementalSAXSource(CoroutineManager co, int controllerCoroutineID) {
        return new IncrementalSAXSource_Filter(co, controllerCoroutineID);
    }

    public void init(CoroutineManager co, int controllerCoroutineID, int sourceCoroutineID) {
        if (co == null) {
            co = new CoroutineManager();
        }
        this.fCoroutineManager = co;
        this.fControllerCoroutineID = co.co_joinCoroutineSet(controllerCoroutineID);
        this.fSourceCoroutineID = co.co_joinCoroutineSet(sourceCoroutineID);
        if (this.fControllerCoroutineID == -1 || this.fSourceCoroutineID == -1) {
            throw new RuntimeException(XMLMessages.createXMLMessage("ER_COJOINROUTINESET_FAILED", null));
        }
        this.fNoMoreEvents = false;
        this.eventcounter = this.frequency;
    }

    public void setXMLReader(XMLReader eventsource) {
        this.fXMLReader = eventsource;
        eventsource.setContentHandler(this);
        eventsource.setDTDHandler(this);
        eventsource.setErrorHandler(this);
        try {
            eventsource.setProperty("http://xml.org/sax/properties/lexical-handler", this);
        } catch (SAXNotRecognizedException e2) {
        } catch (SAXNotSupportedException e3) {
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void setContentHandler(ContentHandler handler) {
        this.clientContentHandler = handler;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void setDTDHandler(DTDHandler handler) {
        this.clientDTDHandler = handler;
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void setLexicalHandler(LexicalHandler handler) {
        this.clientLexicalHandler = handler;
    }

    public void setErrHandler(ErrorHandler handler) {
        this.clientErrorHandler = handler;
    }

    public void setReturnFrequency(int events) {
        if (events < 1) {
            events = 1;
        }
        int i2 = events;
        this.eventcounter = i2;
        this.frequency = i2;
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.characters(ch, start, length);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        if (this.clientContentHandler != null) {
            this.clientContentHandler.endDocument();
        }
        this.eventcounter = 0;
        co_yield(false);
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.endElement(namespaceURI, localName, qName);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.endPrefixMapping(prefix);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.ignorableWhitespace(ch, start, length);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.processingInstruction(target, data);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.skippedEntity(name);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        co_entry_pause();
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.startDocument();
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.startElement(namespaceURI, localName, qName, atts);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
        if (this.clientContentHandler != null) {
            this.clientContentHandler.startPrefixMapping(prefix, uri);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.comment(ch, start, length);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.endCDATA();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.endDTD();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.endEntity(name);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.startCDATA();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.startDTD(name, publicId, systemId);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
        if (null != this.clientLexicalHandler) {
            this.clientLexicalHandler.startEntity(name);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String a2, String b2, String c2) throws SAXException {
        if (null != this.clientDTDHandler) {
            this.clientDTDHandler.notationDecl(a2, b2, c2);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String a2, String b2, String c2, String d2) throws SAXException {
        if (null != this.clientDTDHandler) {
            this.clientDTDHandler.unparsedEntityDecl(a2, b2, c2, d2);
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void error(SAXParseException exception) throws SAXException {
        if (null != this.clientErrorHandler) {
            this.clientErrorHandler.error(exception);
        }
    }

    @Override // org.xml.sax.ErrorHandler
    public void fatalError(SAXParseException exception) throws SAXException {
        if (null != this.clientErrorHandler) {
            this.clientErrorHandler.error(exception);
        }
        this.eventcounter = 0;
        co_yield(false);
    }

    @Override // org.xml.sax.ErrorHandler
    public void warning(SAXParseException exception) throws SAXException {
        if (null != this.clientErrorHandler) {
            this.clientErrorHandler.error(exception);
        }
    }

    public int getSourceCoroutineID() {
        return this.fSourceCoroutineID;
    }

    public int getControllerCoroutineID() {
        return this.fControllerCoroutineID;
    }

    public CoroutineManager getCoroutineManager() {
        return this.fCoroutineManager;
    }

    protected void count_and_yield(boolean moreExpected) throws SAXException {
        if (!moreExpected) {
            this.eventcounter = 0;
        }
        int i2 = this.eventcounter - 1;
        this.eventcounter = i2;
        if (i2 <= 0) {
            co_yield(true);
            this.eventcounter = this.frequency;
        }
    }

    private void co_entry_pause() throws SAXException {
        if (this.fCoroutineManager == null) {
            init(null, -1, -1);
        }
        try {
            Object arg = this.fCoroutineManager.co_entry_pause(this.fSourceCoroutineID);
            if (arg == Boolean.FALSE) {
                co_yield(false);
            }
        } catch (NoSuchMethodException e2) {
            if (this.DEBUG) {
                e2.printStackTrace();
            }
            throw new SAXException(e2);
        }
    }

    private void co_yield(boolean moreRemains) throws SAXException {
        if (this.fNoMoreEvents) {
            return;
        }
        try {
            Object arg = Boolean.FALSE;
            if (moreRemains) {
                arg = this.fCoroutineManager.co_resume(Boolean.TRUE, this.fSourceCoroutineID, this.fControllerCoroutineID);
            }
            if (arg == Boolean.FALSE) {
                this.fNoMoreEvents = true;
                if (this.fXMLReader != null) {
                    throw new StopException();
                }
                this.fCoroutineManager.co_exit_to(Boolean.FALSE, this.fSourceCoroutineID, this.fControllerCoroutineID);
            }
        } catch (NoSuchMethodException e2) {
            this.fNoMoreEvents = true;
            this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public void startParse(InputSource source) throws SAXException {
        if (this.fNoMoreEvents) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_INCRSAXSRCFILTER_NOT_RESTARTABLE", null));
        }
        if (this.fXMLReader == null) {
            throw new SAXException(XMLMessages.createXMLMessage("ER_XMLRDR_NOT_BEFORE_STARTPARSE", null));
        }
        this.fXMLReaderInputSource = source;
        ThreadControllerWrapper.runThread(this, -1);
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.fXMLReader == null) {
            return;
        }
        if (this.DEBUG) {
            System.out.println("IncrementalSAXSource_Filter parse thread launched");
        }
        Object arg = Boolean.FALSE;
        try {
            this.fXMLReader.parse(this.fXMLReaderInputSource);
        } catch (StopException e2) {
            if (this.DEBUG) {
                System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
            }
        } catch (IOException ex) {
            arg = ex;
        } catch (SAXException ex2) {
            Exception inner = ex2.getException();
            if (inner instanceof StopException) {
                if (this.DEBUG) {
                    System.out.println("Active IncrementalSAXSource_Filter normal stop exception");
                }
            } else {
                if (this.DEBUG) {
                    System.out.println("Active IncrementalSAXSource_Filter UNEXPECTED SAX exception: " + ((Object) inner));
                    inner.printStackTrace();
                }
                arg = ex2;
            }
        }
        this.fXMLReader = null;
        try {
            this.fNoMoreEvents = true;
            this.fCoroutineManager.co_exit_to(arg, this.fSourceCoroutineID, this.fControllerCoroutineID);
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace(System.err);
            this.fCoroutineManager.co_exit(this.fSourceCoroutineID);
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/IncrementalSAXSource_Filter$StopException.class */
    class StopException extends RuntimeException {
        static final long serialVersionUID = -1129245796185754956L;

        StopException() {
        }
    }

    @Override // com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource
    public Object deliverMoreNodes(boolean parsemore) {
        if (this.fNoMoreEvents) {
            return Boolean.FALSE;
        }
        try {
            Object result = this.fCoroutineManager.co_resume(parsemore ? Boolean.TRUE : Boolean.FALSE, this.fControllerCoroutineID, this.fSourceCoroutineID);
            if (result == Boolean.FALSE) {
                this.fCoroutineManager.co_exit(this.fControllerCoroutineID);
            }
            return result;
        } catch (NoSuchMethodException e2) {
            return e2;
        }
    }
}
