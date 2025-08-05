package sun.net.www.protocol.http.spnego;

import com.sun.security.jgss.ExtendedGSSContext;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.net.www.protocol.http.HttpCallerInfo;
import sun.net.www.protocol.http.Negotiator;
import sun.security.action.GetBooleanAction;
import sun.security.jgss.GSSManagerImpl;
import sun.security.jgss.GSSUtil;
import sun.security.jgss.HttpCaller;
import sun.security.krb5.internal.PAForUserEnc;

/* loaded from: rt.jar:sun/net/www/protocol/http/spnego/NegotiatorImpl.class */
public class NegotiatorImpl extends Negotiator {
    private static final boolean DEBUG = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.security.krb5.debug"))).booleanValue();
    private GSSContext context;
    private byte[] oneToken;

    private void init(HttpCallerInfo httpCallerInfo) throws GSSException {
        Oid oid;
        if (httpCallerInfo.scheme.equalsIgnoreCase(PAForUserEnc.AUTH_PACKAGE) || ((String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.net.www.protocol.http.spnego.NegotiatorImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return System.getProperty("http.auth.preference", "spnego");
            }
        })).equalsIgnoreCase("kerberos")) {
            oid = GSSUtil.GSS_KRB5_MECH_OID;
        } else {
            oid = GSSUtil.GSS_SPNEGO_MECH_OID;
        }
        GSSManagerImpl gSSManagerImpl = new GSSManagerImpl(new HttpCaller(httpCallerInfo));
        this.context = gSSManagerImpl.createContext(gSSManagerImpl.createName("HTTP@" + httpCallerInfo.host.toLowerCase(), GSSName.NT_HOSTBASED_SERVICE), oid, null, 0);
        if (this.context instanceof ExtendedGSSContext) {
            ((ExtendedGSSContext) this.context).requestDelegPolicy(true);
        }
        this.oneToken = this.context.initSecContext(new byte[0], 0, 0);
    }

    public NegotiatorImpl(HttpCallerInfo httpCallerInfo) throws IOException {
        try {
            init(httpCallerInfo);
        } catch (GSSException e2) {
            if (DEBUG) {
                System.out.println("Negotiate support not initiated, will fallback to other scheme if allowed. Reason:");
                e2.printStackTrace();
            }
            IOException iOException = new IOException("Negotiate support not initiated");
            iOException.initCause(e2);
            throw iOException;
        }
    }

    @Override // sun.net.www.protocol.http.Negotiator
    public byte[] firstToken() {
        return this.oneToken;
    }

    @Override // sun.net.www.protocol.http.Negotiator
    public byte[] nextToken(byte[] bArr) throws IOException {
        try {
            return this.context.initSecContext(bArr, 0, bArr.length);
        } catch (GSSException e2) {
            if (DEBUG) {
                System.out.println("Negotiate support cannot continue. Reason:");
                e2.printStackTrace();
            }
            IOException iOException = new IOException("Negotiate support cannot continue");
            iOException.initCause(e2);
            throw iOException;
        }
    }
}
