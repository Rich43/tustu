package com.sun.xml.internal.fastinfoset.sax;

import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.internal.fastinfoset.util.StringIntMap;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import com.sun.xml.internal.org.jvnet.fastinfoset.RestrictedAlphabet;
import com.sun.xml.internal.org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/sax/SAXDocumentSerializerWithPrefixMapping.class */
public class SAXDocumentSerializerWithPrefixMapping extends SAXDocumentSerializer {
    protected Map _namespaceToPrefixMapping;
    protected Map _prefixToPrefixMapping;
    protected String _lastCheckedNamespace;
    protected String _lastCheckedPrefix;
    protected StringIntMap _declaredNamespaces;

    public SAXDocumentSerializerWithPrefixMapping(Map namespaceToPrefixMapping) {
        super(true);
        this._namespaceToPrefixMapping = new HashMap(namespaceToPrefixMapping);
        this._prefixToPrefixMapping = new HashMap();
        this._namespaceToPrefixMapping.put("", "");
        this._namespaceToPrefixMapping.put("http://www.w3.org/XML/1998/namespace", "xml");
        this._declaredNamespaces = new StringIntMap(4);
    }

    @Override // com.sun.xml.internal.fastinfoset.sax.SAXDocumentSerializer, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void startPrefixMapping(String prefix, String uri) throws SAXException {
        try {
            if (!this._elementHasNamespaces) {
                encodeTermination();
                mark();
                this._elementHasNamespaces = true;
                write(56);
                this._declaredNamespaces.clear();
                this._declaredNamespaces.obtainIndex(uri);
            } else if (this._declaredNamespaces.obtainIndex(uri) != -1) {
                String p2 = getPrefix(uri);
                if (p2 != null) {
                    this._prefixToPrefixMapping.put(prefix, p2);
                    return;
                }
                return;
            }
            String p3 = getPrefix(uri);
            if (p3 != null) {
                encodeNamespaceAttribute(p3, uri);
                this._prefixToPrefixMapping.put(prefix, p3);
            } else {
                putPrefix(uri, prefix);
                encodeNamespaceAttribute(prefix, uri);
            }
        } catch (IOException e2) {
            throw new SAXException("startElement", e2);
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.sax.SAXDocumentSerializer
    protected final void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            if (encodeElementMapEntry(entry, namespaceURI)) {
                return;
            }
            if (this._v.elementName.isQNameFromReadOnlyMap(entry._value[0])) {
                entry = this._v.elementName.obtainDynamicEntry(localName);
                if (entry._valueIndex > 0 && encodeElementMapEntry(entry, namespaceURI)) {
                    return;
                }
            }
        }
        encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefix(namespaceURI), localName, entry);
    }

    protected boolean encodeElementMapEntry(LocalNameQualifiedNamesMap.Entry entry, String namespaceURI) throws IOException {
        QualifiedName[] names = entry._value;
        for (int i2 = 0; i2 < entry._valueIndex; i2++) {
            if (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName)) {
                encodeNonZeroIntegerOnThirdBit(names[i2].index);
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.xml.internal.fastinfoset.sax.SAXDocumentSerializer
    protected final void encodeAttributes(Attributes atts) throws FastInfosetException, IOException {
        if (atts instanceof EncodingAlgorithmAttributes) {
            EncodingAlgorithmAttributes eAtts = (EncodingAlgorithmAttributes) atts;
            for (int i2 = 0; i2 < eAtts.getLength(); i2++) {
                String uri = atts.getURI(i2);
                if (encodeAttribute(uri, atts.getQName(i2), atts.getLocalName(i2))) {
                    Object data = eAtts.getAlgorithmData(i2);
                    if (data == null) {
                        String value = eAtts.getValue(i2);
                        boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
                        boolean mustToBeAddedToTable = eAtts.getToIndex(i2);
                        String alphabet = eAtts.getAlpababet(i2);
                        if (alphabet == null) {
                            if (uri == "http://www.w3.org/2001/XMLSchema-instance" || uri.equals("http://www.w3.org/2001/XMLSchema-instance")) {
                                value = convertQName(value);
                            }
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustToBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.DATE_TIME_CHARACTERS) {
                            encodeDateTimeNonIdentifyingStringOnFirstBit(value, addToTable, mustToBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.NUMERIC_CHARACTERS) {
                            encodeNumericNonIdentifyingStringOnFirstBit(value, addToTable, mustToBeAddedToTable);
                        } else {
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustToBeAddedToTable);
                        }
                    } else {
                        encodeNonIdentifyingStringOnFirstBit(eAtts.getAlgorithmURI(i2), eAtts.getAlgorithmIndex(i2), data);
                    }
                }
            }
        } else {
            for (int i3 = 0; i3 < atts.getLength(); i3++) {
                String uri2 = atts.getURI(i3);
                if (encodeAttribute(atts.getURI(i3), atts.getQName(i3), atts.getLocalName(i3))) {
                    String value2 = atts.getValue(i3);
                    boolean addToTable2 = isAttributeValueLengthMatchesLimit(value2.length());
                    if (uri2 == "http://www.w3.org/2001/XMLSchema-instance" || uri2.equals("http://www.w3.org/2001/XMLSchema-instance")) {
                        value2 = convertQName(value2);
                    }
                    encodeNonIdentifyingStringOnFirstBit(value2, this._v.attributeValue, addToTable2, false);
                }
            }
        }
        this._b = 240;
        this._terminate = true;
    }

    private String convertQName(String qName) {
        int i2 = qName.indexOf(58);
        String prefix = "";
        String localName = qName;
        if (i2 != -1) {
            prefix = qName.substring(0, i2);
            localName = qName.substring(i2 + 1);
        }
        String p2 = (String) this._prefixToPrefixMapping.get(prefix);
        if (p2 != null) {
            if (p2.length() == 0) {
                return localName;
            }
            return p2 + CallSiteDescriptor.TOKEN_DELIMITER + localName;
        }
        return qName;
    }

    @Override // com.sun.xml.internal.fastinfoset.sax.SAXDocumentSerializer
    protected final boolean encodeAttribute(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            if (encodeAttributeMapEntry(entry, namespaceURI)) {
                return true;
            }
            if (this._v.attributeName.isQNameFromReadOnlyMap(entry._value[0])) {
                entry = this._v.attributeName.obtainDynamicEntry(localName);
                if (entry._valueIndex > 0 && encodeAttributeMapEntry(entry, namespaceURI)) {
                    return true;
                }
            }
        }
        return encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefix(namespaceURI), localName, entry);
    }

    protected boolean encodeAttributeMapEntry(LocalNameQualifiedNamesMap.Entry entry, String namespaceURI) throws IOException {
        QualifiedName[] names = entry._value;
        for (int i2 = 0; i2 < entry._valueIndex; i2++) {
            if (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName)) {
                encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i2].index);
                return true;
            }
        }
        return false;
    }

    protected final String getPrefix(String namespaceURI) {
        if (this._lastCheckedNamespace == namespaceURI) {
            return this._lastCheckedPrefix;
        }
        this._lastCheckedNamespace = namespaceURI;
        String str = (String) this._namespaceToPrefixMapping.get(namespaceURI);
        this._lastCheckedPrefix = str;
        return str;
    }

    protected final void putPrefix(String namespaceURI, String prefix) {
        this._namespaceToPrefixMapping.put(namespaceURI, prefix);
        this._lastCheckedNamespace = namespaceURI;
        this._lastCheckedPrefix = prefix;
    }
}
