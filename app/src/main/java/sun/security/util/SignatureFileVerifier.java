package sun.security.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import sun.security.action.GetIntegerAction;
import sun.security.jca.Providers;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.ManifestDigester;

/* loaded from: rt.jar:sun/security/util/SignatureFileVerifier.class */
public class SignatureFileVerifier {
    private ArrayList<CodeSigner[]> signerCache;
    private PKCS7 block;
    private byte[] sfBytes;
    private String name;
    private ManifestDigester md;
    private HashMap<String, MessageDigest> createdDigests;
    private CertificateFactory certificateFactory;
    private JarConstraintsParameters params;
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private static final Debug debug = Debug.getInstance("jar");
    private static final String ATTR_DIGEST = "-DIGEST-Manifest-Main-Attributes".toUpperCase(Locale.ENGLISH);
    public static final int MAX_SIG_FILE_SIZE = initializeMaxSigFileSize();
    private static final char[] hexc = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private boolean workaround = false;
    private Map<String, Boolean> permittedAlgs = new HashMap();

    public SignatureFileVerifier(ArrayList<CodeSigner[]> arrayList, ManifestDigester manifestDigester, String str, byte[] bArr) throws IOException, CertificateException {
        this.certificateFactory = null;
        Object objStartJarVerification = null;
        try {
            objStartJarVerification = Providers.startJarVerification();
            this.block = new PKCS7(bArr);
            this.sfBytes = this.block.getContentInfo().getData();
            this.certificateFactory = CertificateFactory.getInstance("X509");
            Providers.stopJarVerification(objStartJarVerification);
            this.name = str.substring(0, str.lastIndexOf(46)).toUpperCase(Locale.ENGLISH);
            this.md = manifestDigester;
            this.signerCache = arrayList;
        } catch (Throwable th) {
            Providers.stopJarVerification(objStartJarVerification);
            throw th;
        }
    }

    public boolean needSignatureFileBytes() {
        return this.sfBytes == null;
    }

    public boolean needSignatureFile(String str) {
        return this.name.equalsIgnoreCase(str);
    }

    public void setSignatureFile(byte[] bArr) {
        this.sfBytes = bArr;
    }

    public static boolean isBlockOrSF(String str) {
        return str.endsWith(".SF") || str.endsWith(".DSA") || str.endsWith(".RSA") || str.endsWith(".EC");
    }

    public static boolean isSigningRelated(String str) {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        if (!upperCase.startsWith("META-INF/")) {
            return false;
        }
        String strSubstring = upperCase.substring(9);
        if (strSubstring.indexOf(47) != -1) {
            return false;
        }
        if (isBlockOrSF(strSubstring) || strSubstring.equals("MANIFEST.MF")) {
            return true;
        }
        if (strSubstring.startsWith("SIG-")) {
            int iLastIndexOf = strSubstring.lastIndexOf(46);
            if (iLastIndexOf != -1) {
                String strSubstring2 = strSubstring.substring(iLastIndexOf + 1);
                if (strSubstring2.length() > 3 || strSubstring2.length() < 1) {
                    return false;
                }
                for (int i2 = 0; i2 < strSubstring2.length(); i2++) {
                    char cCharAt = strSubstring2.charAt(i2);
                    if ((cCharAt < 'A' || cCharAt > 'Z') && (cCharAt < '0' || cCharAt > '9')) {
                        return false;
                    }
                }
                return true;
            }
            return true;
        }
        return false;
    }

