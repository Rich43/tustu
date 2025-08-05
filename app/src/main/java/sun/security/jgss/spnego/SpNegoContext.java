package sun.security.jgss.spnego;

import com.sun.security.jgss.ExtendedGSSContext;
import com.sun.security.jgss.InquireType;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.Provider;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;
import sun.security.action.GetBooleanAction;
import sun.security.jgss.GSSCredentialImpl;
import sun.security.jgss.GSSNameImpl;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spnego.SpNegoToken;
import sun.security.util.BitArray;
import sun.security.util.DerOutputStream;

/* loaded from: rt.jar:sun/security/jgss/spnego/SpNegoContext.class */
public class SpNegoContext implements GSSContextSpi {
    private static final int STATE_NEW = 1;
    private static final int STATE_IN_PROCESS = 2;
    private static final int STATE_DONE = 3;
    private static final int STATE_DELETED = 4;
    private int state;
    private boolean credDelegState;
    private boolean mutualAuthState;
    private boolean replayDetState;
    private boolean sequenceDetState;
    private boolean confState;
    private boolean integState;
    private boolean delegPolicyState;
    private GSSNameSpi peerName;
    private GSSNameSpi myName;
    private SpNegoCredElement myCred;
    private GSSContext mechContext;
    private byte[] DER_mechTypes;
    private int lifetime;
    private ChannelBinding channelBinding;
    private boolean initiator;
    private Oid internal_mech;
    private final SpNegoMechFactory factory;
    static final boolean DEBUG = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.security.spnego.debug"))).booleanValue();

    public SpNegoContext(SpNegoMechFactory spNegoMechFactory, GSSNameSpi gSSNameSpi, GSSCredentialSpi gSSCredentialSpi, int i2) throws GSSException {
        this.state = 1;
        this.credDelegState = false;
        this.mutualAuthState = true;
        this.replayDetState = true;
        this.sequenceDetState = true;
        this.confState = true;
        this.integState = true;
        this.delegPolicyState = false;
        this.peerName = null;
        this.myName = null;
        this.myCred = null;
        this.mechContext = null;
        this.DER_mechTypes = null;
        this.internal_mech = null;
        if (gSSNameSpi == null) {
            throw new IllegalArgumentException("Cannot have null peer name");
        }
        if (gSSCredentialSpi != null && !(gSSCredentialSpi instanceof SpNegoCredElement)) {
            throw new IllegalArgumentException("Wrong cred element type");
        }
        this.peerName = gSSNameSpi;
        this.myCred = (SpNegoCredElement) gSSCredentialSpi;
        this.lifetime = i2;
        this.initiator = true;
        this.factory = spNegoMechFactory;
    }

    public SpNegoContext(SpNegoMechFactory spNegoMechFactory, GSSCredentialSpi gSSCredentialSpi) throws GSSException {
        this.state = 1;
        this.credDelegState = false;
        this.mutualAuthState = true;
        this.replayDetState = true;
        this.sequenceDetState = true;
        this.confState = true;
        this.integState = true;
        this.delegPolicyState = false;
        this.peerName = null;
        this.myName = null;
        this.myCred = null;
        this.mechContext = null;
        this.DER_mechTypes = null;
        this.internal_mech = null;
        if (gSSCredentialSpi != null && !(gSSCredentialSpi instanceof SpNegoCredElement)) {
            throw new IllegalArgumentException("Wrong cred element type");
        }
        this.myCred = (SpNegoCredElement) gSSCredentialSpi;
        this.initiator = false;
        this.factory = spNegoMechFactory;
    }

