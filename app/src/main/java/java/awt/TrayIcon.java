package java.awt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.peer.TrayIconPeer;
import java.security.AccessControlContext;
import java.security.AccessController;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.HeadlessToolkit;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/TrayIcon.class */
public class TrayIcon {
    private Image image;
    private String tooltip;
    private PopupMenu popup;
    private boolean autosize;
    private int id;
    private String actionCommand;
    private transient TrayIconPeer peer;
    transient MouseListener mouseListener;
    transient MouseMotionListener mouseMotionListener;
    transient ActionListener actionListener;
    private final AccessControlContext acc;

    /* loaded from: rt.jar:java/awt/TrayIcon$MessageType.class */
    public enum MessageType {
        ERROR,
        WARNING,
        INFO,
        NONE
    }

    private static native void initIDs();

    final AccessControlContext getAccessControlContext() {
        if (this.acc == null) {
            throw new SecurityException("TrayIcon is missing AccessControlContext");
        }
        return this.acc;
    }

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
        AWTAccessor.setTrayIconAccessor(new AWTAccessor.TrayIconAccessor() { // from class: java.awt.TrayIcon.1
            @Override // sun.awt.AWTAccessor.TrayIconAccessor
            public void addNotify(TrayIcon trayIcon) throws AWTException {
                trayIcon.addNotify();
            }

            @Override // sun.awt.AWTAccessor.TrayIconAccessor
            public void removeNotify(TrayIcon trayIcon) {
                trayIcon.removeNotify();
            }
        });
    }

    private TrayIcon() throws UnsupportedOperationException, SecurityException {
        this.acc = AccessController.getContext();
        SystemTray.checkSystemTrayAllowed();
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        if (!SystemTray.isSupported()) {
            throw new UnsupportedOperationException();
        }
        SunToolkit.insertTargetMapping(this, AppContext.getAppContext());
    }

    public TrayIcon(Image image) {
        this();
        if (image == null) {
            throw new IllegalArgumentException("creating TrayIcon with null Image");
        }
        setImage(image);
    }

    public TrayIcon(Image image, String str) {
        this(image);
        setToolTip(str);
    }

    public TrayIcon(Image image, String str, PopupMenu popupMenu) {
        this(image, str);
        setPopupMenu(popupMenu);
    }

    public void setImage(Image image) {
        if (image == null) {
            throw new NullPointerException("setting null Image");
        }
        this.image = image;
        TrayIconPeer trayIconPeer = this.peer;
        if (trayIconPeer != null) {
            trayIconPeer.updateImage();
        }
    }

    public Image getImage() {
        return this.image;
    }

    public void setPopupMenu(PopupMenu popupMenu) {
        if (popupMenu == this.popup) {
            return;
        }
        synchronized (TrayIcon.class) {
            if (popupMenu != null) {
                if (popupMenu.isTrayIconPopup) {
                    throw new IllegalArgumentException("the PopupMenu is already set for another TrayIcon");
                }
                popupMenu.isTrayIconPopup = true;
            }
            if (this.popup != null) {
                this.popup.isTrayIconPopup = false;
            }
            this.popup = popupMenu;
        }
    }

    public PopupMenu getPopupMenu() {
        return this.popup;
    }

    public void setToolTip(String str) {
        this.tooltip = str;
        TrayIconPeer trayIconPeer = this.peer;
        if (trayIconPeer != null) {
            trayIconPeer.setToolTip(str);
        }
    }

    public String getToolTip() {
        return this.tooltip;
    }

    public void setImageAutoSize(boolean z2) {
        this.autosize = z2;
        TrayIconPeer trayIconPeer = this.peer;
        if (trayIconPeer != null) {
            trayIconPeer.updateImage();
        }
    }

    public boolean isImageAutoSize() {
        return this.autosize;
    }

    public synchronized void addMouseListener(MouseListener mouseListener) {
        if (mouseListener == null) {
            return;
        }
        this.mouseListener = AWTEventMulticaster.add(this.mouseListener, mouseListener);
    }

    public synchronized void removeMouseListener(MouseListener mouseListener) {
        if (mouseListener == null) {
            return;
        }
        this.mouseListener = AWTEventMulticaster.remove(this.mouseListener, mouseListener);
    }

    public synchronized MouseListener[] getMouseListeners() {
        return (MouseListener[]) AWTEventMulticaster.getListeners(this.mouseListener, MouseListener.class);
    }

    public synchronized void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        if (mouseMotionListener == null) {
            return;
        }
        this.mouseMotionListener = AWTEventMulticaster.add(this.mouseMotionListener, mouseMotionListener);
    }

    public synchronized void removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
        if (mouseMotionListener == null) {
            return;
        }
        this.mouseMotionListener = AWTEventMulticaster.remove(this.mouseMotionListener, mouseMotionListener);
    }

    public synchronized MouseMotionListener[] getMouseMotionListeners() {
        return (MouseMotionListener[]) AWTEventMulticaster.getListeners(this.mouseMotionListener, MouseMotionListener.class);
    }

    public String getActionCommand() {
        return this.actionCommand;
    }

    public void setActionCommand(String str) {
        this.actionCommand = str;
    }

    public synchronized void addActionListener(ActionListener actionListener) {
        if (actionListener == null) {
            return;
        }
        this.actionListener = AWTEventMulticaster.add(this.actionListener, actionListener);
    }

    public synchronized void removeActionListener(ActionListener actionListener) {
        if (actionListener == null) {
            return;
        }
        this.actionListener = AWTEventMulticaster.remove(this.actionListener, actionListener);
    }

    public synchronized ActionListener[] getActionListeners() {
        return (ActionListener[]) AWTEventMulticaster.getListeners(this.actionListener, ActionListener.class);
    }

    public void displayMessage(String str, String str2, MessageType messageType) {
        if (str == null && str2 == null) {
            throw new NullPointerException("displaying the message with both caption and text being null");
        }
        TrayIconPeer trayIconPeer = this.peer;
        if (trayIconPeer != null) {
            trayIconPeer.displayMessage(str, str2, messageType.name());
        }
    }

    public Dimension getSize() {
        return SystemTray.getSystemTray().getTrayIconSize();
    }

    void addNotify() throws AWTException {
        synchronized (this) {
            if (this.peer == null) {
                Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                if (defaultToolkit instanceof SunToolkit) {
                    this.peer = ((SunToolkit) Toolkit.getDefaultToolkit()).createTrayIcon(this);
                } else if (defaultToolkit instanceof HeadlessToolkit) {
                    this.peer = ((HeadlessToolkit) Toolkit.getDefaultToolkit()).createTrayIcon(this);
                }
            }
        }
        this.peer.setToolTip(this.tooltip);
    }

    void removeNotify() {
        TrayIconPeer trayIconPeer;
        synchronized (this) {
            trayIconPeer = this.peer;
            this.peer = null;
        }
        if (trayIconPeer != null) {
            trayIconPeer.dispose();
        }
    }

    void setID(int i2) {
        this.id = i2;
    }

    int getID() {
        return this.id;
    }

    void dispatchEvent(AWTEvent aWTEvent) {
        EventQueue.setCurrentEventAndMostRecentTime(aWTEvent);
        Toolkit.getDefaultToolkit().notifyAWTEventListeners(aWTEvent);
        processEvent(aWTEvent);
    }

    void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof MouseEvent) {
            switch (aWTEvent.getID()) {
                case 500:
                case 501:
                case 502:
                    processMouseEvent((MouseEvent) aWTEvent);
                    break;
                case 503:
                    processMouseMotionEvent((MouseEvent) aWTEvent);
                    break;
            }
        }
        if (aWTEvent instanceof ActionEvent) {
            processActionEvent((ActionEvent) aWTEvent);
        }
    }

    void processMouseEvent(MouseEvent mouseEvent) {
        MouseListener mouseListener = this.mouseListener;
        if (mouseListener != null) {
            switch (mouseEvent.getID()) {
                case 500:
                    mouseListener.mouseClicked(mouseEvent);
                    break;
                case 501:
                    mouseListener.mousePressed(mouseEvent);
                    break;
                case 502:
                    mouseListener.mouseReleased(mouseEvent);
                    break;
            }
        }
    }

    void processMouseMotionEvent(MouseEvent mouseEvent) {
        MouseMotionListener mouseMotionListener = this.mouseMotionListener;
        if (mouseMotionListener != null && mouseEvent.getID() == 503) {
            mouseMotionListener.mouseMoved(mouseEvent);
        }
    }

    void processActionEvent(ActionEvent actionEvent) {
        ActionListener actionListener = this.actionListener;
        if (actionListener != null) {
            actionListener.actionPerformed(actionEvent);
        }
    }
}
