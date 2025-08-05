package sun.security.jgss;

import com.sun.security.jgss.ExtendedGSSContext;
import com.sun.security.jgss.InquireSecContextPermission;
import com.sun.security.jgss.InquireType;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.ietf.jgss.ChannelBinding;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;
import org.ietf.jgss.Oid;
import sun.security.jgss.spi.GSSContextSpi;
import sun.security.jgss.spi.GSSCredentialSpi;
import sun.security.util.ObjectIdentifier;

/* loaded from: rt.jar:sun/security/jgss/GSSContextImpl.class */
class GSSContextImpl implements ExtendedGSSContext {
    private final GSSManagerImpl gssManager;
    private final boolean initiator;
    private static final int PRE_INIT = 1;
    private static final int IN_PROGRESS = 2;
    private static final int READY = 3;
    private static final int DELETED = 4;
    private int currentState;
    private GSSContextSpi mechCtxt;
    private Oid mechOid;
    private ObjectIdentifier objId;
    private GSSCredentialImpl myCred;
    private GSSNameImpl srcName;
    private GSSNameImpl targName;
    private int reqLifetime;
    private ChannelBinding channelBindings;
    private boolean reqConfState;
    private boolean reqIntegState;
    private boolean reqMutualAuthState;
    private boolean reqReplayDetState;
    private boolean reqSequenceDetState;
    private boolean reqCredDelegState;
    private boolean reqAnonState;
    private boolean reqDelegPolicyState;

    public GSSContextImpl(GSSManagerImpl gSSManagerImpl, GSSName gSSName, Oid oid, GSSCredential gSSCredential, int i2) throws GSSException {
        this.currentState = 1;
        this.mechCtxt = null;
        this.mechOid = null;
        this.objId = null;
        this.myCred = null;
        this.srcName = null;
        this.targName = null;
        this.reqLifetime = Integer.MAX_VALUE;
        this.channelBindings = null;
        this.reqConfState = true;
        this.reqIntegState = true;
        this.reqMutualAuthState = true;
        this.reqReplayDetState = true;
        this.reqSequenceDetState = true;
        this.reqCredDelegState = false;
        this.reqAnonState = false;
        this.reqDelegPolicyState = false;
        if (gSSName == null || !(gSSName instanceof GSSNameImpl)) {
            throw new GSSException(3);
        }
        oid = oid == null ? ProviderList.DEFAULT_MECH_OID : oid;
        this.gssManager = gSSManagerImpl;
        this.myCred = (GSSCredentialImpl) gSSCredential;
        this.reqLifetime = i2;
        this.targName = (GSSNameImpl) gSSName;
        this.mechOid = oid;
        this.initiator = true;
    }

    public GSSContextImpl(GSSManagerImpl gSSManagerImpl, GSSCredential gSSCredential) throws GSSException {
        this.currentState = 1;
        this.mechCtxt = null;
        this.mechOid = null;
        this.objId = null;
        this.myCred = null;
        this.srcName = null;
        this.targName = null;
        this.reqLifetime = Integer.MAX_VALUE;
        this.channelBindings = null;
        this.reqConfState = true;
        this.reqIntegState = true;
        this.reqMutualAuthState = true;
        this.reqReplayDetState = true;
        this.reqSequenceDetState = true;
        this.reqCredDelegState = false;
        this.reqAnonState = false;
        this.reqDelegPolicyState = false;
        this.gssManager = gSSManagerImpl;
        this.myCred = (GSSCredentialImpl) gSSCredential;
        this.initiator = false;
    }

    public GSSContextImpl(GSSManagerImpl gSSManagerImpl, byte[] bArr) throws GSSException {
        this.currentState = 1;
        this.mechCtxt = null;
        this.mechOid = null;
        this.objId = null;
        this.myCred = null;
        this.srcName = null;
        this.targName = null;
        this.reqLifetime = Integer.MAX_VALUE;
        this.channelBindings = null;
        this.reqConfState = true;
        this.reqIntegState = true;
        this.reqMutualAuthState = true;
        this.reqReplayDetState = true;
        this.reqSequenceDetState = true;
        this.reqCredDelegState = false;
        this.reqAnonState = false;
        this.reqDelegPolicyState = false;
        this.gssManager = gSSManagerImpl;
        this.mechCtxt = gSSManagerImpl.getMechanismContext(bArr);
        this.initiator = this.mechCtxt.isInitiator();
        this.mechOid = this.mechCtxt.getMech();
    }

