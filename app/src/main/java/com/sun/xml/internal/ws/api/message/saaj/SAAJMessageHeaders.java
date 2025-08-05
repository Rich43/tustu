package com.sun.xml.internal.ws.api.message.saaj;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.message.Header;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
import com.sun.xml.internal.ws.message.saaj.SAAJHeader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SAAJMessageHeaders.class */
public class SAAJMessageHeaders implements MessageHeaders {
    SOAPMessage sm;
    Map<SOAPHeaderElement, Header> nonSAAJHeaders;
    Map<QName, Integer> notUnderstoodCount;
    SOAPVersion soapVersion;
    private Set<QName> understoodHeaders;

    public SAAJMessageHeaders(SOAPMessage sm, SOAPVersion version) {
        this.sm = sm;
        this.soapVersion = version;
        initHeaderUnderstanding();
    }

    private void initHeaderUnderstanding() {
        SOAPHeader soapHeader = ensureSOAPHeader();
        if (soapHeader == null) {
            return;
        }
        Iterator allHeaders = soapHeader.examineAllHeaderElements();
        while (allHeaders.hasNext()) {
            SOAPHeaderElement nextHdrElem = (SOAPHeaderElement) allHeaders.next();
            if (nextHdrElem != null && nextHdrElem.getMustUnderstand()) {
                notUnderstood(nextHdrElem.getElementQName());
            }
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void understood(Header header) {
        understood(header.getNamespaceURI(), header.getLocalPart());
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void understood(String nsUri, String localName) {
        understood(new QName(nsUri, localName));
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void understood(QName qName) {
        if (this.notUnderstoodCount == null) {
            this.notUnderstoodCount = new HashMap();
        }
        Integer count = this.notUnderstoodCount.get(qName);
        if (count != null && count.intValue() > 0) {
            Integer count2 = Integer.valueOf(count.intValue() - 1);
            if (count2.intValue() <= 0) {
                this.notUnderstoodCount.remove(qName);
            } else {
                this.notUnderstoodCount.put(qName, count2);
            }
        }
        if (this.understoodHeaders == null) {
            this.understoodHeaders = new HashSet();
        }
        this.understoodHeaders.add(qName);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean isUnderstood(Header header) {
        return isUnderstood(header.getNamespaceURI(), header.getLocalPart());
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean isUnderstood(String nsUri, String localName) {
        return isUnderstood(new QName(nsUri, localName));
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean isUnderstood(QName name) {
        if (this.understoodHeaders == null) {
            return false;
        }
        return this.understoodHeaders.contains(name);
    }

    public boolean isUnderstood(int index) {
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Header get(String nsUri, String localName, boolean markAsUnderstood) {
        SOAPHeaderElement h2 = find(nsUri, localName);
        if (h2 != null) {
            if (markAsUnderstood) {
                understood(nsUri, localName);
            }
            return new SAAJHeader(h2);
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Header get(QName name, boolean markAsUnderstood) {
        return get(name.getNamespaceURI(), name.getLocalPart(), markAsUnderstood);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Iterator<Header> getHeaders(QName headerName, boolean markAsUnderstood) {
        return getHeaders(headerName.getNamespaceURI(), headerName.getLocalPart(), markAsUnderstood);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Iterator<Header> getHeaders(String nsUri, String localName, boolean markAsUnderstood) {
        SOAPHeader soapHeader = ensureSOAPHeader();
        if (soapHeader == null) {
            return null;
        }
        Iterator allHeaders = soapHeader.examineAllHeaderElements();
        if (markAsUnderstood) {
            List<Header> headers = new ArrayList<>();
            while (allHeaders.hasNext()) {
                SOAPHeaderElement nextHdr = (SOAPHeaderElement) allHeaders.next();
                if (nextHdr != null && nextHdr.getNamespaceURI().equals(nsUri) && (localName == null || nextHdr.getLocalName().equals(localName))) {
                    understood(nextHdr.getNamespaceURI(), nextHdr.getLocalName());
                    headers.add(new SAAJHeader(nextHdr));
                }
            }
            return headers.iterator();
        }
        return new HeaderReadIterator(allHeaders, nsUri, localName);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Iterator<Header> getHeaders(String nsUri, boolean markAsUnderstood) {
        return getHeaders(nsUri, null, markAsUnderstood);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean add(Header header) {
        try {
            header.writeTo(this.sm);
            notUnderstood(new QName(header.getNamespaceURI(), header.getLocalPart()));
            if (isNonSAAJHeader(header)) {
                addNonSAAJHeader(find(header.getNamespaceURI(), header.getLocalPart()), header);
                return true;
            }
            return true;
        } catch (SOAPException e2) {
            return false;
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Header remove(QName name) {
        return remove(name.getNamespaceURI(), name.getLocalPart());
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Header remove(String nsUri, String localName) {
        SOAPHeaderElement headerElem;
        SOAPHeader soapHeader = ensureSOAPHeader();
        if (soapHeader == null || (headerElem = find(nsUri, localName)) == null) {
            return null;
        }
        SOAPHeaderElement headerElem2 = (SOAPHeaderElement) soapHeader.removeChild(headerElem);
        removeNonSAAJHeader(headerElem2);
        QName hdrName = nsUri == null ? new QName(localName) : new QName(nsUri, localName);
        if (this.understoodHeaders != null) {
            this.understoodHeaders.remove(hdrName);
        }
        removeNotUnderstood(hdrName);
        return new SAAJHeader(headerElem2);
    }

    private void removeNotUnderstood(QName hdrName) {
        Integer notUnderstood;
        if (this.notUnderstoodCount != null && (notUnderstood = this.notUnderstoodCount.get(hdrName)) != null) {
            int intNotUnderstood = notUnderstood.intValue();
            if (intNotUnderstood - 1 <= 0) {
                this.notUnderstoodCount.remove(hdrName);
            }
        }
    }

    private SOAPHeaderElement find(QName qName) {
        return find(qName.getNamespaceURI(), qName.getLocalPart());
    }

    private SOAPHeaderElement find(String nsUri, String localName) {
        SOAPHeader soapHeader = ensureSOAPHeader();
        if (soapHeader == null) {
            return null;
        }
        Iterator allHeaders = soapHeader.examineAllHeaderElements();
        while (allHeaders.hasNext()) {
            SOAPHeaderElement nextHdrElem = (SOAPHeaderElement) allHeaders.next();
            if (nextHdrElem.getNamespaceURI().equals(nsUri) && nextHdrElem.getLocalName().equals(localName)) {
                return nextHdrElem;
            }
        }
        return null;
    }

    private void notUnderstood(QName qName) {
        if (this.notUnderstoodCount == null) {
            this.notUnderstoodCount = new HashMap();
        }
        Integer count = this.notUnderstoodCount.get(qName);
        if (count == null) {
            this.notUnderstoodCount.put(qName, 1);
        } else {
            this.notUnderstoodCount.put(qName, Integer.valueOf(count.intValue() + 1));
        }
        if (this.understoodHeaders != null) {
            this.understoodHeaders.remove(qName);
        }
    }

    private SOAPHeader ensureSOAPHeader() {
        try {
            SOAPHeader header = this.sm.getSOAPPart().getEnvelope().getHeader();
            if (header != null) {
                return header;
            }
            return this.sm.getSOAPPart().getEnvelope().addHeader();
        } catch (Exception e2) {
            return null;
        }
    }

    private boolean isNonSAAJHeader(Header header) {
        return !(header instanceof SAAJHeader);
    }

    private void addNonSAAJHeader(SOAPHeaderElement headerElem, Header header) {
        if (this.nonSAAJHeaders == null) {
            this.nonSAAJHeaders = new HashMap();
        }
        this.nonSAAJHeaders.put(headerElem, header);
    }

    private void removeNonSAAJHeader(SOAPHeaderElement headerElem) {
        if (this.nonSAAJHeaders != null) {
            this.nonSAAJHeaders.remove(headerElem);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean addOrReplace(Header header) {
        remove(header.getNamespaceURI(), header.getLocalPart());
        return add(header);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void replace(Header old, Header header) {
        if (remove(old.getNamespaceURI(), old.getLocalPart()) == null) {
            throw new IllegalArgumentException();
        }
        add(header);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Set<QName> getUnderstoodHeaders() {
        return this.understoodHeaders;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Set<QName> getNotUnderstoodHeaders(Set<String> roles, Set<QName> knownHeaders, WSBinding binding) {
        Set<QName> notUnderstoodHeaderNames = new HashSet<>();
        if (this.notUnderstoodCount == null) {
            return notUnderstoodHeaderNames;
        }
        for (QName headerName : this.notUnderstoodCount.keySet()) {
            int count = this.notUnderstoodCount.get(headerName).intValue();
            if (count > 0) {
                SOAPHeaderElement hdrElem = find(headerName);
                if (hdrElem.getMustUnderstand()) {
                    SAAJHeader hdr = new SAAJHeader(hdrElem);
                    boolean understood = false;
                    if (roles != null) {
                        understood = !roles.contains(hdr.getRole(this.soapVersion));
                    }
                    if (!understood) {
                        if (binding != null && (binding instanceof SOAPBindingImpl)) {
                            understood = ((SOAPBindingImpl) binding).understandsHeader(headerName);
                            if (!understood && knownHeaders != null && knownHeaders.contains(headerName)) {
                                understood = true;
                            }
                        }
                        if (!understood) {
                            notUnderstoodHeaderNames.add(headerName);
                        }
                    }
                }
            }
        }
        return notUnderstoodHeaderNames;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Iterator<Header> getHeaders() {
        SOAPHeader soapHeader = ensureSOAPHeader();
        if (soapHeader == null) {
            return null;
        }
        Iterator allHeaders = soapHeader.examineAllHeaderElements();
        return new HeaderReadIterator(allHeaders, null, null);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/saaj/SAAJMessageHeaders$HeaderReadIterator.class */
    private static class HeaderReadIterator implements Iterator<Header> {
        SOAPHeaderElement current;
        Iterator soapHeaders;
        String myNsUri;
        String myLocalName;

        public HeaderReadIterator(Iterator allHeaders, String nsUri, String localName) {
            this.soapHeaders = allHeaders;
            this.myNsUri = nsUri;
            this.myLocalName = localName;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.current == null) {
                advance();
            }
            return this.current != null;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Header next() {
            if (!hasNext() || this.current == null) {
                return null;
            }
            SAAJHeader ret = new SAAJHeader(this.current);
            this.current = null;
            return ret;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void advance() {
            while (this.soapHeaders.hasNext()) {
                SOAPHeaderElement nextHdr = (SOAPHeaderElement) this.soapHeaders.next();
                if (nextHdr != null && (this.myNsUri == null || nextHdr.getNamespaceURI().equals(this.myNsUri))) {
                    if (this.myLocalName == null || nextHdr.getLocalName().equals(this.myLocalName)) {
                        this.current = nextHdr;
                        return;
                    }
                }
            }
            this.current = null;
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean hasHeaders() {
        SOAPHeader soapHeader = ensureSOAPHeader();
        if (soapHeader == null) {
            return false;
        }
        Iterator allHeaders = soapHeader.examineAllHeaderElements();
        return allHeaders.hasNext();
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public List<Header> asList() {
        SOAPHeader soapHeader = ensureSOAPHeader();
        if (soapHeader == null) {
            return Collections.emptyList();
        }
        Iterator allHeaders = soapHeader.examineAllHeaderElements();
        List<Header> headers = new ArrayList<>();
        while (allHeaders.hasNext()) {
            SOAPHeaderElement nextHdr = (SOAPHeaderElement) allHeaders.next();
            headers.add(new SAAJHeader(nextHdr));
        }
        return headers;
    }
}
