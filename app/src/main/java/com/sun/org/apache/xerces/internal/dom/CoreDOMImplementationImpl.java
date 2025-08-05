package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.impl.RevalidationHandler;
import com.sun.org.apache.xerces.internal.parsers.DOMParserImpl;
import com.sun.org.apache.xerces.internal.parsers.DTDConfiguration;
import com.sun.org.apache.xerces.internal.parsers.XIncludeAwareParserConfiguration;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.utils.ConfigurationError;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import com.sun.org.apache.xml.internal.serialize.DOMSerializerImpl;
import java.util.MissingResourceException;
import org.slf4j.Marker;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/CoreDOMImplementationImpl.class */
public class CoreDOMImplementationImpl implements DOMImplementation, DOMImplementationLS {
    private static final int SIZE = 2;
    private RevalidationHandler[] validators = new RevalidationHandler[2];
    private RevalidationHandler[] dtdValidators = new RevalidationHandler[2];
    private int freeValidatorIndex = -1;
    private int freeDTDValidatorIndex = -1;
    private int currentSize = 2;
    private int docAndDoctypeCounter = 0;
    static CoreDOMImplementationImpl singleton = new CoreDOMImplementationImpl();

    public static DOMImplementation getDOMImplementation() {
        return singleton;
    }

    @Override // org.w3c.dom.DOMImplementation
    public boolean hasFeature(String feature, String version) throws ConfigurationError {
        boolean anyVersion = version == null || version.length() == 0;
        if (feature.equalsIgnoreCase("+XPath") && (anyVersion || version.equals("3.0"))) {
            try {
                Class xpathClass = ObjectFactory.findProviderClass("com.sun.org.apache.xpath.internal.domapi.XPathEvaluatorImpl", true);
                Class[] interfaces = xpathClass.getInterfaces();
                for (int i2 = 0; i2 < interfaces.length && !interfaces[i2].getName().equals("org.w3c.dom.xpath.XPathEvaluator"); i2++) {
                }
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
        if (feature.startsWith(Marker.ANY_NON_NULL_MARKER)) {
            feature = feature.substring(1);
        }
        return (feature.equalsIgnoreCase("Core") && (anyVersion || version.equals("1.0") || version.equals("2.0") || version.equals("3.0"))) || (feature.equalsIgnoreCase("XML") && (anyVersion || version.equals("1.0") || version.equals("2.0") || version.equals("3.0"))) || (feature.equalsIgnoreCase("LS") && (anyVersion || version.equals("3.0")));
    }

    @Override // org.w3c.dom.DOMImplementation
    public DocumentType createDocumentType(String qualifiedName, String publicID, String systemID) throws MissingResourceException {
        checkQName(qualifiedName);
        return new DocumentTypeImpl(null, qualifiedName, publicID, systemID);
    }

    final void checkQName(String qname) throws MissingResourceException {
        int index = qname.indexOf(58);
        int lastIndex = qname.lastIndexOf(58);
        int length = qname.length();
        if (index == 0 || index == length - 1 || lastIndex != index) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
            throw new DOMException((short) 14, msg);
        }
        int start = 0;
        if (index > 0) {
            if (!XMLChar.isNCNameStart(qname.charAt(0))) {
                String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
                throw new DOMException((short) 5, msg2);
            }
            for (int i2 = 1; i2 < index; i2++) {
                if (!XMLChar.isNCName(qname.charAt(i2))) {
                    String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
                    throw new DOMException((short) 5, msg3);
                }
            }
            start = index + 1;
        }
        if (!XMLChar.isNCNameStart(qname.charAt(start))) {
            String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
            throw new DOMException((short) 5, msg4);
        }
        for (int i3 = start + 1; i3 < length; i3++) {
            if (!XMLChar.isNCName(qname.charAt(i3))) {
                String msg5 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
                throw new DOMException((short) 5, msg5);
            }
        }
    }

    @Override // org.w3c.dom.DOMImplementation
    public Document createDocument(String namespaceURI, String qualifiedName, DocumentType doctype) throws DOMException, MissingResourceException {
        if (doctype != null && doctype.getOwnerDocument() != null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "WRONG_DOCUMENT_ERR", null);
            throw new DOMException((short) 4, msg);
        }
        CoreDocumentImpl doc = new CoreDocumentImpl(doctype);
        Element e2 = doc.createElementNS(namespaceURI, qualifiedName);
        doc.appendChild(e2);
        return doc;
    }

