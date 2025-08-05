package java.security;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import sun.security.jca.GetInstance;
import sun.security.jca.ProviderList;
import sun.security.jca.Providers;
import sun.security.util.Debug;
import sun.security.util.PropertyExpander;

/* loaded from: rt.jar:java/security/Security.class */
public final class Security {
    private static final Debug sdebug = Debug.getInstance("properties");
    private static Properties props;
    private static final Map<String, Class<?>> spiMap;

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.security.Security.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                Security.initialize();
                return null;
            }
        });
        spiMap = new ConcurrentHashMap();
    }

    /* loaded from: rt.jar:java/security/Security$ProviderProperty.class */
    private static class ProviderProperty {
        String className;
        Provider provider;

        private ProviderProperty() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void initialize() {
        props = new Properties();
        boolean z2 = false;
        boolean z3 = false;
        File fileSecurityPropFile = securityPropFile("java.security");
        if (fileSecurityPropFile.exists()) {
            BufferedInputStream bufferedInputStream = null;
            try {
                try {
                    bufferedInputStream = new BufferedInputStream(new FileInputStream(fileSecurityPropFile));
                    props.load(bufferedInputStream);
                    z2 = true;
                    if (sdebug != null) {
                        sdebug.println("reading security properties file: " + ((Object) fileSecurityPropFile));
                    }
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e2) {
                            if (sdebug != null) {
                                sdebug.println("unable to close input stream");
                            }
                        }
                    }
                } catch (Throwable th) {
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (IOException e3) {
                            if (sdebug != null) {
                                sdebug.println("unable to close input stream");
                            }
                        }
                    }
                    throw th;
                }
            } catch (IOException e4) {
                if (sdebug != null) {
                    sdebug.println("unable to load security properties from " + ((Object) fileSecurityPropFile));
                    e4.printStackTrace();
                }
                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e5) {
                        if (sdebug != null) {
                            sdebug.println("unable to close input stream");
                        }
                    }
                }
            }
        }
        if ("true".equalsIgnoreCase(props.getProperty("security.overridePropertiesFile"))) {
            String property = System.getProperty("java.security.properties");
            if (property != null && property.startsWith("=")) {
                z3 = true;
                property = property.substring(1);
            }
            if (z3) {
                props = new Properties();
                if (sdebug != null) {
                    sdebug.println("overriding other security properties files!");
                }
            }
            if (property != null) {
                BufferedInputStream bufferedInputStream2 = null;
                try {
                    try {
                        property = PropertyExpander.expand(property);
                        File file = new File(property);
                        URL url = file.exists() ? new URL("file:" + file.getCanonicalPath()) : new URL(property);
                        bufferedInputStream2 = new BufferedInputStream(url.openStream());
                        props.load(bufferedInputStream2);
                        z2 = true;
                        if (sdebug != null) {
                            sdebug.println("reading security properties file: " + ((Object) url));
                            if (z3) {
                                sdebug.println("overriding other security properties files!");
                            }
                        }
                        if (bufferedInputStream2 != null) {
                            try {
                                bufferedInputStream2.close();
                            } catch (IOException e6) {
                                if (sdebug != null) {
                                    sdebug.println("unable to close input stream");
                                }
                            }
                        }
                    } catch (Throwable th2) {
                        if (bufferedInputStream2 != null) {
                            try {
                                bufferedInputStream2.close();
                            } catch (IOException e7) {
                                if (sdebug != null) {
                                    sdebug.println("unable to close input stream");
                                }
                            }
                        }
                        throw th2;
                    }
                } catch (Exception e8) {
                    if (sdebug != null) {
                        sdebug.println("unable to load security properties from " + property);
                        e8.printStackTrace();
                    }
                    if (bufferedInputStream2 != null) {
                        try {
                            bufferedInputStream2.close();
                        } catch (IOException e9) {
                            if (sdebug != null) {
                                sdebug.println("unable to close input stream");
                            }
                        }
                    }
                }
            }
        }
        if (z2) {
            return;
        }
        initializeStatic();
        if (sdebug != null) {
            sdebug.println("unable to load security properties -- using defaults");
        }
    }

    private static void initializeStatic() {
        props.put("security.provider.1", "sun.security.provider.Sun");
        props.put("security.provider.2", "sun.security.rsa.SunRsaSign");
        props.put("security.provider.3", "com.sun.net.ssl.internal.ssl.Provider");
        props.put("security.provider.4", "com.sun.crypto.provider.SunJCE");
        props.put("security.provider.5", "sun.security.jgss.SunProvider");
        props.put("security.provider.6", "com.sun.security.sasl.Provider");
    }

    private Security() {
    }

    private static File securityPropFile(String str) {
        String str2 = File.separator;
        return new File(System.getProperty("java.home") + str2 + "lib" + str2 + "security" + str2 + str);
    }

    private static ProviderProperty getProviderProperty(String str) {
        List<Provider> listProviders = Providers.getProviderList().providers();
        for (int i2 = 0; i2 < listProviders.size(); i2++) {
            Provider provider = listProviders.get(i2);
            String property = provider.getProperty(str);
            if (property == null) {
                Enumeration<Object> enumerationKeys = provider.keys();
                while (true) {
                    if (!enumerationKeys.hasMoreElements() || property != null) {
                        break;
                    }
                    String str2 = (String) enumerationKeys.nextElement2();
                    if (str.equalsIgnoreCase(str2)) {
                        property = provider.getProperty(str2);
                        break;
                    }
                }
            }
            if (property != null) {
                ProviderProperty providerProperty = new ProviderProperty();
                providerProperty.className = property;
                providerProperty.provider = provider;
                return providerProperty;
            }
        }
        return null;
    }

    private static String getProviderProperty(String str, Provider provider) {
        String property = provider.getProperty(str);
        if (property == null) {
            Enumeration<Object> enumerationKeys = provider.keys();
            while (true) {
                if (!enumerationKeys.hasMoreElements() || property != null) {
                    break;
                }
                String str2 = (String) enumerationKeys.nextElement2();
                if (str.equalsIgnoreCase(str2)) {
                    property = provider.getProperty(str2);
                    break;
                }
            }
        }
        return property;
    }

    @Deprecated
    public static String getAlgorithmProperty(String str, String str2) {
        ProviderProperty providerProperty = getProviderProperty("Alg." + str2 + "." + str);
        if (providerProperty != null) {
            return providerProperty.className;
        }
        return null;
    }

    public static synchronized int insertProviderAt(Provider provider, int i2) {
        String name = provider.getName();
        checkInsertProvider(name);
        ProviderList fullProviderList = Providers.getFullProviderList();
        ProviderList providerListInsertAt = ProviderList.insertAt(fullProviderList, provider, i2 - 1);
        if (fullProviderList == providerListInsertAt) {
            return -1;
        }
        Providers.setProviderList(providerListInsertAt);
        return providerListInsertAt.getIndex(name) + 1;
    }

    public static int addProvider(Provider provider) {
        return insertProviderAt(provider, 0);
    }

    public static synchronized void removeProvider(String str) {
        check("removeProvider." + str);
        Providers.setProviderList(ProviderList.remove(Providers.getFullProviderList(), str));
    }

    public static Provider[] getProviders() {
        return Providers.getFullProviderList().toArray();
    }

    public static Provider getProvider(String str) {
        return Providers.getProviderList().getProvider(str);
    }

    public static Provider[] getProviders(String str) {
        String strSubstring;
        String strSubstring2;
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1) {
            strSubstring = str;
            strSubstring2 = "";
        } else {
            strSubstring = str.substring(0, iIndexOf);
            strSubstring2 = str.substring(iIndexOf + 1);
        }
        Hashtable hashtable = new Hashtable(1);
        hashtable.put(strSubstring, strSubstring2);
        return getProviders(hashtable);
    }

    public static Provider[] getProviders(Map<String, String> map) {
        Provider[] providers = getProviders();
        Set<String> setKeySet = map.keySet();
        LinkedHashSet<Provider> linkedHashSet = new LinkedHashSet<>(5);
        if (setKeySet == null || providers == null) {
            return providers;
        }
        boolean z2 = true;
        for (String str : setKeySet) {
            LinkedHashSet<Provider> allQualifyingCandidates = getAllQualifyingCandidates(str, map.get(str), providers);
            if (z2) {
                linkedHashSet = allQualifyingCandidates;
                z2 = false;
            }
            if (allQualifyingCandidates != null && !allQualifyingCandidates.isEmpty()) {
                Iterator<Provider> it = linkedHashSet.iterator();
                while (it.hasNext()) {
                    if (!allQualifyingCandidates.contains(it.next())) {
                        it.remove();
                    }
                }
            } else {
                linkedHashSet = null;
                break;
            }
        }
        if (linkedHashSet == null || linkedHashSet.isEmpty()) {
            return null;
        }
        Object[] array = linkedHashSet.toArray();
        Provider[] providerArr = new Provider[array.length];
        for (int i2 = 0; i2 < providerArr.length; i2++) {
            providerArr[i2] = (Provider) array[i2];
        }
        return providerArr;
    }

    private static Class<?> getSpiClass(String str) {
        Class<?> cls = spiMap.get(str);
        if (cls != null) {
            return cls;
        }
        try {
            Class<?> cls2 = Class.forName("java.security." + str + "Spi");
            spiMap.put(str, cls2);
            return cls2;
        } catch (ClassNotFoundException e2) {
            throw new AssertionError("Spi class not found", e2);
        }
    }

    static Object[] getImpl(String str, String str2, String str3) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str3 == null) {
            return GetInstance.getInstance(str2, getSpiClass(str2), str).toArray();
        }
        return GetInstance.getInstance(str2, getSpiClass(str2), str, str3).toArray();
    }

    static Object[] getImpl(String str, String str2, String str3, Object obj) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        if (str3 == null) {
            return GetInstance.getInstance(str2, getSpiClass(str2), str, obj).toArray();
        }
        return GetInstance.getInstance(str2, getSpiClass(str2), str, obj, str3).toArray();
    }

    static Object[] getImpl(String str, String str2, Provider provider) throws NoSuchAlgorithmException {
        return GetInstance.getInstance(str2, getSpiClass(str2), str, provider).toArray();
    }

    static Object[] getImpl(String str, String str2, Provider provider, Object obj) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        return GetInstance.getInstance(str2, getSpiClass(str2), str, obj, provider).toArray();
    }

    public static String getProperty(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SecurityPermission("getProperty." + str));
        }
        String property = props.getProperty(str);
        if (property != null) {
            property = property.trim();
        }
        return property;
    }

    public static void setProperty(String str, String str2) {
        check("setProperty." + str);
        props.put(str, str2);
        invalidateSMCache(str);
    }

    private static void invalidateSMCache(String str) {
        final boolean zEquals = str.equals("package.access");
        boolean zEquals2 = str.equals("package.definition");
        if (zEquals || zEquals2) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.security.Security.2
                /* JADX WARN: Can't rename method to resolve collision */
                /* JADX WARN: Multi-variable type inference failed */
                /* JADX WARN: Type inference failed for: r0v13, types: [java.lang.reflect.Field] */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    Field declaredField;
                    boolean zIsAccessible;
                    try {
                        Class<?> cls = Class.forName("java.lang.SecurityManager", false, null);
                        if (zEquals) {
                            declaredField = cls.getDeclaredField("packageAccessValid");
                            zIsAccessible = declaredField.isAccessible();
                            declaredField.setAccessible(true);
                        } else {
                            declaredField = cls.getDeclaredField("packageDefinitionValid");
                            zIsAccessible = declaredField.isAccessible();
                            declaredField.setAccessible(true);
                        }
                        declaredField.setBoolean(declaredField, false);
                        declaredField.setAccessible(zIsAccessible);
                        return null;
                    } catch (Exception e2) {
                        return null;
                    }
                }
            });
        }
    }

    private static void check(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkSecurityAccess(str);
        }
    }

    private static void checkInsertProvider(String str) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            try {
                securityManager.checkSecurityAccess("insertProvider");
            } catch (SecurityException e2) {
                try {
                    securityManager.checkSecurityAccess("insertProvider." + str);
                } catch (SecurityException e3) {
                    e2.addSuppressed(e3);
                    throw e2;
                }
            }
        }
    }

    private static LinkedHashSet<Provider> getAllQualifyingCandidates(String str, String str2, Provider[] providerArr) {
        String[] filterComponents = getFilterComponents(str, str2);
        return getProvidersNotUsingCache(filterComponents[0], filterComponents[1], filterComponents[2], str2, providerArr);
    }

    private static LinkedHashSet<Provider> getProvidersNotUsingCache(String str, String str2, String str3, String str4, Provider[] providerArr) {
        LinkedHashSet<Provider> linkedHashSet = new LinkedHashSet<>(5);
        for (int i2 = 0; i2 < providerArr.length; i2++) {
            if (isCriterionSatisfied(providerArr[i2], str, str2, str3, str4)) {
                linkedHashSet.add(providerArr[i2]);
            }
        }
        return linkedHashSet;
    }

    private static boolean isCriterionSatisfied(Provider provider, String str, String str2, String str3, String str4) {
        String str5 = str + '.' + str2;
        if (str3 != null) {
            str5 = str5 + ' ' + str3;
        }
        String providerProperty = getProviderProperty(str5, provider);
        if (providerProperty == null) {
            String providerProperty2 = getProviderProperty("Alg.Alias." + str + "." + str2, provider);
            if (providerProperty2 != null) {
                String str6 = str + "." + providerProperty2;
                if (str3 != null) {
                    str6 = str6 + ' ' + str3;
                }
                providerProperty = getProviderProperty(str6, provider);
            }
            if (providerProperty == null) {
                return false;
            }
        }
        if (str3 == null) {
            return true;
        }
        if (isStandardAttr(str3)) {
            return isConstraintSatisfied(str3, str4, providerProperty);
        }
        return str4.equalsIgnoreCase(providerProperty);
    }

    private static boolean isStandardAttr(String str) {
        if (str.equalsIgnoreCase("KeySize") || str.equalsIgnoreCase("ImplementedIn")) {
            return true;
        }
        return false;
    }

    private static boolean isConstraintSatisfied(String str, String str2, String str3) {
        if (str.equalsIgnoreCase("KeySize")) {
            if (Integer.parseInt(str2) <= Integer.parseInt(str3)) {
                return true;
            }
            return false;
        }
        if (str.equalsIgnoreCase("ImplementedIn")) {
            return str2.equalsIgnoreCase(str3);
        }
        return false;
    }

    static String[] getFilterComponents(String str, String str2) {
        String strSubstring;
        int iIndexOf = str.indexOf(46);
        if (iIndexOf < 0) {
            throw new InvalidParameterException("Invalid filter");
        }
        String strSubstring2 = str.substring(0, iIndexOf);
        String strTrim = null;
        if (str2.length() == 0) {
            strSubstring = str.substring(iIndexOf + 1).trim();
            if (strSubstring.length() == 0) {
                throw new InvalidParameterException("Invalid filter");
            }
        } else {
            int iIndexOf2 = str.indexOf(32);
            if (iIndexOf2 == -1) {
                throw new InvalidParameterException("Invalid filter");
            }
            strTrim = str.substring(iIndexOf2 + 1).trim();
            if (strTrim.length() == 0) {
                throw new InvalidParameterException("Invalid filter");
            }
            if (iIndexOf2 < iIndexOf || iIndexOf == iIndexOf2 - 1) {
                throw new InvalidParameterException("Invalid filter");
            }
            strSubstring = str.substring(iIndexOf + 1, iIndexOf2);
        }
        return new String[]{strSubstring2, strSubstring, strTrim};
    }

    public static Set<String> getAlgorithms(String str) {
        if (str == null || str.length() == 0 || str.endsWith(".")) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        for (Provider provider : getProviders()) {
            Enumeration<Object> enumerationKeys = provider.keys();
            while (enumerationKeys.hasMoreElements()) {
                String upperCase = ((String) enumerationKeys.nextElement2()).toUpperCase(Locale.ENGLISH);
                if (upperCase.startsWith(str.toUpperCase(Locale.ENGLISH)) && upperCase.indexOf(" ") < 0) {
                    hashSet.add(upperCase.substring(str.length() + 1));
                }
            }
        }
        return Collections.unmodifiableSet(hashSet);
    }
}
