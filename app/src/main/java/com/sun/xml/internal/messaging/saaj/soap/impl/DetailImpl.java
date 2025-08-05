package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/DetailImpl.class */
public abstract class DetailImpl extends FaultElementImpl implements Detail {
    protected abstract DetailEntry createDetailEntry(Name name);

    protected abstract DetailEntry createDetailEntry(QName qName);

    public DetailImpl(SOAPDocumentImpl ownerDoc, NameImpl detailName) {
        super(ownerDoc, detailName);
    }

    @Override // javax.xml.soap.Detail
    public DetailEntry addDetailEntry(Name name) throws SOAPException {
        DetailEntry entry = createDetailEntry(name);
        addNode(entry);
        return entry;
    }

    @Override // javax.xml.soap.Detail
    public DetailEntry addDetailEntry(QName qname) throws SOAPException {
        DetailEntry entry = createDetailEntry(qname);
        addNode(entry);
        return entry;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(Name name) throws SOAPException {
        return addDetailEntry(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(QName name) throws SOAPException {
        return addDetailEntry(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement convertToSoapElement(Element element) {
        if (element instanceof DetailEntry) {
            return (SOAPElement) element;
        }
        return replaceElementWithSOAPElement(element, (ElementImpl) createDetailEntry(NameImpl.copyElementName(element)));
    }

    @Override // javax.xml.soap.Detail
    public Iterator getDetailEntries() {
        return new Iterator() { // from class: com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl.1
            Iterator eachNode;
            SOAPElement next = null;
            SOAPElement last = null;

            {
                this.eachNode = DetailImpl.this.getChildElementNodes();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.next == null) {
                    while (this.eachNode.hasNext()) {
                        this.next = (SOAPElement) this.eachNode.next();
                        if (this.next instanceof DetailEntry) {
                            break;
                        }
                        this.next = null;
                    }
                }
                return this.next != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                this.last = this.next;
                this.next = null;
                return this.last;
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.last == null) {
                    throw new IllegalStateException();
                }
                SOAPElement target = this.last;
                DetailImpl.this.removeChild(target);
                this.last = null;
            }
        };
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl
    protected boolean isStandardFaultElement() {
        return true;
    }
}
