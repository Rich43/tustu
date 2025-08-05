package com.sun.xml.internal.stream.events;

import com.sun.xml.internal.stream.util.ReadOnlyIterator;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/StartElementEvent.class */
public class StartElementEvent extends DummyEvent implements StartElement {
    private Map fAttributes;
    private List fNamespaces;
    private NamespaceContext fNamespaceContext;
    private QName fQName;

    public StartElementEvent(String prefix, String uri, String localpart) {
        this(new QName(uri, localpart, prefix));
    }

    public StartElementEvent(QName qname) {
        this.fNamespaceContext = null;
        this.fQName = qname;
        init();
    }

    public StartElementEvent(StartElement startelement) {
        this(startelement.getName());
        addAttributes(startelement.getAttributes());
        addNamespaceAttributes(startelement.getNamespaces());
    }

    protected void init() {
        setEventType(1);
        this.fAttributes = new HashMap();
        this.fNamespaces = new ArrayList();
    }

    @Override // javax.xml.stream.events.StartElement
    public QName getName() {
        return this.fQName;
    }

    public void setName(QName qname) {
        this.fQName = qname;
    }

    @Override // javax.xml.stream.events.StartElement
    public Iterator getAttributes() {
        if (this.fAttributes != null) {
            Collection coll = this.fAttributes.values();
            return new ReadOnlyIterator(coll.iterator());
        }
        return new ReadOnlyIterator();
    }

    @Override // javax.xml.stream.events.StartElement
    public Iterator getNamespaces() {
        if (this.fNamespaces != null) {
            return new ReadOnlyIterator(this.fNamespaces.iterator());
        }
        return new ReadOnlyIterator();
    }

    @Override // javax.xml.stream.events.StartElement
    public Attribute getAttributeByName(QName qname) {
        if (qname == null) {
            return null;
        }
        return (Attribute) this.fAttributes.get(qname);
    }

    public String getNamespace() {
        return this.fQName.getNamespaceURI();
    }

    @Override // javax.xml.stream.events.StartElement
    public String getNamespaceURI(String prefix) {
        if (getNamespace() != null && this.fQName.getPrefix().equals(prefix)) {
            return getNamespace();
        }
        if (this.fNamespaceContext != null) {
            return this.fNamespaceContext.getNamespaceURI(prefix);
        }
        return null;
    }

    public String toString() {
        StringBuffer startElement = new StringBuffer();
        startElement.append("<");
        startElement.append(nameAsString());
        if (this.fAttributes != null) {
            Iterator it = getAttributes();
            while (it.hasNext()) {
                Attribute attr = (Attribute) it.next();
                startElement.append(" ");
                startElement.append(attr.toString());
            }
        }
        if (this.fNamespaces != null) {
            for (Namespace attr2 : this.fNamespaces) {
                startElement.append(" ");
                startElement.append(attr2.toString());
            }
        }
        startElement.append(">");
        return startElement.toString();
    }

    public String nameAsString() {
        if ("".equals(this.fQName.getNamespaceURI())) {
            return this.fQName.getLocalPart();
        }
        if (this.fQName.getPrefix() != null) {
            return "['" + this.fQName.getNamespaceURI() + "']:" + this.fQName.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + this.fQName.getLocalPart();
        }
        return "['" + this.fQName.getNamespaceURI() + "']:" + this.fQName.getLocalPart();
    }

    @Override // javax.xml.stream.events.StartElement
    public NamespaceContext getNamespaceContext() {
        return this.fNamespaceContext;
    }

    public void setNamespaceContext(NamespaceContext nc) {
        this.fNamespaceContext = nc;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(toString());
    }

    void addAttribute(Attribute attr) {
        if (attr.isNamespace()) {
            this.fNamespaces.add(attr);
        } else {
            this.fAttributes.put(attr.getName(), attr);
        }
    }

    void addAttributes(Iterator attrs) {
        if (attrs == null) {
            return;
        }
        while (attrs.hasNext()) {
            Attribute attr = (Attribute) attrs.next();
            this.fAttributes.put(attr.getName(), attr);
        }
    }

    void addNamespaceAttribute(Namespace attr) {
        if (attr == null) {
            return;
        }
        this.fNamespaces.add(attr);
    }

    void addNamespaceAttributes(Iterator attrs) {
        if (attrs == null) {
            return;
        }
        while (attrs.hasNext()) {
            Namespace attr = (Namespace) attrs.next();
            this.fNamespaces.add(attr);
        }
    }
}
