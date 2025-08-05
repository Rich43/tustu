package sun.launcher;

import com.sun.xml.internal.ws.policy.PolicyConstants;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeSet;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.platform.Container;
import jdk.internal.platform.Metrics;
import org.icepdf.core.util.PdfOps;
import sun.misc.VM;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: rt.jar:sun/launcher/LauncherHelper.class */
public enum LauncherHelper {
    INSTANCE;

    private static final String MAIN_CLASS = "Main-Class";
    private static StringBuilder outBuf = new StringBuilder();
    private static final String INDENT = "    ";
    private static final String VM_SETTINGS = "VM settings:";
    private static final String PROP_SETTINGS = "Property settings:";
    private static final String LOCALE_SETTINGS = "Locale settings:";
    private static final String diagprop = "sun.java.launcher.diag";
    static final boolean trace;
    private static final String defaultBundleName = "sun.launcher.resources.launcher";
    private static PrintStream ostream;
    private static final ClassLoader scloader;
    private static Class<?> appClass;
    private static final int LM_UNKNOWN = 0;
    private static final int LM_CLASS = 1;
    private static final int LM_JAR = 2;
    private static final String encprop = "sun.jnu.encoding";
    private static String encoding;
    private static boolean isCharsetSupported;

    static {
        trace = VM.getSavedProperty(diagprop) != null;
        scloader = ClassLoader.getSystemClassLoader();
        encoding = null;
        isCharsetSupported = false;
    }

    /* loaded from: rt.jar:sun/launcher/LauncherHelper$ResourceBundleHolder.class */
    private static class ResourceBundleHolder {
        private static final ResourceBundle RB = ResourceBundle.getBundle(LauncherHelper.defaultBundleName);

        private ResourceBundleHolder() {
        }
    }

    static void showSettings(boolean z2, String str, long j2, long j3, long j4, boolean z3) {
        initOutput(z2);
        String[] strArrSplit = str.split(CallSiteDescriptor.TOKEN_DELIMITER);
        switch ((strArrSplit.length <= 1 || strArrSplit[1] == null) ? "all" : strArrSplit[1].trim()) {
            case "vm":
                printVmSettings(j2, j3, j4, z3);
                return;
            case "properties":
                printProperties();
                return;
            case "locale":
                printLocale();
                return;
            case "system":
                if (System.getProperty("os.name").contains("Linux")) {
                    printSystemMetrics();
                    return;
                }
                break;
        }
        printVmSettings(j2, j3, j4, z3);
        printProperties();
        printLocale();
        if (System.getProperty("os.name").contains("Linux")) {
            printSystemMetrics();
        }
    }

    private static void printVmSettings(long j2, long j3, long j4, boolean z2) {
        ostream.println(VM_SETTINGS);
        if (j4 != 0) {
            ostream.println("    Stack Size: " + SizePrefix.scaleValue(j4));
        }
        if (j2 != 0) {
            ostream.println("    Min. Heap Size: " + SizePrefix.scaleValue(j2));
        }
        if (j3 != 0) {
            ostream.println("    Max. Heap Size: " + SizePrefix.scaleValue(j3));
        } else {
            ostream.println("    Max. Heap Size (Estimated): " + SizePrefix.scaleValue(Runtime.getRuntime().maxMemory()));
        }
        ostream.println("    Ergonomics Machine Class: " + (z2 ? "server" : PolicyConstants.CLIENT_CONFIGURATION_IDENTIFIER));
        ostream.println("    Using VM: " + System.getProperty("java.vm.name"));
        ostream.println();
    }

    private static void printProperties() {
        Properties properties = System.getProperties();
        ostream.println(PROP_SETTINGS);
        ArrayList<String> arrayList = new ArrayList();
        arrayList.addAll(properties.stringPropertyNames());
        Collections.sort(arrayList);
        for (String str : arrayList) {
            printPropertyValue(str, properties.getProperty(str));
        }
        ostream.println();
    }

