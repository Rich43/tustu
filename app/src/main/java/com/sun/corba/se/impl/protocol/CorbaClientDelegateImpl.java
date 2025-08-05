package com.sun.corba.se.impl.protocol;

import com.sun.corba.se.impl.corba.RequestImpl;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.util.JDKBridge;
import com.sun.corba.se.pept.broker.Broker;
import com.sun.corba.se.pept.encoding.InputObject;
import com.sun.corba.se.pept.encoding.OutputObject;
import com.sun.corba.se.pept.protocol.ClientInvocationInfo;
import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
import com.sun.corba.se.pept.transport.ContactInfoList;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
import com.sun.corba.se.spi.transport.CorbaContactInfo;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import com.sun.corba.se.spi.transport.CorbaContactInfoListIterator;
import java.util.Iterator;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.NVList;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.Object;
import org.omg.CORBA.Request;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.RemarshalException;
import org.omg.CORBA.portable.ServantObject;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/CorbaClientDelegateImpl.class */
public class CorbaClientDelegateImpl extends CorbaClientDelegate {
    private ORB orb;
    private ORBUtilSystemException wrapper;
    private CorbaContactInfoList contactInfoList;

    public CorbaClientDelegateImpl(ORB orb, CorbaContactInfoList corbaContactInfoList) {
        this.orb = orb;
        this.wrapper = ORBUtilSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.contactInfoList = corbaContactInfoList;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientDelegate
    public Broker getBroker() {
        return this.orb;
    }

    @Override // com.sun.corba.se.pept.protocol.ClientDelegate
    public ContactInfoList getContactInfoList() {
        return this.contactInfoList;
    }

    @Override // org.omg.CORBA.portable.Delegate
    public OutputStream request(Object object, String str, boolean z2) {
        ClientInvocationInfo clientInvocationInfoCreateOrIncrementInvocationInfo = this.orb.createOrIncrementInvocationInfo();
        Iterator contactInfoListIterator = clientInvocationInfoCreateOrIncrementInvocationInfo.getContactInfoListIterator();
        if (contactInfoListIterator == null) {
            contactInfoListIterator = this.contactInfoList.iterator();
            clientInvocationInfoCreateOrIncrementInvocationInfo.setContactInfoListIterator(contactInfoListIterator);
        }
        if (!contactInfoListIterator.hasNext()) {
            throw ((CorbaContactInfoListIterator) contactInfoListIterator).getFailureException();
        }
        CorbaContactInfo corbaContactInfo = (CorbaContactInfo) contactInfoListIterator.next();
        ClientRequestDispatcher clientRequestDispatcher = corbaContactInfo.getClientRequestDispatcher();
        clientInvocationInfoCreateOrIncrementInvocationInfo.setClientRequestDispatcher(clientRequestDispatcher);
        return (OutputStream) clientRequestDispatcher.beginRequest(object, str, !z2, corbaContactInfo);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.omg.CORBA.portable.Delegate
    public InputStream invoke(Object object, OutputStream outputStream) throws ApplicationException, RemarshalException {
        return (InputStream) getClientRequestDispatcher().marshalingComplete(object, (OutputObject) outputStream);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.omg.CORBA.portable.Delegate
    public void releaseReply(Object object, InputStream inputStream) {
        getClientRequestDispatcher().endRequest(this.orb, object, (InputObject) inputStream);
        this.orb.releaseOrDecrementInvocationInfo();
    }

    private ClientRequestDispatcher getClientRequestDispatcher() {
        return ((CorbaInvocationInfo) this.orb.getInvocationInfo()).getClientRequestDispatcher();
    }

    @Override // org.omg.CORBA.portable.Delegate
    public Object get_interface_def(Object object) {
        try {
            try {
                InputStream inputStreamInvoke = invoke((Object) null, request(null, "_interface", true));
                Object object2 = inputStreamInvoke.read_Object();
                if (!object2._is_a("IDL:omg.org/CORBA/InterfaceDef:1.0")) {
                    throw this.wrapper.wrongInterfaceDef(CompletionStatus.COMPLETED_MAYBE);
                }
                try {
                    Object object3 = (Object) JDKBridge.loadClass("org.omg.CORBA._InterfaceDefStub").newInstance();
                    StubAdapter.setDelegate(object3, StubAdapter.getDelegate(object2));
                    releaseReply((Object) null, inputStreamInvoke);
                    return object3;
                } catch (Exception e2) {
                    throw this.wrapper.noInterfaceDefStub(e2);
                }
            } catch (ApplicationException e3) {
                throw this.wrapper.applicationExceptionInSpecialMethod(e3);
            } catch (RemarshalException e4) {
                Object object4 = get_interface_def(object);
                releaseReply((Object) null, null);
                return object4;
            }
        } catch (Throwable th) {
            releaseReply((Object) null, null);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.Delegate
    public boolean is_a(Object object, String str) {
        String[] typeIds = StubAdapter.getTypeIds(object);
        if (str.equals(this.contactInfoList.getTargetIOR().getTypeId())) {
            return true;
        }
        for (String str2 : typeIds) {
            if (str.equals(str2)) {
                return true;
            }
        }
        InputStream inputStreamInvoke = null;
        try {
            try {
                try {
                    OutputStream outputStreamRequest = request(null, "_is_a", true);
                    outputStreamRequest.write_string(str);
                    inputStreamInvoke = invoke((Object) null, outputStreamRequest);
                    boolean z2 = inputStreamInvoke.read_boolean();
                    releaseReply((Object) null, inputStreamInvoke);
                    return z2;
                } catch (RemarshalException e2) {
                    boolean zIs_a = is_a(object, str);
                    releaseReply((Object) null, inputStreamInvoke);
                    return zIs_a;
                }
            } catch (ApplicationException e3) {
                throw this.wrapper.applicationExceptionInSpecialMethod(e3);
            }
        } catch (Throwable th) {
            releaseReply((Object) null, inputStreamInvoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.Delegate
    public boolean non_existent(Object object) {
        InputStream inputStreamInvoke = null;
        try {
            try {
                inputStreamInvoke = invoke((Object) null, request(null, "_non_existent", true));
                boolean z2 = inputStreamInvoke.read_boolean();
                releaseReply((Object) null, inputStreamInvoke);
                return z2;
            } catch (ApplicationException e2) {
                throw this.wrapper.applicationExceptionInSpecialMethod(e2);
            } catch (RemarshalException e3) {
                boolean zNon_existent = non_existent(object);
                releaseReply((Object) null, inputStreamInvoke);
                return zNon_existent;
            }
        } catch (Throwable th) {
            releaseReply((Object) null, inputStreamInvoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.Delegate
    public Object duplicate(Object object) {
        return object;
    }

    @Override // org.omg.CORBA.portable.Delegate
    public void release(Object object) {
    }

    @Override // org.omg.CORBA.portable.Delegate
    public boolean is_equivalent(Object object, Object object2) {
        Delegate delegate;
        if (object2 == null || !StubAdapter.isStub(object2) || (delegate = StubAdapter.getDelegate(object2)) == null) {
            return false;
        }
        if (delegate == this) {
            return true;
        }
        if (!(delegate instanceof CorbaClientDelegateImpl)) {
            return false;
        }
        return this.contactInfoList.getTargetIOR().isEquivalent(((CorbaContactInfoList) ((CorbaClientDelegateImpl) delegate).getContactInfoList()).getTargetIOR());
    }

    @Override // org.omg.CORBA.portable.Delegate
    public boolean equals(Object object, Object obj) {
        Delegate delegate;
        if (obj != null && StubAdapter.isStub(obj) && (delegate = StubAdapter.getDelegate(obj)) != null && (delegate instanceof CorbaClientDelegateImpl)) {
            return this.contactInfoList.getTargetIOR().equals(((CorbaClientDelegateImpl) delegate).contactInfoList.getTargetIOR());
        }
        return false;
    }

    @Override // org.omg.CORBA.portable.Delegate
    public int hashCode(Object object) {
        return hashCode();
    }

    @Override // org.omg.CORBA.portable.Delegate
    public int hash(Object object, int i2) {
        int iHashCode = hashCode();
        if (iHashCode > i2) {
            return 0;
        }
        return iHashCode;
    }

    @Override // org.omg.CORBA.portable.Delegate
    public Request request(Object object, String str) {
        return new RequestImpl(this.orb, object, null, str, null, null, null, null);
    }

    @Override // org.omg.CORBA.portable.Delegate
    public Request create_request(Object object, Context context, String str, NVList nVList, NamedValue namedValue) {
        return new RequestImpl(this.orb, object, context, str, nVList, namedValue, null, null);
    }

    @Override // org.omg.CORBA.portable.Delegate
    public Request create_request(Object object, Context context, String str, NVList nVList, NamedValue namedValue, ExceptionList exceptionList, ContextList contextList) {
        return new RequestImpl(this.orb, object, context, str, nVList, namedValue, exceptionList, contextList);
    }

    @Override // org.omg.CORBA.portable.Delegate
    public org.omg.CORBA.ORB orb(Object object) {
        return this.orb;
    }

    @Override // org.omg.CORBA.portable.Delegate
    public boolean is_local(Object object) {
        return this.contactInfoList.getEffectiveTargetIOR().getProfile().isLocal();
    }

    @Override // org.omg.CORBA.portable.Delegate
    public ServantObject servant_preinvoke(Object object, String str, Class cls) {
        return this.contactInfoList.getLocalClientRequestDispatcher().servant_preinvoke(object, str, cls);
    }

    @Override // org.omg.CORBA.portable.Delegate
    public void servant_postinvoke(Object object, ServantObject servantObject) {
        this.contactInfoList.getLocalClientRequestDispatcher().servant_postinvoke(object, servantObject);
    }

    @Override // org.omg.CORBA_2_3.portable.Delegate
    public String get_codebase(Object object) {
        if (this.contactInfoList.getTargetIOR() != null) {
            return this.contactInfoList.getTargetIOR().getProfile().getCodebase();
        }
        return null;
    }

    @Override // org.omg.CORBA.portable.Delegate
    public String toString(Object object) {
        return this.contactInfoList.getTargetIOR().stringify();
    }

    public int hashCode() {
        return this.contactInfoList.hashCode();
    }
}
