package java.awt;

import java.awt.Dialog;
import java.awt.datatransfer.Clipboard;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.AWTEventListener;
import java.awt.event.AWTEventListenerProxy;
import java.awt.font.TextAttribute;
import java.awt.im.InputMethodHighlight;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DesktopPeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FileDialogPeer;
import java.awt.peer.FontPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.LabelPeer;
import java.awt.peer.LightweightPeer;
import java.awt.peer.ListPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.MouseInfoPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.PopupMenuPeer;
import java.awt.peer.ScrollPanePeer;
import java.awt.peer.ScrollbarPeer;
import java.awt.peer.TextAreaPeer;
import java.awt.peer.TextFieldPeer;
import java.awt.peer.WindowPeer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.HeadlessToolkit;
import sun.awt.NullComponentPeer;
import sun.awt.PeerEvent;
import sun.awt.SunToolkit;
import sun.awt.UngrabEvent;
import sun.security.util.SecurityConstants;
import sun.util.CoreResourceBundleControl;

/* loaded from: rt.jar:java/awt/Toolkit.class */
public abstract class Toolkit {
    private static LightweightPeer lightweightMarker;
    private static Toolkit toolkit;
    private static String atNames;
    private static ResourceBundle resources;
    private static ResourceBundle platformResources;
    private static boolean loaded;
    private static final int LONG_BITS = 64;
    private static volatile long enabledOnToolkitMask;
    static final /* synthetic */ boolean $assertionsDisabled;
    protected final Map<String, Object> desktopProperties = new HashMap();
    protected final PropertyChangeSupport desktopPropsSupport = createPropertyChangeSupport(this);
    private int[] calls = new int[64];
    private AWTEventListener eventListener = null;
    private WeakHashMap<AWTEventListener, SelectiveAWTEventListener> listener2SelectiveListener = new WeakHashMap<>();

