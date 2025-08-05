package com.sun.xml.internal.fastinfoset.tools;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.CharArray;
import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import com.sun.xml.internal.fastinfoset.util.QualifiedNameArray;
import com.sun.xml.internal.fastinfoset.util.StringArray;
import com.sun.xml.internal.fastinfoset.util.StringIntMap;
import com.sun.xml.internal.fastinfoset.vocab.ParserVocabulary;
import com.sun.xml.internal.fastinfoset.vocab.SerializerVocabulary;
import com.sun.xml.internal.org.jvnet.fastinfoset.Vocabulary;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/tools/VocabularyGenerator.class */
public class VocabularyGenerator extends DefaultHandler implements LexicalHandler {
    protected SerializerVocabulary _serializerVocabulary;
    protected ParserVocabulary _parserVocabulary;
    protected Vocabulary _v;
    protected int attributeValueSizeConstraint;
    protected int characterContentChunkSizeContraint;

    public VocabularyGenerator() {
        this.attributeValueSizeConstraint = 32;
        this.characterContentChunkSizeContraint = 32;
        this._serializerVocabulary = new SerializerVocabulary();
        this._parserVocabulary = new ParserVocabulary();
        this._v = new Vocabulary();
    }

    public VocabularyGenerator(SerializerVocabulary serializerVocabulary) {
        this.attributeValueSizeConstraint = 32;
        this.characterContentChunkSizeContraint = 32;
        this._serializerVocabulary = serializerVocabulary;
        this._parserVocabulary = new ParserVocabulary();
        this._v = new Vocabulary();
    }

    public VocabularyGenerator(ParserVocabulary parserVocabulary) {
        this.attributeValueSizeConstraint = 32;
        this.characterContentChunkSizeContraint = 32;
        this._serializerVocabulary = new SerializerVocabulary();
        this._parserVocabulary = parserVocabulary;
        this._v = new Vocabulary();
    }

    public VocabularyGenerator(SerializerVocabulary serializerVocabulary, ParserVocabulary parserVocabulary) {
        this.attributeValueSizeConstraint = 32;
        this.characterContentChunkSizeContraint = 32;
        this._serializerVocabulary = serializerVocabulary;
        this._parserVocabulary = parserVocabulary;
        this._v = new Vocabulary();
    }

    public Vocabulary getVocabulary() {
        return this._v;
    }

    public void setCharacterContentChunkSizeLimit(int size) {
        if (size < 0) {
            size = 0;
        }
        this.characterContentChunkSizeContraint = size;
    }

    public int getCharacterContentChunkSizeLimit() {
        return this.characterContentChunkSizeContraint;
    }

    public void setAttributeValueSizeLimit(int size) {
        if (size < 0) {
            size = 0;
        }
        this.attributeValueSizeConstraint = size;
    }

    public int getAttributeValueSizeLimit() {
        return this.attributeValueSizeConstraint;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        addToTable(prefix, this._v.prefixes, this._serializerVocabulary.prefix, this._parserVocabulary.prefix);
        addToTable(uri, this._v.namespaceNames, this._serializerVocabulary.namespaceName, this._parserVocabulary.namespaceName);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        addToNameTable(namespaceURI, qName, localName, this._v.elements, this._serializerVocabulary.elementName, this._parserVocabulary.elementName, false);
        for (int a2 = 0; a2 < atts.getLength(); a2++) {
            addToNameTable(atts.getURI(a2), atts.getQName(a2), atts.getLocalName(a2), this._v.attributes, this._serializerVocabulary.attributeName, this._parserVocabulary.attributeName, true);
            String value = atts.getValue(a2);
            if (value.length() < this.attributeValueSizeConstraint) {
                addToTable(value, this._v.attributeValues, this._serializerVocabulary.attributeValue, this._parserVocabulary.attributeValue);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (length < this.characterContentChunkSizeContraint) {
            addToCharArrayTable(new CharArray(ch, start, length, true));
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
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

    public void addToTable(String s2, Set v2, StringIntMap m2, StringArray a2) {
        if (s2.length() == 0) {
            return;
        }
        if (m2.obtainIndex(s2) == -1) {
            a2.add(s2);
        }
        v2.add(s2);
    }

    public void addToTable(String s2, Set v2, StringIntMap m2, PrefixArray a2) {
        if (s2.length() == 0) {
            return;
        }
        if (m2.obtainIndex(s2) == -1) {
            a2.add(s2);
        }
        v2.add(s2);
    }

    public void addToCharArrayTable(CharArray c2) {
        if (this._serializerVocabulary.characterContentChunk.obtainIndex(c2.ch, c2.start, c2.length, false) == -1) {
            this._parserVocabulary.characterContentChunk.add(c2.ch, c2.length);
        }
        this._v.characterContentChunks.add(c2.toString());
    }

    public void addToNameTable(String namespaceURI, String qName, String localName, Set v2, LocalNameQualifiedNamesMap m2, QualifiedNameArray a2, boolean isAttribute) throws SAXException {
        LocalNameQualifiedNamesMap.Entry entry = m2.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i2 = 0; i2 < entry._valueIndex; i2++) {
                if (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName)) {
                    return;
                }
            }
        }
        String prefix = getPrefixFromQualifiedName(qName);
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (namespaceURI.length() > 0) {
            namespaceURIIndex = this._serializerVocabulary.namespaceName.get(namespaceURI);
            if (namespaceURIIndex == -1) {
                throw new SAXException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[]{Integer.valueOf(namespaceURIIndex)}));
            }
            if (prefix.length() > 0) {
                prefixIndex = this._serializerVocabulary.prefix.get(prefix);
                if (prefixIndex == -1) {
                    throw new SAXException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[]{Integer.valueOf(prefixIndex)}));
                }
            }
        }
        int localNameIndex = this._serializerVocabulary.localName.obtainIndex(localName);
        if (localNameIndex == -1) {
            this._parserVocabulary.localName.add(localName);
            localNameIndex = this._parserVocabulary.localName.getSize() - 1;
        }
        QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, m2.getNextIndex(), prefixIndex, namespaceURIIndex, localNameIndex);
        if (isAttribute) {
            name.createAttributeValues(256);
        }
        entry.addQualifiedName(name);
        a2.add(name);
        v2.add(name.getQName());
    }

    public static String getPrefixFromQualifiedName(String qName) {
        int i2 = qName.indexOf(58);
        String prefix = "";
        if (i2 != -1) {
            prefix = qName.substring(0, i2);
        }
        return prefix;
    }
}
