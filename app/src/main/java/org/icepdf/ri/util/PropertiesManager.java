package org.icepdf.ri.util;

import java.awt.HeadlessException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Document;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/PropertiesManager.class */
public class PropertiesManager {
    private static final Logger logger = Logger.getLogger(PropertiesManager.class.toString());
    private static final String DEFAULT_HOME_DIR = ".icesoft/icepdf_viewer";
    private static final String LOCK_FILE = "_syslock";
    private static final String USER_FILENAME = "pdfviewerri.properties";
    private static final String BACKUP_FILENAME = "old_pdfviewerri.properties";
    private static final String DEFAULT_PROP_FILE = "ICEpdfDefault.properties";
    private static final String DEFAULT_PROP_FILE_PATH = "org/icepdf/ri/viewer/res/";
    public static final String DEFAULT_MESSAGE_BUNDLE = "org.icepdf.ri.resources.MessageBundle";
    private static final String PROPERTY_DEFAULT_FILE_PATH = "application.defaultFilePath";
    private static final String PROPERTY_DEFAULT_URL = "application.defaultURL";
    public static final String PROPERTY_DIVIDER_LOCATION = "application.divider.location";
    public static final String PROPERTY_DEFAULT_PAGEFIT = "document.pagefitMode";
    public static final String PROPERTY_PRINT_MEDIA_SIZE_WIDTH = "document.print.mediaSize.width";
    public static final String PROPERTY_PRINT_MEDIA_SIZE_HEIGHT = "document.print.mediaSize.height";
    public static final String PROPERTY_PRINT_MEDIA_SIZE_UNIT = "document.print.mediaSize.unit";
    public static final String SYSPROPERTY_HIGHLIGHT_COLOR = "org.icepdf.core.views.page.text.highlightColor";
    public static final String PROPERTY_SHOW_TOOLBAR_UTILITY = "application.toolbar.show.utility";
    public static final String PROPERTY_SHOW_TOOLBAR_PAGENAV = "application.toolbar.show.pagenav";
    public static final String PROPERTY_SHOW_TOOLBAR_ZOOM = "application.toolbar.show.zoom";
    public static final String PROPERTY_SHOW_TOOLBAR_FIT = "application.toolbar.show.fit";
    public static final String PROPERTY_SHOW_TOOLBAR_ROTATE = "application.toolbar.show.rotate";
    public static final String PROPERTY_SHOW_TOOLBAR_TOOL = "application.toolbar.show.tool";
    public static final String PROPERTY_SHOW_TOOLBAR_ANNOTATION = "application.toolbar.show.annotation";
    public static final String PROPERTY_SHOW_STATUSBAR = "application.statusbar";
    public static final String PROPERTY_SHOW_STATUSBAR_STATUSLABEL = "application.statusbar.show.statuslabel";
    public static final String PROPERTY_SHOW_STATUSBAR_VIEWMODE = "application.statusbar.show.viewmode";
    public static final String PROPERTY_SHOW_UTILITY_OPEN = "application.toolbar.show.utility.open";
    public static final String PROPERTY_SHOW_UTILITY_SAVE = "application.toolbar.show.utility.save";
    public static final String PROPERTY_SHOW_UTILITY_PRINT = "application.toolbar.show.utility.print";
    public static final String PROPERTY_SHOW_UTILITY_SEARCH = "application.toolbar.show.utility.search";
    public static final String PROPERTY_SHOW_UTILITY_UPANE = "application.toolbar.show.utility.upane";
    public static final String PROPERTY_HIDE_UTILITYPANE = "application.utilitypane.hide";
    public static final String PROPERTY_SHOW_UTILITYPANE_BOOKMARKS = "application.utilitypane.show.bookmarks";
    public static final String PROPERTY_SHOW_UTILITYPANE_SEARCH = "application.utilitypane.show.search";
    public static final String PROPERTY_SHOW_UTILITYPANE_THUMBNAILS = "application.utilitypane.show.thumbs";
    public static final String PROPERTY_SHOW_UTILITYPANE_LAYERS = "application.utilitypane.show.layers";
    public static final String PROPERTY_SHOW_UTILITYPANE_ANNOTATION = "application.utilitypane.show.annotation";
    public static final String PROPERTY_SHOW_UTILITYPANE_ANNOTATION_FLAGS = "application.utilitypane.show.annotation.flags";
    public static final String PROPERTY_UTILITYPANE_THUMBNAILS_ZOOM = "application.utilitypane.thumbnail.zoom";
    public static final String PROPERTY_DEFAULT_ZOOM_LEVEL = "application.zoom.factor.default";
    public static final String PROPERTY_ZOOM_RANGES = "application.zoom.range.default";
    public static final String PROPERTY_SHOW_KEYBOARD_SHORTCUTS = "application.menuitem.show.keyboard.shortcuts";
    public static final String PROPERTY_VIEWPREF_HIDETOOLBAR = "application.viewerpreferences.hidetoolbar";
    public static final String PROPERTY_VIEWPREF_HIDEMENUBAR = "application.viewerpreferences.hidemenubar";
    public static final String PROPERTY_VIEWPREF_FITWINDOW = "application.viewerpreferences.fitwindow";
    String versionName;
    private boolean unrecoverableError;
    Properties sysProps;
    private ResourceBundle messageBundle;
    File userHome;
    private File dataDir;
    private File lockDir;
    private File propertyFile;
    private Date myLastModif;
    private Properties props;
    private Properties defaultProps;
    private boolean userRejectedCreatingLocalDataDir;
    private boolean thisExecutionTriedCreatingLocalDataDir;

