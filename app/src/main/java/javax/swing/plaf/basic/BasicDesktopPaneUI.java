package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.AbstractAction;
import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SortingFocusTraversalPolicy;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DesktopPaneUI;
import javax.swing.plaf.UIResource;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI.class */
public class BasicDesktopPaneUI extends DesktopPaneUI {
    private static final Actions SHARED_ACTION = new Actions();
    private Handler handler;
    private PropertyChangeListener pcl;
    protected JDesktopPane desktop;
    protected DesktopManager desktopManager;

    @Deprecated
    protected KeyStroke minimizeKey;

    @Deprecated
    protected KeyStroke maximizeKey;

    @Deprecated
    protected KeyStroke closeKey;

    @Deprecated
    protected KeyStroke navigateKey;

    @Deprecated
    protected KeyStroke navigateKey2;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicDesktopPaneUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.desktop = (JDesktopPane) jComponent;
        installDefaults();
        installDesktopManager();
        installListeners();
        installKeyboardActions();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallKeyboardActions();
        uninstallListeners();
        uninstallDesktopManager();
        uninstallDefaults();
        this.desktop = null;
        this.handler = null;
    }

    protected void installDefaults() {
        if (this.desktop.getBackground() == null || (this.desktop.getBackground() instanceof UIResource)) {
            this.desktop.setBackground(UIManager.getColor("Desktop.background"));
        }
        LookAndFeel.installProperty(this.desktop, "opaque", Boolean.TRUE);
    }

    protected void uninstallDefaults() {
    }

    protected void installListeners() {
        this.pcl = createPropertyChangeListener();
        this.desktop.addPropertyChangeListener(this.pcl);
    }

    protected void uninstallListeners() {
        this.desktop.removePropertyChangeListener(this.pcl);
        this.pcl = null;
    }

    protected void installDesktopManager() {
        this.desktopManager = this.desktop.getDesktopManager();
        if (this.desktopManager == null) {
            this.desktopManager = new BasicDesktopManager();
            this.desktop.setDesktopManager(this.desktopManager);
        }
    }

    protected void uninstallDesktopManager() {
        if (this.desktop.getDesktopManager() instanceof UIResource) {
            this.desktop.setDesktopManager(null);
        }
        this.desktopManager = null;
    }

    protected void installKeyboardActions() {
        InputMap inputMap = getInputMap(2);
        if (inputMap != null) {
            SwingUtilities.replaceUIInputMap(this.desktop, 2, inputMap);
        }
        InputMap inputMap2 = getInputMap(1);
        if (inputMap2 != null) {
            SwingUtilities.replaceUIInputMap(this.desktop, 1, inputMap2);
        }
        LazyActionMap.installLazyActionMap(this.desktop, BasicDesktopPaneUI.class, "DesktopPane.actionMap");
        registerKeyboardActions();
    }

    protected void registerKeyboardActions() {
    }

    protected void unregisterKeyboardActions() {
    }

    InputMap getInputMap(int i2) {
        if (i2 == 2) {
            return createInputMap(i2);
        }
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(this.desktop, this, "Desktop.ancestorInputMap");
        }
        return null;
    }

    InputMap createInputMap(int i2) {
        Object[] objArr;
        if (i2 == 2 && (objArr = (Object[]) DefaultLookup.get(this.desktop, this, "Desktop.windowBindings")) != null) {
            return LookAndFeel.makeComponentInputMap(this.desktop, objArr);
        }
        return null;
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions(Actions.RESTORE));
        lazyActionMap.put(new Actions(Actions.CLOSE));
        lazyActionMap.put(new Actions(Actions.MOVE));
        lazyActionMap.put(new Actions(Actions.RESIZE));
        lazyActionMap.put(new Actions(Actions.LEFT));
        lazyActionMap.put(new Actions(Actions.SHRINK_LEFT));
        lazyActionMap.put(new Actions(Actions.RIGHT));
        lazyActionMap.put(new Actions(Actions.SHRINK_RIGHT));
        lazyActionMap.put(new Actions(Actions.UP));
        lazyActionMap.put(new Actions(Actions.SHRINK_UP));
        lazyActionMap.put(new Actions(Actions.DOWN));
        lazyActionMap.put(new Actions(Actions.SHRINK_DOWN));
        lazyActionMap.put(new Actions(Actions.ESCAPE));
        lazyActionMap.put(new Actions(Actions.MINIMIZE));
        lazyActionMap.put(new Actions(Actions.MAXIMIZE));
        lazyActionMap.put(new Actions(Actions.NEXT_FRAME));
        lazyActionMap.put(new Actions(Actions.PREVIOUS_FRAME));
        lazyActionMap.put(new Actions(Actions.NAVIGATE_NEXT));
        lazyActionMap.put(new Actions(Actions.NAVIGATE_PREVIOUS));
    }

    protected void uninstallKeyboardActions() {
        unregisterKeyboardActions();
        SwingUtilities.replaceUIInputMap(this.desktop, 2, null);
        SwingUtilities.replaceUIInputMap(this.desktop, 1, null);
        SwingUtilities.replaceUIActionMap(this.desktop, null);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        return new Dimension(0, 0);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    private Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$Handler.class */
    private class Handler implements PropertyChangeListener {
        private Handler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("desktopManager" == propertyChangeEvent.getPropertyName()) {
                BasicDesktopPaneUI.this.installDesktopManager();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$BasicDesktopManager.class */
    private class BasicDesktopManager extends DefaultDesktopManager implements UIResource {
        private BasicDesktopManager() {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$Actions.class */
    private static class Actions extends UIAction {
        private final int MOVE_RESIZE_INCREMENT = 10;
        private static String CLOSE = "close";
        private static String ESCAPE = "escape";
        private static String MAXIMIZE = "maximize";
        private static String MINIMIZE = "minimize";
        private static String MOVE = "move";
        private static String RESIZE = "resize";
        private static String RESTORE = "restore";
        private static String LEFT = JSplitPane.LEFT;
        private static String RIGHT = JSplitPane.RIGHT;
        private static String UP = "up";
        private static String DOWN = "down";
        private static String SHRINK_LEFT = "shrinkLeft";
        private static String SHRINK_RIGHT = "shrinkRight";
        private static String SHRINK_UP = "shrinkUp";
        private static String SHRINK_DOWN = "shrinkDown";
        private static String NEXT_FRAME = "selectNextFrame";
        private static String PREVIOUS_FRAME = "selectPreviousFrame";
        private static String NAVIGATE_NEXT = "navigateNext";
        private static String NAVIGATE_PREVIOUS = "navigatePrevious";
        private static boolean moving = false;
        private static boolean resizing = false;
        private static JInternalFrame sourceFrame = null;
        private static Component focusOwner = null;

        Actions() {
            super(null);
            this.MOVE_RESIZE_INCREMENT = 10;
        }

        Actions(String str) {
            super(str);
            this.MOVE_RESIZE_INCREMENT = 10;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            int i2;
            int i3;
            int i4;
            int i5;
            FocusTraversalPolicy focusTraversalPolicy;
            JDesktopPane jDesktopPane = (JDesktopPane) actionEvent.getSource();
            String name = getName();
            if (CLOSE == name || MAXIMIZE == name || MINIMIZE == name || RESTORE == name) {
                setState(jDesktopPane, name);
                return;
            }
            if (ESCAPE == name) {
                if (sourceFrame == jDesktopPane.getSelectedFrame() && focusOwner != null) {
                    focusOwner.requestFocus();
                }
                moving = false;
                resizing = false;
                sourceFrame = null;
                focusOwner = null;
                return;
            }
            if (MOVE == name || RESIZE == name) {
                sourceFrame = jDesktopPane.getSelectedFrame();
                if (sourceFrame == null) {
                    return;
                }
                moving = name == MOVE;
                resizing = name == RESIZE;
                focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                if (!SwingUtilities.isDescendingFrom(focusOwner, sourceFrame)) {
                    focusOwner = null;
                }
                sourceFrame.requestFocus();
                return;
            }
            if (LEFT == name || RIGHT == name || UP == name || DOWN == name || SHRINK_RIGHT == name || SHRINK_LEFT == name || SHRINK_UP == name || SHRINK_DOWN == name) {
                JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
                if (sourceFrame == null || selectedFrame != sourceFrame || KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != sourceFrame) {
                    return;
                }
                Insets insets = UIManager.getInsets("Desktop.minOnScreenInsets");
                Dimension size = selectedFrame.getSize();
                Dimension minimumSize = selectedFrame.getMinimumSize();
                int width = jDesktopPane.getWidth();
                int height = jDesktopPane.getHeight();
                Point location = selectedFrame.getLocation();
                if (LEFT == name) {
                    if (moving) {
                        selectedFrame.setLocation((location.f12370x + size.width) - 10 < insets.right ? (-size.width) + insets.right : location.f12370x - 10, location.f12371y);
                        return;
                    } else {
                        if (resizing) {
                            selectedFrame.setLocation(location.f12370x - 10, location.f12371y);
                            selectedFrame.setSize(size.width + 10, size.height);
                            return;
                        }
                        return;
                    }
                }
                if (RIGHT == name) {
                    if (moving) {
                        selectedFrame.setLocation(location.f12370x + 10 > width - insets.left ? width - insets.left : location.f12370x + 10, location.f12371y);
                        return;
                    } else {
                        if (resizing) {
                            selectedFrame.setSize(size.width + 10, size.height);
                            return;
                        }
                        return;
                    }
                }
                if (UP == name) {
                    if (moving) {
                        selectedFrame.setLocation(location.f12370x, (location.f12371y + size.height) - 10 < insets.bottom ? (-size.height) + insets.bottom : location.f12371y - 10);
                        return;
                    } else {
                        if (resizing) {
                            selectedFrame.setLocation(location.f12370x, location.f12371y - 10);
                            selectedFrame.setSize(size.width, size.height + 10);
                            return;
                        }
                        return;
                    }
                }
                if (DOWN == name) {
                    if (moving) {
                        selectedFrame.setLocation(location.f12370x, location.f12371y + 10 > height - insets.top ? height - insets.top : location.f12371y + 10);
                        return;
                    } else {
                        if (resizing) {
                            selectedFrame.setSize(size.width, size.height + 10);
                            return;
                        }
                        return;
                    }
                }
                if (SHRINK_LEFT == name && resizing) {
                    if (minimumSize.width < size.width - 10) {
                        i5 = 10;
                    } else {
                        i5 = size.width - minimumSize.width;
                    }
                    if ((location.f12370x + size.width) - i5 < insets.left) {
                        i5 = (location.f12370x + size.width) - insets.left;
                    }
                    selectedFrame.setSize(size.width - i5, size.height);
                    return;
                }
                if (SHRINK_RIGHT == name && resizing) {
                    if (minimumSize.width < size.width - 10) {
                        i4 = 10;
                    } else {
                        i4 = size.width - minimumSize.width;
                    }
                    if (location.f12370x + i4 > width - insets.right) {
                        i4 = (width - insets.right) - location.f12370x;
                    }
                    selectedFrame.setLocation(location.f12370x + i4, location.f12371y);
                    selectedFrame.setSize(size.width - i4, size.height);
                    return;
                }
                if (SHRINK_UP == name && resizing) {
                    if (minimumSize.height < size.height - 10) {
                        i3 = 10;
                    } else {
                        i3 = size.height - minimumSize.height;
                    }
                    if ((location.f12371y + size.height) - i3 < insets.bottom) {
                        i3 = (location.f12371y + size.height) - insets.bottom;
                    }
                    selectedFrame.setSize(size.width, size.height - i3);
                    return;
                }
                if (SHRINK_DOWN == name && resizing) {
                    if (minimumSize.height < size.height - 10) {
                        i2 = 10;
                    } else {
                        i2 = size.height - minimumSize.height;
                    }
                    if (location.f12371y + i2 > height - insets.top) {
                        i2 = (height - insets.top) - location.f12371y;
                    }
                    selectedFrame.setLocation(location.f12370x, location.f12371y + i2);
                    selectedFrame.setSize(size.width, size.height - i2);
                    return;
                }
                return;
            }
            if (NEXT_FRAME == name || PREVIOUS_FRAME == name) {
                jDesktopPane.selectFrame(name == NEXT_FRAME);
                return;
            }
            if (NAVIGATE_NEXT == name || NAVIGATE_PREVIOUS == name) {
                boolean z2 = true;
                if (NAVIGATE_PREVIOUS == name) {
                    z2 = false;
                }
                Container focusCycleRootAncestor = jDesktopPane.getFocusCycleRootAncestor();
                if (focusCycleRootAncestor != null && (focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy()) != null && (focusTraversalPolicy instanceof SortingFocusTraversalPolicy)) {
                    SortingFocusTraversalPolicy sortingFocusTraversalPolicy = (SortingFocusTraversalPolicy) focusTraversalPolicy;
                    boolean implicitDownCycleTraversal = sortingFocusTraversalPolicy.getImplicitDownCycleTraversal();
                    try {
                        sortingFocusTraversalPolicy.setImplicitDownCycleTraversal(false);
                        if (z2) {
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(jDesktopPane);
                        } else {
                            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(jDesktopPane);
                        }
                    } finally {
                        sortingFocusTraversalPolicy.setImplicitDownCycleTraversal(implicitDownCycleTraversal);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setState(JDesktopPane jDesktopPane, String str) {
            JInternalFrame selectedFrame;
            if (str == CLOSE) {
                JInternalFrame selectedFrame2 = jDesktopPane.getSelectedFrame();
                if (selectedFrame2 == null) {
                    return;
                }
                selectedFrame2.doDefaultCloseAction();
                return;
            }
            if (str == MAXIMIZE) {
                JInternalFrame selectedFrame3 = jDesktopPane.getSelectedFrame();
                if (selectedFrame3 != null && !selectedFrame3.isMaximum()) {
                    if (selectedFrame3.isIcon()) {
                        try {
                            selectedFrame3.setIcon(false);
                            selectedFrame3.setMaximum(true);
                            return;
                        } catch (PropertyVetoException e2) {
                            return;
                        }
                    } else {
                        try {
                            selectedFrame3.setMaximum(true);
                            return;
                        } catch (PropertyVetoException e3) {
                            return;
                        }
                    }
                }
                return;
            }
            if (str == MINIMIZE) {
                JInternalFrame selectedFrame4 = jDesktopPane.getSelectedFrame();
                if (selectedFrame4 != null && !selectedFrame4.isIcon()) {
                    try {
                        selectedFrame4.setIcon(true);
                        return;
                    } catch (PropertyVetoException e4) {
                        return;
                    }
                }
                return;
            }
            if (str != RESTORE || (selectedFrame = jDesktopPane.getSelectedFrame()) == null) {
                return;
            }
            try {
                if (selectedFrame.isIcon()) {
                    selectedFrame.setIcon(false);
                } else if (selectedFrame.isMaximum()) {
                    selectedFrame.setMaximum(false);
                }
                selectedFrame.setSelected(true);
            } catch (PropertyVetoException e5) {
            }
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if (obj instanceof JDesktopPane) {
                JDesktopPane jDesktopPane = (JDesktopPane) obj;
                String name = getName();
                if (name == NEXT_FRAME || name == PREVIOUS_FRAME) {
                    return true;
                }
                JInternalFrame selectedFrame = jDesktopPane.getSelectedFrame();
                if (selectedFrame == null) {
                    return false;
                }
                if (name == CLOSE) {
                    return selectedFrame.isClosable();
                }
                if (name == MINIMIZE) {
                    return selectedFrame.isIconifiable();
                }
                if (name == MAXIMIZE) {
                    return selectedFrame.isMaximizable();
                }
                return true;
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$OpenAction.class */
    protected class OpenAction extends AbstractAction {
        protected OpenAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicDesktopPaneUI.SHARED_ACTION.setState((JDesktopPane) actionEvent.getSource(), Actions.RESTORE);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$CloseAction.class */
    protected class CloseAction extends AbstractAction {
        protected CloseAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicDesktopPaneUI.SHARED_ACTION.setState((JDesktopPane) actionEvent.getSource(), Actions.CLOSE);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            JInternalFrame selectedFrame = BasicDesktopPaneUI.this.desktop.getSelectedFrame();
            if (selectedFrame != null) {
                return selectedFrame.isClosable();
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$MinimizeAction.class */
    protected class MinimizeAction extends AbstractAction {
        protected MinimizeAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicDesktopPaneUI.SHARED_ACTION.setState((JDesktopPane) actionEvent.getSource(), Actions.MINIMIZE);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            JInternalFrame selectedFrame = BasicDesktopPaneUI.this.desktop.getSelectedFrame();
            if (selectedFrame != null) {
                return selectedFrame.isIconifiable();
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$MaximizeAction.class */
    protected class MaximizeAction extends AbstractAction {
        protected MaximizeAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicDesktopPaneUI.SHARED_ACTION.setState((JDesktopPane) actionEvent.getSource(), Actions.MAXIMIZE);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            JInternalFrame selectedFrame = BasicDesktopPaneUI.this.desktop.getSelectedFrame();
            if (selectedFrame != null) {
                return selectedFrame.isMaximizable();
            }
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicDesktopPaneUI$NavigateAction.class */
    protected class NavigateAction extends AbstractAction {
        protected NavigateAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            ((JDesktopPane) actionEvent.getSource()).selectFrame(true);
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return true;
        }
    }
}
