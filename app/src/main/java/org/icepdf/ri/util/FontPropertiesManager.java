package org.icepdf.ri.util;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.fonts.FontManager;
import org.icepdf.core.util.PdfOps;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/util/FontPropertiesManager.class */
public class FontPropertiesManager {
    private static final Logger logger = Logger.getLogger(FontPropertiesManager.class.toString());
    private static final String DEFAULT_HOME_DIR = ".icesoft/icepdf_viewer";
    private static final String LOCK_FILE = "_syslock";
    private static final String USER_FILENAME = "pdfviewerfontcache.properties";
    private Properties sysProps;
    private PropertiesManager props;
    File userHome;
    private File dataDir;
    private File lockDir;
    private File propertyFile;
    private ResourceBundle messageBundle;
    String versionName = Document.getLibraryVersion();
    private Properties fontProps = new Properties();
    private FontManager fontManager = FontManager.getInstance();

    public FontPropertiesManager(PropertiesManager appProps, Properties sysProps, ResourceBundle messageBundle) throws HeadlessException {
        this.sysProps = sysProps;
        this.props = appProps;
        this.messageBundle = messageBundle;
        setupHomeDir(null);
        recordMofifTime();
        setupLock();
        loadProperties();
    }

    public synchronized void loadProperties() throws HeadlessException {
        if (this.dataDir != null) {
            this.propertyFile = new File(this.dataDir, USER_FILENAME);
            if (this.propertyFile.exists()) {
                try {
                    InputStream in = new FileInputStream(this.propertyFile);
                    try {
                        this.fontProps.load(in);
                        this.fontManager.setFontProperties(this.fontProps);
                        in.close();
                        return;
                    } catch (Throwable th) {
                        in.close();
                        throw th;
                    }
                } catch (IOException ex) {
                    if (getBoolean("application.showLocalStorageDialogs", true)) {
                        Resources.showMessageDialog(null, 0, this.messageBundle, "fontManager.properties.title", "manager.properties.session.readError", ex);
                    }
                    if (logger.isLoggable(Level.WARNING)) {
                        logger.log(Level.WARNING, "Error loading font properties cache", (Throwable) ex);
                        return;
                    }
                    return;
                } catch (IllegalArgumentException e2) {
                    setupDefaultProperties();
                    saveProperties();
                    return;
                }
            }
            setupDefaultProperties();
            saveProperties();
        }
    }

    public synchronized void saveProperties() throws HeadlessException {
        if (ownLock()) {
            try {
                FileOutputStream out = new FileOutputStream(this.propertyFile);
                try {
                    this.fontProps.store(out, "-- ICEpf Font properties --");
                    out.close();
                    recordMofifTime();
                } catch (Throwable th) {
                    out.close();
                    throw th;
                }
            } catch (IOException ex) {
                if (getBoolean("application.showLocalStorageDialogs", true)) {
                    Resources.showMessageDialog(null, 0, this.messageBundle, "fontManager.properties.title", "manager.properties.saveError", ex);
                }
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Error saving font properties cache", (Throwable) ex);
                }
            }
        }
    }

    private boolean ownLock() {
        return this.lockDir != null;
    }

    private void recordMofifTime() {
        Calendar c2 = new GregorianCalendar();
        c2.setTime(new Date());
        c2.set(12, c2.get(12) + 1);
        c2.set(13, 0);
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
                    Resources.showMessageDialog(null, 0, this.messageBundle, "fontManager.properties.title", "manager.properties.session.nolock", LOCK_FILE);
                }
            }
        }
        this.lockDir = dir;
    }

    private boolean setupDefaultProperties() throws HeadlessException {
        this.fontProps = new Properties();
        try {
            this.fontManager.readSystemFonts(null);
            this.fontProps = this.fontManager.getFontProperties();
            return true;
        } catch (Exception ex) {
            if (getBoolean("application.showLocalStorageDialogs", true)) {
                Resources.showMessageDialog(null, 0, this.messageBundle, "fontManager.properties.title", "manager.properties.session.readError", ex);
            }
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Error loading default properties", (Throwable) ex);
                return false;
            }
            return false;
        }
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
            String dataDirStr = this.props.getString("application.datadir", DEFAULT_HOME_DIR);
            this.dataDir = new File(this.userHome, dataDirStr);
        }
        if (!this.dataDir.isDirectory()) {
            String path = this.dataDir.getAbsolutePath();
            if (this.props.hasUserRejectedCreatingLocalDataDir()) {
                create = false;
            } else if (getBoolean("application.showLocalStorageDialogs", true)) {
                create = Resources.showConfirmDialog(null, this.messageBundle, "fontManager.properties.title", "manager.properties.createNewDirectory", path);
                if (!create) {
                    this.props.setUserRejectedCreatingLocalDataDir();
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
                    Resources.showMessageDialog(null, 0, this.messageBundle, "fontManager.properties.title", "manager.properties.failedCreation", this.dataDir.getAbsolutePath());
                }
                this.dataDir = null;
            }
        }
    }

    public boolean getBoolean(String propertyName, boolean defaultValue) throws HeadlessException {
        Boolean result = getBooleanImpl(propertyName);
        if (result == null) {
            return defaultValue;
        }
        return result == Boolean.TRUE;
    }

    private Boolean getBooleanImpl(String propertyName) throws HeadlessException {
        String value = this.props.getString(propertyName);
        if (value != null) {
            Boolean result = Parse.parseBoolean(value, this.messageBundle);
            if (result != null) {
                return result;
            }
            this.props.remove(propertyName);
        }
        String value2 = this.props.getString(propertyName);
        if (value2 != null) {
            Boolean result2 = Parse.parseBoolean(value2, null);
            if (result2 != null) {
                return result2;
            }
            throwBrokenDefault(propertyName, value2);
            return null;
        }
        return null;
    }

    private void throwBrokenDefault(String propertyName, String value) {
        throw new IllegalStateException("Broken default property '" + propertyName + "' value: '" + value + PdfOps.SINGLE_QUOTE_TOKEN);
    }
}