    public PropertiesManager(Properties sysProps, ResourceBundle messageBundle) {
        this(sysProps, new Properties(), messageBundle);
    }

    public PropertiesManager(Properties sysProps, Properties props, ResourceBundle messageBundle) throws HeadlessException {
        this.versionName = Document.getLibraryVersion();
        this.myLastModif = new Date();
        this.unrecoverableError = true;
        this.sysProps = sysProps;
        this.messageBundle = messageBundle;
        if (!setupDefaultProperties()) {
            return;
        }
        if (props != null) {
            Enumeration keys = props.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement2();
                this.props.setProperty(key, props.getProperty(key));
            }
        }
        setupHomeDir(null);
        loadProperties();
        recordMofifTime();
        setupLock();
        this.unrecoverableError = false;
    }

    public PropertiesManager(Properties sysProps, String propPath, ResourceBundle messageBundle) throws HeadlessException {
        this.versionName = Document.getLibraryVersion();
        this.myLastModif = new Date();
        this.unrecoverableError = true;
        this.sysProps = sysProps;
        this.messageBundle = messageBundle;
        if (!setupDefaultProperties()) {
            return;
        }
        if (propPath != null) {
            this.propertyFile = new File(propPath);
            loadProperties();
        }
        setupHomeDir(null);
        recordMofifTime();
        setupLock();
        if (propPath == null) {
            loadProperties();
        }
        this.unrecoverableError = false;
    }

    private boolean setupDefaultProperties() throws HeadlessException {
        this.defaultProps = new Properties();
        try {
            InputStream in = getResourceAsStream(DEFAULT_PROP_FILE_PATH, DEFAULT_PROP_FILE);
            try {
                this.defaultProps.load(in);
                in.close();
                this.props = this.defaultProps;
                return true;
            } catch (Throwable th) {
                in.close();
                throw th;
            }
        } catch (IOException ex) {
            if (getBoolean("application.showLocalStorageDialogs", true)) {
                Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.session.readError", DEFAULT_PROP_FILE);
            }
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Error loading default properties cache", (Throwable) ex);
                return false;
            }
            return false;
        }
    }

    boolean hasUserRejectedCreatingLocalDataDir() {
        return this.userRejectedCreatingLocalDataDir;
    }

    void setUserRejectedCreatingLocalDataDir() {
        this.userRejectedCreatingLocalDataDir = true;
    }

    boolean unrecoverableError() {
        return this.unrecoverableError;
    }

    private boolean ownLock() {
        return this.lockDir != null;
    }

    private void setupHomeDir(String homeString) throws HeadlessException {
        boolean create;
        if (homeString == null) {
            homeString = this.sysProps.getProperty("swingri.home");
        }
        if (homeString != null) {
            this.dataDir = new File(homeString);
        } else {
            this.userHome = new File(this.sysProps.getProperty("user.home"));
            String dataDirStr = this.props.getProperty("application.datadir", DEFAULT_HOME_DIR);
            this.dataDir = new File(this.userHome, dataDirStr);
        }
        if (!this.dataDir.isDirectory()) {
            String path = this.dataDir.getAbsolutePath();
            if (hasUserRejectedCreatingLocalDataDir()) {
                create = false;
            } else if (getBoolean("application.showLocalStorageDialogs", true)) {
                create = Resources.showConfirmDialog(null, this.messageBundle, "manager.properties.title", "manager.properties.createNewDirectory", path);
                if (!create) {
                    setUserRejectedCreatingLocalDataDir();
                }
            } else {
                create = true;
            }
            if (!create) {
                this.dataDir = null;
                return;
            }
            this.dataDir.mkdirs();
            if (!this.dataDir.isDirectory()) {
                if (getBoolean("application.showLocalStorageDialogs", true)) {
                    Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.failedCreation", this.dataDir.getAbsolutePath());
                }
                this.dataDir = null;
            }
            this.thisExecutionTriedCreatingLocalDataDir = true;
        }
    }

    private void setupLock() throws HeadlessException {
        if (this.dataDir == null) {
            this.lockDir = null;
            return;
        }
        File dir = new File(this.dataDir, LOCK_FILE);
        if (!dir.mkdir()) {
            dir.delete();
            if (!dir.mkdir()) {
                dir = null;
                if (getBoolean("application.showLocalStorageDialogs", true)) {
                    Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.session.nolock", LOCK_FILE);
                }
            }
        }
        this.lockDir = dir;
    }

    private boolean checkPropertyFileValid(File toCheck) {
        return toCheck != null && toCheck.exists() && toCheck.canRead();
    }

    public synchronized void loadProperties() throws HeadlessException {
        if (!checkPropertyFileValid(this.propertyFile) && this.dataDir != null) {
            this.propertyFile = new File(this.dataDir, USER_FILENAME);
        }
        if (checkPropertyFileValid(this.propertyFile)) {
            try {
                InputStream in = new FileInputStream(this.propertyFile);
                try {
                    this.props.load(in);
                    in.close();
                } catch (Throwable th) {
                    in.close();
                    throw th;
                }
            } catch (IOException ex) {
                if (getBoolean("application.showLocalStorageDialogs", true)) {
                    Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.session.readError", this.propertyFile.getAbsolutePath());
                }
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Error loading properties cache", (Throwable) ex);
                }
            }
        }
    }

    public synchronized void saveAndEnd() throws HeadlessException {
        if (this.dataDir != null) {
            saveProperties();
            this.lockDir.delete();
        }
    }

    public synchronized void saveProperties() throws HeadlessException {
        if (ownLock()) {
            long lastModified = this.propertyFile.lastModified();
            boolean saveIt = true;
            if (this.thisExecutionTriedCreatingLocalDataDir) {
                saveIt = true;
            } else if (getBoolean("application.showLocalStorageDialogs", true)) {
                if (lastModified == 0) {
                    saveIt = Resources.showConfirmDialog(null, this.messageBundle, "manager.properties.title", "manager.properties.deleted", this.propertyFile.getAbsolutePath());
                } else if (this.myLastModif.before(new Date(lastModified))) {
                    saveIt = Resources.showConfirmDialog(null, this.messageBundle, "manager.properties.title", "manager.properties.modified", this.myLastModif);
                }
            }
            if (!saveIt) {
                return;
            }
            try {
                FileOutputStream out = new FileOutputStream(this.propertyFile);
                try {
                    this.props.store(out, "-- ICEpdf properties --");
                    out.close();
                    recordMofifTime();
                } catch (Throwable th) {
                    out.close();
                    throw th;
                }
            } catch (IOException ex) {
                if (getBoolean("application.showLocalStorageDialogs", true)) {
                    Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.saveError", ex);
                }
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Error saving properties cache", (Throwable) ex);
                }
            }
        }
    }

    private void recordMofifTime() {
        Calendar c2 = new GregorianCalendar();
        c2.setTime(new Date());
        c2.set(12, c2.get(12) + 1);
        c2.set(13, 0);
        this.myLastModif = new Date((c2.getTime().getTime() / 1000) * 1000);
    }

    public boolean backupProperties() throws HeadlessException {
        boolean result = false;
        if (ownLock()) {
            File backupFile = new File(this.dataDir, BACKUP_FILENAME);
            try {
                FileOutputStream out = new FileOutputStream(backupFile);
                try {
                    this.props.store(out, "-- ICEbrowser properties backup --");
                    result = true;
                    out.close();
                } catch (Throwable th) {
                    out.close();
                    throw th;
                }
            } catch (IOException ex) {
                if (getBoolean("application.showLocalStorageDialogs", true)) {
                    Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.saveError", ex);
                }
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Error saving properties cache", (Throwable) ex);
                }
            }
        }
        return result;
    }

    public void set(String propertyName, String value) {
        this.props.put(propertyName, value);
    }

    public void remove(String propertyName) {
        this.props.remove(propertyName);
    }

    public String getString(String propertyName, String defaultValue) {
        String value = (String) this.props.get(propertyName);
        if (value != null) {
            return value.trim();
        }
        String value2 = (String) this.defaultProps.get(propertyName);
        if (value2 != null) {
            return value2.trim();
        }
        return defaultValue;
    }

    public String getString(String propertyName) throws HeadlessException {
        String value = getString(propertyName, null);
        if (value == null) {
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.missingProperty", propertyName, value);
        }
        return value;
    }

    public int getInt(String propertyName, int defaultValue) throws HeadlessException {
        Integer result = getIntImpl(propertyName);
        if (result == null) {
            return defaultValue;
        }
        return result.intValue();
    }

    public int getInt(String propertyName) throws HeadlessException {
        Integer result = getIntImpl(propertyName);
        if (result == null) {
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.missingProperty", propertyName, result);
            return 0;
        }
        return result.intValue();
    }

    private Integer getIntImpl(String propertyName) throws HeadlessException {
        String value = (String) this.props.get(propertyName);
        if (value != null) {
            Integer result = Parse.parseInteger(value, this.messageBundle);
            if (result != null) {
                return result;
            }
            this.props.remove(propertyName);
        }
        String value2 = (String) this.defaultProps.get(propertyName);
        if (value2 != null) {
            Integer result2 = Parse.parseInteger(value2, null);
            if (result2 != null) {
                return result2;
            }
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.brokenProperty ", propertyName, value2);
            return null;
        }
        return null;
    }

    public void setInt(String propertyName, int value) {
        set(propertyName, Integer.toString(value));
    }

    public double getDouble(String propertyName, double defaultValue) throws HeadlessException {
        Double result = getDoubleImpl(propertyName);
        if (result == null) {
            return defaultValue;
        }
        return result.doubleValue();
    }

    public double getDouble(String propertyName) throws HeadlessException {
        Double result = getDoubleImpl(propertyName);
        if (result == null) {
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.missingProperty", propertyName, result);
            return 0.0d;
        }
        return result.doubleValue();
    }

    public float getFloat(String propertyName) throws HeadlessException {
        Float result = getFloatImpl(propertyName);
        if (result == null) {
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.missingProperty", propertyName, result);
            return 0.0f;
        }
        return result.floatValue();
    }

    private Double getDoubleImpl(String propertyName) throws HeadlessException {
        String value = (String) this.props.get(propertyName);
        if (value != null) {
            Double result = Parse.parseDouble(value, this.messageBundle);
            if (result != null) {
                return result;
            }
            this.props.remove(propertyName);
        }
        String value2 = (String) this.defaultProps.get(propertyName);
        if (value2 != null) {
            Double result2 = Parse.parseDouble(value2, this.messageBundle);
            if (result2 != null) {
                return result2;
            }
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.brokenProperty ", propertyName, value2);
            return null;
        }
        return null;
    }

    private Float getFloatImpl(String propertyName) throws HeadlessException {
        String value = (String) this.props.get(propertyName);
        if (value != null) {
            Float result = Parse.parseFloat(value, this.messageBundle);
            if (result != null) {
                return result;
            }
            this.props.remove(propertyName);
        }
        String value2 = (String) this.defaultProps.get(propertyName);
        if (value2 != null) {
            Float result2 = Parse.parseFloat(value2, this.messageBundle);
            if (result2 != null) {
                return result2;
            }
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.brokenProperty ", propertyName, value2);
            return null;
        }
        return null;
    }

    public void setDouble(String propertyName, double value) {
        set(propertyName, Double.toString(value));
    }

    public void setFloat(String propertyName, float value) {
        set(propertyName, Float.toString(value));
    }

    public long getLong(String propertyName, long defaultValue) throws HeadlessException {
        Long result = getLongImpl(propertyName);
        if (result == null) {
            return defaultValue;
        }
        return result.longValue();
    }

    public long getLong(String propertyName) throws HeadlessException {
        Long result = getLongImpl(propertyName);
        if (result == null) {
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.missingProperty", propertyName, result);
            return 0L;
        }
        return result.longValue();
    }

    private Long getLongImpl(String propertyName) throws HeadlessException {
        String value = (String) this.props.get(propertyName);
        if (value != null) {
            Long result = Parse.parseLong(value, this.messageBundle);
            if (result != null) {
                return result;
            }
            this.props.remove(propertyName);
        }
        String value2 = (String) this.defaultProps.get(propertyName);
        if (value2 != null) {
            Long result2 = Parse.parseLong(value2, null);
            if (result2 != null) {
                return result2;
            }
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.brokenProperty ", propertyName, value2);
            return null;
        }
        return null;
    }

    public void setLong(String propertyName, long value) {
        set(propertyName, Long.toString(value));
    }

    public boolean getBoolean(String propertyName, boolean defaultValue) throws HeadlessException {
        Boolean result = getBooleanImpl(propertyName);
        if (result == null) {
            return defaultValue;
        }
        return result == Boolean.TRUE;
    }

    public boolean getBoolean(String propertyName) throws HeadlessException {
        Boolean result = getBooleanImpl(propertyName);
        if (result == null) {
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.missingProperty", propertyName, result);
        }
        return result == Boolean.TRUE;
    }

    private Boolean getBooleanImpl(String propertyName) throws HeadlessException {
        String value = (String) this.props.get(propertyName);
        if (value != null) {
            Boolean result = Parse.parseBoolean(value, this.messageBundle);
            if (result != null) {
                return result;
            }
            this.props.remove(propertyName);
        }
        String value2 = (String) this.defaultProps.get(propertyName);
        if (value2 != null) {
            Boolean result2 = Parse.parseBoolean(value2, null);
            if (result2 != null) {
                return result2;
            }
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.brokenProperty ", propertyName, value2);
            return null;
        }
        return null;
    }

    public void setBoolean(String propertyName, boolean value) {
        set(propertyName, value ? "true" : "false");
    }

    public String getSystemEncoding() {
        return new OutputStreamWriter(new ByteArrayOutputStream()).getEncoding();
    }

    public String getLookAndFeel(String propertyName, String defaultValue) throws HeadlessException {
        String value = (String) this.props.get(propertyName);
        if (value != null) {
            String result = Parse.parseLookAndFeel(value, this.messageBundle);
            if (result != null) {
                return result;
            }
            this.props.remove(propertyName);
        }
        String value2 = (String) this.defaultProps.get(propertyName);
        if (value2 != null) {
            String result2 = Parse.parseLookAndFeel(value2, null);
            if (result2 != null) {
                return result2;
            }
            this.defaultProps.remove(propertyName);
            Resources.showMessageDialog(null, 0, this.messageBundle, "manager.properties.title", "manager.properties.lafError", value2);
        }
        return defaultValue;
    }

    public String getDefaultFilePath() {
        return getString(PROPERTY_DEFAULT_FILE_PATH, null);
    }

    public String getDefaultURL() {
        return getString(PROPERTY_DEFAULT_URL, null);
    }

    public void setDefaultFilePath(String defaultFilePath) {
        if (defaultFilePath == null) {
            remove(PROPERTY_DEFAULT_FILE_PATH);
        } else {
            set(PROPERTY_DEFAULT_FILE_PATH, defaultFilePath);
        }
    }

    public void setDefaultURL(String defaultURL) {
        if (defaultURL == null) {
            remove(PROPERTY_DEFAULT_URL);
        } else {
            set(PROPERTY_DEFAULT_URL, defaultURL);
        }
    }

    public InputStream getResourceAsStream(String prefix, String resourcePath) {
        InputStream result;
        int colon = resourcePath.indexOf(58);
        if (colon >= 0 && resourcePath.lastIndexOf(colon - 1, 47) < 0) {
            try {
                return new URL(resourcePath).openStream();
            } catch (IOException e2) {
                return null;
            }
        }
        String resourcePath2 = makeResPath(prefix, resourcePath);
        ClassLoader cl = getClass().getClassLoader();
        if (cl != null && (result = cl.getResourceAsStream(resourcePath2)) != null) {
            return result;
        }
        return ClassLoader.getSystemResourceAsStream(resourcePath2);
    }

    public static String makeResPath(String prefix, String base_name) {
        if (base_name.length() != 0 && base_name.charAt(0) == '/') {
            return base_name.substring(1, base_name.length());
        }
        if (prefix == null) {
            return base_name;
        }
        return prefix + base_name;
    }

    public static boolean checkAndStoreBooleanProperty(PropertiesManager properties, String propertyName) {
        return checkAndStoreBooleanProperty(properties, propertyName, true);
    }

    public static boolean checkAndStoreBooleanProperty(PropertiesManager properties, String propertyName, boolean defaultVal) throws HeadlessException {
        if (properties == null) {
            return defaultVal;
        }
        boolean returnValue = properties.getBoolean(propertyName, defaultVal);
        properties.setBoolean(propertyName, returnValue);
        return returnValue;
    }

    public static double checkAndStoreDoubleProperty(PropertiesManager properties, String propertyName) {
        return checkAndStoreDoubleProperty(properties, propertyName, 1.0d);
    }

    public static double checkAndStoreDoubleProperty(PropertiesManager properties, String propertyName, double defaultVal) throws HeadlessException {
        if (properties == null) {
            return defaultVal;
        }
        double returnValue = properties.getDouble(propertyName, defaultVal);
        properties.setDouble(propertyName, returnValue);
        return returnValue;
    }

    public static int checkAndStoreIntegerProperty(PropertiesManager properties, String propertyName) {
        return checkAndStoreIntegerProperty(properties, propertyName, 1);
    }

    public static int checkAndStoreIntegerProperty(PropertiesManager properties, String propertyName, int defaultVal) throws HeadlessException {
        if (properties == null) {
            return defaultVal;
        }
        int returnValue = properties.getInt(propertyName, defaultVal);
        properties.setInt(propertyName, returnValue);
        return returnValue;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x005c A[Catch: Exception -> 0x00a1, TryCatch #1 {Exception -> 0x00a1, blocks: (B:11:0x001d, B:13:0x0027, B:14:0x0039, B:16:0x0041, B:18:0x0053, B:20:0x005c, B:21:0x006c, B:23:0x0073, B:25:0x0086, B:26:0x008e, B:27:0x0094), top: B:34:0x001d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static float[] checkAndStoreFloatArrayProperty(org.icepdf.ri.util.PropertiesManager r5, java.lang.String r6, float[] r7) {
        /*
            r0 = r5
            if (r0 == 0) goto Lb
            r0 = r5
            java.util.Properties r0 = r0.props
            if (r0 != 0) goto Ld
        Lb:
            r0 = r7
            return r0
        Ld:
            r0 = r5
            java.util.Properties r0 = r0.props
            r1 = r6
            java.lang.String r0 = r0.getProperty(r1)
            r8 = r0
            r0 = r7
            r9 = r0
            r0 = r8
            if (r0 == 0) goto L5c
            r0 = r8
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Exception -> La1
            int r0 = r0.length()     // Catch: java.lang.Exception -> La1
            if (r0 <= 0) goto L5c
            r0 = r8
            java.lang.String r1 = ","
            java.lang.String[] r0 = r0.split(r1)     // Catch: java.lang.Exception -> La1
            r10 = r0
            r0 = r10
            int r0 = r0.length     // Catch: java.lang.Exception -> La1
            float[] r0 = new float[r0]     // Catch: java.lang.Exception -> La1
            r9 = r0
            r0 = 0
            r11 = r0
        L39:
            r0 = r11
            r1 = r10
            int r1 = r1.length     // Catch: java.lang.Exception -> La1
            if (r0 >= r1) goto L59
            r0 = r9
            r1 = r11
            r2 = r10
            r3 = r11
            r2 = r2[r3]     // Catch: java.lang.NumberFormatException -> L51 java.lang.Exception -> La1
            float r2 = java.lang.Float.parseFloat(r2)     // Catch: java.lang.NumberFormatException -> L51 java.lang.Exception -> La1
            r0[r1] = r2     // Catch: java.lang.NumberFormatException -> L51 java.lang.Exception -> La1
            goto L53
        L51:
            r12 = move-exception
        L53:
            int r11 = r11 + 1
            goto L39
        L59:
            goto L9e
        L5c:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> La1
            r1 = r0
            r2 = r7
            int r2 = r2.length     // Catch: java.lang.Exception -> La1
            r3 = 2
            int r2 = r2 * r3
            r1.<init>(r2)     // Catch: java.lang.Exception -> La1
            r10 = r0
            r0 = 0
            r11 = r0
        L6c:
            r0 = r11
            r1 = r7
            int r1 = r1.length     // Catch: java.lang.Exception -> La1
            if (r0 >= r1) goto L94
            r0 = r10
            r1 = r7
            r2 = r11
            r1 = r1[r2]     // Catch: java.lang.Exception -> La1
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> La1
            r0 = r11
            r1 = 1
            int r0 = r0 + r1
            r1 = r7
            int r1 = r1.length     // Catch: java.lang.Exception -> La1
            if (r0 >= r1) goto L8e
            r0 = r10
            java.lang.String r1 = ","
            java.lang.StringBuilder r0 = r0.append(r1)     // Catch: java.lang.Exception -> La1
        L8e:
            int r11 = r11 + 1
            goto L6c
        L94:
            r0 = r5
            r1 = r6
            r2 = r10
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Exception -> La1
            r0.set(r1, r2)     // Catch: java.lang.Exception -> La1
        L9e:
            goto La3
        La1:
            r10 = move-exception
        La3:
            r0 = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.icepdf.ri.util.PropertiesManager.checkAndStoreFloatArrayProperty(org.icepdf.ri.util.PropertiesManager, java.lang.String, float[]):float[]");
    }
}
