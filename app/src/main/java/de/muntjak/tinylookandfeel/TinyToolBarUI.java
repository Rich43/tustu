package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.borders.TinyToolButtonBorder;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Window;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalToolBarUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyToolBarUI.class */
public class TinyToolBarUI extends MetalToolBarUI {
    public static final String IS_TOOL_BAR_BUTTON_KEY = "isToolbarButton";
    public static final int FLOATABLE_GRIP_SIZE = 8;
    static Class class$javax$swing$JToolBar;
    private static List components = new ArrayList();
    private static Border toolButtonBorder = new TinyToolButtonBorder();

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyToolBarUI();
    }

    public static boolean doesMenuBarBorderToolBar(JMenuBar jMenuBar) throws Throwable {
        Class clsClass$;
        if (class$javax$swing$JToolBar == null) {
            clsClass$ = class$("javax.swing.JToolBar");
            class$javax$swing$JToolBar = clsClass$;
        } else {
            clsClass$ = class$javax$swing$JToolBar;
        }
        JToolBar jToolBar = (JToolBar) findRegisteredComponentOfType(jMenuBar, clsClass$);
        if (jToolBar == null || jToolBar.getOrientation() != 0) {
            return false;
        }
        JRootPane rootPane = SwingUtilities.getRootPane(jMenuBar);
        Point pointConvertPoint = SwingUtilities.convertPoint(jMenuBar, new Point(0, 0), rootPane);
        int i2 = pointConvertPoint.f12370x;
        int i3 = pointConvertPoint.f12371y;
        pointConvertPoint.f12371y = 0;
        pointConvertPoint.f12370x = 0;
        Point pointConvertPoint2 = SwingUtilities.convertPoint(jToolBar, pointConvertPoint, rootPane);
        return pointConvertPoint2.f12370x == i2 && i3 + jMenuBar.getHeight() == pointConvertPoint2.f12371y && jMenuBar.getWidth() == jToolBar.getWidth();
    }

    static synchronized Object findRegisteredComponentOfType(JComponent jComponent, Class cls) {
        JRootPane rootPane = SwingUtilities.getRootPane(jComponent);
        if (rootPane == null) {
            return null;
        }
        for (int size = components.size() - 1; size >= 0; size--) {
            Object obj = ((WeakReference) components.get(size)).get();
            if (obj == null) {
                components.remove(size);
            } else if (cls.isInstance(obj) && SwingUtilities.getRootPane((Component) obj) == rootPane) {
                return obj;
            }
        }
        return null;
    }

    static synchronized void register(JComponent jComponent) {
        if (jComponent == null) {
            throw new NullPointerException("JComponent must be non-null");
        }
        components.add(new WeakReference(jComponent));
    }

    static synchronized void unregister(JComponent jComponent) {
        for (int size = components.size() - 1; size >= 0; size--) {
            T t2 = ((WeakReference) components.get(size)).get();
            if (t2 == jComponent || t2 == 0) {
                components.remove(size);
            }
        }
    }

    @Override // javax.swing.plaf.metal.MetalToolBarUI, javax.swing.plaf.basic.BasicToolBarUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        jComponent.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        register(jComponent);
    }

    @Override // javax.swing.plaf.metal.MetalToolBarUI, javax.swing.plaf.basic.BasicToolBarUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        jComponent.putClientProperty("JToolBar.isRollover", null);
        unregister(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected RootPaneContainer createFloatingWindow(JToolBar jToolBar) {
        Window windowAncestor = SwingUtilities.getWindowAncestor(jToolBar);
        JDialog jDialog = windowAncestor instanceof Frame ? new JDialog((Frame) windowAncestor, jToolBar.getName(), false) : windowAncestor instanceof Dialog ? new JDialog((Dialog) windowAncestor, jToolBar.getName(), false) : new JDialog((Frame) null, jToolBar.getName(), false);
        jDialog.setTitle(jToolBar.getName());
        jDialog.setResizable(false);
        jDialog.addWindowListener(createFrameListener());
        return jDialog;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (jComponent.getBackground() instanceof ColorUIResource) {
            graphics.setColor(Theme.toolBarColor.getColor());
        } else {
            graphics.setColor(jComponent.getBackground());
        }
        graphics.fillRect(0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void setBorderToRollover(Component component) {
        setBorderToNormal(component);
    }

    @Override // javax.swing.plaf.basic.BasicToolBarUI
    protected void setBorderToNormal(Component component) {
        if (!(component instanceof AbstractButton) || (component instanceof JCheckBox) || (component instanceof JRadioButton)) {
            return;
        }
        AbstractButton abstractButton = (AbstractButton) component;
        abstractButton.setRolloverEnabled(true);
        abstractButton.putClientProperty(IS_TOOL_BAR_BUTTON_KEY, Boolean.TRUE);
        if ((abstractButton.getBorder() instanceof UIResource) || (abstractButton.getBorder() instanceof TinyToolButtonBorder)) {
            abstractButton.setBorder(toolButtonBorder);
        }
    }

    static Class class$(String str) throws Throwable {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError().initCause(e2);
        }
    }
}
