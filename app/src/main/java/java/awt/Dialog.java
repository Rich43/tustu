package java.awt;

import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.InvocationEvent;
import java.awt.peer.DialogPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicLong;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.awt.util.IdentityArrayList;
import sun.awt.util.IdentityLinkedList;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/awt/Dialog.class */
public class Dialog extends Window {
    boolean resizable;
    boolean undecorated;
    private transient boolean initialized;
    public static final ModalityType DEFAULT_MODALITY_TYPE;
    boolean modal;
    ModalityType modalityType;
    static transient IdentityArrayList<Dialog> modalDialogs;
    transient IdentityArrayList<Window> blockedWindows;
    String title;
    private transient ModalEventFilter modalFilter;
    private volatile transient SecondaryLoop secondaryLoop;
    volatile transient boolean isInHide;
    volatile transient boolean isInDispose;
    private static final String base = "dialog";
    private static int nameCounter;
    private static final long serialVersionUID = 5920926903803293709L;

    /* loaded from: rt.jar:java/awt/Dialog$ModalExclusionType.class */
    public enum ModalExclusionType {
        NO_EXCLUDE,
        APPLICATION_EXCLUDE,
        TOOLKIT_EXCLUDE
    }

    /* loaded from: rt.jar:java/awt/Dialog$ModalityType.class */
    public enum ModalityType {
        MODELESS,
        DOCUMENT_MODAL,
        APPLICATION_MODAL,
        TOOLKIT_MODAL
    }

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        DEFAULT_MODALITY_TYPE = ModalityType.APPLICATION_MODAL;
        modalDialogs = new IdentityArrayList<>();
        nameCounter = 0;
    }

    public Dialog(Frame frame) {
        this(frame, "", false);
    }

    public Dialog(Frame frame, boolean z2) {
        this(frame, "", z2);
    }

    public Dialog(Frame frame, String str) {
        this(frame, str, false);
    }

    public Dialog(Frame frame, String str, boolean z2) {
        this(frame, str, z2 ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
    }

    public Dialog(Frame frame, String str, boolean z2, GraphicsConfiguration graphicsConfiguration) {
        this(frame, str, z2 ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, graphicsConfiguration);
    }

    public Dialog(Dialog dialog) {
        this(dialog, "", false);
    }

    public Dialog(Dialog dialog, String str) {
        this(dialog, str, false);
    }

    public Dialog(Dialog dialog, String str, boolean z2) {
        this(dialog, str, z2 ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
    }

    public Dialog(Dialog dialog, String str, boolean z2, GraphicsConfiguration graphicsConfiguration) {
        this(dialog, str, z2 ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS, graphicsConfiguration);
    }

    public Dialog(Window window) {
        this(window, "", ModalityType.MODELESS);
    }

    public Dialog(Window window, String str) {
        this(window, str, ModalityType.MODELESS);
    }

    public Dialog(Window window, ModalityType modalityType) {
        this(window, "", modalityType);
    }

    public Dialog(Window window, String str, ModalityType modalityType) {
        super(window);
        this.resizable = true;
        this.undecorated = false;
        this.initialized = false;
        this.blockedWindows = new IdentityArrayList<>();
        this.isInHide = false;
        this.isInDispose = false;
        if (window != null && !(window instanceof Frame) && !(window instanceof Dialog)) {
            throw new IllegalArgumentException("Wrong parent window");
        }
        this.title = str;
        setModalityType(modalityType);
        SunToolkit.checkAndSetPolicy(this);
        this.initialized = true;
    }

    public Dialog(Window window, String str, ModalityType modalityType, GraphicsConfiguration graphicsConfiguration) {
        super(window, graphicsConfiguration);
        this.resizable = true;
        this.undecorated = false;
        this.initialized = false;
        this.blockedWindows = new IdentityArrayList<>();
        this.isInHide = false;
        this.isInDispose = false;
        if (window != null && !(window instanceof Frame) && !(window instanceof Dialog)) {
            throw new IllegalArgumentException("wrong owner window");
        }
        this.title = str;
        setModalityType(modalityType);
        SunToolkit.checkAndSetPolicy(this);
        this.initialized = true;
    }

    @Override // java.awt.Window, java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Dialog.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.parent != null && this.parent.getPeer() == null) {
                this.parent.addNotify();
            }
            if (this.peer == null) {
                this.peer = getToolkit().createDialog(this);
            }
            super.addNotify();
        }
    }

    public boolean isModal() {
        return isModal_NoClientCode();
    }

    final boolean isModal_NoClientCode() {
        return this.modalityType != ModalityType.MODELESS;
    }

    public void setModal(boolean z2) {
        this.modal = z2;
        setModalityType(z2 ? DEFAULT_MODALITY_TYPE : ModalityType.MODELESS);
    }

    public ModalityType getModalityType() {
        return this.modalityType;
    }

    public void setModalityType(ModalityType modalityType) {
        if (modalityType == null) {
            modalityType = ModalityType.MODELESS;
        }
        if (!Toolkit.getDefaultToolkit().isModalityTypeSupported(modalityType)) {
            modalityType = ModalityType.MODELESS;
        }
        if (this.modalityType == modalityType) {
            return;
        }
        checkModalityPermission(modalityType);
        this.modalityType = modalityType;
        this.modal = this.modalityType != ModalityType.MODELESS;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        String str2 = this.title;
        synchronized (this) {
            this.title = str;
            DialogPeer dialogPeer = (DialogPeer) this.peer;
            if (dialogPeer != null) {
                dialogPeer.setTitle(str);
            }
        }
        firePropertyChange("title", str2, str);
    }

    private boolean conditionalShow(Component component, AtomicLong atomicLong) {
        boolean z2;
        closeSplashScreen();
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                addNotify();
            }
            validateUnconditionally();
            if (this.visible) {
                toFront();
                z2 = false;
            } else {
                z2 = true;
                this.visible = true;
                if (!isModal()) {
                    checkShouldBeBlocked(this);
                } else {
                    modalDialogs.add(this);
                    modalShow();
                }
                if (component != null && atomicLong != null && isFocusable() && isEnabled() && !isModalBlocked()) {
                    atomicLong.set(Toolkit.getEventQueue().getMostRecentKeyEventTime());
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().enqueueKeyEvents(atomicLong.get(), component);
                }
                mixOnShowing();
                this.peer.setVisible(true);
                if (isModalBlocked()) {
                    this.modalBlocker.toFront();
                }
                setLocationByPlatform(false);
                for (int i2 = 0; i2 < this.ownedWindowList.size(); i2++) {
                    Window window = this.ownedWindowList.elementAt(i2).get();
                    if (window != null && window.showWithParent) {
                        window.show();
                        window.showWithParent = false;
                    }
                }
                Window.updateChildFocusableWindowState(this);
                createHierarchyEvents(1400, this, this.parent, 4L, Toolkit.enabledOnToolkit(32768L));
                if (this.componentListener != null || (this.eventMask & 1) != 0 || Toolkit.enabledOnToolkit(1L)) {
                    Toolkit.getEventQueue().postEvent(new ComponentEvent(this, 102));
                }
            }
        }
        if (z2 && (this.state & 1) == 0) {
            postWindowEvent(200);
            this.state |= 1;
        }
        return z2;
    }

    @Override // java.awt.Window, java.awt.Component
    public void setVisible(boolean z2) {
        super.setVisible(z2);
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.awt.Window, java.awt.Component
    @Deprecated
    public void show() {
        if (!this.initialized) {
            throw new IllegalStateException("The dialog component has not been initialized properly");
        }
        this.beforeFirstShow = false;
        if (!isModal()) {
            conditionalShow(null, null);
            return;
        }
        AppContext appContext = AppContext.getAppContext();
        AtomicLong atomicLong = new AtomicLong();
        Component mostRecentFocusOwner = null;
        try {
            mostRecentFocusOwner = getMostRecentFocusOwner();
            if (conditionalShow(mostRecentFocusOwner, atomicLong)) {
                this.modalFilter = ModalEventFilter.createFilterForDialog(this);
                Conditional conditional = new Conditional() { // from class: java.awt.Dialog.1
                    @Override // java.awt.Conditional
                    public boolean evaluate() {
                        return Dialog.this.windowClosingException == null;
                    }
                };
                if (this.modalityType == ModalityType.TOOLKIT_MODAL) {
                    for (AppContext appContext2 : AppContext.getAppContexts()) {
                        if (appContext2 != appContext) {
                            EventQueue eventQueue = (EventQueue) appContext2.get(AppContext.EVENT_QUEUE_KEY);
                            eventQueue.postEvent(new InvocationEvent(this, new Runnable() { // from class: java.awt.Dialog.2
                                @Override // java.lang.Runnable
                                public void run() {
                                }
                            }));
                            eventQueue.getDispatchThread().addEventFilter(this.modalFilter);
                        }
                    }
                }
                modalityPushed();
                try {
                    this.secondaryLoop = ((EventQueue) AccessController.doPrivileged(new PrivilegedAction<EventQueue>() { // from class: java.awt.Dialog.3
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        public EventQueue run() {
                            return Toolkit.getDefaultToolkit().getSystemEventQueue();
                        }
                    })).createSecondaryLoop(conditional, this.modalFilter, 0L);
                    if (!this.secondaryLoop.enter()) {
                        this.secondaryLoop = null;
                    }
                    modalityPopped();
                    if (this.modalityType == ModalityType.TOOLKIT_MODAL) {
                        for (AppContext appContext3 : AppContext.getAppContexts()) {
                            if (appContext3 != appContext) {
                                ((EventQueue) appContext3.get(AppContext.EVENT_QUEUE_KEY)).getDispatchThread().removeEventFilter(this.modalFilter);
                            }
                        }
                    }
                    if (this.windowClosingException != null) {
                        this.windowClosingException.fillInStackTrace();
                        throw this.windowClosingException;
                    }
                } catch (Throwable th) {
                    modalityPopped();
                    throw th;
                }
            }
            if (mostRecentFocusOwner != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().dequeueKeyEvents(atomicLong.get(), mostRecentFocusOwner);
            }
        } catch (Throwable th2) {
            if (mostRecentFocusOwner != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().dequeueKeyEvents(atomicLong.get(), mostRecentFocusOwner);
            }
            throw th2;
        }
    }

    final void modalityPushed() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            ((SunToolkit) defaultToolkit).notifyModalityPushed(this);
        }
    }

    final void modalityPopped() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            ((SunToolkit) defaultToolkit).notifyModalityPopped(this);
        }
    }

    void interruptBlocking() {
        if (isModal()) {
            disposeImpl();
        } else if (this.windowClosingException != null) {
            this.windowClosingException.fillInStackTrace();
            this.windowClosingException.printStackTrace();
            this.windowClosingException = null;
        }
    }

    private void hideAndDisposePreHandler() {
        this.isInHide = true;
        synchronized (getTreeLock()) {
            if (this.secondaryLoop != null) {
                modalHide();
                if (this.modalFilter != null) {
                    this.modalFilter.disable();
                }
                modalDialogs.remove(this);
            }
        }
    }

    private void hideAndDisposeHandler() {
        if (this.secondaryLoop != null) {
            this.secondaryLoop.exit();
            this.secondaryLoop = null;
        }
        this.isInHide = false;
    }

    @Override // java.awt.Window, java.awt.Component
    @Deprecated
    public void hide() {
        hideAndDisposePreHandler();
        super.hide();
        if (!this.isInDispose) {
            hideAndDisposeHandler();
        }
    }

    @Override // java.awt.Window
    void doDispose() {
        this.isInDispose = true;
        super.doDispose();
        hideAndDisposeHandler();
        this.isInDispose = false;
    }

    @Override // java.awt.Window
    public void toBack() {
        super.toBack();
        if (this.visible) {
            synchronized (getTreeLock()) {
                Iterator<Window> it = this.blockedWindows.iterator();
                while (it.hasNext()) {
                    it.next().toBack_NoClientCode();
                }
            }
        }
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public void setResizable(boolean z2) {
        boolean z3 = false;
        synchronized (this) {
            this.resizable = z2;
            DialogPeer dialogPeer = (DialogPeer) this.peer;
            if (dialogPeer != null) {
                dialogPeer.setResizable(z2);
                z3 = true;
            }
        }
        if (z3) {
            invalidateIfValid();
        }
    }

    public void setUndecorated(boolean z2) {
        synchronized (getTreeLock()) {
            if (isDisplayable()) {
                throw new IllegalComponentStateException("The dialog is displayable.");
            }
            if (!z2) {
                if (getOpacity() < 1.0f) {
                    throw new IllegalComponentStateException("The dialog is not opaque");
                }
                if (getShape() != null) {
                    throw new IllegalComponentStateException("The dialog does not have a default shape");
                }
                Color background = getBackground();
                if (background != null && background.getAlpha() < 255) {
                    throw new IllegalComponentStateException("The dialog background color is not opaque");
                }
            }
            this.undecorated = z2;
        }
    }

    public boolean isUndecorated() {
        return this.undecorated;
    }

    @Override // java.awt.Window
    public void setOpacity(float f2) {
        synchronized (getTreeLock()) {
            if (f2 < 1.0f) {
                if (!isUndecorated()) {
                    throw new IllegalComponentStateException("The dialog is decorated");
                }
            }
            super.setOpacity(f2);
        }
    }

    @Override // java.awt.Window
    public void setShape(Shape shape) {
        synchronized (getTreeLock()) {
            if (shape != null) {
                if (!isUndecorated()) {
                    throw new IllegalComponentStateException("The dialog is decorated");
                }
            }
            super.setShape(shape);
        }
    }

    @Override // java.awt.Window, java.awt.Component
    public void setBackground(Color color) {
        synchronized (getTreeLock()) {
            if (color != null) {
                if (color.getAlpha() < 255 && !isUndecorated()) {
                    throw new IllegalComponentStateException("The dialog is decorated");
                }
            }
            super.setBackground(color);
        }
    }

    @Override // java.awt.Container, java.awt.Component
    protected String paramString() {
        String str = super.paramString() + "," + ((Object) this.modalityType);
        if (this.title != null) {
            str = str + ",title=" + this.title;
        }
        return str;
    }

    void modalShow() {
        Window window;
        IdentityArrayList identityArrayList = new IdentityArrayList();
        Iterator<Dialog> it = modalDialogs.iterator();
        while (it.hasNext()) {
            Dialog next = it.next();
            if (next.shouldBlock(this)) {
                Window owner_NoClientCode = next;
                while (true) {
                    window = owner_NoClientCode;
                    if (window == null || window == this) {
                        break;
                    } else {
                        owner_NoClientCode = window.getOwner_NoClientCode();
                    }
                }
                if (window == this || !shouldBlock(next) || this.modalityType.compareTo(next.getModalityType()) < 0) {
                    identityArrayList.add(next);
                }
            }
        }
        for (int i2 = 0; i2 < identityArrayList.size(); i2++) {
            Dialog dialog = (Dialog) identityArrayList.get(i2);
            if (dialog.isModalBlocked()) {
                Dialog modalBlocker = dialog.getModalBlocker();
                if (!identityArrayList.contains(modalBlocker)) {
                    identityArrayList.add(i2 + 1, modalBlocker);
                }
            }
        }
        if (identityArrayList.size() > 0) {
            ((Dialog) identityArrayList.get(0)).blockWindow(this);
        }
        IdentityArrayList identityArrayList2 = new IdentityArrayList(identityArrayList);
        for (int i3 = 0; i3 < identityArrayList2.size(); i3++) {
            for (Window window2 : ((Window) identityArrayList2.get(i3)).getOwnedWindows_NoClientCode()) {
                identityArrayList2.add(window2);
            }
        }
        java.util.List<Window> identityLinkedList = new IdentityLinkedList<>();
        Iterator<Window> it2 = Window.getAllUnblockedWindows().iterator();
        while (it2.hasNext()) {
            Window next2 = it2.next();
            if (shouldBlock(next2) && !identityArrayList2.contains(next2)) {
                if ((next2 instanceof Dialog) && ((Dialog) next2).isModal_NoClientCode()) {
                    Dialog dialog2 = (Dialog) next2;
                    if (!dialog2.shouldBlock(this) || modalDialogs.indexOf(dialog2) <= modalDialogs.indexOf(this)) {
                    }
                }
                identityLinkedList.add(next2);
            }
        }
        blockWindows(identityLinkedList);
        if (!isModalBlocked()) {
            updateChildrenBlocking();
        }
    }

    void modalHide() {
        IdentityArrayList identityArrayList = new IdentityArrayList();
        int size = this.blockedWindows.size();
        for (int i2 = 0; i2 < size; i2++) {
            Window window = this.blockedWindows.get(0);
            identityArrayList.add(window);
            unblockWindow(window);
        }
        for (int i3 = 0; i3 < size; i3++) {
            Window window2 = (Window) identityArrayList.get(i3);
            if ((window2 instanceof Dialog) && ((Dialog) window2).isModal_NoClientCode()) {
                ((Dialog) window2).modalShow();
            } else {
                checkShouldBeBlocked(window2);
            }
        }
    }

    boolean shouldBlock(Window window) {
        Window window2;
        Dialog dialog;
        if (isVisible_NoClientCode()) {
            if ((!window.isVisible_NoClientCode() && !window.isInShow) || this.isInHide || window == this || !isModal_NoClientCode()) {
                return false;
            }
            if ((window instanceof Dialog) && ((Dialog) window).isInHide) {
                return false;
            }
            Dialog modalBlocker = this;
            while (true) {
                Dialog dialog2 = modalBlocker;
                if (dialog2 != null) {
                    Container parent_NoClientCode = window;
                    while (true) {
                        dialog = parent_NoClientCode;
                        if (dialog == null || dialog == dialog2) {
                            break;
                        }
                        parent_NoClientCode = dialog.getParent_NoClientCode();
                    }
                    if (dialog == dialog2) {
                        return false;
                    }
                    modalBlocker = dialog2.getModalBlocker();
                } else {
                    switch (this.modalityType) {
                        case DOCUMENT_MODAL:
                            if (window.isModalExcluded(ModalExclusionType.APPLICATION_EXCLUDE)) {
                                Container parent_NoClientCode2 = this;
                                while (true) {
                                    window2 = parent_NoClientCode2;
                                    if (window2 != null && window2 != window) {
                                        parent_NoClientCode2 = window2.getParent_NoClientCode();
                                    }
                                }
                                if (window2 == window) {
                                }
                            } else if (getDocumentRoot() == window.getDocumentRoot()) {
                            }
                            break;
                        case APPLICATION_MODAL:
                            if (window.isModalExcluded(ModalExclusionType.APPLICATION_EXCLUDE) || this.appContext != window.appContext) {
                            }
                            break;
                        case TOOLKIT_MODAL:
                            if (!window.isModalExcluded(ModalExclusionType.TOOLKIT_EXCLUDE)) {
                            }
                            break;
                    }
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    void blockWindow(Window window) {
        if (!window.isModalBlocked()) {
            window.setModalBlocked(this, true, true);
            this.blockedWindows.add(window);
        }
    }

    void blockWindows(java.util.List<Window> list) {
        DialogPeer dialogPeer = (DialogPeer) this.peer;
        if (dialogPeer == null) {
            return;
        }
        Iterator<Window> it = list.iterator();
        while (it.hasNext()) {
            Window next = it.next();
            if (!next.isModalBlocked()) {
                next.setModalBlocked(this, true, false);
            } else {
                it.remove();
            }
        }
        dialogPeer.blockWindows(list);
        this.blockedWindows.addAll(list);
    }

    void unblockWindow(Window window) {
        if (window.isModalBlocked() && this.blockedWindows.contains(window)) {
            this.blockedWindows.remove(window);
            window.setModalBlocked(this, false, true);
        }
    }

    static void checkShouldBeBlocked(Window window) {
        synchronized (window.getTreeLock()) {
            int i2 = 0;
            while (true) {
                if (i2 >= modalDialogs.size()) {
                    break;
                }
                Dialog dialog = modalDialogs.get(i2);
                if (!dialog.shouldBlock(window)) {
                    i2++;
                } else {
                    dialog.blockWindow(window);
                    break;
                }
            }
        }
    }

    private void checkModalityPermission(ModalityType modalityType) {
        SecurityManager securityManager;
        if (modalityType == ModalityType.TOOLKIT_MODAL && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(SecurityConstants.AWT.TOOLKIT_MODALITY_PERMISSION);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        ModalityType modalityType = (ModalityType) fields.get("modalityType", (Object) null);
        try {
            checkModalityPermission(modalityType);
        } catch (AccessControlException e2) {
            modalityType = DEFAULT_MODALITY_TYPE;
        }
        if (modalityType == null) {
            this.modal = fields.get("modal", false);
            setModal(this.modal);
        } else {
            this.modalityType = modalityType;
        }
        this.resizable = fields.get("resizable", true);
        this.undecorated = fields.get("undecorated", false);
        this.title = (String) fields.get("title", "");
        this.blockedWindows = new IdentityArrayList<>();
        SunToolkit.checkAndSetPolicy(this);
        this.initialized = true;
    }

    @Override // java.awt.Window, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTDialog();
        }
        return this.accessibleContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:java/awt/Dialog$AccessibleAWTDialog.class */
    public class AccessibleAWTDialog extends Window.AccessibleAWTWindow {
        private static final long serialVersionUID = 4837230331833941201L;

        protected AccessibleAWTDialog() {
            super();
        }

        @Override // java.awt.Window.AccessibleAWTWindow, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.DIALOG;
        }

        @Override // java.awt.Window.AccessibleAWTWindow, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (Dialog.this.getFocusOwner() != null) {
                accessibleStateSet.add(AccessibleState.ACTIVE);
            }
            if (Dialog.this.isModal()) {
                accessibleStateSet.add(AccessibleState.MODAL);
            }
            if (Dialog.this.isResizable()) {
                accessibleStateSet.add(AccessibleState.RESIZABLE);
            }
            return accessibleStateSet;
        }
    }
}
