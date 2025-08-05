package com.sun.xml.internal.stream.events;

import javax.xml.namespace.QName;
import javax.xml.stream.events.Namespace;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/NamespaceImpl.class */
public class NamespaceImpl extends AttributeImpl implements Namespace {
    public NamespaceImpl() {
        init();
    }

    public NamespaceImpl(String namespaceURI) {
        super("xmlns", "http://www.w3.org/2000/xmlns/", "", namespaceURI, (String) null);
        init();
    }

    public NamespaceImpl(String prefix, String namespaceURI) {
        super("xmlns", "http://www.w3.org/2000/xmlns/", prefix, namespaceURI, (String) null);
        init();
    }

    @Override // javax.xml.stream.events.Namespace
    public boolean isDefaultNamespaceDeclaration() {
        QName name = getName();
        if (name != null && name.getLocalPart().equals("")) {
            return true;
        }
        return false;
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
        QName name = getName();
        if (name != null) {
            return name.getLocalPart();
        }
        return null;
    }

    @Override // javax.xml.stream.events.Namespace
    public String getNamespaceURI() {
        return getValue();
    }

    void setNamespaceURI(String uri) {
        setValue(uri);
    }

    @Override // com.sun.xml.internal.stream.events.AttributeImpl
    protected void init() {
        setEventType(13);
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent, javax.xml.stream.events.XMLEvent
    public int getEventType() {
        return 13;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent, javax.xml.stream.events.XMLEvent
    public boolean isNamespace() {
        return true;
    }
}
