package sun.security.jgss.krb5;

import com.sun.security.jgss.AuthorizationDataEntry;
import com.sun.security.jgss.InquireType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Key;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.Provider;
import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosTicket;
import javax.security.auth.kerberos.ServicePermission;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;
import sun.misc.HexDumpEncoder;
import sun.security.jgss.GSSCaller;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.TokenTracker;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.krb5.Credentials;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbApReq;
import sun.security.krb5.KrbException;
import sun.security.krb5.internal.Ticket;

/* loaded from: rt.jar:sun/security/jgss/krb5/Krb5Context.class */
class Krb5Context implements GSSContextSpi {
    private static final int STATE_NEW = 1;
    private static final int STATE_IN_PROCESS = 2;
    private static final int STATE_DONE = 3;
    private static final int STATE_DELETED = 4;
    private int state;
    public static final int SESSION_KEY = 0;
    public static final int INITIATOR_SUBKEY = 1;
    public static final int ACCEPTOR_SUBKEY = 2;
    private boolean credDelegState;
    private boolean mutualAuthState;
    private boolean replayDetState;
    private boolean sequenceDetState;
    private boolean confState;
    private boolean integState;
    private boolean delegPolicyState;
    private boolean isConstrainedDelegationTried;
    private int mySeqNumber;
    private int peerSeqNumber;
    private int keySrc;
    private TokenTracker peerTokenTracker;
    private CipherHelper cipherHelper;
    private Object mySeqNumberLock;
    private Object peerSeqNumberLock;
    private EncryptionKey key;
    private Krb5NameElement myName;
    private Krb5NameElement peerName;
    private int lifetime;
    private boolean initiator;
    private ChannelBinding channelBinding;
    private Krb5CredElement myCred;
    private Krb5CredElement delegatedCred;
    private Credentials serviceCreds;
    private KrbApReq apReq;
    Ticket serviceTicket;
    private final GSSCaller caller;
    private static final boolean DEBUG = Krb5Util.DEBUG;
    private boolean[] tktFlags;
    private String authTime;
    private AuthorizationDataEntry[] authzData;

    Krb5Context(GSSCaller gSSCaller, Krb5NameElement krb5NameElement, Krb5CredElement krb5CredElement, int i2) throws GSSException {
        this.state = 1;
        this.credDelegState = false;
        this.mutualAuthState = true;
        this.replayDetState = true;
        this.sequenceDetState = true;
        this.confState = true;
        this.integState = true;
        this.delegPolicyState = false;
        this.isConstrainedDelegationTried = false;
        this.cipherHelper = null;
        this.mySeqNumberLock = new Object();
        this.peerSeqNumberLock = new Object();
        if (krb5NameElement == null) {
            throw new IllegalArgumentException("Cannot have null peer name");
        }
        this.caller = gSSCaller;
        this.peerName = krb5NameElement;
        this.myCred = krb5CredElement;
        this.lifetime = i2;
        this.initiator = true;
    }

    Krb5Context(GSSCaller gSSCaller, Krb5CredElement krb5CredElement) throws GSSException {
        this.state = 1;
        this.credDelegState = false;
        this.mutualAuthState = true;
        this.replayDetState = true;
        this.sequenceDetState = true;
        this.confState = true;
        this.integState = true;
        this.delegPolicyState = false;
        this.isConstrainedDelegationTried = false;
        this.cipherHelper = null;
        this.mySeqNumberLock = new Object();
        this.peerSeqNumberLock = new Object();
        this.caller = gSSCaller;
        this.myCred = krb5CredElement;
        this.initiator = false;
    }

