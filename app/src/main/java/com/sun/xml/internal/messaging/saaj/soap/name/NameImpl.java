package com.sun.xml.internal.messaging.saaj.soap.name;

import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPConstants;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl.class */
public class NameImpl implements Name {
    public static final String XML_NAMESPACE_PREFIX = "xml";
    public static final String XML_SCHEMA_NAMESPACE_PREFIX = "xs";
    public static final String SOAP_ENVELOPE_PREFIX = "SOAP-ENV";
    public static final String XML_NAMESPACE = "http://www.w3.org/XML/1998/namespace";
    public static final String SOAP11_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";
    public static final String SOAP12_NAMESPACE = "http://www.w3.org/2003/05/soap-envelope";
    public static final String XML_SCHEMA_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
    protected String uri;
    protected String localName;
    protected String prefix;
    private String qualifiedName;
    protected static final Logger log = Logger.getLogger(LogDomainConstants.NAMING_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.name.LocalStrings");
    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();

    protected NameImpl(String name) {
        this.uri = "";
        this.localName = "";
        this.prefix = "";
        this.qualifiedName = null;
        this.localName = name == null ? "" : name;
    }

    protected NameImpl(String name, String prefix, String uri) {
        this.uri = "";
        this.localName = "";
        this.prefix = "";
        this.qualifiedName = null;
        this.uri = uri == null ? "" : uri;
        this.localName = name == null ? "" : name;
        this.prefix = prefix == null ? "" : prefix;
        if (this.prefix.equals("xmlns") && this.uri.equals("")) {
            this.uri = XMLNS_URI;
        }
        if (this.uri.equals(XMLNS_URI) && this.prefix.equals("")) {
            this.prefix = "xmlns";
        }
    }

    public static Name convertToName(QName qname) {
        return new NameImpl(qname.getLocalPart(), qname.getPrefix(), qname.getNamespaceURI());
    }

    public static QName convertToQName(Name name) {
        return new QName(name.getURI(), name.getLocalName(), name.getPrefix());
    }

    public static NameImpl createFromUnqualifiedName(String name) {
        return new NameImpl(name);
    }

    public static Name createFromTagName(String tagName) {
        return createFromTagAndUri(tagName, "");
    }

    public static Name createFromQualifiedName(String qualifiedName, String uri) {
        return createFromTagAndUri(qualifiedName, uri);
    }

    protected static Name createFromTagAndUri(String tagName, String uri) {
        if (tagName == null) {
            log.severe("SAAJ0201.name.not.created.from.null.tag");
            throw new IllegalArgumentException("Cannot create a name from a null tag.");
        }
        int index = tagName.indexOf(58);
        if (index < 0) {
            return new NameImpl(tagName, "", uri);
        }
        return new NameImpl(tagName.substring(index + 1), tagName.substring(0, index), uri);
    }

    protected static int getPrefixSeparatorIndex(String qualifiedName) {
        int index = qualifiedName.indexOf(58);
        if (index < 0) {
            log.log(Level.SEVERE, "SAAJ0202.name.invalid.arg.format", (Object[]) new String[]{qualifiedName});
            throw new IllegalArgumentException("Argument \"" + qualifiedName + "\" must be of the form: \"prefix:localName\"");
        }
        return index;
    }

    public static String getPrefixFromQualifiedName(String qualifiedName) {
        return qualifiedName.substring(0, getPrefixSeparatorIndex(qualifiedName));
    }

    public static String getLocalNameFromQualifiedName(String qualifiedName) {
        return qualifiedName.substring(getPrefixSeparatorIndex(qualifiedName) + 1);
    }

    public static String getPrefixFromTagName(String tagName) {
        if (isQualified(tagName)) {
            return getPrefixFromQualifiedName(tagName);
        }
        return "";
    }

    public static String getLocalNameFromTagName(String tagName) {
        if (isQualified(tagName)) {
            return getLocalNameFromQualifiedName(tagName);
        }
        return tagName;
    }

    public static boolean isQualified(String tagName) {
        return tagName.indexOf(58) >= 0;
    }

    public static NameImpl create(String name, String prefix, String uri) {
        if (prefix == null) {
            prefix = "";
        }
        if (uri == null) {
            uri = "";
        }
        if (name == null) {
            name = "";
        }
        if (!uri.equals("") && !name.equals("")) {
            if (uri.equals("http://schemas.xmlsoap.org/soap/envelope/")) {
                if (name.equalsIgnoreCase("Envelope")) {
                    return createEnvelope1_1Name(prefix);
                }
                if (name.equalsIgnoreCase("Header")) {
                    return createHeader1_1Name(prefix);
                }
                if (name.equalsIgnoreCase("Body")) {
                    return createBody1_1Name(prefix);
                }
                if (name.equalsIgnoreCase(SOAPNamespaceConstants.TAG_FAULT)) {
                    return createFault1_1Name(prefix);
                }
                return new SOAP1_1Name(name, prefix);
            }
            if (uri.equals("http://www.w3.org/2003/05/soap-envelope")) {
                if (name.equalsIgnoreCase("Envelope")) {
                    return createEnvelope1_2Name(prefix);
                }
                if (name.equalsIgnoreCase("Header")) {
                    return createHeader1_2Name(prefix);
                }
                if (name.equalsIgnoreCase("Body")) {
                    return createBody1_2Name(prefix);
                }
                if (name.equals(SOAPNamespaceConstants.TAG_FAULT) || name.equals("Reason") || name.equals("Detail")) {
                    return createFault1_2Name(name, prefix);
                }
                if (name.equals("Code") || name.equals("Subcode")) {
                    return createCodeSubcode1_2Name(prefix, name);
                }
                return new SOAP1_2Name(name, prefix);
            }
        }
        return new NameImpl(name, prefix, uri);
    }

    public static String createQName(String prefix, String localName) {
        if (prefix == null || prefix.equals("")) {
            return localName;
        }
        return prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Name)) {
            return false;
        }
        Name otherName = (Name) obj;
        if (!this.uri.equals(otherName.getURI()) || !this.localName.equals(otherName.getLocalName())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.localName.hashCode();
    }

