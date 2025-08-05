package sun.security.util;

import java.io.IOException;
import java.security.CodeSigner;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.security.jca.Providers;

/* loaded from: rt.jar:sun/security/util/ManifestEntryVerifier.class */
public class ManifestEntryVerifier {
    private final String manifestFileName;
    private final Manifest man;
    private JarEntry entry;
    private static final Debug debug = Debug.getInstance("jar");
    private static final char[] hexc = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private String name = null;
    private boolean skip = true;
    private CodeSigner[] signers = null;
    HashMap<String, MessageDigest> createdDigests = new HashMap<>(11);
    ArrayList<MessageDigest> digests = new ArrayList<>();
    ArrayList<byte[]> manifestHashes = new ArrayList<>();

    /* loaded from: rt.jar:sun/security/util/ManifestEntryVerifier$SunProviderHolder.class */
    private static class SunProviderHolder {
        private static final Provider instance = Providers.getSunProvider();

        private SunProviderHolder() {
        }
    }

    public ManifestEntryVerifier(Manifest manifest, String str) {
        this.manifestFileName = str;
        this.man = manifest;
    }

    public void setEntry(String str, JarEntry jarEntry) throws IOException {
        this.digests.clear();
        this.manifestHashes.clear();
        this.name = str;
        this.entry = jarEntry;
        this.skip = true;
        this.signers = null;
        if (this.man == null || str == null) {
            return;
        }
        this.skip = false;
        Attributes attributes = this.man.getAttributes(str);
        if (attributes == null) {
            attributes = this.man.getAttributes("./" + str);
            if (attributes == null) {
                attributes = this.man.getAttributes("/" + str);
                if (attributes == null) {
                    return;
                }
            }
        }
        for (Map.Entry<Object, Object> entry : attributes.entrySet()) {
            String string = entry.getKey().toString();
            if (string.toUpperCase(Locale.ENGLISH).endsWith("-DIGEST")) {
                String strSubstring = string.substring(0, string.length() - 7);
                MessageDigest messageDigest = this.createdDigests.get(strSubstring);
                if (messageDigest == null) {
                    try {
                        messageDigest = MessageDigest.getInstance(strSubstring, SunProviderHolder.instance);
                        this.createdDigests.put(strSubstring, messageDigest);
                    } catch (NoSuchAlgorithmException e2) {
                    }
                }
                if (messageDigest != null) {
                    messageDigest.reset();
                    this.digests.add(messageDigest);
                    this.manifestHashes.add(Base64.getMimeDecoder().decode((String) entry.getValue()));
                }
            }
        }
    }

    public void update(byte b2) {
        if (this.skip) {
            return;
        }
        for (int i2 = 0; i2 < this.digests.size(); i2++) {
            this.digests.get(i2).update(b2);
        }
    }

    public void update(byte[] bArr, int i2, int i3) {
        if (this.skip) {
            return;
        }
        for (int i4 = 0; i4 < this.digests.size(); i4++) {
            this.digests.get(i4).update(bArr, i2, i3);
        }
    }

    public JarEntry getEntry() {
        return this.entry;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00da A[PHI: r12
  0x00da: PHI (r12v2 sun.security.util.JarConstraintsParameters) = 
  (r12v1 sun.security.util.JarConstraintsParameters)
  (r12v1 sun.security.util.JarConstraintsParameters)
  (r12v4 sun.security.util.JarConstraintsParameters)
 binds: [B:18:0x007e, B:29:0x00d4, B:27:0x00bf] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0168 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x018b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.security.CodeSigner[] verify(java.util.Hashtable<java.lang.String, java.security.CodeSigner[]> r6, java.util.Hashtable<java.lang.String, java.security.CodeSigner[]> r7, java.util.Map<java.security.CodeSigner[], java.util.Map<java.lang.String, java.lang.Boolean>> r8) throws java.util.jar.JarException {
        /*
            Method dump skipped, instructions count: 448
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.ManifestEntryVerifier.verify(java.util.Hashtable, java.util.Hashtable, java.util.Map):java.security.CodeSigner[]");
    }

    private static Map<String, Boolean> algsPermittedStatusForSigners(Map<CodeSigner[], Map<String, Boolean>> map, CodeSigner[] codeSignerArr) {
        if (codeSignerArr != null) {
            Map<String, Boolean> map2 = map.get(codeSignerArr);
            if (map2 == null) {
                map2 = new HashMap();
                map.put(codeSignerArr, map2);
            }
            return map2;
        }
        return null;
    }

    private boolean checkConstraints(String str, JarConstraintsParameters jarConstraintsParameters) {
        try {
            jarConstraintsParameters.setExtendedExceptionMsg(JarFile.MANIFEST_NAME, this.name + " entry");
            DisabledAlgorithmConstraints.jarConstraints().permits(str, (ConstraintsParameters) jarConstraintsParameters, false);
            return true;
        } catch (GeneralSecurityException e2) {
            if (debug != null) {
                debug.println("Digest algorithm is restricted: " + ((Object) e2));
                return false;
            }
            return false;
        }
    }

    static String toHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (int i2 = 0; i2 < bArr.length; i2++) {
            stringBuffer.append(hexc[(bArr[i2] >> 4) & 15]);
            stringBuffer.append(hexc[bArr[i2] & 15]);
        }
        return stringBuffer.toString();
    }
}
