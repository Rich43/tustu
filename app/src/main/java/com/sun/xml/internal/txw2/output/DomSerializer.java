package com.sun.xml.internal.txw2.output;

import com.sun.xml.internal.txw2.TxwException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/output/DomSerializer.class */
public class DomSerializer implements XmlSerializer {
    private final SaxSerializer serializer;

    public DomSerializer(Node node) {
        Dom2SaxAdapter adapter = new Dom2SaxAdapter(node);
        this.serializer = new SaxSerializer(adapter, adapter, false);
    }

    public DomSerializer(DOMResult domResult) {
        Node node = domResult.getNode();
        if (node == null) {
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setNamespaceAware(true);
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.newDocument();
                domResult.setNode(doc);
                this.serializer = new SaxSerializer(new Dom2SaxAdapter(doc), null, false);
                return;
            } catch (ParserConfigurationException pce) {
                throw new TxwException(pce);
            }
        }
        this.serializer = new SaxSerializer(new Dom2SaxAdapter(node), null, false);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void startDocument() {
        this.serializer.startDocument();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void beginStartTag(String uri, String localName, String prefix) {
        this.serializer.beginStartTag(uri, localName, prefix);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
        this.serializer.writeAttribute(uri, localName, prefix, value);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void writeXmlns(String prefix, String uri) {
        this.serializer.writeXmlns(prefix, uri);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endStartTag(String uri, String localName, String prefix) {
        this.serializer.endStartTag(uri, localName, prefix);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endTag() {
        this.serializer.endTag();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void text(StringBuilder text) {
        this.serializer.text(text);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void cdata(StringBuilder text) {
        this.serializer.cdata(text);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void comment(StringBuilder comment) {
        this.serializer.comment(comment);
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void endDocument() {
        this.serializer.endDocument();
    }

    @Override // com.sun.xml.internal.txw2.output.XmlSerializer
    public void flush() {
    }
}
