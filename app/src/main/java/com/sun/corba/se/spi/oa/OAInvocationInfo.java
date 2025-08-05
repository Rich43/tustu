package com.sun.corba.se.spi.oa;

import com.sun.corba.se.spi.copyobject.ObjectCopierFactory;
import javax.rmi.CORBA.Tie;
import org.omg.CORBA.portable.ServantObject;
import org.omg.PortableServer.ServantLocatorPackage.CookieHolder;

/* loaded from: rt.jar:com/sun/corba/se/spi/oa/OAInvocationInfo.class */
public class OAInvocationInfo extends ServantObject {
    private Object servantContainer;
    private ObjectAdapter oa;
    private byte[] oid;
    private CookieHolder cookieHolder;
    private String operation;
    private ObjectCopierFactory factory;

    public OAInvocationInfo(ObjectAdapter objectAdapter, byte[] bArr) {
        this.oa = objectAdapter;
        this.oid = bArr;
    }

    public OAInvocationInfo(OAInvocationInfo oAInvocationInfo, String str) {
        this.servant = oAInvocationInfo.servant;
        this.servantContainer = oAInvocationInfo.servantContainer;
        this.cookieHolder = oAInvocationInfo.cookieHolder;
        this.oa = oAInvocationInfo.oa;
        this.oid = oAInvocationInfo.oid;
        this.factory = oAInvocationInfo.factory;
        this.operation = str;
    }

    public ObjectAdapter oa() {
        return this.oa;
    }

    public byte[] id() {
        return this.oid;
    }

    public Object getServantContainer() {
        return this.servantContainer;
    }

    public CookieHolder getCookieHolder() {
        if (this.cookieHolder == null) {
            this.cookieHolder = new CookieHolder();
        }
        return this.cookieHolder;
    }

    public String getOperation() {
        return this.operation;
    }

    public ObjectCopierFactory getCopierFactory() {
        return this.factory;
    }

    public void setOperation(String str) {
        this.operation = str;
    }

    public void setCopierFactory(ObjectCopierFactory objectCopierFactory) {
        this.factory = objectCopierFactory;
    }

    public void setServant(Object obj) {
        this.servantContainer = obj;
        if (obj instanceof Tie) {
            this.servant = ((Tie) obj).getTarget();
        } else {
            this.servant = obj;
        }
    }
}
