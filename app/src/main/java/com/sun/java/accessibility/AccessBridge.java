package com.sun.java.accessibility;

import com.sun.java.accessibility.util.AWTEventMonitor;
import com.sun.java.accessibility.util.AccessibilityEventMonitor;
import com.sun.java.accessibility.util.EventQueueMonitor;
import com.sun.java.accessibility.util.SwingEventMonitor;
import com.sun.java.accessibility.util.Translator;
import com.sun.jmx.defaults.ServiceName;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import javafx.fxml.FXMLLoader;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleEditableText;
import javax.accessibility.AccessibleExtendedComponent;
import javax.accessibility.AccessibleExtendedTable;
import javax.accessibility.AccessibleHyperlink;
import javax.accessibility.AccessibleHypertext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleKeyBinding;
import javax.accessibility.AccessibleRelation;
import javax.accessibility.AccessibleRelationSet;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleTable;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.TreeUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabSet;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import jdk.Exported;
import org.icepdf.core.util.PdfOps;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

/* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge.class
 */
@Exported(false)
/* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge.class */
public final class AccessBridge extends AccessBridgeLoader {
    private static AccessBridge theAccessBridge;
    private ObjectReferences references;
    private EventHandler eventHandler;
    private boolean runningOnJDK1_4;
    private boolean runningOnJDK1_5;
    private Method javaGetComponentFromNativeWindowHandleMethod;
    private Method javaGetNativeWindowHandleFromComponentMethod;
    Toolkit toolkit;
    private static ConcurrentHashMap<Integer, AccessibleContext> windowHandleToContextMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<AccessibleContext, Integer> contextToWindowHandleMap = new ConcurrentHashMap<>();
    private static Vector<NativeWindowHandler> nativeWindowHandlers = new Vector<>();
    private int _visibleChildrenCount;
    private AccessibleContext _visibleChild;
    private int _currentVisibleIndex;
    private boolean _foundVisibleChild;
    private static final long PROPERTY_CHANGE_EVENTS = 1;
    private static final long FOCUS_GAINED_EVENTS = 2;
    private static final long FOCUS_LOST_EVENTS = 4;
    private static final long FOCUS_EVENTS = 6;
    private static final long CARET_UPATE_EVENTS = 8;
    private static final long CARET_EVENTS = 8;
    private static final long MOUSE_CLICKED_EVENTS = 16;
    private static final long MOUSE_ENTERED_EVENTS = 32;
    private static final long MOUSE_EXITED_EVENTS = 64;
    private static final long MOUSE_PRESSED_EVENTS = 128;
    private static final long MOUSE_RELEASED_EVENTS = 256;
    private static final long MOUSE_EVENTS = 496;
    private static final long MENU_CANCELED_EVENTS = 512;
    private static final long MENU_DESELECTED_EVENTS = 1024;
    private static final long MENU_SELECTED_EVENTS = 2048;
    private static final long MENU_EVENTS = 3584;
    private static final long POPUPMENU_CANCELED_EVENTS = 4096;
    private static final long POPUPMENU_WILL_BECOME_INVISIBLE_EVENTS = 8192;
    private static final long POPUPMENU_WILL_BECOME_VISIBLE_EVENTS = 16384;
    private static final long POPUPMENU_EVENTS = 28672;
    private static final long PROPERTY_NAME_CHANGE_EVENTS = 1;
    private static final long PROPERTY_DESCRIPTION_CHANGE_EVENTS = 2;
    private static final long PROPERTY_STATE_CHANGE_EVENTS = 4;
    private static final long PROPERTY_VALUE_CHANGE_EVENTS = 8;
    private static final long PROPERTY_SELECTION_CHANGE_EVENTS = 16;
    private static final long PROPERTY_TEXT_CHANGE_EVENTS = 32;
    private static final long PROPERTY_CARET_CHANGE_EVENTS = 64;
    private static final long PROPERTY_VISIBLEDATA_CHANGE_EVENTS = 128;
    private static final long PROPERTY_CHILD_CHANGE_EVENTS = 256;
    private static final long PROPERTY_ACTIVEDESCENDENT_CHANGE_EVENTS = 512;
    private static final long PROPERTY_EVENTS = 1023;
    private final String AccessBridgeVersion = "AccessBridge 2.0.4";
    private ConcurrentHashMap<String, AccessibleRole> accessibleRoleMap = new ConcurrentHashMap<>();
    private ArrayList<AccessibleRole> extendedVirtualNameSearchRoles = new ArrayList<>();
    private ArrayList<AccessibleRole> noExtendedVirtualNameSearchParentRoles = new ArrayList<>();
    ConcurrentHashMap<AccessibleTable, AccessibleContext> hashtab = new ConcurrentHashMap<>();
    private Map<AccessibleHypertext, AccessibleContext> hyperTextContextMap = new WeakHashMap();
    private Map<AccessibleHyperlink, AccessibleContext> hyperLinkContextMap = new WeakHashMap();
    private AccessibleRole[] allAccessibleRoles = {AccessibleRole.ALERT, AccessibleRole.COLUMN_HEADER, AccessibleRole.CANVAS, AccessibleRole.COMBO_BOX, AccessibleRole.DESKTOP_ICON, AccessibleRole.INTERNAL_FRAME, AccessibleRole.DESKTOP_PANE, AccessibleRole.OPTION_PANE, AccessibleRole.WINDOW, AccessibleRole.FRAME, AccessibleRole.DIALOG, AccessibleRole.COLOR_CHOOSER, AccessibleRole.DIRECTORY_PANE, AccessibleRole.FILE_CHOOSER, AccessibleRole.FILLER, AccessibleRole.ICON, AccessibleRole.LABEL, AccessibleRole.ROOT_PANE, AccessibleRole.GLASS_PANE, AccessibleRole.LAYERED_PANE, AccessibleRole.LIST, AccessibleRole.LIST_ITEM, AccessibleRole.MENU_BAR, AccessibleRole.POPUP_MENU, AccessibleRole.MENU, AccessibleRole.MENU_ITEM, AccessibleRole.SEPARATOR, AccessibleRole.PAGE_TAB_LIST, AccessibleRole.PAGE_TAB, AccessibleRole.PANEL, AccessibleRole.PROGRESS_BAR, AccessibleRole.PASSWORD_TEXT, AccessibleRole.PUSH_BUTTON, AccessibleRole.TOGGLE_BUTTON, AccessibleRole.CHECK_BOX, AccessibleRole.RADIO_BUTTON, AccessibleRole.ROW_HEADER, AccessibleRole.SCROLL_PANE, AccessibleRole.SCROLL_BAR, AccessibleRole.VIEWPORT, AccessibleRole.SLIDER, AccessibleRole.SPLIT_PANE, AccessibleRole.TABLE, AccessibleRole.TEXT, AccessibleRole.TREE, AccessibleRole.TOOL_BAR, AccessibleRole.TOOL_TIP, AccessibleRole.AWT_COMPONENT, AccessibleRole.SWING_COMPONENT, AccessibleRole.UNKNOWN};

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$NativeWindowHandler.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$NativeWindowHandler.class */
    private interface NativeWindowHandler {
        Accessible getAccessibleFromNativeWindowHandle(int i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public native void runDLL();

    private native void sendDebugString(String str);

    private native int isJAWTInstalled();

    private native int jawtGetNativeWindowHandleFromComponent(Component component);

    /* JADX INFO: Access modifiers changed from: private */
    public native Component jawtGetComponentFromNativeWindowHandle(int i2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyCaretChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext, int i2, int i3);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyDescriptionChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext, String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyNameChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext, String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertySelectionChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyStateChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext, String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyTextChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyValueChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext, String str, String str2);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyVisibleDataChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyChildChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext, AccessibleContext accessibleContext2, AccessibleContext accessibleContext3);

    /* JADX INFO: Access modifiers changed from: private */
    public native void propertyActiveDescendentChange(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext, AccessibleContext accessibleContext2, AccessibleContext accessibleContext3);

    /* JADX INFO: Access modifiers changed from: private */
    public native void javaShutdown();

    /* JADX INFO: Access modifiers changed from: private */
    public native void focusGained(FocusEvent focusEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void focusLost(FocusEvent focusEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void caretUpdate(CaretEvent caretEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void mouseClicked(MouseEvent mouseEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void mouseEntered(MouseEvent mouseEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void mouseExited(MouseEvent mouseEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void mousePressed(MouseEvent mouseEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void mouseReleased(MouseEvent mouseEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void menuCanceled(MenuEvent menuEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void menuDeselected(MenuEvent menuEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void menuSelected(MenuEvent menuEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void popupMenuCanceled(PopupMenuEvent popupMenuEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent, AccessibleContext accessibleContext);

    /* JADX INFO: Access modifiers changed from: private */
    public native void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent, AccessibleContext accessibleContext);

    public AccessBridge() {
        this.runningOnJDK1_4 = false;
        this.runningOnJDK1_5 = false;
        theAccessBridge = this;
        this.references = new ObjectReferences();
        Runtime.getRuntime().addShutdownHook(new Thread(new shutdownHook()));
        initAccessibleRoleMap();
        String javaVersionProperty = getJavaVersionProperty();
        debugString("[INFO]:JDK version = " + javaVersionProperty);
        this.runningOnJDK1_4 = javaVersionProperty.compareTo(ServiceName.JMX_SPEC_VERSION) >= 0;
        this.runningOnJDK1_5 = javaVersionProperty.compareTo("1.5") >= 0;
        if (initHWNDcalls()) {
            EventQueueMonitor.isGUIInitialized();
            this.eventHandler = new EventHandler(this);
            if (this.runningOnJDK1_4) {
                MenuSelectionManager.defaultManager().addChangeListener(this.eventHandler);
            }
            addNativeWindowHandler(new DefaultNativeWindowHandler());
            Thread thread = new Thread(new dllRunner());
            thread.setDaemon(true);
            thread.start();
            debugString("[INFO]:AccessBridge started");
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$dllRunner.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$dllRunner.class */
    private class dllRunner implements Runnable {
        private dllRunner() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AccessBridge.this.runDLL();
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$shutdownHook.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$shutdownHook.class */
    private class shutdownHook implements Runnable {
        private shutdownHook() {
        }

        @Override // java.lang.Runnable
        public void run() {
            AccessBridge.this.debugString("[INFO]:***** shutdownHook: shutting down...");
            AccessBridge.this.javaShutdown();
        }
    }

    private void initAccessibleRoleMap() {
        try {
            Class<?> cls = Class.forName("javax.accessibility.AccessibleRole");
            if (null != cls) {
                AccessibleRole accessibleRole = AccessibleRole.UNKNOWN;
                for (Field field : cls.getFields()) {
                    if (AccessibleRole.class == field.getType()) {
                        AccessibleRole accessibleRole2 = (AccessibleRole) field.get(accessibleRole);
                        this.accessibleRoleMap.put(accessibleRole2.toDisplayString(Locale.US), accessibleRole2);
                    }
                }
            }
        } catch (Exception e2) {
        }
        this.extendedVirtualNameSearchRoles.add(AccessibleRole.COMBO_BOX);
        try {
            this.extendedVirtualNameSearchRoles.add(AccessibleRole.DATE_EDITOR);
        } catch (NoSuchFieldError e3) {
        }
        this.extendedVirtualNameSearchRoles.add(AccessibleRole.LIST);
        this.extendedVirtualNameSearchRoles.add(AccessibleRole.PASSWORD_TEXT);
        this.extendedVirtualNameSearchRoles.add(AccessibleRole.SLIDER);
        try {
            this.extendedVirtualNameSearchRoles.add(AccessibleRole.SPIN_BOX);
        } catch (NoSuchFieldError e4) {
        }
        this.extendedVirtualNameSearchRoles.add(AccessibleRole.TABLE);
        this.extendedVirtualNameSearchRoles.add(AccessibleRole.TEXT);
        this.extendedVirtualNameSearchRoles.add(AccessibleRole.UNKNOWN);
        this.noExtendedVirtualNameSearchParentRoles.add(AccessibleRole.TABLE);
        this.noExtendedVirtualNameSearchParentRoles.add(AccessibleRole.TOOL_BAR);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void debugString(String str) {
        sendDebugString(str);
    }

    private void decrementReference(Object obj) {
        this.references.decrement(obj);
    }

    private String getJavaVersionProperty() {
        String property = System.getProperty("java.version");
        if (property != null) {
            this.references.increment(property);
            return property;
        }
        return null;
    }

    private String getAccessBridgeVersion() {
        String str = new String("AccessBridge 2.0.4");
        this.references.increment(str);
        return str;
    }

    private boolean initHWNDcalls() {
        Class<?>[] clsArr = {Integer.TYPE};
        Class<?>[] clsArr2 = new Class[1];
        try {
            clsArr2[0] = Class.forName("java.awt.Component");
        } catch (ClassNotFoundException e2) {
            debugString("[ERROR]:Exception: " + e2.toString());
        }
        Object[] objArr = new Object[1];
        boolean z2 = false;
        this.toolkit = Toolkit.getDefaultToolkit();
        if (this.useJAWT_DLL) {
            z2 = true;
        } else {
            try {
                this.javaGetComponentFromNativeWindowHandleMethod = this.toolkit.getClass().getMethod("getComponentFromNativeWindowHandle", clsArr);
                if (this.javaGetComponentFromNativeWindowHandleMethod != null) {
                    try {
                        objArr[0] = new Integer(1);
                        z2 = true;
                    } catch (IllegalAccessException e3) {
                        debugString("[ERROR]:Exception: " + e3.toString());
                    } catch (InvocationTargetException e4) {
                        debugString("[ERROR]:Exception: " + e4.toString());
                    }
                }
            } catch (NoSuchMethodException e5) {
                debugString("[ERROR]:Exception: " + e5.toString());
            } catch (SecurityException e6) {
                debugString("[ERROR]:Exception: " + e6.toString());
            }
            try {
                this.javaGetNativeWindowHandleFromComponentMethod = this.toolkit.getClass().getMethod("getNativeWindowHandleFromComponent", clsArr2);
                if (this.javaGetNativeWindowHandleFromComponentMethod != null) {
                    try {
                        objArr[0] = new Button("OK");
                        z2 = true;
                    } catch (IllegalAccessException e7) {
                        debugString("[ERROR]:Exception: " + e7.toString());
                    } catch (InvocationTargetException e8) {
                        debugString("[ERROR]:Exception: " + e8.toString());
                    } catch (Exception e9) {
                        debugString("[ERROR]:Exception: " + e9.toString());
                    }
                }
            } catch (NoSuchMethodException e10) {
                debugString("[ERROR]:Exception: " + e10.toString());
            } catch (SecurityException e11) {
                debugString("[ERROR]:Exception: " + e11.toString());
            }
        }
        return z2;
    }

    private static void registerVirtualFrame(final Accessible accessible, Integer num) {
        if (accessible != null) {
            AccessibleContext accessibleContext = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleContext call() throws Exception {
                    return accessible.getAccessibleContext();
                }
            }, accessible);
            windowHandleToContextMap.put(num, accessibleContext);
            contextToWindowHandleMap.put(accessibleContext, num);
        }
    }

    private static void revokeVirtualFrame(final Accessible accessible, Integer num) {
        AccessibleContext accessibleContext = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                return accessible.getAccessibleContext();
            }
        }, accessible);
        windowHandleToContextMap.remove(num);
        contextToWindowHandleMap.remove(accessibleContext);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void addNativeWindowHandler(NativeWindowHandler nativeWindowHandler) {
        if (nativeWindowHandler == null) {
            throw new IllegalArgumentException();
        }
        nativeWindowHandlers.addElement(nativeWindowHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean removeNativeWindowHandler(NativeWindowHandler nativeWindowHandler) {
        if (nativeWindowHandler == null) {
            throw new IllegalArgumentException();
        }
        return nativeWindowHandlers.removeElement(nativeWindowHandler);
    }

    private boolean isJavaWindow(int i2) {
        AccessibleContext contextFromNativeWindowHandle = getContextFromNativeWindowHandle(i2);
        if (contextFromNativeWindowHandle != null) {
            saveContextToWindowHandleMapping(contextFromNativeWindowHandle, i2);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveContextToWindowHandleMapping(AccessibleContext accessibleContext, int i2) {
        debugString("[INFO]:saveContextToWindowHandleMapping...");
        if (accessibleContext != null && !contextToWindowHandleMap.containsKey(accessibleContext)) {
            debugString("[INFO]: saveContextToWindowHandleMapping: ac = " + ((Object) accessibleContext) + "; handle = " + i2);
            contextToWindowHandleMap.put(accessibleContext, Integer.valueOf(i2));
        }
    }

    private AccessibleContext getContextFromNativeWindowHandle(int i2) {
        AccessibleContext accessibleContext = windowHandleToContextMap.get(Integer.valueOf(i2));
        if (accessibleContext != null) {
            saveContextToWindowHandleMapping(accessibleContext, i2);
            return accessibleContext;
        }
        int size = nativeWindowHandlers.size();
        for (int i3 = 0; i3 < size; i3++) {
            final Accessible accessibleFromNativeWindowHandle = nativeWindowHandlers.elementAt(i3).getAccessibleFromNativeWindowHandle(i2);
            if (accessibleFromNativeWindowHandle != null) {
                AccessibleContext accessibleContext2 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleContext call() throws Exception {
                        return accessibleFromNativeWindowHandle.getAccessibleContext();
                    }
                }, accessibleFromNativeWindowHandle);
                saveContextToWindowHandleMapping(accessibleContext2, i2);
                return accessibleContext2;
            }
        }
        return null;
    }

    private int getNativeWindowHandleFromContext(AccessibleContext accessibleContext) {
        debugString("[INFO]: getNativeWindowHandleFromContext: ac = " + ((Object) accessibleContext));
        try {
            return contextToWindowHandleMap.get(accessibleContext).intValue();
        } catch (Exception e2) {
            return 0;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$DefaultNativeWindowHandler.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$DefaultNativeWindowHandler.class */
    private class DefaultNativeWindowHandler implements NativeWindowHandler {
        private DefaultNativeWindowHandler() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.java.accessibility.AccessBridge.NativeWindowHandler
        public Accessible getAccessibleFromNativeWindowHandle(int i2) {
            final Component componentFromNativeWindowHandle = getComponentFromNativeWindowHandle(i2);
            if (componentFromNativeWindowHandle instanceof Accessible) {
                AccessBridge.this.saveContextToWindowHandleMapping((AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.DefaultNativeWindowHandler.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleContext call() throws Exception {
                        return componentFromNativeWindowHandle.getAccessibleContext();
                    }
                }, componentFromNativeWindowHandle), i2);
                return (Accessible) componentFromNativeWindowHandle;
            }
            return null;
        }

        private Component getComponentFromNativeWindowHandle(int i2) {
            if (AccessBridge.this.useJAWT_DLL) {
                AccessBridge.this.debugString("[INFO]:*** calling jawtGetComponentFromNativeWindowHandle");
                return AccessBridge.this.jawtGetComponentFromNativeWindowHandle(i2);
            }
            AccessBridge.this.debugString("[INFO]:*** calling javaGetComponentFromNativeWindowHandle");
            Object[] objArr = new Object[1];
            if (AccessBridge.this.javaGetComponentFromNativeWindowHandleMethod != null) {
                try {
                    objArr[0] = Integer.valueOf(i2);
                    Object objInvoke = AccessBridge.this.javaGetComponentFromNativeWindowHandleMethod.invoke(AccessBridge.this.toolkit, objArr);
                    if (objInvoke instanceof Accessible) {
                        final Accessible accessible = (Accessible) objInvoke;
                        AccessBridge.this.saveContextToWindowHandleMapping((AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.DefaultNativeWindowHandler.2
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public AccessibleContext call() throws Exception {
                                return accessible.getAccessibleContext();
                            }
                        }, (Component) objInvoke), i2);
                    }
                    return (Component) objInvoke;
                } catch (IllegalAccessException | InvocationTargetException e2) {
                    AccessBridge.this.debugString("[ERROR]:Exception: " + e2.toString());
                    return null;
                }
            }
            return null;
        }
    }

    private int getNativeWindowHandleFromComponent(final Component component) {
        if (this.useJAWT_DLL) {
            debugString("[INFO]:*** calling jawtGetNativeWindowHandleFromComponent");
            return jawtGetNativeWindowHandleFromComponent(component);
        }
        Object[] objArr = new Object[1];
        debugString("[INFO]:*** calling javaGetNativeWindowHandleFromComponent");
        if (this.javaGetNativeWindowHandleFromComponentMethod != null) {
            try {
                objArr[0] = component;
                Integer num = (Integer) this.javaGetNativeWindowHandleFromComponentMethod.invoke(this.toolkit, objArr);
                contextToWindowHandleMap.put((AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleContext call() throws Exception {
                        return component.getAccessibleContext();
                    }
                }, component), num);
                return num.intValue();
            } catch (IllegalAccessException e2) {
                debugString("[ERROR]:Exception: " + e2.toString());
                return -1;
            } catch (InvocationTargetException e3) {
                debugString("[ERROR]:Exception: " + e3.toString());
                return -1;
            }
        }
        return -1;
    }

    private AccessibleContext getAccessibleContextAt(int i2, int i3, AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        if (windowHandleToContextMap != null && windowHandleToContextMap.containsValue(getRootAccessibleContext(accessibleContext))) {
            return getAccessibleContextAt_1(i2, i3, accessibleContext);
        }
        return getAccessibleContextAt_2(i2, i3, accessibleContext);
    }

    private AccessibleContext getRootAccessibleContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent == null) {
                    return accessibleContext;
                }
                Accessible accessibleParent2 = accessibleParent.getAccessibleContext().getAccessibleParent();
                while (true) {
                    Accessible accessible = accessibleParent2;
                    if (accessible != null) {
                        accessibleParent = accessible;
                        accessibleParent2 = accessibleParent.getAccessibleContext().getAccessibleParent();
                    } else {
                        return accessibleParent.getAccessibleContext();
                    }
                }
            }
        }, accessibleContext);
    }

    private AccessibleContext getAccessibleContextAt_1(final int i2, final int i3, final AccessibleContext accessibleContext) {
        AccessibleContext accessibleContext2;
        debugString("[INFO]: getAccessibleContextAt_1 called");
        debugString("[INFO]:   -> x = " + i2 + " y = " + i3 + " parent = " + ((Object) accessibleContext));
        if (accessibleContext == null) {
            return null;
        }
        final AccessibleComponent accessibleComponent = (AccessibleComponent) InvocationUtils.invokeAndWait(new Callable<AccessibleComponent>() { // from class: com.sun.java.accessibility.AccessBridge.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleComponent call() throws Exception {
                return accessibleContext.getAccessibleComponent();
            }
        }, accessibleContext);
        if (accessibleComponent != null) {
            final Point point = (Point) InvocationUtils.invokeAndWait(new Callable<Point>() { // from class: com.sun.java.accessibility.AccessBridge.7
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Point call() throws Exception {
                    return accessibleComponent.getLocation();
                }
            }, accessibleContext);
            final Accessible accessible = (Accessible) InvocationUtils.invokeAndWait(new Callable<Accessible>() { // from class: com.sun.java.accessibility.AccessBridge.8
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Accessible call() throws Exception {
                    return accessibleComponent.getAccessibleAt(new Point(i2 - point.f12370x, i3 - point.f12371y));
                }
            }, accessibleContext);
            if (accessible != null && (accessibleContext2 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.9
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleContext call() throws Exception {
                    return accessible.getAccessibleContext();
                }
            }, accessibleContext)) != null) {
                if (accessibleContext2 != accessibleContext) {
                    return getAccessibleContextAt_1(i2 - point.f12370x, i3 - point.f12371y, accessibleContext2);
                }
                return accessibleContext2;
            }
        }
        return accessibleContext;
    }

    private AccessibleContext getAccessibleContextAt_2(final int i2, final int i3, AccessibleContext accessibleContext) {
        debugString("[INFO]: getAccessibleContextAt_2 called");
        debugString("[INFO]:   -> x = " + i2 + " y = " + i3 + " parent = " + ((Object) accessibleContext));
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                AccessibleContext accessibleContext2;
                Accessible accessibleAt = EventQueueMonitor.getAccessibleAt(new Point(i2, i3));
                if (accessibleAt != null && (accessibleContext2 = accessibleAt.getAccessibleContext()) != null) {
                    AccessBridge.this.debugString("[INFO]:   returning childAC = " + ((Object) accessibleContext2));
                    return accessibleContext2;
                }
                return null;
            }
        }, accessibleContext);
    }

