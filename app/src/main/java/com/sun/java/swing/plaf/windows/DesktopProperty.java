package com.sun.java.swing.plaf.windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/DesktopProperty.class */
public class DesktopProperty implements UIDefaults.ActiveValue {
    private static boolean updatePending;
    private static final ReferenceQueue<DesktopProperty> queue = new ReferenceQueue<>();
    private WeakPCL pcl;
    private final String key;
    private Object value;
    private final Object fallback;

    static void flushUnreferencedProperties() {
        while (true) {
            WeakPCL weakPCL = (WeakPCL) queue.poll();
            if (weakPCL != null) {
                weakPCL.dispose();
            } else {
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static synchronized void setUpdatePending(boolean z2) {
        updatePending = z2;
    }

    private static synchronized boolean isUpdatePending() {
        return updatePending;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateAllUIs() {
        if (UIManager.getLookAndFeel().getClass().getPackage().equals(DesktopProperty.class.getPackage())) {
            XPStyle.invalidateStyle();
        }
        for (Frame frame : Frame.getFrames()) {
            updateWindowUI(frame);
        }
    }

    private static void updateWindowUI(Window window) {
        SwingUtilities.updateComponentTreeUI(window);
        for (Window window2 : window.getOwnedWindows()) {
            updateWindowUI(window2);
        }
    }

    public DesktopProperty(String str, Object obj) {
        this.key = str;
        this.fallback = obj;
        flushUnreferencedProperties();
    }

    @Override // javax.swing.UIDefaults.ActiveValue
    public Object createValue(UIDefaults uIDefaults) {
        if (this.value == null) {
            this.value = configureValue(getValueFromDesktop());
            if (this.value == null) {
                this.value = configureValue(getDefaultValue());
            }
        }
        return this.value;
    }

    protected Object getValueFromDesktop() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (this.pcl == null) {
            this.pcl = new WeakPCL(this, getKey(), UIManager.getLookAndFeel());
            defaultToolkit.addPropertyChangeListener(getKey(), this.pcl);
        }
        return defaultToolkit.getDesktopProperty(getKey());
    }

    protected Object getDefaultValue() {
        return this.fallback;
    }

    public void invalidate(LookAndFeel lookAndFeel) {
        invalidate();
    }

    public void invalidate() {
        this.value = null;
    }

    protected void updateUI() {
        if (!isUpdatePending()) {
            setUpdatePending(true);
            SwingUtilities.invokeLater(new Runnable() { // from class: com.sun.java.swing.plaf.windows.DesktopProperty.1
                @Override // java.lang.Runnable
                public void run() {
                    DesktopProperty.updateAllUIs();
                    DesktopProperty.setUpdatePending(false);
                }
            });
        }
    }

    protected Object configureValue(Object obj) {
        if (obj != null) {
            if (obj instanceof Color) {
                return new ColorUIResource((Color) obj);
            }
            if (obj instanceof Font) {
                return new FontUIResource((Font) obj);
            }
            if (obj instanceof UIDefaults.LazyValue) {
                obj = ((UIDefaults.LazyValue) obj).createValue(null);
            } else if (obj instanceof UIDefaults.ActiveValue) {
                obj = ((UIDefaults.ActiveValue) obj).createValue(null);
            }
        }
        return obj;
    }

    protected String getKey() {
        return this.key;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/DesktopProperty$WeakPCL.class */
    private static class WeakPCL extends WeakReference<DesktopProperty> implements PropertyChangeListener {
        private String key;
        private LookAndFeel laf;

        WeakPCL(DesktopProperty desktopProperty, String str, LookAndFeel lookAndFeel) {
            super(desktopProperty, DesktopProperty.queue);
            this.key = str;
            this.laf = lookAndFeel;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            DesktopProperty desktopProperty = get();
            if (desktopProperty == null || this.laf != UIManager.getLookAndFeel()) {
                dispose();
            } else {
                desktopProperty.invalidate(this.laf);
                desktopProperty.updateUI();
            }
        }

        void dispose() {
            Toolkit.getDefaultToolkit().removePropertyChangeListener(this.key, this);
        }
    }
}
