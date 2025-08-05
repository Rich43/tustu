package sun.security.jgss.wrapper;

import com.sun.security.jgss.InquireType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import javax.security.auth.kerberos.DelegationPermission;
import org.icepdf.core.util.PdfOps;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;
import sun.security.jgss.GSSExceptionImpl;
import sun.security.jgss.GSSHeader;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.jgss.spi.GSSNameSpi;
import sun.security.jgss.spnego.NegTokenInit;
import sun.security.jgss.spnego.NegTokenTarg;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/security/jgss/wrapper/NativeGSSContext.class */
class NativeGSSContext implements GSSContextSpi {
    private static final int GSS_C_DELEG_FLAG = 1;
    private static final int GSS_C_MUTUAL_FLAG = 2;
    private static final int GSS_C_REPLAY_FLAG = 4;
    private static final int GSS_C_SEQUENCE_FLAG = 8;
    private static final int GSS_C_CONF_FLAG = 16;
    private static final int GSS_C_INTEG_FLAG = 32;
    private static final int GSS_C_ANON_FLAG = 64;
    private static final int GSS_C_PROT_READY_FLAG = 128;
    private static final int GSS_C_TRANS_FLAG = 256;
    private static final int NUM_OF_INQUIRE_VALUES = 6;
    private long pContext;
    private GSSNameElement srcName;
    private GSSNameElement targetName;
    private GSSCredElement cred;
    private boolean isInitiator;
    private boolean isEstablished;
    private Oid actualMech;
    private ChannelBinding cb;
    private GSSCredElement delegatedCred;
    private int flags;
    private int lifetime;
    private final GSSLibStub cStub;
    private boolean skipDelegPermCheck;
    private boolean skipServicePermCheck;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !NativeGSSContext.class.desiredAssertionStatus();
    }

    private static Oid getMechFromSpNegoToken(byte[] bArr, boolean z2) throws GSSException {
        Oid supportedMech = null;
        if (z2) {
            try {
                int mechTokenLength = new GSSHeader(new ByteArrayInputStream(bArr)).getMechTokenLength();
                byte[] bArr2 = new byte[mechTokenLength];
                System.arraycopy(bArr, bArr.length - mechTokenLength, bArr2, 0, bArr2.length);
                NegTokenInit negTokenInit = new NegTokenInit(bArr2);
                if (negTokenInit.getMechToken() != null) {
                    supportedMech = negTokenInit.getMechTypeList()[0];
                }
            } catch (IOException e2) {
                throw new GSSExceptionImpl(11, e2);
            }
        } else {
            supportedMech = new NegTokenTarg(bArr).getSupportedMech();
        }
        return supportedMech;
    }

    private void doServicePermCheck() throws GSSException {
        if (System.getSecurityManager() != null) {
            String str = this.isInitiator ? "initiate" : SecurityConstants.SOCKET_ACCEPT_ACTION;
            if (GSSUtil.isSpNegoMech(this.cStub.getMech()) && this.isInitiator && !this.isEstablished) {
                if (this.srcName == null) {
                    new GSSCredElement(null, this.lifetime, 1, GSSLibStub.getInstance(GSSUtil.GSS_KRB5_MECH_OID)).dispose();
                } else {
                    Krb5Util.checkServicePermission(Krb5Util.getTGSName(this.srcName), str);
                }
            }
            Krb5Util.checkServicePermission(this.targetName.getKrbName(), str);
            this.skipServicePermCheck = true;
        }
    }

    private void doDelegPermCheck() throws GSSException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            String krbName = this.targetName.getKrbName();
            String tGSName = Krb5Util.getTGSName(this.targetName);
            StringBuffer stringBuffer = new StringBuffer(PdfOps.DOUBLE_QUOTE__TOKEN);
            stringBuffer.append(krbName).append("\" \"");
            stringBuffer.append(tGSName).append('\"');
            String string = stringBuffer.toString();
            SunNativeProvider.debug("Checking DelegationPermission (" + string + ")");
            securityManager.checkPermission(new DelegationPermission(string));
            this.skipDelegPermCheck = true;
        }
    }

    private byte[] retrieveToken(InputStream inputStream, int i2) throws GSSException {
        byte[] byteArray;
        try {
            if (i2 != -1) {
                SunNativeProvider.debug("Precomputed mechToken length: " + i2);
                GSSHeader gSSHeader = new GSSHeader(new ObjectIdentifier(this.cStub.getMech().toString()), i2);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(600);
                byte[] bArr = new byte[i2];
                int i3 = inputStream.read(bArr);
                if (!$assertionsDisabled && i2 != i3) {
                    throw new AssertionError();
                }
                gSSHeader.encode(byteArrayOutputStream);
                byteArrayOutputStream.write(bArr);
                byteArray = byteArrayOutputStream.toByteArray();
            } else {
                if (!$assertionsDisabled && i2 != -1) {
                    throw new AssertionError();
                }
                byteArray = new DerValue(inputStream).toByteArray();
            }
            SunNativeProvider.debug("Complete Token length: " + byteArray.length);
            return byteArray;
        } catch (IOException e2) {
            throw new GSSExceptionImpl(11, e2);
        }
    }

    NativeGSSContext(GSSNameElement gSSNameElement, GSSCredElement gSSCredElement, int i2, GSSLibStub gSSLibStub) throws GSSException {
        this.pContext = 0L;
        this.lifetime = 0;
        if (gSSNameElement == null) {
            throw new GSSException(11, 1, "null peer");
        }
        this.cStub = gSSLibStub;
        this.cred = gSSCredElement;
        this.targetName = gSSNameElement;
        this.isInitiator = true;
        this.lifetime = i2;
        if (GSSUtil.isKerberosMech(this.cStub.getMech())) {
            doServicePermCheck();
            if (this.cred == null) {
                this.cred = new GSSCredElement(null, this.lifetime, 1, this.cStub);
            }
            this.srcName = this.cred.getName();
        }
    }

    NativeGSSContext(GSSCredElement gSSCredElement, GSSLibStub gSSLibStub) throws GSSException {
        this.pContext = 0L;
        this.lifetime = 0;
        this.cStub = gSSLibStub;
        this.cred = gSSCredElement;
        if (this.cred != null) {
            this.targetName = this.cred.getName();
        }
        this.isInitiator = false;
        if (GSSUtil.isKerberosMech(this.cStub.getMech()) && this.targetName != null) {
            doServicePermCheck();
        }
    }

    NativeGSSContext(long j2, GSSLibStub gSSLibStub) throws GSSException {
        this.pContext = 0L;
        this.lifetime = 0;
        if (!$assertionsDisabled && this.pContext == 0) {
            throw new AssertionError();
        }
        this.pContext = j2;
        this.cStub = gSSLibStub;
        long[] jArrInquireContext = this.cStub.inquireContext(this.pContext);
        if (jArrInquireContext.length != 6) {
            throw new RuntimeException("Bug w/ GSSLibStub.inquireContext()");
        }
        this.srcName = new GSSNameElement(jArrInquireContext[0], this.cStub);
        this.targetName = new GSSNameElement(jArrInquireContext[1], this.cStub);
        this.isInitiator = jArrInquireContext[2] != 0;
        this.isEstablished = jArrInquireContext[3] != 0;
        this.flags = (int) jArrInquireContext[4];
        this.lifetime = (int) jArrInquireContext[5];
        Oid mech = this.cStub.getMech();
        if (GSSUtil.isSpNegoMech(mech) || GSSUtil.isKerberosMech(mech)) {
            doServicePermCheck();
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public Provider getProvider() {
        return SunNativeProvider.INSTANCE;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public byte[] initSecContext(InputStream inputStream, int i2) throws GSSException {
        byte[] bArrInitContext = null;
        if (!this.isEstablished && this.isInitiator) {
            byte[] bArrRetrieveToken = null;
            if (this.pContext != 0) {
                bArrRetrieveToken = retrieveToken(inputStream, i2);
                SunNativeProvider.debug("initSecContext=> inToken len=" + bArrRetrieveToken.length);
            }
            if (!getCredDelegState()) {
                this.skipDelegPermCheck = true;
            }
            if (GSSUtil.isKerberosMech(this.cStub.getMech()) && !this.skipDelegPermCheck) {
                doDelegPermCheck();
            }
            bArrInitContext = this.cStub.initContext(this.cred == null ? 0L : this.cred.pCred, this.targetName.pName, this.cb, bArrRetrieveToken, this);
            SunNativeProvider.debug("initSecContext=> outToken len=" + (bArrInitContext == null ? 0 : bArrInitContext.length));
            if (GSSUtil.isSpNegoMech(this.cStub.getMech()) && bArrInitContext != null) {
                this.actualMech = getMechFromSpNegoToken(bArrInitContext, true);
                if (GSSUtil.isKerberosMech(this.actualMech)) {
                    if (!this.skipServicePermCheck) {
                        doServicePermCheck();
                    }
                    if (!this.skipDelegPermCheck) {
                        doDelegPermCheck();
                    }
                }
            }
            if (this.isEstablished) {
                if (this.srcName == null) {
                    this.srcName = new GSSNameElement(this.cStub.getContextName(this.pContext, true), this.cStub);
                }
                if (this.cred == null) {
                    this.cred = new GSSCredElement(this.srcName, this.lifetime, 1, this.cStub);
                }
            }
        }
        return bArrInitContext;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public byte[] acceptSecContext(InputStream inputStream, int i2) throws GSSException {
        byte[] bArrAcceptContext = null;
        if (!this.isEstablished && !this.isInitiator) {
            byte[] bArrRetrieveToken = retrieveToken(inputStream, i2);
            SunNativeProvider.debug("acceptSecContext=> inToken len=" + bArrRetrieveToken.length);
            bArrAcceptContext = this.cStub.acceptContext(this.cred == null ? 0L : this.cred.pCred, this.cb, bArrRetrieveToken, this);
            SunNativeProvider.debug("acceptSecContext=> outToken len=" + (bArrAcceptContext == null ? 0 : bArrAcceptContext.length));
            if (this.targetName == null) {
                this.targetName = new GSSNameElement(this.cStub.getContextName(this.pContext, false), this.cStub);
                if (this.cred != null) {
                    this.cred.dispose();
                }
                this.cred = new GSSCredElement(this.targetName, this.lifetime, 2, this.cStub);
            }
            if (GSSUtil.isSpNegoMech(this.cStub.getMech()) && bArrAcceptContext != null && !this.skipServicePermCheck && GSSUtil.isKerberosMech(getMechFromSpNegoToken(bArrAcceptContext, false))) {
                doServicePermCheck();
            }
        }
        return bArrAcceptContext;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean isEstablished() {
        return this.isEstablished;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void dispose() throws GSSException {
        this.srcName = null;
        this.targetName = null;
        this.cred = null;
        this.delegatedCred = null;
        if (this.pContext != 0) {
            this.pContext = this.cStub.deleteContext(this.pContext);
            this.pContext = 0L;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public int getWrapSizeLimit(int i2, boolean z2, int i3) throws GSSException {
        return this.cStub.wrapSizeLimit(this.pContext, z2 ? 1 : 0, i2, i3);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public byte[] wrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        byte[] bArr2 = bArr;
        if (i2 != 0 || i3 != bArr.length) {
            bArr2 = new byte[i3];
            System.arraycopy(bArr, i2, bArr2, 0, i3);
        }
        return this.cStub.wrap(this.pContext, bArr2, messageProp);
    }

    public void wrap(byte[] bArr, int i2, int i3, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        try {
            outputStream.write(wrap(bArr, i2, i3, messageProp));
        } catch (IOException e2) {
            throw new GSSExceptionImpl(11, e2);
        }
    }

    public int wrap(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, MessageProp messageProp) throws GSSException {
        byte[] bArrWrap = wrap(bArr, i2, i3, messageProp);
        System.arraycopy(bArrWrap, 0, bArr2, i4, bArrWrap.length);
        return bArrWrap.length;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void wrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream.available()];
            outputStream.write(wrap(bArr, 0, inputStream.read(bArr), messageProp));
        } catch (IOException e2) {
            throw new GSSExceptionImpl(11, e2);
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public byte[] unwrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (i2 != 0 || i3 != bArr.length) {
            byte[] bArr2 = new byte[i3];
            System.arraycopy(bArr, i2, bArr2, 0, i3);
            return this.cStub.unwrap(this.pContext, bArr2, messageProp);
        }
        return this.cStub.unwrap(this.pContext, bArr, messageProp);
    }

    public int unwrap(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, MessageProp messageProp) throws GSSException {
        byte[] bArrUnwrap;
        if (i2 != 0 || i3 != bArr.length) {
            byte[] bArr3 = new byte[i3];
            System.arraycopy(bArr, i2, bArr3, 0, i3);
            bArrUnwrap = this.cStub.unwrap(this.pContext, bArr3, messageProp);
        } else {
            bArrUnwrap = this.cStub.unwrap(this.pContext, bArr, messageProp);
        }
        System.arraycopy(bArrUnwrap, 0, bArr2, i4, bArrUnwrap.length);
        return bArrUnwrap.length;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void unwrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream.available()];
            outputStream.write(unwrap(bArr, 0, inputStream.read(bArr), messageProp));
            outputStream.flush();
        } catch (IOException e2) {
            throw new GSSExceptionImpl(11, e2);
        }
    }

    public int unwrap(InputStream inputStream, byte[] bArr, int i2, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr2 = new byte[inputStream.available()];
            int i3 = inputStream.read(bArr2);
            unwrap(bArr2, 0, i3, messageProp);
            byte[] bArrUnwrap = unwrap(bArr2, 0, i3, messageProp);
            System.arraycopy(bArrUnwrap, 0, bArr, i2, bArrUnwrap.length);
            return bArrUnwrap.length;
        } catch (IOException e2) {
            throw new GSSExceptionImpl(11, e2);
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public byte[] getMIC(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        int qop = messageProp == null ? 0 : messageProp.getQOP();
        byte[] bArr2 = bArr;
        if (i2 != 0 || i3 != bArr.length) {
            bArr2 = new byte[i3];
            System.arraycopy(bArr, i2, bArr2, 0, i3);
        }
        return this.cStub.getMic(this.pContext, qop, bArr2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void getMIC(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream.available()];
            byte[] mic = getMIC(bArr, 0, inputStream.read(bArr), messageProp);
            if (mic != null && mic.length != 0) {
                outputStream.write(mic);
            }
        } catch (IOException e2) {
            throw new GSSExceptionImpl(11, e2);
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void verifyMIC(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, MessageProp messageProp) throws GSSException {
        byte[] bArr3 = bArr;
        byte[] bArr4 = bArr2;
        if (i2 != 0 || i3 != bArr.length) {
            bArr3 = new byte[i3];
            System.arraycopy(bArr, i2, bArr3, 0, i3);
        }
        if (i4 != 0 || i5 != bArr2.length) {
            bArr4 = new byte[i5];
            System.arraycopy(bArr2, i4, bArr4, 0, i5);
        }
        this.cStub.verifyMic(this.pContext, bArr3, bArr4, messageProp);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void verifyMIC(InputStream inputStream, InputStream inputStream2, MessageProp messageProp) throws GSSException {
        try {
            byte[] bArr = new byte[inputStream2.available()];
            int i2 = inputStream2.read(bArr);
            byte[] bArr2 = new byte[inputStream.available()];
            verifyMIC(bArr2, 0, inputStream.read(bArr2), bArr, 0, i2, messageProp);
        } catch (IOException e2) {
            throw new GSSExceptionImpl(11, e2);
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public byte[] export() throws GSSException {
        byte[] bArrExportContext = this.cStub.exportContext(this.pContext);
        this.pContext = 0L;
        return bArrExportContext;
    }

    private void changeFlags(int i2, boolean z2) {
        if (this.isInitiator && this.pContext == 0) {
            if (z2) {
                this.flags |= i2;
            } else {
                this.flags &= i2 ^ (-1);
            }
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestMutualAuth(boolean z2) throws GSSException {
        changeFlags(2, z2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestReplayDet(boolean z2) throws GSSException {
        changeFlags(4, z2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestSequenceDet(boolean z2) throws GSSException {
        changeFlags(8, z2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestCredDeleg(boolean z2) throws GSSException {
        changeFlags(1, z2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestAnonymity(boolean z2) throws GSSException {
        changeFlags(64, z2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestConf(boolean z2) throws GSSException {
        changeFlags(16, z2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestInteg(boolean z2) throws GSSException {
        changeFlags(32, z2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestDelegPolicy(boolean z2) throws GSSException {
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void requestLifetime(int i2) throws GSSException {
        if (this.isInitiator && this.pContext == 0) {
            this.lifetime = i2;
        }
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public void setChannelBinding(ChannelBinding channelBinding) throws GSSException {
        if (this.pContext == 0) {
            this.cb = channelBinding;
        }
    }

    private boolean checkFlags(int i2) {
        return (this.flags & i2) != 0;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getCredDelegState() {
        return checkFlags(1);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getMutualAuthState() {
        return checkFlags(2);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getReplayDetState() {
        return checkFlags(4);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getSequenceDetState() {
        return checkFlags(8);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getAnonymityState() {
        return checkFlags(64);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean isTransferable() throws GSSException {
        return checkFlags(256);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean isProtReady() {
        return checkFlags(128);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getConfState() {
        return checkFlags(16);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getIntegState() {
        return checkFlags(32);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean getDelegPolicyState() {
        return false;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public int getLifetime() {
        return this.cStub.getContextTime(this.pContext);
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public GSSNameSpi getSrcName() throws GSSException {
        return this.srcName;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public GSSNameSpi getTargName() throws GSSException {
        return this.targetName;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public Oid getMech() throws GSSException {
        if (this.isEstablished && this.actualMech != null) {
            return this.actualMech;
        }
        return this.cStub.getMech();
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public GSSCredentialSpi getDelegCred() throws GSSException {
        return this.delegatedCred;
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public boolean isInitiator() {
        return this.isInitiator;
    }

    protected void finalize() throws Throwable {
        dispose();
    }

    @Override // sun.security.jgss.spi.GSSContextSpi
    public Object inquireSecContext(InquireType inquireType) throws GSSException {
        throw new GSSException(16, -1, "Inquire type not supported.");
    }
}