    private static boolean isPath(String str) {
        return str.endsWith(".dirs") || str.endsWith(".path");
    }

    private static void printPropertyValue(String str, String str2) {
        ostream.print(INDENT + str + " = ");
        if (str.equals("line.separator")) {
            for (byte b2 : str2.getBytes()) {
                switch (b2) {
                    case 10:
                        ostream.print("\\n ");
                        break;
                    case 13:
                        ostream.print("\\r ");
                        break;
                    default:
                        ostream.printf("0x%02X", Integer.valueOf(b2 & 255));
                        break;
                }
            }
            ostream.println();
            return;
        }
        if (!isPath(str)) {
            ostream.println(str2);
            return;
        }
        boolean z2 = true;
        for (String str3 : str2.split(System.getProperty("path.separator"))) {
            if (z2) {
                ostream.println(str3);
                z2 = false;
            } else {
                ostream.println("        " + str3);
            }
        }
    }

    private static void printLocale() {
        Locale locale = Locale.getDefault();
        ostream.println(LOCALE_SETTINGS);
        ostream.println("    default locale = " + locale.getDisplayLanguage());
        ostream.println("    default display locale = " + Locale.getDefault(Locale.Category.DISPLAY).getDisplayName());
        ostream.println("    default format locale = " + Locale.getDefault(Locale.Category.FORMAT).getDisplayName());
        printLocales();
        ostream.println();
    }

    private static void printLocales() {
        Locale[] availableLocales = Locale.getAvailableLocales();
        int length = availableLocales == null ? 0 : availableLocales.length;
        if (length < 1) {
            return;
        }
        TreeSet treeSet = new TreeSet();
        for (Locale locale : availableLocales) {
            treeSet.add(locale.toString());
        }
        ostream.print("    available locales = ");
        Iterator<E> it = treeSet.iterator();
        int i2 = length - 1;
        int i3 = 0;
        while (it.hasNext()) {
            ostream.print((String) it.next());
            if (i3 != i2) {
                ostream.print(", ");
            }
            if ((i3 + 1) % 8 == 0) {
                ostream.println();
                ostream.print("        ");
            }
            i3++;
        }
    }

    public static void printSystemMetrics() {
        Metrics metrics = Container.metrics();
        ostream.println("Operating System Metrics:");
        if (metrics == null) {
            ostream.println("    No metrics available for this platform");
            return;
        }
        ostream.println("    Provider: " + metrics.getProvider());
        ostream.println("    Effective CPU Count: " + metrics.getEffectiveCpuCount());
        ostream.println(formatCpuVal(metrics.getCpuPeriod(), "    CPU Period: ", -2L));
        ostream.println(formatCpuVal(metrics.getCpuQuota(), "    CPU Quota: ", -2L));
        ostream.println(formatCpuVal(metrics.getCpuShares(), "    CPU Shares: ", -2L));
        int[] cpuSetCpus = metrics.getCpuSetCpus();
        if (cpuSetCpus != null) {
            ostream.println("    List of Processors, " + cpuSetCpus.length + " total: ");
            ostream.print(INDENT);
            for (int i2 : cpuSetCpus) {
                ostream.print(i2 + " ");
            }
            if (cpuSetCpus.length > 0) {
                ostream.println("");
            }
        } else {
            ostream.println("    List of Processors: N/A");
        }
        int[] effectiveCpuSetCpus = metrics.getEffectiveCpuSetCpus();
        if (effectiveCpuSetCpus != null) {
            ostream.println("    List of Effective Processors, " + effectiveCpuSetCpus.length + " total: ");
            ostream.print(INDENT);
            for (int i3 : effectiveCpuSetCpus) {
                ostream.print(i3 + " ");
            }
            if (effectiveCpuSetCpus.length > 0) {
                ostream.println("");
            }
        } else {
            ostream.println("    List of Effective Processors: N/A");
        }
        int[] cpuSetMems = metrics.getCpuSetMems();
        if (cpuSetMems != null) {
            ostream.println("    List of Memory Nodes, " + cpuSetMems.length + " total: ");
            ostream.print(INDENT);
            for (int i4 : cpuSetMems) {
                ostream.print(i4 + " ");
            }
            if (cpuSetMems.length > 0) {
                ostream.println("");
            }
        } else {
            ostream.println("    List of Memory Nodes: N/A");
        }
        int[] effectiveCpuSetMems = metrics.getEffectiveCpuSetMems();
        if (effectiveCpuSetMems != null) {
            ostream.println("    List of Available Memory Nodes, " + effectiveCpuSetMems.length + " total: ");
            ostream.print(INDENT);
            for (int i5 : effectiveCpuSetMems) {
                ostream.print(i5 + " ");
            }
            if (effectiveCpuSetMems.length > 0) {
                ostream.println("");
            }
        } else {
            ostream.println("    List of Available Memory Nodes: N/A");
        }
        ostream.println(formatLimitString(metrics.getMemoryLimit(), "    Memory Limit: ", -2L));
        ostream.println(formatLimitString(metrics.getMemorySoftLimit(), "    Memory Soft Limit: ", -2L));
        ostream.println(formatLimitString(metrics.getMemoryAndSwapLimit(), "    Memory & Swap Limit: ", -2L));
        ostream.println("");
    }

