package com.sun.istack.internal;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/istack/internal/XMLStreamReaderToContentHandler.class */
public class XMLStreamReaderToContentHandler {
    private final XMLStreamReader staxStreamReader;
    private final ContentHandler saxHandler;
    private final boolean eagerQuit;
    private final boolean fragment;
    private final String[] inscopeNamespaces;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !XMLStreamReaderToContentHandler.class.desiredAssertionStatus();
    }

    public XMLStreamReaderToContentHandler(XMLStreamReader staxCore, ContentHandler saxCore, boolean eagerQuit, boolean fragment) {
        this(staxCore, saxCore, eagerQuit, fragment, new String[0]);
    }

    public XMLStreamReaderToContentHandler(XMLStreamReader staxCore, ContentHandler saxCore, boolean eagerQuit, boolean fragment, String[] inscopeNamespaces) {
        this.staxStreamReader = staxCore;
        this.saxHandler = saxCore;
        this.eagerQuit = eagerQuit;
        this.fragment = fragment;
        this.inscopeNamespaces = inscopeNamespaces;
        if (!$assertionsDisabled && inscopeNamespaces.length % 2 != 0) {
            throw new AssertionError();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:41:0x0164 A[Catch: SAXException -> 0x0180, LOOP:3: B:39:0x015b->B:41:0x0164, LOOP_END, TryCatch #0 {SAXException -> 0x0180, blocks: (B:2:0x0000, B:4:0x0012, B:6:0x001e, B:9:0x0030, B:10:0x004a, B:11:0x004b, B:12:0x0051, B:14:0x005a, B:16:0x0078, B:17:0x00c4, B:36:0x014b, B:39:0x015b, B:41:0x0164, B:42:0x0179, B:18:0x00ce, B:20:0x00d9, B:23:0x00e3, B:24:0x00ea, B:25:0x00f1, B:26:0x00f8, B:27:0x00ff, B:28:0x0106, B:29:0x010d, B:30:0x0114, B:31:0x011b, B:32:0x0122, B:33:0x0129, B:34:0x0130, B:35:0x014a), top: B:47:0x0000 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void bridge() throws javax.xml.stream.XMLStreamException {
        /*
            Method dump skipped, instructions count: 395
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.istack.internal.XMLStreamReaderToContentHandler.bridge():void");
    }

    private void handleEndDocument() throws SAXException {
        if (this.fragment) {
            return;
        }
        this.saxHandler.endDocument();
    }

    private void handleStartDocument() throws SAXException {
        if (this.fragment) {
            return;
        }
        this.saxHandler.setDocumentLocator(new Locator() { // from class: com.sun.istack.internal.XMLStreamReaderToContentHandler.1
            @Override // org.xml.sax.Locator
            public int getColumnNumber() {
                return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getColumnNumber();
            }

            @Override // org.xml.sax.Locator
            public int getLineNumber() {
                return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getLineNumber();
            }

            @Override // org.xml.sax.Locator
            public String getPublicId() {
                return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getPublicId();
            }

            @Override // org.xml.sax.Locator
            public String getSystemId() {
                return XMLStreamReaderToContentHandler.this.staxStreamReader.getLocation().getSystemId();
            }
        });
        this.saxHandler.startDocument();
    }

    private void handlePI() throws XMLStreamException {
        try {
            this.saxHandler.processingInstruction(this.staxStreamReader.getPITarget(), this.staxStreamReader.getPIData());
        } catch (SAXException e2) {
            throw new XMLStreamException2(e2);
        }
    }

    private void handleCharacters() throws XMLStreamException {
        try {
            this.saxHandler.characters(this.staxStreamReader.getTextCharacters(), this.staxStreamReader.getTextStart(), this.staxStreamReader.getTextLength());
        } catch (SAXException e2) {
            throw new XMLStreamException2(e2);
        }
    }

    private void handleEndElement() throws XMLStreamException {
        String localPart;
        QName qName = this.staxStreamReader.getName();
        try {
            String pfix = qName.getPrefix();
            if (pfix == null || pfix.length() == 0) {
                localPart = qName.getLocalPart();
            } else {
                localPart = pfix + ':' + qName.getLocalPart();
            }
            String rawname = localPart;
            this.saxHandler.endElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname);
            int nsCount = this.staxStreamReader.getNamespaceCount();
            for (int i2 = nsCount - 1; i2 >= 0; i2--) {
                String prefix = this.staxStreamReader.getNamespacePrefix(i2);
                if (prefix == null) {
                    prefix = "";
                }
                this.saxHandler.endPrefixMapping(prefix);
            }
        } catch (SAXException e2) {
            throw new XMLStreamException2(e2);
        }
    }

    private void handleStartElement() throws XMLStreamException {
        String rawname;
        try {
            int nsCount = this.staxStreamReader.getNamespaceCount();
            for (int i2 = 0; i2 < nsCount; i2++) {
                this.saxHandler.startPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i2)), fixNull(this.staxStreamReader.getNamespaceURI(i2)));
            }
            QName qName = this.staxStreamReader.getName();
            String prefix = qName.getPrefix();
            if (prefix == null || prefix.length() == 0) {
                rawname = qName.getLocalPart();
            } else {
                rawname = prefix + ':' + qName.getLocalPart();
            }
            Attributes attrs = getAttributes();
            this.saxHandler.startElement(qName.getNamespaceURI(), qName.getLocalPart(), rawname, attrs);
        } catch (SAXException e2) {
            throw new XMLStreamException2(e2);
        }
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    private Attributes getAttributes() {
        String str;
        AttributesImpl attrs = new AttributesImpl();
        int eventType = this.staxStreamReader.getEventType();
        if (eventType != 10 && eventType != 1) {
            throw new InternalError("getAttributes() attempting to process: " + eventType);
        }
        for (int i2 = 0; i2 < this.staxStreamReader.getAttributeCount(); i2++) {
            String uri = this.staxStreamReader.getAttributeNamespace(i2);
            if (uri == null) {
                uri = "";
            }
            String localName = this.staxStreamReader.getAttributeLocalName(i2);
            String prefix = this.staxStreamReader.getAttributePrefix(i2);
            if (prefix == null || prefix.length() == 0) {
                str = localName;
            } else {
                str = prefix + ':' + localName;
            }
            String qName = str;
            String type = this.staxStreamReader.getAttributeType(i2);
            String value = this.staxStreamReader.getAttributeValue(i2);
            attrs.addAttribute(uri, localName, qName, type, value);
        }
        return attrs;
    }

    private void handleNamespace() {
    }

    private void handleAttribute() {
    }

    private void handleDTD() {
    }

    private void handleComment() {
    }

    private void handleEntityReference() {
    }

    private void handleSpace() {
    }

    private void handleNotationDecl() {
    }

    private void handleEntityDecl() {
    }

    private void handleCDATA() {
    }
}
