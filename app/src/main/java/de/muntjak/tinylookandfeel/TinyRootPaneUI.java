package de.muntjak.tinylookandfeel;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRootPaneUI.class */
public class TinyRootPaneUI extends BasicRootPaneUI implements FocusListener {
    private static final int CORNER_DRAG_WIDTH = 16;
    private static final int BORDER_DRAG_THICKNESS = 5;
    private Window window;
    private JComponent titlePane;
    private MouseInputListener mouseInputListener;
    private LayoutManager layoutManager;
    private LayoutManager savedOldLayout;
    private JRootPane root;
    private Cursor lastCursor = Cursor.getPredefinedCursor(0);
    private AWTEventListener mouseHandler = new MouseHandler(this, null);
    private KeyEventPostProcessor keyPostProcessor = new KeyPostProcessor(this, null);
    private boolean topMenuToClose = false;
    private Vector registeredKeyCodes;
    private static final boolean PARENT = false;
    private static final boolean CHILD = true;
    private static final boolean FORWARD = true;
    private static final boolean BACKWARD = false;
    private static final String CANCEL = "cancel";
    private static final String RETURN = "return";
    private static final String SELECT_PARENT = "selectParent";
    private static final String SELECT_CHILD = "selectChild";
    private static final String[] borderKeys = {null, "RootPane.frameBorder", "RootPane.plainDialogBorder", "RootPane.informationDialogBorder", "RootPane.errorDialogBorder", "RootPane.colorChooserDialogBorder", "RootPane.fileChooserDialogBorder", "RootPane.questionDialogBorder", "RootPane.warningDialogBorder"};
    private static final int[] cursorMapping = {6, 6, 8, 7, 7, 6, 0, 0, 0, 7, 10, 0, 0, 0, 11, 4, 0, 0, 0, 5, 4, 4, 9, 5, 5};

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRootPaneUI$KeyPostProcessor.class */
    private class KeyPostProcessor implements KeyEventPostProcessor {
        private final TinyRootPaneUI this$0;

        private KeyPostProcessor(TinyRootPaneUI tinyRootPaneUI) {
            this.this$0 = tinyRootPaneUI;
        }

        @Override // java.awt.KeyEventPostProcessor
        public boolean postProcessKeyEvent(KeyEvent keyEvent) {
            if (!this.this$0.topMenuToClose || keyEvent.getID() != 401 || keyEvent.getKeyCode() == 27) {
                return false;
            }
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            Iterator it = this.this$0.registeredKeyCodes.iterator();
            while (it.hasNext()) {
                if (((Integer) it.next()).intValue() == keyEvent.getKeyCode()) {
                    if (selectedPath.length <= 2) {
                        return false;
                    }
                    this.this$0.topMenuToClose = false;
                    this.this$0.removeEscapeMenuHandlers();
                    return false;
                }
            }
            if (selectedPath.length == 2) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
            }
            this.this$0.topMenuToClose = false;
            this.this$0.removeEscapeMenuHandlers();
            return false;
        }

