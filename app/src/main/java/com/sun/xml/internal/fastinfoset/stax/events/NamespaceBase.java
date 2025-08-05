package com.sun.xml.internal.fastinfoset.stax.events;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Namespace;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/NamespaceBase.class */
public class NamespaceBase extends AttributeBase implements Namespace {
    static final String DEFAULT_NS_PREFIX = "";
    static final String XML_NS_URI = "http://www.w3.org/XML/1998/namespace";
    static final String XML_NS_PREFIX = "xml";
    static final String XMLNS_ATTRIBUTE_NS_URI = "http://www.w3.org/2000/xmlns/";
    static final String XMLNS_ATTRIBUTE = "xmlns";
    static final String W3C_XML_SCHEMA_NS_URI = "http://www.w3.org/2001/XMLSchema";
    static final String W3C_XML_SCHEMA_INSTANCE_NS_URI = "http://www.w3.org/2001/XMLSchema-instance";
    private boolean defaultDeclaration;

    public NamespaceBase(String namespaceURI) {
        super("xmlns", "", namespaceURI);
        this.defaultDeclaration = false;
        setEventType(13);
    }

    public NamespaceBase(String prefix, String namespaceURI) {
        super("xmlns", prefix, namespaceURI);
        this.defaultDeclaration = false;
        setEventType(13);
        if (Util.isEmptyString(prefix)) {
            this.defaultDeclaration = true;
        }
    }

    void setPrefix(String prefix) {
        if (prefix == null) {
            setName(new QName("http://www.w3.org/2000/xmlns/", "", "xmlns"));
        } else {
            setName(new QName("http://www.w3.org/2000/xmlns/", prefix, "xmlns"));
        }
    }

    @Override // javax.xml.stream.events.Namespace
    public String getPrefix() {
        return this.defaultDeclaration ? "" : super.getLocalName();
    }

    void setNamespaceURI(String uri) {
        setValue(uri);
    }

    @Override // javax.xml.stream.events.Namespace
    public String getNamespaceURI() {
        return getValue();
    }

    @Override // com.sun.xml.internal.fastinfoset.stax.events.EventBase, javax.xml.stream.events.XMLEvent
    public boolean isNamespace() {
        return true;
    }

    @Override // javax.xml.stream.events.Namespace
    public boolean isDefaultNamespaceDeclaration() {
        return this.defaultDeclaration;
    }
}
