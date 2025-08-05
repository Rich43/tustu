package com.sun.xml.internal.ws.message.source;

import com.sun.xml.internal.ws.message.RootElementSniffer;
import com.sun.xml.internal.ws.streaming.SourceReaderFactory;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/source/SourceUtils.class */
final class SourceUtils {
    int srcType;
    private static final int domSource = 1;
    private static final int streamSource = 2;
    private static final int saxSource = 4;

    public SourceUtils(Source src) {
        if (src instanceof StreamSource) {
            this.srcType = 2;
        } else if (src instanceof DOMSource) {
            this.srcType = 1;
        } else if (src instanceof SAXSource) {
            this.srcType = 4;
        }
    }

    public boolean isDOMSource() {
        return (this.srcType & 1) == 1;
    }

    public boolean isStreamSource() {
        return (this.srcType & 2) == 2;
    }

    public boolean isSaxSource() {
        return (this.srcType & 4) == 4;
    }

    public QName sniff(Source src) {
        return sniff(src, new RootElementSniffer());
    }

    public QName sniff(Source src, RootElementSniffer sniffer) {
        String localName = null;
        String namespaceUri = null;
        if (isDOMSource()) {
            DOMSource domSrc = (DOMSource) src;
            Node n2 = domSrc.getNode();
            if (n2.getNodeType() == 9) {
                n2 = ((Document) n2).getDocumentElement();
            }
            localName = n2.getLocalName();
            namespaceUri = n2.getNamespaceURI();
        } else if (isSaxSource()) {
            SAXSource saxSrc = (SAXSource) src;
            SAXResult saxResult = new SAXResult(sniffer);
            try {
                Transformer tr = XmlUtil.newTransformer();
                tr.transform(saxSrc, saxResult);
            } catch (TransformerConfigurationException e2) {
                throw new WebServiceException(e2);
            } catch (TransformerException e3) {
                localName = sniffer.getLocalName();
                namespaceUri = sniffer.getNsUri();
            }
        }
        return new QName(namespaceUri, localName);
    }

    public static void serializeSource(Source src, XMLStreamWriter writer) throws XMLStreamException {
        int state;
        XMLStreamReader reader = SourceReaderFactory.createSourceReader(src, true);
        do {
            state = reader.next();
            switch (state) {
                case 1:
                    String uri = reader.getNamespaceURI();
                    String prefix = reader.getPrefix();
                    String localName = reader.getLocalName();
                    if (prefix == null) {
                        if (uri == null) {
                            writer.writeStartElement(localName);
                        } else {
                            writer.writeStartElement(uri, localName);
                        }
                    } else if (prefix.length() > 0) {
                        String writerURI = null;
                        if (writer.getNamespaceContext() != null) {
                            writerURI = writer.getNamespaceContext().getNamespaceURI(prefix);
                        }
                        String writerPrefix = writer.getPrefix(uri);
                        if (declarePrefix(prefix, uri, writerPrefix, writerURI)) {
                            writer.writeStartElement(prefix, localName, uri);
                            writer.setPrefix(prefix, uri != null ? uri : "");
                            writer.writeNamespace(prefix, uri);
                        } else {
                            writer.writeStartElement(prefix, localName, uri);
                        }
                    } else {
                        writer.writeStartElement(prefix, localName, uri);
                    }
                    int n2 = reader.getNamespaceCount();
                    for (int i2 = 0; i2 < n2; i2++) {
                        String nsPrefix = reader.getNamespacePrefix(i2);
                        if (nsPrefix == null) {
                            nsPrefix = "";
                        }
                        String writerURI2 = null;
                        if (writer.getNamespaceContext() != null) {
                            writerURI2 = writer.getNamespaceContext().getNamespaceURI(nsPrefix);
                        }
                        String readerURI = reader.getNamespaceURI(i2);
                        if (writerURI2 == null || nsPrefix.length() == 0 || prefix.length() == 0 || (!nsPrefix.equals(prefix) && !writerURI2.equals(readerURI))) {
                            writer.setPrefix(nsPrefix, readerURI != null ? readerURI : "");
                            writer.writeNamespace(nsPrefix, readerURI != null ? readerURI : "");
                        }
                    }
                    int n3 = reader.getAttributeCount();
                    for (int i3 = 0; i3 < n3; i3++) {
                        String attrPrefix = reader.getAttributePrefix(i3);
                        String attrURI = reader.getAttributeNamespace(i3);
                        writer.writeAttribute(attrPrefix != null ? attrPrefix : "", attrURI != null ? attrURI : "", reader.getAttributeLocalName(i3), reader.getAttributeValue(i3));
                        setUndeclaredPrefix(attrPrefix, attrURI, writer);
                    }
                    break;
                case 2:
                    writer.writeEndElement();
                    break;
                case 4:
                    writer.writeCharacters(reader.getText());
                    break;
            }
        } while (state != 8);
        reader.close();
    }

    private static void setUndeclaredPrefix(String prefix, String readerURI, XMLStreamWriter writer) throws XMLStreamException {
        String writerURI = null;
        if (writer.getNamespaceContext() != null) {
            writerURI = writer.getNamespaceContext().getNamespaceURI(prefix);
        }
        if (writerURI == null) {
            writer.setPrefix(prefix, readerURI != null ? readerURI : "");
            writer.writeNamespace(prefix, readerURI != null ? readerURI : "");
        }
    }

    private static boolean declarePrefix(String rPrefix, String rUri, String wPrefix, String wUri) {
        if (wUri == null) {
            return true;
        }
        if (wPrefix == null || rPrefix.equals(wPrefix)) {
            if (rUri != null && !wUri.equals(rUri)) {
                return true;
            }
            return false;
        }
        return true;
    }
}
