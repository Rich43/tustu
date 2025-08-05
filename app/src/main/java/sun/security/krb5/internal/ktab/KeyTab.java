package sun.security.krb5.internal.ktab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import sun.security.action.GetPropertyAction;
import sun.security.krb5.Config;
import sun.security.krb5.EncryptionKey;
import sun.security.krb5.KrbException;
import sun.security.krb5.PrincipalName;
import sun.security.krb5.RealmException;
import sun.security.krb5.internal.KerberosTime;
import sun.security.krb5.internal.Krb5;
import sun.security.krb5.internal.crypto.EType;

/* loaded from: rt.jar:sun/security/krb5/internal/ktab/KeyTab.class */
public class KeyTab implements KeyTabConstants {
    private static final boolean DEBUG = Krb5.DEBUG;
    private static String defaultTabName = null;
    private static Map<String, KeyTab> map = new HashMap();
    private boolean isMissing;
    private boolean isValid;
    private final String tabName;
    private long lastModified;
    private int kt_vno = 1282;
    private Vector<KeyTabEntry> entries = new Vector<>();

    private KeyTab(String str) {
        this.isMissing = false;
        this.isValid = true;
        this.tabName = str;
        try {
            this.lastModified = new File(this.tabName).lastModified();
            KeyTabInputStream keyTabInputStream = new KeyTabInputStream(new FileInputStream(str));
            Throwable th = null;
            try {
                try {
                    load(keyTabInputStream);
                    if (keyTabInputStream != null) {
                        if (0 != 0) {
                            try {
                                keyTabInputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            keyTabInputStream.close();
                        }
                    }
                } catch (Throwable th3) {
                    if (keyTabInputStream != null) {
                        if (th != null) {
                            try {
                                keyTabInputStream.close();
                            } catch (Throwable th4) {
                                th.addSuppressed(th4);
                            }
                        } else {
                            keyTabInputStream.close();
                        }
                    }
                    throw th3;
                }
            } catch (Throwable th5) {
                th = th5;
                throw th5;
            }
        } catch (FileNotFoundException e2) {
            this.entries.clear();
            this.isMissing = true;
        } catch (Exception e3) {
            this.entries.clear();
            this.isValid = false;
        }
    }

    private static synchronized KeyTab getInstance0(String str) {
        long jLastModified = new File(str).lastModified();
        KeyTab keyTab = map.get(str);
        if (keyTab != null && keyTab.isValid() && keyTab.lastModified == jLastModified) {
            return keyTab;
        }
        KeyTab keyTab2 = new KeyTab(str);
        if (keyTab2.isValid()) {
            map.put(str, keyTab2);
            return keyTab2;
        }
        if (keyTab != null) {
            return keyTab;
        }
        return keyTab2;
    }

    public static KeyTab getInstance(String str) {
        if (str == null) {
            return getInstance();
        }
        return getInstance0(normalize(str));
    }

    public static KeyTab getInstance(File file) {
        if (file == null) {
            return getInstance();
        }
        return getInstance0(file.getPath());
    }

    public static KeyTab getInstance() {
        return getInstance(getDefaultTabName());
    }

    public boolean isMissing() {
        return this.isMissing;
    }

    public boolean isValid() {
        return this.isValid;
    }

    private static String getDefaultTabName() {
        if (defaultTabName != null) {
            return defaultTabName;
        }
        String strNormalize = null;
        try {
            String str = Config.getInstance().get("libdefaults", "default_keytab_name");
            if (str != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(str, " ");
                while (stringTokenizer.hasMoreTokens()) {
                    strNormalize = normalize(stringTokenizer.nextToken());
                    if (new File(strNormalize).exists()) {
                        break;
                    }
                }
            }
        } catch (KrbException e2) {
            strNormalize = null;
        }
        if (strNormalize == null) {
            String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("user.home"));
            if (str2 == null) {
                str2 = (String) AccessController.doPrivileged(new GetPropertyAction("user.dir"));
            }
            strNormalize = str2 + File.separator + "krb5.keytab";
        }
        defaultTabName = strNormalize;
        return strNormalize;
    }

    public static String normalize(String str) {
        String strSubstring;
        if (str.length() >= 5 && str.substring(0, 5).equalsIgnoreCase("FILE:")) {
            strSubstring = str.substring(5);
        } else if (str.length() >= 9 && str.substring(0, 9).equalsIgnoreCase("ANY:FILE:")) {
            strSubstring = str.substring(9);
        } else if (str.length() >= 7 && str.substring(0, 7).equalsIgnoreCase("SRVTAB:")) {
            strSubstring = str.substring(7);
        } else {
            strSubstring = str;
        }
        return strSubstring;
    }

