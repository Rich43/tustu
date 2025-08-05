package sun.security.krb5;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import net.lingala.zip4j.util.InternalZipConstants;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import sun.net.dns.ResolverConfiguration;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.KrbAsReqBuilder;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.crypto.EType;
import sun.security.util.SecurityProperties;

/* loaded from: rt.jar:sun/security/krb5/Config.class */
public class Config {
    public static final boolean DISABLE_REFERRALS;
    public static final int MAX_REFERRALS;
    private static Config singleton;
    private Hashtable<String, Object> stanzaTable;
    private static boolean DEBUG;
    private static final int BASE16_0 = 1;
    private static final int BASE16_1 = 16;
    private static final int BASE16_2 = 256;
    private static final int BASE16_3 = 4096;
    private final String defaultRealm;
    private final String defaultKDC;

    private static native String getWindowsDirectory(boolean z2);

    static {
        String strPrivilegedGetOverridable = SecurityProperties.privilegedGetOverridable("sun.security.krb5.disableReferrals");
        if (strPrivilegedGetOverridable != null) {
            DISABLE_REFERRALS = "true".equalsIgnoreCase(strPrivilegedGetOverridable);
        } else {
            DISABLE_REFERRALS = false;
        }
        int i2 = 5;
        try {
            i2 = Integer.parseInt(SecurityProperties.privilegedGetOverridable("sun.security.krb5.maxReferrals"));
        } catch (NumberFormatException e2) {
        }
        MAX_REFERRALS = i2;
        singleton = null;
        DEBUG = Krb5.DEBUG;
    }

    public static synchronized Config getInstance() throws KrbException {
        if (singleton == null) {
            singleton = new Config();
        }
        return singleton;
    }

    public static void refresh() throws KrbException {
        synchronized (Config.class) {
            singleton = new Config();
        }
        KdcComm.initStatic();
        EType.initStatic();
        Checksum.initStatic();
        KrbAsReqBuilder.ReferralsState.initStatic();
    }

    private static boolean isMacosLionOrBetter() {
        if (!getProperty("os.name").contains("OS X")) {
            return false;
        }
        String[] strArrSplit = getProperty("os.version").split("\\.");
        if (!strArrSplit[0].equals("10") || strArrSplit.length < 2) {
            return false;
        }
        try {
            return Integer.parseInt(strArrSplit[1]) >= 7;
        } catch (NumberFormatException e2) {
            return false;
        }
    }

    private Config() throws KrbException {
        this.stanzaTable = new Hashtable<>();
        String property = getProperty("java.security.krb5.kdc");
        if (property != null) {
            this.defaultKDC = property.replace(':', ' ');
        } else {
            this.defaultKDC = null;
        }
        this.defaultRealm = getProperty("java.security.krb5.realm");
        if ((this.defaultKDC == null && this.defaultRealm != null) || (this.defaultRealm == null && this.defaultKDC != null)) {
            throw new KrbException("System property java.security.krb5.kdc and java.security.krb5.realm both must be set or neither must be set.");
        }
        try {
            String javaFileName = getJavaFileName();
            if (javaFileName != null) {
                this.stanzaTable = parseStanzaTable(loadConfigFile(javaFileName));
                if (DEBUG) {
                    System.out.println("Loaded from Java config");
                }
            } else {
                boolean z2 = false;
                if (isMacosLionOrBetter()) {
                    try {
                        this.stanzaTable = SCDynamicStoreConfig.getConfig();
                        if (DEBUG) {
                            System.out.println("Loaded from SCDynamicStoreConfig");
                        }
                        z2 = true;
                    } catch (IOException e2) {
                    }
                }
                if (!z2) {
                    this.stanzaTable = parseStanzaTable(loadConfigFile(getNativeFileName()));
                    if (DEBUG) {
                        System.out.println("Loaded from native config");
                    }
                }
            }
        } catch (IOException e3) {
        }
    }

