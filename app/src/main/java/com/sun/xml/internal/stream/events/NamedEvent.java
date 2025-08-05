package com.sun.xml.internal.stream.events;

import java.io.IOException;
import java.io.Writer;
import javax.xml.namespace.QName;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/stream/events/NamedEvent.class */
public class NamedEvent extends DummyEvent {
    private QName name;

    public NamedEvent() {
    }

    public NamedEvent(QName qname) {
        this.name = qname;
    }

    public NamedEvent(String prefix, String uri, String localpart) {
        this.name = new QName(uri, localpart, prefix);
    }

    public String getPrefix() {
        return this.name.getPrefix();
    }

    public QName getName() {
        return this.name;
    }

    public void setName(QName qname) {
        this.name = qname;
    }

    public String nameAsString() {
        if ("".equals(this.name.getNamespaceURI())) {
            return this.name.getLocalPart();
        }
        if (this.name.getPrefix() != null) {
            return "['" + this.name.getNamespaceURI() + "']:" + getPrefix() + CallSiteDescriptor.TOKEN_DELIMITER + this.name.getLocalPart();
        }
        return "['" + this.name.getNamespaceURI() + "']:" + this.name.getLocalPart();
    }

    public String getNamespace() {
        return this.name.getNamespaceURI();
    }

    @Override // com.sun.xml.internal.stream.events.DummyEvent
    protected void writeAsEncodedUnicodeEx(Writer writer) throws IOException {
        writer.write(nameAsString());
    }
}
