package sun.awt;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.DefaultKeyboardFocusManager;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.KeyboardFocusManager;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuComponent;
import java.awt.MenuContainer;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.ScrollPaneAdjustable;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.peer.ComponentPeer;
import java.awt.peer.MenuComponentPeer;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessControlContext;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import sun.awt.CausedFocusEvent;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/awt/AWTAccessor.class */
public final class AWTAccessor {
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static ComponentAccessor componentAccessor;
    private static ContainerAccessor containerAccessor;
    private static WindowAccessor windowAccessor;
    private static AWTEventAccessor awtEventAccessor;
    private static InputEventAccessor inputEventAccessor;
    private static MouseEventAccessor mouseEventAccessor;
    private static FrameAccessor frameAccessor;
    private static KeyboardFocusManagerAccessor kfmAccessor;
    private static MenuComponentAccessor menuComponentAccessor;
    private static EventQueueAccessor eventQueueAccessor;
    private static PopupMenuAccessor popupMenuAccessor;
    private static FileDialogAccessor fileDialogAccessor;
    private static ScrollPaneAdjustableAccessor scrollPaneAdjustableAccessor;
    private static CheckboxMenuItemAccessor checkboxMenuItemAccessor;
    private static CursorAccessor cursorAccessor;
    private static MenuBarAccessor menuBarAccessor;
    private static MenuItemAccessor menuItemAccessor;
    private static MenuAccessor menuAccessor;
    private static KeyEventAccessor keyEventAccessor;
    private static ClientPropertyKeyAccessor clientPropertyKeyAccessor;
    private static SystemTrayAccessor systemTrayAccessor;
    private static TrayIconAccessor trayIconAccessor;
    private static DefaultKeyboardFocusManagerAccessor defaultKeyboardFocusManagerAccessor;
    private static SequencedEventAccessor sequencedEventAccessor;
    private static ToolkitAccessor toolkitAccessor;
    private static InvocationEventAccessor invocationEventAccessor;
    private static SystemColorAccessor systemColorAccessor;
    private static AccessibleContextAccessor accessibleContextAccessor;

    /* loaded from: rt.jar:sun/awt/AWTAccessor$AWTEventAccessor.class */
    public interface AWTEventAccessor {
        void setPosted(AWTEvent aWTEvent);

        void setSystemGenerated(AWTEvent aWTEvent);

        boolean isSystemGenerated(AWTEvent aWTEvent);

        AccessControlContext getAccessControlContext(AWTEvent aWTEvent);

        byte[] getBData(AWTEvent aWTEvent);

        void setBData(AWTEvent aWTEvent, byte[] bArr);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$AccessibleContextAccessor.class */
    public interface AccessibleContextAccessor {
        void setAppContext(AccessibleContext accessibleContext, AppContext appContext);

        AppContext getAppContext(AccessibleContext accessibleContext);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$CheckboxMenuItemAccessor.class */
    public interface CheckboxMenuItemAccessor {
        boolean getState(CheckboxMenuItem checkboxMenuItem);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$ClientPropertyKeyAccessor.class */
    public interface ClientPropertyKeyAccessor {
        Object getJComponent_TRANSFER_HANDLER();
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$ComponentAccessor.class */
    public interface ComponentAccessor {
        void setBackgroundEraseDisabled(Component component, boolean z2);

        boolean getBackgroundEraseDisabled(Component component);

        Rectangle getBounds(Component component);

        void setMixingCutoutShape(Component component, Shape shape);

        void setGraphicsConfiguration(Component component, GraphicsConfiguration graphicsConfiguration);

        boolean requestFocus(Component component, CausedFocusEvent.Cause cause);

        boolean canBeFocusOwner(Component component);

        boolean isVisible(Component component);

        void setRequestFocusController(RequestFocusController requestFocusController);

        AppContext getAppContext(Component component);

        void setAppContext(Component component, AppContext appContext);

        Container getParent(Component component);

        void setParent(Component component, Container container);

        void setSize(Component component, int i2, int i3);

        Point getLocation(Component component);

        void setLocation(Component component, int i2, int i3);

        boolean isEnabled(Component component);

        boolean isDisplayable(Component component);

        Cursor getCursor(Component component);

        ComponentPeer getPeer(Component component);

        void setPeer(Component component, ComponentPeer componentPeer);

        boolean isLightweight(Component component);

        boolean getIgnoreRepaint(Component component);