    private static String formatLimitString(long j2, String str, long j3) {
        if (j2 >= 0) {
            return str + SizePrefix.scaleValue(j2);
        }
        if (j2 == j3) {
            return str + "N/A";
        }
        return str + "Unlimited";
    }

    private static String formatCpuVal(long j2, String str, long j3) {
        if (j2 >= 0) {
            return str + j2 + "us";
        }
        if (j2 == j3) {
            return str + "N/A";
        }
        return str + j2;
    }

    /* loaded from: rt.jar:sun/launcher/LauncherHelper$SizePrefix.class */
    private enum SizePrefix {
        KILO(1024, PdfOps.K_TOKEN),
        MEGA(1048576, PdfOps.M_TOKEN),
        GIGA(PKCS11Constants.CKF_ARRAY_ATTRIBUTE, "G"),
        TERA(1099511627776L, "T");

        long size;
        String abbrev;

        SizePrefix(long j2, String str) {
            this.size = j2;
            this.abbrev = str;
        }

        private static String scale(long j2, SizePrefix sizePrefix) {
            return BigDecimal.valueOf(j2).divide(BigDecimal.valueOf(sizePrefix.size), 2, RoundingMode.HALF_EVEN).toPlainString() + sizePrefix.abbrev;
        }

        static String scaleValue(long j2) {
            if (j2 < MEGA.size) {
                return scale(j2, KILO);
            }
            if (j2 < GIGA.size) {
                return scale(j2, MEGA);
            }
            if (j2 < TERA.size) {
                return scale(j2, GIGA);
            }
            return scale(j2, TERA);
        }
    }

    private static String getLocalizedMessage(String str, Object... objArr) {
        String string = ResourceBundleHolder.RB.getString(str);
        return objArr != null ? MessageFormat.format(string, objArr) : string;
    }

    static void initHelpMessage(String str) {
        StringBuilder sb = outBuf;
        Object[] objArr = new Object[1];
        objArr[0] = str == null ? "java" : str;
        outBuf = sb.append(getLocalizedMessage("java.launcher.opt.header", objArr));
        outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.datamodel", 32));
        outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.datamodel", 64));
    }

    static void appendVmSelectMessage(String str, String str2) {
        outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.vmselect", str, str2));
    }

    static void appendVmSynonymMessage(String str, String str2) {
        outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.hotspot", str, str2));
    }

