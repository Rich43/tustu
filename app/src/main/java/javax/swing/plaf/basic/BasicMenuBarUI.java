package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuBarUI;
import javax.swing.plaf.UIResource;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuBarUI.class */
public class BasicMenuBarUI extends MenuBarUI {
    protected JMenuBar menuBar = null;
    protected ContainerListener containerListener;
    protected ChangeListener changeListener;
    private Handler handler;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicMenuBarUI();
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("takeFocus"));
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.menuBar = (JMenuBar) jComponent;
        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    protected void installDefaults() {
        if (this.menuBar.getLayout() == null || (this.menuBar.getLayout() instanceof UIResource)) {
            this.menuBar.setLayout(new DefaultMenuLayout(this.menuBar, 2));
        }
        LookAndFeel.installProperty(this.menuBar, "opaque", Boolean.TRUE);
        LookAndFeel.installBorder(this.menuBar, "MenuBar.border");
        LookAndFeel.installColorsAndFont(this.menuBar, "MenuBar.background", "MenuBar.foreground", "MenuBar.font");
    }

    protected void installListeners() {
        this.containerListener = createContainerListener();
        this.changeListener = createChangeListener();
        for (int i2 = 0; i2 < this.menuBar.getMenuCount(); i2++) {
            JMenu menu = this.menuBar.getMenu(i2);
            if (menu != null) {
                menu.getModel().addChangeListener(this.changeListener);
            }
        }
        this.menuBar.addContainerListener(this.containerListener);
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.menuBar, 2, getInputMap(2));
        LazyActionMap.installLazyActionMap(this.menuBar, BasicMenuBarUI.class, "MenuBar.actionMap");
    }

    InputMap getInputMap(int i2) {
        Object[] objArr;
        if (i2 == 2 && (objArr = (Object[]) DefaultLookup.get(this.menuBar, this, "MenuBar.windowBindings")) != null) {
            return LookAndFeel.makeComponentInputMap(this.menuBar, objArr);
        }
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults();
        uninstallListeners();
        uninstallKeyboardActions();
        this.menuBar = null;
    }

    protected void uninstallDefaults() {
        if (this.menuBar != null) {
            LookAndFeel.uninstallBorder(this.menuBar);
        }
    }

    protected void uninstallListeners() {
        this.menuBar.removeContainerListener(this.containerListener);
        for (int i2 = 0; i2 < this.menuBar.getMenuCount(); i2++) {
            JMenu menu = this.menuBar.getMenu(i2);
            if (menu != null) {
                menu.getModel().removeChangeListener(this.changeListener);
            }
        }
        this.containerListener = null;
        this.changeListener = null;
        this.handler = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.menuBar, 2, null);
        SwingUtilities.replaceUIActionMap(this.menuBar, null);
    }

    protected ContainerListener createContainerListener() {
        return getHandler();
    }

    protected ChangeListener createChangeListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return null;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuBarUI$Handler.class */
    private class Handler implements ChangeListener, ContainerListener {
        private Handler() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            int menuCount = BasicMenuBarUI.this.menuBar.getMenuCount();
            for (int i2 = 0; i2 < menuCount; i2++) {
                JMenu menu = BasicMenuBarUI.this.menuBar.getMenu(i2);
                if (menu != null && menu.isSelected()) {
                    BasicMenuBarUI.this.menuBar.getSelectionModel().setSelectedIndex(i2);
                    return;
                }
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) {
            Component child = containerEvent.getChild();
            if (child instanceof JMenu) {
                ((JMenu) child).getModel().addChangeListener(BasicMenuBarUI.this.changeListener);
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) {
            Component child = containerEvent.getChild();
            if (child instanceof JMenu) {
                ((JMenu) child).getModel().removeChangeListener(BasicMenuBarUI.this.changeListener);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuBarUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String TAKE_FOCUS = "takeFocus";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JMenuBar jMenuBar = (JMenuBar) actionEvent.getSource();
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            JMenu menu = jMenuBar.getMenu(0);
            if (menu != null) {
                menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{jMenuBar, menu, menu.getPopupMenu()});
            }
        }
    }
}
