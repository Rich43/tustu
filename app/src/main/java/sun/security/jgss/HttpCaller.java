package sun.security.jgss;

import sun.net.www.protocol.http.HttpCallerInfo;

/* loaded from: rt.jar:sun/security/jgss/HttpCaller.class */
public class HttpCaller extends GSSCaller {
    private final HttpCallerInfo hci;

    public HttpCaller(HttpCallerInfo httpCallerInfo) {
        super("HTTP_CLIENT");
        this.hci = httpCallerInfo;
    }

    public HttpCallerInfo info() {
        return this.hci;
    }
}