        int getWidth(Component component);

        int getHeight(Component component);

        int getX(Component component);

        int getY(Component component);

        Color getForeground(Component component);

        Color getBackground(Component component);

        void setBackground(Component component, Color color);

        Font getFont(Component component);

        void processEvent(Component component, AWTEvent aWTEvent);

        AccessControlContext getAccessControlContext(Component component);

        void revalidateSynchronously(Component component);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$ContainerAccessor.class */
    public interface ContainerAccessor {
        void validateUnconditionally(Container container);

        Component findComponentAt(Container container, int i2, int i3, boolean z2);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$CursorAccessor.class */
    public interface CursorAccessor {
        long getPData(Cursor cursor);

        void setPData(Cursor cursor, long j2);

        int getType(Cursor cursor);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$DefaultKeyboardFocusManagerAccessor.class */
    public interface DefaultKeyboardFocusManagerAccessor {
        void consumeNextKeyTyped(DefaultKeyboardFocusManager defaultKeyboardFocusManager, KeyEvent keyEvent);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$EventQueueAccessor.class */
    public interface EventQueueAccessor {
        Thread getDispatchThread(EventQueue eventQueue);

        boolean isDispatchThreadImpl(EventQueue eventQueue);

        void removeSourceEvents(EventQueue eventQueue, Object obj, boolean z2);

        boolean noEvents(EventQueue eventQueue);

        void wakeup(EventQueue eventQueue, boolean z2);

        void invokeAndWait(Object obj, Runnable runnable) throws InterruptedException, InvocationTargetException;

        void setFwDispatcher(EventQueue eventQueue, FwDispatcher fwDispatcher);

        long getMostRecentEventTime(EventQueue eventQueue);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$FileDialogAccessor.class */
    public interface FileDialogAccessor {
        void setFiles(FileDialog fileDialog, File[] fileArr);

        void setFile(FileDialog fileDialog, String str);

        void setDirectory(FileDialog fileDialog, String str);

        boolean isMultipleMode(FileDialog fileDialog);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$FrameAccessor.class */
    public interface FrameAccessor {
        void setExtendedState(Frame frame, int i2);

        int getExtendedState(Frame frame);

        Rectangle getMaximizedBounds(Frame frame);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$InputEventAccessor.class */
    public interface InputEventAccessor {
        int[] getButtonDownMasks();
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$InvocationEventAccessor.class */
    public interface InvocationEventAccessor {
        void dispose(InvocationEvent invocationEvent);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$KeyEventAccessor.class */
    public interface KeyEventAccessor {
        void setRawCode(KeyEvent keyEvent, long j2);

        void setPrimaryLevelUnicode(KeyEvent keyEvent, long j2);

        void setExtendedKeyCode(KeyEvent keyEvent, long j2);

        Component getOriginalSource(KeyEvent keyEvent);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$KeyboardFocusManagerAccessor.class */
    public interface KeyboardFocusManagerAccessor {
        int shouldNativelyFocusHeavyweight(Component component, Component component2, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause);

        boolean processSynchronousLightweightTransfer(Component component, Component component2, boolean z2, boolean z3, long j2);

        void removeLastFocusRequest(Component component);

        void setMostRecentFocusOwner(Window window, Component component);

        KeyboardFocusManager getCurrentKeyboardFocusManager(AppContext appContext);

        Container getCurrentFocusCycleRoot();
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$MenuAccessor.class */
    public interface MenuAccessor {
        Vector getItems(Menu menu);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$MenuBarAccessor.class */
    public interface MenuBarAccessor {
        Menu getHelpMenu(MenuBar menuBar);

        Vector getMenus(MenuBar menuBar);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$MenuComponentAccessor.class */
    public interface MenuComponentAccessor {
        AppContext getAppContext(MenuComponent menuComponent);

        void setAppContext(MenuComponent menuComponent, AppContext appContext);

        MenuContainer getParent(MenuComponent menuComponent);

        Font getFont_NoClientCode(MenuComponent menuComponent);

        <T extends MenuComponentPeer> T getPeer(MenuComponent menuComponent);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$MenuItemAccessor.class */
    public interface MenuItemAccessor {
        boolean isEnabled(MenuItem menuItem);

        String getActionCommandImpl(MenuItem menuItem);

        boolean isItemEnabled(MenuItem menuItem);

        String getLabel(MenuItem menuItem);

