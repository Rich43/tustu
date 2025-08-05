package java.awt;

import java.awt.peer.SystemTrayPeer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Vector;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.HeadlessToolkit;
import sun.awt.SunToolkit;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/awt/SystemTray.class */
public class SystemTray {
    private static SystemTray systemTray;
    private int currentIconID = 0;
    private transient SystemTrayPeer peer;
    private static final TrayIcon[] EMPTY_TRAY_ARRAY = new TrayIcon[0];

    static {
        AWTAccessor.setSystemTrayAccessor(new AWTAccessor.SystemTrayAccessor() { // from class: java.awt.SystemTray.1
            @Override // sun.awt.AWTAccessor.SystemTrayAccessor
            public void firePropertyChange(SystemTray systemTray2, String str, Object obj, Object obj2) {
                systemTray2.firePropertyChange(str, obj, obj2);
            }
        });
    }

    private SystemTray() {
        addNotify();
    }

    public static SystemTray getSystemTray() {
        checkSystemTrayAllowed();
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        initializeSystemTrayIfNeeded();
        if (!isSupported()) {
            throw new UnsupportedOperationException("The system tray is not supported on the current platform.");
        }
        return systemTray;
    }

    public static boolean isSupported() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            initializeSystemTrayIfNeeded();
            return ((SunToolkit) defaultToolkit).isTraySupported();
        }
        if (defaultToolkit instanceof HeadlessToolkit) {
            return ((HeadlessToolkit) defaultToolkit).isTraySupported();
        }
        return false;
    }

    public void add(TrayIcon trayIcon) throws AWTException {
        TrayIcon[] trayIcons;
        Vector vector;
        TrayIcon[] trayIcons2;
        if (trayIcon == null) {
            throw new NullPointerException("adding null TrayIcon");
        }
        synchronized (this) {
            trayIcons = systemTray.getTrayIcons();
            vector = (Vector) AppContext.getAppContext().get(TrayIcon.class);
            if (vector == null) {
                vector = new Vector(3);
                AppContext.getAppContext().put(TrayIcon.class, vector);
            } else if (vector.contains(trayIcon)) {
                throw new IllegalArgumentException("adding TrayIcon that is already added");
            }
            vector.add(trayIcon);
            trayIcons2 = systemTray.getTrayIcons();
            int i2 = this.currentIconID + 1;
            this.currentIconID = i2;
            trayIcon.setID(i2);
        }
        try {
            trayIcon.addNotify();
            firePropertyChange("trayIcons", trayIcons, trayIcons2);
        } catch (AWTException e2) {
            vector.remove(trayIcon);
            throw e2;
        }
    }

    public void remove(TrayIcon trayIcon) {
        if (trayIcon == null) {
            return;
        }
        synchronized (this) {
            TrayIcon[] trayIcons = systemTray.getTrayIcons();
            Vector vector = (Vector) AppContext.getAppContext().get(TrayIcon.class);
            if (vector == null || !vector.remove(trayIcon)) {
                return;
            }
            trayIcon.removeNotify();
            firePropertyChange("trayIcons", trayIcons, systemTray.getTrayIcons());
        }
    }

    public TrayIcon[] getTrayIcons() {
        Vector vector = (Vector) AppContext.getAppContext().get(TrayIcon.class);
        if (vector != null) {
            return (TrayIcon[]) vector.toArray(new TrayIcon[vector.size()]);
        }
        return EMPTY_TRAY_ARRAY;
    }

    public Dimension getTrayIconSize() {
        return this.peer.getTrayIconSize();
    }

    public synchronized void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener == null) {
            return;
        }
        getCurrentChangeSupport().addPropertyChangeListener(str, propertyChangeListener);
    }

    public synchronized void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        if (propertyChangeListener == null) {
            return;
        }
        getCurrentChangeSupport().removePropertyChangeListener(str, propertyChangeListener);
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners(String str) {
        return getCurrentChangeSupport().getPropertyChangeListeners(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void firePropertyChange(String str, Object obj, Object obj2) {
        if (obj != null && obj2 != null && obj.equals(obj2)) {
            return;
        }
        getCurrentChangeSupport().firePropertyChange(str, obj, obj2);
    }

    private synchronized PropertyChangeSupport getCurrentChangeSupport() {
        PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(SystemTray.class);
        if (propertyChangeSupport == null) {
            propertyChangeSupport = new PropertyChangeSupport(this);
            AppContext.getAppContext().put(SystemTray.class, propertyChangeSupport);
        }
        return propertyChangeSupport;
    }

    synchronized void addNotify() {
        if (this.peer == null) {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            if (defaultToolkit instanceof SunToolkit) {
                this.peer = ((SunToolkit) Toolkit.getDefaultToolkit()).createSystemTray(this);
            } else if (defaultToolkit instanceof HeadlessToolkit) {
                this.peer = ((HeadlessToolkit) Toolkit.getDefaultToolkit()).createSystemTray(this);
            }
        }
    }

    static void checkSystemTrayAllowed() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.ACCESS_SYSTEM_TRAY_PERMISSION);
        }
    }

    private static void initializeSystemTrayIfNeeded() {
        synchronized (SystemTray.class) {
            if (systemTray == null) {
                systemTray = new SystemTray();
            }
        }
    }
}
