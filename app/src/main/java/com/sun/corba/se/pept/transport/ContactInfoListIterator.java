package com.sun.corba.se.pept.transport;

import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/pept/transport/ContactInfoListIterator.class */
public interface ContactInfoListIterator extends Iterator {
    ContactInfoList getContactInfoList();

    void reportSuccess(ContactInfo contactInfo);

    boolean reportException(ContactInfo contactInfo, RuntimeException runtimeException);

    RuntimeException getFailureException();
}
