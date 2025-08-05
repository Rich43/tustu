package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.protocol.MessageMediator;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/CorbaInvocationInfo.class */
public class CorbaInvocationInfo implements ClientInvocationInfo {
    private boolean isRetryInvocation;
    private int entryCount;
    private ORB orb;
    private Iterator contactInfoListIterator;
    private ClientRequestDispatcher clientRequestDispatcher;
    private MessageMediator messageMediator;

    private CorbaInvocationInfo() {
    }

    public CorbaInvocationInfo(ORB orb) {
        this.orb = orb;
        this.isRetryInvocation = false;
        this.entryCount = 0;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public Iterator getContactInfoListIterator() {
        return this.contactInfoListIterator;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public void setContactInfoListIterator(Iterator it) {
        this.contactInfoListIterator = it;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public boolean isRetryInvocation() {
        return this.isRetryInvocation;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public void setIsRetryInvocation(boolean z2) {
        this.isRetryInvocation = z2;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public int getEntryCount() {
        return this.entryCount;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public void incrementEntryCount() {
        this.entryCount++;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public void decrementEntryCount() {
        this.entryCount--;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public void setClientRequestDispatcher(ClientRequestDispatcher clientRequestDispatcher) {
        this.clientRequestDispatcher = clientRequestDispatcher;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public ClientRequestDispatcher getClientRequestDispatcher() {
        return this.clientRequestDispatcher;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public void setMessageMediator(MessageMediator messageMediator) {
        this.messageMediator = messageMediator;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientInvocationInfo
    public MessageMediator getMessageMediator() {
        return this.messageMediator;
    }
}
