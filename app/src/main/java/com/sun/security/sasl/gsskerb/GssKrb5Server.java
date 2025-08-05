package com.sun.security.sasl.gsskerb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.logging.Level;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.AuthorizeCallback;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.MessageProp;

/* loaded from: rt.jar:com/sun/security/sasl/gsskerb/GssKrb5Server.class */
final class GssKrb5Server extends GssKrb5Base implements SaslServer {
    private static final String MY_CLASS_NAME = GssKrb5Server.class.getName();
    private int handshakeStage;
    private String peer;
    private String me;
    private String authzid;
    private CallbackHandler cbh;
    private final String protocolSaved;

    GssKrb5Server(String str, String str2, Map<String, ?> map, CallbackHandler callbackHandler) throws SaslException {
        String str3;
        super(map, MY_CLASS_NAME);
        this.handshakeStage = 0;
        this.cbh = callbackHandler;
        if (str2 == null) {
            this.protocolSaved = str;
            str3 = null;
        } else {
            this.protocolSaved = null;
            str3 = str + "@" + str2;
        }
        logger.log(Level.FINE, "KRB5SRV01:Using service name: {0}", str3);
        try {
            GSSManager gSSManager = GSSManager.getInstance();
            this.secCtx = gSSManager.createContext(gSSManager.createCredential(str3 == null ? null : gSSManager.createName(str3, GSSName.NT_HOSTBASED_SERVICE, KRB5_OID), Integer.MAX_VALUE, KRB5_OID, 2));
            if ((this.allQop & 2) != 0) {
                this.secCtx.requestInteg(true);
            }
            if ((this.allQop & 4) != 0) {
                this.secCtx.requestConf(true);
            }
            logger.log(Level.FINE, "KRB5SRV02:Initialization complete");
        } catch (GSSException e2) {
            throw new SaslException("Failure to initialize security context", e2);
        }
    }

    @Override // javax.security.sasl.SaslServer
    public byte[] evaluateResponse(byte[] bArr) throws SaslException {
        if (this.completed) {
            throw new SaslException("SASL authentication already complete");
        }
        if (logger.isLoggable(Level.FINER)) {
            traceOutput(MY_CLASS_NAME, "evaluateResponse", "KRB5SRV03:Response [raw]:", bArr);
        }
        switch (this.handshakeStage) {
            case 1:
                return doHandshake1(bArr);
            case 2:
                return doHandshake2(bArr);
            default:
                try {
                    byte[] bArrAcceptSecContext = this.secCtx.acceptSecContext(bArr, 0, bArr.length);
                    if (logger.isLoggable(Level.FINER)) {
                        traceOutput(MY_CLASS_NAME, "evaluateResponse", "KRB5SRV04:Challenge: [after acceptSecCtx]", bArrAcceptSecContext);
                    }
                    if (this.secCtx.isEstablished()) {
                        this.handshakeStage = 1;
                        this.peer = this.secCtx.getSrcName().toString();
                        this.me = this.secCtx.getTargName().toString();
                        logger.log(Level.FINE, "KRB5SRV05:Peer name is : {0}, my name is : {1}", new Object[]{this.peer, this.me});
                        if (this.protocolSaved != null && !this.protocolSaved.equalsIgnoreCase(this.me.split("[/@]")[0])) {
                            throw new SaslException("GSS context targ name protocol error: " + this.me);
                        }
                        if (bArrAcceptSecContext == null) {
                            return doHandshake1(EMPTY);
                        }
                    }
                    return bArrAcceptSecContext;
                } catch (GSSException e2) {
                    throw new SaslException("GSS initiate failed", e2);
                }
        }
    }

