package sun.net.www.protocol.http;

import java.net.URL;

@Deprecated
/* loaded from: rt.jar:sun/net/www/protocol/http/HttpAuthenticator.class */
public interface HttpAuthenticator {
    boolean schemeSupported(String str);

    String authString(URL url, String str, String str2);
}
