package sun.security.tools;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.text.Collator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.xml.soap.SOAPConstants;

/* loaded from: rt.jar:sun/security/tools/KeyStoreUtil.class */
public class KeyStoreUtil {
    private static final String JKS = "jks";

    private KeyStoreUtil() {
    }

    public static boolean isSelfSigned(X509Certificate x509Certificate) {
        return signedBy(x509Certificate, x509Certificate);
    }

    public static boolean signedBy(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
        if (!x509Certificate2.getSubjectX500Principal().equals(x509Certificate.getIssuerX500Principal())) {
            return false;
        }
        try {
            x509Certificate.verify(x509Certificate2.getPublicKey());
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean isWindowsKeyStore(String str) {
        return str != null && (str.equalsIgnoreCase("Windows-MY") || str.equalsIgnoreCase("Windows-ROOT"));
    }

    public static String niceStoreTypeName(String str) {
        if (str.equalsIgnoreCase("Windows-MY")) {
            return "Windows-MY";
        }
        if (str.equalsIgnoreCase("Windows-ROOT")) {
            return "Windows-ROOT";
        }
        return str.toUpperCase(Locale.ENGLISH);
    }

    public static KeyStore getCacertsKeyStore() throws Exception {
        String str = File.separator;
        File file = new File(System.getProperty("java.home") + str + "lib" + str + "security" + str + "cacerts");
        if (!file.exists()) {
            return null;
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        Throwable th = null;
        try {
            try {
                KeyStore keyStore = KeyStore.getInstance(JKS);
                keyStore.load(fileInputStream, null);
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
                return keyStore;
            } finally {
            }
        } catch (Throwable th3) {
            if (fileInputStream != null) {
                if (th != null) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    fileInputStream.close();
                }
            }
            throw th3;
        }
    }

    public static char[] getPassWithModifier(String str, String str2, ResourceBundle resourceBundle, Collator collator) {
        URL url;
        if (str == null) {
            return str2.toCharArray();
        }
        if (collator.compare(str, SOAPConstants.SOAP_ENV_PREFIX) == 0) {
            String str3 = System.getenv(str2);
            if (str3 == null) {
                System.err.println(resourceBundle.getString("Cannot.find.environment.variable.") + str2);
                return null;
            }
            return str3.toCharArray();
        }
        if (collator.compare(str, DeploymentDescriptorParser.ATTR_FILE) == 0) {
            try {
                try {
                    url = new URL(str2);
                } catch (MalformedURLException e2) {
                    File file = new File(str2);
                    if (file.exists()) {
                        url = file.toURI().toURL();
                    } else {
                        System.err.println(resourceBundle.getString("Cannot.find.file.") + str2);
                        return null;
                    }
                }
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                Throwable th = null;
                try {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        char[] cArr = new char[0];
                        if (bufferedReader != null) {
                            if (0 != 0) {
                                try {
                                    bufferedReader.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                bufferedReader.close();
                            }
                        }
                        return cArr;
                    }
                    char[] charArray = line.toCharArray();
                    if (bufferedReader != null) {
                        if (0 != 0) {
                            try {
                                bufferedReader.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        } else {
                            bufferedReader.close();
                        }
                    }
                    return charArray;
                } finally {
                }
            } catch (IOException e3) {
                System.err.println(e3);
                return null;
            }
            System.err.println(e3);
            return null;
        }
        System.err.println(resourceBundle.getString("Unknown.password.type.") + str);
        return null;
    }
}
