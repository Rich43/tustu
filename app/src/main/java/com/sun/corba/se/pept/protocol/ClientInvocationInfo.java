package com.sun.corba.se.pept.protocol;

import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/pept/protocol/ClientInvocationInfo.class */
public interface ClientInvocationInfo {
    Iterator getContactInfoListIterator();

    void setContactInfoListIterator(Iterator it);

    boolean isRetryInvocation();

    void setIsRetryInvocation(boolean z2);

    int getEntryCount();

    void incrementEntryCount();

    void decrementEntryCount();

    void setClientRequestDispatcher(ClientRequestDispatcher clientRequestDispatcher);

    ClientRequestDispatcher getClientRequestDispatcher();

    void setMessageMediator(MessageMediator messageMediator);

    MessageMediator getMessageMediator();
}