    public String get(String... strArr) {
        Vector<String> string0 = getString0(strArr);
        if (string0 == null) {
            return null;
        }
        return string0.lastElement();
    }

    public Boolean getBooleanObject(String... strArr) {
        String str = get(strArr);
        if (str == null) {
            return null;
        }
        switch (str.toLowerCase(Locale.US)) {
            case "yes":
            case "true":
                return Boolean.TRUE;
            case "no":
            case "false":
                return Boolean.FALSE;
            default:
                return null;
        }
    }

    public String getAll(String... strArr) {
        Vector<String> string0 = getString0(strArr);
        if (string0 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean z2 = true;
        Iterator<String> it = string0.iterator();
        while (it.hasNext()) {
            String strReplaceAll = it.next().replaceAll("[\\s,]+", " ");
            if (z2) {
                sb.append(strReplaceAll);
                z2 = false;
            } else {
                sb.append(' ').append(strReplaceAll);
            }
        }
        return sb.toString();
    }

    public boolean exists(String... strArr) {
        return get0(strArr) != null;
    }

    private Vector<String> getString0(String... strArr) {
        try {
            return (Vector) get0(strArr);
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    private Object get0(String... strArr) {
        Hashtable<String, Object> hashtable = this.stanzaTable;
        try {
            for (String str : strArr) {
                hashtable = hashtable.get(str);
                if (hashtable == null) {
                    return null;
                }
            }
            return hashtable;
        } catch (ClassCastException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    public static int duration(String str) throws NumberFormatException, KrbException {
        if (str.isEmpty()) {
            throw new KrbException("Duration cannot be empty");
        }
        if (str.matches("\\d+")) {
            return Integer.parseInt(str);
        }
        Matcher matcher = Pattern.compile("(\\d+):(\\d+)(:(\\d+))?").matcher(str);
        if (matcher.matches()) {
            int i2 = Integer.parseInt(matcher.group(1));
            int i3 = Integer.parseInt(matcher.group(2));
            if (i3 >= 60) {
                throw new KrbException("Illegal duration format " + str);
            }
            int i4 = (i2 * 3600) + (i3 * 60);
            if (matcher.group(4) != null) {
                int i5 = Integer.parseInt(matcher.group(4));
                if (i5 >= 60) {
                    throw new KrbException("Illegal duration format " + str);
                }
                i4 += i5;
            }
            return i4;
        }
        Matcher matcher2 = Pattern.compile("((\\d+)d)?\\s*((\\d+)h)?\\s*((\\d+)m)?\\s*((\\d+)s)?", 2).matcher(str);
        if (matcher2.matches()) {
            int i6 = 0;
            if (matcher2.group(2) != null) {
                i6 = 0 + (Krb5.DEFAULT_MAXIMUM_TICKET_LIFETIME * Integer.parseInt(matcher2.group(2)));
            }
            if (matcher2.group(4) != null) {
                i6 += 3600 * Integer.parseInt(matcher2.group(4));
            }
            if (matcher2.group(6) != null) {
                i6 += 60 * Integer.parseInt(matcher2.group(6));
            }
            if (matcher2.group(8) != null) {
                i6 += Integer.parseInt(matcher2.group(8));
            }
            return i6;
        }
        throw new KrbException("Illegal duration format " + str);
    }

    public int getIntValue(String... strArr) {
        String str = get(strArr);
        int intValue = Integer.MIN_VALUE;
        if (str != null) {
            try {
                intValue = parseIntValue(str);
            } catch (NumberFormatException e2) {
                if (DEBUG) {
                    System.out.println("Exception in getting value of " + Arrays.toString(strArr) + " " + e2.getMessage());
                    System.out.println("Setting " + Arrays.toString(strArr) + " to minimum value");
                }
                intValue = Integer.MIN_VALUE;
            }
        }
        return intValue;
    }

    public boolean getBooleanValue(String... strArr) {
        String str = get(strArr);
        if (str != null && str.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    private int parseIntValue(String str) throws NumberFormatException {
        int base = 0;
        if (str.startsWith(Marker.ANY_NON_NULL_MARKER)) {
            return Integer.parseInt(str.substring(1));
        }
        if (str.startsWith("0x")) {
            char[] charArray = str.substring(2).toCharArray();
            if (charArray.length > 8) {
                throw new NumberFormatException();
            }
            for (int i2 = 0; i2 < charArray.length; i2++) {
                int length = (charArray.length - i2) - 1;
                switch (charArray[i2]) {
                    case '0':
                        base += 0;
                        break;
                    case '1':
                        base += 1 * getBase(length);
                        break;
                    case '2':
                        base += 2 * getBase(length);
                        break;
                    case '3':
                        base += 3 * getBase(length);
                        break;
                    case '4':
                        base += 4 * getBase(length);
                        break;
                    case '5':
                        base += 5 * getBase(length);
                        break;
                    case '6':
                        base += 6 * getBase(length);
                        break;
                    case '7':
                        base += 7 * getBase(length);
                        break;
                    case '8':
                        base += 8 * getBase(length);
                        break;
                    case '9':
                        base += 9 * getBase(length);
                        break;
                    case ':':
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                    case '?':
                    case '@':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '[':
                    case '\\':
                    case ']':
                    case '^':
                    case '_':
                    case '`':
                    default:
                        throw new NumberFormatException("Invalid numerical format");
                    case 'A':
                    case 'a':
                        base += 10 * getBase(length);
                        break;
                    case 'B':
                    case 'b':
                        base += 11 * getBase(length);
                        break;
                    case 'C':
                    case 'c':
                        base += 12 * getBase(length);
                        break;
                    case 'D':
                    case 'd':
                        base += 13 * getBase(length);
                        break;
                    case 'E':
                    case 'e':
                        base += 14 * getBase(length);
                        break;
                    case 'F':
                    case 'f':
                        base += 15 * getBase(length);
                        break;
                }
            }
            if (base < 0) {
                throw new NumberFormatException("Data overflow.");
            }
        } else {
            base = Integer.parseInt(str);
        }
        return base;
    }

    private int getBase(int i2) {
        int i3 = 16;
        switch (i2) {
            case 0:
                i3 = 1;
                break;
            case 1:
                i3 = 16;
                break;
            case 2:
                i3 = 256;
                break;
            case 3:
                i3 = 4096;
                break;
            default:
                for (int i4 = 1; i4 < i2; i4++) {
                    i3 *= 16;
                }
                break;
        }
        return i3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:40:0x0158, code lost:
    
        if (r15 == null) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x015b, code lost:
    
        r0.add(r15);
        r0.add("}");
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x016e, code lost:
    
        if (r0 == null) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0173, code lost:
    
        if (0 == 0) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0176, code lost:
    
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x017d, code lost:
    
        r14 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x017f, code lost:
    
        r0.addSuppressed(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0189, code lost:
    
        r0.close();
     */
    /* JADX WARN: Finally extract failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.util.List<java.lang.String> loadConfigFile(final java.lang.String r10) throws java.io.IOException, sun.security.krb5.KrbException {
        /*
            Method dump skipped, instructions count: 457
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.krb5.Config.loadConfigFile(java.lang.String):java.util.List");
    }

    private Hashtable<String, Object> parseStanzaTable(List<String> list) throws KrbException {
        Vector vector;
        Hashtable<String, Object> hashtable = this.stanzaTable;
        for (String str : list) {
            if (str.equals("}")) {
                hashtable = (Hashtable) hashtable.remove(" PARENT ");
                if (hashtable == null) {
                    throw new KrbException("Unmatched close brace");
                }
            } else {
                int iIndexOf = str.indexOf(61);
                if (iIndexOf < 0) {
                    throw new KrbException("Illegal config content:" + str);
                }
                String strTrim = str.substring(0, iIndexOf).trim();
                String strTrimmed = trimmed(str.substring(iIndexOf + 1));
                if (strTrimmed.equals(VectorFormat.DEFAULT_PREFIX)) {
                    if (hashtable == this.stanzaTable) {
                        strTrim = strTrim.toLowerCase(Locale.US);
                    }
                    Hashtable<String, Object> hashtable2 = new Hashtable<>();
                    hashtable.put(strTrim, hashtable2);
                    hashtable2.put(" PARENT ", hashtable);
                    hashtable = hashtable2;
                } else {
                    if (hashtable.containsKey(strTrim)) {
                        if (!(hashtable.get(strTrim) instanceof Vector)) {
                            throw new KrbException("Key " + strTrim + "used for both value and section");
                        }
                        vector = (Vector) hashtable.get(strTrim);
                    } else {
                        vector = new Vector();
                        hashtable.put(strTrim, vector);
                    }
                    vector.add(strTrimmed);
                }
            }
        }
        if (hashtable != this.stanzaTable) {
            throw new KrbException("Not closed");
        }
        return hashtable;
    }

    private String getJavaFileName() {
        String property = getProperty("java.security.krb5.conf");
        if (property == null) {
            property = getProperty("java.home") + File.separator + "lib" + File.separator + "security" + File.separator + "krb5.conf";
            if (!fileExists(property)) {
                property = null;
            }
        }
        if (DEBUG) {
            System.out.println("Java config name: " + property);
        }
        return property;
    }

    private String getNativeFileName() {
        String windowsDirectory;
        String str;
        String str2;
        String strFindMacosConfigFile = null;
        String property = getProperty("os.name");
        if (property.startsWith("Windows")) {
            try {
                Credentials.ensureLoaded();
            } catch (Exception e2) {
            }
            if (Credentials.alreadyLoaded) {
                String windowsDirectory2 = getWindowsDirectory(false);
                if (windowsDirectory2 != null) {
                    if (windowsDirectory2.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                        str2 = windowsDirectory2 + "krb5.ini";
                    } else {
                        str2 = windowsDirectory2 + "\\krb5.ini";
                    }
                    if (fileExists(str2)) {
                        strFindMacosConfigFile = str2;
                    }
                }
                if (strFindMacosConfigFile == null && (windowsDirectory = getWindowsDirectory(true)) != null) {
                    if (windowsDirectory.endsWith(FXMLLoader.ESCAPE_PREFIX)) {
                        str = windowsDirectory + "krb5.ini";
                    } else {
                        str = windowsDirectory + "\\krb5.ini";
                    }
                    strFindMacosConfigFile = str;
                }
            }
            if (strFindMacosConfigFile == null) {
                strFindMacosConfigFile = "c:\\winnt\\krb5.ini";
            }
        } else if (property.startsWith("SunOS")) {
            strFindMacosConfigFile = "/etc/krb5/krb5.conf";
        } else if (property.contains("OS X")) {
            strFindMacosConfigFile = findMacosConfigFile();
        } else {
            strFindMacosConfigFile = "/etc/krb5.conf";
        }
        if (DEBUG) {
            System.out.println("Native config name: " + strFindMacosConfigFile);
        }
        return strFindMacosConfigFile;
    }

    private static String getProperty(String str) {
        return (String) AccessController.doPrivileged(new GetPropertyAction(str));
    }

    private String findMacosConfigFile() {
        String str = getProperty("user.home") + "/Library/Preferences/edu.mit.Kerberos";
        if (fileExists(str)) {
            return str;
        }
        if (fileExists("/Library/Preferences/edu.mit.Kerberos")) {
            return "/Library/Preferences/edu.mit.Kerberos";
        }
        return "/etc/krb5.conf";
    }

    private static String trimmed(String str) {
        String strTrim = str.trim();
        if (strTrim.length() >= 2 && ((strTrim.charAt(0) == '\"' && strTrim.charAt(strTrim.length() - 1) == '\"') || (strTrim.charAt(0) == '\'' && strTrim.charAt(strTrim.length() - 1) == '\''))) {
            strTrim = strTrim.substring(1, strTrim.length() - 1).trim();
        }
        return strTrim;
    }

    public void listTable() {
        System.out.println(this);
    }

    public int[] defaultEtype(String str) throws KrbException {
        int[] builtInDefaults;
        String str2 = get("libdefaults", str);
        if (str2 == null) {
            if (DEBUG) {
                System.out.println("Using builtin default etypes for " + str);
            }
            builtInDefaults = EType.getBuiltInDefaults();
        } else {
            String str3 = " ";
            int i2 = 0;
            while (true) {
                if (i2 >= str2.length()) {
                    break;
                }
                if (!str2.substring(i2, i2 + 1).equals(",")) {
                    i2++;
                } else {
                    str3 = ",";
                    break;
                }
            }
            StringTokenizer stringTokenizer = new StringTokenizer(str2, str3);
            int iCountTokens = stringTokenizer.countTokens();
            ArrayList arrayList = new ArrayList(iCountTokens);
            for (int i3 = 0; i3 < iCountTokens; i3++) {
                int type = getType(stringTokenizer.nextToken());
                if (type != -1 && EType.isSupported(type)) {
                    arrayList.add(Integer.valueOf(type));
                }
            }
            if (arrayList.isEmpty()) {
                throw new KrbException("no supported default etypes for " + str);
            }
            builtInDefaults = new int[arrayList.size()];
            for (int i4 = 0; i4 < builtInDefaults.length; i4++) {
                builtInDefaults[i4] = ((Integer) arrayList.get(i4)).intValue();
            }
        }
        if (DEBUG) {
            System.out.print("default etypes for " + str + CallSiteDescriptor.TOKEN_DELIMITER);
            for (int i5 : builtInDefaults) {
                System.out.print(" " + i5);
            }
            System.out.println(".");
        }
        return builtInDefaults;
    }

    public static int getType(String str) {
        int i2 = -1;
        if (str == null) {
            return -1;
        }
        if (str.startsWith(PdfOps.d_TOKEN) || str.startsWith(PdfOps.D_TOKEN)) {
            if (str.equalsIgnoreCase("des-cbc-crc")) {
                i2 = 1;
            } else if (str.equalsIgnoreCase("des-cbc-md5")) {
                i2 = 3;
            } else if (str.equalsIgnoreCase("des-mac")) {
                i2 = 4;
            } else if (str.equalsIgnoreCase("des-mac-k")) {
                i2 = 5;
            } else if (str.equalsIgnoreCase("des-cbc-md4")) {
                i2 = 2;
            } else if (str.equalsIgnoreCase("des3-cbc-sha1") || str.equalsIgnoreCase("des3-hmac-sha1") || str.equalsIgnoreCase("des3-cbc-sha1-kd") || str.equalsIgnoreCase("des3-cbc-hmac-sha1-kd")) {
                i2 = 16;
            }
        } else if (str.startsWith("a") || str.startsWith("A")) {
            if (str.equalsIgnoreCase("aes128-cts") || str.equalsIgnoreCase("aes128-cts-hmac-sha1-96")) {
                i2 = 17;
            } else if (str.equalsIgnoreCase("aes256-cts") || str.equalsIgnoreCase("aes256-cts-hmac-sha1-96")) {
                i2 = 18;
            } else if (str.equalsIgnoreCase("arcfour-hmac") || str.equalsIgnoreCase("arcfour-hmac-md5")) {
                i2 = 23;
            }
        } else if (str.equalsIgnoreCase("rc4-hmac")) {
            i2 = 23;
        } else if (str.equalsIgnoreCase("CRC32")) {
            i2 = 1;
        } else if (str.startsWith(InternalZipConstants.READ_MODE) || str.startsWith("R")) {
            if (str.equalsIgnoreCase("rsa-md5")) {
                i2 = 7;
            } else if (str.equalsIgnoreCase("rsa-md5-des")) {
                i2 = 8;
            }
        } else if (str.equalsIgnoreCase("hmac-sha1-des3-kd")) {
            i2 = 12;
        } else if (str.equalsIgnoreCase("hmac-sha1-96-aes128")) {
            i2 = 15;
        } else if (str.equalsIgnoreCase("hmac-sha1-96-aes256")) {
            i2 = 16;
        } else if (str.equalsIgnoreCase("hmac-md5-rc4") || str.equalsIgnoreCase("hmac-md5-arcfour") || str.equalsIgnoreCase("hmac-md5-enc")) {
            i2 = -138;
        } else if (str.equalsIgnoreCase("NULL")) {
            i2 = 0;
        }
        return i2;
    }

    public void resetDefaultRealm(String str) {
        if (DEBUG) {
            System.out.println(">>> Config try resetting default kdc " + str);
        }
    }

    public boolean useAddresses() {
        String str = get("libdefaults", "no_addresses");
        boolean z2 = str != null && str.equalsIgnoreCase("false");
        if (!z2) {
            String str2 = get("libdefaults", "noaddresses");
            z2 = str2 != null && str2.equalsIgnoreCase("false");
        }
        return z2;
    }

    private boolean useDNS(String str, boolean z2) {
        Boolean booleanObject = getBooleanObject("libdefaults", str);
        if (booleanObject != null) {
            return booleanObject.booleanValue();
        }
        Boolean booleanObject2 = getBooleanObject("libdefaults", "dns_fallback");
        if (booleanObject2 != null) {
            return booleanObject2.booleanValue();
        }
        return z2;
    }

    private boolean useDNS_KDC() {
        return useDNS("dns_lookup_kdc", true);
    }

    private boolean useDNS_Realm() {
        return useDNS("dns_lookup_realm", false);
    }

    public String getDefaultRealm() throws KrbException {
        if (this.defaultRealm != null) {
            return this.defaultRealm;
        }
        KrbException krbException = null;
        String realmFromDNS = get("libdefaults", "default_realm");
        if (realmFromDNS == null && useDNS_Realm()) {
            try {
                realmFromDNS = getRealmFromDNS();
            } catch (KrbException e2) {
                krbException = e2;
            }
        }
        if (realmFromDNS == null) {
            realmFromDNS = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.krb5.Config.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public String run2() {
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        return System.getenv("USERDNSDOMAIN");
                    }
                    return null;
                }
            });
        }
        if (realmFromDNS == null) {
            KrbException krbException2 = new KrbException("Cannot locate default realm");
            if (krbException != null) {
                krbException2.initCause(krbException);
            }
            throw krbException2;
        }
        return realmFromDNS;
    }

    public String getKDCList(String str) throws KrbException {
        if (str == null) {
            str = getDefaultRealm();
        }
        if (str.equalsIgnoreCase(this.defaultRealm)) {
            return this.defaultKDC;
        }
        KrbException krbException = null;
        String all = getAll("realms", str, "kdc");
        if (all == null && useDNS_KDC()) {
            try {
                all = getKDCFromDNS(str);
            } catch (KrbException e2) {
                krbException = e2;
            }
        }
        if (all == null) {
            all = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.security.krb5.Config.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public String run2() {
                    if (System.getProperty("os.name").startsWith("Windows")) {
                        String strSubstring = System.getenv("LOGONSERVER");
                        if (strSubstring != null && strSubstring.startsWith("\\\\")) {
                            strSubstring = strSubstring.substring(2);
                        }
                        return strSubstring;
                    }
                    return null;
                }
            });
        }
        if (all == null) {
            if (this.defaultKDC != null) {
                return this.defaultKDC;
            }
            KrbException krbException2 = new KrbException("Cannot locate KDC");
            if (krbException != null) {
                krbException2.initCause(krbException);
            }
            throw krbException2;
        }
        return all;
    }

    private String getRealmFromDNS() throws KrbException {
        String strCheckRealm = null;
        try {
            String strMapHostToRealm = PrincipalName.mapHostToRealm(InetAddress.getLocalHost().getCanonicalHostName());
            if (strMapHostToRealm == null) {
                Iterator<String> it = ResolverConfiguration.open().searchlist().iterator();
                while (it.hasNext()) {
                    strCheckRealm = checkRealm(it.next());
                    if (strCheckRealm != null) {
                        break;
                    }
                }
            } else {
                strCheckRealm = checkRealm(strMapHostToRealm);
            }
            if (strCheckRealm == null) {
                throw new KrbException(60, "Unable to locate Kerberos realm");
            }
            return strCheckRealm;
        } catch (UnknownHostException e2) {
            KrbException krbException = new KrbException(60, "Unable to locate Kerberos realm: " + e2.getMessage());
            krbException.initCause(e2);
            throw krbException;
        }
    }

    private static String checkRealm(String str) {
        if (DEBUG) {
            System.out.println("getRealmFromDNS: trying " + str);
        }
        String[] kerberosService = null;
        String realmComponent = str;
        while (true) {
            String str2 = realmComponent;
            if (kerberosService != null || str2 == null) {
                break;
            }
            kerberosService = KrbServiceLocator.getKerberosService(str2);
            realmComponent = Realm.parseRealmComponent(str2);
        }
        if (kerberosService != null) {
            for (int i2 = 0; i2 < kerberosService.length; i2++) {
                if (kerberosService[i2].equalsIgnoreCase(str)) {
                    return kerberosService[i2];
                }
            }
            return null;
        }
        return null;
    }

    private String getKDCFromDNS(String str) throws KrbException {
        String str2 = "";
        if (DEBUG) {
            System.out.println("getKDCFromDNS using UDP");
        }
        String[] kerberosService = KrbServiceLocator.getKerberosService(str, "_udp");
        if (kerberosService == null) {
            if (DEBUG) {
                System.out.println("getKDCFromDNS using TCP");
            }
            kerberosService = KrbServiceLocator.getKerberosService(str, "_tcp");
        }
        if (kerberosService == null) {
            throw new KrbException(60, "Unable to locate KDC for realm " + str);
        }
        if (kerberosService.length == 0) {
            return null;
        }
        for (String str3 : kerberosService) {
            str2 = str2 + str3.trim() + " ";
        }
        String strTrim = str2.trim();
        if (strTrim.equals("")) {
            return null;
        }
        return strTrim;
    }

    private boolean fileExists(String str) {
        return ((Boolean) AccessController.doPrivileged(new FileExistsAction(str))).booleanValue();
    }

    /* loaded from: rt.jar:sun/security/krb5/Config$FileExistsAction.class */
    static class FileExistsAction implements PrivilegedAction<Boolean> {
        private String fileName;

        public FileExistsAction(String str) {
            this.fileName = str;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Boolean run2() {
            return Boolean.valueOf(new File(this.fileName).exists());
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        toStringInternal("", this.stanzaTable, stringBuffer);
        return stringBuffer.toString();
    }

    private static void toStringInternal(String str, Object obj, StringBuffer stringBuffer) {
        if (obj instanceof String) {
            stringBuffer.append(obj).append('\n');
            return;
        }
        if (obj instanceof Hashtable) {
            Hashtable hashtable = (Hashtable) obj;
            stringBuffer.append("{\n");
            for (Object obj2 : hashtable.keySet()) {
                stringBuffer.append(str).append("    ").append(obj2).append(" = ");
                toStringInternal(str + "    ", hashtable.get(obj2), stringBuffer);
            }
            stringBuffer.append(str).append("}\n");
            return;
        }
        if (obj instanceof Vector) {
            stringBuffer.append("[");
            boolean z2 = true;
            for (Object obj3 : ((Vector) obj).toArray()) {
                if (!z2) {
                    stringBuffer.append(",");
                }
                stringBuffer.append(obj3);
                z2 = false;
            }
            stringBuffer.append("]\n");
        }
    }
}
