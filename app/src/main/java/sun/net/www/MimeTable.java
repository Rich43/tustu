package sun.net.www;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.FileNameMap;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

/* loaded from: rt.jar:sun/net/www/MimeTable.class */
public class MimeTable implements FileNameMap {
    private Hashtable<String, MimeEntry> entries = new Hashtable<>();
    private Hashtable<String, MimeEntry> extensionMap = new Hashtable<>();
    private static String tempFileTemplate;
    private static final String filePreamble = "sun.net.www MIME content-types table";
    private static final String fileMagic = "#sun.net.www MIME content-types table";
    protected static String[] mailcapLocations;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.www.MimeTable.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                String unused = MimeTable.tempFileTemplate = System.getProperty("content.types.temp.file.template", "/tmp/%s");
                MimeTable.mailcapLocations = new String[]{System.getProperty("user.mailcap"), System.getProperty("user.home") + "/.mailcap", "/etc/mailcap", "/usr/etc/mailcap", "/usr/local/etc/mailcap", System.getProperty("hotjava.home", "/usr/local/hotjava") + "/lib/mailcap"};
                return null;
            }
        });
    }

    MimeTable() {
        load();
    }

    /* loaded from: rt.jar:sun/net/www/MimeTable$DefaultInstanceHolder.class */
    private static class DefaultInstanceHolder {
        static final MimeTable defaultInstance = getDefaultInstance();

        private DefaultInstanceHolder() {
        }

        static MimeTable getDefaultInstance() {
            return (MimeTable) AccessController.doPrivileged(new PrivilegedAction<MimeTable>() { // from class: sun.net.www.MimeTable.DefaultInstanceHolder.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public MimeTable run2() {
                    MimeTable mimeTable = new MimeTable();
                    URLConnection.setFileNameMap(mimeTable);
                    return mimeTable;
                }
            });
        }
    }

    public static MimeTable getDefaultTable() {
        return DefaultInstanceHolder.defaultInstance;
    }

    public static FileNameMap loadTable() {
        return getDefaultTable();
    }

    public synchronized int getSize() {
        return this.entries.size();
    }

    @Override // java.net.FileNameMap
    public synchronized String getContentTypeFor(String str) {
        MimeEntry mimeEntryFindByFileName = findByFileName(str);
        if (mimeEntryFindByFileName != null) {
            return mimeEntryFindByFileName.getType();
        }
        return null;
    }

    public synchronized void add(MimeEntry mimeEntry) {
        this.entries.put(mimeEntry.getType(), mimeEntry);
        String[] extensions = mimeEntry.getExtensions();
        if (extensions == null) {
            return;
        }
        for (String str : extensions) {
            this.extensionMap.put(str, mimeEntry);
        }
    }

    public synchronized MimeEntry remove(String str) {
        return remove(this.entries.get(str));
    }

    public synchronized MimeEntry remove(MimeEntry mimeEntry) {
        String[] extensions = mimeEntry.getExtensions();
        if (extensions != null) {
            for (String str : extensions) {
                this.extensionMap.remove(str);
            }
        }
        return this.entries.remove(mimeEntry.getType());
    }

    public synchronized MimeEntry find(String str) {
        MimeEntry mimeEntry = this.entries.get(str);
        if (mimeEntry == null) {
            Enumeration<MimeEntry> enumerationElements = this.entries.elements();
            while (enumerationElements.hasMoreElements()) {
                MimeEntry mimeEntryNextElement = enumerationElements.nextElement2();
                if (mimeEntryNextElement.matches(str)) {
                    return mimeEntryNextElement;
                }
            }
        }
        return mimeEntry;
    }

    public MimeEntry findByFileName(String str) {
        String lowerCase = "";
        int iLastIndexOf = str.lastIndexOf(35);
        if (iLastIndexOf > 0) {
            str = str.substring(0, iLastIndexOf - 1);
        }
        int iMax = Math.max(Math.max(str.lastIndexOf(46), str.lastIndexOf(47)), str.lastIndexOf(63));
        if (iMax != -1 && str.charAt(iMax) == '.') {
            lowerCase = str.substring(iMax).toLowerCase();
        }
        return findByExt(lowerCase);
    }

    public synchronized MimeEntry findByExt(String str) {
        return this.extensionMap.get(str);
    }

    public synchronized MimeEntry findByDescription(String str) {
        Enumeration<MimeEntry> enumerationElements = elements();
        while (enumerationElements.hasMoreElements()) {
            MimeEntry mimeEntryNextElement = enumerationElements.nextElement2();
            if (str.equals(mimeEntryNextElement.getDescription())) {
                return mimeEntryNextElement;
            }
        }
        return find(str);
    }

    String getTempFileTemplate() {
        return tempFileTemplate;
    }

    public synchronized Enumeration<MimeEntry> elements() {
        return this.entries.elements();
    }

    public synchronized void load() {
        Properties properties = new Properties();
        File file = null;
        try {
            String property = System.getProperty("content.types.user.table");
            if (property != null) {
                file = new File(property);
                if (!file.exists()) {
                    file = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "content-types.properties");
                }
            } else {
                file = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "content-types.properties");
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            properties.load(bufferedInputStream);
            bufferedInputStream.close();
            parse(properties);
        } catch (IOException e2) {
            System.err.println("Warning: default mime table not found: " + file.getPath());
        }
    }

    void parse(Properties properties) {
        String str = (String) properties.get("temp.file.template");
        if (str != null) {
            properties.remove("temp.file.template");
            tempFileTemplate = str;
        }
        Enumeration<?> enumerationPropertyNames = properties.propertyNames();
        while (enumerationPropertyNames.hasMoreElements()) {
            String str2 = (String) enumerationPropertyNames.nextElement2();
            parse(str2, properties.getProperty(str2));
        }
    }

    void parse(String str, String str2) {
        MimeEntry mimeEntry = new MimeEntry(str);
        StringTokenizer stringTokenizer = new StringTokenizer(str2, ";");
        while (stringTokenizer.hasMoreTokens()) {
            parse(stringTokenizer.nextToken(), mimeEntry);
        }
        add(mimeEntry);
    }

    void parse(String str, MimeEntry mimeEntry) {
        String strTrim = null;
        String strTrim2 = null;
        boolean z2 = false;
        StringTokenizer stringTokenizer = new StringTokenizer(str, "=");
        while (stringTokenizer.hasMoreTokens()) {
            if (z2) {
                strTrim2 = stringTokenizer.nextToken().trim();
            } else {
                strTrim = stringTokenizer.nextToken().trim();
                z2 = true;
            }
        }
        fill(mimeEntry, strTrim, strTrim2);
    }

    void fill(MimeEntry mimeEntry, String str, String str2) {
        if ("description".equalsIgnoreCase(str)) {
            mimeEntry.setDescription(str2);
            return;
        }
        if ("action".equalsIgnoreCase(str)) {
            mimeEntry.setAction(getActionCode(str2));
            return;
        }
        if ("application".equalsIgnoreCase(str)) {
            mimeEntry.setCommand(str2);
        } else if ("icon".equalsIgnoreCase(str)) {
            mimeEntry.setImageFileName(str2);
        } else if ("file_extensions".equalsIgnoreCase(str)) {
            mimeEntry.setExtensions(str2);
        }
    }

    String[] getExtensions(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        int iCountTokens = stringTokenizer.countTokens();
        String[] strArr = new String[iCountTokens];
        for (int i2 = 0; i2 < iCountTokens; i2++) {
            strArr[i2] = stringTokenizer.nextToken();
        }
        return strArr;
    }

    int getActionCode(String str) {
        for (int i2 = 0; i2 < MimeEntry.actionKeywords.length; i2++) {
            if (str.equalsIgnoreCase(MimeEntry.actionKeywords[i2])) {
                return i2;
            }
        }
        return 0;
    }

    public synchronized boolean save(String str) {
        if (str == null) {
            str = System.getProperty("user.home" + File.separator + "lib" + File.separator + "content-types.properties");
        }
        return saveAsProperties(new File(str));
    }

    public Properties getAsProperties() {
        Properties properties = new Properties();
        Enumeration<MimeEntry> enumerationElements = elements();
        while (enumerationElements.hasMoreElements()) {
            MimeEntry mimeEntryNextElement = enumerationElements.nextElement2();
            properties.put(mimeEntryNextElement.getType(), mimeEntryNextElement.toProperty());
        }
        return properties;
    }

    protected boolean saveAsProperties(File file) {
        AutoCloseable autoCloseable = null;
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Properties asProperties = getAsProperties();
                asProperties.put("temp.file.template", tempFileTemplate);
                String property = System.getProperty("user.name");
                if (property != null) {
                    asProperties.store(fileOutputStream, filePreamble + ("; customized for " + property));
                } else {
                    asProperties.store(fileOutputStream, filePreamble);
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        return true;
                    } catch (IOException e2) {
                        return true;
                    }
                }
                return true;
            } catch (IOException e3) {
                e3.printStackTrace();
                if (0 != 0) {
                    try {
                        autoCloseable.close();
                    } catch (IOException e4) {
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    autoCloseable.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
    }
}
