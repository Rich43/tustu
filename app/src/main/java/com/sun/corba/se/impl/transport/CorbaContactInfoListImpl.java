package com.sun.corba.se.impl.transport;

import com.sun.corba.se.impl.protocol.NotLocalLocalCRDImpl;
import com.sun.corba.se.pept.transport.ContactInfo;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.SocketInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/impl/transport/CorbaContactInfoListImpl.class */
public class CorbaContactInfoListImpl implements CorbaContactInfoList {
    protected ORB orb;
    protected LocalClientRequestDispatcher LocalClientRequestDispatcher;
    protected IOR targetIOR;
    protected IOR effectiveTargetIOR;
    protected List effectiveTargetIORContactInfoList;
    protected ContactInfo primaryContactInfo;

    public CorbaContactInfoListImpl(ORB orb) {
        this.orb = orb;
    }

    public CorbaContactInfoListImpl(ORB orb, IOR ior) {
        this(orb);
        setTargetIOR(ior);
    }

    @Override // com.sun.corba.se.pept.transport.ContactInfoList
    public synchronized Iterator iterator() {
        createContactInfoList();
        return new CorbaContactInfoListIteratorImpl(this.orb, this, this.primaryContactInfo, this.effectiveTargetIORContactInfoList);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoList
    public synchronized void setTargetIOR(IOR ior) {
        this.targetIOR = ior;
        setEffectiveTargetIOR(ior);
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoList
    public synchronized IOR getTargetIOR() {
        return this.targetIOR;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoList
    public synchronized void setEffectiveTargetIOR(IOR ior) {
        this.effectiveTargetIOR = ior;
        this.effectiveTargetIORContactInfoList = null;
        if (this.primaryContactInfo != null && this.orb.getORBData().getIIOPPrimaryToContactInfo() != null) {
            this.orb.getORBData().getIIOPPrimaryToContactInfo().reset(this.primaryContactInfo);
        }
        this.primaryContactInfo = null;
        setLocalSubcontract();
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoList
    public synchronized IOR getEffectiveTargetIOR() {
        return this.effectiveTargetIOR;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoList
    public synchronized LocalClientRequestDispatcher getLocalClientRequestDispatcher() {
        return this.LocalClientRequestDispatcher;
    }

    @Override // com.sun.corba.se.spi.transport.CorbaContactInfoList
    public synchronized int hashCode() {
        return this.targetIOR.hashCode();
    }

    protected void createContactInfoList() {
        if (this.effectiveTargetIORContactInfoList != null) {
            return;
        }
        this.effectiveTargetIORContactInfoList = new ArrayList();
        IIOPProfile profile = this.effectiveTargetIOR.getProfile();
        this.primaryContactInfo = createContactInfo("IIOP_CLEAR_TEXT", ((IIOPProfileTemplate) profile.getTaggedProfileTemplate()).getPrimaryAddress().getHost().toLowerCase(), ((IIOPProfileTemplate) profile.getTaggedProfileTemplate()).getPrimaryAddress().getPort());
        if (profile.isLocal()) {
            this.effectiveTargetIORContactInfoList.add(new SharedCDRContactInfoImpl(this.orb, this, this.effectiveTargetIOR, this.orb.getORBData().getGIOPAddressDisposition()));
        } else {
            addRemoteContactInfos(this.effectiveTargetIOR, this.effectiveTargetIORContactInfoList);
        }
    }

    protected void addRemoteContactInfos(IOR ior, List list) {
        for (SocketInfo socketInfo : this.orb.getORBData().getIORToSocketInfo().getSocketInfo(ior)) {
            list.add(createContactInfo(socketInfo.getType(), socketInfo.getHost().toLowerCase(), socketInfo.getPort()));
        }
    }

    protected ContactInfo createContactInfo(String str, String str2, int i2) {
        return new SocketOrChannelContactInfoImpl(this.orb, this, this.effectiveTargetIOR, this.orb.getORBData().getGIOPAddressDisposition(), str, str2, i2);
    }

    protected void setLocalSubcontract() {
        if (!this.effectiveTargetIOR.getProfile().isLocal()) {
            this.LocalClientRequestDispatcher = new NotLocalLocalCRDImpl();
        } else {
            int subcontractId = this.effectiveTargetIOR.getProfile().getObjectKeyTemplate().getSubcontractId();
            this.LocalClientRequestDispatcher = this.orb.getRequestDispatcherRegistry().getLocalClientRequestDispatcherFactory(subcontractId).create(subcontractId, this.effectiveTargetIOR);
        }
    }
}
