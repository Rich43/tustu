package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.protocol.CorbaInvocationInfo;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.pept.transport.ContactInfoList;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
import java.util.Iterator;
import java.util.List;
import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.SystemException;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaContactInfoListIteratorImpl.class */
public class CorbaContactInfoListIteratorImpl implements CorbaContactInfoListIterator {
    protected ORB orb;
    protected CorbaContactInfoList contactInfoList;
    protected CorbaContactInfo successContactInfo;
    protected CorbaContactInfo failureContactInfo;
    protected RuntimeException failureException;
    protected Iterator effectiveTargetIORIterator;
    protected CorbaContactInfo previousContactInfo;
    protected boolean isAddrDispositionRetry;
    protected IIOPPrimaryToContactInfo primaryToContactInfo;
    protected ContactInfo primaryContactInfo;
    protected List listOfContactInfos;

    public CorbaContactInfoListIteratorImpl(ORB orb, CorbaContactInfoList corbaContactInfoList, ContactInfo contactInfo, List list) {
        this.orb = orb;
        this.contactInfoList = corbaContactInfoList;
        this.primaryContactInfo = contactInfo;
        if (list != null) {
            this.effectiveTargetIORIterator = list.iterator();
        }
        this.listOfContactInfos = list;
        this.previousContactInfo = null;
        this.isAddrDispositionRetry = false;
        this.successContactInfo = null;
        this.failureContactInfo = null;
        this.failureException = null;
        this.primaryToContactInfo = orb.getORBData().getIIOPPrimaryToContactInfo();
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        boolean zHasNext;
        if (this.isAddrDispositionRetry) {
            return true;
        }
        if (this.primaryToContactInfo != null) {
            zHasNext = this.primaryToContactInfo.hasNext(this.primaryContactInfo, this.previousContactInfo, this.listOfContactInfos);
        } else {
            zHasNext = this.effectiveTargetIORIterator.hasNext();
        }
        return zHasNext;
    }

    @Override // java.util.Iterator
    public Object next() {
        if (this.isAddrDispositionRetry) {
            this.isAddrDispositionRetry = false;
            return this.previousContactInfo;
        }
        if (this.primaryToContactInfo != null) {
            this.previousContactInfo = (CorbaContactInfo) this.primaryToContactInfo.next(this.primaryContactInfo, this.previousContactInfo, this.listOfContactInfos);
        } else {
            this.previousContactInfo = (CorbaContactInfo) this.effectiveTargetIORIterator.next();
        }
        return this.previousContactInfo;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfoListIterator
    public ContactInfoList getContactInfoList() {
        return this.contactInfoList;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfoListIterator
    public void reportSuccess(ContactInfo contactInfo) {
        this.successContactInfo = (CorbaContactInfo) contactInfo;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfoListIterator
    public boolean reportException(ContactInfo contactInfo, RuntimeException runtimeException) {
        this.failureContactInfo = (CorbaContactInfo) contactInfo;
        this.failureException = runtimeException;
        if ((runtimeException instanceof COMM_FAILURE) && ((SystemException) runtimeException).completed == CompletionStatus.COMPLETED_NO) {
            if (hasNext()) {
                return true;
            }
            if (this.contactInfoList.getEffectiveTargetIOR() != this.contactInfoList.getTargetIOR()) {
                updateEffectiveTargetIOR(this.contactInfoList.getTargetIOR());
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfoListIterator
    public RuntimeException getFailureException() {
        if (this.failureException == null) {
            return ORBUtilSystemException.get(this.orb, CORBALogDomains.RPC_TRANSPORT).invalidContactInfoListIteratorFailureException();
        }
        return this.failureException;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListIterator
    public void reportAddrDispositionRetry(CorbaContactInfo corbaContactInfo, short s2) {
        this.previousContactInfo.setAddressingDisposition(s2);
        this.isAddrDispositionRetry = true;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoListIterator
    public void reportRedirect(CorbaContactInfo corbaContactInfo, IOR ior) {
        updateEffectiveTargetIOR(ior);
    }

    public void updateEffectiveTargetIOR(IOR ior) {
        this.contactInfoList.setEffectiveTargetIOR(ior);
        ((CorbaInvocationInfo) this.orb.getInvocationInfo()).setContactInfoListIterator(this.contactInfoList.iterator());
    }
}
