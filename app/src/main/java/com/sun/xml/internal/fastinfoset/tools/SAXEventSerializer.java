package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/SAXEventSerializer.class */
public class SAXEventSerializer extends DefaultHandler implements LexicalHandler {
    private Writer _writer;
    private StringBuffer _characters;
    protected List _namespaceAttributes;
    private Stack _namespaceStack = new Stack();
    private boolean _charactersAreCDATA = false;

    public SAXEventSerializer(OutputStream s2) throws IOException {
        this._writer = new OutputStreamWriter(s2);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        try {
            this._writer.write("<sax xmlns=\"http://www.sun.com/xml/sax-events\">\n");
            this._writer.write("<startDocument/>\n");
            this._writer.flush();
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        try {
            this._writer.write("<endDocument/>\n");
            this._writer.write("</sax>");
            this._writer.flush();
            this._writer.close();
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this._namespaceAttributes == null) {
            this._namespaceAttributes = new ArrayList();
        }
        String qName = prefix.length() == 0 ? "xmlns" : "xmlns" + prefix;
        AttributeValueHolder attribute = new AttributeValueHolder(qName, prefix, uri, null, null);
        this._namespaceAttributes.add(attribute);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            outputCharacters();
            if (this._namespaceAttributes != null) {
                AttributeValueHolder[] attrsHolder = (AttributeValueHolder[]) this._namespaceAttributes.toArray(new AttributeValueHolder[0]);
                quicksort(attrsHolder, 0, attrsHolder.length - 1);
                for (int i2 = 0; i2 < attrsHolder.length; i2++) {
                    this._writer.write("<startPrefixMapping prefix=\"" + attrsHolder[i2].localName + "\" uri=\"" + attrsHolder[i2].uri + "\"/>\n");
                    this._writer.flush();
                }
                this._namespaceStack.push(attrsHolder);
                this._namespaceAttributes = null;
            } else {
                this._namespaceStack.push(null);
            }
            AttributeValueHolder[] attrsHolder2 = new AttributeValueHolder[attributes.getLength()];
            for (int i3 = 0; i3 < attributes.getLength(); i3++) {
                attrsHolder2[i3] = new AttributeValueHolder(attributes.getQName(i3), attributes.getLocalName(i3), attributes.getURI(i3), attributes.getType(i3), attributes.getValue(i3));
            }
            quicksort(attrsHolder2, 0, attrsHolder2.length - 1);
            int attributeCount = 0;
            for (AttributeValueHolder attributeValueHolder : attrsHolder2) {
                if (!attributeValueHolder.uri.equals("http://www.w3.org/2000/xmlns/")) {
                    attributeCount++;
                }
            }
            if (attributeCount == 0) {
                this._writer.write("<startElement uri=\"" + uri + "\" localName=\"" + localName + "\" qName=\"" + qName + "\"/>\n");
                return;
            }
            this._writer.write("<startElement uri=\"" + uri + "\" localName=\"" + localName + "\" qName=\"" + qName + "\">\n");
            for (int i4 = 0; i4 < attrsHolder2.length; i4++) {
                if (!attrsHolder2[i4].uri.equals("http://www.w3.org/2000/xmlns/")) {
                    this._writer.write("  <attribute qName=\"" + attrsHolder2[i4].qName + "\" localName=\"" + attrsHolder2[i4].localName + "\" uri=\"" + attrsHolder2[i4].uri + "\" value=\"" + attrsHolder2[i4].value + "\"/>\n");
                }
            }
            this._writer.write("</startElement>\n");
            this._writer.flush();
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            outputCharacters();
            this._writer.write("<endElement uri=\"" + uri + "\" localName=\"" + localName + "\" qName=\"" + qName + "\"/>\n");
            this._writer.flush();
            AttributeValueHolder[] attrsHolder = (AttributeValueHolder[]) this._namespaceStack.pop();
            if (attrsHolder != null) {
                for (AttributeValueHolder attributeValueHolder : attrsHolder) {
                    this._writer.write("<endPrefixMapping prefix=\"" + attributeValueHolder.localName + "\"/>\n");
                    this._writer.flush();
                }
            }
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (length == 0) {
            return;
        }
        if (this._characters == null) {
            this._characters = new StringBuffer();
        }
        this._characters.append(ch, start, length);
    }

    private void outputCharacters() throws SAXException {
        if (this._characters == null) {
            return;
        }
        try {
            this._writer.write("<characters>" + (this._charactersAreCDATA ? "<![CDATA[" : "") + ((Object) this._characters) + (this._charactersAreCDATA ? "]]>" : "") + "</characters>\n");
            this._writer.flush();
            this._characters = null;
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            outputCharacters();
            this._writer.write("<processingInstruction target=\"" + target + "\" data=\"" + data + "\"/>\n");
            this._writer.flush();
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        this._charactersAreCDATA = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this._charactersAreCDATA = false;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        try {
            outputCharacters();
            this._writer.write("<comment>" + new String(ch, start, length) + "</comment>\n");
            this._writer.flush();
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    private void quicksort(AttributeValueHolder[] attrs, int p2, int r2) {
        while (p2 < r2) {
            int q2 = partition(attrs, p2, r2);
            quicksort(attrs, p2, q2);
            p2 = q2 + 1;
        }
    }

    private int partition(AttributeValueHolder[] attributeValueHolderArr, int p2, int r2) {
        AttributeValueHolder x2 = attributeValueHolderArr[(p2 + r2) >>> 1];
        int i2 = p2 - 1;
        int j2 = r2 + 1;
        while (true) {
            j2--;
            if (x2.compareTo(attributeValueHolderArr[j2]) >= 0) {
                do {
                    i2++;
                } while (x2.compareTo(attributeValueHolderArr[i2]) > 0);
                if (i2 < j2) {
                    AttributeValueHolder t2 = attributeValueHolderArr[i2];
                    attributeValueHolderArr[i2] = attributeValueHolderArr[j2];
                    attributeValueHolderArr[j2] = t2;
                } else {
                    return j2;
                }
            }
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/SAXEventSerializer$AttributeValueHolder.class */
    public static class AttributeValueHolder implements Comparable {
        public final String qName;
        public final String localName;
        public final String uri;
        public final String type;
        public final String value;

        public AttributeValueHolder(String qName, String localName, String uri, String type, String value) {
            this.qName = qName;
            this.localName = localName;
            this.uri = uri;
            this.type = type;
            this.value = value;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object o2) {
            try {
                return this.qName.compareTo(((AttributeValueHolder) o2).qName);
            } catch (Exception e2) {
                throw new RuntimeException(CommonResourceBundle.getInstance().getString("message.AttributeValueHolderExpected"));
            }
        }

        public boolean equals(Object o2) {
            try {
                if (o2 instanceof AttributeValueHolder) {
                    if (this.qName.equals(((AttributeValueHolder) o2).qName)) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e2) {
                throw new RuntimeException(CommonResourceBundle.getInstance().getString("message.AttributeValueHolderExpected"));
            }
        }

        public int hashCode() {
            int hash = (97 * 7) + (this.qName != null ? this.qName.hashCode() : 0);
            return hash;
        }
    }
}
