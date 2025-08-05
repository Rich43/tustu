package sun.awt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringBufferInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/DebugSettings.class */
final class DebugSettings {
    static final String PREFIX = "awtdebug";
    static final String PROP_FILE = "properties";
    private Properties props = new Properties();
    private static final PlatformLogger log = PlatformLogger.getLogger("sun.awt.debug.DebugSettings");
    private static final String[] DEFAULT_PROPS = {"awtdebug.assert=true", "awtdebug.trace=false", "awtdebug.on=true", "awtdebug.ctrace=false"};
    private static DebugSettings instance = null;
    private static final String PROP_CTRACE = "ctrace";
    private static final int PROP_CTRACE_LEN = PROP_CTRACE.length();

    private native synchronized void setCTracingOn(boolean z2);

    private native synchronized void setCTracingOn(boolean z2, String str);

    private native synchronized void setCTracingOn(boolean z2, String str, int i2);

    static void init() {
        if (instance != null) {
            return;
        }
        NativeLibLoader.loadLibraries();
        instance = new DebugSettings();
        instance.loadNativeSettings();
    }

    private DebugSettings() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.DebugSettings.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                DebugSettings.this.loadProperties();
                return null;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void loadProperties() {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.awt.DebugSettings.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                DebugSettings.this.loadDefaultProperties();
                DebugSettings.this.loadFileProperties();
                DebugSettings.this.loadSystemProperties();
                return null;
            }
        });
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("DebugSettings:\n{0}", this);
        }
    }

    public String toString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        for (String str : this.props.stringPropertyNames()) {
            printStream.println(str + " = " + this.props.getProperty(str, ""));
        }
        return new String(byteArrayOutputStream.toByteArray());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadDefaultProperties() {
        for (int i2 = 0; i2 < DEFAULT_PROPS.length; i2++) {
            try {
                StringBufferInputStream stringBufferInputStream = new StringBufferInputStream(DEFAULT_PROPS[i2]);
                this.props.load(stringBufferInputStream);
                stringBufferInputStream.close();
            } catch (IOException e2) {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadFileProperties() {
        String property = System.getProperty("awtdebug.properties", "");
        if (property.equals("")) {
            property = System.getProperty("user.home", "") + File.separator + PREFIX + "." + PROP_FILE;
        }
        File file = new File(property);
        try {
            println("Reading debug settings from '" + file.getCanonicalPath() + "'...");
            FileInputStream fileInputStream = new FileInputStream(file);
            this.props.load(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e2) {
            println("Did not find settings file.");
        } catch (IOException e3) {
            println("Problem reading settings, IOException: " + e3.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSystemProperties() {
        Properties properties = System.getProperties();
        for (String str : properties.stringPropertyNames()) {
            String property = properties.getProperty(str, "");
            if (str.startsWith(PREFIX)) {
                this.props.setProperty(str, property);
            }
        }
    }

    public synchronized boolean getBoolean(String str, boolean z2) {
        return getString(str, String.valueOf(z2)).equalsIgnoreCase("true");
    }

    public synchronized int getInt(String str, int i2) {
        return Integer.parseInt(getString(str, String.valueOf(i2)));
    }

    public synchronized String getString(String str, String str2) {
        return this.props.getProperty("awtdebug." + str, str2);
    }

    private synchronized List<String> getPropertyNames() {
        LinkedList linkedList = new LinkedList();
        Iterator<String> it = this.props.stringPropertyNames().iterator();
        while (it.hasNext()) {
            linkedList.add(it.next().substring(PREFIX.length() + 1));
        }
        return linkedList;
    }

    private void println(Object obj) {
        if (log.isLoggable(PlatformLogger.Level.FINER)) {
            log.finer(obj.toString());
        }
    }

    private void loadNativeSettings() {
        setCTracingOn(getBoolean(PROP_CTRACE, false));
        LinkedList<String> linkedList = new LinkedList();
        for (String str : getPropertyNames()) {
            if (str.startsWith(PROP_CTRACE) && str.length() > PROP_CTRACE_LEN) {
                linkedList.add(str);
            }
        }
        Collections.sort(linkedList);
        for (String str2 : linkedList) {
            String strSubstring = str2.substring(PROP_CTRACE_LEN + 1);
            int iIndexOf = strSubstring.indexOf(64);
            String strSubstring2 = iIndexOf != -1 ? strSubstring.substring(0, iIndexOf) : strSubstring;
            String strSubstring3 = iIndexOf != -1 ? strSubstring.substring(iIndexOf + 1) : "";
            boolean z2 = getBoolean(str2, false);
            if (strSubstring3.length() == 0) {
                setCTracingOn(z2, strSubstring2);
            } else {
                setCTracingOn(z2, strSubstring2, Integer.parseInt(strSubstring3, 10));
            }
        }
    }
}
