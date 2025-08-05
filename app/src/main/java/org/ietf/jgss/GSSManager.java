package org.ietf.jgss;

import java.security.Provider;
import sun.security.jgss.GSSManagerImpl;

/* loaded from: rt.jar:org/ietf/jgss/GSSManager.class */
public abstract class GSSManager {
    public abstract Oid[] getMechs();

    public abstract Oid[] getNamesForMech(Oid oid) throws GSSException;

    public abstract Oid[] getMechsForName(Oid oid);

    public abstract GSSName createName(String str, Oid oid) throws GSSException;

    public abstract GSSName createName(byte[] bArr, Oid oid) throws GSSException;

    public abstract GSSName createName(String str, Oid oid, Oid oid2) throws GSSException;

    public abstract GSSName createName(byte[] bArr, Oid oid, Oid oid2) throws GSSException;

    public abstract GSSCredential createCredential(int i2) throws GSSException;

    public abstract GSSCredential createCredential(GSSName gSSName, int i2, Oid oid, int i3) throws GSSException;

    public abstract GSSCredential createCredential(GSSName gSSName, int i2, Oid[] oidArr, int i3) throws GSSException;

    public abstract GSSContext createContext(GSSName gSSName, Oid oid, GSSCredential gSSCredential, int i2) throws GSSException;

    public abstract GSSContext createContext(GSSCredential gSSCredential) throws GSSException;

    public abstract GSSContext createContext(byte[] bArr) throws GSSException;

    public abstract void addProviderAtFront(Provider provider, Oid oid) throws GSSException;

    public abstract void addProviderAtEnd(Provider provider, Oid oid) throws GSSException;

    public static GSSManager getInstance() {
        return new GSSManagerImpl();
    }
}
