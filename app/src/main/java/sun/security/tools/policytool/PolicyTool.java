package sun.security.tools.policytool;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.text.Collator;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.security.auth.login.LoginException;
import javax.security.auth.x500.X500Principal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import sun.security.provider.PolicyParser;
import sun.security.util.PolicyUtil;
import sun.security.util.PropertyExpander;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/tools/policytool/PolicyTool.class */
public class PolicyTool {
    static final ResourceBundle rb = ResourceBundle.getBundle("sun.security.tools.policytool.Resources");
    static final Collator collator = Collator.getInstance();
    Vector<String> warnings;
    boolean newWarning;
    boolean modified;
    private static final boolean testing = false;
    private static final Class<?>[] TWOPARAMS;
    private static final Class<?>[] ONEPARAMS;
    private static final Class<?>[] NOPARAMS;
    private static String policyFileName;
    private Vector<PolicyEntry> policyEntries;
    private PolicyParser parser;
    private KeyStore keyStore;
    private String keyStoreName;
    private String keyStoreType;
    private String keyStoreProvider;
    private String keyStorePwdURL;
    private static final String P11KEYSTORE = "PKCS11";
    private static final String NONE = "NONE";

    static {
        collator.setStrength(0);
        if (System.getProperty("apple.laf.useScreenMenuBar") == null) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }
        System.setProperty("apple.awt.application.name", getMessage("Policy.Tool"));
        if (System.getProperty("swing.defaultlaf") == null) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e2) {
            }
        }
        TWOPARAMS = new Class[]{String.class, String.class};
        ONEPARAMS = new Class[]{String.class};
        NOPARAMS = new Class[0];
        policyFileName = null;
    }

    private PolicyTool() {
        this.newWarning = false;
        this.modified = false;
        this.policyEntries = null;
        this.parser = null;
        this.keyStore = null;
        this.keyStoreName = " ";
        this.keyStoreType = " ";
        this.keyStoreProvider = " ";
        this.keyStorePwdURL = " ";
        this.policyEntries = new Vector<>();
        this.parser = new PolicyParser();
        this.warnings = new Vector<>();
    }

    String getPolicyFileName() {
        return policyFileName;
    }

    void setPolicyFileName(String str) {
        policyFileName = str;
    }

    void clearKeyStoreInfo() {
        this.keyStoreName = null;
        this.keyStoreType = null;
        this.keyStoreProvider = null;
        this.keyStorePwdURL = null;
        this.keyStore = null;
    }

    String getKeyStoreName() {
        return this.keyStoreName;
    }

    String getKeyStoreType() {
        return this.keyStoreType;
    }

    String getKeyStoreProvider() {
        return this.keyStoreProvider;
    }

    String getKeyStorePwdURL() {
        return this.keyStorePwdURL;
    }

    void openPolicy(String str) throws IllegalAccessException, NoSuchMethodException, PolicyParser.ParsingException, IOException, KeyStoreException, CertificateException, InvocationTargetException, NoSuchAlgorithmException, UnrecoverableKeyException, InstantiationException, SecurityException, ClassNotFoundException, NoSuchProviderException, PropertyExpander.ExpandException {
        this.newWarning = false;
        this.policyEntries = new Vector<>();
        this.parser = new PolicyParser();
        this.warnings = new Vector<>();
        setPolicyFileName(null);
        clearKeyStoreInfo();
        if (str == null) {
            this.modified = false;
            return;
        }
        setPolicyFileName(str);
        this.parser.read(new FileReader(str));
        openKeyStore(this.parser.getKeyStoreUrl(), this.parser.getKeyStoreType(), this.parser.getKeyStoreProvider(), this.parser.getStorePassURL());
        Enumeration<PolicyParser.GrantEntry> enumerationGrantElements = this.parser.grantElements();
        while (enumerationGrantElements.hasMoreElements()) {
            PolicyParser.GrantEntry grantEntryNextElement2 = enumerationGrantElements.nextElement2();
            if (grantEntryNextElement2.signedBy != null) {
                String[] signers = parseSigners(grantEntryNextElement2.signedBy);
                for (int i2 = 0; i2 < signers.length; i2++) {
                    if (getPublicKeyAlias(signers[i2]) == null) {
                        this.newWarning = true;
                        this.warnings.addElement(new MessageFormat(getMessage("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured.")).format(new Object[]{signers[i2]}));
                    }
                }
            }
            ListIterator<PolicyParser.PrincipalEntry> listIterator = grantEntryNextElement2.principals.listIterator(0);
            while (listIterator.hasNext()) {
                PolicyParser.PrincipalEntry next = listIterator.next();
                try {
                    verifyPrincipal(next.getPrincipalClass(), next.getPrincipalName());
                } catch (ClassNotFoundException e2) {
                    this.newWarning = true;
                    this.warnings.addElement(new MessageFormat(getMessage("Warning.Class.not.found.class")).format(new Object[]{next.getPrincipalClass()}));
                }
            }
            Enumeration<PolicyParser.PermissionEntry> enumerationPermissionElements = grantEntryNextElement2.permissionElements();
            while (enumerationPermissionElements.hasMoreElements()) {
                PolicyParser.PermissionEntry permissionEntryNextElement2 = enumerationPermissionElements.nextElement2();
                try {
                    verifyPermission(permissionEntryNextElement2.permission, permissionEntryNextElement2.name, permissionEntryNextElement2.action);
                } catch (ClassNotFoundException e3) {
                    this.newWarning = true;
                    this.warnings.addElement(new MessageFormat(getMessage("Warning.Class.not.found.class")).format(new Object[]{permissionEntryNextElement2.permission}));
                } catch (InvocationTargetException e4) {
                    this.newWarning = true;
                    this.warnings.addElement(new MessageFormat(getMessage("Warning.Invalid.argument.s.for.constructor.arg")).format(new Object[]{permissionEntryNextElement2.permission}));
                }
                if (permissionEntryNextElement2.signedBy != null) {
                    String[] signers2 = parseSigners(permissionEntryNextElement2.signedBy);
                    for (int i3 = 0; i3 < signers2.length; i3++) {
                        if (getPublicKeyAlias(signers2[i3]) == null) {
                            this.newWarning = true;
                            this.warnings.addElement(new MessageFormat(getMessage("Warning.A.public.key.for.alias.signers.i.does.not.exist.Make.sure.a.KeyStore.is.properly.configured.")).format(new Object[]{signers2[i3]}));
                        }
                    }
                }
            }
            this.policyEntries.addElement(new PolicyEntry(this, grantEntryNextElement2));
        }
        this.modified = false;
    }

    void savePolicy(String str) throws IOException {
        this.parser.setKeyStoreUrl(this.keyStoreName);
        this.parser.setKeyStoreType(this.keyStoreType);
        this.parser.setKeyStoreProvider(this.keyStoreProvider);
        this.parser.setStorePassURL(this.keyStorePwdURL);
        this.parser.write(new FileWriter(str));
        this.modified = false;
    }

    void openKeyStore(String str, String str2, String str3, String str4) throws NoSuchAlgorithmException, UnrecoverableKeyException, IOException, KeyStoreException, CertificateException, NoSuchProviderException, PropertyExpander.ExpandException {
        if (str == null && str2 == null && str3 == null && str4 == null) {
            this.keyStoreName = null;
            this.keyStoreType = null;
            this.keyStoreProvider = null;
            this.keyStorePwdURL = null;
            return;
        }
        URL url = null;
        if (policyFileName != null) {
            url = new URL("file:" + new File(policyFileName).getCanonicalPath());
        }
        if (str != null && str.length() > 0) {
            str = PropertyExpander.expand(str).replace(File.separatorChar, '/');
        }
        if (str2 == null || str2.length() == 0) {
            str2 = KeyStore.getDefaultType();
        }
        if (str4 != null && str4.length() > 0) {
            str4 = PropertyExpander.expand(str4).replace(File.separatorChar, '/');
        }
        try {
            this.keyStore = PolicyUtil.getKeyStore(url, str, str2, str3, str4, null);
            this.keyStoreName = str;
            this.keyStoreType = str2;
            this.keyStoreProvider = str3;
            this.keyStorePwdURL = str4;
        } catch (IOException e2) {
            Throwable cause = e2.getCause();
            if (cause != null && (cause instanceof LoginException) && "no password provided, and no callback handler available for retrieving password".equals(cause.getMessage())) {
                throw new IOException("no password provided, and no callback handler available for retrieving password");
            }
            throw e2;
        }
    }

    boolean addEntry(PolicyEntry policyEntry, int i2) {
        if (i2 < 0) {
            this.policyEntries.addElement(policyEntry);
            this.parser.add(policyEntry.getGrantEntry());
            return true;
        }
        this.parser.replace(this.policyEntries.elementAt(i2).getGrantEntry(), policyEntry.getGrantEntry());
        this.policyEntries.setElementAt(policyEntry, i2);
        return true;
    }

    boolean addPrinEntry(PolicyEntry policyEntry, PolicyParser.PrincipalEntry principalEntry, int i2) {
        PolicyParser.GrantEntry grantEntry = policyEntry.getGrantEntry();
        if (grantEntry.contains(principalEntry)) {
            return false;
        }
        LinkedList<PolicyParser.PrincipalEntry> linkedList = grantEntry.principals;
        if (i2 != -1) {
            linkedList.set(i2, principalEntry);
        } else {
            linkedList.add(principalEntry);
        }
        this.modified = true;
        return true;
    }

    boolean addPermEntry(PolicyEntry policyEntry, PolicyParser.PermissionEntry permissionEntry, int i2) {
        PolicyParser.GrantEntry grantEntry = policyEntry.getGrantEntry();
        if (grantEntry.contains(permissionEntry)) {
            return false;
        }
        Vector<PolicyParser.PermissionEntry> vector = grantEntry.permissionEntries;
        if (i2 != -1) {
            vector.setElementAt(permissionEntry, i2);
        } else {
            vector.addElement(permissionEntry);
        }
        this.modified = true;
        return true;
    }

    boolean removePermEntry(PolicyEntry policyEntry, PolicyParser.PermissionEntry permissionEntry) {
        this.modified = policyEntry.getGrantEntry().remove(permissionEntry);
        return this.modified;
    }

    boolean removeEntry(PolicyEntry policyEntry) {
        this.parser.remove(policyEntry.getGrantEntry());
        this.modified = true;
        return this.policyEntries.removeElement(policyEntry);
    }

    PolicyEntry[] getEntry() {
        if (this.policyEntries.size() > 0) {
            PolicyEntry[] policyEntryArr = new PolicyEntry[this.policyEntries.size()];
            for (int i2 = 0; i2 < this.policyEntries.size(); i2++) {
                policyEntryArr[i2] = this.policyEntries.elementAt(i2);
            }
            return policyEntryArr;
        }
        return null;
    }

    PublicKey getPublicKeyAlias(String str) throws KeyStoreException {
        Certificate certificate;
        if (this.keyStore == null || (certificate = this.keyStore.getCertificate(str)) == null) {
            return null;
        }
        return certificate.getPublicKey();
    }

    String[] getPublicKeyAlias() throws KeyStoreException {
        int i2 = 0;
        String[] strArr = null;
        if (this.keyStore == null) {
            return null;
        }
        Enumeration<String> enumerationAliases = this.keyStore.aliases();
        while (enumerationAliases.hasMoreElements()) {
            enumerationAliases.nextElement2();
            i2++;
        }
        if (i2 > 0) {
            strArr = new String[i2];
            int i3 = 0;
            Enumeration<String> enumerationAliases2 = this.keyStore.aliases();
            while (enumerationAliases2.hasMoreElements()) {
                strArr[i3] = new String(enumerationAliases2.nextElement2());
                i3++;
            }
        }
        return strArr;
    }

    String[] parseSigners(String str) {
        int i2 = 1;
        int i3 = 0;
        int iIndexOf = 0;
        int i4 = 0;
        while (iIndexOf >= 0) {
            iIndexOf = str.indexOf(44, i3);
            if (iIndexOf >= 0) {
                i2++;
                i3 = iIndexOf + 1;
            }
        }
        String[] strArr = new String[i2];
        int i5 = 0;
        int i6 = 0;
        while (i5 >= 0) {
            int iIndexOf2 = str.indexOf(44, i6);
            i5 = iIndexOf2;
            if (iIndexOf2 >= 0) {
                strArr[i4] = str.substring(i6, i5).trim();
                i4++;
                i6 = i5 + 1;
            } else {
                strArr[i4] = str.substring(i6).trim();
            }
        }
        return strArr;
    }

    void verifyPrincipal(String str, String str2) throws InstantiationException, ClassNotFoundException {
        if (str.equals(PolicyParser.PrincipalEntry.WILDCARD_CLASS) || str.equals(PolicyParser.PrincipalEntry.REPLACE_NAME)) {
            return;
        }
        Class<?> cls = Class.forName("java.security.Principal");
        Class<?> cls2 = Class.forName(str, true, Thread.currentThread().getContextClassLoader());
        if (!cls.isAssignableFrom(cls2)) {
            throw new InstantiationException(new MessageFormat(getMessage("Illegal.Principal.Type.type")).format(new Object[]{str}));
        }
        if (ToolDialog.X500_PRIN_CLASS.equals(cls2.getName())) {
            new X500Principal(str2);
        }
    }

    void verifyPermission(String str, String str2, String str3) throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, SecurityException, InvocationTargetException {
        Class<?> cls = Class.forName(str, true, Thread.currentThread().getContextClassLoader());
        Constructor<?> constructor = null;
        Vector vector = new Vector(2);
        if (str2 != null) {
            vector.add(str2);
        }
        if (str3 != null) {
            vector.add(str3);
        }
        switch (vector.size()) {
            case 0:
                try {
                    constructor = cls.getConstructor(NOPARAMS);
                    break;
                } catch (NoSuchMethodException e2) {
                    vector.add(null);
                }
            case 1:
                try {
                    constructor = cls.getConstructor(ONEPARAMS);
                    break;
                } catch (NoSuchMethodException e3) {
                    vector.add(null);
                }
            case 2:
                constructor = cls.getConstructor(TWOPARAMS);
                break;
        }
    }

    static void parseArgs(String[] strArr) {
        int i2 = 0;
        while (i2 < strArr.length && strArr[i2].startsWith(LanguageTag.SEP)) {
            String str = strArr[i2];
            if (collator.compare(str, "-file") == 0) {
                i2++;
                if (i2 == strArr.length) {
                    usage();
                }
                policyFileName = strArr[i2];
            } else {
                System.err.println(new MessageFormat(getMessage("Illegal.option.option")).format(new Object[]{str}));
                usage();
            }
            i2++;
        }
    }

    static void usage() {
        System.out.println(getMessage("Usage.policytool.options."));
        System.out.println();
        System.out.println(getMessage(".file.file.policy.file.location"));
        System.out.println();
        System.exit(1);
    }

    public static void main(final String[] strArr) {
        parseArgs(strArr);
        SwingUtilities.invokeLater(new Runnable() { // from class: sun.security.tools.policytool.PolicyTool.1
            @Override // java.lang.Runnable
            public void run() {
                new ToolWindow(new PolicyTool()).displayToolWindow(strArr);
            }
        });
    }

    static String splitToWords(String str) {
        return str.replaceAll("([A-Z])", " $1");
    }

    static String getMessage(String str) {
        return removeMnemonicAmpersand(rb.getString(str));
    }

    static int getMnemonicInt(String str) {
        return findMnemonicInt(rb.getString(str));
    }

    static int getDisplayedMnemonicIndex(String str) {
        return findMnemonicIndex(rb.getString(str));
    }

    private static int findMnemonicInt(String str) {
        int i2 = 0;
        while (i2 < str.length() - 1) {
            if (str.charAt(i2) == '&') {
                if (str.charAt(i2 + 1) != '&') {
                    return KeyEvent.getExtendedKeyCodeForChar(str.charAt(i2 + 1));
                }
                i2++;
            }
            i2++;
        }
        return 0;
    }

    private static int findMnemonicIndex(String str) {
        int i2 = 0;
        while (i2 < str.length() - 1) {
            if (str.charAt(i2) == '&') {
                if (str.charAt(i2 + 1) != '&') {
                    return i2;
                }
                i2++;
            }
            i2++;
        }
        return -1;
    }

    private static String removeMnemonicAmpersand(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt != '&' || i2 == str.length() - 1 || str.charAt(i2 + 1) == '&') {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }
}
