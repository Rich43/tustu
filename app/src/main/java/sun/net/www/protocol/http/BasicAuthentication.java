package sun.net.www.protocol.http;

import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Base64;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.ftp.FTP;
import sun.net.www.HeaderParser;

/* loaded from: rt.jar:sun/net/www/protocol/http/BasicAuthentication.class */
class BasicAuthentication extends AuthenticationInfo {
    private static final long serialVersionUID = 100;
    String auth;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BasicAuthentication.class.desiredAssertionStatus();
    }

    public BasicAuthentication(boolean z2, String str, int i2, String str2, PasswordAuthentication passwordAuthentication) {
        super(z2 ? 'p' : 's', AuthScheme.BASIC, str, i2, str2);
        byte[] bytes = null;
        try {
            bytes = (passwordAuthentication.getUserName() + CallSiteDescriptor.TOKEN_DELIMITER).getBytes(FTP.DEFAULT_CONTROL_ENCODING);
        } catch (UnsupportedEncodingException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        char[] password = passwordAuthentication.getPassword();
        byte[] bArr = new byte[password.length];
        for (int i3 = 0; i3 < password.length; i3++) {
            bArr[i3] = (byte) password[i3];
        }
        byte[] bArr2 = new byte[bytes.length + bArr.length];
        System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
        System.arraycopy(bArr, 0, bArr2, bytes.length, bArr.length);
        this.auth = "Basic " + Base64.getEncoder().encodeToString(bArr2);
        this.pw = passwordAuthentication;
    }

    public BasicAuthentication(boolean z2, String str, int i2, String str2, String str3) {
        super(z2 ? 'p' : 's', AuthScheme.BASIC, str, i2, str2);
        this.auth = "Basic " + str3;
    }

    public BasicAuthentication(boolean z2, URL url, String str, PasswordAuthentication passwordAuthentication) {
        super(z2 ? 'p' : 's', AuthScheme.BASIC, url, str);
        byte[] bytes = null;
        try {
            bytes = (passwordAuthentication.getUserName() + CallSiteDescriptor.TOKEN_DELIMITER).getBytes(FTP.DEFAULT_CONTROL_ENCODING);
        } catch (UnsupportedEncodingException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        char[] password = passwordAuthentication.getPassword();
        byte[] bArr = new byte[password.length];
        for (int i2 = 0; i2 < password.length; i2++) {
            bArr[i2] = (byte) password[i2];
        }
        byte[] bArr2 = new byte[bytes.length + bArr.length];
        System.arraycopy(bytes, 0, bArr2, 0, bytes.length);
        System.arraycopy(bArr, 0, bArr2, bytes.length, bArr.length);
        this.auth = "Basic " + Base64.getEncoder().encodeToString(bArr2);
        this.pw = passwordAuthentication;
    }

    public BasicAuthentication(boolean z2, URL url, String str, String str2) {
        super(z2 ? 'p' : 's', AuthScheme.BASIC, url, str);
        this.auth = "Basic " + str2;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean supportsPreemptiveAuthorization() {
        return true;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean setHeaders(HttpURLConnection httpURLConnection, HeaderParser headerParser, String str) {
        httpURLConnection.setAuthenticationProperty(getHeaderName(), getHeaderValue(null, null));
        return true;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public String getHeaderValue(URL url, String str) {
        return this.auth;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean isAuthorizationStale(String str) {
        return false;
    }

    static String getRootPath(String str, String str2) {
        int i2 = 0;
        try {
            str = new URI(str).normalize().getPath();
            str2 = new URI(str2).normalize().getPath();
        } catch (URISyntaxException e2) {
        }
        while (i2 < str2.length()) {
            int iIndexOf = str2.indexOf(47, i2 + 1);
            if (iIndexOf != -1 && str2.regionMatches(0, str, 0, iIndexOf + 1)) {
                i2 = iIndexOf;
            } else {
                return str2.substring(0, i2 + 1);
            }
        }
        return str;
    }
}
