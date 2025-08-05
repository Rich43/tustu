package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.logging.POASystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.oa.OAInvocationInfo;
import com.sun.corba.se.spi.oa.ObjectAdapter;
import com.sun.corba.se.spi.orb.ORB;
import java.util.EmptyStackException;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.PortableServer.Current;
import org.omg.PortableServer.CurrentPackage.NoContext;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.Servant;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/POACurrent.class */
public class POACurrent extends ObjectImpl implements Current {
    private ORB orb;
    private POASystemException wrapper;

    public POACurrent(ORB orb) {
        this.orb = orb;
        this.wrapper = POASystemException.get(orb, CORBALogDomains.OA_INVOCATION);
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return new String[]{"IDL:omg.org/PortableServer/Current:1.0"};
    }

    @Override // org.omg.PortableServer.CurrentOperations
    public POA get_POA() throws NoContext {
        POA poa = (POA) peekThrowNoContext().oa();
        throwNoContextIfNull(poa);
        return poa;
    }

    @Override // org.omg.PortableServer.CurrentOperations
    public byte[] get_object_id() throws NoContext {
        byte[] bArrId = peekThrowNoContext().id();
        throwNoContextIfNull(bArrId);
        return bArrId;
    }

    public ObjectAdapter getOA() {
        ObjectAdapter objectAdapterOa = peekThrowInternal().oa();
        throwInternalIfNull(objectAdapterOa);
        return objectAdapterOa;
    }

    public byte[] getObjectId() {
        byte[] bArrId = peekThrowInternal().id();
        throwInternalIfNull(bArrId);
        return bArrId;
    }

    Servant getServant() {
        return (Servant) peekThrowInternal().getServantContainer();
    }

    CookieHolder getCookieHolder() {
        CookieHolder cookieHolder = peekThrowInternal().getCookieHolder();
        throwInternalIfNull(cookieHolder);
        return cookieHolder;
    }

    public String getOperation() {
        String operation = peekThrowInternal().getOperation();
        throwInternalIfNull(operation);
        return operation;
    }

    void setServant(Servant servant) {
        peekThrowInternal().setServant(servant);
    }

    private OAInvocationInfo peekThrowNoContext() throws NoContext {
        try {
            return this.orb.peekInvocationInfo();
        } catch (EmptyStackException e2) {
            throw new NoContext();
        }
    }

    private OAInvocationInfo peekThrowInternal() {
        try {
            return this.orb.peekInvocationInfo();
        } catch (EmptyStackException e2) {
            throw this.wrapper.poacurrentUnbalancedStack(e2);
        }
    }

    private void throwNoContextIfNull(Object obj) throws NoContext {
        if (obj == null) {
            throw new NoContext();
        }
    }

    private void throwInternalIfNull(Object obj) {
        if (obj == null) {
            throw this.wrapper.poacurrentNullField(CompletionStatus.COMPLETED_MAYBE);
        }
    }
}
