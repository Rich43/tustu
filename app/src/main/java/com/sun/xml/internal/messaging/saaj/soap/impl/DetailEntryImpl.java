package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import javax.xml.namespace.QName;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/DetailEntryImpl.class */
public abstract class DetailEntryImpl extends ElementImpl implements DetailEntry {
    public DetailEntryImpl(SOAPDocumentImpl ownerDoc, Name qname) {
        super(ownerDoc, qname);
    }

    public DetailEntryImpl(SOAPDocumentImpl ownerDoc, QName qname) {
        super(ownerDoc, qname);
    }
}
