package com.sun.corba.se.impl.legacy.connection;

import com.sun.corba.se.impl.transport.CorbaContactInfoListIteratorImpl;
import com.sun.corba.se.impl.transport.SharedCDRContactInfoImpl;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.SocketInfo;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/legacy/connection/SocketFactoryContactInfoListIteratorImpl.class */
public class SocketFactoryContactInfoListIteratorImpl extends CorbaContactInfoListIteratorImpl {
    private SocketInfo socketInfoCookie;

    public SocketFactoryContactInfoListIteratorImpl(ORB orb, CorbaContactInfoList corbaContactInfoList) {
        super(orb, corbaContactInfoList, null, null);
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoListIteratorImpl, java.util.Iterator
    public boolean hasNext() {
        return true;
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoListIteratorImpl, java.util.Iterator
    public Object next() {
        if (this.contactInfoList.getEffectiveTargetIOR().getProfile().isLocal()) {
            return new SharedCDRContactInfoImpl(this.orb, this.contactInfoList, this.contactInfoList.getEffectiveTargetIOR(), this.orb.getORBData().getGIOPAddressDisposition());
        }
        return new SocketFactoryContactInfoImpl(this.orb, this.contactInfoList, this.contactInfoList.getEffectiveTargetIOR(), this.orb.getORBData().getGIOPAddressDisposition(), this.socketInfoCookie);
    }

    @Override // com.sun.corba.se.impl.transport.CorbaContactInfoListIteratorImpl, com.sun.corba.se.pept.transport.ContactInfoListIterator
    public boolean reportException(ContactInfo contactInfo, RuntimeException runtimeException) {
        this.failureContactInfo = (CorbaContactInfo) contactInfo;
        this.failureException = runtimeException;
        if (runtimeException instanceof COMM_FAILURE) {
            if (runtimeException.getCause() instanceof GetEndPointInfoAgainException) {
                this.socketInfoCookie = ((GetEndPointInfoAgainException) runtimeException.getCause()).getEndPointInfo();
                return true;
            }
            if (((SystemException) runtimeException).completed == CompletionStatus.COMPLETED_NO && this.contactInfoList.getEffectiveTargetIOR() != this.contactInfoList.getTargetIOR()) {
                this.contactInfoList.setEffectiveTargetIOR(this.contactInfoList.getTargetIOR());
                return true;
            }
            return false;
        }
        return false;
    }
}