    private void load(KeyTabInputStream keyTabInputStream) throws IOException, RealmException {
        this.entries.clear();
        this.kt_vno = keyTabInputStream.readVersion();
        if (this.kt_vno == 1281) {
            keyTabInputStream.setNativeByteOrder();
        }
        while (keyTabInputStream.available() > 0) {
            int entryLength = keyTabInputStream.readEntryLength();
            KeyTabEntry entry = keyTabInputStream.readEntry(entryLength, this.kt_vno);
            if (DEBUG) {
                System.out.println(">>> KeyTab: load() entry length: " + entryLength + "; type: " + (entry != null ? entry.keyType : 0));
            }
            if (entry != null) {
                this.entries.addElement(entry);
            }
        }
    }

    public PrincipalName getOneName() {
        int size = this.entries.size();
        if (size > 0) {
            return this.entries.elementAt(size - 1).service;
        }
        return null;
    }

    public EncryptionKey[] readServiceKeys(PrincipalName principalName) {
        int size = this.entries.size();
        ArrayList arrayList = new ArrayList(size);
        if (DEBUG) {
            System.out.println("Looking for keys for: " + ((Object) principalName));
        }
        for (int i2 = size - 1; i2 >= 0; i2--) {
            KeyTabEntry keyTabEntryElementAt = this.entries.elementAt(i2);
            if (keyTabEntryElementAt.service.match(principalName)) {
                if (EType.isSupported(keyTabEntryElementAt.keyType)) {
                    arrayList.add(new EncryptionKey(keyTabEntryElementAt.keyblock, keyTabEntryElementAt.keyType, new Integer(keyTabEntryElementAt.keyVersion)));
                    if (DEBUG) {
                        System.out.println("Added key: " + keyTabEntryElementAt.keyType + "version: " + keyTabEntryElementAt.keyVersion);
                    }
                } else if (DEBUG) {
                    System.out.println("Found unsupported keytype (" + keyTabEntryElementAt.keyType + ") for " + ((Object) principalName));
                }
            }
        }
        EncryptionKey[] encryptionKeyArr = (EncryptionKey[]) arrayList.toArray(new EncryptionKey[arrayList.size()]);
        Arrays.sort(encryptionKeyArr, new Comparator<EncryptionKey>() { // from class: sun.security.krb5.internal.ktab.KeyTab.1
            @Override // java.util.Comparator
            public int compare(EncryptionKey encryptionKey, EncryptionKey encryptionKey2) {
                return encryptionKey2.getKeyVersionNumber().intValue() - encryptionKey.getKeyVersionNumber().intValue();
            }
        });
        return encryptionKeyArr;
    }

    public boolean findServiceEntry(PrincipalName principalName) {
        for (int i2 = 0; i2 < this.entries.size(); i2++) {
            KeyTabEntry keyTabEntryElementAt = this.entries.elementAt(i2);
            if (keyTabEntryElementAt.service.match(principalName)) {
                if (EType.isSupported(keyTabEntryElementAt.keyType)) {
                    return true;
                }
                if (DEBUG) {
                    System.out.println("Found unsupported keytype (" + keyTabEntryElementAt.keyType + ") for " + ((Object) principalName));
                }
            }
        }
        return false;
    }

    public String tabName() {
        return this.tabName;
    }

    public void addEntry(PrincipalName principalName, char[] cArr, int i2, boolean z2) throws KrbException {
        addEntry(principalName, principalName.getSalt(), cArr, i2, z2);
    }

    public void addEntry(PrincipalName principalName, String str, char[] cArr, int i2, boolean z2) throws KrbException {
        EncryptionKey[] encryptionKeyArrAcquireSecretKeys = EncryptionKey.acquireSecretKeys(cArr, str);
        int i3 = 0;
        for (int size = this.entries.size() - 1; size >= 0; size--) {
            KeyTabEntry keyTabEntry = this.entries.get(size);
            if (keyTabEntry.service.match(principalName)) {
                if (keyTabEntry.keyVersion > i3) {
                    i3 = keyTabEntry.keyVersion;
                }
                if (!z2 || keyTabEntry.keyVersion == i2) {
                    this.entries.removeElementAt(size);
                }
            }
        }
        if (i2 == -1) {
            i2 = i3 + 1;
        }
        for (int i4 = 0; encryptionKeyArrAcquireSecretKeys != null && i4 < encryptionKeyArrAcquireSecretKeys.length; i4++) {
            this.entries.addElement(new KeyTabEntry(principalName, principalName.getRealm(), new KerberosTime(System.currentTimeMillis()), i2, encryptionKeyArrAcquireSecretKeys[i4].getEType(), encryptionKeyArrAcquireSecretKeys[i4].getBytes()));
        }
    }

