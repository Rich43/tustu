package com.sun.xml.internal.ws.encoding;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/TagInfoset.class */
public final class TagInfoset {

    @NotNull
    public final String[] ns;

    @NotNull
    public final AttributesImpl atts;

    @Nullable
    public final String prefix;

    @Nullable
    public final String nsUri;

    @NotNull
    public final String localName;

    @Nullable
    private String qname;
    private static final String[] EMPTY_ARRAY = new String[0];
    private static final AttributesImpl EMPTY_ATTRIBUTES = new AttributesImpl();

    public TagInfoset(String nsUri, String localName, String prefix, AttributesImpl atts, String... ns) {
        this.nsUri = nsUri;
        this.prefix = prefix;
        this.localName = localName;
        this.atts = atts;
        this.ns = ns;
    }

    public TagInfoset(XMLStreamReader reader) {
        String string;
        this.prefix = reader.getPrefix();
        this.nsUri = reader.getNamespaceURI();
        this.localName = reader.getLocalName();
        int nsc = reader.getNamespaceCount();
        if (nsc > 0) {
            this.ns = new String[nsc * 2];
            for (int i2 = 0; i2 < nsc; i2++) {
                this.ns[i2 * 2] = fixNull(reader.getNamespacePrefix(i2));
                this.ns[(i2 * 2) + 1] = fixNull(reader.getNamespaceURI(i2));
            }
        } else {
            this.ns = EMPTY_ARRAY;
        }
        int ac2 = reader.getAttributeCount();
        if (ac2 > 0) {
            this.atts = new AttributesImpl();
            StringBuilder sb = new StringBuilder();
            for (int i3 = 0; i3 < ac2; i3++) {
                sb.setLength(0);
                String prefix = reader.getAttributePrefix(i3);
                String localName = reader.getAttributeLocalName(i3);
                if (prefix != null && prefix.length() != 0) {
                    sb.append(prefix);
                    sb.append(CallSiteDescriptor.TOKEN_DELIMITER);
                    sb.append(localName);
                    string = sb.toString();
                } else {
                    string = localName;
                }
                String qname = string;
                this.atts.addAttribute(fixNull(reader.getAttributeNamespace(i3)), localName, qname, reader.getAttributeType(i3), reader.getAttributeValue(i3));
            }
            return;
        }
        this.atts = EMPTY_ATTRIBUTES;
    }

    public void writeStart(ContentHandler contentHandler) throws SAXException {
        for (int i2 = 0; i2 < this.ns.length; i2 += 2) {
            contentHandler.startPrefixMapping(fixNull(this.ns[i2]), fixNull(this.ns[i2 + 1]));
        }
        contentHandler.startElement(fixNull(this.nsUri), this.localName, getQName(), this.atts);
    }

    public void writeEnd(ContentHandler contentHandler) throws SAXException {
        contentHandler.endElement(fixNull(this.nsUri), this.localName, getQName());
        for (int i2 = this.ns.length - 2; i2 >= 0; i2 -= 2) {
            contentHandler.endPrefixMapping(fixNull(this.ns[i2]));
        }
    }

    public void writeStart(XMLStreamWriter w2) throws XMLStreamException {
        if (this.prefix == null) {
            if (this.nsUri == null) {
                w2.writeStartElement(this.localName);
            } else {
                w2.writeStartElement("", this.localName, this.nsUri);
            }
        } else {
            w2.writeStartElement(this.prefix, this.localName, this.nsUri);
        }
        for (int i2 = 0; i2 < this.ns.length; i2 += 2) {
            w2.writeNamespace(this.ns[i2], this.ns[i2 + 1]);
        }
        for (int i3 = 0; i3 < this.atts.getLength(); i3++) {
            String nsUri = this.atts.getURI(i3);
            if (nsUri == null || nsUri.length() == 0) {
                w2.writeAttribute(this.atts.getLocalName(i3), this.atts.getValue(i3));
            } else {
                String rawName = this.atts.getQName(i3);
                String prefix = rawName.substring(0, rawName.indexOf(58));
                w2.writeAttribute(prefix, nsUri, this.atts.getLocalName(i3), this.atts.getValue(i3));
            }
        }
    }

    private String getQName() {
        if (this.qname != null) {
            return this.qname;
        }
        StringBuilder sb = new StringBuilder();
        if (this.prefix != null) {
            sb.append(this.prefix);
            sb.append(':');
            sb.append(this.localName);
            this.qname = sb.toString();
        } else {
            this.qname = this.localName;
        }
        return this.qname;
    }

    private static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }

    public String getNamespaceURI(String prefix) {
        int size = this.ns.length / 2;
        for (int i2 = 0; i2 < size; i2++) {
            String p2 = this.ns[i2 * 2];
            String n2 = this.ns[(i2 * 2) + 1];
            if (prefix.equals(p2)) {
                return n2;
            }
        }
        return null;
    }

    public String getPrefix(String namespaceURI) {
        int size = this.ns.length / 2;
        for (int i2 = 0; i2 < size; i2++) {
            String p2 = this.ns[i2 * 2];
            String n2 = this.ns[(i2 * 2) + 1];
            if (namespaceURI.equals(n2)) {
                return p2;
            }
        }
        return null;
    }

    public List<String> allPrefixes(String namespaceURI) {
        int size = this.ns.length / 2;
        List<String> l2 = new ArrayList<>();
        for (int i2 = 0; i2 < size; i2++) {
            String p2 = this.ns[i2 * 2];
            String n2 = this.ns[(i2 * 2) + 1];
            if (namespaceURI.equals(n2)) {
                l2.add(p2);
            }
        }
        return l2;
    }
}
