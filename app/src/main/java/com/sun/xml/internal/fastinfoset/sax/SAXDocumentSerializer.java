package com.sun.xml.internal.fastinfoset.sax;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.fastinfoset.Encoder;
import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import com.sun.xml.internal.org.jvnet.fastinfoset.RestrictedAlphabet;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.FastInfosetWriter;
import java.io.IOException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/sax/SAXDocumentSerializer.class */
public class SAXDocumentSerializer extends Encoder implements FastInfosetWriter {
    protected boolean _elementHasNamespaces;
    protected boolean _charactersAsCDATA;

    protected SAXDocumentSerializer(boolean v2) {
        super(v2);
        this._elementHasNamespaces = false;
        this._charactersAsCDATA = false;
    }

    public SAXDocumentSerializer() {
        this._elementHasNamespaces = false;
        this._charactersAsCDATA = false;
    }

    @Override // com.sun.xml.internal.fastinfoset.Encoder, com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetSerializer
    public void reset() {
        super.reset();
        this._elementHasNamespaces = false;
        this._charactersAsCDATA = false;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void startDocument() throws SAXException {
        try {
            reset();
            encodeHeader(false);
            encodeInitialVocabulary();
        } catch (IOException e2) {
            throw new SAXException("startDocument", e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void endDocument() throws SAXException {
        try {
            encodeDocumentTermination();
        } catch (IOException e2) {
            throw new SAXException("endDocument", e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        try {
            if (!this._elementHasNamespaces) {
                encodeTermination();
                mark();
                this._elementHasNamespaces = true;
                write(56);
            }
            encodeNamespaceAttribute(prefix, uri);
        } catch (IOException e2) {
            throw new SAXException("startElement", e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        int attributeCount = (atts == null || atts.getLength() <= 0) ? 0 : countAttributes(atts);
        try {
            if (this._elementHasNamespaces) {
                this._elementHasNamespaces = false;
                if (attributeCount > 0) {
                    byte[] bArr = this._octetBuffer;
                    int i2 = this._markIndex;
                    bArr[i2] = (byte) (bArr[i2] | 64);
                }
                resetMark();
                write(240);
                this._b = 0;
            } else {
                encodeTermination();
                this._b = 0;
                if (attributeCount > 0) {
                    this._b |= 64;
                }
            }
            encodeElement(namespaceURI, qName, localName);
            if (attributeCount > 0) {
                encodeAttributes(atts);
            }
        } catch (FastInfosetException e2) {
            throw new SAXException("startElement", e2);
        } catch (IOException e3) {
            throw new SAXException("startElement", e3);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            encodeElementTermination();
        } catch (IOException e2) {
            throw new SAXException("endElement", e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void characters(char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, start, length)) {
            return;
        }
        try {
            encodeTermination();
            if (!this._charactersAsCDATA) {
                encodeCharacters(ch, start, length);
            } else {
                encodeCIIBuiltInAlgorithmDataAsCDATA(ch, start, length);
            }
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (getIgnoreWhiteSpaceTextContent()) {
            return;
        }
        characters(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void processingInstruction(String target, String data) throws SAXException {
        try {
            if (getIgnoreProcesingInstructions()) {
                return;
            }
            if (target.length() == 0) {
                throw new SAXException(CommonResourceBundle.getInstance().getString("message.processingInstructionTargetIsEmpty"));
            }
            encodeTermination();
            encodeProcessingInstruction(target, data);
        } catch (IOException e2) {
            throw new SAXException("processingInstruction", e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void skippedEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void comment(char[] ch, int start, int length) throws SAXException {
        try {
            if (getIgnoreComments()) {
                return;
            }
            encodeTermination();
            encodeComment(ch, start, length);
        } catch (IOException e2) {
            throw new SAXException("startElement", e2);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void startCDATA() throws SAXException {
        this._charactersAsCDATA = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void endCDATA() throws SAXException {
        this._charactersAsCDATA = false;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (getIgnoreDTD()) {
            return;
        }
        try {
            encodeTermination();
            encodeDocumentTypeDeclaration(publicId, systemId);
            encodeElementTermination();
        } catch (IOException e2) {
            throw new SAXException("startDTD", e2);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void endEntity(String name) throws SAXException {
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public final void octets(String URI, int id, byte[] b2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeNonIdentifyingStringOnThirdBit(URI, id, b2, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public final void object(String URI, int id, Object data) throws SAXException {
        try {
            encodeTermination();
            encodeNonIdentifyingStringOnThirdBit(URI, id, data);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void bytes(byte[] b2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIOctetAlgorithmData(1, b2, start, length);
        } catch (IOException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void shorts(short[] s2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(2, s2, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void ints(int[] i2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(3, i2, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void longs(long[] l2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(4, l2, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void booleans(boolean[] b2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(5, b2, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void floats(float[] f2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(6, f2, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void doubles(double[] d2, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(7, d2, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void uuids(long[] msblsb, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(8, msblsb, start, length);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.RestrictedAlphabetContentHandler
    public void numericCharacters(char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
            encodeNumericFourBitCharacters(ch, start, length, addToTable);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.RestrictedAlphabetContentHandler
    public void dateTimeCharacters(char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
            encodeDateTimeFourBitCharacters(ch, start, length, addToTable);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.RestrictedAlphabetContentHandler
    public void alphabetCharacters(String alphabet, char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
            encodeAlphabetCharacters(alphabet, ch, start, length, addToTable);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.fastinfoset.sax.ExtendedContentHandler
    public void characters(char[] ch, int start, int length, boolean index) throws SAXException {
        if (length <= 0) {
            return;
        }
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, start, length)) {
            return;
        }
        try {
            encodeTermination();
            if (!this._charactersAsCDATA) {
                encodeNonIdentifyingStringOnThirdBit(ch, start, length, this._v.characterContentChunk, index, true);
            } else {
                encodeCIIBuiltInAlgorithmDataAsCDATA(ch, start, length);
            }
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        } catch (IOException e3) {
            throw new SAXException(e3);
        }
    }

    protected final int countAttributes(Attributes atts) {
        int count = 0;
        for (int i2 = 0; i2 < atts.getLength(); i2++) {
            String uri = atts.getURI(i2);
            if (uri != "http://www.w3.org/2000/xmlns/" && !uri.equals("http://www.w3.org/2000/xmlns/")) {
                count++;
            }
        }
        return count;
    }

    protected void encodeAttributes(Attributes atts) throws FastInfosetException, IOException {
        if (atts instanceof EncodingAlgorithmAttributes) {
            EncodingAlgorithmAttributes eAtts = (EncodingAlgorithmAttributes) atts;
            for (int i2 = 0; i2 < eAtts.getLength(); i2++) {
                if (encodeAttribute(atts.getURI(i2), atts.getQName(i2), atts.getLocalName(i2))) {
                    Object data = eAtts.getAlgorithmData(i2);
                    if (data == null) {
                        String value = eAtts.getValue(i2);
                        boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
                        boolean mustBeAddedToTable = eAtts.getToIndex(i2);
                        String alphabet = eAtts.getAlpababet(i2);
                        if (alphabet == null) {
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.DATE_TIME_CHARACTERS) {
                            encodeDateTimeNonIdentifyingStringOnFirstBit(value, addToTable, mustBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.NUMERIC_CHARACTERS) {
                            encodeNumericNonIdentifyingStringOnFirstBit(value, addToTable, mustBeAddedToTable);
                        } else {
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustBeAddedToTable);
                        }
                    } else {
                        encodeNonIdentifyingStringOnFirstBit(eAtts.getAlgorithmURI(i2), eAtts.getAlgorithmIndex(i2), data);
                    }
                }
            }
        } else {
            for (int i3 = 0; i3 < atts.getLength(); i3++) {
                if (encodeAttribute(atts.getURI(i3), atts.getQName(i3), atts.getLocalName(i3))) {
                    String value2 = atts.getValue(i3);
                    encodeNonIdentifyingStringOnFirstBit(value2, this._v.attributeValue, isAttributeValueLengthMatchesLimit(value2.length()), false);
                }
            }
        }
        this._b = 240;
        this._terminate = true;
    }

    protected void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i2 = 0; i2 < entry._valueIndex; i2++) {
                QualifiedName n2 = names[i2];
                if (namespaceURI == n2.namespaceName || namespaceURI.equals(n2.namespaceName)) {
                    encodeNonZeroIntegerOnThirdBit(names[i2].index);
                    return;
                }
            }
        }
        encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
    }

    protected boolean encodeAttribute(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i2 = 0; i2 < entry._valueIndex; i2++) {
                if (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName)) {
                    encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i2].index);
                    return true;
                }
            }
        }
        return encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
    }
}
