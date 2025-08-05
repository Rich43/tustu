package sun.net.www.protocol.http;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.URL;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.apache.commons.net.ftp.FTP;
import org.icepdf.core.util.PdfOps;
import sun.net.NetProperties;
import sun.net.www.HeaderParser;
import sun.net.www.protocol.http.HttpURLConnection;

/* loaded from: rt.jar:sun/net/www/protocol/http/DigestAuthentication.class */
class DigestAuthentication extends AuthenticationInfo {
    private static final long serialVersionUID = 100;
    private String authMethod;
    private static final String compatPropName = "http.auth.digest.quoteParameters";
    private static final boolean delimCompatFlag;
    Parameters params;
    private static final char[] charArray;
    private static final String[] zeroPad;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DigestAuthentication.class.desiredAssertionStatus();
        Boolean bool = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.net.www.protocol.http.DigestAuthentication.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return NetProperties.getBoolean(DigestAuthentication.compatPropName);
            }
        });
        delimCompatFlag = bool == null ? false : bool.booleanValue();
        charArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        zeroPad = new String[]{"00000000", "0000000", "000000", "00000", "0000", "000", "00", "0"};
    }

    /* loaded from: rt.jar:sun/net/www/protocol/http/DigestAuthentication$Parameters.class */
    static class Parameters implements Serializable {
        private static final long serialVersionUID = -3584543755194526252L;
        private String cnonce;
        private static final int cnonceRepeat = 5;
        private static final int cnoncelen = 40;
        private static Random random = new Random();
        private int NCcount = 0;
        private boolean redoCachedHA1 = true;
        int cnonce_count = 0;
        private boolean serverQop = false;
        private String opaque = null;
        private String algorithm = null;
        private String cachedHA1 = null;
        private String nonce = null;

        Parameters() {
            setNewCnonce();
        }

        boolean authQop() {
            return this.serverQop;
        }

        synchronized void incrementNC() {
            this.NCcount++;
        }

        synchronized int getNCCount() {
            return this.NCcount;
        }

        synchronized String getCnonce() {
            if (this.cnonce_count >= 5) {
                setNewCnonce();
            }
            this.cnonce_count++;
            return this.cnonce;
        }

        synchronized void setNewCnonce() {
            byte[] bArr = new byte[20];
            char[] cArr = new char[40];
            random.nextBytes(bArr);
            for (int i2 = 0; i2 < 20; i2++) {
                int i3 = bArr[i2] + 128;
                cArr[i2 * 2] = (char) (65 + (i3 / 16));
                cArr[(i2 * 2) + 1] = (char) (65 + (i3 % 16));
            }
            this.cnonce = new String(cArr, 0, 40);
            this.cnonce_count = 0;
            this.redoCachedHA1 = true;
        }

        synchronized void setQop(String str) {
            if (str != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
                while (stringTokenizer.hasMoreTokens()) {
                    if (stringTokenizer.nextToken().equalsIgnoreCase("auth")) {
                        this.serverQop = true;
                        return;
                    }
                }
            }
            this.serverQop = false;
        }

        synchronized String getOpaque() {
            return this.opaque;
        }

        synchronized void setOpaque(String str) {
            this.opaque = str;
        }

        synchronized String getNonce() {
            return this.nonce;
        }

        synchronized void setNonce(String str) {
            if (!str.equals(this.nonce)) {
                this.nonce = str;
                this.NCcount = 0;
                this.redoCachedHA1 = true;
            }
        }

        synchronized String getCachedHA1() {
            if (this.redoCachedHA1) {
                return null;
            }
            return this.cachedHA1;
        }

        synchronized void setCachedHA1(String str) {
            this.cachedHA1 = str;
            this.redoCachedHA1 = false;
        }

        synchronized String getAlgorithm() {
            return this.algorithm;
        }

        synchronized void setAlgorithm(String str) {
            this.algorithm = str;
        }
    }

    public DigestAuthentication(boolean z2, URL url, String str, String str2, PasswordAuthentication passwordAuthentication, Parameters parameters) {
        super(z2 ? 'p' : 's', AuthScheme.DIGEST, url, str);
        this.authMethod = str2;
        this.pw = passwordAuthentication;
        this.params = parameters;
    }

    public DigestAuthentication(boolean z2, String str, int i2, String str2, String str3, PasswordAuthentication passwordAuthentication, Parameters parameters) {
        super(z2 ? 'p' : 's', AuthScheme.DIGEST, str, i2, str2);
        this.authMethod = str3;
        this.pw = passwordAuthentication;
        this.params = parameters;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean supportsPreemptiveAuthorization() {
        return true;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public String getHeaderValue(URL url, String str) {
        return getHeaderValueImpl(url.getFile(), str);
    }

    String getHeaderValue(String str, String str2) {
        return getHeaderValueImpl(str, str2);
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean isAuthorizationStale(String str) {
        String strFindValue;
        HeaderParser headerParser = new HeaderParser(str);
        String strFindValue2 = headerParser.findValue("stale");
        if (strFindValue2 == null || !strFindValue2.equals("true") || (strFindValue = headerParser.findValue("nonce")) == null || "".equals(strFindValue)) {
            return false;
        }
        this.params.setNonce(strFindValue);
        return true;
    }

    @Override // sun.net.www.protocol.http.AuthenticationInfo
    public boolean setHeaders(HttpURLConnection httpURLConnection, HeaderParser headerParser, String str) {
        String method;
        this.params.setNonce(headerParser.findValue("nonce"));
        this.params.setOpaque(headerParser.findValue("opaque"));
        this.params.setQop(headerParser.findValue("qop"));
        String requestURI = "";
        if (this.type == 'p' && httpURLConnection.tunnelState() == HttpURLConnection.TunnelState.SETUP) {
            requestURI = HttpURLConnection.connectRequestURI(httpURLConnection.getURL());
            method = HttpURLConnection.HTTP_CONNECT;
        } else {
            try {
                requestURI = httpURLConnection.getRequestURI();
            } catch (IOException e2) {
            }
            method = httpURLConnection.getMethod();
        }
        if (this.params.nonce == null || this.authMethod == null || this.pw == null || this.realm == null) {
            return false;
        }
        if (this.authMethod.length() >= 1) {
            this.authMethod = Character.toUpperCase(this.authMethod.charAt(0)) + this.authMethod.substring(1).toLowerCase();
        }
        String strFindValue = headerParser.findValue("algorithm");
        if (strFindValue == null || "".equals(strFindValue)) {
            strFindValue = "MD5";
        }
        this.params.setAlgorithm(strFindValue);
        if (this.params.authQop()) {
            this.params.setNewCnonce();
        }
        String headerValueImpl = getHeaderValueImpl(requestURI, method);
        if (headerValueImpl != null) {
            httpURLConnection.setAuthenticationProperty(getHeaderName(), headerValueImpl);
            return true;
        }
        return false;
    }

    private String getHeaderValueImpl(String str, String str2) {
        String str3;
        String str4;
        char[] password = this.pw.getPassword();
        boolean zAuthQop = this.params.authQop();
        String opaque = this.params.getOpaque();
        String cnonce = this.params.getCnonce();
        String nonce = this.params.getNonce();
        String algorithm = this.params.getAlgorithm();
        this.params.incrementNC();
        int nCCount = this.params.getNCCount();
        String lowerCase = null;
        if (nCCount != -1) {
            lowerCase = Integer.toHexString(nCCount).toLowerCase();
            int length = lowerCase.length();
            if (length < 8) {
                lowerCase = zeroPad[length] + lowerCase;
            }
        }
        try {
            String strComputeDigest = computeDigest(true, this.pw.getUserName(), password, this.realm, str2, str, nonce, cnonce, lowerCase);
            String str5 = PdfOps.DOUBLE_QUOTE__TOKEN;
            if (zAuthQop) {
                str5 = "\", nc=" + lowerCase;
            }
            if (delimCompatFlag) {
                str3 = ", algorithm=\"" + algorithm + PdfOps.DOUBLE_QUOTE__TOKEN;
                str4 = ", qop=\"auth\"";
            } else {
                str3 = ", algorithm=" + algorithm;
                str4 = ", qop=auth";
            }
            String str6 = this.authMethod + " username=\"" + this.pw.getUserName() + "\", realm=\"" + this.realm + "\", nonce=\"" + nonce + str5 + ", uri=\"" + str + "\", response=\"" + strComputeDigest + PdfOps.DOUBLE_QUOTE__TOKEN + str3;
            if (opaque != null) {
                str6 = str6 + ", opaque=\"" + opaque + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            if (cnonce != null) {
                str6 = str6 + ", cnonce=\"" + cnonce + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            if (zAuthQop) {
                str6 = str6 + str4;
            }
            return str6;
        } catch (NoSuchAlgorithmException e2) {
            return null;
        }
    }

    public void checkResponse(String str, String str2, URL url) throws IOException {
        checkResponse(str, str2, url.getFile());
    }

    public void checkResponse(String str, String str2, String str3) throws IOException {
        char[] password = this.pw.getPassword();
        String userName = this.pw.getUserName();
        this.params.authQop();
        this.params.getOpaque();
        String str4 = this.params.cnonce;
        String nonce = this.params.getNonce();
        this.params.getAlgorithm();
        int nCCount = this.params.getNCCount();
        String upperCase = null;
        if (str == null) {
            throw new ProtocolException("No authentication information in response");
        }
        if (nCCount != -1) {
            upperCase = Integer.toHexString(nCCount).toUpperCase();
            int length = upperCase.length();
            if (length < 8) {
                upperCase = zeroPad[length] + upperCase;
            }
        }
        try {
            String strComputeDigest = computeDigest(false, userName, password, this.realm, str2, str3, nonce, str4, upperCase);
            HeaderParser headerParser = new HeaderParser(str);
            String strFindValue = headerParser.findValue("rspauth");
            if (strFindValue == null) {
                throw new ProtocolException("No digest in response");
            }
            if (!strFindValue.equals(strComputeDigest)) {
                throw new ProtocolException("Response digest invalid");
            }
            String strFindValue2 = headerParser.findValue("nextnonce");
            if (strFindValue2 != null && !"".equals(strFindValue2)) {
                this.params.setNonce(strFindValue2);
            }
        } catch (NoSuchAlgorithmException e2) {
            throw new ProtocolException("Unsupported algorithm in response");
        }
    }

    private String computeDigest(boolean z2, String str, char[] cArr, String str2, String str3, String str4, String str5, String str6, String str7) throws NoSuchAlgorithmException {
        String strEncode;
        String str8;
        String str9;
        String algorithm = this.params.getAlgorithm();
        boolean zEqualsIgnoreCase = algorithm.equalsIgnoreCase("MD5-sess");
        MessageDigest messageDigest = MessageDigest.getInstance(zEqualsIgnoreCase ? "MD5" : algorithm);
        if (zEqualsIgnoreCase) {
            String cachedHA1 = this.params.getCachedHA1();
            strEncode = cachedHA1;
            if (cachedHA1 == null) {
                strEncode = encode(encode(str + CallSiteDescriptor.TOKEN_DELIMITER + str2 + CallSiteDescriptor.TOKEN_DELIMITER, cArr, messageDigest) + CallSiteDescriptor.TOKEN_DELIMITER + str5 + CallSiteDescriptor.TOKEN_DELIMITER + str6, null, messageDigest);
                this.params.setCachedHA1(strEncode);
            }
        } else {
            strEncode = encode(str + CallSiteDescriptor.TOKEN_DELIMITER + str2 + CallSiteDescriptor.TOKEN_DELIMITER, cArr, messageDigest);
        }
        if (z2) {
            str8 = str3 + CallSiteDescriptor.TOKEN_DELIMITER + str4;
        } else {
            str8 = CallSiteDescriptor.TOKEN_DELIMITER + str4;
        }
        String strEncode2 = encode(str8, null, messageDigest);
        if (this.params.authQop()) {
            str9 = strEncode + CallSiteDescriptor.TOKEN_DELIMITER + str5 + CallSiteDescriptor.TOKEN_DELIMITER + str7 + CallSiteDescriptor.TOKEN_DELIMITER + str6 + ":auth:" + strEncode2;
        } else {
            str9 = strEncode + CallSiteDescriptor.TOKEN_DELIMITER + str5 + CallSiteDescriptor.TOKEN_DELIMITER + strEncode2;
        }
        return encode(str9, null, messageDigest);
    }

    private String encode(String str, char[] cArr, MessageDigest messageDigest) {
        try {
            messageDigest.update(str.getBytes(FTP.DEFAULT_CONTROL_ENCODING));
        } catch (UnsupportedEncodingException e2) {
            if (!$assertionsDisabled) {
                throw new AssertionError();
            }
        }
        if (cArr != null) {
            byte[] bArr = new byte[cArr.length];
            for (int i2 = 0; i2 < cArr.length; i2++) {
                bArr[i2] = (byte) cArr[i2];
            }
            messageDigest.update(bArr);
            Arrays.fill(bArr, (byte) 0);
        }
        byte[] bArrDigest = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer(bArrDigest.length * 2);
        for (int i3 = 0; i3 < bArrDigest.length; i3++) {
            stringBuffer.append(charArray[(bArrDigest[i3] >>> 4) & 15]);
            stringBuffer.append(charArray[bArrDigest[i3] & 15]);
        }
        return stringBuffer.toString();
    }
}
