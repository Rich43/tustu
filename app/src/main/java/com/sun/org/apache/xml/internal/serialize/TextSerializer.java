package com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import java.util.MissingResourceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/TextSerializer.class */
public class TextSerializer extends BaseMarkupSerializer {
    public TextSerializer() {
        super(new OutputFormat("text", (String) null, false));
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, com.sun.org.apache.xml.internal.serialize.Serializer
    public void setOutputFormat(OutputFormat format) throws MissingResourceException {
        super.setOutputFormat(format != null ? format : new OutputFormat("text", (String) null, false));
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String rawName, Attributes attrs) throws SAXException {
        startElement(rawName == null ? localName : rawName, null);
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
        endElement(rawName == null ? localName : rawName);
    }

    @Override // org.xml.sax.DocumentHandler
    public void startElement(String tagName, AttributeList attrs) throws SAXException {
        try {
            ElementState state = getElementState();
            if (isDocumentState() && !this._started) {
                startDocument(tagName);
            }
            boolean preserveSpace = state.preserveSpace;
            enterElementState(null, null, tagName, preserveSpace);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    @Override // org.xml.sax.DocumentHandler
    public void endElement(String tagName) throws SAXException {
        try {
            endElementIO(tagName);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    public void endElementIO(String tagName) throws IOException {
        getElementState();
        ElementState state = leaveElementState();
        state.afterElement = true;
        state.empty = false;
        if (isDocumentState()) {
            this._printer.flush();
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void processingInstructionIO(String target, String code) throws IOException {
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    public void comment(String text) {
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, org.xml.sax.ext.LexicalHandler
    public void comment(char[] chars, int start, int length) {
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer, org.xml.sax.ContentHandler
    public void characters(char[] chars, int start, int length) throws SAXException {
        try {
            ElementState state = content();
            state.inCData = false;
            state.doCData = false;
            printText(chars, start, length, true, true);
        } catch (IOException except) {
            throw new SAXException(except);
        }
    }

    protected void characters(String text, boolean unescaped) throws IOException {
        ElementState state = content();
        state.inCData = false;
        state.doCData = false;
        printText(text, true, true);
    }

    protected void startDocument(String rootTagName) throws IOException {
        this._printer.leaveDTD();
        this._started = true;
        serializePreRoot();
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void serializeElement(Element elem) throws DOMException, IOException {
        String tagName = elem.getTagName();
        ElementState state = getElementState();
        if (isDocumentState() && !this._started) {
            startDocument(tagName);
        }
        boolean preserveSpace = state.preserveSpace;
        if (elem.hasChildNodes()) {
            enterElementState(null, null, tagName, preserveSpace);
            Node firstChild = elem.getFirstChild();
            while (true) {
                Node child = firstChild;
                if (child != null) {
                    serializeNode(child);
                    firstChild = child.getNextSibling();
                } else {
                    endElementIO(tagName);
                    return;
                }
            }
        } else if (!isDocumentState()) {
            state.afterElement = true;
            state.empty = false;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected void serializeNode(Node node) throws DOMException, IOException {
        switch (node.getNodeType()) {
            case 1:
                serializeElement((Element) node);
                break;
            case 3:
                String text = node.getNodeValue();
                if (text != null) {
                    characters(node.getNodeValue(), true);
                    break;
                }
                break;
            case 4:
                String text2 = node.getNodeValue();
                if (text2 != null) {
                    characters(node.getNodeValue(), true);
                    break;
                }
                break;
            case 9:
            case 11:
                Node firstChild = node.getFirstChild();
                while (true) {
                    Node child = firstChild;
                    if (child == null) {
                        break;
                    } else {
                        serializeNode(child);
                        firstChild = child.getNextSibling();
                    }
                }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected ElementState content() {
        ElementState state = getElementState();
        if (!isDocumentState()) {
            if (state.empty) {
                state.empty = false;
            }
            state.afterElement = false;
        }
        return state;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.BaseMarkupSerializer
    protected String getEntityRef(int ch) {
        return null;
    }
}
