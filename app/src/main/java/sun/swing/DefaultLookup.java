package sun.swing;

import java.awt.Color;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import sun.awt.AppContext;

/* loaded from: rt.jar:sun/swing/DefaultLookup.class */
public class DefaultLookup {
    private static final Object DEFAULT_LOOKUP_KEY = new StringBuffer("DefaultLookup");
    private static Thread currentDefaultThread;
    private static DefaultLookup currentDefaultLookup;
    private static boolean isLookupSet;

    public static void setDefaultLookup(DefaultLookup defaultLookup) {
        synchronized (DefaultLookup.class) {
            if (isLookupSet || defaultLookup != null) {
                if (defaultLookup == null) {
                    defaultLookup = new DefaultLookup();
                }
                isLookupSet = true;
                AppContext.getAppContext().put(DEFAULT_LOOKUP_KEY, defaultLookup);
                currentDefaultThread = Thread.currentThread();
                currentDefaultLookup = defaultLookup;
            }
        }
    }

    public static Object get(JComponent jComponent, ComponentUI componentUI, String str) {
        boolean z2;
        DefaultLookup defaultLookup;
        synchronized (DefaultLookup.class) {
            z2 = isLookupSet;
        }
        if (!z2) {
            return UIManager.get(str, jComponent.getLocale());
        }
        Thread threadCurrentThread = Thread.currentThread();
        synchronized (DefaultLookup.class) {
            if (threadCurrentThread == currentDefaultThread) {
                defaultLookup = currentDefaultLookup;
            } else {
                defaultLookup = (DefaultLookup) AppContext.getAppContext().get(DEFAULT_LOOKUP_KEY);
                if (defaultLookup == null) {
                    defaultLookup = new DefaultLookup();
                    AppContext.getAppContext().put(DEFAULT_LOOKUP_KEY, defaultLookup);
                }
                currentDefaultThread = threadCurrentThread;
                currentDefaultLookup = defaultLookup;
            }
        }
        return defaultLookup.getDefault(jComponent, componentUI, str);
    }

    public static int getInt(JComponent jComponent, ComponentUI componentUI, String str, int i2) {
        Object obj = get(jComponent, componentUI, str);
        if (obj == null || !(obj instanceof Number)) {
            return i2;
        }
        return ((Number) obj).intValue();
    }

    public static int getInt(JComponent jComponent, ComponentUI componentUI, String str) {
        return getInt(jComponent, componentUI, str, -1);
    }

    public static Insets getInsets(JComponent jComponent, ComponentUI componentUI, String str, Insets insets) {
        Object obj = get(jComponent, componentUI, str);
        if (obj == null || !(obj instanceof Insets)) {
            return insets;
        }
        return (Insets) obj;
    }

    public static Insets getInsets(JComponent jComponent, ComponentUI componentUI, String str) {
        return getInsets(jComponent, componentUI, str, null);
    }

    public static boolean getBoolean(JComponent jComponent, ComponentUI componentUI, String str, boolean z2) {
        Object obj = get(jComponent, componentUI, str);
        if (obj == null || !(obj instanceof Boolean)) {
            return z2;
        }
        return ((Boolean) obj).booleanValue();
    }

    public static boolean getBoolean(JComponent jComponent, ComponentUI componentUI, String str) {
        return getBoolean(jComponent, componentUI, str, false);
    }

    public static Color getColor(JComponent jComponent, ComponentUI componentUI, String str, Color color) {
        Object obj = get(jComponent, componentUI, str);
        if (obj == null || !(obj instanceof Color)) {
            return color;
        }
        return (Color) obj;
    }

    public static Color getColor(JComponent jComponent, ComponentUI componentUI, String str) {
        return getColor(jComponent, componentUI, str, null);
    }

    public static Icon getIcon(JComponent jComponent, ComponentUI componentUI, String str, Icon icon) {
        Object obj = get(jComponent, componentUI, str);
        if (obj == null || !(obj instanceof Icon)) {
            return icon;
        }
        return (Icon) obj;
    }

    public static Icon getIcon(JComponent jComponent, ComponentUI componentUI, String str) {
        return getIcon(jComponent, componentUI, str, null);
    }

    public static Border getBorder(JComponent jComponent, ComponentUI componentUI, String str, Border border) {
        Object obj = get(jComponent, componentUI, str);
        if (obj == null || !(obj instanceof Border)) {
            return border;
        }
        return (Border) obj;
    }

    public static Border getBorder(JComponent jComponent, ComponentUI componentUI, String str) {
        return getBorder(jComponent, componentUI, str, null);
    }

    public Object getDefault(JComponent jComponent, ComponentUI componentUI, String str) {
        return UIManager.get(str, jComponent.getLocale());
    }
}
