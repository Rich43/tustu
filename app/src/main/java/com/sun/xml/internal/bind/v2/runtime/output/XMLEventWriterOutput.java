package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import java.io.IOException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/XMLEventWriterOutput.class */
public class XMLEventWriterOutput extends XmlOutputAbstractImpl {
    private final XMLEventWriter out;
    private final XMLEventFactory ef = XMLEventFactory.newInstance();
    private final Characters sp = this.ef.createCharacters(" ");

    public XMLEventWriterOutput(XMLEventWriter out) {
        this.out = out;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, XMLStreamException, IOException {
        super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
        if (!fragment) {
            this.out.add(this.ef.createStartDocument());
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws XMLStreamException, SAXException, IOException {
        if (!fragment) {
            this.out.add(this.ef.createEndDocument());
            this.out.flush();
        }
        super.endDocument(fragment);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(int prefix, String localName) throws XMLStreamException, IOException {
        this.out.add(this.ef.createStartElement(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName));
        NamespaceContextImpl.Element nse = this.nsContext.getCurrent();
        if (nse.count() > 0) {
            for (int i2 = nse.count() - 1; i2 >= 0; i2--) {
                String uri = nse.getNsUri(i2);
                if (uri.length() != 0 || nse.getBase() != 1) {
                    this.out.add(this.ef.createNamespace(nse.getPrefix(i2), uri));
                }
            }
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) throws XMLStreamException, IOException {
        Attribute att;
        if (prefix == -1) {
            att = this.ef.createAttribute(localName, value);
        } else {
            att = this.ef.createAttribute(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName, value);
        }
        this.out.add(att);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws SAXException, IOException {
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(int prefix, String localName) throws XMLStreamException, SAXException, IOException {
        this.out.add(this.ef.createEndElement(this.nsContext.getPrefix(prefix), this.nsContext.getNamespaceURI(prefix), localName));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(String value, boolean needsSeparatingWhitespace) throws XMLStreamException, SAXException, IOException {
        if (needsSeparatingWhitespace) {
            this.out.add(this.sp);
        }
        this.out.add(this.ef.createCharacters(value));
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needsSeparatingWhitespace) throws XMLStreamException, SAXException, IOException {
        text(value.toString(), needsSeparatingWhitespace);
    }
}
