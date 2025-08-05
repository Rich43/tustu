package com.sun.xml.internal.fastinfoset.stax.events;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/stax/events/EndElementEvent.class */
public class EndElementEvent extends EventBase implements EndElement {
    List _namespaces = null;
    QName _qname;

    public void reset() {
        if (this._namespaces != null) {
            this._namespaces.clear();
        }
    }

    public EndElementEvent() {
        setEventType(2);
    }

    public EndElementEvent(String prefix, String namespaceURI, String localpart) {
        this._qname = getQName(namespaceURI, localpart, prefix);
        setEventType(2);
    }

    public EndElementEvent(QName qname) {
        this._qname = qname;
        setEventType(2);
    }

    @Override // javax.xml.stream.events.EndElement
    public QName getName() {
        return this._qname;
    }

    public void setName(QName qname) {
        this._qname = qname;
    }

    @Override // javax.xml.stream.events.EndElement
    public Iterator getNamespaces() {
        if (this._namespaces != null) {
            return this._namespaces.iterator();
        }
        return EmptyIterator.getInstance();
    }

    public void addNamespace(Namespace namespace) {
        if (this._namespaces == null) {
            this._namespaces = new ArrayList();
        }
        this._namespaces.add(namespace);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("</").append(nameAsString());
        Iterator namespaces = getNamespaces();
        while (namespaces.hasNext()) {
            sb.append(" ").append(namespaces.next().toString());
        }
        sb.append(">");
        return sb.toString();
    }

    private String nameAsString() {
        if ("".equals(this._qname.getNamespaceURI())) {
            return this._qname.getLocalPart();
        }
        if (this._qname.getPrefix() != null) {
            return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + this._qname.getLocalPart();
        }
        return "['" + this._qname.getNamespaceURI() + "']:" + this._qname.getLocalPart();
    }

    private QName getQName(String uri, String localPart, String prefix) {
        QName qn = null;
        if (prefix != null && uri != null) {
            qn = new QName(uri, localPart, prefix);
        } else if (prefix == null && uri != null) {
            qn = new QName(uri, localPart);
        } else if (prefix == null && uri == null) {
            qn = new QName(localPart);
        }
        return qn;
    }
}
