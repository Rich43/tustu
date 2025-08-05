package sun.awt.windows;

import com.sun.javafx.font.LogicalFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import sun.awt.SunToolkit;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:sun/awt/windows/WDesktopProperties.class */
final class WDesktopProperties {
    private static final PlatformLogger log;
    private static final String PREFIX = "win.";
    private static final String FILE_PREFIX = "awt.file.";
    private static final String PROP_NAMES = "win.propNames";
    private long pData;
    private WToolkit wToolkit;
    private HashMap<String, Object> map = new HashMap<>();
    static HashMap<String, String> fontNameMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void initIDs();

    private native void init();

    private native void getWindowsParameters();

    /* JADX INFO: Access modifiers changed from: private */
    public native void playWindowsSound(String str);

    static {
        $assertionsDisabled = !WDesktopProperties.class.desiredAssertionStatus();
        log = PlatformLogger.getLogger("sun.awt.windows.WDesktopProperties");
        initIDs();
        fontNameMap = new HashMap<>();
        fontNameMap.put("Courier", "Monospaced");
        fontNameMap.put("MS Serif", "Microsoft Serif");
        fontNameMap.put("MS Sans Serif", "Microsoft Sans Serif");
        fontNameMap.put("Terminal", Font.DIALOG);
        fontNameMap.put("FixedSys", "Monospaced");
        fontNameMap.put(LogicalFont.SYSTEM, Font.DIALOG);
    }

    static boolean isWindowsProperty(String str) {
        return str.startsWith(PREFIX) || str.startsWith(FILE_PREFIX) || str.equals(SunToolkit.DESKTOPFONTHINTS);
    }

    WDesktopProperties(WToolkit wToolkit) {
        this.wToolkit = wToolkit;
        init();
    }

    private String[] getKeyNames() {
        Object[] array = this.map.keySet().toArray();
        String[] strArr = new String[array.length];
        for (int i2 = 0; i2 < array.length; i2++) {
            strArr[i2] = array[i2].toString();
        }
        Arrays.sort(strArr);
        return strArr;
    }

    private synchronized void setBooleanProperty(String str, boolean z2) {
        if (!$assertionsDisabled && str == null) {
            throw new AssertionError();
        }
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine(str + "=" + String.valueOf(z2));
        }
        this.map.put(str, Boolean.valueOf(z2));
    }

    private synchronized void setIntegerProperty(String str, int i2) {
        if (!$assertionsDisabled && str == null) {
            throw new AssertionError();
        }
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine(str + "=" + String.valueOf(i2));
        }
        this.map.put(str, Integer.valueOf(i2));
    }

    private synchronized void setStringProperty(String str, String str2) {
        if (!$assertionsDisabled && str == null) {
            throw new AssertionError();
        }
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine(str + "=" + str2);
        }
        this.map.put(str, str2);
    }

    private synchronized void setColorProperty(String str, int i2, int i3, int i4) {
        if (!$assertionsDisabled && (str == null || i2 > 255 || i3 > 255 || i4 > 255)) {
            throw new AssertionError();
        }
        Color color = new Color(i2, i3, i4);
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine(str + "=" + ((Object) color));
        }
        this.map.put(str, color);
    }

    private synchronized void setFontProperty(String str, String str2, int i2, int i3) {
        if (!$assertionsDisabled && (str == null || i2 > 3 || i3 < 0)) {
            throw new AssertionError();
        }
        String str3 = fontNameMap.get(str2);
        if (str3 != null) {
            str2 = str3;
        }
        Font font = new Font(str2, i2, i3);
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine(str + "=" + ((Object) font));
        }
        this.map.put(str, font);
        String str4 = str + ".height";
        Integer numValueOf = Integer.valueOf(i3);
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine(str4 + "=" + ((Object) numValueOf));
        }
        this.map.put(str4, numValueOf);
    }

    private synchronized void setSoundProperty(String str, String str2) {
        if (!$assertionsDisabled && (str == null || str2 == null)) {
            throw new AssertionError();
        }
        WinPlaySound winPlaySound = new WinPlaySound(str2);
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine(str + "=" + ((Object) winPlaySound));
        }
        this.map.put(str, winPlaySound);
    }

    /* loaded from: rt.jar:sun/awt/windows/WDesktopProperties$WinPlaySound.class */
    class WinPlaySound implements Runnable {
        String winEventName;

        WinPlaySound(String str) {
            this.winEventName = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            WDesktopProperties.this.playWindowsSound(this.winEventName);
        }

        public String toString() {
            return "WinPlaySound(" + this.winEventName + ")";
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            try {
                return this.winEventName.equals(((WinPlaySound) obj).winEventName);
            } catch (Exception e2) {
                return false;
            }
        }

        public int hashCode() {
            return this.winEventName.hashCode();
        }
    }

    synchronized Map<String, Object> getProperties() {
        ThemeReader.flush();
        this.map = new HashMap<>();
        getWindowsParameters();
        this.map.put(SunToolkit.DESKTOPFONTHINTS, SunToolkit.getDesktopFontHints());
        this.map.put(PROP_NAMES, getKeyNames());
        this.map.put("DnD.Autoscroll.cursorHysteresis", this.map.get("win.drag.x"));
        return (Map) this.map.clone();
    }

    synchronized RenderingHints getDesktopAAHints() {
        Object obj = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
        Integer numValueOf = null;
        Boolean bool = (Boolean) this.map.get("win.text.fontSmoothingOn");
        if (bool != null && bool.equals(Boolean.TRUE)) {
            Integer num = (Integer) this.map.get("win.text.fontSmoothingType");
            if (num == null || num.intValue() <= 1 || num.intValue() > 2) {
                obj = RenderingHints.VALUE_TEXT_ANTIALIAS_GASP;
            } else {
                Integer num2 = (Integer) this.map.get("win.text.fontSmoothingOrientation");
                if (num2 == null || num2.intValue() != 0) {
                    obj = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB;
                } else {
                    obj = RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR;
                }
                Integer num3 = (Integer) this.map.get("win.text.fontSmoothingContrast");
                numValueOf = num3 == null ? 140 : Integer.valueOf(num3.intValue() / 10);
            }
        }
        RenderingHints renderingHints = new RenderingHints(null);
        renderingHints.put(RenderingHints.KEY_TEXT_ANTIALIASING, obj);
        if (numValueOf != null) {
            renderingHints.put(RenderingHints.KEY_TEXT_LCD_CONTRAST, numValueOf);
        }
        return renderingHints;
    }
}
