package org.ietf.jgss;

/* loaded from: rt.jar:org/ietf/jgss/GSSCredential.class */
public interface GSSCredential extends Cloneable {
    public static final int INITIATE_AND_ACCEPT = 0;
    public static final int INITIATE_ONLY = 1;
    public static final int ACCEPT_ONLY = 2;
    public static final int DEFAULT_LIFETIME = 0;
    public static final int INDEFINITE_LIFETIME = Integer.MAX_VALUE;

    void dispose() throws GSSException;

    GSSName getName() throws GSSException;

    GSSName getName(Oid oid) throws GSSException;

    int getRemainingLifetime() throws GSSException;

    int getRemainingInitLifetime(Oid oid) throws GSSException;

    int getRemainingAcceptLifetime(Oid oid) throws GSSException;

    int getUsage() throws GSSException;

    int getUsage(Oid oid) throws GSSException;

    Oid[] getMechs() throws GSSException;

    void add(GSSName gSSName, int i2, int i3, Oid oid, int i4) throws GSSException;

    boolean equals(Object obj);

    int hashCode();
}