    public SpNegoContext(SpNegoMechFactory spNegoMechFactory, byte[] bArr) throws GSSException {
        this.state = 1;
        this.credDelegState = false;
        this.mutualAuthState = true;
        this.replayDetState = true;
        this.sequenceDetState = true;
        this.confState = true;
        this.integState = true;
        this.delegPolicyState = false;
        this.peerName = null;
        this.myName = null;
        this.myCred = null;
        this.mechContext = null;
        this.DER_mechTypes = null;
        this.internal_mech = null;
        throw new GSSException(16, -1, "GSS Import Context not available");
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
    public final void requestDelegPolicy(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.delegPolicyState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getIntegState() {
        return this.integState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getDelegPolicyState() {
        if (isInitiator() && this.mechContext != null && (this.mechContext instanceof ExtendedGSSContext) && (this.state == 2 || this.state == 3)) {
            return ((ExtendedGSSContext) this.mechContext).getDelegPolicyState();
        }
        return this.delegPolicyState;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestCredDeleg(boolean z2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.credDelegState = z2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getCredDelegState() {
        if (isInitiator() && this.mechContext != null && (this.state == 2 || this.state == 3)) {
            return this.mechContext.getCredDelegState();
        }
        return this.credDelegState;
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
    public final Oid getMech() {
        if (isEstablished()) {
            return getNegotiatedMech();
        }
        return SpNegoMechFactory.GSS_SPNEGO_MECH_OID;
    }

    public final Oid getNegotiatedMech() {
        return this.internal_mech;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final Provider getProvider() {
        return SpNegoMechFactory.PROVIDER;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void dispose() throws GSSException {
        this.state = 4;
        if (this.mechContext != null) {
            this.mechContext.dispose();
            this.mechContext = null;
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
        byte[] encoded = null;
        byte[] bArrGSS_initSecContext = null;
        if (DEBUG) {
            System.out.println("Entered SpNego.initSecContext with state=" + printState(this.state));
        }
        if (!isInitiator()) {
            throw new GSSException(11, -1, "initSecContext on an acceptor GSSContext");
        }
        try {
            if (this.state == 1) {
                this.state = 2;
                Oid[] availableMechs = getAvailableMechs();
                this.DER_mechTypes = getEncodedMechs(availableMechs);
                this.internal_mech = availableMechs[0];
                NegTokenInit negTokenInit = new NegTokenInit(this.DER_mechTypes, getContextFlags(), GSS_initSecContext(null), null);
                if (DEBUG) {
                    System.out.println("SpNegoContext.initSecContext: sending token of type = " + SpNegoToken.getTokenName(negTokenInit.getType()));
                }
                encoded = negTokenInit.getEncoded();
            } else if (this.state == 2) {
                if (inputStream == null) {
                    throw new GSSException(11, -1, "No token received from peer!");
                }
                byte[] bArr = new byte[inputStream.available()];
                SpNegoToken.readFully(inputStream, bArr);
                if (DEBUG) {
                    System.out.println("SpNegoContext.initSecContext: process received token = " + SpNegoToken.getHexBytes(bArr));
                }
                NegTokenTarg negTokenTarg = new NegTokenTarg(bArr);
                if (DEBUG) {
                    System.out.println("SpNegoContext.initSecContext: received token of type = " + SpNegoToken.getTokenName(negTokenTarg.getType()));
                }
                this.internal_mech = negTokenTarg.getSupportedMech();
                if (this.internal_mech == null) {
                    throw new GSSException(10, -1, "supported mechanism from server is null");
                }
                SpNegoToken.NegoResult negoResult = null;
                switch (negTokenTarg.getNegotiatedResult()) {
                    case 0:
                        negoResult = SpNegoToken.NegoResult.ACCEPT_COMPLETE;
                        this.state = 3;
                        break;
                    case 1:
                        negoResult = SpNegoToken.NegoResult.ACCEPT_INCOMPLETE;
                        this.state = 2;
                        break;
                    case 2:
                        negoResult = SpNegoToken.NegoResult.REJECT;
                        this.state = 4;
                        break;
                    default:
                        this.state = 3;
                        break;
                }
                if (negoResult == SpNegoToken.NegoResult.REJECT) {
                    throw new GSSException(2, -1, this.internal_mech.toString());
                }
                if (negoResult == SpNegoToken.NegoResult.ACCEPT_COMPLETE || negoResult == SpNegoToken.NegoResult.ACCEPT_INCOMPLETE) {
                    byte[] responseToken = negTokenTarg.getResponseToken();
                    if (responseToken == null) {
                        if (!isMechContextEstablished()) {
                            throw new GSSException(10, -1, "mechanism token from server is null");
                        }
                    } else {
                        bArrGSS_initSecContext = GSS_initSecContext(responseToken);
                    }
                    if (!GSSUtil.useMSInterop()) {
                        if (!verifyMechListMIC(this.DER_mechTypes, negTokenTarg.getMechListMIC())) {
                            throw new GSSException(10, -1, "verification of MIC on MechList Failed!");
                        }
                    }
                    if (isMechContextEstablished()) {
                        this.state = 3;
                        encoded = bArrGSS_initSecContext;
                        if (DEBUG) {
                            System.out.println("SPNEGO Negotiated Mechanism = " + ((Object) this.internal_mech) + " " + GSSUtil.getMechStr(this.internal_mech));
                        }
                    } else {
                        NegTokenInit negTokenInit2 = new NegTokenInit(null, null, bArrGSS_initSecContext, null);
                        if (DEBUG) {
                            System.out.println("SpNegoContext.initSecContext: continue sending token of type = " + SpNegoToken.getTokenName(negTokenInit2.getType()));
                        }
                        encoded = negTokenInit2.getEncoded();
                    }
                }
            } else if (DEBUG) {
                System.out.println(this.state);
            }
            if (DEBUG && encoded != null) {
                System.out.println("SNegoContext.initSecContext: sending token = " + SpNegoToken.getHexBytes(encoded));
            }
            return encoded;
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        } catch (GSSException e3) {
            GSSException gSSException2 = new GSSException(11, -1, e3.getMessage());
            gSSException2.initCause(e3);
            throw gSSException2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] acceptSecContext(InputStream inputStream, int i2) throws GSSException {
        SpNegoToken.NegoResult negoResult;
        byte[] bArrGSS_acceptSecContext;
        SpNegoToken.NegoResult negoResult2;
        byte[] encoded = null;
        boolean zVerifyMechListMIC = true;
        if (DEBUG) {
            System.out.println("Entered SpNegoContext.acceptSecContext with state=" + printState(this.state));
        }
        if (isInitiator()) {
            throw new GSSException(11, -1, "acceptSecContext on an initiator GSSContext");
        }
        try {
            if (this.state == 1) {
                this.state = 2;
                byte[] bArr = new byte[inputStream.available()];
                SpNegoToken.readFully(inputStream, bArr);
                if (DEBUG) {
                    System.out.println("SpNegoContext.acceptSecContext: receiving token = " + SpNegoToken.getHexBytes(bArr));
                }
                NegTokenInit negTokenInit = new NegTokenInit(bArr);
                if (DEBUG) {
                    System.out.println("SpNegoContext.acceptSecContext: received token of type = " + SpNegoToken.getTokenName(negTokenInit.getType()));
                }
                Oid[] mechTypeList = negTokenInit.getMechTypeList();
                this.DER_mechTypes = negTokenInit.getMechTypes();
                if (this.DER_mechTypes == null) {
                    zVerifyMechListMIC = false;
                }
                Oid oidNegotiate_mech_type = negotiate_mech_type(getAvailableMechs(), mechTypeList);
                if (oidNegotiate_mech_type == null) {
                    zVerifyMechListMIC = false;
                }
                this.internal_mech = oidNegotiate_mech_type;
                if (mechTypeList[0].equals(oidNegotiate_mech_type) || (GSSUtil.isKerberosMech(mechTypeList[0]) && GSSUtil.isKerberosMech(oidNegotiate_mech_type))) {
                    if (DEBUG && !oidNegotiate_mech_type.equals(mechTypeList[0])) {
                        System.out.println("SpNegoContext.acceptSecContext: negotiated mech adjusted to " + ((Object) mechTypeList[0]));
                    }
                    byte[] mechToken = negTokenInit.getMechToken();
                    if (mechToken == null) {
                        throw new GSSException(11, -1, "mechToken is missing");
                    }
                    bArrGSS_acceptSecContext = GSS_acceptSecContext(mechToken);
                    oidNegotiate_mech_type = mechTypeList[0];
                } else {
                    bArrGSS_acceptSecContext = null;
                }
                if (!GSSUtil.useMSInterop() && zVerifyMechListMIC) {
                    zVerifyMechListMIC = verifyMechListMIC(this.DER_mechTypes, negTokenInit.getMechListMIC());
                }
                if (zVerifyMechListMIC) {
                    if (isMechContextEstablished()) {
                        negoResult2 = SpNegoToken.NegoResult.ACCEPT_COMPLETE;
                        this.state = 3;
                        setContextFlags();
                        if (DEBUG) {
                            System.out.println("SPNEGO Negotiated Mechanism = " + ((Object) this.internal_mech) + " " + GSSUtil.getMechStr(this.internal_mech));
                        }
                    } else {
                        negoResult2 = SpNegoToken.NegoResult.ACCEPT_INCOMPLETE;
                        this.state = 2;
                    }
                } else {
                    negoResult2 = SpNegoToken.NegoResult.REJECT;
                    this.state = 3;
                }
                if (DEBUG) {
                    System.out.println("SpNegoContext.acceptSecContext: mechanism wanted = " + ((Object) oidNegotiate_mech_type));
                    System.out.println("SpNegoContext.acceptSecContext: negotiated result = " + ((Object) negoResult2));
                }
                NegTokenTarg negTokenTarg = new NegTokenTarg(negoResult2.ordinal(), oidNegotiate_mech_type, bArrGSS_acceptSecContext, null);
                if (DEBUG) {
                    System.out.println("SpNegoContext.acceptSecContext: sending token of type = " + SpNegoToken.getTokenName(negTokenTarg.getType()));
                }
                encoded = negTokenTarg.getEncoded();
            } else if (this.state == 2) {
                byte[] bArr2 = new byte[inputStream.available()];
                SpNegoToken.readFully(inputStream, bArr2);
                if (DEBUG) {
                    System.out.println("SpNegoContext.acceptSecContext: receiving token = " + SpNegoToken.getHexBytes(bArr2));
                }
                NegTokenTarg negTokenTarg2 = new NegTokenTarg(bArr2);
                if (DEBUG) {
                    System.out.println("SpNegoContext.acceptSecContext: received token of type = " + SpNegoToken.getTokenName(negTokenTarg2.getType()));
                }
                byte[] bArrGSS_acceptSecContext2 = GSS_acceptSecContext(negTokenTarg2.getResponseToken());
                if (bArrGSS_acceptSecContext2 == null) {
                    zVerifyMechListMIC = false;
                }
                if (zVerifyMechListMIC) {
                    if (isMechContextEstablished()) {
                        negoResult = SpNegoToken.NegoResult.ACCEPT_COMPLETE;
                        this.state = 3;
                    } else {
                        negoResult = SpNegoToken.NegoResult.ACCEPT_INCOMPLETE;
                        this.state = 2;
                    }
                } else {
                    negoResult = SpNegoToken.NegoResult.REJECT;
                    this.state = 3;
                }
                NegTokenTarg negTokenTarg3 = new NegTokenTarg(negoResult.ordinal(), null, bArrGSS_acceptSecContext2, null);
                if (DEBUG) {
                    System.out.println("SpNegoContext.acceptSecContext: sending token of type = " + SpNegoToken.getTokenName(negTokenTarg3.getType()));
                }
                encoded = negTokenTarg3.getEncoded();
            } else if (DEBUG) {
                System.out.println("AcceptSecContext: state = " + this.state);
            }
            if (DEBUG) {
                System.out.println("SpNegoContext.acceptSecContext: sending token = " + SpNegoToken.getHexBytes(encoded));
            }
            if (this.state == 3) {
                setContextFlags();
            }
            return encoded;
        } catch (IOException e2) {
            GSSException gSSException = new GSSException(11, -1, e2.getMessage());
            gSSException.initCause(e2);
            throw gSSException;
        }
    }

    private Oid[] getAvailableMechs() {
        if (this.myCred != null) {
            return new Oid[]{this.myCred.getInternalMech()};
        }
        return this.factory.availableMechs;
    }

    private byte[] getEncodedMechs(Oid[] oidArr) throws IOException, GSSException {
        DerOutputStream derOutputStream = new DerOutputStream();
        for (Oid oid : oidArr) {
            derOutputStream.write(oid.getDER());
        }
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write((byte) 48, derOutputStream);
        return derOutputStream2.toByteArray();
    }

    private BitArray getContextFlags() throws ArrayIndexOutOfBoundsException {
        BitArray bitArray = new BitArray(7);
        if (getCredDelegState()) {
            bitArray.set(0, true);
        }
        if (getMutualAuthState()) {
            bitArray.set(1, true);
        }
        if (getReplayDetState()) {
            bitArray.set(2, true);
        }
        if (getSequenceDetState()) {
            bitArray.set(3, true);
        }
        if (getConfState()) {
            bitArray.set(5, true);
        }
        if (getIntegState()) {
            bitArray.set(6, true);
        }
        return bitArray;
    }

    private void setContextFlags() {
        if (this.mechContext != null) {
            if (this.mechContext.getCredDelegState()) {
                this.credDelegState = true;
            }
            if (!this.mechContext.getMutualAuthState()) {
                this.mutualAuthState = false;
            }
            if (!this.mechContext.getReplayDetState()) {
                this.replayDetState = false;
            }
            if (!this.mechContext.getSequenceDetState()) {
                this.sequenceDetState = false;
            }
            if (!this.mechContext.getIntegState()) {
                this.integState = false;
            }
            if (!this.mechContext.getConfState()) {
                this.confState = false;
            }
        }
    }

    private boolean verifyMechListMIC(byte[] bArr, byte[] bArr2) throws GSSException {
        boolean z2;
        if (bArr2 == null) {
            if (DEBUG) {
                System.out.println("SpNegoContext: no MIC token validation");
                return true;
            }
            return true;
        }
        if (!this.mechContext.getIntegState()) {
            if (DEBUG) {
                System.out.println("SpNegoContext: no MIC token validation - mechanism does not support integrity");
                return true;
            }
            return true;
        }
        try {
            verifyMIC(bArr2, 0, bArr2.length, bArr, 0, bArr.length, new MessageProp(0, true));
            z2 = true;
        } catch (GSSException e2) {
            z2 = false;
            if (DEBUG) {
                System.out.println("SpNegoContext: MIC validation failed! " + e2.getMessage());
            }
        }
        return z2;
    }

    private byte[] GSS_initSecContext(byte[] bArr) throws GSSException {
        byte[] bArr2;
        if (this.mechContext == null) {
            GSSName gSSNameCreateName = this.factory.manager.createName(this.peerName.toString(), this.peerName.getStringNameType(), this.internal_mech);
            GSSCredentialImpl gSSCredentialImpl = null;
            if (this.myCred != null) {
                gSSCredentialImpl = new GSSCredentialImpl(this.factory.manager, this.myCred.getInternalCred());
            }
            this.mechContext = this.factory.manager.createContext(gSSNameCreateName, this.internal_mech, gSSCredentialImpl, 0);
            this.mechContext.requestConf(this.confState);
            this.mechContext.requestInteg(this.integState);
            this.mechContext.requestCredDeleg(this.credDelegState);
            this.mechContext.requestMutualAuth(this.mutualAuthState);
            this.mechContext.requestReplayDet(this.replayDetState);
            this.mechContext.requestSequenceDet(this.sequenceDetState);
            if (this.mechContext instanceof ExtendedGSSContext) {
                ((ExtendedGSSContext) this.mechContext).requestDelegPolicy(this.delegPolicyState);
            }
        }
        if (bArr != null) {
            bArr2 = bArr;
        } else {
            bArr2 = new byte[0];
        }
        return this.mechContext.initSecContext(bArr2, 0, bArr2.length);
    }

    private byte[] GSS_acceptSecContext(byte[] bArr) throws GSSException {
        if (this.mechContext == null) {
            GSSCredentialImpl gSSCredentialImpl = null;
            if (this.myCred != null) {
                gSSCredentialImpl = new GSSCredentialImpl(this.factory.manager, this.myCred.getInternalCred());
            }
            this.mechContext = this.factory.manager.createContext(gSSCredentialImpl);
        }
        return this.mechContext.acceptSecContext(bArr, 0, bArr.length);
    }

    private static Oid negotiate_mech_type(Oid[] oidArr, Oid[] oidArr2) {
        for (Oid oid : oidArr) {
            for (int i2 = 0; i2 < oidArr2.length; i2++) {
                if (oidArr2[i2].equals(oid)) {
                    if (DEBUG) {
                        System.out.println("SpNegoContext: negotiated mechanism = " + ((Object) oidArr2[i2]));
                    }
                    return oidArr2[i2];
                }
            }
        }
        return null;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean isEstablished() {
        return this.state == 3;
    }

    public final boolean isMechContextEstablished() {
        if (this.mechContext != null) {
            return this.mechContext.isEstablished();
        }
        if (DEBUG) {
            System.out.println("The underlying mechanism context has not been initialized");
            return false;
        }
        return false;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] export() throws GSSException {
        throw new GSSException(16, -1, "GSS Export Context not available");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void setChannelBinding(ChannelBinding channelBinding) throws GSSException {
        this.channelBinding = channelBinding;
    }

    final ChannelBinding getChannelBinding() {
        return this.channelBinding;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void requestAnonymity(boolean z2) throws GSSException {
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean getAnonymityState() {
        return false;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestLifetime(int i2) throws GSSException {
        if (this.state == 1 && isInitiator()) {
            this.lifetime = i2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final int getLifetime() {
        if (this.mechContext != null) {
            return this.mechContext.getLifetime();
        }
        return Integer.MAX_VALUE;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final boolean isTransferable() throws GSSException {
        return false;
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
    public final GSSNameSpi getTargName() throws GSSException {
        if (this.mechContext != null) {
            this.peerName = ((GSSNameImpl) this.mechContext.getTargName()).getElement(this.internal_mech);
            return this.peerName;
        }
        if (DEBUG) {
            System.out.println("The underlying mechanism context has not been initialized");
            return null;
        }
        return null;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final GSSNameSpi getSrcName() throws GSSException {
        if (this.mechContext != null) {
            this.myName = ((GSSNameImpl) this.mechContext.getSrcName()).getElement(this.internal_mech);
            return this.myName;
        }
        if (DEBUG) {
            System.out.println("The underlying mechanism context has not been initialized");
            return null;
        }
        return null;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final GSSCredentialSpi getDelegCred() throws GSSException {
        if (this.state != 2 && this.state != 3) {
            throw new GSSException(12);
        }
        if (this.mechContext != null) {
            GSSCredentialImpl gSSCredentialImpl = (GSSCredentialImpl) this.mechContext.getDelegCred();
            if (gSSCredentialImpl == null) {
                return null;
            }
            boolean z2 = false;
            if (gSSCredentialImpl.getUsage() == 1) {
                z2 = true;
            }
            return new SpNegoCredElement(gSSCredentialImpl.getElement(this.internal_mech, z2)).getInternalCred();
        }
        throw new GSSException(12, -1, "getDelegCred called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final int getWrapSizeLimit(int i2, boolean z2, int i3) throws GSSException {
        if (this.mechContext != null) {
            return this.mechContext.getWrapSizeLimit(i2, z2, i3);
        }
        throw new GSSException(12, -1, "getWrapSizeLimit called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] wrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            return this.mechContext.wrap(bArr, i2, i3, messageProp);
        }
        throw new GSSException(12, -1, "Wrap called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void wrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            this.mechContext.wrap(inputStream, outputStream, messageProp);
            return;
        }
        throw new GSSException(12, -1, "Wrap called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] unwrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            return this.mechContext.unwrap(bArr, i2, i3, messageProp);
        }
        throw new GSSException(12, -1, "UnWrap called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void unwrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            this.mechContext.unwrap(inputStream, outputStream, messageProp);
            return;
        }
        throw new GSSException(12, -1, "UnWrap called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final byte[] getMIC(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            return this.mechContext.getMIC(bArr, i2, i3, messageProp);
        }
        throw new GSSException(12, -1, "getMIC called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void getMIC(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            this.mechContext.getMIC(inputStream, outputStream, messageProp);
            return;
        }
        throw new GSSException(12, -1, "getMIC called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void verifyMIC(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            this.mechContext.verifyMIC(bArr, i2, i3, bArr2, i4, i5, messageProp);
            return;
        }
        throw new GSSException(12, -1, "verifyMIC called in invalid state!");
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public final void verifyMIC(InputStream inputStream, InputStream inputStream2, MessageProp messageProp) throws GSSException {
        if (this.mechContext != null) {
            this.mechContext.verifyMIC(inputStream, inputStream2, messageProp);
            return;
        }
        throw new GSSException(12, -1, "verifyMIC called in invalid state!");
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

    @Override // sun.security.jgss.spi.GSSContextSpi
    public Object inquireSecContext(InquireType inquireType) throws GSSException {
        if (this.mechContext == null) {
            throw new GSSException(12, -1, "Underlying mech not established.");
        }
        if (this.mechContext instanceof ExtendedGSSContext) {
            return ((ExtendedGSSContext) this.mechContext).inquireSecContext(inquireType);
        }
        throw new GSSException(2, -1, "inquireSecContext not supported by underlying mech.");
    }
}
