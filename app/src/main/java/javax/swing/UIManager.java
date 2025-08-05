package javax.swing;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.SwingPropertyChangeSupport;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalLookAndFeel;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.OSInfo;
import sun.awt.PaintEventDispatcher;
import sun.awt.SunToolkit;
import sun.awt.windows.WToolkit;
import sun.security.action.GetPropertyAction;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/UIManager.class */
public class UIManager implements Serializable {
    private static final Object classLock = new Object();
    private static final String defaultLAFKey = "swing.defaultlaf";
    private static final String auxiliaryLAFsKey = "swing.auxiliarylaf";
    private static final String multiplexingLAFKey = "swing.plaf.multiplexinglaf";
    private static final String installedLAFsKey = "swing.installedlafs";
    private static final String disableMnemonicKey = "swing.disablenavaids";
    private static LookAndFeelInfo[] installedLAFs;

    /* loaded from: rt.jar:javax/swing/UIManager$LAFState.class */
    private static class LAFState {
        Properties swingProps;
        private UIDefaults[] tables;
        boolean initialized;
        boolean focusPolicyInitialized;
        MultiUIDefaults multiUIDefaults;
        LookAndFeel lookAndFeel;
        LookAndFeel multiLookAndFeel;
        Vector<LookAndFeel> auxLookAndFeels;
        SwingPropertyChangeSupport changeSupport;
        LookAndFeelInfo[] installedLAFs;

        private LAFState() {
            this.tables = new UIDefaults[2];
            this.initialized = false;
            this.focusPolicyInitialized = false;
            this.multiUIDefaults = new MultiUIDefaults(this.tables);
            this.multiLookAndFeel = null;
            this.auxLookAndFeels = null;
        }

        UIDefaults getLookAndFeelDefaults() {
            return this.tables[0];
        }

        void setLookAndFeelDefaults(UIDefaults uIDefaults) {
            this.tables[0] = uIDefaults;
        }

        UIDefaults getSystemDefaults() {
            return this.tables[1];
        }

        void setSystemDefaults(UIDefaults uIDefaults) {
            this.tables[1] = uIDefaults;
        }

        public synchronized SwingPropertyChangeSupport getPropertyChangeSupport(boolean z2) {
            if (z2 && this.changeSupport == null) {
                this.changeSupport = new SwingPropertyChangeSupport(UIManager.class);
            }
            return this.changeSupport;
        }
    }

