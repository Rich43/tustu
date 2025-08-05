package com.sun.xml.internal.ws.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.WSBinding;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
import com.sun.xml.internal.ws.binding.SOAPBindingImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/message/HeaderList.class */
public class HeaderList extends ArrayList<Header> implements MessageHeaders {
    private static final long serialVersionUID = -6358045781349627237L;
    private int understoodBits;
    private BitSet moreUnderstoodBits;
    private SOAPVersion soapVersion;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !HeaderList.class.desiredAssertionStatus();
    }

    @Deprecated
    public HeaderList() {
        this.moreUnderstoodBits = null;
    }

    public HeaderList(SOAPVersion soapVersion) {
        this.moreUnderstoodBits = null;
        this.soapVersion = soapVersion;
    }

    public HeaderList(HeaderList that) {
        super(that);
        this.moreUnderstoodBits = null;
        this.understoodBits = that.understoodBits;
        if (that.moreUnderstoodBits != null) {
            this.moreUnderstoodBits = (BitSet) that.moreUnderstoodBits.clone();
        }
    }

    public HeaderList(MessageHeaders that) {
        super(that.asList());
        this.moreUnderstoodBits = null;
        if (that instanceof HeaderList) {
            HeaderList hThat = (HeaderList) that;
            this.understoodBits = hThat.understoodBits;
            if (hThat.moreUnderstoodBits != null) {
                this.moreUnderstoodBits = (BitSet) hThat.moreUnderstoodBits.clone();
                return;
            }
            return;
        }
        Set<QName> understood = that.getUnderstoodHeaders();
        if (understood != null) {
            for (QName qname : understood) {
                understood(qname);
            }
        }
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return super.size();
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean hasHeaders() {
        return !isEmpty();
    }

    @Deprecated
    public void addAll(Header... headers) {
        addAll(Arrays.asList(headers));
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public Header get(int index) {
        return (Header) super.get(index);
    }

    public void understood(int index) {
        if (index >= size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if (index < 32) {
            this.understoodBits |= 1 << index;
            return;
        }
        if (this.moreUnderstoodBits == null) {
            this.moreUnderstoodBits = new BitSet();
        }
        this.moreUnderstoodBits.set(index - 32);
    }

    public boolean isUnderstood(int index) {
        if (index >= size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        if (index < 32) {
            return this.understoodBits == (this.understoodBits | (1 << index));
        }
        if (this.moreUnderstoodBits == null) {
            return false;
        }
        return this.moreUnderstoodBits.get(index - 32);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void understood(@NotNull Header header) {
        int sz = size();
        for (int i2 = 0; i2 < sz; i2++) {
            if (get(i2) == header) {
                understood(i2);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    @Nullable
    public Header get(@NotNull String nsUri, @NotNull String localName, boolean markAsUnderstood) {
        int len = size();
        for (int i2 = 0; i2 < len; i2++) {
            Header h2 = get(i2);
            if (h2.getLocalPart().equals(localName) && h2.getNamespaceURI().equals(nsUri)) {
                if (markAsUnderstood) {
                    understood(i2);
                }
                return h2;
            }
        }
        return null;
    }

    public Header get(String nsUri, String localName) {
        return get(nsUri, localName, true);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    @Nullable
    public Header get(@NotNull QName name, boolean markAsUnderstood) {
        return get(name.getNamespaceURI(), name.getLocalPart(), markAsUnderstood);
    }

    @Nullable
    public Header get(@NotNull QName name) {
        return get(name, true);
    }

    public Iterator<Header> getHeaders(String nsUri, String localName) {
        return getHeaders(nsUri, localName, true);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    @NotNull
    public Iterator<Header> getHeaders(@NotNull final String nsUri, @NotNull final String localName, final boolean markAsUnderstood) {
        return new Iterator<Header>() { // from class: com.sun.xml.internal.ws.api.message.HeaderList.1
            int idx = 0;
            Header next;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !HeaderList.class.desiredAssertionStatus();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.next == null) {
                    fetch();
                }
                return this.next != null;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Header next() {
                if (this.next == null) {
                    fetch();
                    if (this.next == null) {
                        throw new NoSuchElementException();
                    }
                }
                if (markAsUnderstood) {
                    if (!$assertionsDisabled && HeaderList.this.get(this.idx - 1) != this.next) {
                        throw new AssertionError();
                    }
                    HeaderList.this.understood(this.idx - 1);
                }
                Header r2 = this.next;
                this.next = null;
                return r2;
            }

            private void fetch() {
                while (this.idx < HeaderList.this.size()) {
                    HeaderList headerList = HeaderList.this;
                    int i2 = this.idx;
                    this.idx = i2 + 1;
                    Header h2 = headerList.get(i2);
                    if (h2.getLocalPart().equals(localName) && h2.getNamespaceURI().equals(nsUri)) {
                        this.next = h2;
                        return;
                    }
                }
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    @NotNull
    public Iterator<Header> getHeaders(@NotNull QName headerName, boolean markAsUnderstood) {
        return getHeaders(headerName.getNamespaceURI(), headerName.getLocalPart(), markAsUnderstood);
    }

    @NotNull
    public Iterator<Header> getHeaders(@NotNull String nsUri) {
        return getHeaders(nsUri, true);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    @NotNull
    public Iterator<Header> getHeaders(@NotNull final String nsUri, final boolean markAsUnderstood) {
        return new Iterator<Header>() { // from class: com.sun.xml.internal.ws.api.message.HeaderList.2
            int idx = 0;
            Header next;
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !HeaderList.class.desiredAssertionStatus();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.next == null) {
                    fetch();
                }
                return this.next != null;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Header next() {
                if (this.next == null) {
                    fetch();
                    if (this.next == null) {
                        throw new NoSuchElementException();
                    }
                }
                if (markAsUnderstood) {
                    if (!$assertionsDisabled && HeaderList.this.get(this.idx - 1) != this.next) {
                        throw new AssertionError();
                    }
                    HeaderList.this.understood(this.idx - 1);
                }
                Header r2 = this.next;
                this.next = null;
                return r2;
            }

            private void fetch() {
                while (this.idx < HeaderList.this.size()) {
                    HeaderList headerList = HeaderList.this;
                    int i2 = this.idx;
                    this.idx = i2 + 1;
                    Header h2 = headerList.get(i2);
                    if (h2.getNamespaceURI().equals(nsUri)) {
                        this.next = h2;
                        return;
                    }
                }
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public String getTo(AddressingVersion av2, SOAPVersion sv) {
        return AddressingUtils.getTo(this, av2, sv);
    }

    public String getAction(@NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        return AddressingUtils.getAction(this, av2, sv);
    }

    public WSEndpointReference getReplyTo(@NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        return AddressingUtils.getReplyTo(this, av2, sv);
    }

    public WSEndpointReference getFaultTo(@NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        return AddressingUtils.getFaultTo(this, av2, sv);
    }

    public String getMessageID(@NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        return AddressingUtils.getMessageID(this, av2, sv);
    }

    public String getRelatesTo(@NotNull AddressingVersion av2, @NotNull SOAPVersion sv) {
        return AddressingUtils.getRelatesTo(this, av2, sv);
    }

    public void fillRequestAddressingHeaders(Packet packet, AddressingVersion av2, SOAPVersion sv, boolean oneway, String action, boolean mustUnderstand) {
        AddressingUtils.fillRequestAddressingHeaders(this, packet, av2, sv, oneway, action, mustUnderstand);
    }

    public void fillRequestAddressingHeaders(Packet packet, AddressingVersion av2, SOAPVersion sv, boolean oneway, String action) {
        AddressingUtils.fillRequestAddressingHeaders(this, packet, av2, sv, oneway, action);
    }

    public void fillRequestAddressingHeaders(WSDLPort wsdlPort, @NotNull WSBinding binding, Packet packet) {
        AddressingUtils.fillRequestAddressingHeaders(this, wsdlPort, binding, packet);
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Header header) {
        return super.add((HeaderList) header);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    @Nullable
    public Header remove(@NotNull String nsUri, @NotNull String localName) {
        int len = size();
        for (int i2 = 0; i2 < len; i2++) {
            Header h2 = get(i2);
            if (h2.getLocalPart().equals(localName) && h2.getNamespaceURI().equals(nsUri)) {
                return remove(i2);
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean addOrReplace(Header header) {
        for (int i2 = 0; i2 < size(); i2++) {
            Header hdr = get(i2);
            if (hdr.getNamespaceURI().equals(header.getNamespaceURI()) && hdr.getLocalPart().equals(header.getLocalPart())) {
                removeInternal(i2);
                addInternal(i2, header);
                return true;
            }
        }
        return add(header);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void replace(Header old, Header header) {
        for (int i2 = 0; i2 < size(); i2++) {
            Header hdr = get(i2);
            if (hdr.getNamespaceURI().equals(header.getNamespaceURI()) && hdr.getLocalPart().equals(header.getLocalPart())) {
                removeInternal(i2);
                addInternal(i2, header);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    protected void addInternal(int index, Header header) {
        super.add(index, header);
    }

    protected Header removeInternal(int index) {
        return (Header) super.remove(index);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    @Nullable
    public Header remove(@NotNull QName name) {
        return remove(name.getNamespaceURI(), name.getLocalPart());
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public Header remove(int index) {
        removeUnderstoodBit(index);
        return (Header) super.remove(index);
    }

    private void removeUnderstoodBit(int index) {
        if (!$assertionsDisabled && index >= size()) {
            throw new AssertionError();
        }
        if (index < 32) {
            int shiftedUpperBits = (this.understoodBits >>> ((-31) + index)) << index;
            int lowerBits = ((this.understoodBits << (-index)) >>> (31 - index)) >>> 1;
            this.understoodBits = shiftedUpperBits | lowerBits;
            if (this.moreUnderstoodBits != null && this.moreUnderstoodBits.cardinality() > 0) {
                if (this.moreUnderstoodBits.get(0)) {
                    this.understoodBits |= Integer.MIN_VALUE;
                }
                this.moreUnderstoodBits.clear(0);
                int iNextSetBit = this.moreUnderstoodBits.nextSetBit(1);
                while (true) {
                    int i2 = iNextSetBit;
                    if (i2 <= 0) {
                        break;
                    }
                    this.moreUnderstoodBits.set(i2 - 1);
                    this.moreUnderstoodBits.clear(i2);
                    iNextSetBit = this.moreUnderstoodBits.nextSetBit(i2 + 1);
                }
            }
        } else if (this.moreUnderstoodBits != null && this.moreUnderstoodBits.cardinality() > 0) {
            int index2 = index - 32;
            this.moreUnderstoodBits.clear(index2);
            int iNextSetBit2 = this.moreUnderstoodBits.nextSetBit(index2);
            while (true) {
                int i3 = iNextSetBit2;
                if (i3 < 1) {
                    break;
                }
                this.moreUnderstoodBits.set(i3 - 1);
                this.moreUnderstoodBits.clear(i3);
                iNextSetBit2 = this.moreUnderstoodBits.nextSetBit(i3 + 1);
            }
        }
        if (size() - 1 <= 33 && this.moreUnderstoodBits != null) {
            this.moreUnderstoodBits = null;
        }
    }

    @Override // java.util.ArrayList, java.util.AbstractCollection, java.util.Collection, java.util.Set
    public boolean remove(Object o2) {
        if (o2 != null) {
            for (int index = 0; index < size(); index++) {
                if (o2.equals(get(index))) {
                    remove(index);
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    public Header remove(Header h2) {
        if (remove((Object) h2)) {
            return h2;
        }
        return null;
    }

    public static HeaderList copy(MessageHeaders original) {
        if (original == null) {
            return null;
        }
        return new HeaderList(original);
    }

    public static HeaderList copy(HeaderList original) {
        return copy((MessageHeaders) original);
    }

    public void readResponseAddressingHeaders(WSDLPort wsdlPort, WSBinding binding) {
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void understood(QName name) {
        get(name, true);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public void understood(String nsUri, String localName) {
        get(nsUri, localName, true);
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Set<QName> getUnderstoodHeaders() {
        Set<QName> understoodHdrs = new HashSet<>();
        for (int i2 = 0; i2 < size(); i2++) {
            if (isUnderstood(i2)) {
                Header header = get(i2);
                understoodHdrs.add(new QName(header.getNamespaceURI(), header.getLocalPart()));
            }
        }
        return understoodHdrs;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean isUnderstood(Header header) {
        return isUnderstood(header.getNamespaceURI(), header.getLocalPart());
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean isUnderstood(String nsUri, String localName) {
        for (int i2 = 0; i2 < size(); i2++) {
            Header h2 = get(i2);
            if (h2.getLocalPart().equals(localName) && h2.getNamespaceURI().equals(nsUri)) {
                return isUnderstood(i2);
            }
        }
        return false;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public boolean isUnderstood(QName name) {
        return isUnderstood(name.getNamespaceURI(), name.getLocalPart());
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Set<QName> getNotUnderstoodHeaders(Set<String> roles, Set<QName> knownHeaders, WSBinding binding) {
        Set<QName> notUnderstoodHeaders = null;
        if (roles == null) {
            roles = new HashSet();
        }
        SOAPVersion effectiveSoapVersion = getEffectiveSOAPVersion(binding);
        roles.add(effectiveSoapVersion.implicitRole);
        for (int i2 = 0; i2 < size(); i2++) {
            if (!isUnderstood(i2)) {
                Header header = get(i2);
                if (!header.isIgnorable(effectiveSoapVersion, roles)) {
                    QName qName = new QName(header.getNamespaceURI(), header.getLocalPart());
                    if (binding == null) {
                        if (notUnderstoodHeaders == null) {
                            notUnderstoodHeaders = new HashSet<>();
                        }
                        notUnderstoodHeaders.add(qName);
                    } else if ((binding instanceof SOAPBindingImpl) && !((SOAPBindingImpl) binding).understandsHeader(qName) && !knownHeaders.contains(qName)) {
                        if (notUnderstoodHeaders == null) {
                            notUnderstoodHeaders = new HashSet<>();
                        }
                        notUnderstoodHeaders.add(qName);
                    }
                }
            }
        }
        return notUnderstoodHeaders;
    }

    private SOAPVersion getEffectiveSOAPVersion(WSBinding binding) {
        SOAPVersion mySOAPVersion = this.soapVersion != null ? this.soapVersion : binding.getSOAPVersion();
        if (mySOAPVersion == null) {
            mySOAPVersion = SOAPVersion.SOAP_11;
        }
        return mySOAPVersion;
    }

    public void setSoapVersion(SOAPVersion soapVersion) {
        this.soapVersion = soapVersion;
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public Iterator<Header> getHeaders() {
        return iterator();
    }

    @Override // com.sun.xml.internal.ws.api.message.MessageHeaders
    public List<Header> asList() {
        return this;
    }
}