    private MessageDigest getDigest(String str) throws SignatureException {
        if (this.createdDigests == null) {
            this.createdDigests = new HashMap<>();
        }
        MessageDigest messageDigest = this.createdDigests.get(str);
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance(str);
                this.createdDigests.put(str, messageDigest);
            } catch (NoSuchAlgorithmException e2) {
            }
        }
        return messageDigest;
    }

    public void process(Hashtable<String, CodeSigner[]> hashtable, List<Object> list, String str) throws SignatureException, NoSuchAlgorithmException, IOException, CertificateException {
        Object objStartJarVerification = null;
        try {
            objStartJarVerification = Providers.startJarVerification();
            processImpl(hashtable, list, str);
            Providers.stopJarVerification(objStartJarVerification);
        } catch (Throwable th) {
            Providers.stopJarVerification(objStartJarVerification);
            throw th;
        }
    }

    private void processImpl(Hashtable<String, CodeSigner[]> hashtable, List<Object> list, String str) throws NoSuchAlgorithmException, SignatureException, IOException, CertificateException {
        Manifest manifest = new Manifest();
        manifest.read(new ByteArrayInputStream(this.sfBytes));
        String value = manifest.getMainAttributes().getValue(Attributes.Name.SIGNATURE_VERSION);
        if (value == null || !value.equalsIgnoreCase("1.0")) {
            return;
        }
        SignerInfo[] signerInfoArrVerify = this.block.verify(this.sfBytes);
        if (signerInfoArrVerify == null) {
            throw new SecurityException("cannot verify signature block file " + this.name);
        }
        CodeSigner[] signers = getSigners(signerInfoArrVerify, this.block);
        if (signers == null) {
            return;
        }
        this.params = new JarConstraintsParameters(signers);
        Iterator<String> it = SignerInfo.verifyAlgorithms(signerInfoArrVerify, this.params, this.name + " PKCS7").iterator();
        while (it.hasNext()) {
            this.permittedAlgs.put(it.next(), Boolean.TRUE);
        }
        boolean zVerifyManifestHash = verifyManifestHash(manifest, this.md, list);
        if (!zVerifyManifestHash && !verifyManifestMainAttrs(manifest, this.md)) {
            throw new SecurityException("Invalid signature file digest for Manifest main attributes");
        }
        for (Map.Entry<String, Attributes> entry : manifest.getEntries().entrySet()) {
            String key = entry.getKey();
            if (zVerifyManifestHash || verifySection(entry.getValue(), key, this.md)) {
                if (key.startsWith("./")) {
                    key = key.substring(2);
                }
                if (key.startsWith("/")) {
                    key = key.substring(1);
                }
                updateSigners(signers, hashtable, key);
                if (debug != null) {
                    debug.println("processSignature signed name = " + key);
                }
            } else if (debug != null) {
                debug.println("processSignature unsigned name = " + key);
            }
        }
        updateSigners(signers, hashtable, str);
    }

    private boolean permittedCheck(String str, String str2) {
        Boolean bool = this.permittedAlgs.get(str2);
        if (bool == null) {
            try {
                this.params.setExtendedExceptionMsg(this.name + ".SF", str + " attribute");
                DisabledAlgorithmConstraints.jarConstraints().permits(str2, (ConstraintsParameters) this.params, false);
                this.permittedAlgs.put(str2, Boolean.TRUE);
                return true;
            } catch (GeneralSecurityException e2) {
                this.permittedAlgs.put(str2, Boolean.FALSE);
                this.permittedAlgs.put(str.toUpperCase(), Boolean.FALSE);
                if (debug != null) {
                    if (e2.getMessage() != null) {
                        debug.println(str + ":  " + e2.getMessage());
                        return false;
                    }
                    debug.println("Debug info only. " + str + ":  " + str2 + " was disabled, no exception msg given.");
                    e2.printStackTrace();
                    return false;
                }
                return false;
            }
        }
        return bool.booleanValue();
    }

    String getWeakAlgorithms(String str) {
        String str2 = "";
        try {
            for (String str3 : this.permittedAlgs.keySet()) {
                if (str3.endsWith(str)) {
                    str2 = str2 + str3.substring(0, str3.length() - str.length()) + " ";
                }
            }
        } catch (RuntimeException e2) {
            str2 = "Unknown Algorithm(s).  Error processing " + str + ".  " + e2.getMessage();
        }
        if (str2.length() == 0) {
            return "Unknown Algorithm(s)";
        }
        return str2;
    }

    private boolean verifyManifestHash(Manifest manifest, ManifestDigester manifestDigester, List<Object> list) throws SignatureException, IOException {
        boolean z2 = false;
        boolean z3 = true;
        boolean z4 = false;
        for (Map.Entry<Object, Object> entry : manifest.getMainAttributes().entrySet()) {
            String string = entry.getKey().toString();
            if (string.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST-MANIFEST")) {
                String strSubstring = string.substring(0, string.length() - 16);
                z4 = true;
                if (permittedCheck(string, strSubstring)) {
                    z3 = false;
                    list.add(string);
                    list.add(entry.getValue());
                    MessageDigest digest = getDigest(strSubstring);
                    if (digest != null) {
                        byte[] bArrManifestDigest = manifestDigester.manifestDigest(digest);
                        byte[] bArrDecode = Base64.getMimeDecoder().decode((String) entry.getValue());
                        if (debug != null) {
                            debug.println("Signature File: Manifest digest " + strSubstring);
                            debug.println("  sigfile  " + toHex(bArrDecode));
                            debug.println("  computed " + toHex(bArrManifestDigest));
                            debug.println();
                        }
                        if (MessageDigest.isEqual(bArrManifestDigest, bArrDecode)) {
                            z2 = true;
                        }
                    }
                }
            }
        }
        if (debug != null) {
            debug.println("PermittedAlgs mapping: ");
            for (String str : this.permittedAlgs.keySet()) {
                debug.println(str + " : " + this.permittedAlgs.get(str).toString());
            }
        }
        if (z4 && z3) {
            throw new SignatureException("Manifest hash check failed (DIGEST-MANIFEST). Disabled algorithm(s) used: " + getWeakAlgorithms("-DIGEST-MANIFEST"));
        }
        return z2;
    }

    private boolean verifyManifestMainAttrs(Manifest manifest, ManifestDigester manifestDigester) throws SignatureException, IOException {
        boolean z2 = true;
        boolean z3 = true;
        boolean z4 = false;
        Iterator<Map.Entry<Object, Object>> it = manifest.getMainAttributes().entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry<Object, Object> next = it.next();
            String string = next.getKey().toString();
            if (string.toUpperCase(Locale.ENGLISH).endsWith(ATTR_DIGEST)) {
                String strSubstring = string.substring(0, string.length() - ATTR_DIGEST.length());
                z4 = true;
                if (permittedCheck(string, strSubstring)) {
                    z3 = false;
                    MessageDigest digest = getDigest(strSubstring);
                    if (digest == null) {
                        continue;
                    } else {
                        ManifestDigester.Entry entry = manifestDigester.get(ManifestDigester.MF_MAIN_ATTRS, false);
                        if (entry == null) {
                            throw new SignatureException("Manifest Main Attribute check failed due to missing main attributes entry");
                        }
                        byte[] bArrDigest = entry.digest(digest);
                        byte[] bArrDecode = Base64.getMimeDecoder().decode((String) next.getValue());
                        if (debug != null) {
                            debug.println("Signature File: Manifest Main Attributes digest " + digest.getAlgorithm());
                            debug.println("  sigfile  " + toHex(bArrDecode));
                            debug.println("  computed " + toHex(bArrDigest));
                            debug.println();
                        }
                        if (!MessageDigest.isEqual(bArrDigest, bArrDecode)) {
                            z2 = false;
                            if (debug != null) {
                                debug.println("Verification of Manifest main attributes failed");
                                debug.println();
                            }
                        }
                    }
                } else {
                    continue;
                }
            }
        }
        if (debug != null) {
            debug.println("PermittedAlgs mapping: ");
            for (String str : this.permittedAlgs.keySet()) {
                debug.println(str + " : " + this.permittedAlgs.get(str).toString());
            }
        }
        if (z4 && z3) {
            throw new SignatureException("Manifest Main Attribute check failed (" + ATTR_DIGEST + ").  Disabled algorithm(s) used: " + getWeakAlgorithms(ATTR_DIGEST));
        }
        return z2;
    }

    private boolean verifySection(Attributes attributes, String str, ManifestDigester manifestDigester) throws SignatureException, IOException {
        byte[] bArrDigest;
        boolean z2 = false;
        ManifestDigester.Entry entry = manifestDigester.get(str, this.block.isOldStyle());
        boolean z3 = true;
        boolean z4 = false;
        if (entry == null) {
            throw new SecurityException("no manifest section for signature file entry " + str);
        }
        if (attributes != null) {
            for (Map.Entry<Object, Object> entry2 : attributes.entrySet()) {
                String string = entry2.getKey().toString();
                if (string.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST")) {
                    String strSubstring = string.substring(0, string.length() - 7);
                    z4 = true;
                    if (permittedCheck(string, strSubstring)) {
                        z3 = false;
                        MessageDigest digest = getDigest(strSubstring);
                        if (digest != null) {
                            boolean z5 = false;
                            byte[] bArrDecode = Base64.getMimeDecoder().decode((String) entry2.getValue());
                            if (this.workaround) {
                                bArrDigest = entry.digestWorkaround(digest);
                            } else {
                                bArrDigest = entry.digest(digest);
                            }
                            if (debug != null) {
                                debug.println("Signature Block File: " + str + " digest=" + digest.getAlgorithm());
                                debug.println("  expected " + toHex(bArrDecode));
                                debug.println("  computed " + toHex(bArrDigest));
                                debug.println();
                            }
                            if (MessageDigest.isEqual(bArrDigest, bArrDecode)) {
                                z2 = true;
                                z5 = true;
                            } else if (!this.workaround) {
                                byte[] bArrDigestWorkaround = entry.digestWorkaround(digest);
                                if (MessageDigest.isEqual(bArrDigestWorkaround, bArrDecode)) {
                                    if (debug != null) {
                                        debug.println("  re-computed " + toHex(bArrDigestWorkaround));
                                        debug.println();
                                    }
                                    this.workaround = true;
                                    z2 = true;
                                    z5 = true;
                                }
                            }
                            if (!z5) {
                                throw new SecurityException("invalid " + digest.getAlgorithm() + " signature file digest for " + str);
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        if (debug != null) {
            debug.println("PermittedAlgs mapping: ");
            for (String str2 : this.permittedAlgs.keySet()) {
                debug.println(str2 + " : " + this.permittedAlgs.get(str2).toString());
            }
        }
        if (z4 && z3) {
            throw new SignatureException("Manifest Main Attribute check failed (DIGEST).  Disabled algorithm(s) used: " + getWeakAlgorithms("DIGEST"));
        }
        return z2;
    }

    private CodeSigner[] getSigners(SignerInfo[] signerInfoArr, PKCS7 pkcs7) throws NoSuchAlgorithmException, SignatureException, IOException, CertificateException {
        ArrayList arrayList = null;
        for (SignerInfo signerInfo : signerInfoArr) {
            ArrayList<X509Certificate> certificateChain = signerInfo.getCertificateChain(pkcs7);
            CertPath certPathGenerateCertPath = this.certificateFactory.generateCertPath(certificateChain);
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            arrayList.add(new CodeSigner(certPathGenerateCertPath, signerInfo.getTimestamp()));
            if (debug != null) {
                debug.println("Signature Block Certificate: " + ((Object) certificateChain.get(0)));
            }
        }
        if (arrayList != null) {
            return (CodeSigner[]) arrayList.toArray(new CodeSigner[arrayList.size()]);
        }
        return null;
    }

    static String toHex(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i2 = 0; i2 < bArr.length; i2++) {
            sb.append(hexc[(bArr[i2] >> 4) & 15]);
            sb.append(hexc[bArr[i2] & 15]);
        }
        return sb.toString();
    }

    static boolean contains(CodeSigner[] codeSignerArr, CodeSigner codeSigner) {
        for (CodeSigner codeSigner2 : codeSignerArr) {
            if (codeSigner2.equals(codeSigner)) {
                return true;
            }
        }
        return false;
    }

    static boolean isSubSet(CodeSigner[] codeSignerArr, CodeSigner[] codeSignerArr2) {
        if (codeSignerArr2 == codeSignerArr) {
            return true;
        }
        for (CodeSigner codeSigner : codeSignerArr) {
            if (!contains(codeSignerArr2, codeSigner)) {
                return false;
            }
        }
        return true;
    }

    static boolean matches(CodeSigner[] codeSignerArr, CodeSigner[] codeSignerArr2, CodeSigner[] codeSignerArr3) {
        if (codeSignerArr2 == null && codeSignerArr == codeSignerArr3) {
            return true;
        }
        if ((codeSignerArr2 != null && !isSubSet(codeSignerArr2, codeSignerArr)) || !isSubSet(codeSignerArr3, codeSignerArr)) {
            return false;
        }
        for (int i2 = 0; i2 < codeSignerArr.length; i2++) {
            if (!((codeSignerArr2 != null && contains(codeSignerArr2, codeSignerArr[i2])) || contains(codeSignerArr3, codeSignerArr[i2]))) {
                return false;
            }
        }
        return true;
    }

    void updateSigners(CodeSigner[] codeSignerArr, Hashtable<String, CodeSigner[]> hashtable, String str) {
        CodeSigner[] codeSignerArr2;
        CodeSigner[] codeSignerArr3 = hashtable.get(str);
        for (int size = this.signerCache.size() - 1; size != -1; size--) {
            CodeSigner[] codeSignerArr4 = this.signerCache.get(size);
            if (matches(codeSignerArr4, codeSignerArr3, codeSignerArr)) {
                hashtable.put(str, codeSignerArr4);
                return;
            }
        }
        if (codeSignerArr3 == null) {
            codeSignerArr2 = codeSignerArr;
        } else {
            codeSignerArr2 = new CodeSigner[codeSignerArr3.length + codeSignerArr.length];
            System.arraycopy(codeSignerArr3, 0, codeSignerArr2, 0, codeSignerArr3.length);
            System.arraycopy(codeSignerArr, 0, codeSignerArr2, codeSignerArr3.length, codeSignerArr.length);
        }
        this.signerCache.add(codeSignerArr2);
        hashtable.put(str, codeSignerArr2);
    }

    private static int initializeMaxSigFileSize() {
        Integer num = (Integer) AccessController.doPrivileged(new GetIntegerAction("jdk.jar.maxSignatureFileSize", 8000000));
        if (num.intValue() < 0 || num.intValue() > MAX_ARRAY_SIZE) {
            if (debug != null) {
                debug.println("Default signature file size 8000000 bytes is used as the specified size for the jdk.jar.maxSignatureFileSize system property is out of range: " + ((Object) num));
            }
            num = 8000000;
        }
        return num.intValue();
    }
}
