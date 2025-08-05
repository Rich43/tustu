package com.efiAnalytics.ui;

import com.sun.glass.ui.Platform;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

/* renamed from: com.efiAnalytics.ui.fp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fp.class */
public class C1685fp {
    public static void a(Component component, boolean z2) {
        if (component instanceof Container) {
            for (Component component2 : ((Container) component).getComponents()) {
                a(component2, z2);
            }
        }
        component.setEnabled(z2);
    }

    public static void a(Container container, boolean z2) {
        if (container instanceof Container) {
            for (Component component : container.getComponents()) {
                a(component, z2);
            }
        }
    }

    public static boolean a() {
        return System.getProperty("os.name", "").startsWith(Platform.MAC);
    }

    public static boolean b() {
        return System.getProperty("os.name", "").startsWith("Linux");
    }

    public static boolean c() throws HeadlessException {
        GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        return defaultScreenDevice.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT) && defaultScreenDevice.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT);
    }

    public static boolean a(Component[] componentArr, Component component) {
        for (Component component2 : componentArr) {
            if (component2.equals(component)) {
                return true;
            }
        }
        return false;
    }

    public static JPopupMenu a(JPopupMenu jPopupMenu) {
        for (Object obj : jPopupMenu.getComponents()) {
            if (obj instanceof InterfaceC1581bs) {
                InterfaceC1581bs interfaceC1581bs = (InterfaceC1581bs) obj;
                if (interfaceC1581bs.i() != null) {
                    interfaceC1581bs.setVisible(interfaceC1581bs.i().a());
                }
                if (interfaceC1581bs.e() != null) {
                    interfaceC1581bs.setEnabled(interfaceC1581bs.e().a());
                }
            }
            if (obj instanceof JMenu) {
                a((JMenu) obj);
            }
        }
        return jPopupMenu;
    }

    public static JMenu a(JMenu jMenu) {
        for (Object obj : jMenu.getMenuComponents()) {
            if (obj instanceof InterfaceC1581bs) {
                InterfaceC1581bs interfaceC1581bs = (InterfaceC1581bs) obj;
                if (interfaceC1581bs.i() != null) {
                    interfaceC1581bs.setVisible(interfaceC1581bs.i().a());
                }
                if (interfaceC1581bs.e() != null) {
                    interfaceC1581bs.setEnabled(interfaceC1581bs.e().a());
                }
            }
            if (obj instanceof JMenu) {
                a((JMenu) obj);
            }
        }
        return jMenu;
    }
}