        KeyPostProcessor(TinyRootPaneUI tinyRootPaneUI, AnonymousClass1 anonymousClass1) {
            this(tinyRootPaneUI);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRootPaneUI$MenuActions.class */
    private class MenuActions extends AbstractAction {
        private String name;
        private final TinyRootPaneUI this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        MenuActions(TinyRootPaneUI tinyRootPaneUI, String str) {
            super(str);
            this.this$0 = tinyRootPaneUI;
            this.name = str;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (TinyRootPaneUI.CANCEL.equals(this.name)) {
                cancel();
                return;
            }
            if (TinyRootPaneUI.SELECT_PARENT.equals(this.name)) {
                selectParentChild(false);
            } else if (TinyRootPaneUI.SELECT_CHILD.equals(this.name)) {
                selectParentChild(true);
            } else if ("return".equals(this.name)) {
                doReturn();
            }
        }

        private void cancel() {
            JPopupMenu lastPopup = getLastPopup();
            if (lastPopup != null) {
                lastPopup.putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
            }
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (selectedPath.length <= 2) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
                if (this.this$0.topMenuToClose) {
                    this.this$0.topMenuToClose = false;
                    this.this$0.removeEscapeMenuHandlers();
                    return;
                }
                return;
            }
            int iMax = Math.max(2, selectedPath.length - 2);
            MenuElement[] menuElementArr = new MenuElement[iMax];
            System.arraycopy(selectedPath, 0, menuElementArr, 0, iMax);
            MenuSelectionManager.defaultManager().setSelectedPath(menuElementArr);
            if (iMax != 2 || this.this$0.topMenuToClose) {
                return;
            }
            this.this$0.topMenuToClose = true;
            this.this$0.addEscapeMenuHandlers();
        }

        private void doReturn() {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (focusOwner == null || (focusOwner instanceof JRootPane)) {
                MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
                MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
                if (selectedPath.length > 0) {
                    MenuElement menuElement = selectedPath[selectedPath.length - 1];
                    if (!(menuElement instanceof JMenu)) {
                        if (menuElement instanceof JMenuItem) {
                            JMenuItem jMenuItem = (JMenuItem) menuElement;
                            if (jMenuItem.getUI() instanceof TinyMenuItemUI) {
                                ((TinyMenuItemUI) jMenuItem.getUI()).doClick(menuSelectionManagerDefaultManager);
                                return;
                            } else {
                                menuSelectionManagerDefaultManager.clearSelectedPath();
                                jMenuItem.doClick(0);
                                return;
                            }
                        }
                        return;
                    }
                    JPopupMenu popupMenu = ((JMenu) menuElement).getPopupMenu();
                    MenuElement menuElementFindEnabledChild = findEnabledChild(popupMenu.getSubElements(), -1, true);
                    if (menuElementFindEnabledChild == null) {
                        MenuElement[] menuElementArr = new MenuElement[selectedPath.length + 1];
                        System.arraycopy(selectedPath, 0, menuElementArr, 0, selectedPath.length);
                        menuElementArr[selectedPath.length] = popupMenu;
                        menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
                        return;
                    }
                    MenuElement[] menuElementArr2 = new MenuElement[selectedPath.length + 2];
                    System.arraycopy(selectedPath, 0, menuElementArr2, 0, selectedPath.length);
                    menuElementArr2[selectedPath.length] = popupMenu;
                    menuElementArr2[selectedPath.length + 1] = menuElementFindEnabledChild;
                    menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr2);
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0034 A[PHI: r11
  0x0034: PHI (r11v3 int) = (r11v2 int), (r11v4 int) binds: [B:7:0x0024, B:9:0x0031] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void selectParentChild(boolean r7) {
            /*
                Method dump skipped, instructions count: 394
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: de.muntjak.tinylookandfeel.TinyRootPaneUI.MenuActions.selectParentChild(boolean):void");
        }

        private void selectItem(boolean z2) {
            MenuElement menuElementFindEnabledChild;
            MenuElement[] menuElementArr;
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length < 2) {
                return;
            }
            int length = selectedPath.length;
            if ((selectedPath[0] instanceof JMenuBar) && (selectedPath[1] instanceof JMenu) && length == 2) {
                JPopupMenu popupMenu = ((JMenu) selectedPath[1]).getPopupMenu();
                MenuElement menuElementFindEnabledChild2 = findEnabledChild(popupMenu.getSubElements(), -1, true);
                if (menuElementFindEnabledChild2 != null) {
                    menuElementArr = new MenuElement[4];
                    menuElementArr[3] = menuElementFindEnabledChild2;
                } else {
                    menuElementArr = new MenuElement[3];
                }
                System.arraycopy(selectedPath, 0, menuElementArr, 0, 2);
                menuElementArr[2] = popupMenu;
                menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
                return;
            }
            if (!(selectedPath[length - 1] instanceof JPopupMenu) || !(selectedPath[length - 2] instanceof JMenu)) {
                MenuElement[] subElements = selectedPath[length - 2].getSubElements();
                MenuElement menuElementFindEnabledChild3 = findEnabledChild(subElements, selectedPath[length - 1], z2);
                if (menuElementFindEnabledChild3 == null) {
                    menuElementFindEnabledChild3 = findEnabledChild(subElements, -1, z2);
                }
                if (menuElementFindEnabledChild3 != null) {
                    selectedPath[length - 1] = menuElementFindEnabledChild3;
                    menuSelectionManagerDefaultManager.setSelectedPath(selectedPath);
                    return;
                }
                return;
            }
            JMenu jMenu = (JMenu) selectedPath[length - 2];
            MenuElement menuElementFindEnabledChild4 = findEnabledChild(jMenu.getPopupMenu().getSubElements(), -1, z2);
            if (menuElementFindEnabledChild4 != null) {
                MenuElement[] menuElementArr2 = new MenuElement[length + 1];
                System.arraycopy(selectedPath, 0, menuElementArr2, 0, length);
                menuElementArr2[length] = menuElementFindEnabledChild4;
                menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr2);
                return;
            }
            if (length <= 2 || !(selectedPath[length - 3] instanceof JPopupMenu) || (menuElementFindEnabledChild = findEnabledChild(((JPopupMenu) selectedPath[length - 3]).getSubElements(), jMenu, z2)) == null || menuElementFindEnabledChild == jMenu) {
                return;
            }
            MenuElement[] menuElementArr3 = new MenuElement[length - 1];
            System.arraycopy(selectedPath, 0, menuElementArr3, 0, length - 2);
            menuElementArr3[length - 2] = menuElementFindEnabledChild;
            menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr3);
        }

        private JPopupMenu getLastPopup() {
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            JPopupMenu jPopupMenu = null;
            for (int length = selectedPath.length - 1; jPopupMenu == null && length >= 0; length--) {
                if (selectedPath[length] instanceof JPopupMenu) {
                    jPopupMenu = (JPopupMenu) selectedPath[length];
                }
            }
            return jPopupMenu;
        }

        private MenuElement findEnabledChild(MenuElement[] menuElementArr, int i2, boolean z2) {
            MenuElement menuElementPreviousEnabledChild;
            if (z2) {
                menuElementPreviousEnabledChild = nextEnabledChild(menuElementArr, i2 + 1, menuElementArr.length - 1);
                if (menuElementPreviousEnabledChild == null) {
                    menuElementPreviousEnabledChild = nextEnabledChild(menuElementArr, 0, i2 - 1);
                }
            } else {
                menuElementPreviousEnabledChild = previousEnabledChild(menuElementArr, i2 - 1, 0);
                if (menuElementPreviousEnabledChild == null) {
                    menuElementPreviousEnabledChild = previousEnabledChild(menuElementArr, menuElementArr.length - 1, i2 + 1);
                }
            }
            return menuElementPreviousEnabledChild;
        }

        private MenuElement nextEnabledChild(MenuElement[] menuElementArr, int i2, int i3) {
            Component component;
            for (int i4 = i2; i4 <= i3; i4++) {
                if (menuElementArr[i4] != null && (component = menuElementArr[i4].getComponent()) != null && ((component.isEnabled() || UIManager.getBoolean("MenuItem.disabledAreNavigable")) && component.isVisible())) {
                    return menuElementArr[i4];
                }
            }
            return null;
        }

        private MenuElement previousEnabledChild(MenuElement[] menuElementArr, int i2, int i3) {
            Component component;
            for (int i4 = i2; i4 >= i3; i4--) {
                if (menuElementArr[i4] != null && (component = menuElementArr[i4].getComponent()) != null && ((component.isEnabled() || UIManager.getBoolean("MenuItem.disabledAreNavigable")) && component.isVisible())) {
                    return menuElementArr[i4];
                }
            }
            return null;
        }

        private MenuElement findEnabledChild(MenuElement[] menuElementArr, MenuElement menuElement, boolean z2) {
            for (int i2 = 0; i2 < menuElementArr.length; i2++) {
                if (menuElementArr[i2] == menuElement) {
                    return findEnabledChild(menuElementArr, i2, z2);
                }
            }
            return null;
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRootPaneUI$MetalRootLayout.class */
    private static class MetalRootLayout implements LayoutManager2 {
        private MetalRootLayout() {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            JComponent titlePane;
            Dimension preferredSize;
            Dimension preferredSize2;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            Insets insets = container.getInsets();
            JRootPane jRootPane = (JRootPane) container;
            Dimension preferredSize3 = jRootPane.getContentPane() != null ? jRootPane.getContentPane().getPreferredSize() : jRootPane.getSize();
            if (preferredSize3 != null) {
                i2 = preferredSize3.width;
                i3 = preferredSize3.height;
            }
            if (jRootPane.getJMenuBar() != null && (preferredSize2 = jRootPane.getJMenuBar().getPreferredSize()) != null) {
                i4 = preferredSize2.width;
                i5 = preferredSize2.height;
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof TinyRootPaneUI) && (titlePane = ((TinyRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (preferredSize = titlePane.getPreferredSize()) != null) {
                i6 = preferredSize.width;
                i7 = preferredSize.height;
            }
            return new Dimension(Math.max(Math.max(i2, i4), i6) + insets.left + insets.right, i3 + i5 + i7 + insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            JComponent titlePane;
            Dimension minimumSize;
            Dimension minimumSize2;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            int i7 = 0;
            Insets insets = container.getInsets();
            JRootPane jRootPane = (JRootPane) container;
            Dimension minimumSize3 = jRootPane.getContentPane() != null ? jRootPane.getContentPane().getMinimumSize() : jRootPane.getSize();
            if (minimumSize3 != null) {
                i2 = minimumSize3.width;
                i3 = minimumSize3.height;
            }
            if (jRootPane.getJMenuBar() != null && (minimumSize2 = jRootPane.getJMenuBar().getMinimumSize()) != null) {
                i4 = minimumSize2.width;
                i5 = minimumSize2.height;
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof TinyRootPaneUI) && (titlePane = ((TinyRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (minimumSize = titlePane.getMinimumSize()) != null) {
                i6 = minimumSize.width;
                i7 = minimumSize.height;
            }
            return new Dimension(Math.max(Math.max(i2, i4), i6) + insets.left + insets.right, i3 + i5 + i7 + insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager2
        public Dimension maximumLayoutSize(Container container) {
            JComponent titlePane;
            Dimension maximumSize;
            Dimension maximumSize2;
            Dimension maximumSize3;
            int i2 = Integer.MAX_VALUE;
            int i3 = Integer.MAX_VALUE;
            int i4 = Integer.MAX_VALUE;
            int i5 = Integer.MAX_VALUE;
            int i6 = Integer.MAX_VALUE;
            int i7 = Integer.MAX_VALUE;
            Insets insets = container.getInsets();
            JRootPane jRootPane = (JRootPane) container;
            if (jRootPane.getContentPane() != null && (maximumSize3 = jRootPane.getContentPane().getMaximumSize()) != null) {
                i2 = maximumSize3.width;
                i3 = maximumSize3.height;
            }
            if (jRootPane.getJMenuBar() != null && (maximumSize2 = jRootPane.getJMenuBar().getMaximumSize()) != null) {
                i4 = maximumSize2.width;
                i5 = maximumSize2.height;
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof TinyRootPaneUI) && (titlePane = ((TinyRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (maximumSize = titlePane.getMaximumSize()) != null) {
                i6 = maximumSize.width;
                i7 = maximumSize.height;
            }
            int iMax = Math.max(Math.max(i3, i5), i7);
            if (iMax != Integer.MAX_VALUE) {
                iMax = i3 + i5 + i7 + insets.top + insets.bottom;
            }
            int iMax2 = Math.max(Math.max(i2, i4), i6);
            if (iMax2 != Integer.MAX_VALUE) {
                iMax2 += insets.left + insets.right;
            }
            return new Dimension(iMax2, iMax);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            JComponent titlePane;
            Dimension preferredSize;
            JRootPane jRootPane = (JRootPane) container;
            Rectangle bounds = jRootPane.getBounds();
            Insets insets = jRootPane.getInsets();
            int i2 = 0;
            int i3 = (bounds.width - insets.right) - insets.left;
            int i4 = (bounds.height - insets.top) - insets.bottom;
            if (jRootPane.getLayeredPane() != null) {
                jRootPane.getLayeredPane().setBounds(insets.left, insets.top, i3, i4);
            }
            if (jRootPane.getGlassPane() != null) {
                jRootPane.getGlassPane().setBounds(insets.left, insets.top, i3, i4);
            }
            if (jRootPane.getWindowDecorationStyle() != 0 && (jRootPane.getUI() instanceof TinyRootPaneUI) && (titlePane = ((TinyRootPaneUI) jRootPane.getUI()).getTitlePane()) != null && (preferredSize = titlePane.getPreferredSize()) != null) {
                int i5 = preferredSize.height;
                titlePane.setBounds(0, 0, i3, i5);
                i2 = 0 + i5;
            }
            if (jRootPane.getJMenuBar() != null) {
                Dimension preferredSize2 = jRootPane.getJMenuBar().getPreferredSize();
                jRootPane.getJMenuBar().setBounds(0, i2, i3, preferredSize2.height);
                i2 += preferredSize2.height;
            }
            if (jRootPane.getContentPane() != null) {
                jRootPane.getContentPane().getPreferredSize();
                jRootPane.getContentPane().setBounds(0, i2, i3, i4 < i2 ? 0 : i4 - i2);
            }
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager2
        public void addLayoutComponent(Component component, Object obj) {
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentX(Container container) {
            return 0.0f;
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentY(Container container) {
            return 0.0f;
        }

        @Override // java.awt.LayoutManager2
        public void invalidateLayout(Container container) {
        }

        MetalRootLayout(AnonymousClass1 anonymousClass1) {
            this();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRootPaneUI$MouseHandler.class */
    private class MouseHandler implements AWTEventListener {
        private final TinyRootPaneUI this$0;

        private MouseHandler(TinyRootPaneUI tinyRootPaneUI) {
            this.this$0 = tinyRootPaneUI;
        }

        @Override // java.awt.event.AWTEventListener
        public void eventDispatched(AWTEvent aWTEvent) {
            if (this.this$0.topMenuToClose) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
                this.this$0.topMenuToClose = false;
                this.this$0.removeEscapeMenuHandlers();
            }
        }

        MouseHandler(TinyRootPaneUI tinyRootPaneUI, AnonymousClass1 anonymousClass1) {
            this(tinyRootPaneUI);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRootPaneUI$MouseInputHandler.class */
    private class MouseInputHandler implements MouseInputListener {
        private boolean isMovingWindow;
        private int dragCursor;
        private int dragOffsetX;
        private int dragOffsetY;
        private int dragWidth;
        private int dragHeight;
        private final TinyRootPaneUI this$0;

        private MouseInputHandler(TinyRootPaneUI tinyRootPaneUI) {
            this.this$0 = tinyRootPaneUI;
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (this.this$0.getRootPane().getWindowDecorationStyle() == 0) {
                return;
            }
            Point point = mouseEvent.getPoint();
            Window window = (Window) mouseEvent.getSource();
            Point pointConvertPoint = SwingUtilities.convertPoint(window, point, this.this$0.getTitlePane());
            Frame frame = null;
            Dialog dialog = null;
            if (window instanceof Frame) {
                frame = (Frame) window;
            } else if (window instanceof Dialog) {
                dialog = (Dialog) window;
            }
            int extendedState = frame != null ? frame.getExtendedState() : 0;
            if (this.this$0.getTitlePane() != null && this.this$0.getTitlePane().contains(pointConvertPoint)) {
                if (mouseEvent.getClickCount() == 2 && frame != null && frame.isResizable()) {
                    if ((extendedState & 2) == 2 || (extendedState & 4) == 4) {
                        frame.setExtendedState(extendedState & (-7));
                        return;
                    } else {
                        frame.setExtendedState(extendedState | 6);
                        return;
                    }
                }
                if (((frame != null && (extendedState & 2) != 2 && (extendedState & 4) != 4) || dialog != null) && point.f12371y >= 5 && point.f12370x >= 5 && point.f12370x < window.getWidth() - 5) {
                    this.isMovingWindow = true;
                    this.dragOffsetX = point.f12370x;
                    this.dragOffsetY = point.f12371y;
                    return;
                }
            }
            if ((frame == null || !frame.isResizable() || (extendedState & 2) == 2 || (extendedState & 4) == 4) && (dialog == null || !dialog.isResizable())) {
                return;
            }
            this.dragOffsetX = point.f12370x;
            this.dragOffsetY = point.f12371y;
            this.dragWidth = window.getWidth();
            this.dragHeight = window.getHeight();
            this.dragCursor = getCursor(calculateCorner(window, point.f12370x, point.f12371y));
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (this.dragCursor != 0 && this.this$0.window != null && !this.this$0.window.isValid()) {
                this.this$0.window.validate();
                this.this$0.getRootPane().repaint();
            }
            this.isMovingWindow = false;
            this.dragCursor = 0;
            Window window = (Window) mouseEvent.getSource();
            if (window.getCursor() != this.this$0.lastCursor) {
                window.setCursor(this.this$0.lastCursor);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (this.this$0.getRootPane().getWindowDecorationStyle() == 0) {
                return;
            }
            Window window = (Window) mouseEvent.getSource();
            Frame frame = null;
            Dialog dialog = null;
            if (window instanceof Frame) {
                frame = (Frame) window;
            } else if (window instanceof Dialog) {
                dialog = (Dialog) window;
            }
            int cursor = getCursor(calculateCorner(window, mouseEvent.getX(), mouseEvent.getY()));
            if (cursor == 0 || ((frame == null || !frame.isResizable() || (frame.getExtendedState() & 4) == 4 || (frame.getExtendedState() & 2) == 2) && (dialog == null || !dialog.isResizable()))) {
                window.setCursor(this.this$0.lastCursor);
            } else {
                window.setCursor(Cursor.getPredefinedCursor(cursor));
            }
        }

        private void adjust(Rectangle rectangle, Dimension dimension, int i2, int i3, int i4, int i5) {
            rectangle.f12372x += i2;
            rectangle.f12373y += i3;
            rectangle.width += i4;
            rectangle.height += i5;
            if (dimension != null) {
                if (rectangle.width < dimension.width) {
                    int i6 = dimension.width - rectangle.width;
                    if (i2 != 0) {
                        rectangle.f12372x -= i6;
                    }
                    rectangle.width = dimension.width;
                }
                if (rectangle.height < dimension.height) {
                    int i7 = dimension.height - rectangle.height;
                    if (i3 != 0) {
                        rectangle.f12373y -= i7;
                    }
                    rectangle.height = dimension.height;
                }
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            Window window = (Window) mouseEvent.getSource();
            Point point = mouseEvent.getPoint();
            if (this.isMovingWindow) {
                Point locationOnScreen = window.getLocationOnScreen();
                locationOnScreen.f12370x += point.f12370x - this.dragOffsetX;
                locationOnScreen.f12371y += point.f12371y - this.dragOffsetY;
                window.setLocation(locationOnScreen);
                return;
            }
            if (this.dragCursor != 0) {
                Rectangle bounds = window.getBounds();
                Rectangle rectangle = new Rectangle(bounds);
                Dimension minimumSize = window.getMinimumSize();
                switch (this.dragCursor) {
                    case 4:
                        adjust(bounds, minimumSize, point.f12370x - this.dragOffsetX, 0, -(point.f12370x - this.dragOffsetX), (point.f12371y + (this.dragHeight - this.dragOffsetY)) - bounds.height);
                        break;
                    case 5:
                        adjust(bounds, minimumSize, 0, 0, (point.f12370x + (this.dragWidth - this.dragOffsetX)) - bounds.width, (point.f12371y + (this.dragHeight - this.dragOffsetY)) - bounds.height);
                        break;
                    case 6:
                        adjust(bounds, minimumSize, point.f12370x - this.dragOffsetX, point.f12371y - this.dragOffsetY, -(point.f12370x - this.dragOffsetX), -(point.f12371y - this.dragOffsetY));
                        break;
                    case 7:
                        adjust(bounds, minimumSize, 0, point.f12371y - this.dragOffsetY, (point.f12370x + (this.dragWidth - this.dragOffsetX)) - bounds.width, -(point.f12371y - this.dragOffsetY));
                        break;
                    case 8:
                        adjust(bounds, minimumSize, 0, point.f12371y - this.dragOffsetY, 0, -(point.f12371y - this.dragOffsetY));
                        break;
                    case 9:
                        adjust(bounds, minimumSize, 0, 0, 0, (point.f12371y + (this.dragHeight - this.dragOffsetY)) - bounds.height);
                        break;
                    case 10:
                        adjust(bounds, minimumSize, point.f12370x - this.dragOffsetX, 0, -(point.f12370x - this.dragOffsetX), 0);
                        break;
                    case 11:
                        adjust(bounds, minimumSize, 0, 0, (point.f12370x + (this.dragWidth - this.dragOffsetX)) - bounds.width, 0);
                        break;
                }
                if (bounds.equals(rectangle)) {
                    return;
                }
                window.setBounds(bounds);
                if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                    window.validate();
                    this.this$0.getRootPane().repaint();
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            Window window = (Window) mouseEvent.getSource();
            this.this$0.lastCursor = window.getCursor();
            mouseMoved(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            ((Window) mouseEvent.getSource()).setCursor(this.this$0.lastCursor);
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        private int calculateCorner(Component component, int i2, int i3) {
            int iCalculatePosition = calculatePosition(i2, component.getWidth());
            int iCalculatePosition2 = calculatePosition(i3, component.getHeight());
            if (iCalculatePosition == -1 || iCalculatePosition2 == -1) {
                return -1;
            }
            return (iCalculatePosition2 * 5) + iCalculatePosition;
        }

        private int getCursor(int i2) {
            if (i2 == -1) {
                return 0;
            }
            return TinyRootPaneUI.cursorMapping[i2];
        }

        private int calculatePosition(int i2, int i3) {
            if (i2 < 5) {
                return 0;
            }
            if (i2 < 16) {
                return 1;
            }
            if (i2 >= i3 - 5) {
                return 4;
            }
            return i2 >= i3 - 16 ? 3 : 2;
        }

        MouseInputHandler(TinyRootPaneUI tinyRootPaneUI, AnonymousClass1 anonymousClass1) {
            this(tinyRootPaneUI);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyRootPaneUI();
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        this.root = (JRootPane) jComponent;
        if (this.root.getWindowDecorationStyle() != 0) {
            installClientDecorations(this.root);
        }
    }

    private Vector getRegisteredKeyCodes(JRootPane jRootPane) {
        KeyStroke[] keyStrokeArrAllKeys;
        Vector vector = new Vector();
        InputMap inputMap = jRootPane.getInputMap(2);
        if (inputMap != null && (keyStrokeArrAllKeys = inputMap.allKeys()) != null) {
            for (KeyStroke keyStroke : keyStrokeArrAllKeys) {
                vector.add(new Integer(keyStroke.getKeyCode()));
            }
            return vector;
        }
        return vector;
    }

    private ActionMap getMapForKey(ActionMap actionMap, String str) {
        if (actionMap == null) {
            return null;
        }
        if (actionMap != null && actionMap.keys() != null) {
            for (Object obj : actionMap.keys()) {
                if (str.equals(obj)) {
                    return actionMap;
                }
            }
        }
        return getMapForKey(actionMap.getParent(), str);
    }

    boolean isTopMenuToClose() {
        return this.topMenuToClose;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addEscapeMenuHandlers() {
        AccessController.doPrivileged(new PrivilegedAction(this) { // from class: de.muntjak.tinylookandfeel.TinyRootPaneUI.1
            private final TinyRootPaneUI this$0;

            {
                this.this$0 = this;
            }

            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Toolkit.getDefaultToolkit().addAWTEventListener(this.this$0.mouseHandler, 131120L);
                return null;
            }
        });
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(this.keyPostProcessor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeEscapeMenuHandlers() {
        AccessController.doPrivileged(new PrivilegedAction(this) { // from class: de.muntjak.tinylookandfeel.TinyRootPaneUI.2
            private final TinyRootPaneUI this$0;

            {
                this.this$0 = this;
            }

            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                Toolkit.getDefaultToolkit().removeAWTEventListener(this.this$0.mouseHandler);
                return null;
            }
        });
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(this.keyPostProcessor);
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        super.uninstallUI(jComponent);
        uninstallClientDecorations(this.root);
        this.layoutManager = null;
        this.mouseInputListener = null;
        this.root = null;
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI
    protected void installListeners(JRootPane jRootPane) {
        super.installListeners(jRootPane);
        jRootPane.addFocusListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI
    protected void uninstallListeners(JRootPane jRootPane) {
        super.uninstallListeners(jRootPane);
        jRootPane.removeFocusListener(this);
    }

    void installBorder(JRootPane jRootPane) {
        int windowDecorationStyle = jRootPane.getWindowDecorationStyle();
        if (windowDecorationStyle == 0) {
            LookAndFeel.uninstallBorder(jRootPane);
        } else {
            LookAndFeel.installBorder(jRootPane, borderKeys[windowDecorationStyle]);
        }
    }

    private void uninstallBorder(JRootPane jRootPane) {
        LookAndFeel.uninstallBorder(jRootPane);
    }

    private void installWindowListeners(JRootPane jRootPane, Component component) {
        if (component instanceof Window) {
            this.window = (Window) component;
        } else {
            this.window = SwingUtilities.getWindowAncestor(component);
        }
        if (this.window != null) {
            if (this.mouseInputListener == null) {
                this.mouseInputListener = createWindowMouseInputListener(jRootPane);
            }
            this.window.addMouseListener(this.mouseInputListener);
            this.window.addMouseMotionListener(this.mouseInputListener);
        }
    }

    private void uninstallWindowListeners(JRootPane jRootPane) {
        if (this.window != null) {
            this.window.removeMouseListener(this.mouseInputListener);
            this.window.removeMouseMotionListener(this.mouseInputListener);
        }
    }

    private void installLayout(JRootPane jRootPane) {
        if (this.layoutManager == null) {
            this.layoutManager = createLayoutManager();
        }
        this.savedOldLayout = jRootPane.getLayout();
        jRootPane.setLayout(this.layoutManager);
    }

    private void uninstallLayout(JRootPane jRootPane) {
        if (this.savedOldLayout != null) {
            jRootPane.setLayout(this.savedOldLayout);
            this.savedOldLayout = null;
        }
    }

    private void installClientDecorations(JRootPane jRootPane) {
        installBorder(jRootPane);
        setTitlePane(jRootPane, createTitlePane(jRootPane));
        installWindowListeners(jRootPane, jRootPane.getParent());
        installLayout(jRootPane);
        if (this.window != null) {
            jRootPane.revalidate();
            jRootPane.repaint();
        }
    }

    private void uninstallClientDecorations(JRootPane jRootPane) {
        uninstallBorder(jRootPane);
        uninstallWindowListeners(jRootPane);
        setTitlePane(jRootPane, null);
        uninstallLayout(jRootPane);
        jRootPane.repaint();
        jRootPane.revalidate();
        if (this.window != null) {
            this.window.setCursor(Cursor.getPredefinedCursor(0));
        }
        this.window = null;
    }

    private JComponent createTitlePane(JRootPane jRootPane) {
        return new TinyTitlePane(jRootPane, this);
    }

    private MouseInputListener createWindowMouseInputListener(JRootPane jRootPane) {
        return new MouseInputHandler(this, null);
    }

    private LayoutManager createLayoutManager() {
        return new MetalRootLayout(null);
    }

    private void setTitlePane(JRootPane jRootPane, JComponent jComponent) {
        JLayeredPane layeredPane = jRootPane.getLayeredPane();
        JComponent titlePane = getTitlePane();
        if (titlePane != null) {
            titlePane.setVisible(false);
            layeredPane.remove(titlePane);
        }
        if (jComponent != null) {
            layeredPane.add(jComponent, JLayeredPane.FRAME_CONTENT_LAYER);
            jComponent.setVisible(true);
        }
        this.titlePane = jComponent;
        jRootPane.validate();
        jRootPane.repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JComponent getTitlePane() {
        return this.titlePane;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JRootPane getRootPane() {
        return this.root;
    }

    @Override // javax.swing.plaf.basic.BasicRootPaneUI, java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        super.propertyChange(propertyChangeEvent);
        String propertyName = propertyChangeEvent.getPropertyName();
        if (propertyName == null) {
            return;
        }
        if (propertyName.equals("windowDecorationStyle")) {
            JRootPane jRootPane = (JRootPane) propertyChangeEvent.getSource();
            int windowDecorationStyle = jRootPane.getWindowDecorationStyle();
            uninstallClientDecorations(jRootPane);
            if (windowDecorationStyle != 0) {
                installClientDecorations(jRootPane);
                return;
            }
            return;
        }
        if (propertyName.equals("ancestor")) {
            uninstallWindowListeners(this.root);
            if (((JRootPane) propertyChangeEvent.getSource()).getWindowDecorationStyle() != 0) {
                installWindowListeners(this.root, this.root.getParent());
            }
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        if (this.topMenuToClose) {
            this.topMenuToClose = false;
            removeEscapeMenuHandlers();
            return;
        }
        JRootPane jRootPane = (JRootPane) focusEvent.getSource();
        this.registeredKeyCodes = getRegisteredKeyCodes(jRootPane);
        ActionMap mapForKey = getMapForKey(jRootPane.getActionMap(), CANCEL);
        if (mapForKey != null) {
            mapForKey.put(CANCEL, new MenuActions(this, CANCEL));
            mapForKey.put("return", new MenuActions(this, "return"));
            mapForKey.put(SELECT_PARENT, new MenuActions(this, SELECT_PARENT));
            mapForKey.put(SELECT_CHILD, new MenuActions(this, SELECT_CHILD));
        }
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
