package java.awt;

import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.peer.FramePeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import sun.awt.AWTAccessor;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/Frame.class */
public class Frame extends Window implements MenuContainer {

    @Deprecated
    public static final int DEFAULT_CURSOR = 0;

    @Deprecated
    public static final int CROSSHAIR_CURSOR = 1;

    @Deprecated
    public static final int TEXT_CURSOR = 2;

    @Deprecated
    public static final int WAIT_CURSOR = 3;

    @Deprecated
    public static final int SW_RESIZE_CURSOR = 4;

    @Deprecated
    public static final int SE_RESIZE_CURSOR = 5;

    @Deprecated
    public static final int NW_RESIZE_CURSOR = 6;

    @Deprecated
    public static final int NE_RESIZE_CURSOR = 7;

    @Deprecated
    public static final int N_RESIZE_CURSOR = 8;

    @Deprecated
    public static final int S_RESIZE_CURSOR = 9;

    @Deprecated
    public static final int W_RESIZE_CURSOR = 10;

    @Deprecated
    public static final int E_RESIZE_CURSOR = 11;

    @Deprecated
    public static final int HAND_CURSOR = 12;

    @Deprecated
    public static final int MOVE_CURSOR = 13;
    public static final int NORMAL = 0;
    public static final int ICONIFIED = 1;
    public static final int MAXIMIZED_HORIZ = 2;
    public static final int MAXIMIZED_VERT = 4;
    public static final int MAXIMIZED_BOTH = 6;
    Rectangle maximizedBounds;
    String title;
    MenuBar menuBar;
    boolean resizable;
    boolean undecorated;
    boolean mbManagement;
    private int state;
    Vector<Window> ownedWindows;
    private static final String base = "frame";
    private static int nameCounter = 0;
    private static final long serialVersionUID = 2673458971256075116L;
    private int frameSerializedDataVersion;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setFrameAccessor(new AWTAccessor.FrameAccessor() { // from class: java.awt.Frame.1
            @Override // sun.awt.AWTAccessor.FrameAccessor
            public void setExtendedState(Frame frame, int i2) {
                synchronized (frame.getObjectLock()) {
                    frame.state = i2;
                }
            }

            @Override // sun.awt.AWTAccessor.FrameAccessor
            public int getExtendedState(Frame frame) {
                int i2;
                synchronized (frame.getObjectLock()) {
                    i2 = frame.state;
                }
                return i2;
            }

            @Override // sun.awt.AWTAccessor.FrameAccessor
            public Rectangle getMaximizedBounds(Frame frame) {
                Rectangle rectangle;
                synchronized (frame.getObjectLock()) {
                    rectangle = frame.maximizedBounds;
                }
                return rectangle;
            }
        });
    }

    public Frame() throws HeadlessException {
        this("");
    }

    public Frame(GraphicsConfiguration graphicsConfiguration) {
        this("", graphicsConfiguration);
    }

    public Frame(String str) throws HeadlessException {
        this.title = "Untitled";
        this.resizable = true;
        this.undecorated = false;
        this.mbManagement = false;
        this.state = 0;
        this.frameSerializedDataVersion = 1;
        init(str, null);
    }

    public Frame(String str, GraphicsConfiguration graphicsConfiguration) {
        super(graphicsConfiguration);
        this.title = "Untitled";
        this.resizable = true;
        this.undecorated = false;
        this.mbManagement = false;
        this.state = 0;
        this.frameSerializedDataVersion = 1;
        init(str, graphicsConfiguration);
    }

    private void init(String str, GraphicsConfiguration graphicsConfiguration) {
        this.title = str;
        SunToolkit.checkAndSetPolicy(this);
    }

    @Override // java.awt.Window, java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Frame.class) {
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
            if (this.peer == null) {
                this.peer = getToolkit().createFrame(this);
            }
            FramePeer framePeer = (FramePeer) this.peer;
            MenuBar menuBar = this.menuBar;
            if (menuBar != null) {
                this.mbManagement = true;
                menuBar.addNotify();
                framePeer.setMenuBar(menuBar);
            }
            framePeer.setMaximizedBounds(this.maximizedBounds);
            super.addNotify();
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        String str2 = this.title;
        if (str == null) {
            str = "";
        }
        synchronized (this) {
            this.title = str;
            FramePeer framePeer = (FramePeer) this.peer;
            if (framePeer != null) {
                framePeer.setTitle(str);
            }
        }
        firePropertyChange("title", str2, str);
    }

    public Image getIconImage() {
        java.util.List<Image> list = this.icons;
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override // java.awt.Window
    public void setIconImage(Image image) {
        super.setIconImage(image);
    }

    public MenuBar getMenuBar() {
        return this.menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        synchronized (getTreeLock()) {
            if (this.menuBar == menuBar) {
                return;
            }
            if (menuBar != null && menuBar.parent != null) {
                menuBar.parent.remove(menuBar);
            }
            if (this.menuBar != null) {
                remove(this.menuBar);
            }
            this.menuBar = menuBar;
            if (this.menuBar != null) {
                this.menuBar.parent = this;
                FramePeer framePeer = (FramePeer) this.peer;
                if (framePeer != null) {
                    this.mbManagement = true;
                    this.menuBar.addNotify();
                    invalidateIfValid();
                    framePeer.setMenuBar(this.menuBar);
                }
            }
        }
    }

    public boolean isResizable() {
        return this.resizable;
    }

    public void setResizable(boolean z2) {
        boolean z3 = this.resizable;
        boolean z4 = false;
        synchronized (this) {
            this.resizable = z2;
            FramePeer framePeer = (FramePeer) this.peer;
            if (framePeer != null) {
                framePeer.setResizable(z2);
                z4 = true;
            }
        }
        if (z4) {
            invalidateIfValid();
        }
        firePropertyChange("resizable", z3, z2);
    }

    public synchronized void setState(int i2) {
        int extendedState = getExtendedState();
        if (i2 == 1 && (extendedState & 1) == 0) {
            setExtendedState(extendedState | 1);
        } else if (i2 == 0 && (extendedState & 1) != 0) {
            setExtendedState(extendedState & (-2));
        }
    }

    public void setExtendedState(int i2) {
        if (!isFrameStateSupported(i2)) {
            return;
        }
        synchronized (getObjectLock()) {
            this.state = i2;
        }
        FramePeer framePeer = (FramePeer) this.peer;
        if (framePeer != null) {
            framePeer.setState(i2);
        }
    }

    private boolean isFrameStateSupported(int i2) {
        if (!getToolkit().isFrameStateSupported(i2)) {
            if ((i2 & 1) != 0 && !getToolkit().isFrameStateSupported(1)) {
                return false;
            }
            return getToolkit().isFrameStateSupported(i2 & (-2));
        }
        return true;
    }

    public synchronized int getState() {
        return (getExtendedState() & 1) != 0 ? 1 : 0;
    }

    public int getExtendedState() {
        int i2;
        synchronized (getObjectLock()) {
            i2 = this.state;
        }
        return i2;
    }

    public void setMaximizedBounds(Rectangle rectangle) {
        synchronized (getObjectLock()) {
            this.maximizedBounds = rectangle;
        }
        FramePeer framePeer = (FramePeer) this.peer;
        if (framePeer != null) {
            framePeer.setMaximizedBounds(rectangle);
        }
    }

    public Rectangle getMaximizedBounds() {
        Rectangle rectangle;
        synchronized (getObjectLock()) {
            rectangle = this.maximizedBounds;
        }
        return rectangle;
    }

    public void setUndecorated(boolean z2) {
        synchronized (getTreeLock()) {
            if (isDisplayable()) {
                throw new IllegalComponentStateException("The frame is displayable.");
            }
            if (!z2) {
                if (getOpacity() < 1.0f) {
                    throw new IllegalComponentStateException("The frame is not opaque");
                }
                if (getShape() != null) {
                    throw new IllegalComponentStateException("The frame does not have a default shape");
                }
                Color background = getBackground();
                if (background != null && background.getAlpha() < 255) {
                    throw new IllegalComponentStateException("The frame background color is not opaque");
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
                    throw new IllegalComponentStateException("The frame is decorated");
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
                    throw new IllegalComponentStateException("The frame is decorated");
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
                    throw new IllegalComponentStateException("The frame is decorated");
                }
            }
            super.setBackground(color);
        }
    }

    @Override // java.awt.Component, java.awt.MenuContainer
    public void remove(MenuComponent menuComponent) {
        if (menuComponent == null) {
            return;
        }
        synchronized (getTreeLock()) {
            if (menuComponent == this.menuBar) {
                this.menuBar = null;
                FramePeer framePeer = (FramePeer) this.peer;
                if (framePeer != null) {
                    this.mbManagement = true;
                    invalidateIfValid();
                    framePeer.setMenuBar(null);
                    menuComponent.removeNotify();
                }
                menuComponent.parent = null;
            } else {
                super.remove(menuComponent);
            }
        }
    }

    @Override // java.awt.Window, java.awt.Container, java.awt.Component
    public void removeNotify() {
        synchronized (getTreeLock()) {
            FramePeer framePeer = (FramePeer) this.peer;
            if (framePeer != null) {
                getState();
                if (this.menuBar != null) {
                    this.mbManagement = true;
                    framePeer.setMenuBar(null);
                    this.menuBar.removeNotify();
                }
            }
            super.removeNotify();
        }
    }

    @Override // java.awt.Window, java.awt.Container
    void postProcessKeyEvent(KeyEvent keyEvent) {
        if (this.menuBar != null && this.menuBar.handleShortcut(keyEvent)) {
            keyEvent.consume();
        } else {
            super.postProcessKeyEvent(keyEvent);
        }
    }

    @Override // java.awt.Container, java.awt.Component
    protected String paramString() {
        String strParamString = super.paramString();
        if (this.title != null) {
            strParamString = strParamString + ",title=" + this.title;
        }
        if (this.resizable) {
            strParamString = strParamString + ",resizable";
        }
        int extendedState = getExtendedState();
        if (extendedState == 0) {
            strParamString = strParamString + ",normal";
        } else {
            if ((extendedState & 1) != 0) {
                strParamString = strParamString + ",iconified";
            }
            if ((extendedState & 6) == 6) {
                strParamString = strParamString + ",maximized";
            } else if ((extendedState & 2) != 0) {
                strParamString = strParamString + ",maximized_horiz";
            } else if ((extendedState & 4) != 0) {
                strParamString = strParamString + ",maximized_vert";
            }
        }
        return strParamString;
    }

    @Deprecated
    public void setCursor(int i2) {
        if (i2 < 0 || i2 > 13) {
            throw new IllegalArgumentException("illegal cursor type");
        }
        setCursor(Cursor.getPredefinedCursor(i2));
    }

    @Deprecated
    public int getCursorType() {
        return getCursor().getType();
    }

    public static Frame[] getFrames() {
        Window[] windows = Window.getWindows();
        int i2 = 0;
        for (Window window : windows) {
            if (window instanceof Frame) {
                i2++;
            }
        }
        Frame[] frameArr = new Frame[i2];
        int i3 = 0;
        for (Window window2 : windows) {
            if (window2 instanceof Frame) {
                int i4 = i3;
                i3++;
                frameArr[i4] = (Frame) window2;
            }
        }
        return frameArr;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.icons != null && this.icons.size() > 0) {
            Image image = this.icons.get(0);
            if (image instanceof Serializable) {
                objectOutputStream.writeObject(image);
                return;
            }
        }
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException, HeadlessException {
        objectInputStream.defaultReadObject();
        try {
            Image image = (Image) objectInputStream.readObject();
            if (this.icons == null) {
                this.icons = new ArrayList();
                this.icons.add(image);
            }
        } catch (OptionalDataException e2) {
            if (!e2.eof) {
                throw e2;
            }
        }
        if (this.menuBar != null) {
            this.menuBar.parent = this;
        }
        if (this.ownedWindows != null) {
            for (int i2 = 0; i2 < this.ownedWindows.size(); i2++) {
                connectOwnedWindow(this.ownedWindows.elementAt(i2));
            }
            this.ownedWindows = null;
        }
    }

    @Override // java.awt.Window, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTFrame();
        }
        return this.accessibleContext;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:java/awt/Frame$AccessibleAWTFrame.class */
    public class AccessibleAWTFrame extends Window.AccessibleAWTWindow {
        private static final long serialVersionUID = -6172960752956030250L;

        protected AccessibleAWTFrame() {
            super();
        }

        @Override // java.awt.Window.AccessibleAWTWindow, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.FRAME;
        }

        @Override // java.awt.Window.AccessibleAWTWindow, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (Frame.this.getFocusOwner() != null) {
                accessibleStateSet.add(AccessibleState.ACTIVE);
            }
            if (Frame.this.isResizable()) {
                accessibleStateSet.add(AccessibleState.RESIZABLE);
            }
            return accessibleStateSet;
        }
    }
}