        MenuShortcut getShortcut(MenuItem menuItem);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$MouseEventAccessor.class */
    public interface MouseEventAccessor {
        boolean isCausedByTouchEvent(MouseEvent mouseEvent);

        void setCausedByTouchEvent(MouseEvent mouseEvent, boolean z2);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$PopupMenuAccessor.class */
    public interface PopupMenuAccessor {
        boolean isTrayIconPopup(PopupMenu popupMenu);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$ScrollPaneAdjustableAccessor.class */
    public interface ScrollPaneAdjustableAccessor {
        void setTypedValue(ScrollPaneAdjustable scrollPaneAdjustable, int i2, int i3);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$SequencedEventAccessor.class */
    public interface SequencedEventAccessor {
        AWTEvent getNested(AWTEvent aWTEvent);

        boolean isSequencedEvent(AWTEvent aWTEvent);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$SystemColorAccessor.class */
    public interface SystemColorAccessor {
        void updateSystemColors();
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$SystemTrayAccessor.class */
    public interface SystemTrayAccessor {
        void firePropertyChange(SystemTray systemTray, String str, Object obj, Object obj2);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$ToolkitAccessor.class */
    public interface ToolkitAccessor {
        void setPlatformResources(ResourceBundle resourceBundle);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$TrayIconAccessor.class */
    public interface TrayIconAccessor {
        void addNotify(TrayIcon trayIcon) throws AWTException;

        void removeNotify(TrayIcon trayIcon);
    }

    /* loaded from: rt.jar:sun/awt/AWTAccessor$WindowAccessor.class */
    public interface WindowAccessor {
        float getOpacity(Window window);

        void setOpacity(Window window, float f2);

        Shape getShape(Window window);

        void setShape(Window window, Shape shape);

        void setOpaque(Window window, boolean z2);

        void updateWindow(Window window);

        Dimension getSecurityWarningSize(Window window);

        void setSecurityWarningSize(Window window, int i2, int i3);

        void setSecurityWarningPosition(Window window, Point2D point2D, float f2, float f3);

        Point2D calculateSecurityWarningPosition(Window window, double d2, double d3, double d4, double d5);

        void setLWRequestStatus(Window window, boolean z2);

        boolean isAutoRequestFocus(Window window);

        boolean isTrayIconWindow(Window window);

        void setTrayIconWindow(Window window, boolean z2);

        Window[] getOwnedWindows(Window window);
    }

    private AWTAccessor() {
    }

    public static void setComponentAccessor(ComponentAccessor componentAccessor2) {
        componentAccessor = componentAccessor2;
    }

    public static ComponentAccessor getComponentAccessor() {
        if (componentAccessor == null) {
            unsafe.ensureClassInitialized(Component.class);
        }
        return componentAccessor;
    }

    public static void setContainerAccessor(ContainerAccessor containerAccessor2) {
        containerAccessor = containerAccessor2;
    }

    public static ContainerAccessor getContainerAccessor() {
        if (containerAccessor == null) {
            unsafe.ensureClassInitialized(Container.class);
        }
        return containerAccessor;
    }

    public static void setWindowAccessor(WindowAccessor windowAccessor2) {
        windowAccessor = windowAccessor2;
    }

    public static WindowAccessor getWindowAccessor() {
        if (windowAccessor == null) {
            unsafe.ensureClassInitialized(Window.class);
        }
        return windowAccessor;
    }

    public static void setAWTEventAccessor(AWTEventAccessor aWTEventAccessor) {
        awtEventAccessor = aWTEventAccessor;
    }

    public static AWTEventAccessor getAWTEventAccessor() {
        if (awtEventAccessor == null) {
            unsafe.ensureClassInitialized(AWTEvent.class);
        }
        return awtEventAccessor;
    }

    public static void setInputEventAccessor(InputEventAccessor inputEventAccessor2) {
        inputEventAccessor = inputEventAccessor2;
    }

    public static InputEventAccessor getInputEventAccessor() {
        if (inputEventAccessor == null) {
            unsafe.ensureClassInitialized(InputEvent.class);
        }
        return inputEventAccessor;
    }

    public static void setMouseEventAccessor(MouseEventAccessor mouseEventAccessor2) {
        mouseEventAccessor = mouseEventAccessor2;
    }

    public static MouseEventAccessor getMouseEventAccessor() {
        if (mouseEventAccessor == null) {
            unsafe.ensureClassInitialized(MouseEvent.class);
        }
        return mouseEventAccessor;
    }

