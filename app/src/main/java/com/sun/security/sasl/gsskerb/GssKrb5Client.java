package com.sun.security.sasl.gsskerb;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import net.lingala.zip4j.util.InternalZipConstants;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;

/* loaded from: rt.jar:com/sun/security/sasl/gsskerb/GssKrb5Client.class */
final class GssKrb5Client extends GssKrb5Base implements SaslClient {
    private static final String MY_CLASS_NAME = GssKrb5Client.class.getName();
    private boolean finalHandshake;
    private boolean mutual;
    private byte[] authzID;

    GssKrb5Client(String str, String str2, String str3, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        String str4;
        Object obj;
        super(map, MY_CLASS_NAME);
        this.finalHandshake = false;
        this.mutual = false;
        String str5 = str2 + "@" + str3;
        logger.log(Level.FINE, "KRB5CLNT01:Requesting service name: {0}", str5);
        try {
            GSSManager gSSManager = GSSManager.getInstance();
            GSSName gSSNameCreateName = gSSManager.createName(str5, GSSName.NT_HOSTBASED_SERVICE, KRB5_OID);
            GSSCredential gSSCredential = null;
            if (map != null && (obj = map.get(Sasl.CREDENTIALS)) != null && (obj instanceof GSSCredential)) {
                gSSCredential = (GSSCredential) obj;
                logger.log(Level.FINE, "KRB5CLNT01:Using the credentials supplied in javax.security.sasl.credentials");
            }
            this.secCtx = gSSManager.createContext(gSSNameCreateName, KRB5_OID, gSSCredential, Integer.MAX_VALUE);
            if (gSSCredential != null) {
                this.secCtx.requestCredDeleg(true);
            }
            if (map != null && (str4 = (String) map.get(Sasl.SERVER_AUTH)) != null) {
                this.mutual = "true".equalsIgnoreCase(str4);
            }
            this.secCtx.requestMutualAuth(this.mutual);
            this.secCtx.requestConf(true);
            this.secCtx.requestInteg(true);
            if (str != null && str.length() > 0) {
                try {
                    this.authzID = str.getBytes(InternalZipConstants.CHARSET_UTF8);
                } catch (IOException e2) {
                    throw new SaslException("Cannot encode authorization ID", e2);
                }
            }
        } catch (GSSException e3) {
            throw new SaslException("Failure to initialize security context", e3);
        }
    }

    @Override // javax.security.sasl.SaslClient
    public boolean hasInitialResponse() {
        return true;
    }

    @Override // javax.security.sasl.SaslClient
    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("GSSAPI authentication already complete");
        }
        if (this.finalHandshake) {
            return doFinalHandshake(bArr);
        }
        try {
            byte[] bArrInitSecContext = this.secCtx.initSecContext(bArr, 0, bArr.length);
            if (logger.isLoggable(Level.FINER)) {
                traceOutput(MY_CLASS_NAME, "evaluteChallenge", "KRB5CLNT02:Challenge: [raw]", bArr);
                traceOutput(MY_CLASS_NAME, "evaluateChallenge", "KRB5CLNT03:Response: [after initSecCtx]", bArrInitSecContext);
            }
            if (this.secCtx.isEstablished()) {
                this.finalHandshake = true;
                if (bArrInitSecContext == null) {
                    return EMPTY;
                }
            }
            return bArrInitSecContext;
        } catch (GSSException e2) {
            throw new SaslException("GSS initiate failed", e2);
        }
    }

    private byte[] doFinalHandshake(byte[] bArr) throws SaslException {
        try {
            if (logger.isLoggable(Level.FINER)) {
                traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT04:Challenge [raw]:", bArr);
            }
            if (bArr.length == 0) {
                return EMPTY;
            }
            MessageProp messageProp = new MessageProp(false);
            byte[] bArrUnwrap = this.secCtx.unwrap(bArr, 0, bArr.length, messageProp);
            checkMessageProp("Handshake failure: ", messageProp);
            if (logger.isLoggable(Level.FINE)) {
                if (logger.isLoggable(Level.FINER)) {
                    traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT05:Challenge [unwrapped]:", bArrUnwrap);
                }
                logger.log(Level.FINE, "KRB5CLNT06:Server protections: {0}", new Byte(bArrUnwrap[0]));
            }
            byte bFindPreferredMask = findPreferredMask(bArrUnwrap[0], this.qop);
            if (bFindPreferredMask == 0) {
                throw new SaslException("No common protection layer between client and server");
            }
            if ((bFindPreferredMask & 4) != 0) {
                this.privacy = true;
                this.integrity = true;
            } else if ((bFindPreferredMask & 2) != 0) {
                this.integrity = true;
            }
            int iNetworkByteOrderToInt = networkByteOrderToInt(bArrUnwrap, 1, 3);
            this.sendMaxBufSize = this.sendMaxBufSize == 0 ? iNetworkByteOrderToInt : Math.min(this.sendMaxBufSize, iNetworkByteOrderToInt);
            this.rawSendSize = this.secCtx.getWrapSizeLimit(0, this.privacy, this.sendMaxBufSize);
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "KRB5CLNT07:Client max recv size: {0}; server max recv size: {1}; rawSendSize: {2}", new Object[]{new Integer(this.recvMaxBufSize), new Integer(iNetworkByteOrderToInt), new Integer(this.rawSendSize)});
            }
            int length = 4;
            if (this.authzID != null) {
                length = 4 + this.authzID.length;
            }
            byte[] bArr2 = new byte[length];
            bArr2[0] = bFindPreferredMask;
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "KRB5CLNT08:Selected protection: {0}; privacy: {1}; integrity: {2}", new Object[]{new Byte(bFindPreferredMask), Boolean.valueOf(this.privacy), Boolean.valueOf(this.integrity)});
            }
            intToNetworkByteOrder(this.recvMaxBufSize, bArr2, 1, 3);
            if (this.authzID != null) {
                System.arraycopy(this.authzID, 0, bArr2, 4, this.authzID.length);
                logger.log(Level.FINE, "KRB5CLNT09:Authzid: {0}", this.authzID);
            }
            if (logger.isLoggable(Level.FINER)) {
                traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT10:Response [raw]", bArr2);
            }
            byte[] bArrWrap = this.secCtx.wrap(bArr2, 0, bArr2.length, new MessageProp(0, false));
            if (logger.isLoggable(Level.FINER)) {
                traceOutput(MY_CLASS_NAME, "doFinalHandshake", "KRB5CLNT11:Response [after wrap]", bArrWrap);
            }
            this.completed = true;
            return bArrWrap;
        } catch (GSSException e2) {
            throw new SaslException("Final handshake failed", e2);
        }
    }
}