    static void appendVmErgoMessage(boolean z2, String str) {
        StringBuilder sbAppend;
        outBuf = outBuf.append(getLocalizedMessage("java.launcher.ergo.message1", str));
        if (z2) {
            sbAppend = outBuf.append(",\n" + getLocalizedMessage("java.launcher.ergo.message2", new Object[0]) + "\n\n");
        } else {
            sbAppend = outBuf.append(".\n\n");
        }
        outBuf = sbAppend;
    }

    static void printHelpMessage(boolean z2) {
        initOutput(z2);
        outBuf = outBuf.append(getLocalizedMessage("java.launcher.opt.footer", File.pathSeparator));
        ostream.println(outBuf.toString());
    }

    static void printXUsageMessage(boolean z2) {
        initOutput(z2);
        ostream.println(getLocalizedMessage("java.launcher.X.usage", File.pathSeparator));
        if (System.getProperty("os.name").contains("OS X")) {
            ostream.println(getLocalizedMessage("java.launcher.X.macosx.usage", File.pathSeparator));
        }
    }

    static void initOutput(boolean z2) {
        ostream = z2 ? System.err : System.out;
    }

    /* JADX WARN: Failed to calculate best type for var: r10v0 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Failed to calculate best type for var: r9v1 ??
    java.lang.NullPointerException
     */
    /* JADX WARN: Multi-variable type inference failed. Error: java.lang.NullPointerException
     */
    /* JADX WARN: Not initialized variable reg: 10, insn: 0x00c8: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r10 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]), block:B:41:0x00c8 */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x00c4: MOVE (r0 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) = (r9 I:??[int, float, boolean, short, byte, char, OBJECT, ARRAY]) A[TRY_LEAVE], block:B:39:0x00c4 */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r9v1, types: [java.util.jar.JarFile] */
    static String getMainClassFromJar(String str) {
        try {
            try {
                JarFile jarFile = new JarFile(str);
                Throwable th = null;
                Manifest manifest = jarFile.getManifest();
                if (manifest == null) {
                    abort(null, "java.launcher.jar.error2", str);
                }
                Attributes mainAttributes = manifest.getMainAttributes();
                if (mainAttributes == null) {
                    abort(null, "java.launcher.jar.error3", str);
                }
                String value = mainAttributes.getValue(MAIN_CLASS);
                if (value == null) {
                    abort(null, "java.launcher.jar.error3", str);
                }
                if (mainAttributes.containsKey(new Attributes.Name("JavaFX-Application-Class"))) {
                    String name = FXHelper.class.getName();
                    if (jarFile != null) {
                        if (0 != 0) {
                            try {
                                jarFile.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        } else {
                            jarFile.close();
                        }
                    }
                    return name;
                }
                String strTrim = value.trim();
                if (jarFile != null) {
                    if (0 != 0) {
                        try {
                            jarFile.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                    } else {
                        jarFile.close();
                    }
                }
                return strTrim;
            } finally {
            }
        } catch (IOException e2) {
            abort(e2, "java.launcher.jar.error1", str);
            return null;
        }
        abort(e2, "java.launcher.jar.error1", str);
        return null;
    }

    static void abort(Throwable th, String str, Object... objArr) {
        if (str != null) {
            ostream.println(getLocalizedMessage(str, objArr));
        }
        if (trace) {
            if (th != null) {
                th.printStackTrace();
            } else {
                Thread.dumpStack();
            }
        }
        System.exit(1);
    }

    public static Class<?> checkAndLoadMain(boolean z2, int i2, String str) throws SecurityException {
        String mainClassFromJar;
        initOutput(z2);
        switch (i2) {
            case 1:
                mainClassFromJar = str;
                break;
            case 2:
                mainClassFromJar = getMainClassFromJar(str);
                break;
            default:
                throw new InternalError("" + i2 + ": Unknown launch mode");
        }
        String strReplace = mainClassFromJar.replace('/', '.');
        Class<?> clsLoadClass = null;
        try {
            clsLoadClass = scloader.loadClass(strReplace);
        } catch (ClassNotFoundException | NoClassDefFoundError e2) {
            if (System.getProperty("os.name", "").contains("OS X") && Normalizer.isNormalized(strReplace, Normalizer.Form.NFD)) {
                try {
                    clsLoadClass = scloader.loadClass(Normalizer.normalize(strReplace, Normalizer.Form.NFC));
                } catch (ClassNotFoundException | NoClassDefFoundError e3) {
                    abort(e2, "java.launcher.cls.error1", strReplace);
                }
            } else {
                abort(e2, "java.launcher.cls.error1", strReplace);
            }
        }
        appClass = clsLoadClass;
        if (!clsLoadClass.equals(FXHelper.class) && !FXHelper.doesExtendFXApplication(clsLoadClass)) {
            validateMainClass(clsLoadClass);
            return clsLoadClass;
        }
        FXHelper.setFXLaunchParameters(str, i2);
        return FXHelper.class;
    }

    public static Class<?> getApplicationClass() {
        return appClass;
    }

    static void validateMainClass(Class<?> cls) throws SecurityException {
        try {
            Method method = cls.getMethod("main", String[].class);
            if (!Modifier.isStatic(method.getModifiers())) {
                abort(null, "java.launcher.cls.error2", "static", method.getDeclaringClass().getName());
            }
            if (method.getReturnType() != Void.TYPE) {
                abort(null, "java.launcher.cls.error3", method.getDeclaringClass().getName());
            }
        } catch (NoSuchMethodException e2) {
            abort(null, "java.launcher.cls.error4", cls.getName(), "javafx.application.Application");
        }
    }

    static String makePlatformString(boolean z2, byte[] bArr) {
        initOutput(z2);
        if (encoding == null) {
            encoding = System.getProperty(encprop);
            isCharsetSupported = Charset.isSupported(encoding);
        }
        try {
            return isCharsetSupported ? new String(bArr, encoding) : new String(bArr);
        } catch (UnsupportedEncodingException e2) {
            abort(e2, null, new Object[0]);
            return null;
        }
    }

    static String[] expandArgs(String[] strArr) {
        ArrayList arrayList = new ArrayList();
        for (String str : strArr) {
            arrayList.add(new StdArg(str));
        }
        return expandArgs(arrayList);
    }

    static String[] expandArgs(List<StdArg> list) {
        ArrayList arrayList = new ArrayList();
        if (trace) {
            System.err.println("Incoming arguments:");
        }
        for (StdArg stdArg : list) {
            if (trace) {
                System.err.println(stdArg);
            }
            if (stdArg.needsExpansion) {
                File file = new File(stdArg.arg);
                File parentFile = file.getParentFile();
                String name = file.getName();
                if (parentFile == null) {
                    parentFile = new File(".");
                }
                try {
                    DirectoryStream<Path> directoryStreamNewDirectoryStream = Files.newDirectoryStream(parentFile.toPath(), name);
                    Throwable th = null;
                    try {
                        try {
                            int i2 = 0;
                            Iterator<Path> it = directoryStreamNewDirectoryStream.iterator();
                            while (it.hasNext()) {
                                arrayList.add(it.next().normalize().toString());
                                i2++;
                            }
                            if (i2 == 0) {
                                arrayList.add(stdArg.arg);
                            }
                            if (directoryStreamNewDirectoryStream != null) {
                                if (0 != 0) {
                                    try {
                                        directoryStreamNewDirectoryStream.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                } else {
                                    directoryStreamNewDirectoryStream.close();
                                }
                            }
                        } catch (Throwable th3) {
                            if (directoryStreamNewDirectoryStream != null) {
                                if (th != null) {
                                    try {
                                        directoryStreamNewDirectoryStream.close();
                                    } catch (Throwable th4) {
                                        th.addSuppressed(th4);
                                    }
                                } else {
                                    directoryStreamNewDirectoryStream.close();
                                }
                            }
                            throw th3;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        throw th5;
                    }
                } catch (Exception e2) {
                    arrayList.add(stdArg.arg);
                    if (trace) {
                        System.err.println("Warning: passing argument as-is " + ((Object) stdArg));
                        System.err.print(e2);
                    }
                }
            } else {
                arrayList.add(stdArg.arg);
            }
        }
        String[] strArr = new String[arrayList.size()];
        arrayList.toArray(strArr);
        if (trace) {
            System.err.println("Expanded arguments:");
            for (String str : strArr) {
                System.err.println(str);
            }
        }
        return strArr;
    }

    /* loaded from: rt.jar:sun/launcher/LauncherHelper$StdArg.class */
    private static class StdArg {
        final String arg;
        final boolean needsExpansion;

        StdArg(String str, boolean z2) {
            this.arg = str;
            this.needsExpansion = z2;
        }

        StdArg(String str) {
            this.arg = str.substring(1);
            this.needsExpansion = str.charAt(0) == 'T';
        }

        public String toString() {
            return "StdArg{arg=" + this.arg + ", needsExpansion=" + this.needsExpansion + '}';
        }
    }

    /* loaded from: rt.jar:sun/launcher/LauncherHelper$FXHelper.class */
    static final class FXHelper {
        private static final String JAVAFX_APPLICATION_MARKER = "JavaFX-Application-Class";
        private static final String JAVAFX_APPLICATION_CLASS_NAME = "javafx.application.Application";
        private static final String JAVAFX_LAUNCHER_CLASS_NAME = "com.sun.javafx.application.LauncherImpl";
        private static final String JAVAFX_LAUNCH_MODE_CLASS = "LM_CLASS";
        private static final String JAVAFX_LAUNCH_MODE_JAR = "LM_JAR";
        private static String fxLaunchName = null;
        private static String fxLaunchMode = null;
        private static Class<?> fxLauncherClass = null;
        private static Method fxLauncherMethod = null;

        FXHelper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void setFXLaunchParameters(String str, int i2) {
            try {
                fxLauncherClass = LauncherHelper.scloader.loadClass(JAVAFX_LAUNCHER_CLASS_NAME);
                fxLauncherMethod = fxLauncherClass.getMethod("launchApplication", String.class, String.class, String[].class);
                if (!Modifier.isStatic(fxLauncherMethod.getModifiers())) {
                    LauncherHelper.abort(null, "java.launcher.javafx.error1", new Object[0]);
                }
                if (fxLauncherMethod.getReturnType() != Void.TYPE) {
                    LauncherHelper.abort(null, "java.launcher.javafx.error1", new Object[0]);
                }
            } catch (ClassNotFoundException | NoSuchMethodException e2) {
                LauncherHelper.abort(e2, "java.launcher.cls.error5", e2);
            }
            fxLaunchName = str;
            switch (i2) {
                case 1:
                    fxLaunchMode = "LM_CLASS";
                    return;
                case 2:
                    fxLaunchMode = "LM_JAR";
                    return;
                default:
                    throw new InternalError(i2 + ": Unknown launch mode");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean doesExtendFXApplication(Class<?> cls) {
            Class<? super Object> superclass = cls.getSuperclass();
            while (true) {
                Class<? super Object> cls2 = superclass;
                if (cls2 != null) {
                    if (!cls2.getName().equals(JAVAFX_APPLICATION_CLASS_NAME)) {
                        superclass = cls2.getSuperclass();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        public static void main(String... strArr) throws Exception {
            if (fxLauncherMethod == null || fxLaunchMode == null || fxLaunchName == null) {
                throw new RuntimeException("Invalid JavaFX launch parameters");
            }
            fxLauncherMethod.invoke(null, fxLaunchName, fxLaunchMode, strArr);
        }
    }
}
