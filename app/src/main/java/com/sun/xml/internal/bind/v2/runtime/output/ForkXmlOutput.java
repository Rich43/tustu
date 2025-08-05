package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/ForkXmlOutput.class */
public final class ForkXmlOutput extends XmlOutputAbstractImpl {
    private final XmlOutput lhs;
    private final XmlOutput rhs;

    public ForkXmlOutput(XmlOutput lhs, XmlOutput rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, XMLStreamException, IOException {
        this.lhs.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
        this.rhs.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws SAXException, XMLStreamException, IOException {
        this.lhs.endDocument(fragment);
        this.rhs.endDocument(fragment);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(Name name) throws XMLStreamException, IOException {
        this.lhs.beginStartTag(name);
        this.rhs.beginStartTag(name);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws XMLStreamException, IOException {
        this.lhs.attribute(name, value);
        this.rhs.attribute(name, value);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(Name name) throws SAXException, XMLStreamException, IOException {
        this.lhs.endTag(name);
        this.rhs.endTag(name);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(int prefix, String localName) throws XMLStreamException, IOException {
        this.lhs.beginStartTag(prefix, localName);
        this.rhs.beginStartTag(prefix, localName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) throws XMLStreamException, IOException {
        this.lhs.attribute(prefix, localName, value);
        this.rhs.attribute(prefix, localName, value);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws SAXException, IOException {
        this.lhs.endStartTag();
        this.rhs.endStartTag();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(int prefix, String localName) throws SAXException, XMLStreamException, IOException {
        this.lhs.endTag(prefix, localName);
        this.rhs.endTag(prefix, localName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(String value, boolean needsSeparatingWhitespace) throws SAXException, XMLStreamException, IOException {
        this.lhs.text(value, needsSeparatingWhitespace);
        this.rhs.text(value, needsSeparatingWhitespace);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needsSeparatingWhitespace) throws SAXException, XMLStreamException, IOException {
        this.lhs.text(value, needsSeparatingWhitespace);
        this.rhs.text(value, needsSeparatingWhitespace);
    }
}