    @Override // org.ietf.jgss.GSSContext
    public byte[] initSecContext(byte[] bArr, int i2, int i3) throws GSSException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(600);
        if (initSecContext(new ByteArrayInputStream(bArr, i2, i3), byteArrayOutputStream) == 0) {
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override // org.ietf.jgss.GSSContext
    public int initSecContext(InputStream inputStream, OutputStream outputStream) throws GSSException {
        if (this.mechCtxt != null && this.currentState != 2) {
            throw new GSSExceptionImpl(11, "Illegal call to initSecContext");
        }
        int mechTokenLength = -1;
        GSSCredentialSpi element = null;
        boolean z2 = false;
        try {
            if (this.mechCtxt == null) {
                if (this.myCred != null) {
                    try {
                        element = this.myCred.getElement(this.mechOid, true);
                    } catch (GSSException e2) {
                        if (GSSUtil.isSpNegoMech(this.mechOid) && e2.getMajor() == 13) {
                            element = this.myCred.getElement(this.myCred.getMechs()[0], true);
                        } else {
                            throw e2;
                        }
                    }
                }
                this.mechCtxt = this.gssManager.getMechanismContext(this.targName.getElement(this.mechOid), element, this.reqLifetime, this.mechOid);
                this.mechCtxt.requestConf(this.reqConfState);
                this.mechCtxt.requestInteg(this.reqIntegState);
                this.mechCtxt.requestCredDeleg(this.reqCredDelegState);
                this.mechCtxt.requestMutualAuth(this.reqMutualAuthState);
                this.mechCtxt.requestReplayDet(this.reqReplayDetState);
                this.mechCtxt.requestSequenceDet(this.reqSequenceDetState);
                this.mechCtxt.requestAnonymity(this.reqAnonState);
                this.mechCtxt.setChannelBinding(this.channelBindings);
                this.mechCtxt.requestDelegPolicy(this.reqDelegPolicyState);
                this.objId = new ObjectIdentifier(this.mechOid.toString());
                this.currentState = 2;
                z2 = true;
            } else if (!this.mechCtxt.getProvider().getName().equals("SunNativeGSS") && !GSSUtil.isSpNegoMech(this.mechOid)) {
                GSSHeader gSSHeader = new GSSHeader(inputStream);
                if (!gSSHeader.getOid().equals((Object) this.objId)) {
                    throw new GSSExceptionImpl(10, "Mechanism not equal to " + this.mechOid.toString() + " in initSecContext token");
                }
                mechTokenLength = gSSHeader.getMechTokenLength();
            }
            byte[] bArrInitSecContext = this.mechCtxt.initSecContext(inputStream, mechTokenLength);
            int length = 0;
            if (bArrInitSecContext != null) {
                length = bArrInitSecContext.length;
                if (!this.mechCtxt.getProvider().getName().equals("SunNativeGSS") && (z2 || !GSSUtil.isSpNegoMech(this.mechOid))) {
                    length += new GSSHeader(this.objId, bArrInitSecContext.length).encode(outputStream);
                }
                outputStream.write(bArrInitSecContext);
            }
            if (this.mechCtxt.isEstablished()) {
                this.currentState = 3;
            }
            return length;
        } catch (IOException e3) {
            throw new GSSExceptionImpl(10, e3.getMessage());
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public byte[] acceptSecContext(byte[] bArr, int i2, int i3) throws GSSException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        acceptSecContext(new ByteArrayInputStream(bArr, i2, i3), byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        if (byteArray.length == 0) {
            return null;
        }
        return byteArray;
    }

    @Override // org.ietf.jgss.GSSContext
    public void acceptSecContext(InputStream inputStream, OutputStream outputStream) throws GSSException {
        if (this.mechCtxt != null && this.currentState != 2) {
            throw new GSSExceptionImpl(11, "Illegal call to acceptSecContext");
        }
        int mechTokenLength = -1;
        GSSCredentialSpi element = null;
        try {
            if (this.mechCtxt == null) {
                GSSHeader gSSHeader = new GSSHeader(inputStream);
                mechTokenLength = gSSHeader.getMechTokenLength();
                this.objId = gSSHeader.getOid();
                this.mechOid = new Oid(this.objId.toString());
                if (this.myCred != null) {
                    element = this.myCred.getElement(this.mechOid, false);
                }
                this.mechCtxt = this.gssManager.getMechanismContext(element, this.mechOid);
                this.mechCtxt.setChannelBinding(this.channelBindings);
                this.currentState = 2;
            } else if (!this.mechCtxt.getProvider().getName().equals("SunNativeGSS") && !GSSUtil.isSpNegoMech(this.mechOid)) {
                GSSHeader gSSHeader2 = new GSSHeader(inputStream);
                if (!gSSHeader2.getOid().equals((Object) this.objId)) {
                    throw new GSSExceptionImpl(10, "Mechanism not equal to " + this.mechOid.toString() + " in acceptSecContext token");
                }
                mechTokenLength = gSSHeader2.getMechTokenLength();
            }
            byte[] bArrAcceptSecContext = this.mechCtxt.acceptSecContext(inputStream, mechTokenLength);
            if (bArrAcceptSecContext != null) {
                int length = bArrAcceptSecContext.length;
                if (!this.mechCtxt.getProvider().getName().equals("SunNativeGSS") && !GSSUtil.isSpNegoMech(this.mechOid)) {
                    int iEncode = length + new GSSHeader(this.objId, bArrAcceptSecContext.length).encode(outputStream);
                }
                outputStream.write(bArrAcceptSecContext);
            }
            if (this.mechCtxt.isEstablished()) {
                this.currentState = 3;
            }
        } catch (IOException e2) {
            throw new GSSExceptionImpl(10, e2.getMessage());
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean isEstablished() {
        return this.mechCtxt != null && this.currentState == 3;
    }

    @Override // org.ietf.jgss.GSSContext
    public int getWrapSizeLimit(int i2, boolean z2, int i3) throws GSSException {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getWrapSizeLimit(i2, z2, i3);
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public byte[] wrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            return this.mechCtxt.wrap(bArr, i2, i3, messageProp);
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public void wrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            this.mechCtxt.wrap(inputStream, outputStream, messageProp);
            return;
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public byte[] unwrap(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            return this.mechCtxt.unwrap(bArr, i2, i3, messageProp);
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public void unwrap(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            this.mechCtxt.unwrap(inputStream, outputStream, messageProp);
            return;
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public byte[] getMIC(byte[] bArr, int i2, int i3, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getMIC(bArr, i2, i3, messageProp);
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public void getMIC(InputStream inputStream, OutputStream outputStream, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            this.mechCtxt.getMIC(inputStream, outputStream, messageProp);
            return;
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public void verifyMIC(byte[] bArr, int i2, int i3, byte[] bArr2, int i4, int i5, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            this.mechCtxt.verifyMIC(bArr, i2, i3, bArr2, i4, i5, messageProp);
            return;
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public void verifyMIC(InputStream inputStream, InputStream inputStream2, MessageProp messageProp) throws GSSException {
        if (this.mechCtxt != null) {
            this.mechCtxt.verifyMIC(inputStream, inputStream2, messageProp);
            return;
        }
        throw new GSSExceptionImpl(12, "No mechanism context yet!");
    }

    @Override // org.ietf.jgss.GSSContext
    public byte[] export() throws GSSException {
        byte[] bArrExport = null;
        if (this.mechCtxt.isTransferable() && this.mechCtxt.getProvider().getName().equals("SunNativeGSS")) {
            bArrExport = this.mechCtxt.export();
        }
        return bArrExport;
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestMutualAuth(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqMutualAuthState = z2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestReplayDet(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqReplayDetState = z2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestSequenceDet(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqSequenceDetState = z2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestCredDeleg(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqCredDelegState = z2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestAnonymity(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqAnonState = z2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestConf(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqConfState = z2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestInteg(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqIntegState = z2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void requestLifetime(int i2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqLifetime = i2;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public void setChannelBinding(ChannelBinding channelBinding) throws GSSException {
        if (this.mechCtxt == null) {
            this.channelBindings = channelBinding;
        }
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean getCredDelegState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getCredDelegState();
        }
        return this.reqCredDelegState;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean getMutualAuthState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getMutualAuthState();
        }
        return this.reqMutualAuthState;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean getReplayDetState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getReplayDetState();
        }
        return this.reqReplayDetState;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean getSequenceDetState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getSequenceDetState();
        }
        return this.reqSequenceDetState;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean getAnonymityState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getAnonymityState();
        }
        return this.reqAnonState;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean isTransferable() throws GSSException {
        if (this.mechCtxt != null) {
            return this.mechCtxt.isTransferable();
        }
        return false;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean isProtReady() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.isProtReady();
        }
        return false;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean getConfState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getConfState();
        }
        return this.reqConfState;
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean getIntegState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getIntegState();
        }
        return this.reqIntegState;
    }

    @Override // org.ietf.jgss.GSSContext
    public int getLifetime() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getLifetime();
        }
        return this.reqLifetime;
    }

    @Override // org.ietf.jgss.GSSContext
    public GSSName getSrcName() throws GSSException {
        if (this.srcName == null) {
            this.srcName = GSSNameImpl.wrapElement(this.gssManager, this.mechCtxt.getSrcName());
        }
        return this.srcName;
    }

    @Override // org.ietf.jgss.GSSContext
    public GSSName getTargName() throws GSSException {
        if (this.targName == null) {
            this.targName = GSSNameImpl.wrapElement(this.gssManager, this.mechCtxt.getTargName());
        }
        return this.targName;
    }

    @Override // org.ietf.jgss.GSSContext
    public Oid getMech() throws GSSException {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getMech();
        }
        return this.mechOid;
    }

    @Override // org.ietf.jgss.GSSContext
    public GSSCredential getDelegCred() throws GSSException {
        if (this.mechCtxt == null) {
            throw new GSSExceptionImpl(12, "No mechanism context yet!");
        }
        GSSCredentialSpi delegCred = this.mechCtxt.getDelegCred();
        if (delegCred == null) {
            return null;
        }
        return new GSSCredentialImpl(this.gssManager, delegCred);
    }

    @Override // org.ietf.jgss.GSSContext
    public boolean isInitiator() throws GSSException {
        return this.initiator;
    }

    @Override // org.ietf.jgss.GSSContext
    public void dispose() throws GSSException {
        this.currentState = 4;
        if (this.mechCtxt != null) {
            this.mechCtxt.dispose();
            this.mechCtxt = null;
        }
        this.myCred = null;
        this.srcName = null;
        this.targName = null;
    }

    @Override // com.sun.security.jgss.ExtendedGSSContext
    public Object inquireSecContext(InquireType inquireType) throws GSSException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new InquireSecContextPermission(inquireType.toString()));
        }
        if (this.mechCtxt == null) {
            throw new GSSException(12);
        }
        return this.mechCtxt.inquireSecContext(inquireType);
    }

    @Override // com.sun.security.jgss.ExtendedGSSContext
    public void requestDelegPolicy(boolean z2) throws GSSException {
        if (this.mechCtxt == null && this.initiator) {
            this.reqDelegPolicyState = z2;
        }
    }

    @Override // com.sun.security.jgss.ExtendedGSSContext
    public boolean getDelegPolicyState() {
        if (this.mechCtxt != null) {
            return this.mechCtxt.getDelegPolicyState();
        }
        return this.reqDelegPolicyState;
    }
}
