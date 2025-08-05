package sun.misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.misc.Launcher;
import sun.net.www.ParseUtil;
import sun.security.action.GetPropertyAction;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/misc/ExtensionDependency.class */
public class ExtensionDependency {
    private static Vector<ExtensionInstallationProvider> providers;
    static final boolean DEBUG = false;

    public static synchronized void addExtensionInstallationProvider(ExtensionInstallationProvider extensionInstallationProvider) {
        if (providers == null) {
            providers = new Vector<>();
        }
        providers.add(extensionInstallationProvider);
    }

    public static synchronized void removeExtensionInstallationProvider(ExtensionInstallationProvider extensionInstallationProvider) {
        providers.remove(extensionInstallationProvider);
    }

    public static boolean checkExtensionsDependencies(JarFile jarFile) {
        if (providers == null) {
            return true;
        }
        try {
            return new ExtensionDependency().checkExtensions(jarFile);
        } catch (ExtensionInstallationException e2) {
            debug(e2.getMessage());
            return false;
        }
    }

    protected boolean checkExtensions(JarFile jarFile) throws ExtensionInstallationException {
        try {
            Manifest manifest = jarFile.getManifest();
            if (manifest == null) {
                return true;
            }
            boolean z2 = true;
            Attributes mainAttributes = manifest.getMainAttributes();
            if (mainAttributes != null) {
                String value = mainAttributes.getValue(Attributes.Name.EXTENSION_LIST);
                if (value != null) {
                    StringTokenizer stringTokenizer = new StringTokenizer(value);
                    while (stringTokenizer.hasMoreTokens()) {
                        String strNextToken = stringTokenizer.nextToken();
                        debug("The file " + jarFile.getName() + " appears to depend on " + strNextToken);
                        String str = strNextToken + LanguageTag.SEP + Attributes.Name.EXTENSION_NAME.toString();
                        if (mainAttributes.getValue(str) == null) {
                            debug("The jar file " + jarFile.getName() + " appers to depend on " + strNextToken + " but does not define the " + str + " attribute in its manifest ");
                        } else if (!checkExtension(strNextToken, mainAttributes)) {
                            debug("Failed installing " + strNextToken);
                            z2 = false;
                        }
                    }
                } else {
                    debug("No dependencies for " + jarFile.getName());
                }
            }
            return z2;
        } catch (IOException e2) {
            return false;
        }
    }

    protected synchronized boolean checkExtension(String str, Attributes attributes) throws ExtensionInstallationException {
        debug("Checking extension " + str);
        if (checkExtensionAgainstInstalled(str, attributes)) {
            return true;
        }
        debug("Extension not currently installed ");
        return installExtension(new ExtensionInfo(str, attributes), null);
    }

    boolean checkExtensionAgainstInstalled(String str, Attributes attributes) throws ExtensionInstallationException {
        File fileCheckExtensionExists = checkExtensionExists(str);
        if (fileCheckExtensionExists != null) {
            try {
                if (checkExtensionAgainst(str, attributes, fileCheckExtensionExists)) {
                    return true;
                }
                return false;
            } catch (FileNotFoundException e2) {
                debugException(e2);
                return false;
            } catch (IOException e3) {
                debugException(e3);
                return false;
            }
        }
        try {
            for (File file : getInstalledExtensions()) {
                try {
                } catch (FileNotFoundException e4) {
                    debugException(e4);
                } catch (IOException e5) {
                    debugException(e5);
                }
                if (checkExtensionAgainst(str, attributes, file)) {
                    return true;
                }
            }
            return false;
        } catch (IOException e6) {
            debugException(e6);
            return false;
        }
    }