    @Override // org.w3c.dom.DOMImplementation
    public Object getFeature(String feature, String version) throws ConfigurationError {
        if (singleton.hasFeature(feature, version)) {
            if (feature.equalsIgnoreCase("+XPath")) {
                try {
                    Class xpathClass = ObjectFactory.findProviderClass("com.sun.org.apache.xpath.internal.domapi.XPathEvaluatorImpl", true);
                    Class[] interfaces = xpathClass.getInterfaces();
                    for (Class cls : interfaces) {
                        if (cls.getName().equals("org.w3c.dom.xpath.XPathEvaluator")) {
                            return xpathClass.newInstance();
                        }
                    }
                    return null;
                } catch (Exception e2) {
                    return null;
                }
            }
            return singleton;
        }
        return null;
    }

    @Override // org.w3c.dom.ls.DOMImplementationLS
    public LSParser createLSParser(short mode, String schemaType) throws DOMException, MissingResourceException {
        if (mode != 1 || (schemaType != null && !"http://www.w3.org/2001/XMLSchema".equals(schemaType) && !"http://www.w3.org/TR/REC-xml".equals(schemaType))) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NOT_SUPPORTED_ERR", null);
            throw new DOMException((short) 9, msg);
        }
        if (schemaType != null && schemaType.equals("http://www.w3.org/TR/REC-xml")) {
            return new DOMParserImpl(new DTDConfiguration(), schemaType);
        }
        return new DOMParserImpl(new XIncludeAwareParserConfiguration(), schemaType);
    }

    @Override // org.w3c.dom.ls.DOMImplementationLS
    public LSSerializer createLSSerializer() {
        return new DOMSerializerImpl();
    }

    @Override // org.w3c.dom.ls.DOMImplementationLS
    public LSInput createLSInput() {
        return new DOMInputImpl();
    }

    synchronized RevalidationHandler getValidator(String schemaType) {
        if (schemaType == "http://www.w3.org/2001/XMLSchema") {
            if (this.freeValidatorIndex < 0) {
                return (RevalidationHandler) ObjectFactory.newInstance("com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaValidator", ObjectFactory.findClassLoader(), true);
            }
            RevalidationHandler val = this.validators[this.freeValidatorIndex];
            RevalidationHandler[] revalidationHandlerArr = this.validators;
            int i2 = this.freeValidatorIndex;
            this.freeValidatorIndex = i2 - 1;
            revalidationHandlerArr[i2] = null;
            return val;
        }
        if (schemaType == "http://www.w3.org/TR/REC-xml") {
            if (this.freeDTDValidatorIndex < 0) {
                return (RevalidationHandler) ObjectFactory.newInstance("com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator", ObjectFactory.findClassLoader(), true);
            }
            RevalidationHandler val2 = this.dtdValidators[this.freeDTDValidatorIndex];
            RevalidationHandler[] revalidationHandlerArr2 = this.dtdValidators;
            int i3 = this.freeDTDValidatorIndex;
            this.freeDTDValidatorIndex = i3 - 1;
            revalidationHandlerArr2[i3] = null;
            return val2;
        }
        return null;
    }

    synchronized void releaseValidator(String schemaType, RevalidationHandler validator) {
        if (schemaType == "http://www.w3.org/2001/XMLSchema") {
            this.freeValidatorIndex++;
            if (this.validators.length == this.freeValidatorIndex) {
                this.currentSize += 2;
                RevalidationHandler[] newarray = new RevalidationHandler[this.currentSize];
                System.arraycopy(this.validators, 0, newarray, 0, this.validators.length);
                this.validators = newarray;
            }
            this.validators[this.freeValidatorIndex] = validator;
            return;
        }
        if (schemaType == "http://www.w3.org/TR/REC-xml") {
            this.freeDTDValidatorIndex++;
            if (this.dtdValidators.length == this.freeDTDValidatorIndex) {
                this.currentSize += 2;
                RevalidationHandler[] newarray2 = new RevalidationHandler[this.currentSize];
                System.arraycopy(this.dtdValidators, 0, newarray2, 0, this.dtdValidators.length);
                this.dtdValidators = newarray2;
            }
            this.dtdValidators[this.freeDTDValidatorIndex] = validator;
        }
    }

    protected synchronized int assignDocumentNumber() {
        int i2 = this.docAndDoctypeCounter + 1;
        this.docAndDoctypeCounter = i2;
        return i2;
    }

    protected synchronized int assignDocTypeNumber() {
        int i2 = this.docAndDoctypeCounter + 1;
        this.docAndDoctypeCounter = i2;
        return i2;
    }

    @Override // org.w3c.dom.ls.DOMImplementationLS
    public LSOutput createLSOutput() {
        return new DOMOutputImpl();
    }
}
