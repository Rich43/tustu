package com.sun.org.apache.xalan.internal.xsltc.trax;

import com.sun.org.apache.xalan.internal.xsltc.StripFilter;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.dom.DOMWSFilter;
import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl;
import com.sun.org.apache.xalan.internal.xsltc.dom.XSLTCDTMManager;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMWSFilter;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.TransformerHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/trax/TransformerHandlerImpl.class */
public class TransformerHandlerImpl implements TransformerHandler, DeclHandler {
    private TransformerImpl _transformer;
    private AbstractTranslet _translet;
    private String _systemId;
    private ContentHandler _handler;
    private boolean _isIdentity;
    private SAXImpl _dom = null;
    private LexicalHandler _lexHandler = null;
    private DTDHandler _dtdHandler = null;
    private DeclHandler _declHandler = null;
    private Result _result = null;
    private Locator _locator = null;
    private boolean _done = false;

    public TransformerHandlerImpl(TransformerImpl transformer) {
        this._translet = null;
        this._handler = null;
        this._isIdentity = false;
        this._transformer = transformer;
        if (transformer.isIdentity()) {
            this._handler = new DefaultHandler();
            this._isIdentity = true;
        } else {
            this._translet = this._transformer.getTranslet();
        }
    }

    @Override // javax.xml.transform.sax.TransformerHandler
    public String getSystemId() {
        return this._systemId;
    }

    @Override // javax.xml.transform.sax.TransformerHandler
    public void setSystemId(String id) {
        this._systemId = id;
    }

    @Override // javax.xml.transform.sax.TransformerHandler
    public Transformer getTransformer() {
        return this._transformer;
    }

    @Override // javax.xml.transform.sax.TransformerHandler
    public void setResult(Result result) throws IllegalArgumentException {
        this._result = result;
        if (null == result) {
            ErrorMsg err = new ErrorMsg("ER_RESULT_NULL");
            throw new IllegalArgumentException(err.toString());
        }
        if (this._isIdentity) {
            try {
                SerializationHandler outputHandler = this._transformer.getOutputHandler(result);
                this._transformer.transferOutputProperties(outputHandler);
                this._handler = outputHandler;
                this._lexHandler = outputHandler;
                return;
            } catch (TransformerException e2) {
                this._result = null;
                return;
            }
        }
        if (this._done) {
            try {
                this._transformer.setDOM(this._dom);
                this._transformer.transform(null, this._result);
            } catch (TransformerException e3) {
                throw new IllegalArgumentException(e3.getMessage());
            }
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        this._handler.characters(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        DTMWSFilter wsFilter;
        if (this._result == null) {
            ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_SET_RESULT_ERR);
            throw new SAXException(err.toString());
        }
        if (!this._isIdentity) {
            boolean hasIdCall = this._translet != null ? this._translet.hasIdCall() : false;
            try {
                XSLTCDTMManager dtmManager = this._transformer.getTransformerFactory().createNewDTMManagerInstance();
                if (this._translet != null && (this._translet instanceof StripFilter)) {
                    wsFilter = new DOMWSFilter(this._translet);
                } else {
                    wsFilter = null;
                }
                this._dom = (SAXImpl) dtmManager.getDTM(null, false, wsFilter, true, false, hasIdCall);
                this._handler = this._dom.getBuilder();
                this._lexHandler = (LexicalHandler) this._handler;
                this._dtdHandler = (DTDHandler) this._handler;
                this._declHandler = (DeclHandler) this._handler;
                this._dom.setDocumentURI(this._systemId);
                if (this._locator != null) {
                    this._handler.setDocumentLocator(this._locator);
                }
            } catch (Exception e2) {
                throw new SAXException(e2);
            }
        }
        this._handler.startDocument();
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        this._handler.endDocument();
        if (!this._isIdentity) {
            if (this._result != null) {
                try {
                    this._transformer.setDOM(this._dom);
                    this._transformer.transform(null, this._result);
                } catch (TransformerException e2) {
                    throw new SAXException(e2);
                }
            }
            this._done = true;
            this._transformer.setDOM(this._dom);
        }
        if (this._isIdentity && (this._result instanceof DOMResult)) {
            ((DOMResult) this._result).setNode(this._transformer.getTransletOutputHandlerFactory().getNode());
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qname, Attributes attributes) throws SAXException {
        this._handler.startElement(uri, localName, qname, attributes);
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qname) throws SAXException {
        this._handler.endElement(namespaceURI, localName, qname);
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        this._handler.processingInstruction(target, data);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.startCDATA();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.endCDATA();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.comment(ch, start, length);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        this._handler.ignorableWhitespace(ch, start, length);
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
        this._locator = locator;
        if (this._handler != null) {
            this._handler.setDocumentLocator(locator);
        }
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
        this._handler.skippedEntity(name);
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        this._handler.startPrefixMapping(prefix, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
        this._handler.endPrefixMapping(prefix);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.startDTD(name, publicId, systemId);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.endDTD();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.startEntity(name);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
        if (this._lexHandler != null) {
            this._lexHandler.endEntity(name);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
        if (this._dtdHandler != null) {
            this._dtdHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
        }
    }

    @Override // org.xml.sax.DTDHandler
    public void notationDecl(String name, String publicId, String systemId) throws SAXException {
        if (this._dtdHandler != null) {
            this._dtdHandler.notationDecl(name, publicId, systemId);
        }
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.attributeDecl(eName, aName, type, valueDefault, value);
        }
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void elementDecl(String name, String model) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.elementDecl(name, model);
        }
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.externalEntityDecl(name, publicId, systemId);
        }
    }

    @Override // org.xml.sax.ext.DeclHandler
    public void internalEntityDecl(String name, String value) throws SAXException {
        if (this._declHandler != null) {
            this._declHandler.internalEntityDecl(name, value);
        }
    }

    public void reset() {
        this._systemId = null;
        this._dom = null;
        this._handler = null;
        this._lexHandler = null;
        this._dtdHandler = null;
        this._declHandler = null;
        this._result = null;
        this._locator = null;
    }
}