    private AccessibleContext getAccessibleContextWithFocus() {
        final Accessible accessible;
        AccessibleContext accessibleContext;
        Component componentWithFocus = AWTEventMonitor.getComponentWithFocus();
        if (componentWithFocus != null && (accessible = Translator.getAccessible(componentWithFocus)) != null && (accessibleContext = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                return accessible.getAccessibleContext();
            }
        }, componentWithFocus)) != null) {
            return accessibleContext;
        }
        return null;
    }

    private String getAccessibleNameFromContext(final AccessibleContext accessibleContext) {
        debugString("[INFO]: ***** ac = " + ((Object) accessibleContext.getClass()));
        if (accessibleContext != null) {
            String str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.12
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return accessibleContext.getAccessibleName();
                }
            }, accessibleContext);
            if (str != null) {
                this.references.increment(str);
                debugString("[INFO]: Returning AccessibleName from Context: " + str);
                return str;
            }
            return null;
        }
        debugString("[INFO]: getAccessibleNameFromContext; ac = null!");
        return null;
    }

    private String getVirtualAccessibleNameFromContext(final AccessibleContext accessibleContext) {
        final AccessibleContext accessibleContext2;
        AccessibleRole accessibleRole;
        final AccessibleContext accessibleContext3;
        AccessibleRole accessibleRole2;
        final AccessibleContext accessibleContext4;
        final AccessibleContext accessibleContext5;
        AccessibleRelation accessibleRelation;
        AccessibleContext accessibleContext6;
        String str;
        final AccessibleValue accessibleValue;
        Number number;
        String string;
        final AccessibleIcon[] accessibleIconArr;
        String str2;
        String accessibleTextRangeFromContext;
        if (null != accessibleContext) {
            String str3 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.13
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return accessibleContext.getAccessibleName();
                }
            }, accessibleContext);
            if (null != str3 && 0 != str3.length()) {
                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from AccessibleContext::getAccessibleName.");
                this.references.increment(str3);
                return str3;
            }
            String str4 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.14
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return accessibleContext.getAccessibleDescription();
                }
            }, accessibleContext);
            if (null != str4 && 0 != str4.length()) {
                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from AccessibleContext::getAccessibleDescription.");
                this.references.increment(str4);
                return str4;
            }
            debugString("[WARN]: The Virtual Accessible Name was not found using AccessibleContext::getAccessibleDescription. or getAccessibleName");
            boolean z2 = false;
            AccessibleRole accessibleRole3 = (AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.15
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleRole call() throws Exception {
                    return accessibleContext.getAccessibleRole();
                }
            }, accessibleContext);
            final AccessibleContext accessibleParentFromContext = null;
            AccessibleRole accessibleRole4 = AccessibleRole.UNKNOWN;
            if (this.extendedVirtualNameSearchRoles.contains(accessibleRole3)) {
                accessibleParentFromContext = getAccessibleParentFromContext(accessibleContext);
                if (null != accessibleParentFromContext) {
                    accessibleRole4 = (AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.16
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleRole call() throws Exception {
                            return accessibleParentFromContext.getAccessibleRole();
                        }
                    }, accessibleContext);
                    if (AccessibleRole.UNKNOWN != accessibleRole4) {
                        z2 = true;
                        if (this.noExtendedVirtualNameSearchParentRoles.contains(accessibleRole4)) {
                            z2 = false;
                        }
                    }
                }
            }
            if (false == z2) {
                debugString("[INFO]: bk -- getVirtualAccessibleNameFromContext will not use the extended name search algorithm.  role = " + (accessibleRole3 != null ? accessibleRole3.toDisplayString(Locale.US) : FXMLLoader.NULL_KEYWORD));
                if (AccessibleRole.LABEL != accessibleRole3) {
                    if (AccessibleRole.TOGGLE_BUTTON == accessibleRole3 || AccessibleRole.PUSH_BUTTON == accessibleRole3) {
                        debugString("[INFO]: bk -- Attempting to obtain the Virtual Accessible Name from the Accessible Icon information.");
                        final AccessibleIcon[] accessibleIconArr2 = (AccessibleIcon[]) InvocationUtils.invokeAndWait(new Callable<AccessibleIcon[]>() { // from class: com.sun.java.accessibility.AccessBridge.25
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public AccessibleIcon[] call() {
                                return accessibleContext.getAccessibleIcon();
                            }
                        }, accessibleContext);
                        if (null != accessibleIconArr2 && accessibleIconArr2.length > 0 && (str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.26
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public String call() {
                                return accessibleIconArr2[0].getAccessibleIconDescription();
                            }
                        }, accessibleContext)) != null) {
                            debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from the description of the first Accessible Icon found in the TOGGLE_BUTTON or PUSH_BUTTON object.");
                            this.references.increment(str);
                            return str;
                        }
                        return null;
                    }
                    if (AccessibleRole.CHECK_BOX == accessibleRole3 && null != (accessibleValue = (AccessibleValue) InvocationUtils.invokeAndWait(new Callable<AccessibleValue>() { // from class: com.sun.java.accessibility.AccessBridge.27
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleValue call() throws Exception {
                            return accessibleContext.getAccessibleValue();
                        }
                    }, accessibleContext)) && null != (number = (Number) InvocationUtils.invokeAndWait(new Callable<Number>() { // from class: com.sun.java.accessibility.AccessBridge.28
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Number call() throws Exception {
                            return accessibleValue.getCurrentAccessibleValue();
                        }
                    }, accessibleContext))) {
                        if (1 == number.intValue()) {
                            string = Boolean.TRUE.toString();
                        } else if (0 == number.intValue()) {
                            string = Boolean.FALSE.toString();
                        } else {
                            string = number.toString();
                        }
                        if (null != string) {
                            this.references.increment(string);
                            return string;
                        }
                        return null;
                    }
                    return null;
                }
                final AccessibleText accessibleText = (AccessibleText) InvocationUtils.invokeAndWait(new Callable<AccessibleText>() { // from class: com.sun.java.accessibility.AccessBridge.17
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleText call() throws Exception {
                        return accessibleContext.getAccessibleText();
                    }
                }, accessibleContext);
                if (null != accessibleText && null != (accessibleTextRangeFromContext = getAccessibleTextRangeFromContext(accessibleContext, 0, ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.18
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Integer call() throws Exception {
                        return Integer.valueOf(accessibleText.getCharCount());
                    }
                }, accessibleContext)).intValue()))) {
                    debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from the Accessible Text of the LABEL object.");
                    this.references.increment(accessibleTextRangeFromContext);
                    return accessibleTextRangeFromContext;
                }
                debugString("[INFO]: bk -- Attempting to obtain the Virtual Accessible Name from the Accessible Icon information.");
                final AccessibleIcon[] accessibleIconArr3 = (AccessibleIcon[]) InvocationUtils.invokeAndWait(new Callable<AccessibleIcon[]>() { // from class: com.sun.java.accessibility.AccessBridge.19
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleIcon[] call() throws Exception {
                        return accessibleContext.getAccessibleIcon();
                    }
                }, accessibleContext);
                if (null != accessibleIconArr3 && accessibleIconArr3.length > 0) {
                    String str5 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.20
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public String call() throws Exception {
                            return accessibleIconArr3[0].getAccessibleIconDescription();
                        }
                    }, accessibleContext);
                    if (str5 != null) {
                        debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from the description of the first Accessible Icon found in the LABEL object.");
                        this.references.increment(str5);
                        return str5;
                    }
                    return null;
                }
                final AccessibleContext accessibleParentFromContext2 = getAccessibleParentFromContext(accessibleContext);
                if (null != accessibleParentFromContext2) {
                    if (AccessibleRole.TABLE == ((AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.21
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleRole call() throws Exception {
                            return accessibleParentFromContext2.getAccessibleRole();
                        }
                    }, accessibleContext))) {
                        final AccessibleContext accessibleChildFromContext = getAccessibleChildFromContext(accessibleParentFromContext2, ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.22
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public Integer call() throws Exception {
                                return Integer.valueOf(accessibleContext.getAccessibleIndexInParent());
                            }
                        }, accessibleContext)).intValue());
                        debugString("[INFO]: bk -- Making a second attempt to obtain the Virtual Accessible Name from the Accessible Icon information for the Table Cell.");
                        if (accessibleChildFromContext != null && null != (accessibleIconArr = (AccessibleIcon[]) InvocationUtils.invokeAndWait(new Callable<AccessibleIcon[]>() { // from class: com.sun.java.accessibility.AccessBridge.23
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public AccessibleIcon[] call() throws Exception {
                                return accessibleChildFromContext.getAccessibleIcon();
                            }
                        }, accessibleContext)) && accessibleIconArr.length > 0 && (str2 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.24
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public String call() {
                                return accessibleIconArr[0].getAccessibleIconDescription();
                            }
                        }, accessibleContext)) != null) {
                            debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from the description of the first Accessible Icon found in the Table Cell object.");
                            this.references.increment(str2);
                            return str2;
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            }
            final AccessibleContext accessibleContext7 = accessibleParentFromContext;
            String str6 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.29
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return accessibleContext7.getAccessibleName();
                }
            }, accessibleContext);
            String str7 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.30
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return accessibleContext7.getAccessibleDescription();
                }
            }, accessibleContext);
            if (AccessibleRole.SLIDER == accessibleRole3 && AccessibleRole.PANEL == accessibleRole4 && null != str6) {
                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from the Accessible Name of the SLIDER object's parent object.");
                this.references.increment(str6);
                return str6;
            }
            boolean z3 = false;
            AccessibleContext accessibleContext8 = accessibleContext;
            if (AccessibleRole.TEXT == accessibleRole3 && AccessibleRole.COMBO_BOX == accessibleRole4) {
                z3 = true;
                if (null != str6) {
                    debugString("[INFO]: bk -- The Virtual Accessible Name for this Edit Combo box was obtained from the Accessible Name of the object's parent object.");
                    this.references.increment(str6);
                    return str6;
                }
                if (null != str7) {
                    debugString("[INFO]: bk -- The Virtual Accessible Name for this Edit Combo box was obtained from the Accessible Description of the object's parent object.");
                    this.references.increment(str7);
                    return str7;
                }
                accessibleContext8 = accessibleParentFromContext;
                AccessibleRole accessibleRole5 = AccessibleRole.UNKNOWN;
                accessibleParentFromContext = getAccessibleParentFromContext(accessibleContext8);
                if (null != accessibleParentFromContext) {
                }
            }
            String javaVersionProperty = getJavaVersionProperty();
            if (null != javaVersionProperty && javaVersionProperty.compareTo("1.3") >= 0) {
                final AccessibleContext accessibleContext9 = accessibleParentFromContext;
                AccessibleRelationSet accessibleRelationSet = (AccessibleRelationSet) InvocationUtils.invokeAndWait(new Callable<AccessibleRelationSet>() { // from class: com.sun.java.accessibility.AccessBridge.32
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleRelationSet call() throws Exception {
                        return accessibleContext9.getAccessibleRelationSet();
                    }
                }, accessibleContext);
                if (accessibleRelationSet != null && accessibleRelationSet.size() > 0 && accessibleRelationSet.contains(AccessibleRelation.LABELED_BY) && (accessibleRelation = accessibleRelationSet.get(AccessibleRelation.LABELED_BY)) != null) {
                    Object obj = accessibleRelation.getTarget()[0];
                    if ((obj instanceof Accessible) && (accessibleContext6 = ((Accessible) obj).getAccessibleContext()) != null) {
                        String accessibleName = accessibleContext6.getAccessibleName();
                        String accessibleDescription = accessibleContext6.getAccessibleDescription();
                        if (null != accessibleName) {
                            debugString("[INFO]: bk -- The Virtual Accessible Name was obtained using the LABELED_BY AccessibleRelation -- Name Case.");
                            this.references.increment(accessibleName);
                            return accessibleName;
                        }
                        if (null != accessibleDescription) {
                            debugString("[INFO]: bk -- The Virtual Accessible Name was obtained using the LABELED_BY AccessibleRelation -- Description Case.");
                            this.references.increment(accessibleDescription);
                            return accessibleDescription;
                        }
                    }
                }
            } else {
                debugString("[ERROR]:bk -- This version of Java does not support AccessibleContext::getAccessibleRelationSet.");
            }
            int iIntValue = 0;
            final AccessibleContext accessibleContext10 = accessibleContext8;
            int iIntValue2 = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.33
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    return Integer.valueOf(accessibleContext10.getAccessibleIndexInParent());
                }
            }, accessibleContext)).intValue();
            if (null != accessibleParentFromContext) {
                final AccessibleContext accessibleContext11 = accessibleParentFromContext;
                iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.34
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Integer call() throws Exception {
                        return Integer.valueOf(accessibleContext11.getAccessibleChildrenCount() - 1);
                    }
                }, accessibleContext)).intValue();
            }
            int accessibleXcoordFromContext = getAccessibleXcoordFromContext(accessibleContext8);
            int accessibleYcoordFromContext = getAccessibleYcoordFromContext(accessibleContext8);
            getAccessibleWidthFromContext(accessibleContext8);
            getAccessibleHeightFromContext(accessibleContext8);
            int i2 = accessibleXcoordFromContext + 2;
            int i3 = accessibleYcoordFromContext + 2;
            for (int i4 = iIntValue2 - 1; i4 >= 0; i4--) {
                final int i5 = i4;
                final AccessibleContext accessibleContext12 = accessibleParentFromContext;
                final Accessible accessible = (Accessible) InvocationUtils.invokeAndWait(new Callable<Accessible>() { // from class: com.sun.java.accessibility.AccessBridge.35
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Accessible call() throws Exception {
                        return accessibleContext12.getAccessibleChild(i5);
                    }
                }, accessibleContext);
                if (null != accessible && null != (accessibleContext5 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.36
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleContext call() throws Exception {
                        return accessible.getAccessibleContext();
                    }
                }, accessibleContext))) {
                    if (AccessibleRole.LABEL == ((AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.37
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleRole call() throws Exception {
                            return accessibleContext5.getAccessibleRole();
                        }
                    }, accessibleContext))) {
                        int accessibleXcoordFromContext2 = getAccessibleXcoordFromContext(accessibleContext5);
                        int accessibleYcoordFromContext2 = getAccessibleYcoordFromContext(accessibleContext5);
                        int accessibleWidthFromContext = getAccessibleWidthFromContext(accessibleContext5);
                        int accessibleHeightFromContext = getAccessibleHeightFromContext(accessibleContext5);
                        if (accessibleXcoordFromContext2 < accessibleXcoordFromContext && accessibleYcoordFromContext2 <= i3 && i3 <= accessibleYcoordFromContext2 + accessibleHeightFromContext) {
                            String str8 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.38
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext5.getAccessibleName();
                                }
                            }, accessibleContext);
                            if (null != str8) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Name of a LABEL object positioned to the left of the object.");
                                this.references.increment(str8);
                                return str8;
                            }
                            String str9 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.39
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext5.getAccessibleDescription();
                                }
                            }, accessibleContext);
                            if (null != str9) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Description of a LABEL object positioned to the left of the object.");
                                this.references.increment(str9);
                                return str9;
                            }
                        } else if (accessibleYcoordFromContext2 < i3 && accessibleXcoordFromContext2 <= i2 && i2 <= accessibleXcoordFromContext2 + accessibleWidthFromContext) {
                            String str10 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.40
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext5.getAccessibleName();
                                }
                            }, accessibleContext);
                            if (null != str10) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Name of a LABEL object positioned above the object.");
                                this.references.increment(str10);
                                return str10;
                            }
                            String str11 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.41
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext5.getAccessibleDescription();
                                }
                            }, accessibleContext);
                            if (null != str11) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Description of a LABEL object positioned above the object.");
                                this.references.increment(str11);
                                return str11;
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            for (int i6 = iIntValue2 + 1; i6 <= iIntValue; i6++) {
                final int i7 = i6;
                final AccessibleContext accessibleContext13 = accessibleParentFromContext;
                final Accessible accessible2 = (Accessible) InvocationUtils.invokeAndWait(new Callable<Accessible>() { // from class: com.sun.java.accessibility.AccessBridge.42
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Accessible call() throws Exception {
                        return accessibleContext13.getAccessibleChild(i7);
                    }
                }, accessibleContext);
                if (null != accessible2 && null != (accessibleContext4 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.43
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleContext call() throws Exception {
                        return accessible2.getAccessibleContext();
                    }
                }, accessibleContext))) {
                    if (AccessibleRole.LABEL == ((AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.44
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleRole call() throws Exception {
                            return accessibleContext4.getAccessibleRole();
                        }
                    }, accessibleContext))) {
                        int accessibleXcoordFromContext3 = getAccessibleXcoordFromContext(accessibleContext4);
                        int accessibleYcoordFromContext3 = getAccessibleYcoordFromContext(accessibleContext4);
                        int accessibleWidthFromContext2 = getAccessibleWidthFromContext(accessibleContext4);
                        int accessibleHeightFromContext2 = getAccessibleHeightFromContext(accessibleContext4);
                        if (accessibleXcoordFromContext3 < accessibleXcoordFromContext && accessibleYcoordFromContext3 <= i3 && i3 <= accessibleYcoordFromContext3 + accessibleHeightFromContext2) {
                            String str12 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.45
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext4.getAccessibleName();
                                }
                            }, accessibleContext);
                            if (null != str12) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Name of a LABEL object positioned to the left of the object.");
                                this.references.increment(str12);
                                return str12;
                            }
                            String str13 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.46
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext4.getAccessibleDescription();
                                }
                            }, accessibleContext);
                            if (null != str13) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Description of a LABEL object positioned to the left of the object.");
                                this.references.increment(str13);
                                return str13;
                            }
                        } else if (accessibleYcoordFromContext3 < i3 && accessibleXcoordFromContext3 <= i2 && i2 <= accessibleXcoordFromContext3 + accessibleWidthFromContext2) {
                            String str14 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.47
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext4.getAccessibleName();
                                }
                            }, accessibleContext);
                            if (null != str14) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Name of a LABEL object positioned above the object.");
                                this.references.increment(str14);
                                return str14;
                            }
                            String str15 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.48
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext4.getAccessibleDescription();
                                }
                            }, accessibleContext);
                            if (null != str15) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Description of a LABEL object positioned above the object.");
                                this.references.increment(str15);
                                return str15;
                            }
                        }
                    } else {
                        continue;
                    }
                }
            }
            if (AccessibleRole.TEXT == accessibleRole3 || AccessibleRole.COMBO_BOX == accessibleRole3 || z3) {
                for (int i8 = iIntValue2 - 1; i8 >= 0; i8--) {
                    final int i9 = i8;
                    final AccessibleContext accessibleContext14 = accessibleParentFromContext;
                    final Accessible accessible3 = (Accessible) InvocationUtils.invokeAndWait(new Callable<Accessible>() { // from class: com.sun.java.accessibility.AccessBridge.49
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Accessible call() throws Exception {
                            return accessibleContext14.getAccessibleChild(i9);
                        }
                    }, accessibleContext);
                    if (null != accessible3 && null != (accessibleContext3 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.50
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleContext call() throws Exception {
                            return accessible3.getAccessibleContext();
                        }
                    }, accessibleContext)) && (AccessibleRole.PUSH_BUTTON == (accessibleRole2 = (AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.51
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleRole call() throws Exception {
                            return accessibleContext3.getAccessibleRole();
                        }
                    }, accessibleContext)) || AccessibleRole.TOGGLE_BUTTON == accessibleRole2)) {
                        int accessibleXcoordFromContext4 = getAccessibleXcoordFromContext(accessibleContext3);
                        int accessibleYcoordFromContext4 = getAccessibleYcoordFromContext(accessibleContext3);
                        getAccessibleWidthFromContext(accessibleContext3);
                        int accessibleHeightFromContext3 = getAccessibleHeightFromContext(accessibleContext3);
                        if (accessibleXcoordFromContext4 < accessibleXcoordFromContext && accessibleYcoordFromContext4 <= i3 && i3 <= accessibleYcoordFromContext4 + accessibleHeightFromContext3) {
                            String str16 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.52
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext3.getAccessibleName();
                                }
                            }, accessibleContext);
                            if (null != str16) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Name of a PUSH_BUTTON or TOGGLE_BUTTON object positioned to the left of the object.");
                                this.references.increment(str16);
                                return str16;
                            }
                            String str17 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.53
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext3.getAccessibleDescription();
                                }
                            }, accessibleContext);
                            if (null != str17) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Description of a PUSH_BUTTON or TOGGLE_BUTTON object positioned to the left of the object.");
                                this.references.increment(str17);
                                return str17;
                            }
                        }
                    }
                }
                for (int i10 = iIntValue2 + 1; i10 <= iIntValue; i10++) {
                    final int i11 = i10;
                    final AccessibleContext accessibleContext15 = accessibleParentFromContext;
                    final Accessible accessible4 = (Accessible) InvocationUtils.invokeAndWait(new Callable<Accessible>() { // from class: com.sun.java.accessibility.AccessBridge.54
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Accessible call() throws Exception {
                            return accessibleContext15.getAccessibleChild(i11);
                        }
                    }, accessibleContext);
                    if (null != accessible4 && null != (accessibleContext2 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.55
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleContext call() throws Exception {
                            return accessible4.getAccessibleContext();
                        }
                    }, accessibleContext)) && (AccessibleRole.PUSH_BUTTON == (accessibleRole = (AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.56
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleRole call() throws Exception {
                            return accessibleContext2.getAccessibleRole();
                        }
                    }, accessibleContext)) || AccessibleRole.TOGGLE_BUTTON == accessibleRole)) {
                        int accessibleXcoordFromContext5 = getAccessibleXcoordFromContext(accessibleContext2);
                        int accessibleYcoordFromContext5 = getAccessibleYcoordFromContext(accessibleContext2);
                        getAccessibleWidthFromContext(accessibleContext2);
                        int accessibleHeightFromContext4 = getAccessibleHeightFromContext(accessibleContext2);
                        if (accessibleXcoordFromContext5 < accessibleXcoordFromContext && accessibleYcoordFromContext5 <= i3 && i3 <= accessibleYcoordFromContext5 + accessibleHeightFromContext4) {
                            String str18 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.57
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext2.getAccessibleName();
                                }
                            }, accessibleContext);
                            if (null != str18) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Name of a PUSH_BUTTON or TOGGLE_BUTTON object positioned to the left of the object.");
                                this.references.increment(str18);
                                return str18;
                            }
                            String str19 = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.58
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public String call() {
                                    return accessibleContext2.getAccessibleDescription();
                                }
                            }, accessibleContext);
                            if (null != str19) {
                                debugString("[INFO]: bk -- The Virtual Accessible Name was obtained from Accessible Description of a PUSH_BUTTON or TOGGLE_BUTTON object positioned to the left of the object.");
                                this.references.increment(str19);
                                return str19;
                            }
                        }
                    }
                }
                return null;
            }
            return null;
        }
        debugString("[ERROR]: AccessBridge::getVirtualAccessibleNameFromContext error - ac == null.");
        return null;
    }

    private String getAccessibleDescriptionFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext != null) {
            String str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.59
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return accessibleContext.getAccessibleDescription();
                }
            }, accessibleContext);
            if (str != null) {
                this.references.increment(str);
                debugString("[INFO]: Returning AccessibleDescription from Context: " + str);
                return str;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleDescriptionFromContext; ac = null");
        return null;
    }

    private String getAccessibleRoleStringFromContext(final AccessibleContext accessibleContext) {
        String displayString;
        if (accessibleContext != null) {
            AccessibleRole accessibleRole = (AccessibleRole) InvocationUtils.invokeAndWait(new Callable<AccessibleRole>() { // from class: com.sun.java.accessibility.AccessBridge.60
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleRole call() throws Exception {
                    return accessibleContext.getAccessibleRole();
                }
            }, accessibleContext);
            if (accessibleRole != null && (displayString = accessibleRole.toDisplayString(Locale.US)) != null) {
                this.references.increment(displayString);
                debugString("[INFO]: Returning AccessibleRole from Context: " + displayString);
                return displayString;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleRoleStringFromContext; ac = null");
        return null;
    }

    private String getAccessibleRoleStringFromContext_en_US(AccessibleContext accessibleContext) {
        return getAccessibleRoleStringFromContext(accessibleContext);
    }

    private String getAccessibleStatesStringFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext != null) {
            AccessibleStateSet accessibleStateSet = (AccessibleStateSet) InvocationUtils.invokeAndWait(new Callable<AccessibleStateSet>() { // from class: com.sun.java.accessibility.AccessBridge.61
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleStateSet call() throws Exception {
                    return accessibleContext.getAccessibleStateSet();
                }
            }, accessibleContext);
            if (accessibleStateSet != null) {
                String string = accessibleStateSet.toString();
                if (string != null && string.indexOf(AccessibleState.MANAGES_DESCENDANTS.toDisplayString(Locale.US)) == -1) {
                    AccessibleRole accessibleRole = (AccessibleRole) InvocationUtils.invokeAndWait(() -> {
                        return accessibleContext.getAccessibleRole();
                    }, accessibleContext);
                    if (accessibleRole == AccessibleRole.LIST || accessibleRole == AccessibleRole.TABLE || accessibleRole == AccessibleRole.TREE) {
                        string = (string + ",") + AccessibleState.MANAGES_DESCENDANTS.toDisplayString(Locale.US);
                    }
                    this.references.increment(string);
                    debugString("[INFO]: Returning AccessibleStateSet from Context: " + string);
                    return string;
                }
                return null;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleStatesStringFromContext; ac = null");
        return null;
    }

    private String getAccessibleStatesStringFromContext_en_US(final AccessibleContext accessibleContext) {
        AccessibleStateSet accessibleStateSet;
        if (accessibleContext != null && (accessibleStateSet = (AccessibleStateSet) InvocationUtils.invokeAndWait(new Callable<AccessibleStateSet>() { // from class: com.sun.java.accessibility.AccessBridge.62
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleStateSet call() throws Exception {
                return accessibleContext.getAccessibleStateSet();
            }
        }, accessibleContext)) != null) {
            String displayString = "";
            AccessibleState[] array = accessibleStateSet.toArray();
            if (array != null && array.length > 0) {
                displayString = array[0].toDisplayString(Locale.US);
                for (int i2 = 1; i2 < array.length; i2++) {
                    displayString = displayString + "," + array[i2].toDisplayString(Locale.US);
                }
            }
            this.references.increment(displayString);
            debugString("[INFO]: Returning AccessibleStateSet en_US from Context: " + displayString);
            return displayString;
        }
        debugString("[ERROR]: getAccessibleStatesStringFromContext; ac = null");
        return null;
    }

    private AccessibleContext getAccessibleParentFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.63
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                AccessibleContext accessibleContext2;
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent != null && (accessibleContext2 = accessibleParent.getAccessibleContext()) != null) {
                    return accessibleContext2;
                }
                return null;
            }
        }, accessibleContext);
    }

    private int getAccessibleIndexInParentFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.64
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleContext.getAccessibleIndexInParent());
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleChildrenCountFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.65
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleContext.getAccessibleChildrenCount());
            }
        }, accessibleContext)).intValue();
    }

    private AccessibleContext getAccessibleChildFromContext(final AccessibleContext accessibleContext, final int i2) {
        if (accessibleContext == null) {
            return null;
        }
        final JTable jTable = (JTable) InvocationUtils.invokeAndWait(new Callable<JTable>() { // from class: com.sun.java.accessibility.AccessBridge.66
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public JTable call() throws Exception {
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent != null) {
                    Accessible accessibleChild = accessibleParent.getAccessibleContext().getAccessibleChild(accessibleContext.getAccessibleIndexInParent());
                    if (accessibleChild instanceof JTable) {
                        return (JTable) accessibleChild;
                    }
                    return null;
                }
                return null;
            }
        }, accessibleContext);
        if (jTable == null) {
            return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.67
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleContext call() throws Exception {
                    Accessible accessibleChild = accessibleContext.getAccessibleChild(i2);
                    if (accessibleChild != null) {
                        return accessibleChild.getAccessibleContext();
                    }
                    return null;
                }
            }, accessibleContext);
        }
        AccessibleTable accessibleTableFromContext = getAccessibleTableFromContext(accessibleContext);
        final int accessibleTableRow = getAccessibleTableRow(accessibleTableFromContext, i2);
        final int accessibleTableColumn = getAccessibleTableColumn(accessibleTableFromContext, i2);
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.68
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                TableCellRenderer cellRenderer = jTable.getCellRenderer(accessibleTableRow, accessibleTableColumn);
                if (cellRenderer == null) {
                    cellRenderer = jTable.getDefaultRenderer(jTable.getColumnClass(accessibleTableColumn));
                }
                Component tableCellRendererComponent = cellRenderer.getTableCellRendererComponent(jTable, jTable.getValueAt(accessibleTableRow, accessibleTableColumn), false, false, accessibleTableRow, accessibleTableColumn);
                if (tableCellRendererComponent instanceof Accessible) {
                    return tableCellRendererComponent.getAccessibleContext();
                }
                return null;
            }
        }, accessibleContext);
    }

    private Rectangle getAccessibleBoundsOnScreenFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (Rectangle) InvocationUtils.invokeAndWait(new Callable<Rectangle>() { // from class: com.sun.java.accessibility.AccessBridge.69
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Rectangle call() throws Exception {
                Rectangle bounds;
                AccessibleComponent accessibleComponent = accessibleContext.getAccessibleComponent();
                if (accessibleComponent != null && (bounds = accessibleComponent.getBounds()) != null) {
                    try {
                        Point locationOnScreen = accessibleComponent.getLocationOnScreen();
                        if (locationOnScreen != null) {
                            bounds.f12372x = locationOnScreen.f12370x;
                            bounds.f12373y = locationOnScreen.f12371y;
                            return bounds;
                        }
                        return null;
                    } catch (Exception e2) {
                        return null;
                    }
                }
                return null;
            }
        }, accessibleContext);
    }

    private int getAccessibleXcoordFromContext(AccessibleContext accessibleContext) {
        if (accessibleContext != null) {
            Rectangle accessibleBoundsOnScreenFromContext = getAccessibleBoundsOnScreenFromContext(accessibleContext);
            if (accessibleBoundsOnScreenFromContext != null) {
                debugString("[INFO]: Returning Accessible x coord from Context: " + accessibleBoundsOnScreenFromContext.f12372x);
                return accessibleBoundsOnScreenFromContext.f12372x;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleXcoordFromContext ac = null");
        return -1;
    }

    private int getAccessibleYcoordFromContext(AccessibleContext accessibleContext) {
        debugString("[INFO]: getAccessibleYcoordFromContext() called");
        if (accessibleContext != null) {
            Rectangle accessibleBoundsOnScreenFromContext = getAccessibleBoundsOnScreenFromContext(accessibleContext);
            if (accessibleBoundsOnScreenFromContext != null) {
                return accessibleBoundsOnScreenFromContext.f12373y;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleYcoordFromContext; ac = null");
        return -1;
    }

    private int getAccessibleHeightFromContext(AccessibleContext accessibleContext) {
        if (accessibleContext != null) {
            Rectangle accessibleBoundsOnScreenFromContext = getAccessibleBoundsOnScreenFromContext(accessibleContext);
            if (accessibleBoundsOnScreenFromContext != null) {
                return accessibleBoundsOnScreenFromContext.height;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleHeightFromContext; ac = null");
        return -1;
    }

    private int getAccessibleWidthFromContext(AccessibleContext accessibleContext) {
        if (accessibleContext != null) {
            Rectangle accessibleBoundsOnScreenFromContext = getAccessibleBoundsOnScreenFromContext(accessibleContext);
            if (accessibleBoundsOnScreenFromContext != null) {
                return accessibleBoundsOnScreenFromContext.width;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleWidthFromContext; ac = null");
        return -1;
    }

    private AccessibleComponent getAccessibleComponentFromContext(AccessibleContext accessibleContext) {
        if (accessibleContext != null) {
            AccessibleComponent accessibleComponent = (AccessibleComponent) InvocationUtils.invokeAndWait(() -> {
                return accessibleContext.getAccessibleComponent();
            }, accessibleContext);
            if (accessibleComponent != null) {
                debugString("[INFO]: Returning AccessibleComponent Context");
                return accessibleComponent;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleComponentFromContext; ac = null");
        return null;
    }

    private AccessibleAction getAccessibleActionFromContext(final AccessibleContext accessibleContext) {
        debugString("[INFO]: Returning AccessibleAction Context");
        if (accessibleContext == null) {
            return null;
        }
        return (AccessibleAction) InvocationUtils.invokeAndWait(new Callable<AccessibleAction>() { // from class: com.sun.java.accessibility.AccessBridge.70
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleAction call() throws Exception {
                return accessibleContext.getAccessibleAction();
            }
        }, accessibleContext);
    }

    private AccessibleSelection getAccessibleSelectionFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (AccessibleSelection) InvocationUtils.invokeAndWait(new Callable<AccessibleSelection>() { // from class: com.sun.java.accessibility.AccessBridge.71
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleSelection call() throws Exception {
                return accessibleContext.getAccessibleSelection();
            }
        }, accessibleContext);
    }

    private AccessibleText getAccessibleTextFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (AccessibleText) InvocationUtils.invokeAndWait(new Callable<AccessibleText>() { // from class: com.sun.java.accessibility.AccessBridge.72
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleText call() throws Exception {
                return accessibleContext.getAccessibleText();
            }
        }, accessibleContext);
    }

    private AccessibleValue getAccessibleValueFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (AccessibleValue) InvocationUtils.invokeAndWait(new Callable<AccessibleValue>() { // from class: com.sun.java.accessibility.AccessBridge.73
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleValue call() throws Exception {
                return accessibleContext.getAccessibleValue();
            }
        }, accessibleContext);
    }

    private Rectangle getCaretLocation(final AccessibleContext accessibleContext) {
        debugString("[INFO]: getCaretLocation");
        if (accessibleContext == null) {
            return null;
        }
        return (Rectangle) InvocationUtils.invokeAndWait(new Callable<Rectangle>() { // from class: com.sun.java.accessibility.AccessBridge.74
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Rectangle call() throws Exception {
                Rectangle rectangleModelToView = null;
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent instanceof Accessible) {
                    Accessible accessibleChild = accessibleParent.getAccessibleContext().getAccessibleChild(accessibleContext.getAccessibleIndexInParent());
                    if (accessibleChild instanceof JTextComponent) {
                        JTextComponent jTextComponent = (JTextComponent) accessibleChild;
                        try {
                            rectangleModelToView = jTextComponent.modelToView(jTextComponent.getCaretPosition());
                            if (rectangleModelToView != null) {
                                Point locationOnScreen = jTextComponent.getLocationOnScreen();
                                rectangleModelToView.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                            }
                        } catch (BadLocationException e2) {
                        }
                    }
                }
                return rectangleModelToView;
            }
        }, accessibleContext);
    }

    private int getCaretLocationX(AccessibleContext accessibleContext) {
        Rectangle caretLocation = getCaretLocation(accessibleContext);
        if (caretLocation != null) {
            return caretLocation.f12372x;
        }
        return -1;
    }

    private int getCaretLocationY(AccessibleContext accessibleContext) {
        Rectangle caretLocation = getCaretLocation(accessibleContext);
        if (caretLocation != null) {
            return caretLocation.f12373y;
        }
        return -1;
    }

    private int getCaretLocationHeight(AccessibleContext accessibleContext) {
        Rectangle caretLocation = getCaretLocation(accessibleContext);
        if (caretLocation != null) {
            return caretLocation.height;
        }
        return -1;
    }

    private int getCaretLocationWidth(AccessibleContext accessibleContext) {
        Rectangle caretLocation = getCaretLocation(accessibleContext);
        if (caretLocation != null) {
            return caretLocation.width;
        }
        return -1;
    }

    private int getAccessibleCharCountFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.75
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (accessibleText != null) {
                    return Integer.valueOf(accessibleText.getCharCount());
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleCaretPositionFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.76
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (accessibleText != null) {
                    return Integer.valueOf(accessibleText.getCaretPosition());
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleIndexAtPointFromContext(final AccessibleContext accessibleContext, final int i2, final int i3) {
        debugString("[INFO]: getAccessibleIndexAtPointFromContext: x = " + i2 + "; y = " + i3);
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.77
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                AccessibleComponent accessibleComponent = accessibleContext.getAccessibleComponent();
                if (accessibleText != null && accessibleComponent != null) {
                    try {
                        Point locationOnScreen = accessibleComponent.getLocationOnScreen();
                        if (locationOnScreen != null) {
                            int i4 = i2 - locationOnScreen.f12370x;
                            if (i4 < 0) {
                                i4 = 0;
                            }
                            int i5 = i3 - locationOnScreen.f12371y;
                            if (i5 < 0) {
                                i5 = 0;
                            }
                            new Point(i4, i5);
                            return Integer.valueOf(accessibleText.getIndexAtPoint(new Point(i4, i5)));
                        }
                    } catch (Exception e2) {
                    }
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private String getAccessibleLetterAtIndexFromContext(final AccessibleContext accessibleContext, final int i2) {
        if (accessibleContext != null) {
            String str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.78
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    AccessibleText accessibleText = accessibleContext.getAccessibleText();
                    if (accessibleText == null) {
                        return null;
                    }
                    return accessibleText.getAtIndex(1, i2);
                }
            }, accessibleContext);
            if (str != null) {
                this.references.increment(str);
                return str;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleLetterAtIndexFromContext; ac = null");
        return null;
    }

    private String getAccessibleWordAtIndexFromContext(final AccessibleContext accessibleContext, final int i2) {
        if (accessibleContext != null) {
            String str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.79
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    AccessibleText accessibleText = accessibleContext.getAccessibleText();
                    if (accessibleText == null) {
                        return null;
                    }
                    return accessibleText.getAtIndex(2, i2);
                }
            }, accessibleContext);
            if (str != null) {
                this.references.increment(str);
                return str;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleWordAtIndexFromContext; ac = null");
        return null;
    }

    private String getAccessibleSentenceAtIndexFromContext(final AccessibleContext accessibleContext, final int i2) {
        if (accessibleContext != null) {
            String str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.80
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    AccessibleText accessibleText = accessibleContext.getAccessibleText();
                    if (accessibleText == null) {
                        return null;
                    }
                    return accessibleText.getAtIndex(3, i2);
                }
            }, accessibleContext);
            if (str != null) {
                this.references.increment(str);
                return str;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleSentenceAtIndexFromContext; ac = null");
        return null;
    }

    private int getAccessibleTextSelectionStartFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.81
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (accessibleText != null) {
                    return Integer.valueOf(accessibleText.getSelectionStart());
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleTextSelectionEndFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.82
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (accessibleText != null) {
                    return Integer.valueOf(accessibleText.getSelectionEnd());
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private String getAccessibleTextSelectedTextFromContext(final AccessibleContext accessibleContext) {
        if (accessibleContext != null) {
            String str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.83
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    AccessibleText accessibleText = accessibleContext.getAccessibleText();
                    if (accessibleText == null) {
                        return null;
                    }
                    return accessibleText.getSelectedText();
                }
            }, accessibleContext);
            if (str != null) {
                this.references.increment(str);
                return str;
            }
            return null;
        }
        debugString("[ERROR]: getAccessibleTextSelectedTextFromContext; ac = null");
        return null;
    }

    private String getAccessibleAttributesAtIndexFromContext(final AccessibleContext accessibleContext, final int i2) {
        String strExpandStyleConstants;
        if (accessibleContext != null && (strExpandStyleConstants = expandStyleConstants((AttributeSet) InvocationUtils.invokeAndWait(new Callable<AttributeSet>() { // from class: com.sun.java.accessibility.AccessBridge.84
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AttributeSet call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (accessibleText != null) {
                    return accessibleText.getCharacterAttribute(i2);
                }
                return null;
            }
        }, accessibleContext))) != null) {
            this.references.increment(strExpandStyleConstants);
            return strExpandStyleConstants;
        }
        return null;
    }

    private int getAccessibleTextLineLeftBoundsFromContext(final AccessibleContext accessibleContext, final int i2) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.85
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (accessibleText != null) {
                    Rectangle characterBounds = accessibleText.getCharacterBounds(i2);
                    accessibleText.getCharCount();
                    if (characterBounds == null) {
                        return -1;
                    }
                    int i3 = 1;
                    int i4 = i2 - 1 < 0 ? 0 : i2 - 1;
                    Rectangle characterBounds2 = accessibleText.getCharacterBounds(i4);
                    while (true) {
                        Rectangle rectangle = characterBounds2;
                        if (rectangle == null || rectangle.f12373y < characterBounds.f12373y || i4 <= 0) {
                            break;
                        }
                        i3 <<= 1;
                        i4 = i2 - i3 < 0 ? 0 : i2 - i3;
                        characterBounds2 = accessibleText.getCharacterBounds(i4);
                    }
                    if (i4 != 0) {
                        while (true) {
                            i3 >>= 1;
                            if (i3 <= 0) {
                                break;
                            }
                            if (accessibleText.getCharacterBounds(i4 + i3).f12373y < characterBounds.f12373y) {
                                i4 += i3;
                            }
                        }
                        i4++;
                    }
                    return Integer.valueOf(i4);
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleTextLineRightBoundsFromContext(final AccessibleContext accessibleContext, final int i2) {
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.86
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (accessibleText != null) {
                    Rectangle characterBounds = accessibleText.getCharacterBounds(i2);
                    int charCount = accessibleText.getCharCount();
                    if (characterBounds == null) {
                        return -1;
                    }
                    int i3 = 1;
                    int i4 = i2 + 1 > charCount - 1 ? charCount - 1 : i2 + 1;
                    Rectangle characterBounds2 = accessibleText.getCharacterBounds(i4);
                    while (true) {
                        Rectangle rectangle = characterBounds2;
                        if (rectangle == null || rectangle.f12373y > characterBounds.f12373y || i4 >= charCount - 1) {
                            break;
                        }
                        i3 <<= 1;
                        i4 = i2 + i3 > charCount - 1 ? charCount - 1 : i2 + i3;
                        characterBounds2 = accessibleText.getCharacterBounds(i4);
                    }
                    if (i4 != charCount - 1) {
                        while (true) {
                            i3 >>= 1;
                            if (i3 <= 0) {
                                break;
                            }
                            if (accessibleText.getCharacterBounds(i4 - i3).f12373y > characterBounds.f12373y) {
                                i4 -= i3;
                            }
                        }
                        i4--;
                    }
                    return Integer.valueOf(i4);
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private String getAccessibleTextRangeFromContext(final AccessibleContext accessibleContext, final int i2, final int i3) {
        String str = (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.87
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                AccessibleText accessibleText;
                if (accessibleContext == null || (accessibleText = accessibleContext.getAccessibleText()) == null || i2 > i3 || i3 >= accessibleText.getCharCount()) {
                    return null;
                }
                StringBuffer stringBuffer = new StringBuffer((i3 - i2) + 1);
                for (int i4 = i2; i4 <= i3; i4++) {
                    stringBuffer.append(accessibleText.getAtIndex(1, i4));
                }
                return stringBuffer.toString();
            }
        }, accessibleContext);
        if (str != null) {
            this.references.increment(str);
            return str;
        }
        return null;
    }

    private AttributeSet getAccessibleAttributeSetAtIndexFromContext(final AccessibleContext accessibleContext, final int i2) {
        return (AttributeSet) InvocationUtils.invokeAndWait(new Callable<AttributeSet>() { // from class: com.sun.java.accessibility.AccessBridge.88
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AttributeSet call() throws Exception {
                AccessibleText accessibleText;
                AttributeSet characterAttribute;
                if (accessibleContext != null && (accessibleText = accessibleContext.getAccessibleText()) != null && (characterAttribute = accessibleText.getCharacterAttribute(i2)) != null) {
                    AccessBridge.this.references.increment(characterAttribute);
                    return characterAttribute;
                }
                return null;
            }
        }, accessibleContext);
    }

    private Rectangle getAccessibleTextRectAtIndexFromContext(final AccessibleContext accessibleContext, final int i2) {
        Rectangle rectangle = (Rectangle) InvocationUtils.invokeAndWait(new Callable<Rectangle>() { // from class: com.sun.java.accessibility.AccessBridge.89
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Rectangle call() throws Exception {
                AccessibleText accessibleText;
                Rectangle characterBounds;
                if (accessibleContext != null && (accessibleText = accessibleContext.getAccessibleText()) != null && (characterBounds = accessibleText.getCharacterBounds(i2)) != null) {
                    String atIndex = accessibleText.getAtIndex(1, i2);
                    if (atIndex != null && atIndex.equals("\n")) {
                        characterBounds.width = 0;
                    }
                    return characterBounds;
                }
                return null;
            }
        }, accessibleContext);
        Rectangle accessibleBoundsOnScreenFromContext = getAccessibleBoundsOnScreenFromContext(accessibleContext);
        if (rectangle != null && accessibleBoundsOnScreenFromContext != null) {
            rectangle.translate(accessibleBoundsOnScreenFromContext.f12372x, accessibleBoundsOnScreenFromContext.f12373y);
            return rectangle;
        }
        return null;
    }

    private int getAccessibleXcoordTextRectAtIndexFromContext(AccessibleContext accessibleContext, int i2) {
        if (accessibleContext != null) {
            Rectangle accessibleTextRectAtIndexFromContext = getAccessibleTextRectAtIndexFromContext(accessibleContext, i2);
            if (accessibleTextRectAtIndexFromContext != null) {
                return accessibleTextRectAtIndexFromContext.f12372x;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleXcoordTextRectAtIndexFromContext; ac = null");
        return -1;
    }

    private int getAccessibleYcoordTextRectAtIndexFromContext(AccessibleContext accessibleContext, int i2) {
        if (accessibleContext != null) {
            Rectangle accessibleTextRectAtIndexFromContext = getAccessibleTextRectAtIndexFromContext(accessibleContext, i2);
            if (accessibleTextRectAtIndexFromContext != null) {
                return accessibleTextRectAtIndexFromContext.f12373y;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleYcoordTextRectAtIndexFromContext; ac = null");
        return -1;
    }

    private int getAccessibleHeightTextRectAtIndexFromContext(AccessibleContext accessibleContext, int i2) {
        if (accessibleContext != null) {
            Rectangle accessibleTextRectAtIndexFromContext = getAccessibleTextRectAtIndexFromContext(accessibleContext, i2);
            if (accessibleTextRectAtIndexFromContext != null) {
                return accessibleTextRectAtIndexFromContext.height;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleHeightTextRectAtIndexFromContext; ac = null");
        return -1;
    }

    private int getAccessibleWidthTextRectAtIndexFromContext(AccessibleContext accessibleContext, int i2) {
        if (accessibleContext != null) {
            Rectangle accessibleTextRectAtIndexFromContext = getAccessibleTextRectAtIndexFromContext(accessibleContext, i2);
            if (accessibleTextRectAtIndexFromContext != null) {
                return accessibleTextRectAtIndexFromContext.width;
            }
            return -1;
        }
        debugString("[ERROR]: getAccessibleWidthTextRectAtIndexFromContext; ac = null");
        return -1;
    }

    private boolean getBoldFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.isBold(attributeSet);
        }
        debugString("[ERROR]: getBoldFromAttributeSet; as = null");
        return false;
    }

    private boolean getItalicFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.isItalic(attributeSet);
        }
        debugString("[ERROR]: getItalicFromAttributeSet; as = null");
        return false;
    }

    private boolean getUnderlineFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.isUnderline(attributeSet);
        }
        debugString("[ERROR]: getUnderlineFromAttributeSet; as = null");
        return false;
    }

    private boolean getStrikethroughFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.isStrikeThrough(attributeSet);
        }
        debugString("[ERROR]: getStrikethroughFromAttributeSet; as = null");
        return false;
    }

    private boolean getSuperscriptFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.isSuperscript(attributeSet);
        }
        debugString("[ERROR]: getSuperscriptFromAttributeSet; as = null");
        return false;
    }

    private boolean getSubscriptFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.isSubscript(attributeSet);
        }
        debugString("[ERROR]: getSubscriptFromAttributeSet; as = null");
        return false;
    }

    private String getBackgroundColorFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            String string = StyleConstants.getBackground(attributeSet).toString();
            if (string != null) {
                this.references.increment(string);
                return string;
            }
            return null;
        }
        debugString("[ERROR]: getBackgroundColorFromAttributeSet; as = null");
        return null;
    }

    private String getForegroundColorFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            String string = StyleConstants.getForeground(attributeSet).toString();
            if (string != null) {
                this.references.increment(string);
                return string;
            }
            return null;
        }
        debugString("[ERROR]: getForegroundColorFromAttributeSet; as = null");
        return null;
    }

    private String getFontFamilyFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            String string = StyleConstants.getFontFamily(attributeSet).toString();
            if (string != null) {
                this.references.increment(string);
                return string;
            }
            return null;
        }
        debugString("[ERROR]: getFontFamilyFromAttributeSet; as = null");
        return null;
    }

    private int getFontSizeFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getFontSize(attributeSet);
        }
        debugString("[ERROR]: getFontSizeFromAttributeSet; as = null");
        return -1;
    }

    private int getAlignmentFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getAlignment(attributeSet);
        }
        debugString("[ERROR]: getAlignmentFromAttributeSet; as = null");
        return -1;
    }

    private int getBidiLevelFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getBidiLevel(attributeSet);
        }
        debugString("[ERROR]: getBidiLevelFromAttributeSet; as = null");
        return -1;
    }

    private float getFirstLineIndentFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getFirstLineIndent(attributeSet);
        }
        debugString("[ERROR]: getFirstLineIndentFromAttributeSet; as = null");
        return -1.0f;
    }

    private float getLeftIndentFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getLeftIndent(attributeSet);
        }
        debugString("[ERROR]: getLeftIndentFromAttributeSet; as = null");
        return -1.0f;
    }

    private float getRightIndentFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getRightIndent(attributeSet);
        }
        debugString("[ERROR]: getRightIndentFromAttributeSet; as = null");
        return -1.0f;
    }

    private float getLineSpacingFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getLineSpacing(attributeSet);
        }
        debugString("[ERROR]: getLineSpacingFromAttributeSet; as = null");
        return -1.0f;
    }

    private float getSpaceAboveFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getSpaceAbove(attributeSet);
        }
        debugString("[ERROR]: getSpaceAboveFromAttributeSet; as = null");
        return -1.0f;
    }

    private float getSpaceBelowFromAttributeSet(AttributeSet attributeSet) {
        if (attributeSet != null) {
            return StyleConstants.getSpaceBelow(attributeSet);
        }
        debugString("[ERROR]: getSpaceBelowFromAttributeSet; as = null");
        return -1.0f;
    }

    private String expandStyleConstants(AttributeSet attributeSet) {
        final AccessibleContext accessibleContext;
        String str = "BidiLevel = " + StyleConstants.getBidiLevel(attributeSet);
        final Component component = StyleConstants.getComponent(attributeSet);
        if (component != null) {
            if ((component instanceof Accessible) && (accessibleContext = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.90
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleContext call() throws Exception {
                    return component.getAccessibleContext();
                }
            }, component)) != null) {
                str = str + "; Accessible Component = " + ((String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.91
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public String call() throws Exception {
                        return accessibleContext.getAccessibleName();
                    }
                }, accessibleContext));
            } else {
                str = str + "; Innaccessible Component = " + ((Object) component);
            }
        }
        Icon icon = StyleConstants.getIcon(attributeSet);
        if (icon != null) {
            if (icon instanceof ImageIcon) {
                str = str + "; ImageIcon = " + ((ImageIcon) icon).getDescription();
            } else {
                str = str + "; Icon = " + ((Object) icon);
            }
        }
        String str2 = (str + "; FontFamily = " + StyleConstants.getFontFamily(attributeSet)) + "; FontSize = " + StyleConstants.getFontSize(attributeSet);
        if (StyleConstants.isBold(attributeSet)) {
            str2 = str2 + "; bold";
        }
        if (StyleConstants.isItalic(attributeSet)) {
            str2 = str2 + "; italic";
        }
        if (StyleConstants.isUnderline(attributeSet)) {
            str2 = str2 + "; underline";
        }
        if (StyleConstants.isStrikeThrough(attributeSet)) {
            str2 = str2 + "; strikethrough";
        }
        if (StyleConstants.isSuperscript(attributeSet)) {
            str2 = str2 + "; superscript";
        }
        if (StyleConstants.isSubscript(attributeSet)) {
            str2 = str2 + "; subscript";
        }
        Color foreground = StyleConstants.getForeground(attributeSet);
        if (foreground != null) {
            str2 = str2 + "; Foreground = " + ((Object) foreground);
        }
        Color background = StyleConstants.getBackground(attributeSet);
        if (background != null) {
            str2 = str2 + "; Background = " + ((Object) background);
        }
        String str3 = ((((((str2 + "; FirstLineIndent = " + StyleConstants.getFirstLineIndent(attributeSet)) + "; RightIndent = " + StyleConstants.getRightIndent(attributeSet)) + "; LeftIndent = " + StyleConstants.getLeftIndent(attributeSet)) + "; LineSpacing = " + StyleConstants.getLineSpacing(attributeSet)) + "; SpaceAbove = " + StyleConstants.getSpaceAbove(attributeSet)) + "; SpaceBelow = " + StyleConstants.getSpaceBelow(attributeSet)) + "; Alignment = " + StyleConstants.getAlignment(attributeSet);
        TabSet tabSet = StyleConstants.getTabSet(attributeSet);
        if (tabSet != null) {
            str3 = str3 + "; TabSet = " + ((Object) tabSet);
        }
        return str3;
    }

    private String getCurrentAccessibleValueFromContext(final AccessibleContext accessibleContext) {
        String string;
        if (accessibleContext != null) {
            Number number = (Number) InvocationUtils.invokeAndWait(new Callable<Number>() { // from class: com.sun.java.accessibility.AccessBridge.92
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Number call() throws Exception {
                    AccessibleValue accessibleValue = accessibleContext.getAccessibleValue();
                    if (accessibleValue == null) {
                        return null;
                    }
                    return accessibleValue.getCurrentAccessibleValue();
                }
            }, accessibleContext);
            if (number != null && (string = number.toString()) != null) {
                this.references.increment(string);
                return string;
            }
            return null;
        }
        debugString("[ERROR]: getCurrentAccessibleValueFromContext; ac = null");
        return null;
    }

    private String getMaximumAccessibleValueFromContext(final AccessibleContext accessibleContext) {
        String string;
        if (accessibleContext != null) {
            Number number = (Number) InvocationUtils.invokeAndWait(new Callable<Number>() { // from class: com.sun.java.accessibility.AccessBridge.93
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Number call() throws Exception {
                    AccessibleValue accessibleValue = accessibleContext.getAccessibleValue();
                    if (accessibleValue == null) {
                        return null;
                    }
                    return accessibleValue.getMaximumAccessibleValue();
                }
            }, accessibleContext);
            if (number != null && (string = number.toString()) != null) {
                this.references.increment(string);
                return string;
            }
            return null;
        }
        debugString("[ERROR]: getMaximumAccessibleValueFromContext; ac = null");
        return null;
    }

    private String getMinimumAccessibleValueFromContext(final AccessibleContext accessibleContext) {
        String string;
        if (accessibleContext != null) {
            Number number = (Number) InvocationUtils.invokeAndWait(new Callable<Number>() { // from class: com.sun.java.accessibility.AccessBridge.94
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Number call() throws Exception {
                    AccessibleValue accessibleValue = accessibleContext.getAccessibleValue();
                    if (accessibleValue == null) {
                        return null;
                    }
                    return accessibleValue.getMinimumAccessibleValue();
                }
            }, accessibleContext);
            if (number != null && (string = number.toString()) != null) {
                this.references.increment(string);
                return string;
            }
            return null;
        }
        debugString("[ERROR]: getMinimumAccessibleValueFromContext; ac = null");
        return null;
    }

    private void addAccessibleSelectionFromContext(final AccessibleContext accessibleContext, final int i2) {
        try {
            InvocationUtils.invokeAndWait(new Callable<Object>() { // from class: com.sun.java.accessibility.AccessBridge.95
                @Override // java.util.concurrent.Callable
                public Object call() throws Exception {
                    AccessibleSelection accessibleSelection;
                    if (accessibleContext != null && (accessibleSelection = accessibleContext.getAccessibleSelection()) != null) {
                        accessibleSelection.addAccessibleSelection(i2);
                        return null;
                    }
                    return null;
                }
            }, accessibleContext);
        } catch (Exception e2) {
        }
    }

    private void clearAccessibleSelectionFromContext(final AccessibleContext accessibleContext) {
        try {
            InvocationUtils.invokeAndWait(new Callable<Object>() { // from class: com.sun.java.accessibility.AccessBridge.96
                @Override // java.util.concurrent.Callable
                public Object call() throws Exception {
                    AccessibleSelection accessibleSelection = accessibleContext.getAccessibleSelection();
                    if (accessibleSelection != null) {
                        accessibleSelection.clearAccessibleSelection();
                        return null;
                    }
                    return null;
                }
            }, accessibleContext);
        } catch (Exception e2) {
        }
    }

    private AccessibleContext getAccessibleSelectionFromContext(final AccessibleContext accessibleContext, final int i2) {
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.97
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                AccessibleSelection accessibleSelection;
                Accessible accessibleSelection2;
                if (accessibleContext == null || (accessibleSelection = accessibleContext.getAccessibleSelection()) == null || (accessibleSelection2 = accessibleSelection.getAccessibleSelection(i2)) == null) {
                    return null;
                }
                return accessibleSelection2.getAccessibleContext();
            }
        }, accessibleContext);
    }

    private int getAccessibleSelectionCountFromContext(final AccessibleContext accessibleContext) {
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.98
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleSelection accessibleSelection;
                if (accessibleContext != null && (accessibleSelection = accessibleContext.getAccessibleSelection()) != null) {
                    return Integer.valueOf(accessibleSelection.getAccessibleSelectionCount());
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private boolean isAccessibleChildSelectedFromContext(final AccessibleContext accessibleContext, final int i2) {
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.99
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                AccessibleSelection accessibleSelection;
                if (accessibleContext != null && (accessibleSelection = accessibleContext.getAccessibleSelection()) != null) {
                    return Boolean.valueOf(accessibleSelection.isAccessibleChildSelected(i2));
                }
                return false;
            }
        }, accessibleContext)).booleanValue();
    }

    private void removeAccessibleSelectionFromContext(final AccessibleContext accessibleContext, final int i2) {
        InvocationUtils.invokeAndWait(new Callable<Object>() { // from class: com.sun.java.accessibility.AccessBridge.100
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                AccessibleSelection accessibleSelection;
                if (accessibleContext != null && (accessibleSelection = accessibleContext.getAccessibleSelection()) != null) {
                    accessibleSelection.removeAccessibleSelection(i2);
                    return null;
                }
                return null;
            }
        }, accessibleContext);
    }

    private void selectAllAccessibleSelectionFromContext(final AccessibleContext accessibleContext) {
        InvocationUtils.invokeAndWait(new Callable<Object>() { // from class: com.sun.java.accessibility.AccessBridge.101
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                AccessibleSelection accessibleSelection;
                if (accessibleContext != null && (accessibleSelection = accessibleContext.getAccessibleSelection()) != null) {
                    accessibleSelection.selectAllAccessibleSelection();
                    return null;
                }
                return null;
            }
        }, accessibleContext);
    }

    private AccessibleTable getAccessibleTableFromContext(final AccessibleContext accessibleContext) {
        String javaVersionProperty = getJavaVersionProperty();
        if (javaVersionProperty != null && javaVersionProperty.compareTo("1.3") >= 0) {
            return (AccessibleTable) InvocationUtils.invokeAndWait(new Callable<AccessibleTable>() { // from class: com.sun.java.accessibility.AccessBridge.102
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleTable call() throws Exception {
                    AccessibleTable accessibleTable;
                    if (accessibleContext != null && (accessibleTable = accessibleContext.getAccessibleTable()) != null) {
                        AccessBridge.this.hashtab.put(accessibleTable, accessibleContext);
                        return accessibleTable;
                    }
                    return null;
                }
            }, accessibleContext);
        }
        return null;
    }

    private AccessibleContext getContextFromAccessibleTable(AccessibleTable accessibleTable) {
        return this.hashtab.get(accessibleTable);
    }

    private int getAccessibleTableRowCount(final AccessibleContext accessibleContext) {
        debugString("[INFO]: ##### getAccessibleTableRowCount");
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.103
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleTable accessibleTable;
                if (accessibleContext != null && (accessibleTable = accessibleContext.getAccessibleTable()) != null) {
                    return Integer.valueOf(accessibleTable.getAccessibleRowCount());
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleTableColumnCount(final AccessibleContext accessibleContext) {
        debugString("[INFO]: ##### getAccessibleTableColumnCount");
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.104
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleTable accessibleTable;
                if (accessibleContext != null && (accessibleTable = accessibleContext.getAccessibleTable()) != null) {
                    return Integer.valueOf(accessibleTable.getAccessibleColumnCount());
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private AccessibleContext getAccessibleTableCellAccessibleContext(final AccessibleTable accessibleTable, final int i2, final int i3) {
        debugString("[INFO]: getAccessibleTableCellAccessibleContext: at = " + ((Object) accessibleTable.getClass()));
        if (accessibleTable == null) {
            return null;
        }
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.105
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                if (!(accessibleTable instanceof AccessibleContext)) {
                    Accessible accessibleAt = accessibleTable.getAccessibleAt(i2, i3);
                    if (accessibleAt != null) {
                        return accessibleAt.getAccessibleContext();
                    }
                    return null;
                }
                AccessibleContext accessibleContext = (AccessibleContext) accessibleTable;
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent != null) {
                    Accessible accessibleChild = accessibleParent.getAccessibleContext().getAccessibleChild(accessibleContext.getAccessibleIndexInParent());
                    if (accessibleChild instanceof JTable) {
                        JTable jTable = (JTable) accessibleChild;
                        TableCellRenderer cellRenderer = jTable.getCellRenderer(i2, i3);
                        if (cellRenderer == null) {
                            cellRenderer = jTable.getDefaultRenderer(jTable.getColumnClass(i3));
                        }
                        Component tableCellRendererComponent = cellRenderer.getTableCellRendererComponent(jTable, jTable.getValueAt(i2, i3), false, false, i2, i3);
                        if (tableCellRendererComponent instanceof Accessible) {
                            return tableCellRendererComponent.getAccessibleContext();
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            }
        }, getContextFromAccessibleTable(accessibleTable));
    }

    private int getAccessibleTableCellIndex(final AccessibleTable accessibleTable, int i2, int i3) {
        debugString("[INFO]: ##### getAccessibleTableCellIndex: at=" + ((Object) accessibleTable));
        if (accessibleTable != null) {
            int iIntValue = (i2 * ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.106
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    return Integer.valueOf(accessibleTable.getAccessibleColumnCount());
                }
            }, getContextFromAccessibleTable(accessibleTable))).intValue()) + i3;
            debugString("[INFO]:    ##### getAccessibleTableCellIndex=" + iIntValue);
            return iIntValue;
        }
        debugString("[ERROR]: ##### getAccessibleTableCellIndex FAILED");
        return -1;
    }

    private int getAccessibleTableCellRowExtent(final AccessibleTable accessibleTable, final int i2, final int i3) {
        debugString("[INFO]: ##### getAccessibleTableCellRowExtent");
        if (accessibleTable != null) {
            int iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.107
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    return Integer.valueOf(accessibleTable.getAccessibleRowExtentAt(i2, i3));
                }
            }, getContextFromAccessibleTable(accessibleTable))).intValue();
            debugString("[INFO]:   ##### getAccessibleTableCellRowExtent=" + iIntValue);
            return iIntValue;
        }
        debugString("[ERROR]: ##### getAccessibleTableCellRowExtent FAILED");
        return -1;
    }

    private int getAccessibleTableCellColumnExtent(final AccessibleTable accessibleTable, final int i2, final int i3) {
        debugString("[INFO]: ##### getAccessibleTableCellColumnExtent");
        if (accessibleTable != null) {
            int iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.108
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    return Integer.valueOf(accessibleTable.getAccessibleColumnExtentAt(i2, i3));
                }
            }, getContextFromAccessibleTable(accessibleTable))).intValue();
            debugString("[INFO]:   ##### getAccessibleTableCellColumnExtent=" + iIntValue);
            return iIntValue;
        }
        debugString("[ERROR]: ##### getAccessibleTableCellColumnExtent FAILED");
        return -1;
    }

    private boolean isAccessibleTableCellSelected(final AccessibleTable accessibleTable, final int i2, final int i3) {
        debugString("[INFO]: ##### isAccessibleTableCellSelected: [" + i2 + "][" + i3 + "]");
        if (accessibleTable == null) {
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.109
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                boolean zContains = false;
                Accessible accessibleAt = accessibleTable.getAccessibleAt(i2, i3);
                if (accessibleAt != null) {
                    AccessibleContext accessibleContext = accessibleAt.getAccessibleContext();
                    if (accessibleContext == null) {
                        return false;
                    }
                    AccessibleStateSet accessibleStateSet = accessibleContext.getAccessibleStateSet();
                    if (accessibleStateSet != null) {
                        zContains = accessibleStateSet.contains(AccessibleState.SELECTED);
                    }
                }
                return Boolean.valueOf(zContains);
            }
        }, getContextFromAccessibleTable(accessibleTable))).booleanValue();
    }

    private AccessibleTable getAccessibleTableRowHeader(final AccessibleContext accessibleContext) {
        debugString("[INFO]: #####  getAccessibleTableRowHeader called");
        AccessibleTable accessibleTable = (AccessibleTable) InvocationUtils.invokeAndWait(new Callable<AccessibleTable>() { // from class: com.sun.java.accessibility.AccessBridge.110
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleTable call() throws Exception {
                AccessibleTable accessibleTable2;
                if (accessibleContext != null && (accessibleTable2 = accessibleContext.getAccessibleTable()) != null) {
                    return accessibleTable2.getAccessibleRowHeader();
                }
                return null;
            }
        }, accessibleContext);
        if (accessibleTable != null) {
            this.hashtab.put(accessibleTable, accessibleContext);
        }
        return accessibleTable;
    }

    private AccessibleTable getAccessibleTableColumnHeader(final AccessibleContext accessibleContext) {
        debugString("[INFO]: ##### getAccessibleTableColumnHeader");
        if (accessibleContext == null) {
            return null;
        }
        AccessibleTable accessibleTable = (AccessibleTable) InvocationUtils.invokeAndWait(new Callable<AccessibleTable>() { // from class: com.sun.java.accessibility.AccessBridge.111
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleTable call() throws Exception {
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent != null) {
                    Accessible accessibleChild = accessibleParent.getAccessibleContext().getAccessibleChild(accessibleContext.getAccessibleIndexInParent());
                    if ((accessibleChild instanceof JTable) && ((JTable) accessibleChild).getTableHeader() == null) {
                        return null;
                    }
                }
                AccessibleTable accessibleTable2 = accessibleContext.getAccessibleTable();
                if (accessibleTable2 != null) {
                    return accessibleTable2.getAccessibleColumnHeader();
                }
                return null;
            }
        }, accessibleContext);
        if (accessibleTable != null) {
            this.hashtab.put(accessibleTable, accessibleContext);
        }
        return accessibleTable;
    }

    private int getAccessibleTableRowHeaderRowCount(AccessibleContext accessibleContext) {
        final AccessibleTable accessibleTableRowHeader;
        debugString("[INFO]: #####  getAccessibleTableRowHeaderRowCount called");
        if (accessibleContext != null && (accessibleTableRowHeader = getAccessibleTableRowHeader(accessibleContext)) != null) {
            return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.112
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    if (accessibleTableRowHeader != null) {
                        return Integer.valueOf(accessibleTableRowHeader.getAccessibleRowCount());
                    }
                    return -1;
                }
            }, accessibleContext)).intValue();
        }
        return -1;
    }

    private int getAccessibleTableRowHeaderColumnCount(AccessibleContext accessibleContext) {
        final AccessibleTable accessibleTableRowHeader;
        debugString("[INFO]: #####  getAccessibleTableRowHeaderColumnCount called");
        if (accessibleContext != null && (accessibleTableRowHeader = getAccessibleTableRowHeader(accessibleContext)) != null) {
            return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.113
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    if (accessibleTableRowHeader != null) {
                        return Integer.valueOf(accessibleTableRowHeader.getAccessibleColumnCount());
                    }
                    return -1;
                }
            }, accessibleContext)).intValue();
        }
        debugString("[ERROR]: ##### getAccessibleTableRowHeaderColumnCount FAILED");
        return -1;
    }

    private int getAccessibleTableColumnHeaderRowCount(AccessibleContext accessibleContext) {
        final AccessibleTable accessibleTableColumnHeader;
        debugString("[INFO]: ##### getAccessibleTableColumnHeaderRowCount");
        if (accessibleContext != null && (accessibleTableColumnHeader = getAccessibleTableColumnHeader(accessibleContext)) != null) {
            return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.114
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    if (accessibleTableColumnHeader != null) {
                        return Integer.valueOf(accessibleTableColumnHeader.getAccessibleRowCount());
                    }
                    return -1;
                }
            }, accessibleContext)).intValue();
        }
        debugString("[ERROR]: ##### getAccessibleTableColumnHeaderRowCount FAILED");
        return -1;
    }

    private int getAccessibleTableColumnHeaderColumnCount(AccessibleContext accessibleContext) {
        final AccessibleTable accessibleTableColumnHeader;
        debugString("[ERROR]: #####  getAccessibleTableColumnHeaderColumnCount");
        if (accessibleContext != null && (accessibleTableColumnHeader = getAccessibleTableColumnHeader(accessibleContext)) != null) {
            return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.115
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    if (accessibleTableColumnHeader != null) {
                        return Integer.valueOf(accessibleTableColumnHeader.getAccessibleColumnCount());
                    }
                    return -1;
                }
            }, accessibleContext)).intValue();
        }
        debugString("[ERROR]: ##### getAccessibleTableColumnHeaderColumnCount FAILED");
        return -1;
    }

    private AccessibleContext getAccessibleTableRowDescription(final AccessibleTable accessibleTable, final int i2) {
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.116
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                Accessible accessibleRowDescription;
                if (accessibleTable != null && (accessibleRowDescription = accessibleTable.getAccessibleRowDescription(i2)) != null) {
                    return accessibleRowDescription.getAccessibleContext();
                }
                return null;
            }
        }, getContextFromAccessibleTable(accessibleTable));
    }

    private AccessibleContext getAccessibleTableColumnDescription(final AccessibleTable accessibleTable, final int i2) {
        if (accessibleTable == null) {
            return null;
        }
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.117
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                Accessible accessibleColumnDescription = accessibleTable.getAccessibleColumnDescription(i2);
                if (accessibleColumnDescription != null) {
                    return accessibleColumnDescription.getAccessibleContext();
                }
                return null;
            }
        }, getContextFromAccessibleTable(accessibleTable));
    }

    private int getAccessibleTableRowSelectionCount(final AccessibleTable accessibleTable) {
        if (accessibleTable != null) {
            return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.118
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    int[] selectedAccessibleRows = accessibleTable.getSelectedAccessibleRows();
                    if (selectedAccessibleRows != null) {
                        return Integer.valueOf(selectedAccessibleRows.length);
                    }
                    return -1;
                }
            }, getContextFromAccessibleTable(accessibleTable))).intValue();
        }
        return -1;
    }

    private int getAccessibleTableRowSelections(final AccessibleTable accessibleTable, final int i2) {
        if (accessibleTable != null) {
            return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.119
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Integer call() throws Exception {
                    int[] selectedAccessibleRows = accessibleTable.getSelectedAccessibleRows();
                    if (selectedAccessibleRows.length > i2) {
                        return Integer.valueOf(selectedAccessibleRows[i2]);
                    }
                    return -1;
                }
            }, getContextFromAccessibleTable(accessibleTable))).intValue();
        }
        return -1;
    }

    private boolean isAccessibleTableRowSelected(final AccessibleTable accessibleTable, final int i2) {
        if (accessibleTable == null) {
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.120
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                return Boolean.valueOf(accessibleTable.isAccessibleRowSelected(i2));
            }
        }, getContextFromAccessibleTable(accessibleTable))).booleanValue();
    }

    private boolean isAccessibleTableColumnSelected(final AccessibleTable accessibleTable, final int i2) {
        if (accessibleTable == null) {
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.121
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                return Boolean.valueOf(accessibleTable.isAccessibleColumnSelected(i2));
            }
        }, getContextFromAccessibleTable(accessibleTable))).booleanValue();
    }

    private int getAccessibleTableColumnSelectionCount(final AccessibleTable accessibleTable) {
        if (accessibleTable == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.122
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                int[] selectedAccessibleColumns = accessibleTable.getSelectedAccessibleColumns();
                if (selectedAccessibleColumns != null) {
                    return Integer.valueOf(selectedAccessibleColumns.length);
                }
                return -1;
            }
        }, getContextFromAccessibleTable(accessibleTable))).intValue();
    }

    private int getAccessibleTableColumnSelections(final AccessibleTable accessibleTable, final int i2) {
        if (accessibleTable == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.123
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                int[] selectedAccessibleColumns = accessibleTable.getSelectedAccessibleColumns();
                if (selectedAccessibleColumns != null && selectedAccessibleColumns.length > i2) {
                    return Integer.valueOf(selectedAccessibleColumns[i2]);
                }
                return -1;
            }
        }, getContextFromAccessibleTable(accessibleTable))).intValue();
    }

    private int getAccessibleTableRow(final AccessibleTable accessibleTable, int i2) {
        if (accessibleTable == null) {
            return -1;
        }
        return i2 / ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.124
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleTable.getAccessibleColumnCount());
            }
        }, getContextFromAccessibleTable(accessibleTable))).intValue();
    }

    private int getAccessibleTableColumn(final AccessibleTable accessibleTable, int i2) {
        if (accessibleTable == null) {
            return -1;
        }
        return i2 % ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.125
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleTable.getAccessibleColumnCount());
            }
        }, getContextFromAccessibleTable(accessibleTable))).intValue();
    }

    private int getAccessibleTableIndex(final AccessibleTable accessibleTable, int i2, int i3) {
        if (accessibleTable == null) {
            return -1;
        }
        return (i2 * ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.126
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleTable.getAccessibleColumnCount());
            }
        }, getContextFromAccessibleTable(accessibleTable))).intValue()) + i3;
    }

    private int getAccessibleRelationCount(final AccessibleContext accessibleContext) {
        AccessibleRelationSet accessibleRelationSet;
        String javaVersionProperty = getJavaVersionProperty();
        if (javaVersionProperty != null && javaVersionProperty.compareTo("1.3") >= 0 && accessibleContext != null && (accessibleRelationSet = (AccessibleRelationSet) InvocationUtils.invokeAndWait(new Callable<AccessibleRelationSet>() { // from class: com.sun.java.accessibility.AccessBridge.127
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleRelationSet call() throws Exception {
                return accessibleContext.getAccessibleRelationSet();
            }
        }, accessibleContext)) != null) {
            return accessibleRelationSet.size();
        }
        return 0;
    }

    private String getAccessibleRelationKey(final AccessibleContext accessibleContext, final int i2) {
        return (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.128
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                AccessibleRelationSet accessibleRelationSet;
                AccessibleRelation[] array;
                if (accessibleContext != null && (accessibleRelationSet = accessibleContext.getAccessibleRelationSet()) != null && (array = accessibleRelationSet.toArray()) != null && i2 >= 0 && i2 < array.length) {
                    return array[i2].getKey();
                }
                return null;
            }
        }, accessibleContext);
    }

    private int getAccessibleRelationTargetCount(final AccessibleContext accessibleContext, final int i2) {
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.129
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleRelationSet accessibleRelationSet;
                AccessibleRelation[] array;
                if (accessibleContext != null && (accessibleRelationSet = accessibleContext.getAccessibleRelationSet()) != null && (array = accessibleRelationSet.toArray()) != null && i2 >= 0 && i2 < array.length) {
                    return Integer.valueOf(array[i2].getTarget().length);
                }
                return -1;
            }
        }, accessibleContext)).intValue();
    }

    private AccessibleContext getAccessibleRelationTarget(final AccessibleContext accessibleContext, final int i2, final int i3) {
        debugString("[INFO]: ***** getAccessibleRelationTarget");
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.130
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                AccessibleRelationSet accessibleRelationSet;
                AccessibleRelation[] array;
                Object[] target;
                if (accessibleContext == null || (accessibleRelationSet = accessibleContext.getAccessibleRelationSet()) == null || (array = accessibleRelationSet.toArray()) == null || i2 < 0 || i2 >= array.length || (target = array[i2].getTarget()) == null) {
                    return null;
                }
                if ((i3 >= 0) & (i3 < target.length)) {
                    Object obj = target[i3];
                    if (obj instanceof Accessible) {
                        return ((Accessible) obj).getAccessibleContext();
                    }
                    return null;
                }
                return null;
            }
        }, accessibleContext);
    }

    private AccessibleHypertext getAccessibleHypertext(final AccessibleContext accessibleContext) {
        debugString("[INFO]: getAccessibleHyperlink");
        if (accessibleContext == null) {
            return null;
        }
        AccessibleHypertext accessibleHypertext = (AccessibleHypertext) InvocationUtils.invokeAndWait(new Callable<AccessibleHypertext>() { // from class: com.sun.java.accessibility.AccessBridge.131
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleHypertext call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (!(accessibleText instanceof AccessibleHypertext)) {
                    return null;
                }
                return (AccessibleHypertext) accessibleText;
            }
        }, accessibleContext);
        this.hyperTextContextMap.put(accessibleHypertext, accessibleContext);
        return accessibleHypertext;
    }

    private int getAccessibleHyperlinkCount(AccessibleContext accessibleContext) {
        final AccessibleHypertext accessibleHypertext;
        debugString("[INFO]: getAccessibleHyperlinkCount");
        if (accessibleContext == null || (accessibleHypertext = getAccessibleHypertext(accessibleContext)) == null) {
            return 0;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.132
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleHypertext.getLinkCount());
            }
        }, accessibleContext)).intValue();
    }

    private AccessibleHyperlink getAccessibleHyperlink(final AccessibleHypertext accessibleHypertext, final int i2) {
        debugString("[INFO]: getAccessibleHyperlink");
        if (accessibleHypertext == null) {
            return null;
        }
        AccessibleContext accessibleContext = this.hyperTextContextMap.get(accessibleHypertext);
        if (i2 < 0 || i2 >= ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.133
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleHypertext.getLinkCount());
            }
        }, accessibleContext)).intValue()) {
            return null;
        }
        AccessibleHyperlink accessibleHyperlink = (AccessibleHyperlink) InvocationUtils.invokeAndWait(new Callable<AccessibleHyperlink>() { // from class: com.sun.java.accessibility.AccessBridge.134
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleHyperlink call() throws Exception {
                AccessibleHyperlink link = accessibleHypertext.getLink(i2);
                if (link == null || !link.isValid()) {
                    return null;
                }
                return link;
            }
        }, accessibleContext);
        this.hyperLinkContextMap.put(accessibleHyperlink, accessibleContext);
        return accessibleHyperlink;
    }

    private String getAccessibleHyperlinkText(final AccessibleHyperlink accessibleHyperlink) {
        debugString("[INFO]: getAccessibleHyperlinkText");
        if (accessibleHyperlink == null) {
            return null;
        }
        return (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.135
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                String accessibleActionDescription = accessibleHyperlink.getAccessibleActionDescription(0);
                if (accessibleActionDescription != null) {
                    return accessibleActionDescription.toString();
                }
                return null;
            }
        }, this.hyperLinkContextMap.get(accessibleHyperlink));
    }

    private String getAccessibleHyperlinkURL(final AccessibleHyperlink accessibleHyperlink) {
        debugString("[INFO]: getAccessibleHyperlinkURL");
        if (accessibleHyperlink == null) {
            return null;
        }
        return (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.136
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                Object accessibleActionObject = accessibleHyperlink.getAccessibleActionObject(0);
                if (accessibleActionObject != null) {
                    return accessibleActionObject.toString();
                }
                return null;
            }
        }, this.hyperLinkContextMap.get(accessibleHyperlink));
    }

    private int getAccessibleHyperlinkStartIndex(final AccessibleHyperlink accessibleHyperlink) {
        debugString("[INFO]: getAccessibleHyperlinkStartIndex");
        if (accessibleHyperlink == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.137
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleHyperlink.getStartIndex());
            }
        }, this.hyperLinkContextMap.get(accessibleHyperlink))).intValue();
    }

    private int getAccessibleHyperlinkEndIndex(final AccessibleHyperlink accessibleHyperlink) {
        debugString("[INFO]: getAccessibleHyperlinkEndIndex");
        if (accessibleHyperlink == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.138
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleHyperlink.getEndIndex());
            }
        }, this.hyperLinkContextMap.get(accessibleHyperlink))).intValue();
    }

    private int getAccessibleHypertextLinkIndex(final AccessibleHypertext accessibleHypertext, final int i2) {
        debugString("[INFO]: getAccessibleHypertextLinkIndex: charIndex = " + i2);
        if (accessibleHypertext == null) {
            return -1;
        }
        int iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.139
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleHypertext.getLinkIndex(i2));
            }
        }, this.hyperTextContextMap.get(accessibleHypertext))).intValue();
        debugString("[INFO]: getAccessibleHypertextLinkIndex returning " + iIntValue);
        return iIntValue;
    }

    private boolean activateAccessibleHyperlink(AccessibleContext accessibleContext, final AccessibleHyperlink accessibleHyperlink) {
        if (accessibleHyperlink == null) {
            return false;
        }
        boolean zBooleanValue = ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.140
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                return Boolean.valueOf(accessibleHyperlink.doAccessibleAction(0));
            }
        }, accessibleContext)).booleanValue();
        debugString("[INFO]: activateAccessibleHyperlink: returning = " + zBooleanValue);
        return zBooleanValue;
    }

    private KeyStroke getMnemonic(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (KeyStroke) InvocationUtils.invokeAndWait(new Callable<KeyStroke>() { // from class: com.sun.java.accessibility.AccessBridge.141
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public KeyStroke call() throws Exception {
                AccessibleExtendedComponent accessibleExtendedComponent;
                AccessibleKeyBinding accessibleKeyBinding;
                AccessibleComponent accessibleComponent = accessibleContext.getAccessibleComponent();
                if ((accessibleComponent instanceof AccessibleExtendedComponent) && (accessibleExtendedComponent = (AccessibleExtendedComponent) accessibleComponent) != null && (accessibleKeyBinding = accessibleExtendedComponent.getAccessibleKeyBinding()) != null) {
                    Object accessibleKeyBinding2 = accessibleKeyBinding.getAccessibleKeyBinding(0);
                    if (accessibleKeyBinding2 instanceof KeyStroke) {
                        return (KeyStroke) accessibleKeyBinding2;
                    }
                    return null;
                }
                return null;
            }
        }, accessibleContext);
    }

    private KeyStroke getAccelerator(final AccessibleContext accessibleContext) {
        if (accessibleContext == null) {
            return null;
        }
        return (KeyStroke) InvocationUtils.invokeAndWait(new Callable<KeyStroke>() { // from class: com.sun.java.accessibility.AccessBridge.142
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public KeyStroke call() throws Exception {
                JMenuItem jMenuItem;
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent instanceof Accessible) {
                    Accessible accessibleChild = accessibleParent.getAccessibleContext().getAccessibleChild(accessibleContext.getAccessibleIndexInParent());
                    if (!(accessibleChild instanceof JMenuItem) || (jMenuItem = (JMenuItem) accessibleChild) == null) {
                        return null;
                    }
                    return jMenuItem.getAccelerator();
                }
                return null;
            }
        }, accessibleContext);
    }

    private int fKeyNumber(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return 0;
        }
        int i2 = 0;
        String keyText = KeyEvent.getKeyText(keyStroke.getKeyCode());
        if (keyText != null && ((keyText.length() == 2 || keyText.length() == 3) && keyText.substring(0, 1).equals(PdfOps.F_TOKEN))) {
            try {
                int i3 = Integer.parseInt(keyText.substring(1));
                if (i3 >= 1 && i3 <= 24) {
                    i2 = i3;
                }
            } catch (Exception e2) {
            }
        }
        return i2;
    }

    private int controlCode(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return 0;
        }
        int keyCode = keyStroke.getKeyCode();
        switch (keyCode) {
            case 8:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 127:
            case 155:
            case 224:
            case 225:
            case 226:
            case 227:
                break;
            default:
                keyCode = 0;
                break;
        }
        return keyCode;
    }

    private char getKeyChar(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return (char) 0;
        }
        int iFKeyNumber = fKeyNumber(keyStroke);
        if (iFKeyNumber != 0) {
            debugString("[INFO]:   Shortcut is: F" + iFKeyNumber);
            return (char) iFKeyNumber;
        }
        int iControlCode = controlCode(keyStroke);
        if (iControlCode != 0) {
            debugString("[INFO]:   Shortcut is control character: " + Integer.toHexString(iControlCode));
            return (char) iControlCode;
        }
        String keyText = KeyEvent.getKeyText(keyStroke.getKeyCode());
        debugString("[INFO]:   Shortcut is: " + keyText);
        if (keyText != null || keyText.length() > 0) {
            CharSequence charSequenceSubSequence = keyText.subSequence(0, 1);
            if (charSequenceSubSequence != null || charSequenceSubSequence.length() > 0) {
                return charSequenceSubSequence.charAt(0);
            }
            return (char) 0;
        }
        return (char) 0;
    }

    private int getModifiers(KeyStroke keyStroke) {
        if (keyStroke == null) {
            return 0;
        }
        debugString("[INFO]: In AccessBridge.getModifiers");
        int i2 = 0;
        if (fKeyNumber(keyStroke) != 0) {
            i2 = 0 | 256;
        }
        if (controlCode(keyStroke) != 0) {
            i2 |= 512;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(keyStroke.toString());
        while (stringTokenizer.hasMoreTokens()) {
            String strNextToken = stringTokenizer.nextToken();
            if (strNextToken.startsWith("met")) {
                debugString("[INFO]:   found meta");
                i2 |= 4;
            }
            if (strNextToken.startsWith("ctr")) {
                debugString("[INFO]:   found ctrl");
                i2 |= 2;
            }
            if (strNextToken.startsWith("alt")) {
                debugString("[INFO]:   found alt");
                i2 |= 8;
            }
            if (strNextToken.startsWith("shi")) {
                debugString("[INFO]:   found shift");
                i2 |= 1;
            }
        }
        debugString("[INFO]:   returning modifiers: 0x" + Integer.toHexString(i2));
        return i2;
    }

    private int getAccessibleKeyBindingsCount(AccessibleContext accessibleContext) {
        if (accessibleContext == null || !this.runningOnJDK1_4) {
            return 0;
        }
        int i2 = 0;
        if (getMnemonic(accessibleContext) != null) {
            i2 = 0 + 1;
        }
        if (getAccelerator(accessibleContext) != null) {
            i2++;
        }
        return i2;
    }

    private char getAccessibleKeyBindingChar(AccessibleContext accessibleContext, int i2) {
        KeyStroke accelerator;
        KeyStroke accelerator2;
        if (accessibleContext == null || !this.runningOnJDK1_4) {
            return (char) 0;
        }
        if (i2 == 0 && getMnemonic(accessibleContext) == null && (accelerator2 = getAccelerator(accessibleContext)) != null) {
            return getKeyChar(accelerator2);
        }
        if (i2 == 0) {
            KeyStroke mnemonic = getMnemonic(accessibleContext);
            if (mnemonic != null) {
                return getKeyChar(mnemonic);
            }
            return (char) 0;
        }
        if (i2 == 1 && (accelerator = getAccelerator(accessibleContext)) != null) {
            return getKeyChar(accelerator);
        }
        return (char) 0;
    }

    private int getAccessibleKeyBindingModifiers(AccessibleContext accessibleContext, int i2) {
        KeyStroke accelerator;
        KeyStroke accelerator2;
        if (accessibleContext == null || !this.runningOnJDK1_4) {
            return 0;
        }
        if (i2 == 0 && getMnemonic(accessibleContext) == null && (accelerator2 = getAccelerator(accessibleContext)) != null) {
            return getModifiers(accelerator2);
        }
        if (i2 == 0) {
            KeyStroke mnemonic = getMnemonic(accessibleContext);
            if (mnemonic != null) {
                return getModifiers(mnemonic);
            }
            return 0;
        }
        if (i2 == 1 && (accelerator = getAccelerator(accessibleContext)) != null) {
            return getModifiers(accelerator);
        }
        return 0;
    }

    private int getAccessibleIconsCount(final AccessibleContext accessibleContext) {
        debugString("[INFO]: getAccessibleIconsCount");
        if (accessibleContext == null) {
            return 0;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.143
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleIcon[] accessibleIcon = accessibleContext.getAccessibleIcon();
                if (accessibleIcon == null) {
                    return 0;
                }
                return Integer.valueOf(accessibleIcon.length);
            }
        }, accessibleContext)).intValue();
    }

    private String getAccessibleIconDescription(final AccessibleContext accessibleContext, final int i2) {
        debugString("[INFO]: getAccessibleIconDescription: index = " + i2);
        if (accessibleContext == null) {
            return null;
        }
        return (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.144
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                AccessibleIcon[] accessibleIcon = accessibleContext.getAccessibleIcon();
                if (accessibleIcon == null || i2 < 0 || i2 >= accessibleIcon.length) {
                    return null;
                }
                return accessibleIcon[i2].getAccessibleIconDescription();
            }
        }, accessibleContext);
    }

    private int getAccessibleIconHeight(final AccessibleContext accessibleContext, final int i2) {
        debugString("[INFO]: getAccessibleIconHeight: index = " + i2);
        if (accessibleContext == null) {
            return 0;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.145
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleIcon[] accessibleIcon = accessibleContext.getAccessibleIcon();
                if (accessibleIcon == null || i2 < 0 || i2 >= accessibleIcon.length) {
                    return 0;
                }
                return Integer.valueOf(accessibleIcon[i2].getAccessibleIconHeight());
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleIconWidth(final AccessibleContext accessibleContext, final int i2) {
        debugString("[INFO]: getAccessibleIconWidth: index = " + i2);
        if (accessibleContext == null) {
            return 0;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.146
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleIcon[] accessibleIcon = accessibleContext.getAccessibleIcon();
                if (accessibleIcon == null || i2 < 0 || i2 >= accessibleIcon.length) {
                    return 0;
                }
                return Integer.valueOf(accessibleIcon[i2].getAccessibleIconWidth());
            }
        }, accessibleContext)).intValue();
    }

    private int getAccessibleActionsCount(final AccessibleContext accessibleContext) {
        debugString("[INFO]: getAccessibleActionsCount");
        if (accessibleContext == null) {
            return 0;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.147
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                AccessibleAction accessibleAction = accessibleContext.getAccessibleAction();
                if (accessibleAction == null) {
                    return 0;
                }
                return Integer.valueOf(accessibleAction.getAccessibleActionCount());
            }
        }, accessibleContext)).intValue();
    }

    private String getAccessibleActionName(final AccessibleContext accessibleContext, final int i2) {
        debugString("[INFO]: getAccessibleActionName: index = " + i2);
        if (accessibleContext == null) {
            return null;
        }
        return (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.148
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                AccessibleAction accessibleAction = accessibleContext.getAccessibleAction();
                if (accessibleAction == null) {
                    return null;
                }
                return accessibleAction.getAccessibleActionDescription(i2);
            }
        }, accessibleContext);
    }

    private boolean doAccessibleActions(final AccessibleContext accessibleContext, final String str) {
        debugString("[INFO]: doAccessibleActions: action name = " + str);
        if (accessibleContext == null || str == null) {
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.149
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                AccessibleAction accessibleAction = accessibleContext.getAccessibleAction();
                if (accessibleAction == null) {
                    return false;
                }
                int i2 = -1;
                int accessibleActionCount = accessibleAction.getAccessibleActionCount();
                int i3 = 0;
                while (true) {
                    if (i3 >= accessibleActionCount) {
                        break;
                    }
                    if (!str.equals(accessibleAction.getAccessibleActionDescription(i3))) {
                        i3++;
                    } else {
                        i2 = i3;
                        break;
                    }
                }
                if (i2 == -1) {
                    return false;
                }
                return Boolean.valueOf(accessibleAction.doAccessibleAction(i2));
            }
        }, accessibleContext)).booleanValue();
    }

    private boolean setTextContents(final AccessibleContext accessibleContext, final String str) {
        debugString("[INFO]: setTextContents: ac = " + ((Object) accessibleContext) + "; text = " + str);
        if (!(accessibleContext instanceof AccessibleEditableText)) {
            debugString("[WARN]:   ac not instanceof AccessibleEditableText: " + ((Object) accessibleContext));
            return false;
        }
        if (str == null) {
            debugString("[WARN]:   text is null");
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.150
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                if (!accessibleContext.getAccessibleStateSet().contains(AccessibleState.ENABLED)) {
                    return false;
                }
                ((AccessibleEditableText) accessibleContext).setTextContents(str);
                return true;
            }
        }, accessibleContext)).booleanValue();
    }

    private AccessibleContext getInternalFrame(AccessibleContext accessibleContext) {
        return getParentWithRole(accessibleContext, AccessibleRole.INTERNAL_FRAME.toString());
    }

    private AccessibleContext getTopLevelObject(final AccessibleContext accessibleContext) {
        debugString("[INFO]: getTopLevelObject; ac = " + ((Object) accessibleContext));
        if (accessibleContext == null) {
            return null;
        }
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.151
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                if (accessibleContext.getAccessibleRole() == AccessibleRole.DIALOG) {
                    return accessibleContext;
                }
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent == null) {
                    return accessibleContext;
                }
                Accessible accessibleParent2 = accessibleParent;
                while (true) {
                    Accessible accessible = accessibleParent2;
                    if (accessible == null || accessible.getAccessibleContext() == null) {
                        break;
                    }
                    AccessibleContext accessibleContext2 = accessible.getAccessibleContext();
                    if (accessibleContext2 != null && accessibleContext2.getAccessibleRole() == AccessibleRole.DIALOG) {
                        return accessibleContext2;
                    }
                    accessibleParent = accessible;
                    accessibleParent2 = accessibleParent.getAccessibleContext().getAccessibleParent();
                }
                return accessibleParent.getAccessibleContext();
            }
        }, accessibleContext);
    }

    private AccessibleContext getParentWithRole(final AccessibleContext accessibleContext, final String str) {
        debugString("[INFO]: getParentWithRole; ac = " + ((Object) accessibleContext) + "\n role = " + str);
        if (accessibleContext == null || str == null) {
            return null;
        }
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.152
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                AccessibleContext accessibleContext2;
                AccessibleRole accessibleRole = (AccessibleRole) AccessBridge.this.accessibleRoleMap.get(str);
                if (accessibleRole == null) {
                    return accessibleContext;
                }
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent == null && accessibleContext.getAccessibleRole() == accessibleRole) {
                    return accessibleContext;
                }
                for (Accessible accessibleParent2 = accessibleParent; accessibleParent2 != null && (accessibleContext2 = accessibleParent2.getAccessibleContext()) != null; accessibleParent2 = accessibleParent2.getAccessibleContext().getAccessibleParent()) {
                    if (accessibleContext2.getAccessibleRole() == accessibleRole) {
                        return accessibleContext2;
                    }
                }
                return null;
            }
        }, accessibleContext);
    }

    private AccessibleContext getParentWithRoleElseRoot(AccessibleContext accessibleContext, String str) {
        AccessibleContext parentWithRole = getParentWithRole(accessibleContext, str);
        if (parentWithRole == null) {
            parentWithRole = getTopLevelObject(accessibleContext);
        }
        return parentWithRole;
    }

    private int getObjectDepth(final AccessibleContext accessibleContext) {
        debugString("[INFO]: getObjectDepth: ac = " + ((Object) accessibleContext));
        if (accessibleContext == null) {
            return -1;
        }
        return ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.153
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                int i2 = 0;
                Accessible accessibleParent = accessibleContext.getAccessibleParent();
                if (accessibleParent == null) {
                    return 0;
                }
                Accessible accessibleParent2 = accessibleParent;
                while (accessibleParent2 != null && accessibleParent2.getAccessibleContext() != null) {
                    accessibleParent2 = accessibleParent2.getAccessibleContext().getAccessibleParent();
                    i2++;
                }
                return Integer.valueOf(i2);
            }
        }, accessibleContext)).intValue();
    }

    private AccessibleContext getActiveDescendent(final AccessibleContext accessibleContext) {
        debugString("[INFO]: getActiveDescendent: ac = " + ((Object) accessibleContext));
        if (accessibleContext == null) {
            return null;
        }
        final Accessible accessible = (Accessible) InvocationUtils.invokeAndWait(new Callable<Accessible>() { // from class: com.sun.java.accessibility.AccessBridge.154
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Accessible call() throws Exception {
                return accessibleContext.getAccessibleParent();
            }
        }, accessibleContext);
        if (accessible != null) {
            Accessible accessible2 = (Accessible) InvocationUtils.invokeAndWait(new Callable<Accessible>() { // from class: com.sun.java.accessibility.AccessBridge.155
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Accessible call() throws Exception {
                    return accessible.getAccessibleContext().getAccessibleChild(accessibleContext.getAccessibleIndexInParent());
                }
            }, accessibleContext);
            if (accessible2 instanceof JTree) {
                final JTree jTree = (JTree) accessible2;
                return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.156
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public AccessibleContext call() throws Exception {
                        return AccessBridge.this.new AccessibleJTreeNode(jTree, jTree.getSelectionPath(), null);
                    }
                }, accessible2);
            }
        }
        return (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.157
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public AccessibleContext call() throws Exception {
                Accessible accessibleSelection;
                AccessibleSelection accessibleSelection2 = accessibleContext.getAccessibleSelection();
                if (accessibleSelection2 == null || accessibleSelection2.getAccessibleSelectionCount() != 1 || (accessibleSelection = accessibleSelection2.getAccessibleSelection(0)) == null) {
                    return null;
                }
                return accessibleSelection.getAccessibleContext();
            }
        }, accessibleContext);
    }

    private String getJAWSAccessibleName(final AccessibleContext accessibleContext) {
        debugString("[INFO]:  getJAWSAccessibleName");
        if (accessibleContext == null) {
            return null;
        }
        return (String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.158
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                return accessibleContext.getAccessibleName();
            }
        }, accessibleContext);
    }

    private boolean requestFocus(final AccessibleContext accessibleContext) {
        debugString("[INFO]:  requestFocus");
        if (accessibleContext == null) {
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.159
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                AccessibleComponent accessibleComponent = accessibleContext.getAccessibleComponent();
                if (accessibleComponent == null) {
                    return false;
                }
                accessibleComponent.requestFocus();
                return Boolean.valueOf(accessibleContext.getAccessibleStateSet().contains(AccessibleState.FOCUSED));
            }
        }, accessibleContext)).booleanValue();
    }

    private boolean selectTextRange(final AccessibleContext accessibleContext, final int i2, final int i3) {
        debugString("[INFO]:  selectTextRange: start = " + i2 + "; end = " + i3);
        if (accessibleContext == null) {
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.160
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (!(accessibleText instanceof AccessibleEditableText)) {
                    return false;
                }
                ((AccessibleEditableText) accessibleText).selectText(i2, i3);
                return Boolean.valueOf(accessibleText.getSelectionStart() == i2 && accessibleText.getSelectionEnd() == i3);
            }
        }, accessibleContext)).booleanValue();
    }

    private boolean setCaretPosition(final AccessibleContext accessibleContext, final int i2) {
        debugString("[INFO]: setCaretPosition: position = " + i2);
        if (accessibleContext == null) {
            return false;
        }
        return ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.161
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Boolean call() throws Exception {
                AccessibleText accessibleText = accessibleContext.getAccessibleText();
                if (!(accessibleText instanceof AccessibleEditableText)) {
                    return false;
                }
                ((AccessibleEditableText) accessibleText).selectText(i2, i2);
                return Boolean.valueOf(accessibleText.getCaretPosition() == i2);
            }
        }, accessibleContext)).booleanValue();
    }

    private int getVisibleChildrenCount(AccessibleContext accessibleContext) {
        debugString("[INFO]: getVisibleChildrenCount");
        if (accessibleContext == null) {
            return -1;
        }
        this._visibleChildrenCount = 0;
        _getVisibleChildrenCount(accessibleContext);
        debugString("[INFO]:   _visibleChildrenCount = " + this._visibleChildrenCount);
        return this._visibleChildrenCount;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void _getVisibleChildrenCount(final AccessibleContext accessibleContext) {
        if (accessibleContext == 0) {
            return;
        }
        if (accessibleContext instanceof AccessibleExtendedTable) {
            _getVisibleChildrenCount((AccessibleExtendedTable) accessibleContext);
            return;
        }
        int iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.162
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleContext.getAccessibleChildrenCount());
            }
        }, accessibleContext)).intValue();
        for (int i2 = 0; i2 < iIntValue; i2++) {
            final int i3 = i2;
            final AccessibleContext accessibleContext2 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.163
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleContext call() throws Exception {
                    Accessible accessibleChild = accessibleContext.getAccessibleChild(i3);
                    if (accessibleChild != null) {
                        return accessibleChild.getAccessibleContext();
                    }
                    return null;
                }
            }, accessibleContext);
            if (accessibleContext2 != null && ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.164
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Boolean call() throws Exception {
                    return Boolean.valueOf(accessibleContext2.getAccessibleStateSet().contains(AccessibleState.SHOWING));
                }
            }, accessibleContext)).booleanValue()) {
                this._visibleChildrenCount++;
                if (((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.165
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.concurrent.Callable
                    public Integer call() throws Exception {
                        return Integer.valueOf(accessibleContext2.getAccessibleChildrenCount());
                    }
                }, accessibleContext)).intValue() > 0) {
                    _getVisibleChildrenCount(accessibleContext2);
                }
            }
        }
    }

    private void _getVisibleChildrenCount(final AccessibleExtendedTable accessibleExtendedTable) {
        if (accessibleExtendedTable == null) {
            return;
        }
        int i2 = -1;
        int i3 = -1;
        boolean z2 = false;
        int iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.166
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleExtendedTable.getAccessibleRowCount());
            }
        }, accessibleExtendedTable)).intValue();
        int iIntValue2 = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.167
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleExtendedTable.getAccessibleColumnCount());
            }
        }, accessibleExtendedTable)).intValue();
        for (int i4 = 0; i4 < iIntValue; i4++) {
            for (int i5 = 0; i5 < iIntValue2; i5++) {
                if ((i2 == -1 || i4 <= i2) && (i3 == -1 || i5 <= i3)) {
                    final int i6 = i4;
                    final int i7 = i5;
                    final AccessibleContext accessibleContext = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.168
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleContext call() throws Exception {
                            Accessible accessibleAt = accessibleExtendedTable.getAccessibleAt(i6, i7);
                            if (accessibleAt == null) {
                                return null;
                            }
                            return accessibleAt.getAccessibleContext();
                        }
                    }, accessibleExtendedTable);
                    if (accessibleContext == null || !((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.169
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Boolean call() throws Exception {
                            return Boolean.valueOf(accessibleContext.getAccessibleStateSet().contains(AccessibleState.SHOWING));
                        }
                    }, accessibleExtendedTable)).booleanValue()) {
                        if (z2) {
                            if (i5 != 0 && i3 == -1) {
                                i3 = i5 - 1;
                            } else if (i5 == 0 && i2 == -1) {
                                i2 = i4 - 1;
                            }
                        }
                    } else {
                        z2 = true;
                        this._visibleChildrenCount++;
                        if (((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.170
                            /* JADX WARN: Can't rename method to resolve collision */
                            @Override // java.util.concurrent.Callable
                            public Integer call() throws Exception {
                                return Integer.valueOf(accessibleContext.getAccessibleChildrenCount());
                            }
                        }, accessibleExtendedTable)).intValue() > 0) {
                            _getVisibleChildrenCount(accessibleContext);
                        }
                    }
                }
            }
        }
    }

    private AccessibleContext getVisibleChild(AccessibleContext accessibleContext, int i2) {
        debugString("[INFO]: getVisibleChild: index = " + i2);
        if (accessibleContext == null) {
            return null;
        }
        this._visibleChild = null;
        this._currentVisibleIndex = 0;
        this._foundVisibleChild = false;
        _getVisibleChild(accessibleContext, i2);
        if (this._visibleChild != null) {
            debugString("[INFO]:     getVisibleChild: found child = " + ((String) InvocationUtils.invokeAndWait(new Callable<String>() { // from class: com.sun.java.accessibility.AccessBridge.171
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public String call() throws Exception {
                    return AccessBridge.this._visibleChild.getAccessibleName();
                }
            }, accessibleContext)));
        }
        return this._visibleChild;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void _getVisibleChild(final AccessibleContext accessibleContext, int i2) {
        if (this._visibleChild != null) {
            return;
        }
        if (accessibleContext instanceof AccessibleExtendedTable) {
            _getVisibleChild((AccessibleExtendedTable) accessibleContext, i2);
            return;
        }
        int iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.172
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleContext.getAccessibleChildrenCount());
            }
        }, accessibleContext)).intValue();
        for (int i3 = 0; i3 < iIntValue; i3++) {
            final int i4 = i3;
            final AccessibleContext accessibleContext2 = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.173
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public AccessibleContext call() throws Exception {
                    Accessible accessibleChild = accessibleContext.getAccessibleChild(i4);
                    if (accessibleChild == null) {
                        return null;
                    }
                    return accessibleChild.getAccessibleContext();
                }
            }, accessibleContext);
            if (accessibleContext2 != null && ((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.174
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.concurrent.Callable
                public Boolean call() throws Exception {
                    return Boolean.valueOf(accessibleContext2.getAccessibleStateSet().contains(AccessibleState.SHOWING));
                }
            }, accessibleContext)).booleanValue()) {
                if (!this._foundVisibleChild && this._currentVisibleIndex == i2) {
                    this._visibleChild = accessibleContext2;
                    this._foundVisibleChild = true;
                    return;
                } else {
                    this._currentVisibleIndex++;
                    if (((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.175
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Integer call() throws Exception {
                            return Integer.valueOf(accessibleContext2.getAccessibleChildrenCount());
                        }
                    }, accessibleContext)).intValue() > 0) {
                        _getVisibleChild(accessibleContext2, i2);
                    }
                }
            }
        }
    }

    private void _getVisibleChild(final AccessibleExtendedTable accessibleExtendedTable, int i2) {
        if (this._visibleChild != null) {
            return;
        }
        int i3 = -1;
        int i4 = -1;
        boolean z2 = false;
        int iIntValue = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.176
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleExtendedTable.getAccessibleRowCount());
            }
        }, accessibleExtendedTable)).intValue();
        int iIntValue2 = ((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.177
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            public Integer call() throws Exception {
                return Integer.valueOf(accessibleExtendedTable.getAccessibleColumnCount());
            }
        }, accessibleExtendedTable)).intValue();
        for (int i5 = 0; i5 < iIntValue; i5++) {
            for (int i6 = 0; i6 < iIntValue2; i6++) {
                if ((i3 == -1 || i5 <= i3) && (i4 == -1 || i6 <= i4)) {
                    final int i7 = i5;
                    final int i8 = i6;
                    final AccessibleContext accessibleContext = (AccessibleContext) InvocationUtils.invokeAndWait(new Callable<AccessibleContext>() { // from class: com.sun.java.accessibility.AccessBridge.178
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public AccessibleContext call() throws Exception {
                            Accessible accessibleAt = accessibleExtendedTable.getAccessibleAt(i7, i8);
                            if (accessibleAt == null) {
                                return null;
                            }
                            return accessibleAt.getAccessibleContext();
                        }
                    }, accessibleExtendedTable);
                    if (accessibleContext == null || !((Boolean) InvocationUtils.invokeAndWait(new Callable<Boolean>() { // from class: com.sun.java.accessibility.AccessBridge.179
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.util.concurrent.Callable
                        public Boolean call() throws Exception {
                            return Boolean.valueOf(accessibleContext.getAccessibleStateSet().contains(AccessibleState.SHOWING));
                        }
                    }, accessibleExtendedTable)).booleanValue()) {
                        if (z2) {
                            if (i6 != 0 && i4 == -1) {
                                i4 = i6 - 1;
                            } else if (i6 == 0 && i3 == -1) {
                                i3 = i5 - 1;
                            }
                        }
                    } else {
                        z2 = true;
                        if (!this._foundVisibleChild && this._currentVisibleIndex == i2) {
                            this._visibleChild = accessibleContext;
                            this._foundVisibleChild = true;
                            return;
                        } else {
                            this._currentVisibleIndex++;
                            if (((Integer) InvocationUtils.invokeAndWait(new Callable<Integer>() { // from class: com.sun.java.accessibility.AccessBridge.180
                                /* JADX WARN: Can't rename method to resolve collision */
                                @Override // java.util.concurrent.Callable
                                public Integer call() throws Exception {
                                    return Integer.valueOf(accessibleContext.getAccessibleChildrenCount());
                                }
                            }, accessibleExtendedTable)).intValue() > 0) {
                                _getVisibleChild(accessibleContext, i2);
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$ObjectReferences.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$ObjectReferences.class */
    private class ObjectReferences {
        private ConcurrentHashMap<Object, Reference> refs = new ConcurrentHashMap<>(4);

        /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$ObjectReferences$Reference.class
 */
        /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$ObjectReferences$Reference.class */
        private class Reference {
            private int value;

            static /* synthetic */ int access$1208(Reference reference) {
                int i2 = reference.value;
                reference.value = i2 + 1;
                return i2;
            }

            static /* synthetic */ int access$1210(Reference reference) {
                int i2 = reference.value;
                reference.value = i2 - 1;
                return i2;
            }

            Reference(int i2) {
                this.value = i2;
            }

            public String toString() {
                return "refCount: " + this.value;
            }
        }

        ObjectReferences() {
        }

        String dump() {
            return this.refs.toString();
        }

        void increment(Object obj) {
            if (obj == null) {
                AccessBridge.this.debugString("[WARN]: ObjectReferences::increment - Passed in object is null");
            } else if (this.refs.containsKey(obj)) {
                Reference.access$1208(this.refs.get(obj));
            } else {
                this.refs.put(obj, new Reference(1));
            }
        }

        void decrement(Object obj) {
            Reference reference = this.refs.get(obj);
            if (reference == null) {
                AccessBridge.this.debugString("[ERROR]: object to decrement not in ObjectReferences table");
                return;
            }
            Reference.access$1210(reference);
            if (reference.value != 0) {
                if (reference.value < 0) {
                    AccessBridge.this.debugString("[ERROR]: decrementing reference count below 0");
                    return;
                }
                return;
            }
            this.refs.remove(obj);
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$EventHandler.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$EventHandler.class */
    private class EventHandler implements PropertyChangeListener, FocusListener, CaretListener, MenuListener, PopupMenuListener, MouseListener, WindowListener, ChangeListener {
        private AccessBridge accessBridge;
        private long javaEventMask = 0;
        private long accessibilityEventMask = 0;
        private AccessibleContext prevAC = null;
        private boolean stateChangeListenerAdded = false;

        EventHandler(AccessBridge accessBridge) {
            this.accessBridge = accessBridge;
        }

        @Override // java.awt.event.WindowListener
        public void windowOpened(WindowEvent windowEvent) {
            Object source = null;
            if (windowEvent != null) {
                source = windowEvent.getSource();
            }
            if (source instanceof NativeWindowHandler) {
                AccessBridge.addNativeWindowHandler((NativeWindowHandler) source);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowClosed(WindowEvent windowEvent) {
            Object source = null;
            if (windowEvent != null) {
                source = windowEvent.getSource();
            }
            if (source instanceof NativeWindowHandler) {
                AccessBridge.removeNativeWindowHandler((NativeWindowHandler) source);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowIconified(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeiconified(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
        }

        void addJavaEventNotification(long j2) {
            long j3 = this.javaEventMask | j2;
            if ((this.javaEventMask & 6) == 0 && (j3 & 6) != 0) {
                SwingEventMonitor.addFocusListener(this);
            }
            if ((this.javaEventMask & 8) == 0 && (j3 & 8) != 0) {
                SwingEventMonitor.addCaretListener(this);
            }
            if ((this.javaEventMask & AccessBridge.MOUSE_EVENTS) == 0 && (j3 & AccessBridge.MOUSE_EVENTS) != 0) {
                SwingEventMonitor.addMouseListener(this);
            }
            if ((this.javaEventMask & AccessBridge.MENU_EVENTS) == 0 && (j3 & AccessBridge.MENU_EVENTS) != 0) {
                SwingEventMonitor.addMenuListener(this);
                SwingEventMonitor.addPopupMenuListener(this);
            }
            if ((this.javaEventMask & AccessBridge.POPUPMENU_EVENTS) == 0 && (j3 & AccessBridge.POPUPMENU_EVENTS) != 0) {
                SwingEventMonitor.addPopupMenuListener(this);
            }
            this.javaEventMask = j3;
        }

        void removeJavaEventNotification(long j2) {
            long j3 = this.javaEventMask & (j2 ^ (-1));
            if ((this.javaEventMask & 6) != 0 && (j3 & 6) == 0) {
                SwingEventMonitor.removeFocusListener(this);
            }
            if ((this.javaEventMask & 8) != 0 && (j3 & 8) == 0) {
                SwingEventMonitor.removeCaretListener(this);
            }
            if ((this.javaEventMask & AccessBridge.MOUSE_EVENTS) == 0 && (j3 & AccessBridge.MOUSE_EVENTS) != 0) {
                SwingEventMonitor.removeMouseListener(this);
            }
            if ((this.javaEventMask & AccessBridge.MENU_EVENTS) == 0 && (j3 & AccessBridge.MENU_EVENTS) != 0) {
                SwingEventMonitor.removeMenuListener(this);
            }
            if ((this.javaEventMask & AccessBridge.POPUPMENU_EVENTS) == 0 && (j3 & AccessBridge.POPUPMENU_EVENTS) != 0) {
                SwingEventMonitor.removePopupMenuListener(this);
            }
            this.javaEventMask = j3;
        }

        void addAccessibilityEventNotification(long j2) {
            long j3 = this.accessibilityEventMask | j2;
            if ((this.accessibilityEventMask & AccessBridge.PROPERTY_EVENTS) == 0 && (j3 & AccessBridge.PROPERTY_EVENTS) != 0) {
                AccessibilityEventMonitor.addPropertyChangeListener(this);
            }
            this.accessibilityEventMask = j3;
        }

        void removeAccessibilityEventNotification(long j2) {
            long j3 = this.accessibilityEventMask & (j2 ^ (-1));
            if ((this.accessibilityEventMask & AccessBridge.PROPERTY_EVENTS) != 0 && (j3 & AccessBridge.PROPERTY_EVENTS) == 0) {
                AccessibilityEventMonitor.removePropertyChangeListener(this);
            }
            this.accessibilityEventMask = j3;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            AccessibleContext accessibleContext;
            this.accessBridge.debugString("[INFO]: propertyChange(" + propertyChangeEvent.toString() + ") called");
            if (propertyChangeEvent != null && (this.accessibilityEventMask & AccessBridge.PROPERTY_EVENTS) != 0) {
                Object source = propertyChangeEvent.getSource();
                if (source instanceof AccessibleContext) {
                    accessibleContext = (AccessibleContext) source;
                } else {
                    Accessible accessible = Translator.getAccessible(propertyChangeEvent.getSource());
                    if (accessible == null) {
                        return;
                    } else {
                        accessibleContext = accessible.getAccessibleContext();
                    }
                }
                if (accessibleContext != null) {
                    InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                    this.accessBridge.debugString("[INFO]: AccessibleContext: " + ((Object) accessibleContext));
                    String propertyName = propertyChangeEvent.getPropertyName();
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_CARET_PROPERTY) == 0) {
                        int iIntValue = 0;
                        int iIntValue2 = 0;
                        if (propertyChangeEvent.getOldValue() instanceof Integer) {
                            iIntValue = ((Integer) propertyChangeEvent.getOldValue()).intValue();
                        }
                        if (propertyChangeEvent.getNewValue() instanceof Integer) {
                            iIntValue2 = ((Integer) propertyChangeEvent.getNewValue()).intValue();
                        }
                        this.accessBridge.debugString("[INFO]:  - about to call propertyCaretChange()   old value: " + iIntValue + "new value: " + iIntValue2);
                        this.accessBridge.propertyCaretChange(propertyChangeEvent, accessibleContext, iIntValue, iIntValue2);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY) == 0) {
                        String string = null;
                        String string2 = null;
                        if (propertyChangeEvent.getOldValue() != null) {
                            string = propertyChangeEvent.getOldValue().toString();
                        }
                        if (propertyChangeEvent.getNewValue() != null) {
                            string2 = propertyChangeEvent.getNewValue().toString();
                        }
                        this.accessBridge.debugString("[INFO]:  - about to call propertyDescriptionChange()   old value: " + string + "new value: " + string2);
                        this.accessBridge.propertyDescriptionChange(propertyChangeEvent, accessibleContext, string, string2);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_NAME_PROPERTY) == 0) {
                        String string3 = null;
                        String string4 = null;
                        if (propertyChangeEvent.getOldValue() != null) {
                            string3 = propertyChangeEvent.getOldValue().toString();
                        }
                        if (propertyChangeEvent.getNewValue() != null) {
                            string4 = propertyChangeEvent.getNewValue().toString();
                        }
                        this.accessBridge.debugString("[INFO]:  - about to call propertyNameChange()   old value: " + string3 + " new value: " + string4);
                        this.accessBridge.propertyNameChange(propertyChangeEvent, accessibleContext, string3, string4);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY) == 0) {
                        this.accessBridge.debugString("[INFO]:  - about to call propertySelectionChange() " + ((Object) accessibleContext) + "   " + ((Object) Thread.currentThread()) + "   " + propertyChangeEvent.getSource());
                        this.accessBridge.propertySelectionChange(propertyChangeEvent, accessibleContext);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_STATE_PROPERTY) == 0) {
                        String displayString = null;
                        String displayString2 = null;
                        if (propertyChangeEvent.getOldValue() != null) {
                            displayString = ((AccessibleState) propertyChangeEvent.getOldValue()).toDisplayString(Locale.US);
                        }
                        if (propertyChangeEvent.getNewValue() != null) {
                            displayString2 = ((AccessibleState) propertyChangeEvent.getNewValue()).toDisplayString(Locale.US);
                        }
                        this.accessBridge.debugString("[INFO]:  - about to call propertyStateChange()");
                        this.accessBridge.propertyStateChange(propertyChangeEvent, accessibleContext, displayString, displayString2);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY) == 0) {
                        this.accessBridge.debugString("[INFO]:  - about to call propertyTextChange()");
                        this.accessBridge.propertyTextChange(propertyChangeEvent, accessibleContext);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY) == 0) {
                        String string5 = null;
                        String string6 = null;
                        if (propertyChangeEvent.getOldValue() != null) {
                            string5 = propertyChangeEvent.getOldValue().toString();
                        }
                        if (propertyChangeEvent.getNewValue() != null) {
                            string6 = propertyChangeEvent.getNewValue().toString();
                        }
                        this.accessBridge.debugString("[INFO]:  - about to call propertyDescriptionChange()");
                        this.accessBridge.propertyValueChange(propertyChangeEvent, accessibleContext, string5, string6);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY) == 0) {
                        this.accessBridge.propertyVisibleDataChange(propertyChangeEvent, accessibleContext);
                        return;
                    }
                    if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_CHILD_PROPERTY) != 0) {
                        if (propertyName.compareTo(AccessibleContext.ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY) == 0) {
                            handleActiveDescendentEvent(propertyChangeEvent, accessibleContext);
                            return;
                        }
                        return;
                    }
                    AccessibleContext accessibleContext2 = null;
                    AccessibleContext accessibleContext3 = null;
                    if (propertyChangeEvent.getOldValue() instanceof AccessibleContext) {
                        accessibleContext2 = (AccessibleContext) propertyChangeEvent.getOldValue();
                        InvocationUtils.registerAccessibleContext(accessibleContext2, AppContext.getAppContext());
                    }
                    if (propertyChangeEvent.getNewValue() instanceof AccessibleContext) {
                        accessibleContext3 = (AccessibleContext) propertyChangeEvent.getNewValue();
                        InvocationUtils.registerAccessibleContext(accessibleContext3, AppContext.getAppContext());
                    }
                    this.accessBridge.debugString("[INFO]:  - about to call propertyChildChange()   old AC: " + ((Object) accessibleContext2) + "new AC: " + ((Object) accessibleContext3));
                    this.accessBridge.propertyChildChange(propertyChangeEvent, accessibleContext, accessibleContext2, accessibleContext3);
                }
            }
        }

        private void handleActiveDescendentEvent(PropertyChangeEvent propertyChangeEvent, AccessibleContext accessibleContext) {
            Accessible accessible;
            Accessible accessible2;
            if (propertyChangeEvent == null || accessibleContext == null) {
                return;
            }
            AccessibleContext accessibleContext2 = null;
            AccessibleContext accessibleContext3 = null;
            if (propertyChangeEvent.getOldValue() instanceof Accessible) {
                accessibleContext2 = ((Accessible) propertyChangeEvent.getOldValue()).getAccessibleContext();
            } else if ((propertyChangeEvent.getOldValue() instanceof Component) && (accessible = Translator.getAccessible(propertyChangeEvent.getOldValue())) != null) {
                accessibleContext2 = accessible.getAccessibleContext();
            }
            if (accessibleContext2 != null && (accessibleContext2.getAccessibleParent() instanceof JTree)) {
                accessibleContext2 = this.prevAC;
            }
            if (propertyChangeEvent.getNewValue() instanceof Accessible) {
                accessibleContext3 = ((Accessible) propertyChangeEvent.getNewValue()).getAccessibleContext();
            } else if ((propertyChangeEvent.getNewValue() instanceof Component) && (accessible2 = Translator.getAccessible(propertyChangeEvent.getNewValue())) != null) {
                accessibleContext3 = accessible2.getAccessibleContext();
            }
            if (accessibleContext3 != null) {
                Accessible accessibleParent = accessibleContext3.getAccessibleParent();
                if (accessibleParent instanceof JTree) {
                    JTree jTree = (JTree) accessibleParent;
                    accessibleContext3 = AccessBridge.this.new AccessibleJTreeNode(jTree, jTree.getSelectionPath(), null);
                }
            }
            this.prevAC = accessibleContext3;
            this.accessBridge.debugString("[INFO]:   - about to call propertyActiveDescendentChange()   AC: " + ((Object) accessibleContext) + "   old AC: " + ((Object) accessibleContext2) + "new AC: " + ((Object) accessibleContext3));
            InvocationUtils.registerAccessibleContext(accessibleContext2, AppContext.getAppContext());
            InvocationUtils.registerAccessibleContext(accessibleContext3, AppContext.getAppContext());
            this.accessBridge.propertyActiveDescendentChange(propertyChangeEvent, accessibleContext, accessibleContext2, accessibleContext3);
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            Accessible accessible;
            if (AccessBridge.this.runningOnJDK1_4) {
                processFocusGained();
            } else if ((this.javaEventMask & 2) != 0 && (accessible = Translator.getAccessible(focusEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, SunToolkit.targetToAppContext(focusEvent.getSource()));
                this.accessBridge.focusGained(focusEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            processFocusGained();
        }

        private void processFocusGained() {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (focusOwner == null) {
                return;
            }
            if (focusOwner instanceof JRootPane) {
                MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
                if (selectedPath.length > 1) {
                    Component component = selectedPath[selectedPath.length - 2].getComponent();
                    Component component2 = selectedPath[selectedPath.length - 1].getComponent();
                    if (component2 instanceof JPopupMenu) {
                        FocusEvent focusEvent = new FocusEvent(component, 1004);
                        AccessibleContext accessibleContext = component.getAccessibleContext();
                        InvocationUtils.registerAccessibleContext(accessibleContext, SunToolkit.targetToAppContext(component));
                        this.accessBridge.focusGained(focusEvent, accessibleContext);
                        return;
                    }
                    if (component instanceof JPopupMenu) {
                        FocusEvent focusEvent2 = new FocusEvent(component2, 1004);
                        AccessibleContext accessibleContext2 = component2.getAccessibleContext();
                        InvocationUtils.registerAccessibleContext(accessibleContext2, SunToolkit.targetToAppContext(component2));
                        this.accessBridge.debugString("[INFO]:  - about to call focusGained()   AC: " + ((Object) accessibleContext2));
                        this.accessBridge.focusGained(focusEvent2, accessibleContext2);
                        return;
                    }
                    return;
                }
                return;
            }
            if (focusOwner instanceof Accessible) {
                FocusEvent focusEvent3 = new FocusEvent(focusOwner, 1004);
                AccessibleContext accessibleContext3 = focusOwner.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext3, SunToolkit.targetToAppContext(focusOwner));
                this.accessBridge.debugString("[INFO]:  - about to call focusGained()   AC: " + ((Object) accessibleContext3));
                this.accessBridge.focusGained(focusEvent3, accessibleContext3);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            Accessible accessible;
            if (focusEvent != null && (this.javaEventMask & 4) != 0 && (accessible = Translator.getAccessible(focusEvent.getSource())) != null) {
                this.accessBridge.debugString("[INFO]:  - about to call focusLost()   AC: " + ((Object) accessible.getAccessibleContext()));
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.focusLost(focusEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.CaretListener
        public void caretUpdate(CaretEvent caretEvent) {
            Accessible accessible;
            if (caretEvent != null && (this.javaEventMask & 8) != 0 && (accessible = Translator.getAccessible(caretEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.caretUpdate(caretEvent, accessibleContext);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            Accessible accessible;
            if (mouseEvent != null && (this.javaEventMask & 16) != 0 && (accessible = Translator.getAccessible(mouseEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.mouseClicked(mouseEvent, accessibleContext);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            Accessible accessible;
            if (mouseEvent != null && (this.javaEventMask & 32) != 0 && (accessible = Translator.getAccessible(mouseEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.mouseEntered(mouseEvent, accessibleContext);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            Accessible accessible;
            if (mouseEvent != null && (this.javaEventMask & 64) != 0 && (accessible = Translator.getAccessible(mouseEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.mouseExited(mouseEvent, accessibleContext);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            Accessible accessible;
            if (mouseEvent != null && (this.javaEventMask & 128) != 0 && (accessible = Translator.getAccessible(mouseEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.mousePressed(mouseEvent, accessibleContext);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            Accessible accessible;
            if (mouseEvent != null && (this.javaEventMask & 256) != 0 && (accessible = Translator.getAccessible(mouseEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.mouseReleased(mouseEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.MenuListener
        public void menuCanceled(MenuEvent menuEvent) {
            Accessible accessible;
            if (menuEvent != null && (this.javaEventMask & 512) != 0 && (accessible = Translator.getAccessible(menuEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.menuCanceled(menuEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.MenuListener
        public void menuDeselected(MenuEvent menuEvent) {
            Accessible accessible;
            if (menuEvent != null && (this.javaEventMask & 1024) != 0 && (accessible = Translator.getAccessible(menuEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.menuDeselected(menuEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.MenuListener
        public void menuSelected(MenuEvent menuEvent) {
            Accessible accessible;
            if (menuEvent != null && (this.javaEventMask & 2048) != 0 && (accessible = Translator.getAccessible(menuEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.menuSelected(menuEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
            Accessible accessible;
            if (popupMenuEvent != null && (this.javaEventMask & 4096) != 0 && (accessible = Translator.getAccessible(popupMenuEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.popupMenuCanceled(popupMenuEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
            Accessible accessible;
            if (popupMenuEvent != null && (this.javaEventMask & 8192) != 0 && (accessible = Translator.getAccessible(popupMenuEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.popupMenuWillBecomeInvisible(popupMenuEvent, accessibleContext);
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            Accessible accessible;
            if (popupMenuEvent != null && (this.javaEventMask & 16384) != 0 && (accessible = Translator.getAccessible(popupMenuEvent.getSource())) != null) {
                AccessibleContext accessibleContext = accessible.getAccessibleContext();
                InvocationUtils.registerAccessibleContext(accessibleContext, AppContext.getAppContext());
                this.accessBridge.popupMenuWillBecomeVisible(popupMenuEvent, accessibleContext);
            }
        }
    }

    private void addJavaEventNotification(final long j2) {
        EventQueue.invokeLater(new Runnable() { // from class: com.sun.java.accessibility.AccessBridge.181
            @Override // java.lang.Runnable
            public void run() {
                AccessBridge.this.eventHandler.addJavaEventNotification(j2);
            }
        });
    }

    private void removeJavaEventNotification(final long j2) {
        EventQueue.invokeLater(new Runnable() { // from class: com.sun.java.accessibility.AccessBridge.182
            @Override // java.lang.Runnable
            public void run() {
                AccessBridge.this.eventHandler.removeJavaEventNotification(j2);
            }
        });
    }

    private void addAccessibilityEventNotification(final long j2) {
        EventQueue.invokeLater(new Runnable() { // from class: com.sun.java.accessibility.AccessBridge.183
            @Override // java.lang.Runnable
            public void run() {
                AccessBridge.this.eventHandler.addAccessibilityEventNotification(j2);
            }
        });
    }

    private void removeAccessibilityEventNotification(final long j2) {
        EventQueue.invokeLater(new Runnable() { // from class: com.sun.java.accessibility.AccessBridge.184
            @Override // java.lang.Runnable
            public void run() {
                AccessBridge.this.eventHandler.removeAccessibilityEventNotification(j2);
            }
        });
    }

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$AccessibleJTreeNode.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$AccessibleJTreeNode.class */
    private class AccessibleJTreeNode extends AccessibleContext implements Accessible, AccessibleComponent, AccessibleSelection, AccessibleAction {
        private JTree tree;
        private TreeModel treeModel;
        private Object obj;
        private TreePath path;
        private Accessible accessibleParent;
        private int index = 0;
        private boolean isLeaf;

        AccessibleJTreeNode(JTree jTree, TreePath treePath, Accessible accessible) {
            this.tree = null;
            this.treeModel = null;
            this.obj = null;
            this.path = null;
            this.accessibleParent = null;
            this.isLeaf = false;
            this.tree = jTree;
            this.path = treePath;
            this.accessibleParent = accessible;
            if (jTree != null) {
                this.treeModel = jTree.getModel();
            }
            if (treePath != null) {
                this.obj = treePath.getLastPathComponent();
                if (this.treeModel != null && this.obj != null) {
                    this.isLeaf = this.treeModel.isLeaf(this.obj);
                }
            }
            AccessBridge.this.debugString("[INFO]: AccessibleJTreeNode: name = " + getAccessibleName() + "; TreePath = " + ((Object) treePath) + "; parent = " + ((Object) accessible));
        }

        private TreePath getChildTreePath(int i2) {
            if (i2 < 0 || i2 >= getAccessibleChildrenCount() || this.path == null || this.treeModel == null) {
                return null;
            }
            Object child = this.treeModel.getChild(this.obj, i2);
            Object[] path = this.path.getPath();
            Object[] objArr = new Object[path.length + 1];
            System.arraycopy(path, 0, objArr, 0, path.length);
            objArr[objArr.length - 1] = child;
            return new TreePath(objArr);
        }

        @Override // javax.accessibility.Accessible
        public AccessibleContext getAccessibleContext() {
            return this;
        }

        private AccessibleContext getCurrentAccessibleContext() {
            Component currentComponent = getCurrentComponent();
            if (currentComponent instanceof Accessible) {
                return currentComponent.getAccessibleContext();
            }
            return null;
        }

        private Component getCurrentComponent() {
            AccessBridge.this.debugString("[INFO]: AccessibleJTreeNode: getCurrentComponent");
            if (this.tree != null && this.tree.isVisible(this.path)) {
                TreeCellRenderer cellRenderer = this.tree.getCellRenderer();
                if (cellRenderer == null) {
                    AccessBridge.this.debugString("[WARN]:  returning null 1");
                    return null;
                }
                TreeUI ui = this.tree.getUI();
                if (ui != null) {
                    Component treeCellRendererComponent = cellRenderer.getTreeCellRendererComponent(this.tree, this.obj, this.tree.isPathSelected(this.path), this.tree.isExpanded(this.path), this.isLeaf, ui.getRowForPath(this.tree, this.path), false);
                    AccessBridge.this.debugString("[INFO]:   returning = " + ((Object) treeCellRendererComponent.getClass()));
                    return treeCellRendererComponent;
                }
            }
            AccessBridge.this.debugString("[WARN]:  returning null 2");
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            AccessBridge.this.debugString("[INFO]: AccessibleJTreeNode: getAccessibleName");
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                String accessibleName = currentAccessibleContext.getAccessibleName();
                if (accessibleName != null && !accessibleName.isEmpty()) {
                    String accessibleName2 = currentAccessibleContext.getAccessibleName();
                    AccessBridge.this.debugString("[INFO]:     returning " + accessibleName2);
                    return accessibleName2;
                }
                return null;
            }
            if (this.accessibleName != null && this.accessibleName.isEmpty()) {
                return this.accessibleName;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public void setAccessibleName(String str) {
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                currentAccessibleContext.setAccessibleName(str);
            } else {
                super.setAccessibleName(str);
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                return currentAccessibleContext.getAccessibleDescription();
            }
            return super.getAccessibleDescription();
        }

        @Override // javax.accessibility.AccessibleContext
        public void setAccessibleDescription(String str) {
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                currentAccessibleContext.setAccessibleDescription(str);
            } else {
                super.setAccessibleDescription(str);
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                return currentAccessibleContext.getAccessibleRole();
            }
            return AccessibleRole.UNKNOWN;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet;
            if (this.tree == null) {
                return null;
            }
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            int rowForPath = this.tree.getUI().getRowForPath(this.tree, this.path);
            int leadSelectionRow = this.tree.getLeadSelectionRow();
            if (currentAccessibleContext != null) {
                accessibleStateSet = currentAccessibleContext.getAccessibleStateSet();
            } else {
                accessibleStateSet = new AccessibleStateSet();
            }
            if (isShowing()) {
                accessibleStateSet.add(AccessibleState.SHOWING);
            } else if (accessibleStateSet.contains(AccessibleState.SHOWING)) {
                accessibleStateSet.remove(AccessibleState.SHOWING);
            }
            if (isVisible()) {
                accessibleStateSet.add(AccessibleState.VISIBLE);
            } else if (accessibleStateSet.contains(AccessibleState.VISIBLE)) {
                accessibleStateSet.remove(AccessibleState.VISIBLE);
            }
            if (this.tree.isPathSelected(this.path)) {
                accessibleStateSet.add(AccessibleState.SELECTED);
            }
            if (leadSelectionRow == rowForPath) {
                accessibleStateSet.add(AccessibleState.ACTIVE);
            }
            if (!this.isLeaf) {
                accessibleStateSet.add(AccessibleState.EXPANDABLE);
            }
            if (this.tree.isExpanded(this.path)) {
                accessibleStateSet.add(AccessibleState.EXPANDED);
            } else {
                accessibleStateSet.add(AccessibleState.COLLAPSED);
            }
            if (this.tree.isEditable()) {
                accessibleStateSet.add(AccessibleState.EDITABLE);
            }
            return accessibleStateSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleParent() {
            if (this.accessibleParent == null && this.path != null) {
                Object[] path = this.path.getPath();
                if (path.length > 1) {
                    Object obj = path[path.length - 2];
                    if (this.treeModel != null) {
                        this.index = this.treeModel.getIndexOfChild(obj, this.obj);
                    }
                    Object[] objArr = new Object[path.length - 1];
                    System.arraycopy(path, 0, objArr, 0, path.length - 1);
                    this.accessibleParent = AccessBridge.this.new AccessibleJTreeNode(this.tree, new TreePath(objArr), null);
                    setAccessibleParent(this.accessibleParent);
                } else if (this.treeModel != null) {
                    this.accessibleParent = this.tree;
                    this.index = 0;
                    setAccessibleParent(this.accessibleParent);
                }
            }
            return this.accessibleParent;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            if (this.accessibleParent == null) {
                getAccessibleParent();
            }
            if (this.path != null) {
                Object[] path = this.path.getPath();
                if (path.length > 1) {
                    Object obj = path[path.length - 2];
                    if (this.treeModel != null) {
                        this.index = this.treeModel.getIndexOfChild(obj, this.obj);
                    }
                }
            }
            return this.index;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            if (this.obj != null && this.treeModel != null) {
                return this.treeModel.getChildCount(this.obj);
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (i2 < 0 || i2 >= getAccessibleChildrenCount() || this.path == null || this.treeModel == null) {
                return null;
            }
            Object child = this.treeModel.getChild(this.obj, i2);
            Object[] path = this.path.getPath();
            Object[] objArr = new Object[path.length + 1];
            System.arraycopy(path, 0, objArr, 0, path.length);
            objArr[objArr.length - 1] = child;
            return AccessBridge.this.new AccessibleJTreeNode(this.tree, new TreePath(objArr), this);
        }

        @Override // javax.accessibility.AccessibleContext
        public Locale getLocale() {
            if (this.tree == null) {
                return null;
            }
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                return currentAccessibleContext.getLocale();
            }
            return this.tree.getLocale();
        }

        @Override // javax.accessibility.AccessibleContext
        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                currentAccessibleContext.addPropertyChangeListener(propertyChangeListener);
            } else {
                super.addPropertyChangeListener(propertyChangeListener);
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext != null) {
                currentAccessibleContext.removePropertyChangeListener(propertyChangeListener);
            } else {
                super.removePropertyChangeListener(propertyChangeListener);
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleComponent getAccessibleComponent() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            if (getCurrentAccessibleContext() != null && this.isLeaf) {
                return getCurrentAccessibleContext().getAccessibleSelection();
            }
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            if (getCurrentAccessibleContext() != null) {
                return getCurrentAccessibleContext().getAccessibleText();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            if (getCurrentAccessibleContext() != null) {
                return getCurrentAccessibleContext().getAccessibleValue();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getBackground() {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).getBackground();
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.getBackground();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBackground(Color color) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).setBackground(color);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.setBackground(color);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getForeground() {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).getForeground();
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.getForeground();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setForeground(Color color) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).setForeground(color);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.setForeground(color);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public Cursor getCursor() {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).getCursor();
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.getCursor();
            }
            Accessible accessibleParent = getAccessibleParent();
            if (accessibleParent instanceof AccessibleComponent) {
                return ((AccessibleComponent) accessibleParent).getCursor();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setCursor(Cursor cursor) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).setCursor(cursor);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.setCursor(cursor);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public Font getFont() {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).getFont();
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.getFont();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setFont(Font font) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).setFont(font);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.setFont(font);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public FontMetrics getFontMetrics(Font font) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).getFontMetrics(font);
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.getFontMetrics(font);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isEnabled() {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).isEnabled();
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.isEnabled();
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setEnabled(boolean z2) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).setEnabled(z2);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.setEnabled(z2);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isVisible() {
            if (this.tree == null) {
                return false;
            }
            Rectangle pathBounds = this.tree.getPathBounds(this.path);
            Rectangle visibleRect = this.tree.getVisibleRect();
            if (pathBounds != null && visibleRect != null && visibleRect.intersects(pathBounds)) {
                return true;
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setVisible(boolean z2) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isShowing() {
            return this.tree.isShowing() && isVisible();
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean contains(Point point) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).getBounds().contains(point);
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.getBounds().contains(point);
            }
            return getBounds().contains(point);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocationOnScreen() {
            if (this.tree != null) {
                Point locationOnScreen = this.tree.getLocationOnScreen();
                Rectangle pathBounds = this.tree.getPathBounds(this.path);
                if (locationOnScreen != null && pathBounds != null) {
                    Point point = new Point(pathBounds.f12372x, pathBounds.f12373y);
                    point.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                    return point;
                }
                return null;
            }
            return null;
        }

        private Point getLocationInJTree() {
            Rectangle pathBounds = this.tree.getPathBounds(this.path);
            if (pathBounds != null) {
                return pathBounds.getLocation();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocation() {
            Rectangle bounds = getBounds();
            if (bounds != null) {
                return bounds.getLocation();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setLocation(Point point) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Rectangle getBounds() {
            if (this.tree == null) {
                return null;
            }
            Rectangle pathBounds = this.tree.getPathBounds(this.path);
            Accessible accessibleParent = getAccessibleParent();
            if (accessibleParent instanceof AccessibleJTreeNode) {
                Point locationInJTree = ((AccessibleJTreeNode) accessibleParent).getLocationInJTree();
                if (locationInJTree != null && pathBounds != null) {
                    pathBounds.translate(-locationInJTree.f12370x, -locationInJTree.f12371y);
                } else {
                    return null;
                }
            }
            return pathBounds;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBounds(Rectangle rectangle) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).setBounds(rectangle);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.setBounds(rectangle);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public Dimension getSize() {
            return getBounds().getSize();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setSize(Dimension dimension) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).setSize(dimension);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.setSize(dimension);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).getAccessibleAt(point);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isFocusTraversable() {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                return ((AccessibleComponent) currentAccessibleContext).isFocusTraversable();
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                return currentComponent.isFocusable();
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void requestFocus() {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).requestFocus();
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.requestFocus();
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public void addFocusListener(FocusListener focusListener) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).addFocusListener(focusListener);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.addFocusListener(focusListener);
            }
        }

        @Override // javax.accessibility.AccessibleComponent
        public void removeFocusListener(FocusListener focusListener) {
            Object currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext instanceof AccessibleComponent) {
                ((AccessibleComponent) currentAccessibleContext).removeFocusListener(focusListener);
                return;
            }
            Component currentComponent = getCurrentComponent();
            if (currentComponent != null) {
                currentComponent.removeFocusListener(focusListener);
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            int i2 = 0;
            int accessibleChildrenCount = getAccessibleChildrenCount();
            for (int i3 = 0; i3 < accessibleChildrenCount; i3++) {
                if (this.tree.isPathSelected(getChildTreePath(i3))) {
                    i2++;
                }
            }
            return i2;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            int accessibleChildrenCount = getAccessibleChildrenCount();
            if (i2 < 0 || i2 >= accessibleChildrenCount) {
                return null;
            }
            int i3 = 0;
            for (int i4 = 0; i4 < accessibleChildrenCount && i2 >= i3; i4++) {
                TreePath childTreePath = getChildTreePath(i4);
                if (this.tree.isPathSelected(childTreePath)) {
                    if (i3 == i2) {
                        return AccessBridge.this.new AccessibleJTreeNode(this.tree, childTreePath, this);
                    }
                    i3++;
                }
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            int accessibleChildrenCount = getAccessibleChildrenCount();
            if (i2 < 0 || i2 >= accessibleChildrenCount) {
                return false;
            }
            return this.tree.isPathSelected(getChildTreePath(i2));
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            if (this.tree != null && this.tree.getModel() != null && i2 >= 0 && i2 < getAccessibleChildrenCount()) {
                this.tree.addSelectionPath(getChildTreePath(i2));
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            if (this.tree != null && this.tree.getModel() != null && i2 >= 0 && i2 < getAccessibleChildrenCount()) {
                this.tree.removeSelectionPath(getChildTreePath(i2));
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            int accessibleChildrenCount = getAccessibleChildrenCount();
            for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                removeAccessibleSelection(i2);
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
            if (this.tree != null && this.tree.getModel() != null) {
                int accessibleChildrenCount = getAccessibleChildrenCount();
                for (int i2 = 0; i2 < accessibleChildrenCount; i2++) {
                    this.tree.addSelectionPath(getChildTreePath(i2));
                }
            }
        }

        @Override // javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            AccessibleAction accessibleAction;
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (currentAccessibleContext == null || (accessibleAction = currentAccessibleContext.getAccessibleAction()) == null) {
                return this.isLeaf ? 0 : 1;
            }
            return accessibleAction.getAccessibleActionCount() + (this.isLeaf ? 0 : 1);
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            AccessibleAction accessibleAction;
            if (i2 < 0 || i2 >= getAccessibleActionCount()) {
                return null;
            }
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (i2 == 0) {
                return "toggle expand";
            }
            if (currentAccessibleContext != null && (accessibleAction = currentAccessibleContext.getAccessibleAction()) != null) {
                return accessibleAction.getAccessibleActionDescription(i2 - 1);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            AccessibleAction accessibleAction;
            if (i2 < 0 || i2 >= getAccessibleActionCount()) {
                return false;
            }
            AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
            if (i2 == 0) {
                if (this.tree.isExpanded(this.path)) {
                    this.tree.collapsePath(this.path);
                    return true;
                }
                this.tree.expandPath(this.path);
                return true;
            }
            if (currentAccessibleContext != null && (accessibleAction = currentAccessibleContext.getAccessibleAction()) != null) {
                return accessibleAction.doAccessibleAction(i2 - 1);
            }
            return false;
        }
    }

    /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$InvocationUtils.class
 */
    /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$InvocationUtils.class */
    private static class InvocationUtils {
        private InvocationUtils() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static <T> T invokeAndWait(Callable<T> callable, AccessibleExtendedTable accessibleExtendedTable) {
            if (accessibleExtendedTable instanceof AccessibleContext) {
                return (T) invokeAndWait(callable, (AccessibleContext) accessibleExtendedTable);
            }
            throw new RuntimeException("Unmapped AccessibleContext used to dispatch event: " + ((Object) accessibleExtendedTable));
        }

        /* JADX WARN: Multi-variable type inference failed */
        public static <T> T invokeAndWait(Callable<T> callable, Accessible accessible) {
            if (accessible instanceof Component) {
                return (T) invokeAndWait(callable, (Component) accessible);
            }
            if (accessible instanceof AccessibleContext) {
                return (T) invokeAndWait(callable, (AccessibleContext) accessible);
            }
            throw new RuntimeException("Unmapped Accessible used to dispatch event: " + ((Object) accessible));
        }

        public static <T> T invokeAndWait(Callable<T> callable, Component component) {
            return (T) invokeAndWait(callable, SunToolkit.targetToAppContext(component));
        }

        public static <T> T invokeAndWait(Callable<T> callable, AccessibleContext accessibleContext) {
            AppContext appContext = AWTAccessor.getAccessibleContextAccessor().getAppContext(accessibleContext);
            if (appContext != null) {
                return (T) invokeAndWait(callable, appContext);
            }
            if (accessibleContext instanceof Translator) {
                Object source = ((Translator) accessibleContext).getSource();
                if (source instanceof Component) {
                    return (T) invokeAndWait(callable, (Component) source);
                }
            }
            throw new RuntimeException("Unmapped AccessibleContext used to dispatch event: " + ((Object) accessibleContext));
        }

        private static <T> T invokeAndWait(Callable<T> callable, AppContext appContext) {
            CallableWrapper callableWrapper = new CallableWrapper(callable);
            try {
                invokeAndWait(callableWrapper, appContext);
                T t2 = (T) callableWrapper.getResult();
                updateAppContextMap(t2, appContext);
                return t2;
            } catch (Exception e2) {
                throw new RuntimeException(e2);
            }
        }

        private static void invokeAndWait(Runnable runnable, AppContext appContext) throws InterruptedException, InvocationTargetException {
            EventQueue systemEventQueueImplPP = SunToolkit.getSystemEventQueueImplPP(appContext);
            Object obj = new Object();
            InvocationEvent invocationEvent = new InvocationEvent((Object) Toolkit.getDefaultToolkit(), runnable, obj, true);
            synchronized (obj) {
                systemEventQueueImplPP.postEvent(invocationEvent);
                obj.wait();
            }
            Throwable throwable = invocationEvent.getThrowable();
            if (throwable != null) {
                throw new InvocationTargetException(throwable);
            }
        }

        public static void registerAccessibleContext(AccessibleContext accessibleContext, AppContext appContext) {
            if (accessibleContext != null) {
                AWTAccessor.getAccessibleContextAccessor().setAppContext(accessibleContext, appContext);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        private static <T> void updateAppContextMap(T t2, AppContext appContext) {
            if (t2 instanceof AccessibleContext) {
                registerAccessibleContext((AccessibleContext) t2, appContext);
            }
        }

        /* JADX WARN: Classes with same name are omitted:
  access-bridge-32.jar:com/sun/java/accessibility/AccessBridge$InvocationUtils$CallableWrapper.class
 */
        /* loaded from: access-bridge.jar:com/sun/java/accessibility/AccessBridge$InvocationUtils$CallableWrapper.class */
        private static class CallableWrapper<T> implements Runnable {
            private final Callable<T> callable;
            private volatile T object;

            /* renamed from: e, reason: collision with root package name */
            private Exception f11839e;

            CallableWrapper(Callable<T> callable) {
                this.callable = callable;
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (this.callable != null) {
                        this.object = this.callable.call();
                    }
                } catch (Exception e2) {
                    this.f11839e = e2;
                }
            }

            T getResult() throws Exception {
                if (this.f11839e != null) {
                    throw this.f11839e;
                }
                return this.object;
            }
        }
    }
}
