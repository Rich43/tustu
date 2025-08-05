package com.sun.xml.internal.fastinfoset.stax.events;

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

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/StartElementEvent.class */
public class StartElementEvent extends EventBase implements StartElement {
    private Map _attributes;
    private List _namespaces;
    private NamespaceContext _context;
    private QName _qname;

    public void reset() {
        if (this._attributes != null) {
            this._attributes.clear();
        }
        if (this._namespaces != null) {
            this._namespaces.clear();
        }
        if (this._context != null) {
            this._context = null;
        }
    }

    public StartElementEvent() {
        this._context = null;
        init();
    }

    public StartElementEvent(String prefix, String uri, String localpart) {
        this._context = null;
        init();
        this._qname = new QName(uri == null ? "" : uri, localpart, prefix == null ? "" : prefix);
        setEventType(1);
    }

    public StartElementEvent(QName qname) {
        this._context = null;
        init();
        this._qname = qname;
    }

    public StartElementEvent(StartElement startelement) {
        this(startelement.getName());
        addAttributes(startelement.getAttributes());
        addNamespaces(startelement.getNamespaces());
    }

    protected void init() {
        setEventType(1);
        this._attributes = new HashMap();
        this._namespaces = new ArrayList();
    }

    @Override // javax.xml.stream.events.StartElement
    public QName getName() {
        return this._qname;
    }

    @Override // javax.xml.stream.events.StartElement
    public Iterator getAttributes() {
        if (this._attributes != null) {
            Collection coll = this._attributes.values();
            return new ReadIterator(coll.iterator());
        }
        return EmptyIterator.getInstance();
    }

    @Override // javax.xml.stream.events.StartElement
    public Iterator getNamespaces() {
        if (this._namespaces != null) {
            return new ReadIterator(this._namespaces.iterator());
        }
        return EmptyIterator.getInstance();
    }

    @Override // javax.xml.stream.events.StartElement
    public Attribute getAttributeByName(QName qname) {
        if (qname == null) {
            return null;
        }
        return (Attribute) this._attributes.get(qname);
    }

    @Override // javax.xml.stream.events.StartElement
    public NamespaceContext getNamespaceContext() {
        return this._context;
    }

    public void setName(QName qname) {
        this._qname = qname;
    }

    public String getNamespace() {
        return this._qname.getNamespaceURI();
    }

    @Override // javax.xml.stream.events.StartElement
    public String getNamespaceURI(String prefix) {
        if (getNamespace() != null) {
            return getNamespace();
        }
        if (this._context != null) {
            return this._context.getNamespaceURI(prefix);
        }
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append('<').append(nameAsString());
        if (this._attributes != null) {
            Iterator it = getAttributes();
            while (it.hasNext()) {
                Attribute attr = (Attribute) it.next();
                sb.append(' ').append(attr.toString());
            }
        }
        if (this._namespaces != null) {
            for (Namespace attr2 : this._namespaces) {
                sb.append(' ').append(attr2.toString());
            }
        }
        sb.append('>');
        return sb.toString();
    }

    public String nameAsString() {
        if ("".equals(this._qname.getNamespaceURI())) {
            return this._qname.getLocalPart();
        }
        if (this._qname.getPrefix() != null) {
            return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + this._qname.getLocalPart();
        }
        return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getLocalPart();
    }

    public void setNamespaceContext(NamespaceContext context) {
        this._context = context;
    }

    public void addAttribute(Attribute attr) {
        this._attributes.put(attr.getName(), attr);
    }

    public void addAttributes(Iterator attrs) {
        if (attrs != null) {
            while (attrs.hasNext()) {
                Attribute attr = (Attribute) attrs.next();
                this._attributes.put(attr.getName(), attr);
            }
        }
    }

    public void addNamespace(Namespace namespace) {
        if (namespace != null) {
            this._namespaces.add(namespace);
        }
    }

    public void addNamespaces(Iterator namespaces) {
        if (namespaces != null) {
            while (namespaces.hasNext()) {
                Namespace namespace = (Namespace) namespaces.next();
                this._namespaces.add(namespace);
            }
        }
    }
}