    protected boolean checkExtensionAgainst(String str, Attributes attributes, final File file) throws ExtensionInstallationException, IOException {
        Attributes mainAttributes;
        debug("Checking extension " + str + " against " + file.getName());
        try {
            Manifest manifest = (Manifest) AccessController.doPrivileged(new PrivilegedExceptionAction<Manifest>() { // from class: sun.misc.ExtensionDependency.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Manifest run() throws IOException {
                    if (!file.exists()) {
                        throw new FileNotFoundException(file.getName());
                    }
                    return new JarFile(file).getManifest();
                }
            });
            ExtensionInfo extensionInfo = new ExtensionInfo(str, attributes);
            debug("Requested Extension : " + ((Object) extensionInfo));
            if (manifest != null && (mainAttributes = manifest.getMainAttributes()) != null) {
                ExtensionInfo extensionInfo2 = new ExtensionInfo(null, mainAttributes);
                debug("Extension Installed " + ((Object) extensionInfo2));
                switch (extensionInfo2.isCompatibleWith(extensionInfo)) {
                    case 0:
                        debug("Extensions are compatible");
                        return true;
                    case 4:
                        debug("Extensions are incompatible");
                        return false;
                    default:
                        debug("Extensions require an upgrade or vendor switch");
                        return installExtension(extensionInfo, extensionInfo2);
                }
            }
            return false;
        } catch (PrivilegedActionException e2) {
            if (e2.getException() instanceof FileNotFoundException) {
                throw ((FileNotFoundException) e2.getException());
            }
            throw ((IOException) e2.getException());
        }
    }

    protected boolean installExtension(ExtensionInfo extensionInfo, ExtensionInfo extensionInfo2) throws ExtensionInstallationException {
        Vector vector;
        synchronized (providers) {
            vector = (Vector) providers.clone();
        }
        Enumeration enumerationElements = vector.elements();
        while (enumerationElements.hasMoreElements()) {
            ExtensionInstallationProvider extensionInstallationProvider = (ExtensionInstallationProvider) enumerationElements.nextElement2();
            if (extensionInstallationProvider != null && extensionInstallationProvider.installExtension(extensionInfo, extensionInfo2)) {
                debug(extensionInfo.name + " installation successful");
                addNewExtensionsToClassLoader((Launcher.ExtClassLoader) Launcher.getLauncher().getClassLoader().getParent());
                return true;
            }
        }
        debug(extensionInfo.name + " installation failed");
        return false;
    }

    private File checkExtensionExists(final String str) {
        final String[] strArr = {".jar", ".zip"};
        return (File) AccessController.doPrivileged(new PrivilegedAction<File>() { // from class: sun.misc.ExtensionDependency.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public File run2() {
                File file;
                try {
                    File[] extDirs = ExtensionDependency.getExtDirs();
                    for (int i2 = 0; i2 < extDirs.length; i2++) {
                        for (int i3 = 0; i3 < strArr.length; i3++) {
                            if (str.toLowerCase().endsWith(strArr[i3])) {
                                file = new File(extDirs[i2], str);
                            } else {
                                file = new File(extDirs[i2], str + strArr[i3]);
                            }
                            ExtensionDependency.debug("checkExtensionExists:fileName " + file.getName());
                            if (file.exists()) {
                                return file;
                            }
                        }
                    }
                    return null;
                } catch (Exception e2) {
                    ExtensionDependency.this.debugException(e2);
                    return null;
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] getExtDirs() {
        File[] fileArr;
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("java.ext.dirs"));
        if (str != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, File.pathSeparator);
            int iCountTokens = stringTokenizer.countTokens();
            debug("getExtDirs count " + iCountTokens);
            fileArr = new File[iCountTokens];
            for (int i2 = 0; i2 < iCountTokens; i2++) {
                fileArr[i2] = new File(stringTokenizer.nextToken());
                debug("getExtDirs dirs[" + i2 + "] " + ((Object) fileArr[i2]));
            }
        } else {
            fileArr = new File[0];
            debug("getExtDirs dirs " + ((Object) fileArr));
        }
        debug("getExtDirs dirs.length " + fileArr.length);
        return fileArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static File[] getExtFiles(File[] fileArr) throws IOException {
        Vector vector = new Vector();
        for (int i2 = 0; i2 < fileArr.length; i2++) {
            String[] list = fileArr[i2].list(new JarFilter());
            if (list != null) {
                debug("getExtFiles files.length " + list.length);
                for (int i3 = 0; i3 < list.length; i3++) {
                    File file = new File(fileArr[i2], list[i3]);
                    vector.add(file);
                    debug("getExtFiles f[" + i3 + "] " + ((Object) file));
                }
            }
        }
        File[] fileArr2 = new File[vector.size()];
        vector.copyInto(fileArr2);
        debug("getExtFiles ua.length " + fileArr2.length);
        return fileArr2;
    }

    private File[] getInstalledExtensions() throws IOException {
        return (File[]) AccessController.doPrivileged(new PrivilegedAction<File[]>() { // from class: sun.misc.ExtensionDependency.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public File[] run2() {
                try {
                    return ExtensionDependency.getExtFiles(ExtensionDependency.getExtDirs());
                } catch (IOException e2) {
                    ExtensionDependency.debug("Cannot get list of installed extensions");
                    ExtensionDependency.this.debugException(e2);
                    return new File[0];
                }
            }
        });
    }

    private Boolean addNewExtensionsToClassLoader(Launcher.ExtClassLoader extClassLoader) {
        try {
            for (final File file : getInstalledExtensions()) {
                URL url = (URL) AccessController.doPrivileged(new PrivilegedAction<URL>() { // from class: sun.misc.ExtensionDependency.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public URL run2() {
                        try {
                            return ParseUtil.fileToEncodedURL(file);
                        } catch (MalformedURLException e2) {
                            ExtensionDependency.this.debugException(e2);
                            return null;
                        }
                    }
                });
                if (url != null) {
                    URL[] uRLs = extClassLoader.getURLs();
                    boolean z2 = false;
                    for (int i2 = 0; i2 < uRLs.length; i2++) {
                        debug("URL[" + i2 + "] is " + ((Object) uRLs[i2]) + " looking for " + ((Object) url));
                        if (uRLs[i2].toString().compareToIgnoreCase(url.toString()) == 0) {
                            z2 = true;
                            debug("Found !");
                        }
                    }
                    if (!z2) {
                        debug("Not Found ! adding to the classloader " + ((Object) url));
                        extClassLoader.addExtURL(url);
                    }
                }
            }
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return Boolean.TRUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void debug(String str) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debugException(Throwable th) {
    }
}
