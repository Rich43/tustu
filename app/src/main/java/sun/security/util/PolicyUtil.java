package sun.security.util;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import org.icepdf.core.util.PdfOps;
import sun.net.www.ParseUtil;

/* loaded from: rt.jar:sun/security/util/PolicyUtil.class */
public class PolicyUtil {
    private static final String P11KEYSTORE = "PKCS11";
    private static final String NONE = "NONE";

    public static InputStream getInputStream(URL url) throws IOException {
        if (DeploymentDescriptorParser.ATTR_FILE.equals(url.getProtocol())) {
            return new FileInputStream(ParseUtil.decode(url.getFile().replace('/', File.separatorChar)));
        }
        return url.openStream();
    }

    /* JADX WARN: Finally extract failed */
    public static KeyStore getKeyStore(URL url, String str, String str2, String str3, String str4, Debug debug) throws NoSuchAlgorithmException, IOException, KeyStoreException, CertificateException, NoSuchProviderException {
        KeyStore keyStore;
        URL url2;
        URL url3;
        if (str == null) {
            throw new IllegalArgumentException("null KeyStore name");
        }
        char[] password = null;
        if (str2 == null) {
            try {
                str2 = KeyStore.getDefaultType();
            } finally {
                if (password != null) {
                    Arrays.fill(password, ' ');
                }
            }
        }
        if (P11KEYSTORE.equalsIgnoreCase(str2) && !NONE.equals(str)) {
            throw new IllegalArgumentException("Invalid value (" + str + ") for keystore URL.  If the keystore type is \"" + P11KEYSTORE + "\", the keystore url must be \"" + NONE + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
        if (str3 != null) {
            keyStore = KeyStore.getInstance(str2, str3);
        } else {
            keyStore = KeyStore.getInstance(str2);
        }
        if (str4 != null) {
            try {
                url2 = new URL(str4);
            } catch (MalformedURLException e2) {
                if (url == null) {
                    throw e2;
                }
                url2 = new URL(url, str4);
            }
            if (debug != null) {
                debug.println("reading password" + ((Object) url2));
            }
            InputStream inputStreamOpenStream = null;
            try {
                inputStreamOpenStream = url2.openStream();
                password = Password.readPassword(inputStreamOpenStream);
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
            } catch (Throwable th) {
                if (inputStreamOpenStream != null) {
                    inputStreamOpenStream.close();
                }
                throw th;
            }
        }
        if (NONE.equals(str)) {
            keyStore.load(null, password);
            KeyStore keyStore2 = keyStore;
            if (password != null) {
                Arrays.fill(password, ' ');
            }
            return keyStore2;
        }
        try {
            url3 = new URL(str);
        } catch (MalformedURLException e3) {
            if (url == null) {
                throw e3;
            }
            url3 = new URL(url, str);
        }
        if (debug != null) {
            debug.println("reading keystore" + ((Object) url3));
        }
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(getInputStream(url3));
            keyStore.load(bufferedInputStream, password);
            bufferedInputStream.close();
            return keyStore;
        } catch (Throwable th2) {
            bufferedInputStream.close();
            throw th2;
        }
    }
}
