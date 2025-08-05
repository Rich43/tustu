package com.sun.xml.internal.stream.events;

import com.sun.xml.internal.stream.util.ReadOnlyIterator;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/EndElementEvent.class */
public class EndElementEvent extends DummyEvent implements EndElement {
    List fNamespaces;
    QName fQName;

    public EndElementEvent() {
        this.fNamespaces = null;
        init();
    }

    protected void init() {
        setEventType(2);
        this.fNamespaces = new ArrayList();
    }

    public EndElementEvent(String prefix, String uri, String localpart) {
        this(new QName(uri, localpart, prefix));
    }

    public EndElementEvent(QName qname) {
        this.fNamespaces = null;
        this.fQName = qname;
        init();
    }

    @Override // javax.xml.stream.events.EndElement
    public QName getName() {
        return this.fQName;
    }

    public void setName(QName qname) {
        this.fQName = qname;
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write("</");
        String prefix = this.fQName.getPrefix();
        if (prefix != null && prefix.length() > 0) {
            writer.write(prefix);
            writer.write(58);
        }
        writer.write(this.fQName.getLocalPart());
        writer.write(62);
    }

    @Override // javax.xml.stream.events.EndElement
    public Iterator getNamespaces() {
        if (this.fNamespaces != null) {
            this.fNamespaces.iterator();
        }
        return new ReadOnlyIterator();
    }

    void addNamespace(Namespace attr) {
        if (attr != null) {
            this.fNamespaces.add(attr);
        }
    }

    public String toString() {
        String s2 = "</" + nameAsString();
        return s2 + ">";
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
}