    protected abstract DesktopPeer createDesktopPeer(Desktop desktop) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract ButtonPeer createButton(Button button) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract TextFieldPeer createTextField(TextField textField) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract LabelPeer createLabel(Label label) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract ListPeer createList(List list) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract CheckboxPeer createCheckbox(Checkbox checkbox) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract ScrollbarPeer createScrollbar(Scrollbar scrollbar) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract ScrollPanePeer createScrollPane(ScrollPane scrollPane) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract TextAreaPeer createTextArea(TextArea textArea) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract ChoicePeer createChoice(Choice choice) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract FramePeer createFrame(Frame frame) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract CanvasPeer createCanvas(Canvas canvas);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract PanelPeer createPanel(Panel panel);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract WindowPeer createWindow(Window window) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract DialogPeer createDialog(Dialog dialog) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract MenuBarPeer createMenuBar(MenuBar menuBar) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract MenuPeer createMenu(Menu menu) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract PopupMenuPeer createPopupMenu(PopupMenu popupMenu) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract MenuItemPeer createMenuItem(MenuItem menuItem) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract FileDialogPeer createFileDialog(FileDialog fileDialog) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem) throws HeadlessException;

    /* JADX INFO: Access modifiers changed from: protected */
    @Deprecated
    public abstract FontPeer getFontPeer(String str, int i2);

    public abstract Dimension getScreenSize() throws HeadlessException;

    public abstract int getScreenResolution() throws HeadlessException;

    public abstract ColorModel getColorModel() throws HeadlessException;

    @Deprecated
    public abstract String[] getFontList();

    @Deprecated
    public abstract FontMetrics getFontMetrics(Font font);

    public abstract void sync();

    public abstract Image getImage(String str);

    public abstract Image getImage(URL url);

    public abstract Image createImage(String str);

    public abstract Image createImage(URL url);

    public abstract boolean prepareImage(Image image, int i2, int i3, ImageObserver imageObserver);

    public abstract int checkImage(Image image, int i2, int i3, ImageObserver imageObserver);

    public abstract Image createImage(ImageProducer imageProducer);

    public abstract Image createImage(byte[] bArr, int i2, int i3);

    public abstract PrintJob getPrintJob(Frame frame, String str, Properties properties);

    public abstract void beep();

    public abstract Clipboard getSystemClipboard() throws HeadlessException;

    private static native void initIDs();

    protected abstract EventQueue getSystemEventQueueImpl();

    public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dragGestureEvent) throws InvalidDnDOperationException;

    public abstract boolean isModalityTypeSupported(Dialog.ModalityType modalityType);

    public abstract boolean isModalExclusionTypeSupported(Dialog.ModalExclusionType modalExclusionType);

    public abstract Map<TextAttribute, ?> mapInputMethodHighlight(InputMethodHighlight inputMethodHighlight) throws HeadlessException;

    static {
        $assertionsDisabled = !Toolkit.class.desiredAssertionStatus();
        loaded = false;
        AWTAccessor.setToolkitAccessor(new AWTAccessor.ToolkitAccessor() { // from class: java.awt.Toolkit.4
            @Override // sun.awt.AWTAccessor.ToolkitAccessor
            public void setPlatformResources(ResourceBundle resourceBundle) {
                Toolkit.setPlatformResources(resourceBundle);
            }
        });
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.Toolkit.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                try {
                    ResourceBundle unused = Toolkit.resources = ResourceBundle.getBundle("sun.awt.resources.awt", Locale.getDefault(), ClassLoader.getSystemClassLoader(), CoreResourceBundleControl.getRBControlInstance());
                    return null;
                } catch (MissingResourceException e2) {
                    return null;
                }
            }
        });
        loadLibraries();
        initAssistiveTechnologies();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    protected MouseInfoPeer getMouseInfoPeer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    protected LightweightPeer createComponent(Component component) {
        if (lightweightMarker == null) {
            lightweightMarker = new NullComponentPeer();
        }
        return lightweightMarker;
    }

    protected void loadSystemColors(int[] iArr) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
    }

    public void setDynamicLayout(boolean z2) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            getDefaultToolkit().setDynamicLayout(z2);
        }
    }

    protected boolean isDynamicLayoutSet() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().isDynamicLayoutSet();
        }
        return false;
    }

    public boolean isDynamicLayoutActive() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().isDynamicLayoutActive();
        }
        return false;
    }

    public Insets getScreenInsets(GraphicsConfiguration graphicsConfiguration) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().getScreenInsets(graphicsConfiguration);
        }
        return new Insets(0, 0, 0, 0);
    }

    private static void initAssistiveTechnologies() {
        final String str = File.separator;
        final Properties properties = new Properties();
        atNames = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.awt.Toolkit.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                String property;
                try {
                    FileInputStream fileInputStream = new FileInputStream(new File(System.getProperty("user.home") + str + ".accessibility.properties"));
                    properties.load(fileInputStream);
                    fileInputStream.close();
                } catch (Exception e2) {
                }
                if (properties.size() == 0) {
                    try {
                        FileInputStream fileInputStream2 = new FileInputStream(new File(System.getProperty("java.home") + str + "lib" + str + "accessibility.properties"));
                        properties.load(fileInputStream2);
                        fileInputStream2.close();
                    } catch (Exception e3) {
                    }
                }
                if (System.getProperty("javax.accessibility.screen_magnifier_present") == null && (property = properties.getProperty("screen_magnifier_present", null)) != null) {
                    System.setProperty("javax.accessibility.screen_magnifier_present", property);
                }
                String property2 = System.getProperty("javax.accessibility.assistive_technologies");
                if (property2 == null) {
                    property2 = properties.getProperty("assistive_technologies", null);
                    if (property2 != null) {
                        System.setProperty("javax.accessibility.assistive_technologies", property2);
                    }
                }
                return property2;
            }
        });
    }

    private static void loadAssistiveTechnologies() {
        Class<?> clsLoadClass;
        if (atNames != null) {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            StringTokenizer stringTokenizer = new StringTokenizer(atNames, " ,");
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                if (systemClassLoader != null) {
                    try {
                        clsLoadClass = systemClassLoader.loadClass(strNextToken);
                    } catch (ClassNotFoundException e2) {
                        throw new AWTError("Assistive Technology not found: " + strNextToken);
                    } catch (IllegalAccessException e3) {
                        throw new AWTError("Could not access Assistive Technology: " + strNextToken);
                    } catch (InstantiationException e4) {
                        throw new AWTError("Could not instantiate Assistive Technology: " + strNextToken);
                    } catch (Exception e5) {
                        throw new AWTError("Error trying to install Assistive Technology: " + strNextToken + " " + ((Object) e5));
                    }
                } else {
                    clsLoadClass = Class.forName(strNextToken);
                }
                clsLoadClass.newInstance();
            }
        }
    }

    public static synchronized Toolkit getDefaultToolkit() {
        if (toolkit == null) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.Toolkit.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    Class<?> clsLoadClass = null;
                    String property = System.getProperty("awt.toolkit");
                    try {
                        clsLoadClass = Class.forName(property);
                    } catch (ClassNotFoundException e2) {
                        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                        if (systemClassLoader != null) {
                            try {
                                clsLoadClass = systemClassLoader.loadClass(property);
                            } catch (ClassNotFoundException e3) {
                                throw new AWTError("Toolkit not found: " + property);
                            }
                        }
                    }
                    if (clsLoadClass != null) {
                        try {
                            Toolkit unused = Toolkit.toolkit = (Toolkit) clsLoadClass.newInstance();
                            if (GraphicsEnvironment.isHeadless()) {
                                Toolkit unused2 = Toolkit.toolkit = new HeadlessToolkit(Toolkit.toolkit);
                            }
                        } catch (IllegalAccessException e4) {
                            throw new AWTError("Could not access Toolkit: " + property);
                        } catch (InstantiationException e5) {
                            throw new AWTError("Could not instantiate Toolkit: " + property);
                        }
                    }
                    return null;
                }
            });
            loadAssistiveTechnologies();
        }
        return toolkit;
    }

    public Image createImage(byte[] bArr) {
        return createImage(bArr, 0, bArr.length);
    }

    public PrintJob getPrintJob(Frame frame, String str, JobAttributes jobAttributes, PageAttributes pageAttributes) {
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().getPrintJob(frame, str, jobAttributes, pageAttributes);
        }
        return getPrintJob(frame, str, null);
    }

    public Clipboard getSystemSelection() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().getSystemSelection();
        }
        GraphicsEnvironment.checkHeadless();
        return null;
    }

    public int getMenuShortcutKeyMask() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        return 2;
    }

    public boolean getLockingKeyState(int i2) throws UnsupportedOperationException {
        GraphicsEnvironment.checkHeadless();
        if (i2 != 20 && i2 != 144 && i2 != 145 && i2 != 262) {
            throw new IllegalArgumentException("invalid key for Toolkit.getLockingKeyState");
        }
        throw new UnsupportedOperationException("Toolkit.getLockingKeyState");
    }

    public void setLockingKeyState(int i2, boolean z2) throws UnsupportedOperationException {
        GraphicsEnvironment.checkHeadless();
        if (i2 != 20 && i2 != 144 && i2 != 145 && i2 != 262) {
            throw new IllegalArgumentException("invalid key for Toolkit.setLockingKeyState");
        }
        throw new UnsupportedOperationException("Toolkit.setLockingKeyState");
    }

    protected static Container getNativeContainer(Component component) {
        return component.getNativeContainer();
    }

    public Cursor createCustomCursor(Image image, Point point, String str) throws IndexOutOfBoundsException, HeadlessException {
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().createCustomCursor(image, point, str);
        }
        return new Cursor(0);
    }

    public Dimension getBestCursorSize(int i2, int i3) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().getBestCursorSize(i2, i3);
        }
        return new Dimension(0, 0);
    }

    public int getMaximumCursorColors() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().getMaximumCursorColors();
        }
        return 0;
    }

    public boolean isFrameStateSupported(int i2) throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        if (this != getDefaultToolkit()) {
            return getDefaultToolkit().isFrameStateSupported(i2);
        }
        return i2 == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void setPlatformResources(ResourceBundle resourceBundle) {
        platformResources = resourceBundle;
    }

    static void loadLibraries() {
        if (!loaded) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.Toolkit.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    System.loadLibrary("awt");
                    return null;
                }
            });
            loaded = true;
        }
    }

    public static String getProperty(String str, String str2) {
        if (platformResources != null) {
            try {
                return platformResources.getString(str);
            } catch (MissingResourceException e2) {
            }
        }
        if (resources != null) {
            try {
                return resources.getString(str);
            } catch (MissingResourceException e3) {
            }
        }
        return str2;
    }

    public final EventQueue getSystemEventQueue() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.CHECK_AWT_EVENTQUEUE_PERMISSION);
        }
        return getSystemEventQueueImpl();
    }

    static EventQueue getEventQueue() {
        return getDefaultToolkit().getSystemEventQueueImpl();
    }

    public <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
        return null;
    }

    public final synchronized Object getDesktopProperty(String str) {
        if (this instanceof HeadlessToolkit) {
            return ((HeadlessToolkit) this).getUnderlyingToolkit().getDesktopProperty(str);
        }
        if (this.desktopProperties.isEmpty()) {
            initializeDesktopProperties();
        }
        if (str.equals("awt.dynamicLayoutSupported")) {
            return getDefaultToolkit().lazilyLoadDesktopProperty(str);
        }
        Object objClone = this.desktopProperties.get(str);
        if (objClone == null) {
            objClone = lazilyLoadDesktopProperty(str);
            if (objClone != null) {
                setDesktopProperty(str, objClone);
            }
        }
        if (objClone instanceof RenderingHints) {
            objClone = ((RenderingHints) objClone).clone();
        }
        return objClone;
    }

    protected final void setDesktopProperty(String str, Object obj) {
        Object obj2;
        if (this instanceof HeadlessToolkit) {
            ((HeadlessToolkit) this).getUnderlyingToolkit().setDesktopProperty(str, obj);
            return;
        }
        synchronized (this) {
            obj2 = this.desktopProperties.get(str);
            this.desktopProperties.put(str, obj);
        }
        if (obj2 != null || obj != null) {
            this.desktopPropsSupport.firePropertyChange(str, obj2, obj);
        }
    }

    protected Object lazilyLoadDesktopProperty(String str) {
        return null;
    }

    protected void initializeDesktopProperties() {
    }

    public void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.desktopPropsSupport.addPropertyChangeListener(str, propertyChangeListener);
    }

    public void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
        this.desktopPropsSupport.removePropertyChangeListener(str, propertyChangeListener);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.desktopPropsSupport.getPropertyChangeListeners();
    }

    public PropertyChangeListener[] getPropertyChangeListeners(String str) {
        return this.desktopPropsSupport.getPropertyChangeListeners(str);
    }

    public boolean isAlwaysOnTopSupported() {
        return true;
    }

    private static AWTEventListener deProxyAWTEventListener(AWTEventListener aWTEventListener) {
        AWTEventListener listener = aWTEventListener;
        if (listener == null) {
            return null;
        }
        if (aWTEventListener instanceof AWTEventListenerProxy) {
            listener = ((AWTEventListenerProxy) aWTEventListener).getListener();
        }
        return listener;
    }

    public void addAWTEventListener(AWTEventListener aWTEventListener, long j2) {
        AWTEventListener aWTEventListenerDeProxyAWTEventListener = deProxyAWTEventListener(aWTEventListener);
        if (aWTEventListenerDeProxyAWTEventListener == null) {
            return;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
        }
        synchronized (this) {
            SelectiveAWTEventListener selectiveAWTEventListener = this.listener2SelectiveListener.get(aWTEventListenerDeProxyAWTEventListener);
            if (selectiveAWTEventListener == null) {
                selectiveAWTEventListener = new SelectiveAWTEventListener(aWTEventListenerDeProxyAWTEventListener, j2);
                this.listener2SelectiveListener.put(aWTEventListenerDeProxyAWTEventListener, selectiveAWTEventListener);
                this.eventListener = ToolkitEventMulticaster.add(this.eventListener, selectiveAWTEventListener);
            }
            selectiveAWTEventListener.orEventMasks(j2);
            enabledOnToolkitMask |= j2;
            long j3 = j2;
            for (int i2 = 0; i2 < 64 && j3 != 0; i2++) {
                if ((j3 & 1) != 0) {
                    int[] iArr = this.calls;
                    int i3 = i2;
                    iArr[i3] = iArr[i3] + 1;
                }
                j3 >>>= 1;
            }
        }
    }

    public void removeAWTEventListener(AWTEventListener aWTEventListener) {
        AWTEventListener aWTEventListenerDeProxyAWTEventListener = deProxyAWTEventListener(aWTEventListener);
        if (aWTEventListener == null) {
            return;
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
        }
        synchronized (this) {
            SelectiveAWTEventListener selectiveAWTEventListener = this.listener2SelectiveListener.get(aWTEventListenerDeProxyAWTEventListener);
            if (selectiveAWTEventListener != null) {
                this.listener2SelectiveListener.remove(aWTEventListenerDeProxyAWTEventListener);
                int[] calls = selectiveAWTEventListener.getCalls();
                for (int i2 = 0; i2 < 64; i2++) {
                    int[] iArr = this.calls;
                    int i3 = i2;
                    iArr[i3] = iArr[i3] - calls[i2];
                    if (!$assertionsDisabled && this.calls[i2] < 0) {
                        throw new AssertionError((Object) "Negative Listeners count");
                    }
                    if (this.calls[i2] == 0) {
                        enabledOnToolkitMask &= (1 << i2) ^ (-1);
                    }
                }
            }
            this.eventListener = ToolkitEventMulticaster.remove(this.eventListener, selectiveAWTEventListener == null ? aWTEventListenerDeProxyAWTEventListener : selectiveAWTEventListener);
        }
    }

    static boolean enabledOnToolkit(long j2) {
        return (enabledOnToolkitMask & j2) != 0;
    }

    synchronized int countAWTEventListeners(long j2) {
        int i2 = 0;
        while (j2 != 0) {
            j2 >>>= 1;
            i2++;
        }
        return this.calls[i2 - 1];
    }

    public AWTEventListener[] getAWTEventListeners() {
        AWTEventListener[] aWTEventListenerArr;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
        }
        synchronized (this) {
            EventListener[] listeners = ToolkitEventMulticaster.getListeners(this.eventListener, AWTEventListener.class);
            aWTEventListenerArr = new AWTEventListener[listeners.length];
            for (int i2 = 0; i2 < listeners.length; i2++) {
                SelectiveAWTEventListener selectiveAWTEventListener = (SelectiveAWTEventListener) listeners[i2];
                aWTEventListenerArr[i2] = new AWTEventListenerProxy(selectiveAWTEventListener.getEventMask(), selectiveAWTEventListener.getListener());
            }
        }
        return aWTEventListenerArr;
    }

    public AWTEventListener[] getAWTEventListeners(long j2) {
        AWTEventListener[] aWTEventListenerArr;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.ALL_AWT_EVENTS_PERMISSION);
        }
        synchronized (this) {
            EventListener[] listeners = ToolkitEventMulticaster.getListeners(this.eventListener, AWTEventListener.class);
            ArrayList arrayList = new ArrayList(listeners.length);
            for (EventListener eventListener : listeners) {
                SelectiveAWTEventListener selectiveAWTEventListener = (SelectiveAWTEventListener) eventListener;
                if ((selectiveAWTEventListener.getEventMask() & j2) == j2) {
                    arrayList.add(new AWTEventListenerProxy(selectiveAWTEventListener.getEventMask(), selectiveAWTEventListener.getListener()));
                }
            }
            aWTEventListenerArr = (AWTEventListener[]) arrayList.toArray(new AWTEventListener[0]);
        }
        return aWTEventListenerArr;
    }

    void notifyAWTEventListeners(AWTEvent aWTEvent) {
        if (this instanceof HeadlessToolkit) {
            ((HeadlessToolkit) this).getUnderlyingToolkit().notifyAWTEventListeners(aWTEvent);
            return;
        }
        AWTEventListener aWTEventListener = this.eventListener;
        if (aWTEventListener != null) {
            aWTEventListener.eventDispatched(aWTEvent);
        }
    }

    /* loaded from: rt.jar:java/awt/Toolkit$ToolkitEventMulticaster.class */
    private static class ToolkitEventMulticaster extends AWTEventMulticaster implements AWTEventListener {
        ToolkitEventMulticaster(AWTEventListener aWTEventListener, AWTEventListener aWTEventListener2) {
            super(aWTEventListener, aWTEventListener2);
        }

        static AWTEventListener add(AWTEventListener aWTEventListener, AWTEventListener aWTEventListener2) {
            return aWTEventListener == null ? aWTEventListener2 : aWTEventListener2 == null ? aWTEventListener : new ToolkitEventMulticaster(aWTEventListener, aWTEventListener2);
        }

        static AWTEventListener remove(AWTEventListener aWTEventListener, AWTEventListener aWTEventListener2) {
            return (AWTEventListener) removeInternal(aWTEventListener, aWTEventListener2);
        }

        @Override // java.awt.AWTEventMulticaster
        protected EventListener remove(EventListener eventListener) {
            if (eventListener == this.f12359a) {
                return this.f12360b;
            }
            if (eventListener == this.f12360b) {
                return this.f12359a;
            }
            AWTEventListener aWTEventListener = (AWTEventListener) removeInternal(this.f12359a, eventListener);
            AWTEventListener aWTEventListener2 = (AWTEventListener) removeInternal(this.f12360b, eventListener);
            if (aWTEventListener == this.f12359a && aWTEventListener2 == this.f12360b) {
                return this;
            }
            return add(aWTEventListener, aWTEventListener2);
        }

        @Override // java.awt.event.AWTEventListener
        public void eventDispatched(AWTEvent aWTEvent) {
            ((AWTEventListener) this.f12359a).eventDispatched(aWTEvent);
            ((AWTEventListener) this.f12360b).eventDispatched(aWTEvent);
        }
    }

    /* loaded from: rt.jar:java/awt/Toolkit$SelectiveAWTEventListener.class */
    private class SelectiveAWTEventListener implements AWTEventListener {
        AWTEventListener listener;
        private long eventMask;
        int[] calls = new int[64];

        public AWTEventListener getListener() {
            return this.listener;
        }

        public long getEventMask() {
            return this.eventMask;
        }

        public int[] getCalls() {
            return this.calls;
        }

        public void orEventMasks(long j2) {
            this.eventMask |= j2;
            for (int i2 = 0; i2 < 64 && j2 != 0; i2++) {
                if ((j2 & 1) != 0) {
                    int[] iArr = this.calls;
                    int i3 = i2;
                    iArr[i3] = iArr[i3] + 1;
                }
                j2 >>>= 1;
            }
        }

        SelectiveAWTEventListener(AWTEventListener aWTEventListener, long j2) {
            this.listener = aWTEventListener;
            this.eventMask = j2;
        }

        @Override // java.awt.event.AWTEventListener
        public void eventDispatched(AWTEvent aWTEvent) {
            long j2 = this.eventMask & 1;
            long j3 = j2;
            if (j2 == 0 || aWTEvent.id < 100 || aWTEvent.id > 103) {
                long j4 = this.eventMask & 2;
                j3 = j4;
                if (j4 == 0 || aWTEvent.id < 300 || aWTEvent.id > 301) {
                    long j5 = this.eventMask & 4;
                    j3 = j5;
                    if (j5 == 0 || aWTEvent.id < 1004 || aWTEvent.id > 1005) {
                        long j6 = this.eventMask & 8;
                        j3 = j6;
                        if (j6 == 0 || aWTEvent.id < 400 || aWTEvent.id > 402) {
                            long j7 = this.eventMask & 131072;
                            j3 = j7;
                            if (j7 == 0 || aWTEvent.id != 507) {
                                long j8 = this.eventMask & 32;
                                j3 = j8;
                                if (j8 == 0 || (aWTEvent.id != 503 && aWTEvent.id != 506)) {
                                    long j9 = this.eventMask & 16;
                                    j3 = j9;
                                    if (j9 == 0 || aWTEvent.id == 503 || aWTEvent.id == 506 || aWTEvent.id == 507 || aWTEvent.id < 500 || aWTEvent.id > 507) {
                                        long j10 = this.eventMask & 64;
                                        j3 = j10;
                                        if (j10 == 0 || aWTEvent.id < 200 || aWTEvent.id > 209) {
                                            long j11 = this.eventMask & 128;
                                            j3 = j11;
                                            if (j11 == 0 || aWTEvent.id < 1001 || aWTEvent.id > 1001) {
                                                long j12 = this.eventMask & 256;
                                                j3 = j12;
                                                if (j12 == 0 || aWTEvent.id < 601 || aWTEvent.id > 601) {
                                                    long j13 = this.eventMask & 512;
                                                    j3 = j13;
                                                    if (j13 == 0 || aWTEvent.id < 701 || aWTEvent.id > 701) {
                                                        long j14 = this.eventMask & 1024;
                                                        j3 = j14;
                                                        if (j14 == 0 || aWTEvent.id < 900 || aWTEvent.id > 900) {
                                                            long j15 = this.eventMask & 2048;
                                                            j3 = j15;
                                                            if (j15 == 0 || aWTEvent.id < 1100 || aWTEvent.id > 1101) {
                                                                long j16 = this.eventMask & 8192;
                                                                j3 = j16;
                                                                if (j16 == 0 || aWTEvent.id < 800 || aWTEvent.id > 801) {
                                                                    long j17 = this.eventMask & 16384;
                                                                    j3 = j17;
                                                                    if (j17 == 0 || aWTEvent.id < 1200 || aWTEvent.id > 1200) {
                                                                        long j18 = this.eventMask & 32768;
                                                                        j3 = j18;
                                                                        if (j18 == 0 || aWTEvent.id != 1400) {
                                                                            long j19 = this.eventMask & 65536;
                                                                            j3 = j19;
                                                                            if (j19 == 0 || (aWTEvent.id != 1401 && aWTEvent.id != 1402)) {
                                                                                long j20 = this.eventMask & 262144;
                                                                                j3 = j20;
                                                                                if (j20 == 0 || aWTEvent.id != 209) {
                                                                                    long j21 = this.eventMask & 524288;
                                                                                    j3 = j21;
                                                                                    if (j21 == 0 || (aWTEvent.id != 207 && aWTEvent.id != 208)) {
                                                                                        long j22 = this.eventMask & (-2147483648L);
                                                                                        j3 = j22;
                                                                                        if (j22 == 0 || !(aWTEvent instanceof UngrabEvent)) {
                                                                                            return;
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int i2 = 0;
            long j23 = j3;
            while (j23 != 0) {
                j23 >>>= 1;
                i2++;
            }
            int i3 = i2 - 1;
            for (int i4 = 0; i4 < this.calls[i3]; i4++) {
                this.listener.eventDispatched(aWTEvent);
            }
        }
    }

    private static PropertyChangeSupport createPropertyChangeSupport(Toolkit toolkit2) {
        if ((toolkit2 instanceof SunToolkit) || (toolkit2 instanceof HeadlessToolkit)) {
            return new DesktopPropertyChangeSupport(toolkit2);
        }
        return new PropertyChangeSupport(toolkit2);
    }

    /* loaded from: rt.jar:java/awt/Toolkit$DesktopPropertyChangeSupport.class */
    private static class DesktopPropertyChangeSupport extends PropertyChangeSupport {
        private static final StringBuilder PROP_CHANGE_SUPPORT_KEY = new StringBuilder("desktop property change support key");
        private final Object source;

        public DesktopPropertyChangeSupport(Object obj) {
            super(obj);
            this.source = obj;
        }

        @Override // java.beans.PropertyChangeSupport
        public synchronized void addPropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
            PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
            if (null == propertyChangeSupport) {
                propertyChangeSupport = new PropertyChangeSupport(this.source);
                AppContext.getAppContext().put(PROP_CHANGE_SUPPORT_KEY, propertyChangeSupport);
            }
            propertyChangeSupport.addPropertyChangeListener(str, propertyChangeListener);
        }

        @Override // java.beans.PropertyChangeSupport
        public synchronized void removePropertyChangeListener(String str, PropertyChangeListener propertyChangeListener) {
            PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
            if (null != propertyChangeSupport) {
                propertyChangeSupport.removePropertyChangeListener(str, propertyChangeListener);
            }
        }

        @Override // java.beans.PropertyChangeSupport
        public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
            PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
            if (null != propertyChangeSupport) {
                return propertyChangeSupport.getPropertyChangeListeners();
            }
            return new PropertyChangeListener[0];
        }

        @Override // java.beans.PropertyChangeSupport
        public synchronized PropertyChangeListener[] getPropertyChangeListeners(String str) {
            PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
            if (null != propertyChangeSupport) {
                return propertyChangeSupport.getPropertyChangeListeners(str);
            }
            return new PropertyChangeListener[0];
        }

        @Override // java.beans.PropertyChangeSupport
        public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
            if (null == propertyChangeSupport) {
                propertyChangeSupport = new PropertyChangeSupport(this.source);
                AppContext.getAppContext().put(PROP_CHANGE_SUPPORT_KEY, propertyChangeSupport);
            }
            propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
        }

        @Override // java.beans.PropertyChangeSupport
        public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(PROP_CHANGE_SUPPORT_KEY);
            if (null != propertyChangeSupport) {
                propertyChangeSupport.removePropertyChangeListener(propertyChangeListener);
            }
        }

        @Override // java.beans.PropertyChangeSupport
        public void firePropertyChange(final PropertyChangeEvent propertyChangeEvent) {
            Object oldValue = propertyChangeEvent.getOldValue();
            Object newValue = propertyChangeEvent.getNewValue();
            propertyChangeEvent.getPropertyName();
            if (oldValue != null && newValue != null && oldValue.equals(newValue)) {
                return;
            }
            Runnable runnable = new Runnable() { // from class: java.awt.Toolkit.DesktopPropertyChangeSupport.1
                @Override // java.lang.Runnable
                public void run() {
                    PropertyChangeSupport propertyChangeSupport = (PropertyChangeSupport) AppContext.getAppContext().get(DesktopPropertyChangeSupport.PROP_CHANGE_SUPPORT_KEY);
                    if (null != propertyChangeSupport) {
                        propertyChangeSupport.firePropertyChange(propertyChangeEvent);
                    }
                }
            };
            AppContext appContext = AppContext.getAppContext();
            for (AppContext appContext2 : AppContext.getAppContexts()) {
                if (null != appContext2 && !appContext2.isDisposed()) {
                    if (appContext == appContext2) {
                        runnable.run();
                    } else {
                        SunToolkit.postEvent(appContext2, new PeerEvent(this.source, runnable, 2L));
                    }
                }
            }
        }
    }

    public boolean areExtraMouseButtonsEnabled() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        return getDefaultToolkit().areExtraMouseButtonsEnabled();
    }
}
