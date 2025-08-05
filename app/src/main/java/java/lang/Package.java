package java.lang;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import sun.net.www.ParseUtil;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/* loaded from: rt.jar:java/lang/Package.class */
public class Package implements AnnotatedElement {
    private static Map<String, Package> pkgs = new HashMap(31);
    private static Map<String, URL> urls = new HashMap(10);
    private static Map<String, Manifest> mans = new HashMap(10);
    private final String pkgName;
    private final String specTitle;
    private final String specVersion;
    private final String specVendor;
    private final String implTitle;
    private final String implVersion;
    private final String implVendor;
    private final URL sealBase;
    private final transient ClassLoader loader;
    private transient Class<?> packageInfo;

    private static native String getSystemPackage0(String str);

    private static native String[] getSystemPackages0();

    public String getName() {
        return this.pkgName;
    }

    public String getSpecificationTitle() {
        return this.specTitle;
    }

    public String getSpecificationVersion() {
        return this.specVersion;
    }

    public String getSpecificationVendor() {
        return this.specVendor;
    }

    public String getImplementationTitle() {
        return this.implTitle;
    }

    public String getImplementationVersion() {
        return this.implVersion;
    }

    public String getImplementationVendor() {
        return this.implVendor;
    }

    public boolean isSealed() {
        return this.sealBase != null;
    }

    public boolean isSealed(URL url) {
        return url.equals(this.sealBase);
    }

    public boolean isCompatibleWith(String str) throws NumberFormatException {
        if (this.specVersion == null || this.specVersion.length() < 1) {
            throw new NumberFormatException("Empty version string");
        }
        String[] strArrSplit = this.specVersion.split("\\.", -1);
        int[] iArr = new int[strArrSplit.length];
        for (int i2 = 0; i2 < strArrSplit.length; i2++) {
            iArr[i2] = Integer.parseInt(strArrSplit[i2]);
            if (iArr[i2] < 0) {
                throw NumberFormatException.forInputString("" + iArr[i2]);
            }
        }
        String[] strArrSplit2 = str.split("\\.", -1);
        int[] iArr2 = new int[strArrSplit2.length];
        for (int i3 = 0; i3 < strArrSplit2.length; i3++) {
            iArr2[i3] = Integer.parseInt(strArrSplit2[i3]);
            if (iArr2[i3] < 0) {
                throw NumberFormatException.forInputString("" + iArr2[i3]);
            }
        }
        int iMax = Math.max(iArr2.length, iArr.length);
        int i4 = 0;
        while (i4 < iMax) {
            int i5 = i4 < iArr2.length ? iArr2[i4] : 0;
            int i6 = i4 < iArr.length ? iArr[i4] : 0;
            if (i6 < i5) {
                return false;
            }
            if (i6 <= i5) {
                i4++;
            } else {
                return true;
            }
        }
        return true;
    }

    @CallerSensitive
    public static Package getPackage(String str) {
        ClassLoader classLoader = ClassLoader.getClassLoader(Reflection.getCallerClass());
        if (classLoader != null) {
            return classLoader.getPackage(str);
        }
        return getSystemPackage(str);
    }

    @CallerSensitive
    public static Package[] getPackages() {
        ClassLoader classLoader = ClassLoader.getClassLoader(Reflection.getCallerClass());
        if (classLoader != null) {
            return classLoader.getPackages();
        }
        return getSystemPackages();
    }

    static Package getPackage(Class<?> cls) {
        String name = cls.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        if (iLastIndexOf != -1) {
            String strSubstring = name.substring(0, iLastIndexOf);
            ClassLoader classLoader = cls.getClassLoader();
            if (classLoader != null) {
                return classLoader.getPackage(strSubstring);
            }
            return getSystemPackage(strSubstring);
        }
        return null;
    }

    public int hashCode() {
        return this.pkgName.hashCode();
    }

    public String toString() {
        String str;
        String str2;
        String str3 = this.specTitle;
        String str4 = this.specVersion;
        if (str3 != null && str3.length() > 0) {
            str = ", " + str3;
        } else {
            str = "";
        }
        if (str4 != null && str4.length() > 0) {
            str2 = ", version " + str4;
        } else {
            str2 = "";
        }
        return "package " + this.pkgName + str + str2;
    }

    private Class<?> getPackageInfo() {
        if (this.packageInfo == null) {
            try {
                this.packageInfo = Class.forName(this.pkgName + ".package-info", false, this.loader);
            } catch (ClassNotFoundException e2) {
                this.packageInfo = C1PackageInfoProxy.class;
            }
        }
        return this.packageInfo;
    }