    static {
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new LookAndFeelInfo("Metal", "javax.swing.plaf.metal.MetalLookAndFeel"));
        arrayList.add(new LookAndFeelInfo("Nimbus", "javax.swing.plaf.nimbus.NimbusLookAndFeel"));
        arrayList.add(new LookAndFeelInfo("CDE/Motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
        OSInfo.OSType oSType = (OSInfo.OSType) AccessController.doPrivileged(OSInfo.getOSTypeAction());
        if (oSType == OSInfo.OSType.WINDOWS) {
            arrayList.add(new LookAndFeelInfo("Windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
            if (Toolkit.getDefaultToolkit().getDesktopProperty(WToolkit.XPSTYLE_THEME_ACTIVE) != null) {
                arrayList.add(new LookAndFeelInfo("Windows Classic", "com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel"));
            }
        } else if (oSType == OSInfo.OSType.MACOSX) {
            arrayList.add(new LookAndFeelInfo("Mac OS X", "com.apple.laf.AquaLookAndFeel"));
        } else {
            arrayList.add(new LookAndFeelInfo("GTK+", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"));
        }
        installedLAFs = (LookAndFeelInfo[]) arrayList.toArray(new LookAndFeelInfo[arrayList.size()]);
    }

    private static LAFState getLAFState() {
        LAFState lAFState = (LAFState) SwingUtilities.appContextGet(SwingUtilities2.LAF_STATE_KEY);
        if (lAFState == null) {
            synchronized (classLock) {
                lAFState = (LAFState) SwingUtilities.appContextGet(SwingUtilities2.LAF_STATE_KEY);
                if (lAFState == null) {
                    Object obj = SwingUtilities2.LAF_STATE_KEY;
                    LAFState lAFState2 = new LAFState();
                    lAFState = lAFState2;
                    SwingUtilities.appContextPut(obj, lAFState2);
                }
            }
        }
        return lAFState;
    }

    private static String makeInstalledLAFKey(String str, String str2) {
        return "swing.installedlaf." + str + "." + str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String makeSwingPropertiesFilename() {
        String str = File.separator;
        String property = System.getProperty("java.home");
        if (property == null) {
            property = "<java.home undefined>";
        }
        return property + str + "lib" + str + "swing.properties";
    }

    /* loaded from: rt.jar:javax/swing/UIManager$LookAndFeelInfo.class */
    public static class LookAndFeelInfo {
        private String name;
        private String className;

        public LookAndFeelInfo(String str, String str2) {
            this.name = str;
            this.className = str2;
        }

        public String getName() {
            return this.name;
        }

        public String getClassName() {
            return this.className;
        }

        public String toString() {
            return getClass().getName() + "[" + getName() + " " + getClassName() + "]";
        }
    }

    public static LookAndFeelInfo[] getInstalledLookAndFeels() {
        maybeInitialize();
        LookAndFeelInfo[] lookAndFeelInfoArr = getLAFState().installedLAFs;
        if (lookAndFeelInfoArr == null) {
            lookAndFeelInfoArr = installedLAFs;
        }
        LookAndFeelInfo[] lookAndFeelInfoArr2 = new LookAndFeelInfo[lookAndFeelInfoArr.length];
        System.arraycopy(lookAndFeelInfoArr, 0, lookAndFeelInfoArr2, 0, lookAndFeelInfoArr.length);
        return lookAndFeelInfoArr2;
    }

    public static void setInstalledLookAndFeels(LookAndFeelInfo[] lookAndFeelInfoArr) throws SecurityException {
        maybeInitialize();
        LookAndFeelInfo[] lookAndFeelInfoArr2 = new LookAndFeelInfo[lookAndFeelInfoArr.length];
        System.arraycopy(lookAndFeelInfoArr, 0, lookAndFeelInfoArr2, 0, lookAndFeelInfoArr.length);
        getLAFState().installedLAFs = lookAndFeelInfoArr2;
    }

    public static void installLookAndFeel(LookAndFeelInfo lookAndFeelInfo) throws SecurityException {
        LookAndFeelInfo[] installedLookAndFeels = getInstalledLookAndFeels();
        LookAndFeelInfo[] lookAndFeelInfoArr = new LookAndFeelInfo[installedLookAndFeels.length + 1];
        System.arraycopy(installedLookAndFeels, 0, lookAndFeelInfoArr, 0, installedLookAndFeels.length);
        lookAndFeelInfoArr[installedLookAndFeels.length] = lookAndFeelInfo;
        setInstalledLookAndFeels(lookAndFeelInfoArr);
    }

    public static void installLookAndFeel(String str, String str2) throws SecurityException {
        installLookAndFeel(new LookAndFeelInfo(str, str2));
    }

    public static LookAndFeel getLookAndFeel() {
        maybeInitialize();
        return getLAFState().lookAndFeel;
    }

    public static void setLookAndFeel(LookAndFeel lookAndFeel) throws UnsupportedLookAndFeelException {
        if (lookAndFeel != null && !lookAndFeel.isSupportedLookAndFeel()) {
            throw new UnsupportedLookAndFeelException(lookAndFeel.toString() + " not supported on this platform");
        }
        LAFState lAFState = getLAFState();
        LookAndFeel lookAndFeel2 = lAFState.lookAndFeel;
        if (lookAndFeel2 != null) {
            lookAndFeel2.uninitialize();
        }
        lAFState.lookAndFeel = lookAndFeel;
        if (lookAndFeel != null) {
            DefaultLookup.setDefaultLookup(null);
            lookAndFeel.initialize();
            lAFState.setLookAndFeelDefaults(lookAndFeel.getDefaults());
        } else {
            lAFState.setLookAndFeelDefaults(null);
        }
        SwingPropertyChangeSupport propertyChangeSupport = lAFState.getPropertyChangeSupport(false);
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange("lookAndFeel", lookAndFeel2, lookAndFeel);
        }
    }

    public static void setLookAndFeel(String str) throws IllegalAccessException, UnsupportedLookAndFeelException, InstantiationException, ClassNotFoundException {
        if ("javax.swing.plaf.metal.MetalLookAndFeel".equals(str)) {
            setLookAndFeel(new MetalLookAndFeel());
        } else {
            setLookAndFeel((LookAndFeel) SwingUtilities.loadSystemClass(str).newInstance());
        }
    }

    public static String getSystemLookAndFeelClassName() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("swing.systemlaf"));
        if (str != null) {
            return str;
        }
        OSInfo.OSType oSType = (OSInfo.OSType) AccessController.doPrivileged(OSInfo.getOSTypeAction());
        if (oSType == OSInfo.OSType.WINDOWS) {
            return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        }
        String str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.desktop"));
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if ("gnome".equals(str2) && (defaultToolkit instanceof SunToolkit) && ((SunToolkit) defaultToolkit).isNativeGTKAvailable()) {
            return "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        }
        if (oSType == OSInfo.OSType.MACOSX && defaultToolkit.getClass().getName().equals("sun.lwawt.macosx.LWCToolkit")) {
            return "com.apple.laf.AquaLookAndFeel";
        }
        if (oSType == OSInfo.OSType.SOLARIS) {
            return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        }
        return getCrossPlatformLookAndFeelClassName();
    }

    public static String getCrossPlatformLookAndFeelClassName() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("swing.crossplatformlaf"));
        if (str != null) {
            return str;
        }
        return "javax.swing.plaf.metal.MetalLookAndFeel";
    }

    public static UIDefaults getDefaults() {
        maybeInitialize();
        return getLAFState().multiUIDefaults;
    }

    public static Font getFont(Object obj) {
        return getDefaults().getFont(obj);
    }

    public static Font getFont(Object obj, Locale locale) {
        return getDefaults().getFont(obj, locale);
    }

    public static Color getColor(Object obj) {
        return getDefaults().getColor(obj);
    }

    public static Color getColor(Object obj, Locale locale) {
        return getDefaults().getColor(obj, locale);
    }

    public static Icon getIcon(Object obj) {
        return getDefaults().getIcon(obj);
    }

    public static Icon getIcon(Object obj, Locale locale) {
        return getDefaults().getIcon(obj, locale);
    }

    public static Border getBorder(Object obj) {
        return getDefaults().getBorder(obj);
    }

    public static Border getBorder(Object obj, Locale locale) {
        return getDefaults().getBorder(obj, locale);
    }

    public static String getString(Object obj) {
        return getDefaults().getString(obj);
    }

    public static String getString(Object obj, Locale locale) {
        return getDefaults().getString(obj, locale);
    }

    static String getString(Object obj, Component component) {
        return getString(obj, component == null ? Locale.getDefault() : component.getLocale());
    }

    public static int getInt(Object obj) {
        return getDefaults().getInt(obj);
    }

    public static int getInt(Object obj, Locale locale) {
        return getDefaults().getInt(obj, locale);
    }

    public static boolean getBoolean(Object obj) {
        return getDefaults().getBoolean(obj);
    }

    public static boolean getBoolean(Object obj, Locale locale) {
        return getDefaults().getBoolean(obj, locale);
    }

    public static Insets getInsets(Object obj) {
        return getDefaults().getInsets(obj);
    }

    public static Insets getInsets(Object obj, Locale locale) {
        return getDefaults().getInsets(obj, locale);
    }

    public static Dimension getDimension(Object obj) {
        return getDefaults().getDimension(obj);
    }

    public static Dimension getDimension(Object obj, Locale locale) {
        return getDefaults().getDimension(obj, locale);
    }

    public static Object get(Object obj) {
        return getDefaults().get(obj);
    }

    public static Object get(Object obj, Locale locale) {
        return getDefaults().get(obj, locale);
    }

    public static Object put(Object obj, Object obj2) {
        return getDefaults().put(obj, obj2);
    }

    public static ComponentUI getUI(JComponent jComponent) {
        maybeInitialize();
        maybeInitializeFocusPolicy(jComponent);
        ComponentUI ui = null;
        LookAndFeel lookAndFeel = getLAFState().multiLookAndFeel;
        if (lookAndFeel != null) {
            ui = lookAndFeel.getDefaults().getUI(jComponent);
        }
        if (ui == null) {
            ui = getDefaults().getUI(jComponent);
        }
        return ui;
    }

    public static UIDefaults getLookAndFeelDefaults() {
        maybeInitialize();
        return getLAFState().getLookAndFeelDefaults();
    }

    private static LookAndFeel getMultiLookAndFeel() {
        LookAndFeel lookAndFeel = getLAFState().multiLookAndFeel;
        if (lookAndFeel == null) {
            String property = getLAFState().swingProps.getProperty(multiplexingLAFKey, "javax.swing.plaf.multi.MultiLookAndFeel");
            try {
                lookAndFeel = (LookAndFeel) SwingUtilities.loadSystemClass(property).newInstance();
            } catch (Exception e2) {
                System.err.println("UIManager: failed loading " + property);
            }
        }
        return lookAndFeel;
    }

    public static void addAuxiliaryLookAndFeel(LookAndFeel lookAndFeel) {
        maybeInitialize();
        if (!lookAndFeel.isSupportedLookAndFeel()) {
            return;
        }
        Vector<LookAndFeel> vector = getLAFState().auxLookAndFeels;
        if (vector == null) {
            vector = new Vector<>();
        }
        if (!vector.contains(lookAndFeel)) {
            vector.addElement(lookAndFeel);
            lookAndFeel.initialize();
            getLAFState().auxLookAndFeels = vector;
            if (getLAFState().multiLookAndFeel == null) {
                getLAFState().multiLookAndFeel = getMultiLookAndFeel();
            }
        }
    }

    public static boolean removeAuxiliaryLookAndFeel(LookAndFeel lookAndFeel) {
        maybeInitialize();
        Vector<LookAndFeel> vector = getLAFState().auxLookAndFeels;
        if (vector == null || vector.size() == 0) {
            return false;
        }
        boolean zRemoveElement = vector.removeElement(lookAndFeel);
        if (zRemoveElement) {
            if (vector.size() == 0) {
                getLAFState().auxLookAndFeels = null;
                getLAFState().multiLookAndFeel = null;
            } else {
                getLAFState().auxLookAndFeels = vector;
            }
        }
        lookAndFeel.uninitialize();
        return zRemoveElement;
    }

    public static LookAndFeel[] getAuxiliaryLookAndFeels() {
        maybeInitialize();
        Vector<LookAndFeel> vector = getLAFState().auxLookAndFeels;
        if (vector == null || vector.size() == 0) {
            return null;
        }
        LookAndFeel[] lookAndFeelArr = new LookAndFeel[vector.size()];
        for (int i2 = 0; i2 < lookAndFeelArr.length; i2++) {
            lookAndFeelArr[i2] = vector.elementAt(i2);
        }
        return lookAndFeelArr;
    }

    public static void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        synchronized (classLock) {
            getLAFState().getPropertyChangeSupport(true).addPropertyChangeListener(propertyChangeListener);
        }
    }

    public static void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        synchronized (classLock) {
            getLAFState().getPropertyChangeSupport(true).removePropertyChangeListener(propertyChangeListener);
        }
    }