    public static void setFrameAccessor(FrameAccessor frameAccessor2) {
        frameAccessor = frameAccessor2;
    }

    public static FrameAccessor getFrameAccessor() {
        if (frameAccessor == null) {
            unsafe.ensureClassInitialized(Frame.class);
        }
        return frameAccessor;
    }

    public static void setKeyboardFocusManagerAccessor(KeyboardFocusManagerAccessor keyboardFocusManagerAccessor) {
        kfmAccessor = keyboardFocusManagerAccessor;
    }

    public static KeyboardFocusManagerAccessor getKeyboardFocusManagerAccessor() {
        if (kfmAccessor == null) {
            unsafe.ensureClassInitialized(KeyboardFocusManager.class);
        }
        return kfmAccessor;
    }

    public static void setMenuComponentAccessor(MenuComponentAccessor menuComponentAccessor2) {
        menuComponentAccessor = menuComponentAccessor2;
    }

    public static MenuComponentAccessor getMenuComponentAccessor() {
        if (menuComponentAccessor == null) {
            unsafe.ensureClassInitialized(MenuComponent.class);
        }
        return menuComponentAccessor;
    }

    public static void setEventQueueAccessor(EventQueueAccessor eventQueueAccessor2) {
        eventQueueAccessor = eventQueueAccessor2;
    }

    public static EventQueueAccessor getEventQueueAccessor() {
        if (eventQueueAccessor == null) {
            unsafe.ensureClassInitialized(EventQueue.class);
        }
        return eventQueueAccessor;
    }

    public static void setPopupMenuAccessor(PopupMenuAccessor popupMenuAccessor2) {
        popupMenuAccessor = popupMenuAccessor2;
    }

    public static PopupMenuAccessor getPopupMenuAccessor() {
        if (popupMenuAccessor == null) {
            unsafe.ensureClassInitialized(PopupMenu.class);
        }
        return popupMenuAccessor;
    }

    public static void setFileDialogAccessor(FileDialogAccessor fileDialogAccessor2) {
        fileDialogAccessor = fileDialogAccessor2;
    }

    public static FileDialogAccessor getFileDialogAccessor() {
        if (fileDialogAccessor == null) {
            unsafe.ensureClassInitialized(FileDialog.class);
        }
        return fileDialogAccessor;
    }

    public static void setScrollPaneAdjustableAccessor(ScrollPaneAdjustableAccessor scrollPaneAdjustableAccessor2) {
        scrollPaneAdjustableAccessor = scrollPaneAdjustableAccessor2;
    }

    public static ScrollPaneAdjustableAccessor getScrollPaneAdjustableAccessor() {
        if (scrollPaneAdjustableAccessor == null) {
            unsafe.ensureClassInitialized(ScrollPaneAdjustable.class);
        }
        return scrollPaneAdjustableAccessor;
    }

    public static void setCheckboxMenuItemAccessor(CheckboxMenuItemAccessor checkboxMenuItemAccessor2) {
        checkboxMenuItemAccessor = checkboxMenuItemAccessor2;
    }

    public static CheckboxMenuItemAccessor getCheckboxMenuItemAccessor() {
        if (checkboxMenuItemAccessor == null) {
            unsafe.ensureClassInitialized(CheckboxMenuItemAccessor.class);
        }
        return checkboxMenuItemAccessor;
    }

    public static void setCursorAccessor(CursorAccessor cursorAccessor2) {
        cursorAccessor = cursorAccessor2;
    }

    public static CursorAccessor getCursorAccessor() {
        if (cursorAccessor == null) {
            unsafe.ensureClassInitialized(CursorAccessor.class);
        }
        return cursorAccessor;
    }

    public static void setMenuBarAccessor(MenuBarAccessor menuBarAccessor2) {
        menuBarAccessor = menuBarAccessor2;
    }

    public static MenuBarAccessor getMenuBarAccessor() {
        if (menuBarAccessor == null) {
            unsafe.ensureClassInitialized(MenuBarAccessor.class);
        }
        return menuBarAccessor;
    }

    public static void setMenuItemAccessor(MenuItemAccessor menuItemAccessor2) {
        menuItemAccessor = menuItemAccessor2;
    }

    public static MenuItemAccessor getMenuItemAccessor() {
        if (menuItemAccessor == null) {
            unsafe.ensureClassInitialized(MenuItemAccessor.class);
        }
        return menuItemAccessor;
    }