    public KeyTabEntry[] getEntries() {
        KeyTabEntry[] keyTabEntryArr = new KeyTabEntry[this.entries.size()];
        for (int i2 = 0; i2 < keyTabEntryArr.length; i2++) {
            keyTabEntryArr[i2] = this.entries.elementAt(i2);
        }
        return keyTabEntryArr;
    }

    public static synchronized KeyTab create() throws IOException, RealmException {
        return create(getDefaultTabName());
    }

    public static synchronized KeyTab create(String str) throws IOException, RealmException {
        KeyTabOutputStream keyTabOutputStream = new KeyTabOutputStream(new FileOutputStream(str));
        Throwable th = null;
        try {
            keyTabOutputStream.writeVersion(1282);
            if (keyTabOutputStream != null) {
                if (0 != 0) {
                    try {
                        keyTabOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    keyTabOutputStream.close();
                }
            }
            return new KeyTab(str);
        } catch (Throwable th3) {
            if (keyTabOutputStream != null) {
                if (0 != 0) {
                    try {
                        keyTabOutputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    keyTabOutputStream.close();
                }
            }
            throw th3;
        }
    }

    public synchronized void save() throws IOException {
        KeyTabOutputStream keyTabOutputStream = new KeyTabOutputStream(new FileOutputStream(this.tabName));
        Throwable th = null;
        try {
            keyTabOutputStream.writeVersion(this.kt_vno);
            for (int i2 = 0; i2 < this.entries.size(); i2++) {
                keyTabOutputStream.writeEntry(this.entries.elementAt(i2));
            }
            if (keyTabOutputStream != null) {
                if (0 != 0) {
                    try {
                        keyTabOutputStream.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                keyTabOutputStream.close();
            }
        } catch (Throwable th3) {
            if (keyTabOutputStream != null) {
                if (0 != 0) {
                    try {
                        keyTabOutputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    keyTabOutputStream.close();
                }
            }
            throw th3;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int deleteEntries(PrincipalName principalName, int i2, int i3) {
        int i4 = 0;
        HashMap map2 = new HashMap();
        for (int size = this.entries.size() - 1; size >= 0; size--) {
            KeyTabEntry keyTabEntry = this.entries.get(size);
            if (principalName.match(keyTabEntry.getService()) && (i2 == -1 || keyTabEntry.keyType == i2)) {
                if (i3 == -2) {
                    if (map2.containsKey(Integer.valueOf(keyTabEntry.keyType))) {
                        if (keyTabEntry.keyVersion > ((Integer) map2.get(Integer.valueOf(keyTabEntry.keyType))).intValue()) {
                            map2.put(Integer.valueOf(keyTabEntry.keyType), Integer.valueOf(keyTabEntry.keyVersion));
                        }
                    } else {
                        map2.put(Integer.valueOf(keyTabEntry.keyType), Integer.valueOf(keyTabEntry.keyVersion));
                    }
                } else if (i3 == -1 || keyTabEntry.keyVersion == i3) {
                    this.entries.removeElementAt(size);
                    i4++;
                }
            }
        }
        if (i3 == -2) {
            for (int size2 = this.entries.size() - 1; size2 >= 0; size2--) {
                KeyTabEntry keyTabEntry2 = this.entries.get(size2);
                if (principalName.match(keyTabEntry2.getService()) && (i2 == -1 || keyTabEntry2.keyType == i2)) {
                    if (keyTabEntry2.keyVersion != ((Integer) map2.get(Integer.valueOf(keyTabEntry2.keyType))).intValue()) {
                        this.entries.removeElementAt(size2);
                        i4++;
                    }
                }
            }
        }
        return i4;
    }

    public synchronized void createVersion(File file) throws IOException {
        KeyTabOutputStream keyTabOutputStream = new KeyTabOutputStream(new FileOutputStream(file));
        Throwable th = null;
        try {
            try {
                keyTabOutputStream.write16(1282);
                if (keyTabOutputStream != null) {
                    if (0 != 0) {
                        try {
                            keyTabOutputStream.close();
                            return;
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                            return;
                        }
                    }
                    keyTabOutputStream.close();
                }
            } catch (Throwable th3) {
                th = th3;
                throw th3;
            }
        } catch (Throwable th4) {
            if (keyTabOutputStream != null) {
                if (th != null) {
                    try {
                        keyTabOutputStream.close();
                    } catch (Throwable th5) {
                        th.addSuppressed(th5);
                    }
                } else {
                    keyTabOutputStream.close();
                }
            }
            throw th4;
        }
    }
}