    public Krb5Context(GSSCaller gSSCaller, byte[] bArr) throws GSSException {
        this.state = 1;
        this.credDelegState = false;
        this.mutualAuthState = true;
        this.replayDetState = true;
        this.sequenceDetState = true;
        this.confState = true;
        this.integState = true;
        this.delegPolicyState = false;
        this.isConstrainedDelegationTried = false;
        this.cipherHelper = null;
        this.mySeqNumberLock = new Object();
        this.peerSeqNumberLock = new Object();
        throw new GSSException(16, -1, "GSS Import Context not available");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean isTransferable() throws GSSException {
        return false;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final int getLifetime() {
        return Integer.MAX_VALUE;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestLifetime(int i2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.lifetime = i2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestConf(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.confState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getConfState() {
        return this.confState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestInteg(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.integState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getIntegState() {
        return this.integState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestCredDeleg(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            if (this.myCred == null || !(this.myCred instanceof Krb5ProxyCredential)) {
                this.credDelegState = z2;
            }
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getCredDelegState() {
        if (isInitiator()) {
            return this.credDelegState;
        }
        tryConstrainedDelegation();
        return this.delegatedCred != null;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestMutualAuth(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.mutualAuthState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getMutualAuthState() {
        return this.mutualAuthState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestReplayDet(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.replayDetState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getReplayDetState() {
        return this.replayDetState || this.sequenceDetState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestSequenceDet(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.sequenceDetState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getSequenceDetState() {
        return this.sequenceDetState || this.replayDetState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestDelegPolicy(boolean z2) {
        if (this.state == 1 && isInitiator()) {
            this.delegPolicyState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getDelegPolicyState() {
        return this.delegPolicyState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestAnonymity(boolean z2) throws GSSException {
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getAnonymityState() {
        return false;
    }

    final CipherHelper getCipherHelper(EncryptionKey encryptionKey) throws GSSException {
        if (this.cipherHelper == null) {
            this.cipherHelper = new CipherHelper(getKey() == null ? encryptionKey : getKey());
        }
        return this.cipherHelper;
    }

    final int incrementMySequenceNumber() {
        int i2;
        synchronized (this.mySeqNumberLock) {
            i2 = this.mySeqNumber;
            this.mySeqNumber = i2 + 1;
        }
        return i2;
    }

    final void resetMySequenceNumber(int i2) {
        if (DEBUG) {
            System.out.println("Krb5Context setting mySeqNumber to: " + i2);
        }
        synchronized (this.mySeqNumberLock) {
            this.mySeqNumber = i2;
        }
    }

    final void resetPeerSequenceNumber(int i2) {
        if (DEBUG) {
            System.out.println("Krb5Context setting peerSeqNumber to: " + i2);
        }
        synchronized (this.peerSeqNumberLock) {
            this.peerSeqNumber = i2;
            this.peerTokenTracker = new TokenTracker(this.peerSeqNumber);
        }
    }

    final void setKey(int i2, EncryptionKey encryptionKey) throws GSSException {
        this.key = encryptionKey;
        this.keySrc = i2;
        this.cipherHelper = new CipherHelper(encryptionKey);
    }

    public final int getKeySrc() {
        return this.keySrc;
    }

    private final EncryptionKey getKey() {
        return this.key;
    }

    final void setDelegCred(Krb5CredElement krb5CredElement) {
        this.delegatedCred = krb5CredElement;
    }

    final void setCredDelegState(boolean z2) {
        this.credDelegState = z2;
    }

    final void setMutualAuthState(boolean z2) {
        this.mutualAuthState = z2;
    }

    final void setReplayDetState(boolean z2) {
        this.replayDetState = z2;
    }

    final void setSequenceDetState(boolean z2) {
        this.sequenceDetState = z2;
    }

    final void setConfState(boolean z2) {
        this.confState = z2;
    }

    final void setIntegState(boolean z2) {
        this.integState = z2;
    }

    final void setDelegPolicyState(boolean z2) {
        this.delegPolicyState = z2;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void setChannelBinding(ChannelBinding channelBinding) throws GSSException {
        this.channelBinding = channelBinding;
    }

    final ChannelBinding getChannelBinding() {
        return this.channelBinding;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final Oid getMech() {
        return Krb5MechFactory.GSS_KRB5_MECH_OID;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final GSSNameSpi getSrcName() throws GSSException {
        return isInitiator() ? this.myName : this.peerName;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final GSSNameSpi getTargName() throws GSSException {
        return !isInitiator() ? this.myName : this.peerName;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final GSSCredentialSpi getDelegCred() throws GSSException {
        if (this.state != 2 && this.state != 3) {
            throw new GSSException(12);
        }
        if (isInitiator()) {
            throw new GSSException(13);
        }
        tryConstrainedDelegation();
        if (this.delegatedCred == null) {
            throw new GSSException(13);
        }
        return this.delegatedCred;
    }

    private void tryConstrainedDelegation() {
        if ((this.state == 2 || this.state == 3) && !this.isConstrainedDelegationTried) {
            if (this.delegatedCred == null) {
                if (DEBUG) {
                    System.out.println(">>> Constrained deleg from " + ((Object) this.caller));
                }
                try {
                    this.delegatedCred = new Krb5ProxyCredential(Krb5InitCredential.getInstance(GSSCaller.CALLER_ACCEPT, this.myName, this.lifetime), this.peerName, this.serviceTicket);
                } catch (GSSException e2) {
                }
            }
            this.isConstrainedDelegationTried = true;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean isInitiator() {
        return this.initiator;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean isProtReady() {
        return this.state == 3;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] initSecContext(InputStream inputStream, int i2) throws GSSException {
        Krb5ProxyCredential krb5ProxyCredential;
        Credentials krb5Credentials;
        byte[] bArrEncode = null;
        int i3 = 11;
        if (DEBUG) {
            System.out.println("Entered Krb5Context.initSecContext with state=" + printState(this.state));
        }
        if (!isInitiator()) {
            throw new GSSException(11, -1, "initSecContext on an acceptor GSSContext");
        }
        try {
            if (this.state == 1) {
                this.state = 2;
                i3 = 13;
                if (this.myCred == null) {
                    this.myCred = Krb5InitCredential.getInstance(this.caller, this.myName, 0);
                    this.myCred = Krb5ProxyCredential.tryImpersonation(this.caller, (Krb5InitCredential) this.myCred);
                } else if (!this.myCred.isInitiatorCredential()) {
                    throw new GSSException(13, -1, "No TGT available");
                }
                this.myName = (Krb5NameElement) this.myCred.getName();
                if (this.myCred instanceof Krb5InitCredential) {
                    krb5ProxyCredential = null;
                    krb5Credentials = ((Krb5InitCredential) this.myCred).getKrb5Credentials();
                } else {
                    krb5ProxyCredential = (Krb5ProxyCredential) this.myCred;
                    krb5Credentials = krb5ProxyCredential.self.getKrb5Credentials();
                }
                checkPermission(this.peerName.getKrb5PrincipalName().getName(), "initiate");
                final AccessControlContext context = AccessController.getContext();
                if (GSSUtil.useSubjectCredsOnly(this.caller)) {
                    KerberosTicket kerberosTicket = null;
                    try {
                        final Krb5ProxyCredential krb5ProxyCredential2 = krb5ProxyCredential;
                        kerberosTicket = (KerberosTicket) AccessController.doPrivileged(new PrivilegedExceptionAction<KerberosTicket>() { // from class: sun.security.jgss.krb5.Krb5Context.1
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedExceptionAction
                            public KerberosTicket run() throws Exception {
                                String name;
                                GSSCaller gSSCaller = GSSCaller.CALLER_UNKNOWN;
                                if (krb5ProxyCredential2 == null) {
                                    name = Krb5Context.this.myName.getKrb5PrincipalName().getName();
                                } else {
                                    name = krb5ProxyCredential2.getName().getKrb5PrincipalName().getName();
                                }
                                return Krb5Util.getServiceTicket(gSSCaller, name, Krb5Context.this.peerName.getKrb5PrincipalName().getName(), context);
                            }
                        });
                    } catch (PrivilegedActionException e2) {
                        if (DEBUG) {
                            System.out.println("Attempt to obtain service ticket from the subject failed!");
                        }
                    }
                    if (kerberosTicket != null) {
                        if (DEBUG) {
                            System.out.println("Found service ticket in the subject" + ((Object) kerberosTicket));
                        }
                        this.serviceCreds = Krb5Util.ticketToCreds(kerberosTicket);
                    }
                }
                if (this.serviceCreds == null) {
                    if (DEBUG) {
                        System.out.println("Service ticket not found in the subject");
                    }
                    if (krb5ProxyCredential == null) {
                        this.serviceCreds = Credentials.acquireServiceCreds(this.peerName.getKrb5PrincipalName().getName(), krb5Credentials);
                    } else {
                        this.serviceCreds = Credentials.acquireS4U2proxyCreds(this.peerName.getKrb5PrincipalName().getName(), krb5ProxyCredential.tkt, krb5ProxyCredential.getName().getKrb5PrincipalName(), krb5Credentials);
                    }
                    if (GSSUtil.useSubjectCredsOnly(this.caller)) {
                        final Subject subject = (Subject) AccessController.doPrivileged(new PrivilegedAction<Subject>() { // from class: sun.security.jgss.krb5.Krb5Context.2
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.security.PrivilegedAction
                            /* renamed from: run */
                            public Subject run2() {
                                return Subject.getSubject(context);
                            }
                        });
                        if (subject != null && !subject.isReadOnly()) {
                            final KerberosTicket kerberosTicketCredsToTicket = Krb5Util.credsToTicket(this.serviceCreds);
                            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.security.jgss.krb5.Krb5Context.3
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.security.PrivilegedAction
                                /* renamed from: run */
                                public Void run2() {
                                    subject.getPrivateCredentials().add(kerberosTicketCredsToTicket);
                                    return null;
                                }
                            });
                        } else if (DEBUG) {
                            System.out.println("Subject is readOnly;Kerberos Service ticket not stored");
                        }
                    }
                }
                InitSecContextToken initSecContextToken = new InitSecContextToken(this, krb5Credentials, this.serviceCreds);
                this.apReq = initSecContextToken.getKrbApReq();
                bArrEncode = initSecContextToken.encode();
                this.myCred = null;
                if (!getMutualAuthState()) {
                    this.state = 3;
                }
                if (DEBUG) {
                    System.out.println("Created InitSecContextToken:\n" + new HexDumpEncoder().encodeBuffer(bArrEncode));
                }
            } else if (this.state == 2) {
                new AcceptSecContextToken(this, this.serviceCreds, this.apReq, inputStream);
                this.serviceCreds = null;
                this.apReq = null;
                this.state = 3;
            } else if (DEBUG) {
                System.out.println(this.state);
            }
            return bArrEncode;
        } catch (IOException e3) {
            GSSException gSSException = new GSSException(i3, -1, e3.getMessage());
            gSSException.initCause(e3);
            throw gSSException;
        } catch (KrbException e4) {
            if (DEBUG) {
                e4.printStackTrace();
            }
            GSSException gSSException2 = new GSSException(i3, -1, e4.getMessage());
            gSSException2.initCause(e4);
            throw gSSException2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean isEstablished() {
        return this.state == 3;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] acceptSecContext(InputStream inputStream, int i2) throws GSSException {
        byte[] bArrEncode = null;
        if (DEBUG) {
            System.out.println("Entered Krb5Context.acceptSecContext with state=" + printState(this.state));
        }
        if (isInitiator()) {
            throw new GSSException(11, -1, "acceptSecContext on an initiator GSSContext");
        }
        try {
            if (this.state == 1) {
                this.state = 2;
                if (this.myCred == null) {
                    this.myCred = Krb5AcceptCredential.getInstance(this.caller, this.myName);
                } else if (!this.myCred.isAcceptorCredential()) {
                    throw new GSSException(13, -1, "No Secret Key available");
                }
                this.myName = (Krb5NameElement) this.myCred.getName();
                if (this.myName != null) {
                    Krb5MechFactory.checkAcceptCredPermission(this.myName, this.myName);
                }
                InitSecContextToken initSecContextToken = new InitSecContextToken(this, (Krb5AcceptCredential) this.myCred, inputStream);
                this.peerName = Krb5NameElement.getInstance(initSecContextToken.getKrbApReq().getClient());
                if (this.myName == null) {
                    this.myName = Krb5NameElement.getInstance(initSecContextToken.getKrbApReq().getCreds().getServer());
                    Krb5MechFactory.checkAcceptCredPermission(this.myName, this.myName);
                }
                if (getMutualAuthState()) {
                    bArrEncode = new AcceptSecContextToken(this, initSecContextToken.getKrbApReq()).encode();
                }
                this.serviceTicket = initSecContextToken.getKrbApReq().getCreds().getTicket();
                this.myCred = null;
                this.state = 3;
            } else if (DEBUG) {
                System.out.println(this.state);
            }
            return bArrEncode;
        } catch (IOException e2) {
            if (DEBUG) {
                e2.printStackTrace();
            }
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        } catch (KrbException e3) {
            GSSException gSSException2 = new GSSException(11, -1, e3.getMessage());
            gSSException2.initCause(e3);
            throw gSSException2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final int getWrapSizeLimit(int i2, boolean z2, int i3) throws GSSException {
        int sizeLimit = 0;
        if (this.cipherHelper.getProto() == 0) {
            sizeLimit = WrapToken.getSizeLimit(i2, z2, i3, getCipherHelper(null));
        } else if (this.cipherHelper.getProto() == 1) {
            sizeLimit = WrapToken_v2.getSizeLimit(i2, z2, i3, getCipherHelper(null));
        }
        return sizeLimit;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] wrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (DEBUG) {
            System.out.println("Krb5Context.wrap: data=[" + getHexBytes(bArr, i2, i3) + "]");
        }
        if (this.state != 3) {
            throw new GSSException(12, -1, "Wrap called in invalid state!");
        }
        byte[] bArrEncode = null;
        try {
            if (this.cipherHelper.getProto() == 0) {
                bArrEncode = new WrapToken(this, messageProp, bArr, i2, i3).encode();
            } else if (this.cipherHelper.getProto() == 1) {
                bArrEncode = new WrapToken_v2(this, messageProp, bArr, i2, i3).encode();
            }
            if (DEBUG) {
                System.out.println("Krb5Context.wrap: token=[" + getHexBytes(bArrEncode, 0, bArrEncode.length) + "]");
            }
            return bArrEncode;
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    public final int wrap(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, MessageProp messageProp) throws GSSException {
        if (this.state != 3) {
            throw new GSSException(12, -1, "Wrap called in invalid state!");
        }
        int iEncode = 0;
        try {
            if (this.cipherHelper.getProto() == 0) {
                iEncode = new WrapToken(this, messageProp, bArr, i2, i3).encode(bArr2, i4);
            } else if (this.cipherHelper.getProto() == 1) {
                iEncode = new WrapToken_v2(this, messageProp, bArr, i2, i3).encode(bArr2, i4);
            }
            if (DEBUG) {
                System.out.println("Krb5Context.wrap: token=[" + getHexBytes(bArr2, i4, iEncode) + "]");
            }
            return iEncode;
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    public final void wrap(byte[] bArr, int i2, int i3, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.state != 3) {
            throw new GSSException(12, -1, "Wrap called in invalid state!");
        }
        byte[] bArrEncode = null;
        try {
            if (this.cipherHelper.getProto() == 0) {
                WrapToken wrapToken = new WrapToken(this, messageProp, bArr, i2, i3);
                wrapToken.encode(outputStream);
                if (DEBUG) {
                    bArrEncode = wrapToken.encode();
                }
            } else if (this.cipherHelper.getProto() == 1) {
                WrapToken_v2 wrapToken_v2 = new WrapToken_v2(this, messageProp, bArr, i2, i3);
                wrapToken_v2.encode(outputStream);
                if (DEBUG) {
                    bArrEncode = wrapToken_v2.encode();
                }
            }
            if (DEBUG) {
                System.out.println("Krb5Context.wrap: token=[" + getHexBytes(bArrEncode, 0, bArrEncode.length) + "]");
            }
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void wrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream.available()];
            inputStream.read(bArr);
            wrap(bArr, 0, bArr.length, outputStream, messageProp);
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] unwrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (DEBUG) {
            System.out.println("Krb5Context.unwrap: token=[" + getHexBytes(bArr, i2, i3) + "]");
        }
        if (this.state != 3) {
            throw new GSSException(12, -1, " Unwrap called in invalid state!");
        }
        byte[] data = null;
        if (this.cipherHelper.getProto() == 0) {
            WrapToken wrapToken = new WrapToken(this, bArr, i2, i3, messageProp);
            data = wrapToken.getData();
            setSequencingAndReplayProps(wrapToken, messageProp);
        } else if (this.cipherHelper.getProto() == 1) {
            WrapToken_v2 wrapToken_v2 = new WrapToken_v2(this, bArr, i2, i3, messageProp);
            data = wrapToken_v2.getData();
            setSequencingAndReplayProps(wrapToken_v2, messageProp);
        }
        if (DEBUG) {
            System.out.println("Krb5Context.unwrap: data=[" + getHexBytes(data, 0, data.length) + "]");
        }
        return data;
    }

    public final int unwrap(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, MessageProp messageProp) throws GSSException {
        if (this.state != 3) {
            throw new GSSException(12, -1, "Unwrap called in invalid state!");
        }
        if (this.cipherHelper.getProto() == 0) {
            WrapToken wrapToken = new WrapToken(this, bArr, i2, i3, messageProp);
            i3 = wrapToken.getData(bArr2, i4);
            setSequencingAndReplayProps(wrapToken, messageProp);
        } else if (this.cipherHelper.getProto() == 1) {
            WrapToken_v2 wrapToken_v2 = new WrapToken_v2(this, bArr, i2, i3, messageProp);
            i3 = wrapToken_v2.getData(bArr2, i4);
            setSequencingAndReplayProps(wrapToken_v2, messageProp);
        }
        return i3;
    }

    public final int unwrap(InputStream inputStream, byte[] bArr, int i2, MessageProp messageProp) throws GSSException {
        if (this.state != 3) {
            throw new GSSException(12, -1, "Unwrap called in invalid state!");
        }
        int data = 0;
        if (this.cipherHelper.getProto() == 0) {
            WrapToken wrapToken = new WrapToken(this, inputStream, messageProp);
            data = wrapToken.getData(bArr, i2);
            setSequencingAndReplayProps(wrapToken, messageProp);
        } else if (this.cipherHelper.getProto() == 1) {
            WrapToken_v2 wrapToken_v2 = new WrapToken_v2(this, inputStream, messageProp);
            data = wrapToken_v2.getData(bArr, i2);
            setSequencingAndReplayProps(wrapToken_v2, messageProp);
        }
        return data;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void unwrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.state != 3) {
            throw new GSSException(12, -1, "Unwrap called in invalid state!");
        }
        byte[] data = null;
        if (this.cipherHelper.getProto() == 0) {
            WrapToken wrapToken = new WrapToken(this, inputStream, messageProp);
            data = wrapToken.getData();
            setSequencingAndReplayProps(wrapToken, messageProp);
        } else if (this.cipherHelper.getProto() == 1) {
            WrapToken_v2 wrapToken_v2 = new WrapToken_v2(this, inputStream, messageProp);
            data = wrapToken_v2.getData();
            setSequencingAndReplayProps(wrapToken_v2, messageProp);
        }
        try {
            outputStream.write(data);
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] getMIC(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        byte[] bArrEncode = null;
        try {
            if (this.cipherHelper.getProto() == 0) {
                bArrEncode = new MicToken(this, messageProp, bArr, i2, i3).encode();
            } else if (this.cipherHelper.getProto() == 1) {
                bArrEncode = new MicToken_v2(this, messageProp, bArr, i2, i3).encode();
            }
            return bArrEncode;
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private int getMIC(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, MessageProp messageProp) throws GSSException {
        int iEncode = 0;
        try {
            if (this.cipherHelper.getProto() == 0) {
                iEncode = new MicToken(this, messageProp, bArr, i2, i3).encode(bArr2, i4);
            } else if (this.cipherHelper.getProto() == 1) {
                iEncode = new MicToken_v2(this, messageProp, bArr, i2, i3).encode(bArr2, i4);
            }
            return iEncode;
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private void getMIC(byte[] bArr, int i2, int i3, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        try {
            if (this.cipherHelper.getProto() == 0) {
                new MicToken(this, messageProp, bArr, i2, i3).encode(outputStream);
            } else if (this.cipherHelper.getProto() == 1) {
                new MicToken_v2(this, messageProp, bArr, i2, i3).encode(outputStream);
            }
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void getMIC(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream.available()];
            inputStream.read(bArr);
            getMIC(bArr, 0, bArr.length, outputStream, messageProp);
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void verifyMIC(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, MessageProp messageProp) throws GSSException {
        if (this.cipherHelper.getProto() == 0) {
            MicToken micToken = new MicToken(this, bArr, i2, i3, messageProp);
            micToken.verify(bArr2, i4, i5);
            setSequencingAndReplayProps(micToken, messageProp);
        } else if (this.cipherHelper.getProto() == 1) {
            MicToken_v2 micToken_v2 = new MicToken_v2(this, bArr, i2, i3, messageProp);
            micToken_v2.verify(bArr2, i4, i5);
            setSequencingAndReplayProps(micToken_v2, messageProp);
        }
    }

    private void verifyMIC(InputStream inputStream, byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (this.cipherHelper.getProto() == 0) {
            MicToken micToken = new MicToken(this, inputStream, messageProp);
            micToken.verify(bArr, i2, i3);
            setSequencingAndReplayProps(micToken, messageProp);
        } else if (this.cipherHelper.getProto() == 1) {
            MicToken_v2 micToken_v2 = new MicToken_v2(this, inputStream, messageProp);
            micToken_v2.verify(bArr, i2, i3);
            setSequencingAndReplayProps(micToken_v2, messageProp);
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void verifyMIC(InputStream inputStream, InputStream inputStream2, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream2.available()];
            inputStream2.read(bArr);
            verifyMIC(inputStream, bArr, 0, bArr.length, messageProp);
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] export() throws GSSException {
        throw new GSSException(16, -1, "GSS Export Context not available");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void dispose() throws GSSException {
        this.state = 4;
        this.delegatedCred = null;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final Provider getProvider() {
        return Krb5MechFactory.PROVIDER;
    }

    private void setSequencingAndReplayProps(MessageToken messageToken, MessageProp messageProp) {
        if (this.replayDetState || this.sequenceDetState) {
            this.peerTokenTracker.getProps(messageToken.getSequenceNumber(), messageProp);
        }
    }

    private void setSequencingAndReplayProps(MessageToken_v2 messageToken_v2, MessageProp messageProp) {
        if (this.replayDetState || this.sequenceDetState) {
            this.peerTokenTracker.getProps(messageToken_v2.getSequenceNumber(), messageProp);
        }
    }

    private void checkPermission(String str, String str2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new ServicePermission(str, str2));
        }
    }

    private static String getHexBytes(byte[] bArr, int i2, int i3) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i4 = 0; i4 < i3; i4++) {
            int i5 = (bArr[i4] >> 4) & 15;
            int i6 = bArr[i4] & 15;
            stringBuffer.append(Integer.toHexString(i5));
            stringBuffer.append(Integer.toHexString(i6));
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }

    private static String printState(int i2) {
        switch (i2) {
            case 1:
                return "STATE_NEW";
            case 2:
                return "STATE_IN_PROCESS";
            case 3:
                return "STATE_DONE";
            case 4:
                return "STATE_DELETED";
            default:
                return "Unknown state " + i2;
        }
    }

    GSSCaller getCaller() {
        return this.caller;
    }

    /* loaded from: rt.jar:sun/security/jgss/krb5/Krb5Context$KerberosSessionKey.class */
    static class KerberosSessionKey implements Key {
        private static final long serialVersionUID = 699307378954123869L;
        private final EncryptionKey key;

        KerberosSessionKey(EncryptionKey encryptionKey) {
            this.key = encryptionKey;
        }

        @Override // java.security.Key
        public String getAlgorithm() {
            return Integer.toString(this.key.getEType());
        }

        @Override // java.security.Key
        public String getFormat() {
            return "RAW";
        }

        @Override // java.security.Key
        public byte[] getEncoded() {
            return (byte[]) this.key.getBytes().clone();
        }

        public String toString() {
            return "Kerberos session key: etype: " + this.key.getEType() + "\n" + new HexDumpEncoder().encodeBuffer(this.key.getBytes());
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public Object inquireSecContext(InquireType inquireType) throws GSSException {
        if (!isEstablished()) {
            throw new GSSException(12, -1, "Security context not established.");
        }
        switch (inquireType) {
            case KRB5_GET_SESSION_KEY:
                return new KerberosSessionKey(this.key);
            case KRB5_GET_TKT_FLAGS:
                return this.tktFlags.clone();
            case KRB5_GET_AUTHZ_DATA:
                if (isInitiator()) {
                    throw new GSSException(16, -1, "AuthzData not available on initiator side.");
                }
                if (this.authzData == null) {
                    return null;
                }
                return this.authzData.clone();
            case KRB5_GET_AUTHTIME:
                return this.authTime;
            default:
                throw new GSSException(16, -1, "Inquire type not supported.");
        }
    }

    public void setTktFlags(boolean[] zArr) {
        this.tktFlags = zArr;
    }

    public void setAuthTime(String str) {
        this.authTime = str;
    }

    public void setAuthzData(AuthorizationDataEntry[] authorizationDataEntryArr) {
        this.authzData = authorizationDataEntryArr;
    }
}