    public static void setMenuAccessor(MenuAccessor menuAccessor2) {
        menuAccessor = menuAccessor2;
    }

    public static MenuAccessor getMenuAccessor() {
        if (menuAccessor == null) {
            unsafe.ensureClassInitialized(MenuAccessor.class);
        }
        return menuAccessor;
    }

    public static void setKeyEventAccessor(KeyEventAccessor keyEventAccessor2) {
        keyEventAccessor = keyEventAccessor2;
    }

    public static KeyEventAccessor getKeyEventAccessor() {
        if (keyEventAccessor == null) {
            unsafe.ensureClassInitialized(KeyEventAccessor.class);
        }
        return keyEventAccessor;
    }

    public static void setClientPropertyKeyAccessor(ClientPropertyKeyAccessor clientPropertyKeyAccessor2) {
        clientPropertyKeyAccessor = clientPropertyKeyAccessor2;
    }

    public static ClientPropertyKeyAccessor getClientPropertyKeyAccessor() {
        if (clientPropertyKeyAccessor == null) {
            unsafe.ensureClassInitialized(ClientPropertyKeyAccessor.class);
        }
        return clientPropertyKeyAccessor;
    }

    public static void setSystemTrayAccessor(SystemTrayAccessor systemTrayAccessor2) {
        systemTrayAccessor = systemTrayAccessor2;
    }

    public static SystemTrayAccessor getSystemTrayAccessor() {
        if (systemTrayAccessor == null) {
            unsafe.ensureClassInitialized(SystemTrayAccessor.class);
        }
        return systemTrayAccessor;
    }

    public static void setTrayIconAccessor(TrayIconAccessor trayIconAccessor2) {
        trayIconAccessor = trayIconAccessor2;
    }

    public static TrayIconAccessor getTrayIconAccessor() {
        if (trayIconAccessor == null) {
            unsafe.ensureClassInitialized(TrayIconAccessor.class);
        }
        return trayIconAccessor;
    }

    public static void setDefaultKeyboardFocusManagerAccessor(DefaultKeyboardFocusManagerAccessor defaultKeyboardFocusManagerAccessor2) {
        defaultKeyboardFocusManagerAccessor = defaultKeyboardFocusManagerAccessor2;
    }

    public static DefaultKeyboardFocusManagerAccessor getDefaultKeyboardFocusManagerAccessor() {
        if (defaultKeyboardFocusManagerAccessor == null) {
            unsafe.ensureClassInitialized(DefaultKeyboardFocusManagerAccessor.class);
        }
        return defaultKeyboardFocusManagerAccessor;
    }

    public static void setSequencedEventAccessor(SequencedEventAccessor sequencedEventAccessor2) {
        sequencedEventAccessor = sequencedEventAccessor2;
    }

    public static SequencedEventAccessor getSequencedEventAccessor() {
        return sequencedEventAccessor;
    }

    public static void setToolkitAccessor(ToolkitAccessor toolkitAccessor2) {
        toolkitAccessor = toolkitAccessor2;
    }

    public static ToolkitAccessor getToolkitAccessor() {
        if (toolkitAccessor == null) {
            unsafe.ensureClassInitialized(Toolkit.class);
        }
        return toolkitAccessor;
    }

    public static void setInvocationEventAccessor(InvocationEventAccessor invocationEventAccessor2) {
        invocationEventAccessor = invocationEventAccessor2;
    }

    public static InvocationEventAccessor getInvocationEventAccessor() {
        return invocationEventAccessor;
    }

    public static SystemColorAccessor getSystemColorAccessor() {
        if (systemColorAccessor == null) {
            unsafe.ensureClassInitialized(SystemColor.class);
        }
        return systemColorAccessor;
    }

    public static void setSystemColorAccessor(SystemColorAccessor systemColorAccessor2) {
        systemColorAccessor = systemColorAccessor2;
    }

    public static AccessibleContextAccessor getAccessibleContextAccessor() {
        if (accessibleContextAccessor == null) {
            unsafe.ensureClassInitialized(AccessibleContext.class);
        }
        return accessibleContextAccessor;
    }

    public static void setAccessibleContextAccessor(AccessibleContextAccessor accessibleContextAccessor2) {
        accessibleContextAccessor = accessibleContextAccessor2;
    }
}
