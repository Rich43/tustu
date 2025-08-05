package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/XmlOutputAbstractImpl.class */
public abstract class XmlOutputAbstractImpl implements XmlOutput {
    protected int[] nsUriIndex2prefixIndex;
    protected NamespaceContextImpl nsContext;
    protected XMLSerializer serializer;

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public abstract void beginStartTag(int i2, String str) throws XMLStreamException, IOException;

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public abstract void attribute(int i2, String str, String str2) throws XMLStreamException, IOException;

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public abstract void endStartTag() throws SAXException, IOException;

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public abstract void endTag(int i2, String str) throws SAXException, XMLStreamException, IOException;

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, XMLStreamException, IOException {
        this.nsUriIndex2prefixIndex = nsUriIndex2prefixIndex;
        this.nsContext = nsContext;
        this.serializer = serializer;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws SAXException, XMLStreamException, IOException {
        this.serializer = null;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(Name name) throws XMLStreamException, IOException {
        beginStartTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws XMLStreamException, IOException {
        short idx = name.nsUriIndex;
        if (idx == -1) {
            attribute(-1, name.localName, value);
        } else {
            attribute(this.nsUriIndex2prefixIndex[idx], name.localName, value);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(Name name) throws SAXException, XMLStreamException, IOException {
        endTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
    }
}
