package com.sun.net.httpserver;

import com.sun.net.httpserver.Authenticator;
import java.util.Base64;
import jdk.Exported;
import org.icepdf.core.util.PdfOps;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/BasicAuthenticator.class */
public abstract class BasicAuthenticator extends Authenticator {
    protected String realm;

    public abstract boolean checkCredentials(String str, String str2);

    public BasicAuthenticator(String str) {
        this.realm = str;
    }

    public String getRealm() {
        return this.realm;
    }

    @Override // com.sun.net.httpserver.Authenticator
    public Authenticator.Result authenticate(HttpExchange httpExchange) {
        String first = httpExchange.getRequestHeaders().getFirst("Authorization");
        if (first == null) {
            httpExchange.getResponseHeaders().set("WWW-Authenticate", "Basic realm=\"" + this.realm + PdfOps.DOUBLE_QUOTE__TOKEN);
            return new Authenticator.Retry(401);
        }
        int iIndexOf = first.indexOf(32);
        if (iIndexOf == -1 || !first.substring(0, iIndexOf).equals("Basic")) {
            return new Authenticator.Failure(401);
        }
        String str = new String(Base64.getDecoder().decode(first.substring(iIndexOf + 1)));
        int iIndexOf2 = str.indexOf(58);
        String strSubstring = str.substring(0, iIndexOf2);
        if (checkCredentials(strSubstring, str.substring(iIndexOf2 + 1))) {
            return new Authenticator.Success(new HttpPrincipal(strSubstring, this.realm));
        }
        httpExchange.getResponseHeaders().set("WWW-Authenticate", "Basic realm=\"" + this.realm + PdfOps.DOUBLE_QUOTE__TOKEN);
        return new Authenticator.Failure(401);
    }
}