    @Override // javax.xml.soap.Name
    public String getLocalName() {
        return this.localName;
    }

    @Override // javax.xml.soap.Name
    public String getPrefix() {
        return this.prefix;
    }

    @Override // javax.xml.soap.Name
    public String getURI() {
        return this.uri;
    }

    @Override // javax.xml.soap.Name
    public String getQualifiedName() {
        if (this.qualifiedName == null) {
            if (this.prefix != null && this.prefix.length() > 0) {
                this.qualifiedName = this.prefix + CallSiteDescriptor.TOKEN_DELIMITER + this.localName;
            } else {
                this.qualifiedName = this.localName;
            }
        }
        return this.qualifiedName;
    }

    public static NameImpl createEnvelope1_1Name(String prefix) {
        return new Envelope1_1Name(prefix);
    }

    public static NameImpl createEnvelope1_2Name(String prefix) {
        return new Envelope1_2Name(prefix);
    }

    public static NameImpl createHeader1_1Name(String prefix) {
        return new Header1_1Name(prefix);
    }

    public static NameImpl createHeader1_2Name(String prefix) {
        return new Header1_2Name(prefix);
    }

    public static NameImpl createBody1_1Name(String prefix) {
        return new Body1_1Name(prefix);
    }

    public static NameImpl createBody1_2Name(String prefix) {
        return new Body1_2Name(prefix);
    }

    public static NameImpl createFault1_1Name(String prefix) {
        return new Fault1_1Name(prefix);
    }

    public static NameImpl createNotUnderstood1_2Name(String prefix) {
        return new NotUnderstood1_2Name(prefix);
    }

    public static NameImpl createUpgrade1_2Name(String prefix) {
        return new Upgrade1_2Name(prefix);
    }

    public static NameImpl createSupportedEnvelope1_2Name(String prefix) {
        return new SupportedEnvelope1_2Name(prefix);
    }

    public static NameImpl createFault1_2Name(String localName, String prefix) {
        return new Fault1_2Name(localName, prefix);
    }

    public static NameImpl createCodeSubcode1_2Name(String prefix, String localName) {
        return new CodeSubcode1_2Name(localName, prefix);
    }

    public static NameImpl createDetail1_1Name() {
        return new Detail1_1Name();
    }

    public static NameImpl createDetail1_1Name(String prefix) {
        return new Detail1_1Name(prefix);
    }

    public static NameImpl createFaultElement1_1Name(String localName) {
        return new FaultElement1_1Name(localName);
    }

    public static NameImpl createFaultElement1_1Name(String localName, String prefix) {
        return new FaultElement1_1Name(localName, prefix);
    }

    public static NameImpl createSOAP11Name(String string) {
        return new SOAP1_1Name(string, null);
    }

    public static NameImpl createSOAP12Name(String string) {
        return new SOAP1_2Name(string, null);
    }

    public static NameImpl createSOAP12Name(String localName, String prefix) {
        return new SOAP1_2Name(localName, prefix);
    }

