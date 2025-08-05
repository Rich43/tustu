package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XNIException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/XMLNSDTDValidator.class */
public class XMLNSDTDValidator extends XMLDTDValidator {
    private QName fAttributeQName = new QName();

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator
    protected final void startNamespaceScope(QName element, XMLAttributes attributes, Augmentations augs) throws XNIException {
        this.fNamespaceContext.pushContext();
        if (element.prefix == XMLSymbols.PREFIX_XMLNS) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementXMLNSPrefix", new Object[]{element.rawname}, (short) 2);
        }
        int length = attributes.getLength();
        for (int i2 = 0; i2 < length; i2++) {
            String localpart = attributes.getLocalName(i2);
            String prefix = attributes.getPrefix(i2);
            if (prefix == XMLSymbols.PREFIX_XMLNS || (prefix == XMLSymbols.EMPTY_STRING && localpart == XMLSymbols.PREFIX_XMLNS)) {
                String uri = this.fSymbolTable.addSymbol(attributes.getValue(i2));
                if (prefix == XMLSymbols.PREFIX_XMLNS && localpart == XMLSymbols.PREFIX_XMLNS) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{attributes.getQName(i2)}, (short) 2);
                }
                if (uri == NamespaceContext.XMLNS_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXMLNS", new Object[]{attributes.getQName(i2)}, (short) 2);
                }
                if (localpart == XMLSymbols.PREFIX_XML) {
                    if (uri != NamespaceContext.XML_URI) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{attributes.getQName(i2)}, (short) 2);
                    }
                } else if (uri == NamespaceContext.XML_URI) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "CantBindXML", new Object[]{attributes.getQName(i2)}, (short) 2);
                }
                String prefix2 = localpart != XMLSymbols.PREFIX_XMLNS ? localpart : XMLSymbols.EMPTY_STRING;
                if (uri == XMLSymbols.EMPTY_STRING && localpart != XMLSymbols.PREFIX_XMLNS) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "EmptyPrefixedAttName", new Object[]{attributes.getQName(i2)}, (short) 2);
                } else {
                    this.fNamespaceContext.declarePrefix(prefix2, uri.length() != 0 ? uri : null);
                }
            }
        }
        element.uri = this.fNamespaceContext.getURI(element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING);
        if (element.prefix == null && element.uri != null) {
            element.prefix = XMLSymbols.EMPTY_STRING;
        }
        if (element.prefix != null && element.uri == null) {
            this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "ElementPrefixUnbound", new Object[]{element.prefix, element.rawname}, (short) 2);
        }
        for (int i3 = 0; i3 < length; i3++) {
            attributes.getName(i3, this.fAttributeQName);
            String aprefix = this.fAttributeQName.prefix != null ? this.fAttributeQName.prefix : XMLSymbols.EMPTY_STRING;
            String arawname = this.fAttributeQName.rawname;
            if (arawname == XMLSymbols.PREFIX_XMLNS) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(XMLSymbols.PREFIX_XMLNS);
                attributes.setName(i3, this.fAttributeQName);
            } else if (aprefix != XMLSymbols.EMPTY_STRING) {
                this.fAttributeQName.uri = this.fNamespaceContext.getURI(aprefix);
                if (this.fAttributeQName.uri == null) {
                    this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributePrefixUnbound", new Object[]{element.rawname, arawname, aprefix}, (short) 2);
                }
                attributes.setName(i3, this.fAttributeQName);
            }
        }
        int attrCount = attributes.getLength();
        for (int i4 = 0; i4 < attrCount - 1; i4++) {
            String auri = attributes.getURI(i4);
            if (auri != null && auri != NamespaceContext.XMLNS_URI) {
                String alocalpart = attributes.getLocalName(i4);
                for (int j2 = i4 + 1; j2 < attrCount; j2++) {
                    String blocalpart = attributes.getLocalName(j2);
                    String buri = attributes.getURI(j2);
                    if (alocalpart == blocalpart && auri == buri) {
                        this.fErrorReporter.reportError("http://www.w3.org/TR/1999/REC-xml-names-19990114", "AttributeNSNotUnique", new Object[]{element.rawname, alocalpart, auri}, (short) 2);
                    }
                }
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator
    protected void endNamespaceScope(QName element, Augmentations augs, boolean isEmpty) throws XNIException {
        String eprefix = element.prefix != null ? element.prefix : XMLSymbols.EMPTY_STRING;
        element.uri = this.fNamespaceContext.getURI(eprefix);
        if (element.uri != null) {
            element.prefix = eprefix;
        }
        if (this.fDocumentHandler != null && !isEmpty) {
            this.fDocumentHandler.endElement(element, augs);
        }
        this.fNamespaceContext.popContext();
    }
}
