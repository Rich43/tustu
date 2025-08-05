package sun.net.www.protocol.http;

import java.security.AccessController;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import sun.net.www.HeaderParser;
import sun.net.www.MessageHeader;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/net/www/protocol/http/AuthenticationHeader.class */
public class AuthenticationHeader {
    MessageHeader rsp;
    HeaderParser preferred;
    String preferred_r;
    private final HttpCallerInfo hci;
    boolean dontUseNegotiate;
    static String authPref;
    String hdrname;
    HashMap<String, SchemeMapValue> schemes;

    static {
        authPref = null;
        authPref = (String) AccessController.doPrivileged(new GetPropertyAction("http.auth.preference"));
        if (authPref != null) {
            authPref = authPref.toLowerCase();
            if (authPref.equals("spnego") || authPref.equals("kerberos")) {
                authPref = "negotiate";
            }
        }
    }

    public String toString() {
        return "AuthenticationHeader: prefer " + this.preferred_r;
    }

    public AuthenticationHeader(String str, MessageHeader messageHeader, HttpCallerInfo httpCallerInfo, boolean z2) {
        this(str, messageHeader, httpCallerInfo, z2, Collections.emptySet());
    }

    public AuthenticationHeader(String str, MessageHeader messageHeader, HttpCallerInfo httpCallerInfo, boolean z2, Set<String> set) {
        this.dontUseNegotiate = false;
        this.hci = httpCallerInfo;
        this.dontUseNegotiate = z2;
        this.rsp = messageHeader;
        this.hdrname = str;
        this.schemes = new HashMap<>();
        parse(set);
    }

    public HttpCallerInfo getHttpCallerInfo() {
        return this.hci;
    }

    /* loaded from: rt.jar:sun/net/www/protocol/http/AuthenticationHeader$SchemeMapValue.class */
    static class SchemeMapValue {
        String raw;
        HeaderParser parser;

        SchemeMapValue(HeaderParser headerParser, String str) {
            this.raw = str;
            this.parser = headerParser;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00ea A[PHI: r10
  0x00ea: PHI (r10v1 sun.net.www.protocol.http.AuthenticationHeader$SchemeMapValue) = 
  (r10v0 sun.net.www.protocol.http.AuthenticationHeader$SchemeMapValue)
  (r10v10 sun.net.www.protocol.http.AuthenticationHeader$SchemeMapValue)
 binds: [B:24:0x00d5, B:26:0x00e7] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0192  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parse(java.util.Set<java.lang.String> r8) {
        /*
            Method dump skipped, instructions count: 459
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.protocol.http.AuthenticationHeader.parse(java.util.Set):void");
    }

    public HeaderParser headerParser() {
        return this.preferred;
    }

    public String scheme() {
        if (this.preferred != null) {
            return this.preferred.findKey(0);
        }
        return null;
    }

    public String raw() {
        return this.preferred_r;
    }

    public boolean isPresent() {
        return this.preferred != null;
    }
}