    public static NameImpl createXmlName(String localName) {
        return new NameImpl(localName, "xml", "http://www.w3.org/XML/1998/namespace");
    }

    public static Name copyElementName(Element element) {
        String localName = element.getLocalName();
        String prefix = element.getPrefix();
        String uri = element.getNamespaceURI();
        return create(localName, prefix, uri);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$SOAP1_1Name.class */
    static class SOAP1_1Name extends NameImpl {
        SOAP1_1Name(String name, String prefix) {
            super(name, (prefix == null || prefix.equals("")) ? NameImpl.SOAP_ENVELOPE_PREFIX : prefix, "http://schemas.xmlsoap.org/soap/envelope/");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Envelope1_1Name.class */
    static class Envelope1_1Name extends SOAP1_1Name {
        Envelope1_1Name(String prefix) {
            super("Envelope", prefix);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Header1_1Name.class */
    static class Header1_1Name extends SOAP1_1Name {
        Header1_1Name(String prefix) {
            super("Header", prefix);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Body1_1Name.class */
    static class Body1_1Name extends SOAP1_1Name {
        Body1_1Name(String prefix) {
            super("Body", prefix);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Fault1_1Name.class */
    static class Fault1_1Name extends NameImpl {
        Fault1_1Name(String prefix) {
            super(SOAPNamespaceConstants.TAG_FAULT, (prefix == null || prefix.equals("")) ? NameImpl.SOAP_ENVELOPE_PREFIX : prefix, "http://schemas.xmlsoap.org/soap/envelope/");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Detail1_1Name.class */
    static class Detail1_1Name extends NameImpl {
        Detail1_1Name() {
            super("detail");
        }

        Detail1_1Name(String prefix) {
            super("detail", prefix, "");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$FaultElement1_1Name.class */
    static class FaultElement1_1Name extends NameImpl {
        FaultElement1_1Name(String localName) {
            super(localName);
        }

        FaultElement1_1Name(String localName, String prefix) {
            super(localName, prefix, "");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$SOAP1_2Name.class */
    static class SOAP1_2Name extends NameImpl {
        SOAP1_2Name(String name, String prefix) {
            super(name, (prefix == null || prefix.equals("")) ? SOAPConstants.SOAP_ENV_PREFIX : prefix, "http://www.w3.org/2003/05/soap-envelope");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Envelope1_2Name.class */
    static class Envelope1_2Name extends SOAP1_2Name {
        Envelope1_2Name(String prefix) {
            super("Envelope", prefix);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Header1_2Name.class */
    static class Header1_2Name extends SOAP1_2Name {
        Header1_2Name(String prefix) {
            super("Header", prefix);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Body1_2Name.class */
    static class Body1_2Name extends SOAP1_2Name {
        Body1_2Name(String prefix) {
            super("Body", prefix);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Fault1_2Name.class */
    static class Fault1_2Name extends NameImpl {
        Fault1_2Name(String name, String prefix) {
            super((name == null || name.equals("")) ? SOAPNamespaceConstants.TAG_FAULT : name, (prefix == null || prefix.equals("")) ? SOAPConstants.SOAP_ENV_PREFIX : prefix, "http://www.w3.org/2003/05/soap-envelope");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$NotUnderstood1_2Name.class */
    static class NotUnderstood1_2Name extends NameImpl {
        NotUnderstood1_2Name(String prefix) {
            super(SOAP12NamespaceConstants.TAG_NOT_UNDERSTOOD, (prefix == null || prefix.equals("")) ? SOAPConstants.SOAP_ENV_PREFIX : prefix, "http://www.w3.org/2003/05/soap-envelope");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$Upgrade1_2Name.class */
    static class Upgrade1_2Name extends NameImpl {
        Upgrade1_2Name(String prefix) {
            super("Upgrade", (prefix == null || prefix.equals("")) ? SOAPConstants.SOAP_ENV_PREFIX : prefix, "http://www.w3.org/2003/05/soap-envelope");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$SupportedEnvelope1_2Name.class */
    static class SupportedEnvelope1_2Name extends NameImpl {
        SupportedEnvelope1_2Name(String prefix) {
            super("SupportedEnvelope", (prefix == null || prefix.equals("")) ? SOAPConstants.SOAP_ENV_PREFIX : prefix, "http://www.w3.org/2003/05/soap-envelope");
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/name/NameImpl$CodeSubcode1_2Name.class */
    static class CodeSubcode1_2Name extends SOAP1_2Name {
        CodeSubcode1_2Name(String prefix, String localName) {
            super(prefix, localName);
        }
    }
}
