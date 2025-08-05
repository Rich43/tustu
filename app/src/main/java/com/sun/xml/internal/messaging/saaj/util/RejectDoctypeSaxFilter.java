package com.sun.xml.internal.messaging.saaj.util;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.EncryptionConstants;
import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.soap.SOAPException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/RejectDoctypeSaxFilter.class */
public class RejectDoctypeSaxFilter extends XMLFilterImpl implements XMLReader, LexicalHandler {
    static final String LEXICAL_HANDLER_PROP = "http://xml.org/sax/properties/lexical-handler";
    private LexicalHandler lexicalHandler;
    protected static final Logger log = Logger.getLogger(LogDomainConstants.UTIL_DOMAIN, "com.sun.xml.internal.messaging.saaj.util.LocalStrings");
    static final String WSU_NS = PolicyConstants.WSU_NAMESPACE_URI.intern();
    static final String SIGNATURE_LNAME = Constants._TAG_SIGNATURE.intern();
    static final String ENCRYPTED_DATA_LNAME = "EncryptedData".intern();
    static final String DSIG_NS = "http://www.w3.org/2000/09/xmldsig#".intern();
    static final String XENC_NS = EncryptionConstants.EncryptionSpecNS.intern();
    static final String ID_NAME = "ID".intern();

    public RejectDoctypeSaxFilter(SAXParser saxParser) throws SOAPException {
        try {
            XMLReader xmlReader = saxParser.getXMLReader();
            try {
                xmlReader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
                setParent(xmlReader);
            } catch (Exception e2) {
                log.severe("SAAJ0603.util.setProperty.exception");
                throw new SOAPExceptionImpl("Couldn't set the lexical handler property while constructing a RejectDoctypeSaxFilter", e2);
            }
        } catch (Exception e3) {
            log.severe("SAAJ0602.util.getXMLReader.exception");
            throw new SOAPExceptionImpl("Couldn't get an XMLReader while constructing a RejectDoctypeSaxFilter", e3);
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.XMLReader
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/properties/lexical-handler".equals(name)) {
            this.lexicalHandler = (LexicalHandler) value;
        } else {
            super.setProperty(name, value);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        throw new SAXException("Document Type Declaration is not allowed");
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startEntity(name);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endEntity(name);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.startCDATA();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.endCDATA();
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        if (this.lexicalHandler != null) {
            this.lexicalHandler.comment(ch, start, length);
        }
    }

    @Override // org.xml.sax.helpers.XMLFilterImpl, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (atts != null) {
            boolean eos = false;
            if (namespaceURI == DSIG_NS || XENC_NS == namespaceURI) {
                eos = true;
            }
            int length = atts.getLength();
            AttributesImpl attrImpl = new AttributesImpl();
            for (int i2 = 0; i2 < length; i2++) {
                String name = atts.getLocalName(i2);
                if (name != null && name.equals(Constants._ATT_ID)) {
                    if (eos || atts.getURI(i2) == WSU_NS) {
                        attrImpl.addAttribute(atts.getURI(i2), atts.getLocalName(i2), atts.getQName(i2), ID_NAME, atts.getValue(i2));
                    } else {
                        attrImpl.addAttribute(atts.getURI(i2), atts.getLocalName(i2), atts.getQName(i2), atts.getType(i2), atts.getValue(i2));
                    }
                } else {
                    attrImpl.addAttribute(atts.getURI(i2), atts.getLocalName(i2), atts.getQName(i2), atts.getType(i2), atts.getValue(i2));
                }
            }
            super.startElement(namespaceURI, localName, qName, attrImpl);
            return;
        }
        super.startElement(namespaceURI, localName, qName, null);
    }
}
