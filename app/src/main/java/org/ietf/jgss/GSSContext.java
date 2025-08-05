package org.ietf.jgss;

import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:org/ietf/jgss/GSSContext.class */
public interface GSSContext {
    public static final int DEFAULT_LIFETIME = 0;
    public static final int INDEFINITE_LIFETIME = Integer.MAX_VALUE;

    byte[] initSecContext(byte[] bArr, int i2, int i3) throws GSSException;

    int initSecContext(InputStream inputStream, OutputStream outputStream) throws GSSException;

    byte[] acceptSecContext(byte[] bArr, int i2, int i3) throws GSSException;

    void acceptSecContext(InputStream inputStream, OutputStream outputStream) throws GSSException;

    boolean isEstablished();

    void dispose() throws GSSException;

    int getWrapSizeLimit(int i2, boolean z2, int i3) throws GSSException;

    byte[] wrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException;

    void wrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException;

    byte[] unwrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException;

    void unwrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException;

    byte[] getMIC(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException;

    void getMIC(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException;

    void verifyMIC(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, MessageProp messageProp) throws GSSException;

    void verifyMIC(InputStream inputStream, InputStream inputStream2, MessageProp messageProp) throws GSSException;

    byte[] export() throws GSSException;

    void requestMutualAuth(boolean z2) throws GSSException;

    void requestReplayDet(boolean z2) throws GSSException;

    void requestSequenceDet(boolean z2) throws GSSException;

    void requestCredDeleg(boolean z2) throws GSSException;

    void requestAnonymity(boolean z2) throws GSSException;

    void requestConf(boolean z2) throws GSSException;

    void requestInteg(boolean z2) throws GSSException;

    void requestLifetime(int i2) throws GSSException;

    void setChannelBinding(ChannelBinding channelBinding) throws GSSException;

    boolean getCredDelegState();

    boolean getMutualAuthState();

    boolean getReplayDetState();

    boolean getSequenceDetState();

    boolean getAnonymityState();

    boolean isTransferable() throws GSSException;

    boolean isProtReady();

    boolean getConfState();

    boolean getIntegState();

    int getLifetime();

    GSSName getSrcName() throws GSSException;

    GSSName getTargName() throws GSSException;

    Oid getMech() throws GSSException;

    GSSCredential getDelegCred() throws GSSException;

    boolean isInitiator() throws GSSException;
}
