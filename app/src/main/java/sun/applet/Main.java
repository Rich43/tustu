package sun.applet;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import sun.net.www.ParseUtil;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/applet/Main.class */
public class Main {
    static File theUserPropertiesFile;
    static final String[][] avDefaultUserProps = {new String[]{"http.proxyHost", ""}, new String[]{"http.proxyPort", "80"}, new String[]{"package.restrict.access.sun", "true"}};
    private static AppletMessageHandler amh;
    private boolean debugFlag = false;
    private boolean helpFlag = false;
    private String encoding = null;
    private boolean noSecurityFlag = false;
    private static boolean cmdLineTestFlag;
    private static Vector urlList;
    public static final String theVersion;

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.String[], java.lang.String[][]] */
    static {
        File file = new File(System.getProperty("user.home"));
        file.canWrite();
        theUserPropertiesFile = new File(file, ".appletviewer");
        amh = new AppletMessageHandler("appletviewer");
        cmdLineTestFlag = false;
        urlList = new Vector(1);
        theVersion = System.getProperty("java.version");
    }

    public static void main(String[] strArr) {
        int iRun = new Main().run(strArr);
        if (iRun != 0 || cmdLineTestFlag) {
            System.exit(iRun);
        }
    }

    private int run(String[] strArr) {
        try {
            if (strArr.length == 0) {
                usage();
                return 0;
            }
            int i2 = 0;
            while (i2 < strArr.length) {
                int iDecodeArg = decodeArg(strArr, i2);
                if (iDecodeArg == 0) {
                    throw new ParseException(lookup("main.err.unrecognizedarg", strArr[i2]));
                }
                i2 += iDecodeArg;
            }
            if (this.helpFlag) {
                usage();
                return 0;
            }
            if (urlList.size() == 0) {
                System.err.println(lookup("main.err.inputfile"));
                return 1;
            }
            if (this.debugFlag) {
                return invokeDebugger(strArr);
            }
            if (!this.noSecurityFlag && System.getSecurityManager() == null) {
                init();
            }
            for (int i3 = 0; i3 < urlList.size(); i3++) {
                try {
                    AppletViewer.parse((URL) urlList.elementAt(i3), this.encoding);
                } catch (IOException e2) {
                    System.err.println(lookup("main.err.io", e2.getMessage()));
                    return 1;
                }
            }
            return 0;
        } catch (ParseException e3) {
            System.err.println(e3.getMessage());
            return 1;
        }
    }

    private static void usage() {
        System.out.println(lookup("usage"));
    }

    private int decodeArg(String[] strArr, int i2) throws ParseException {
        String str = strArr[i2];
        int length = strArr.length;
        if ("-help".equalsIgnoreCase(str) || "-?".equals(str)) {
            this.helpFlag = true;
            return 1;
        }
        if ("-encoding".equals(str) && i2 < length - 1) {
            if (this.encoding != null) {
                throw new ParseException(lookup("main.err.dupoption", str));
            }
            this.encoding = strArr[i2 + 1];
            return 2;
        }
        if ("-debug".equals(str)) {
            this.debugFlag = true;
            return 1;
        }
        if ("-Xnosecurity".equals(str)) {
            System.err.println();
            System.err.println(lookup("main.warn.nosecmgr"));
            System.err.println();
            this.noSecurityFlag = true;
            return 1;
        }
        if ("-XcmdLineTest".equals(str)) {
            cmdLineTestFlag = true;
            return 1;
        }
        if (str.startsWith(LanguageTag.SEP)) {
            throw new ParseException(lookup("main.err.unsupportedopt", str));
        }
        URL url = parseURL(str);
        if (url != null) {
            urlList.addElement(url);
            return 1;
        }
        return 0;
    }

    private URL parseURL(String str) throws ParseException {
        URL url;
        try {
            if (str.indexOf(58) <= 1) {
                url = ParseUtil.fileToEncodedURL(new File(str));
            } else if (str.startsWith("file:") && str.length() != "file:".length() && !new File(str.substring("file:".length())).isAbsolute()) {
                url = new URL(DeploymentDescriptorParser.ATTR_FILE, "", ParseUtil.fileToEncodedURL(new File(System.getProperty("user.dir"))).getPath() + str.substring("file:".length()));
            } else {
                url = new URL(str);
            }
            return url;
        } catch (MalformedURLException e2) {
            throw new ParseException(lookup("main.err.badurl", str, e2.getMessage()));
        }
    }

    private int invokeDebugger(String[] strArr) throws IllegalArgumentException {
        String[] strArr2 = new String[strArr.length + 1];
        int i2 = 0 + 1;
        strArr2[0] = "-Djava.class.path=" + (System.getProperty("java.home") + File.separator + "phony");
        int i3 = i2 + 1;
        strArr2[i2] = "sun.applet.Main";
        for (int i4 = 0; i4 < strArr.length; i4++) {
            if (!"-debug".equals(strArr[i4])) {
                int i5 = i3;
                i3++;
                strArr2[i5] = strArr[i4];
            }
        }
        try {
            Class.forName("com.sun.tools.example.debug.tty.TTY", true, ClassLoader.getSystemClassLoader()).getDeclaredMethod("main", String[].class).invoke(null, strArr2);
            return 0;
        } catch (ClassNotFoundException e2) {
            System.err.println(lookup("main.debug.cantfinddebug"));
            return 1;
        } catch (IllegalAccessException e3) {
            System.err.println(lookup("main.debug.cantaccess"));
            return 1;
        } catch (NoSuchMethodException e4) {
            System.err.println(lookup("main.debug.cantfindmain"));
            return 1;
        } catch (InvocationTargetException e5) {
            System.err.println(lookup("main.debug.exceptionindebug"));
            return 1;
        }
    }

    private void init() {
        Properties aVProps = getAVProps();
        aVProps.put("browser", "sun.applet.AppletViewer");
        aVProps.put("browser.version", "1.06");
        aVProps.put("browser.vendor", "Oracle Corporation");
        aVProps.put("http.agent", "Java(tm) 2 SDK, Standard Edition v" + theVersion);
        aVProps.put("package.restrict.definition.java", "true");
        aVProps.put("package.restrict.definition.sun", "true");
        aVProps.put("java.version.applet", "true");
        aVProps.put("java.vendor.applet", "true");
        aVProps.put("java.vendor.url.applet", "true");
        aVProps.put("java.class.version.applet", "true");
        aVProps.put("os.name.applet", "true");
        aVProps.put("os.version.applet", "true");
        aVProps.put("os.arch.applet", "true");
        aVProps.put("file.separator.applet", "true");
        aVProps.put("path.separator.applet", "true");
        aVProps.put("line.separator.applet", "true");
        Properties properties = System.getProperties();
        Enumeration<?> enumerationPropertyNames = properties.propertyNames();
        while (enumerationPropertyNames.hasMoreElements()) {
            String str = (String) enumerationPropertyNames.nextElement2();
            String property = properties.getProperty(str);
            String str2 = (String) aVProps.setProperty(str, property);
            if (str2 != null) {
                System.err.println(lookup("main.warn.prop.overwrite", str, str2, property));
            }
        }
        System.setProperties(aVProps);
        if (!this.noSecurityFlag) {
            System.setSecurityManager(new AppletSecurity());
        } else {
            System.err.println(lookup("main.nosecmgr"));
        }
    }

    private Properties getAVProps() {
        Properties defaultAVProps;
        new Properties();
        File file = theUserPropertiesFile;
        if (file.exists()) {
            if (file.canRead()) {
                defaultAVProps = getAVProps(file);
            } else {
                System.err.println(lookup("main.warn.cantreadprops", file.toString()));
                defaultAVProps = setDefaultAVProps();
            }
        } else {
            File file2 = new File(new File(new File(System.getProperty("user.home")), ".hotjava"), "properties");
            if (file2.exists()) {
                defaultAVProps = getAVProps(file2);
            } else {
                System.err.println(lookup("main.warn.cantreadprops", file2.toString()));
                defaultAVProps = setDefaultAVProps();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                Throwable th = null;
                try {
                    try {
                        defaultAVProps.store(fileOutputStream, lookup("main.prop.store"));
                        if (fileOutputStream != null) {
                            if (0 != 0) {
                                try {
                                    fileOutputStream.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            } else {
                                fileOutputStream.close();
                            }
                        }
                    } finally {
                    }
                } finally {
                }
            } catch (IOException e2) {
                System.err.println(lookup("main.err.prop.cantsave", file.toString()));
            }
        }
        return defaultAVProps;
    }

    private Properties setDefaultAVProps() {
        Properties properties = new Properties();
        for (int i2 = 0; i2 < avDefaultUserProps.length; i2++) {
            properties.setProperty(avDefaultUserProps[i2][0], avDefaultUserProps[i2][1]);
        }
        return properties;
    }

    private Properties getAVProps(File file) {
        FileInputStream fileInputStream;
        Throwable th;
        Properties properties = new Properties();
        Properties properties2 = new Properties();
        try {
            fileInputStream = new FileInputStream(file);
            th = null;
        } catch (IOException e2) {
            System.err.println(lookup("main.err.prop.cantread", file.toString()));
        }
        try {
            try {
                properties2.load(new BufferedInputStream(fileInputStream));
                if (fileInputStream != null) {
                    if (0 != 0) {
                        try {
                            fileInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        fileInputStream.close();
                    }
                }
                for (int i2 = 0; i2 < avDefaultUserProps.length; i2++) {
                    String property = properties2.getProperty(avDefaultUserProps[i2][0]);
                    if (property != null) {
                        properties.setProperty(avDefaultUserProps[i2][0], property);
                    } else {
                        properties.setProperty(avDefaultUserProps[i2][0], avDefaultUserProps[i2][1]);
                    }
                }
                return properties;
            } finally {
            }
        } finally {
        }
    }

    private static String lookup(String str) {
        return amh.getMessage(str);
    }

    private static String lookup(String str, String str2) {
        return amh.getMessage(str, str2);
    }

    private static String lookup(String str, String str2, String str3) {
        return amh.getMessage(str, str2, str3);
    }

    private static String lookup(String str, String str2, String str3, String str4) {
        return amh.getMessage(str, str2, str3, str4);
    }

    /* loaded from: rt.jar:sun/applet/Main$ParseException.class */
    class ParseException extends RuntimeException {

        /* renamed from: t, reason: collision with root package name */
        Throwable f13539t;

        public ParseException(String str) {
            super(str);
            this.f13539t = null;
        }

        public ParseException(Throwable th) {
            super(th.getMessage());
            this.f13539t = null;
            this.f13539t = th;
        }
    }
}
