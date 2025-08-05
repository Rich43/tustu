package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InputMapUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;
import sun.awt.SunToolkit;
import sun.swing.DefaultLayoutStyle;
import sun.swing.ImageIconUIResource;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/LookAndFeel.class */
public abstract class LookAndFeel {
    public abstract String getName();

    public abstract String getID();

    public abstract String getDescription();

    public abstract boolean isNativeLookAndFeel();

    public abstract boolean isSupportedLookAndFeel();

    public static void installColors(JComponent jComponent, String str, String str2) {
        Color background = jComponent.getBackground();
        if (background == null || (background instanceof UIResource)) {
            jComponent.setBackground(UIManager.getColor(str));
        }
        Color foreground = jComponent.getForeground();
        if (foreground == null || (foreground instanceof UIResource)) {
            jComponent.setForeground(UIManager.getColor(str2));
        }
    }

    public static void installColorsAndFont(JComponent jComponent, String str, String str2, String str3) {
        Font font = jComponent.getFont();
        if (font == null || (font instanceof UIResource)) {
            jComponent.setFont(UIManager.getFont(str3));
        }
        installColors(jComponent, str, str2);
    }

    public static void installBorder(JComponent jComponent, String str) {
        Border border = jComponent.getBorder();
        if (border == null || (border instanceof UIResource)) {
            jComponent.setBorder(UIManager.getBorder(str));
        }
    }

    public static void uninstallBorder(JComponent jComponent) {
        if (jComponent.getBorder() instanceof UIResource) {
            jComponent.setBorder(null);
        }
    }

    public static void installProperty(JComponent jComponent, String str, Object obj) {
        if (SunToolkit.isInstanceOf(jComponent, "javax.swing.JPasswordField")) {
            if (!((JPasswordField) jComponent).customSetUIProperty(str, obj)) {
                jComponent.setUIProperty(str, obj);
                return;
            }
            return;
        }
        jComponent.setUIProperty(str, obj);
    }

    public static JTextComponent.KeyBinding[] makeKeyBindings(Object[] objArr) {
        JTextComponent.KeyBinding[] keyBindingArr = new JTextComponent.KeyBinding[objArr.length / 2];
        for (int i2 = 0; i2 < keyBindingArr.length; i2++) {
            Object obj = objArr[2 * i2];
            keyBindingArr[i2] = new JTextComponent.KeyBinding(obj instanceof KeyStroke ? (KeyStroke) obj : KeyStroke.getKeyStroke((String) obj), (String) objArr[(2 * i2) + 1]);
        }
        return keyBindingArr;
    }

    public static InputMap makeInputMap(Object[] objArr) {
        InputMapUIResource inputMapUIResource = new InputMapUIResource();
        loadKeyBindings(inputMapUIResource, objArr);
        return inputMapUIResource;
    }

    public static ComponentInputMap makeComponentInputMap(JComponent jComponent, Object[] objArr) {
        ComponentInputMapUIResource componentInputMapUIResource = new ComponentInputMapUIResource(jComponent);
        loadKeyBindings(componentInputMapUIResource, objArr);
        return componentInputMapUIResource;
    }

    public static void loadKeyBindings(InputMap inputMap, Object[] objArr) {
        if (objArr != null) {
            int i2 = 0;
            int length = objArr.length;
            while (i2 < length) {
                int i3 = i2;
                int i4 = i2 + 1;
                Object obj = objArr[i3];
                inputMap.put(obj instanceof KeyStroke ? (KeyStroke) obj : KeyStroke.getKeyStroke((String) obj), objArr[i4]);
                i2 = i4 + 1;
            }
        }
    }

    public static Object makeIcon(Class<?> cls, String str) {
        return SwingUtilities2.makeIcon(cls, cls, str);
    }

    public LayoutStyle getLayoutStyle() {
        return DefaultLayoutStyle.getInstance();
    }

    public void provideErrorFeedback(Component component) {
        Toolkit defaultToolkit;
        if (component != null) {
            defaultToolkit = component.getToolkit();
        } else {
            defaultToolkit = Toolkit.getDefaultToolkit();
        }
        defaultToolkit.beep();
    }

    public static Object getDesktopPropertyValue(String str, Object obj) {
        Object desktopProperty = Toolkit.getDefaultToolkit().getDesktopProperty(str);
        if (desktopProperty == null) {
            return obj;
        }
        if (desktopProperty instanceof Color) {
            return new ColorUIResource((Color) desktopProperty);
        }
        if (desktopProperty instanceof Font) {
            return new FontUIResource((Font) desktopProperty);
        }
        return desktopProperty;
    }

    public Icon getDisabledIcon(JComponent jComponent, Icon icon) {
        if (icon instanceof ImageIcon) {
            return new ImageIconUIResource(GrayFilter.createDisabledImage(((ImageIcon) icon).getImage()));
        }
        return null;
    }

    public Icon getDisabledSelectedIcon(JComponent jComponent, Icon icon) {
        return getDisabledIcon(jComponent, icon);
    }

    public boolean getSupportsWindowDecorations() {
        return false;
    }

    public void initialize() {
    }

    public void uninitialize() {
    }

    public UIDefaults getDefaults() {
        return null;
    }

    public String toString() {
        return "[" + getDescription() + " - " + getClass().getName() + "]";
    }
}