    public static PropertyChangeListener[] getPropertyChangeListeners() {
        PropertyChangeListener[] propertyChangeListeners;
        synchronized (classLock) {
            propertyChangeListeners = getLAFState().getPropertyChangeSupport(true).getPropertyChangeListeners();
        }
        return propertyChangeListeners;
    }

    private static Properties loadSwingProperties() {
        if (UIManager.class.getClassLoader() != null) {
            return new Properties();
        }
        final Properties properties = new Properties();
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javax.swing.UIManager.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                if (((OSInfo.OSType) AccessController.doPrivileged(OSInfo.getOSTypeAction())) == OSInfo.OSType.MACOSX) {
                    properties.put(UIManager.defaultLAFKey, UIManager.getSystemLookAndFeelClassName());
                }
                try {
                    File file = new File(UIManager.makeSwingPropertiesFilename());
                    if (file.exists()) {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        properties.load(fileInputStream);
                        fileInputStream.close();
                    }
                } catch (Exception e2) {
                }
                UIManager.checkProperty(properties, UIManager.defaultLAFKey);
                UIManager.checkProperty(properties, UIManager.auxiliaryLAFsKey);
                UIManager.checkProperty(properties, UIManager.multiplexingLAFKey);
                UIManager.checkProperty(properties, UIManager.installedLAFsKey);
                UIManager.checkProperty(properties, UIManager.disableMnemonicKey);
                return null;
            }
        });
        return properties;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void checkProperty(Properties properties, String str) {
        String property = System.getProperty(str);
        if (property != null) {
            properties.put(str, property);
        }
    }

    private static void initializeInstalledLAFs(Properties properties) {
        String property = properties.getProperty(installedLAFsKey);
        if (property == null) {
            return;
        }
        Vector vector = new Vector();
        StringTokenizer stringTokenizer = new StringTokenizer(property, ",", false);
        while (stringTokenizer.hasMoreTokens()) {
            vector.addElement(stringTokenizer.nextToken());
        }
        Vector vector2 = new Vector(vector.size());
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            String property2 = properties.getProperty(makeInstalledLAFKey(str, "name"), str);
            String property3 = properties.getProperty(makeInstalledLAFKey(str, Constants.ATTRNAME_CLASS));
            if (property3 != null) {
                vector2.addElement(new LookAndFeelInfo(property2, property3));
            }
        }
        LookAndFeelInfo[] lookAndFeelInfoArr = new LookAndFeelInfo[vector2.size()];
        for (int i2 = 0; i2 < vector2.size(); i2++) {
            lookAndFeelInfoArr[i2] = (LookAndFeelInfo) vector2.elementAt(i2);
        }
        getLAFState().installedLAFs = lookAndFeelInfoArr;
    }

    private static void initializeDefaultLAF(Properties properties) {
        if (getLAFState().lookAndFeel != null) {
            return;
        }
        String crossPlatformLookAndFeelClassName = null;
        HashMap map = (HashMap) AppContext.getAppContext().remove("swing.lafdata");
        if (map != null) {
            crossPlatformLookAndFeelClassName = (String) map.remove("defaultlaf");
        }
        if (crossPlatformLookAndFeelClassName == null) {
            crossPlatformLookAndFeelClassName = getCrossPlatformLookAndFeelClassName();
        }
        String property = properties.getProperty(defaultLAFKey, crossPlatformLookAndFeelClassName);
        try {
            setLookAndFeel(property);
            if (map != null) {
                for (Object obj : map.keySet()) {
                    put(obj, map.get(obj));
                }
            }
        } catch (Exception e2) {
            throw new Error("Cannot load " + property);
        }
    }

    private static void initializeAuxiliaryLAFs(Properties properties) {
        String property = properties.getProperty(auxiliaryLAFsKey);
        if (property == null) {
            return;
        }
        Vector<LookAndFeel> vector = new Vector<>();
        StringTokenizer stringTokenizer = new StringTokenizer(property, ",");
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            try {
                LookAndFeel lookAndFeel = (LookAndFeel) SwingUtilities.loadSystemClass(strNextToken).newInstance();
                lookAndFeel.initialize();
                vector.addElement(lookAndFeel);
            } catch (Exception e2) {
                System.err.println("UIManager: failed loading auxiliary look and feel " + strNextToken);
            }
        }
        if (vector.size() == 0) {
            vector = null;
        } else {
            getLAFState().multiLookAndFeel = getMultiLookAndFeel();
            if (getLAFState().multiLookAndFeel == null) {
                vector = null;
            }
        }
        getLAFState().auxLookAndFeels = vector;
    }

    private static void initializeSystemDefaults(Properties properties) {
        getLAFState().swingProps = properties;
    }

    private static void maybeInitialize() {
        synchronized (classLock) {
            if (!getLAFState().initialized) {
                getLAFState().initialized = true;
                initialize();
            }
        }
    }

    private static void maybeInitializeFocusPolicy(JComponent jComponent) {
        if (jComponent instanceof JRootPane) {
            synchronized (classLock) {
                if (!getLAFState().focusPolicyInitialized) {
                    getLAFState().focusPolicyInitialized = true;
                    if (FocusManager.isFocusManagerEnabled()) {
                        KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
                    }
                }
            }
        }
    }

    private static void initialize() {
        Properties propertiesLoadSwingProperties = loadSwingProperties();
        initializeSystemDefaults(propertiesLoadSwingProperties);
        initializeDefaultLAF(propertiesLoadSwingProperties);
        initializeAuxiliaryLAFs(propertiesLoadSwingProperties);
        initializeInstalledLAFs(propertiesLoadSwingProperties);
        if (RepaintManager.HANDLE_TOP_LEVEL_PAINT) {
            PaintEventDispatcher.setPaintEventDispatcher(new SwingPaintEventDispatcher());
        }
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyEventPostProcessor() { // from class: javax.swing.UIManager.2
            @Override // java.awt.KeyEventPostProcessor
            public boolean postProcessKeyEvent(KeyEvent keyEvent) {
                Component component = keyEvent.getComponent();
                if ((!(component instanceof JComponent) || (component != null && !component.isEnabled())) && JComponent.KeyboardState.shouldProcess(keyEvent) && SwingUtilities.processKeyBindings(keyEvent)) {
                    keyEvent.consume();
                    return true;
                }
                return false;
            }
        });
        AWTAccessor.getComponentAccessor().setRequestFocusController(JComponent.focusController);
    }
}
