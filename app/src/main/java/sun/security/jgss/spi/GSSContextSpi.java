package sun.security.jgss.spi;

import com.sun.security.jgss.InquireType;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;

/* loaded from: rt.jar:sun/security/jgss/spi/GSSContextSpi.class */
public interface GSSContextSpi {
    Provider getProvider();

    void requestLifetime(int i2) throws GSSException;

    void requestMutualAuth(boolean z2) throws GSSException;

    void requestReplayDet(boolean z2) throws GSSException;

    void requestSequenceDet(boolean z2) throws GSSException;

    void requestCredDeleg(boolean z2) throws GSSException;

    void requestAnonymity(boolean z2) throws GSSException;

    void requestConf(boolean z2) throws GSSException;

    void requestInteg(boolean z2) throws GSSException;

    void requestDelegPolicy(boolean z2) throws GSSException;

    void setChannelBinding(ChannelBinding channelBinding) throws GSSException;

    boolean getCredDelegState();

    boolean getMutualAuthState();

    boolean getReplayDetState();

    boolean getSequenceDetState();

    boolean getAnonymityState();

    boolean getDelegPolicyState();

    boolean isTransferable() throws GSSException;

    boolean isProtReady();

    boolean isInitiator();

    boolean getConfState();

    boolean getIntegState();

    int getLifetime();

    boolean isEstablished();

    GSSNameSpi getSrcName() throws GSSException;

    GSSNameSpi getTargName() throws GSSException;

    Oid getMech() throws GSSException;

    GSSCredentialSpi getDelegCred() throws GSSException;

    byte[] initSecContext(InputStream inputStream, int i2) throws GSSException;

    byte[] acceptSecContext(InputStream inputStream, int i2) throws GSSException;

    int getWrapSizeLimit(int i2, boolean z2, int i3) throws GSSException;

    void wrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException;

    byte[] wrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException;

    void unwrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException;

    byte[] unwrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException;

    void getMIC(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException;

    byte[] getMIC(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException;

    void verifyMIC(InputStream inputStream, InputStream inputStream2, MessageProp messageProp) throws GSSException;

    void verifyMIC(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, MessageProp messageProp) throws GSSException;

    byte[] export() throws GSSException;

    void dispose() throws GSSException;

    Object inquireSecContext(InquireType inquireType) throws GSSException;
}
