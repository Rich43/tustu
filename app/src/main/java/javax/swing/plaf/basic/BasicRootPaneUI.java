package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.RootPaneUI;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicRootPaneUI.class */
public class BasicRootPaneUI extends RootPaneUI implements PropertyChangeListener {
    private static RootPaneUI rootPaneUI = new BasicRootPaneUI();

    public static ComponentUI createUI(JComponent jComponent) {
        return rootPaneUI;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        installDefaults((JRootPane) jComponent);
        installComponents((JRootPane) jComponent);
        installListeners((JRootPane) jComponent);
        installKeyboardActions((JRootPane) jComponent);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults((JRootPane) jComponent);
        uninstallComponents((JRootPane) jComponent);
        uninstallListeners((JRootPane) jComponent);
        uninstallKeyboardActions((JRootPane) jComponent);
    }

    protected void installDefaults(JRootPane jRootPane) {
        LookAndFeel.installProperty(jRootPane, "opaque", Boolean.FALSE);
    }

    protected void installComponents(JRootPane jRootPane) {
    }

    protected void installListeners(JRootPane jRootPane) {
        jRootPane.addPropertyChangeListener(this);
    }

    protected void installKeyboardActions(JRootPane jRootPane) {
        SwingUtilities.replaceUIInputMap(jRootPane, 2, getInputMap(2, jRootPane));
        SwingUtilities.replaceUIInputMap(jRootPane, 1, getInputMap(1, jRootPane));
        LazyActionMap.installLazyActionMap(jRootPane, BasicRootPaneUI.class, "RootPane.actionMap");
        updateDefaultButtonBindings(jRootPane);
    }

    protected void uninstallDefaults(JRootPane jRootPane) {
    }

    protected void uninstallComponents(JRootPane jRootPane) {
    }

    protected void uninstallListeners(JRootPane jRootPane) {
        jRootPane.removePropertyChangeListener(this);
    }

    protected void uninstallKeyboardActions(JRootPane jRootPane) {
        SwingUtilities.replaceUIInputMap(jRootPane, 2, null);
        SwingUtilities.replaceUIActionMap(jRootPane, null);
    }

    InputMap getInputMap(int i2, JComponent jComponent) {
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(jComponent, this, "RootPane.ancestorInputMap");
        }
        if (i2 == 2) {
            return createInputMap(i2, jComponent);
        }
        return null;
    }

    ComponentInputMap createInputMap(int i2, JComponent jComponent) {
        return new RootPaneInputMap(jComponent);
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions(Actions.PRESS));
        lazyActionMap.put(new Actions(Actions.RELEASE));
        lazyActionMap.put(new Actions(Actions.POST_POPUP));
    }

    void updateDefaultButtonBindings(JRootPane jRootPane) {
        InputMap inputMap;
        Object[] objArr;
        InputMap uIInputMap = SwingUtilities.getUIInputMap(jRootPane, 2);
        while (true) {
            inputMap = uIInputMap;
            if (inputMap == null || (inputMap instanceof RootPaneInputMap)) {
                break;
            } else {
                uIInputMap = inputMap.getParent();
            }
        }
        if (inputMap != null) {
            inputMap.clear();
            if (jRootPane.getDefaultButton() != null && (objArr = (Object[]) DefaultLookup.get(jRootPane, this, "RootPane.defaultButtonWindowKeyBindings")) != null) {
                LookAndFeel.loadKeyBindings(inputMap, objArr);
            }
        }
    }

    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("defaultButton")) {
            JRootPane jRootPane = (JRootPane) propertyChangeEvent.getSource();
            updateDefaultButtonBindings(jRootPane);
            if (jRootPane.getClientProperty("temporaryDefaultButton") == null) {
                jRootPane.putClientProperty("initialDefaultButton", propertyChangeEvent.getNewValue());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicRootPaneUI$Actions.class */
    static class Actions extends UIAction {
        public static final String PRESS = "press";
        public static final String RELEASE = "release";
        public static final String POST_POPUP = "postPopup";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JComponent jComponent;
            JPopupMenu componentPopupMenu;
            JRootPane jRootPane = (JRootPane) actionEvent.getSource();
            JButton defaultButton = jRootPane.getDefaultButton();
            String name = getName();
            if (name == POST_POPUP) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                if ((focusOwner instanceof JComponent) && (componentPopupMenu = (jComponent = (JComponent) focusOwner).getComponentPopupMenu()) != null) {
                    Point popupLocation = jComponent.getPopupLocation(null);
                    if (popupLocation == null) {
                        Rectangle visibleRect = jComponent.getVisibleRect();
                        popupLocation = new Point(visibleRect.f12372x + (visibleRect.width / 2), visibleRect.f12373y + (visibleRect.height / 2));
                    }
                    componentPopupMenu.show(focusOwner, popupLocation.f12370x, popupLocation.f12371y);
                    return;
                }
                return;
            }
            if (defaultButton != null && SwingUtilities.getRootPane(defaultButton) == jRootPane && name == PRESS) {
                defaultButton.doClick(20);
            }
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if (getName() == POST_POPUP) {
                MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
                if (selectedPath != null && selectedPath.length != 0) {
                    return false;
                }
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                return (focusOwner instanceof JComponent) && ((JComponent) focusOwner).getComponentPopupMenu() != null;
            }
            if (obj != null && (obj instanceof JRootPane)) {
                JButton defaultButton = ((JRootPane) obj).getDefaultButton();
                return defaultButton != null && defaultButton.getModel().isEnabled();
            }
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicRootPaneUI$RootPaneInputMap.class */
    private static class RootPaneInputMap extends ComponentInputMapUIResource {
        public RootPaneInputMap(JComponent jComponent) {
            super(jComponent);
        }
    }
}