    private byte[] doHandshake1(byte[] bArr) throws SaslException {
        if (bArr != null) {
            try {
                if (bArr.length > 0) {
                    throw new SaslException("Handshake expecting no response data from server");
                }
            } catch (GSSException e2) {
                throw new SaslException("Problem wrapping handshake1", e2);
            }
        }
        byte[] bArr2 = new byte[4];
        bArr2[0] = this.allQop;
        intToNetworkByteOrder(this.recvMaxBufSize, bArr2, 1, 3);
        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, "KRB5SRV06:Supported protections: {0}; recv max buf size: {1}", new Object[]{new Byte(this.allQop), new Integer(this.recvMaxBufSize)});
        }
        this.handshakeStage = 2;
        if (logger.isLoggable(Level.FINER)) {
            traceOutput(MY_CLASS_NAME, "doHandshake1", "KRB5SRV07:Challenge [raw]", bArr2);
        }
        byte[] bArrWrap = this.secCtx.wrap(bArr2, 0, bArr2.length, new MessageProp(0, false));
        if (logger.isLoggable(Level.FINER)) {
            traceOutput(MY_CLASS_NAME, "doHandshake1", "KRB5SRV08:Challenge [after wrap]", bArrWrap);
        }
        return bArrWrap;
    }

    private byte[] doHandshake2(byte[] bArr) throws SaslException {
        try {
            MessageProp messageProp = new MessageProp(false);
            byte[] bArrUnwrap = this.secCtx.unwrap(bArr, 0, bArr.length, messageProp);
            checkMessageProp("Handshake failure: ", messageProp);
            if (logger.isLoggable(Level.FINER)) {
                traceOutput(MY_CLASS_NAME, "doHandshake2", "KRB5SRV09:Response [after unwrap]", bArrUnwrap);
            }
            byte b2 = bArrUnwrap[0];
            if ((b2 & this.allQop) == 0) {
                throw new SaslException("Client selected unsupported protection: " + ((int) b2));
            }
            if ((b2 & 4) != 0) {
                this.privacy = true;
                this.integrity = true;
            } else if ((b2 & 2) != 0) {
                this.integrity = true;
            }
            int iNetworkByteOrderToInt = networkByteOrderToInt(bArrUnwrap, 1, 3);
            this.sendMaxBufSize = this.sendMaxBufSize == 0 ? iNetworkByteOrderToInt : Math.min(this.sendMaxBufSize, iNetworkByteOrderToInt);
            this.rawSendSize = this.secCtx.getWrapSizeLimit(0, this.privacy, this.sendMaxBufSize);
            if (logger.isLoggable(Level.FINE)) {
                logger.log(Level.FINE, "KRB5SRV10:Selected protection: {0}; privacy: {1}; integrity: {2}", new Object[]{new Byte(b2), Boolean.valueOf(this.privacy), Boolean.valueOf(this.integrity)});
                logger.log(Level.FINE, "KRB5SRV11:Client max recv size: {0}; server max send size: {1}; rawSendSize: {2}", new Object[]{new Integer(iNetworkByteOrderToInt), new Integer(this.sendMaxBufSize), new Integer(this.rawSendSize)});
            }
            if (bArrUnwrap.length > 4) {
                try {
                    this.authzid = new String(bArrUnwrap, 4, bArrUnwrap.length - 4, "UTF-8");
                } catch (UnsupportedEncodingException e2) {
                    throw new SaslException("Cannot decode authzid", e2);
                }
            } else {
                this.authzid = this.peer;
            }
            logger.log(Level.FINE, "KRB5SRV12:Authzid: {0}", this.authzid);
            AuthorizeCallback authorizeCallback = new AuthorizeCallback(this.peer, this.authzid);
            this.cbh.handle(new Callback[]{authorizeCallback});
            if (authorizeCallback.isAuthorized()) {
                this.authzid = authorizeCallback.getAuthorizedID();
                this.completed = true;
                return null;
            }
            throw new SaslException(this.peer + " is not authorized to connect as " + this.authzid);
        } catch (IOException e3) {
            throw new SaslException("Problem with callback handler", e3);
        } catch (UnsupportedCallbackException e4) {
            throw new SaslException("Problem with callback handler", e4);
        } catch (GSSException e5) {
            throw new SaslException("Final handshake step failed", e5);
        }
    }

    @Override // javax.security.sasl.SaslServer
    public String getAuthorizationID() {
        if (this.completed) {
            return this.authzid;
        }
        throw new IllegalStateException("Authentication incomplete");
    }

    @Override // com.sun.security.sasl.util.AbstractSaslImpl
    public Object getNegotiatedProperty(String str) {
        Object negotiatedProperty;
        if (!this.completed) {
            throw new IllegalStateException("Authentication incomplete");
        }
        switch (str) {
            case "javax.security.sasl.bound.server.name":
                try {
                    negotiatedProperty = this.me.split("[/@]")[1];
                    break;
                } catch (Exception e2) {
                    negotiatedProperty = null;
                    break;
                }
            default:
                negotiatedProperty = super.getNegotiatedProperty(str);
                break;
        }
        return negotiatedProperty;
    }
}