    /* renamed from: java.lang.Package$1PackageInfoProxy, reason: invalid class name */
    /* loaded from: rt.jar:java/lang/Package$1PackageInfoProxy.class */
    class C1PackageInfoProxy {
        C1PackageInfoProxy() {
        }
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        return (A) getPackageInfo().getAnnotation(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return super.isAnnotationPresent(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> cls) {
        return (A[]) getPackageInfo().getAnnotationsByType(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        return getPackageInfo().getAnnotations();
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> cls) {
        return (A) getPackageInfo().getDeclaredAnnotation(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> cls) {
        return (A[]) getPackageInfo().getDeclaredAnnotationsByType(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return getPackageInfo().getDeclaredAnnotations();
    }

    Package(String str, String str2, String str3, String str4, String str5, String str6, String str7, URL url, ClassLoader classLoader) {
        this.pkgName = str;
        this.implTitle = str5;
        this.implVersion = str6;
        this.implVendor = str7;
        this.specTitle = str2;
        this.specVersion = str3;
        this.specVendor = str4;
        this.sealBase = url;
        this.loader = classLoader;
    }

    private Package(String str, Manifest manifest, URL url, ClassLoader classLoader) {
        String value = null;
        String value2 = null;
        String value3 = null;
        String value4 = null;
        String value5 = null;
        String value6 = null;
        String value7 = null;
        URL url2 = null;
        Attributes attributes = manifest.getAttributes(str.replace('.', '/').concat("/"));
        if (attributes != null) {
            value2 = attributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            value3 = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            value4 = attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            value5 = attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            value6 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            value7 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            value = attributes.getValue(Attributes.Name.SEALED);
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes != null) {
            value2 = value2 == null ? mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE) : value2;
            value3 = value3 == null ? mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION) : value3;
            value4 = value4 == null ? mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR) : value4;
            value5 = value5 == null ? mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE) : value5;
            value6 = value6 == null ? mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION) : value6;
            value7 = value7 == null ? mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR) : value7;
            if (value == null) {
                value = mainAttributes.getValue(Attributes.Name.SEALED);
            }
        }
        url2 = "true".equalsIgnoreCase(value) ? url : url2;
        this.pkgName = str;
        this.specTitle = value2;
        this.specVersion = value3;
        this.specVendor = value4;
        this.implTitle = value5;
        this.implVersion = value6;
        this.implVendor = value7;
        this.sealBase = url2;
        this.loader = classLoader;
    }

    static Package getSystemPackage(String str) {
        Package r0;
        String strConcat;
        String systemPackage0;
        synchronized (pkgs) {
            Package packageDefineSystemPackage = pkgs.get(str);
            if (packageDefineSystemPackage == null && (systemPackage0 = getSystemPackage0((strConcat = str.replace('.', '/').concat("/")))) != null) {
                packageDefineSystemPackage = defineSystemPackage(strConcat, systemPackage0);
            }
            r0 = packageDefineSystemPackage;
        }
        return r0;
    }

    static Package[] getSystemPackages() {
        Package[] packageArr;
        String[] systemPackages0 = getSystemPackages0();
        synchronized (pkgs) {
            for (int i2 = 0; i2 < systemPackages0.length; i2++) {
                defineSystemPackage(systemPackages0[i2], getSystemPackage0(systemPackages0[i2]));
            }
            packageArr = (Package[]) pkgs.values().toArray(new Package[pkgs.size()]);
        }
        return packageArr;
    }

    private static Package defineSystemPackage(final String str, final String str2) {
        return (Package) AccessController.doPrivileged(new PrivilegedAction<Package>() { // from class: java.lang.Package.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Package run2() {
                Package r15;
                String str3 = str;
                URL urlFileToEncodedURL = (URL) Package.urls.get(str2);
                if (urlFileToEncodedURL == null) {
                    File file = new File(str2);
                    try {
                        urlFileToEncodedURL = ParseUtil.fileToEncodedURL(file);
                    } catch (MalformedURLException e2) {
                    }
                    if (urlFileToEncodedURL != null) {
                        Package.urls.put(str2, urlFileToEncodedURL);
                        if (file.isFile()) {
                            Package.mans.put(str2, Package.loadManifest(str2));
                        }
                    }
                }
                String strReplace = str3.substring(0, str3.length() - 1).replace('/', '.');
                Manifest manifest = (Manifest) Package.mans.get(str2);
                if (manifest != null) {
                    r15 = new Package(strReplace, manifest, urlFileToEncodedURL, null);
                } else {
                    r15 = new Package(strReplace, null, null, null, null, null, null, null, null);
                }
                Package.pkgs.put(strReplace, r15);
                return r15;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Manifest loadManifest(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            Throwable th = null;
            try {
                JarInputStream jarInputStream = new JarInputStream(fileInputStream, false);
                Throwable th2 = null;
                try {
                    try {
                        Manifest manifest = jarInputStream.getManifest();
                        if (jarInputStream != null) {
                            if (0 != 0) {
                                try {
                                    jarInputStream.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                jarInputStream.close();
                            }
                        }
                        return manifest;
                    } catch (Throwable th4) {
                        if (jarInputStream != null) {
                            if (th2 != null) {
                                try {
                                    jarInputStream.close();
                                } catch (Throwable th5) {
                                    th2.addSuppressed(th5);
                                }
                            } else {
                                jarInputStream.close();
                            }
                        }
                        throw th4;
                    }
                } finally {
                }
            } finally {
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
            }
        } catch (IOException e2) {
            return null;
        }
    }
}
