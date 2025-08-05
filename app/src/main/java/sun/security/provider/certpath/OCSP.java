package sun.security.provider.certpath;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.AccessController;
import java.security.cert.CRLReason;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.Extension;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import sun.security.action.GetIntegerAction;
import sun.security.provider.certpath.OCSPResponse;
import sun.security.util.Debug;
import sun.security.validator.Validator;
import sun.security.x509.AccessDescription;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.GeneralName;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.URIName;
import sun.security.x509.X509CertImpl;

/* loaded from: rt.jar:sun/security/provider/certpath/OCSP.class */
public final class OCSP {
    private static final int DEFAULT_CONNECT_TIMEOUT = 15000;
    private static final Debug debug = Debug.getInstance("certpath");
    private static final int CONNECT_TIMEOUT = initializeTimeout();

    /* loaded from: rt.jar:sun/security/provider/certpath/OCSP$RevocationStatus.class */
    public interface RevocationStatus {

        /* loaded from: rt.jar:sun/security/provider/certpath/OCSP$RevocationStatus$CertStatus.class */
        public enum CertStatus {
            GOOD,
            REVOKED,
            UNKNOWN
        }

        CertStatus getCertStatus();

        Date getRevocationTime();

        CRLReason getRevocationReason();

        Map<String, Extension> getSingleExtensions();
    }

    private static int initializeTimeout() {
        Integer num = (Integer) AccessController.doPrivileged(new GetIntegerAction("com.sun.security.ocsp.timeout"));
        if (num == null || num.intValue() < 0) {
            return DEFAULT_CONNECT_TIMEOUT;
        }
        return num.intValue() * 1000;
    }

    private OCSP() {
    }

    public static RevocationStatus check(X509Certificate x509Certificate, X509Certificate x509Certificate2, URI uri, X509Certificate x509Certificate3, Date date) throws IOException, CertPathValidatorException {
        return check(x509Certificate, x509Certificate2, uri, x509Certificate3, date, (List<Extension>) Collections.emptyList(), Validator.VAR_PLUGIN_CODE_SIGNING);
    }

    public static RevocationStatus check(X509Certificate x509Certificate, X509Certificate x509Certificate2, URI uri, X509Certificate x509Certificate3, Date date, List<Extension> list, String str) throws IOException, CertPathValidatorException {
        return check(x509Certificate, uri, null, x509Certificate2, x509Certificate3, date, list, str);
    }

    public static RevocationStatus check(X509Certificate x509Certificate, URI uri, TrustAnchor trustAnchor, X509Certificate x509Certificate2, X509Certificate x509Certificate3, Date date, List<Extension> list, String str) throws IOException, CertPathValidatorException {
        try {
            CertId certId = new CertId(x509Certificate2, X509CertImpl.toImpl(x509Certificate).getSerialNumberObject());
            return check((List<CertId>) Collections.singletonList(certId), uri, new OCSPResponse.IssuerInfo(trustAnchor, x509Certificate2), x509Certificate3, date, list, str).getSingleResponse(certId);
        } catch (IOException | CertificateException e2) {
            throw new CertPathValidatorException("Exception while encoding OCSPRequest", e2);
        }
    }

    static OCSPResponse check(List<CertId> list, URI uri, OCSPResponse.IssuerInfo issuerInfo, X509Certificate x509Certificate, Date date, List<Extension> list2, String str) throws IOException, CertPathValidatorException {
        byte[] value = null;
        for (Extension extension : list2) {
            if (extension.getId().equals(PKIXExtensions.OCSPNonce_Id.toString())) {
                value = extension.getValue();
            }
        }
        try {
            OCSPResponse oCSPResponse = new OCSPResponse(getOCSPBytes(list, uri, list2));
            oCSPResponse.verify(list, issuerInfo, x509Certificate, date, value, str);
            return oCSPResponse;
        } catch (IOException e2) {
            throw new CertPathValidatorException("Unable to determine revocation status due to network error", e2, null, -1, CertPathValidatorException.BasicReason.UNDETERMINED_REVOCATION_STATUS);
        }
    }

    public static byte[] getOCSPBytes(List<CertId> list, URI uri, List<Extension> list2) throws IOException {
        int i2;
        byte[] bArrEncodeBytes = new OCSPRequest(list, list2).encodeBytes();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL url = uri.toURL();
            if (debug != null) {
                debug.println("connecting to OCSP service at: " + ((Object) url));
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setReadTimeout(CONNECT_TIMEOUT);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-type", "application/ocsp-request");
            httpURLConnection.setRequestProperty("Content-length", String.valueOf(bArrEncodeBytes.length));
            outputStream = httpURLConnection.getOutputStream();
            outputStream.write(bArrEncodeBytes);
            outputStream.flush();
            if (debug != null && httpURLConnection.getResponseCode() != 200) {
                debug.println("Received HTTP error: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());
            }
            inputStream = httpURLConnection.getInputStream();
            int contentLength = httpURLConnection.getContentLength();
            if (contentLength == -1) {
                contentLength = Integer.MAX_VALUE;
            }
            byte[] bArrCopyOf = new byte[contentLength > 2048 ? 2048 : contentLength];
            int i3 = 0;
            while (i3 < contentLength && (i2 = inputStream.read(bArrCopyOf, i3, bArrCopyOf.length - i3)) >= 0) {
                i3 += i2;
                if (i3 >= bArrCopyOf.length && i3 < contentLength) {
                    bArrCopyOf = Arrays.copyOf(bArrCopyOf, i3 * 2);
                }
            }
            byte[] bArrCopyOf2 = Arrays.copyOf(bArrCopyOf, i3);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    throw e2;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e3) {
                    throw e3;
                }
            }
            return bArrCopyOf2;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    throw e4;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e5) {
                    throw e5;
                }
            }
            throw th;
        }
    }

    public static URI getResponderURI(X509Certificate x509Certificate) {
        try {
            return getResponderURI(X509CertImpl.toImpl(x509Certificate));
        } catch (CertificateException e2) {
            return null;
        }
    }

    static URI getResponderURI(X509CertImpl x509CertImpl) {
        AuthorityInfoAccessExtension authorityInfoAccessExtension = x509CertImpl.getAuthorityInfoAccessExtension();
        if (authorityInfoAccessExtension == null) {
            return null;
        }
        for (AccessDescription accessDescription : authorityInfoAccessExtension.getAccessDescriptions()) {
            if (accessDescription.getAccessMethod().equals(AccessDescription.Ad_OCSP_Id)) {
                GeneralName accessLocation = accessDescription.getAccessLocation();
                if (accessLocation.getType() == 6) {
                    return ((URIName) accessLocation.getName()).getURI();
                }
            }
        }
        return null;
    }
}
