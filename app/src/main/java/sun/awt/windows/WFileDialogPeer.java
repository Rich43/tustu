package sun.awt.windows;

import com.sun.istack.internal.localization.Localizable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.dnd.DropTarget;
import java.awt.image.BufferedImage;
import java.awt.peer.ComponentPeer;
import java.awt.peer.FileDialogPeer;
import java.io.File;
import java.io.FilenameFilter;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;
import sun.awt.AWTAccessor;
import sun.awt.CausedFocusEvent;
import sun.java2d.pipe.Region;

/* loaded from: rt.jar:sun/awt/windows/WFileDialogPeer.class */
final class WFileDialogPeer extends WWindowPeer implements FileDialogPeer {
    private WComponentPeer parent;
    private FilenameFilter fileFilter;
    private Vector<WWindowPeer> blockedWindows;

    private static native void setFilterString(String str);

    private native void _dispose();

    /* JADX INFO: Access modifiers changed from: private */
    public native void _show();

    private native void _hide();

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public native void toFront();

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public native void toBack();

    private static native void initIDs();

    static {
        initIDs();
        setFilterString((String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.awt.windows.WFileDialogPeer.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                try {
                    return ResourceBundle.getBundle("sun.awt.windows.awtLocalization").getString("allFiles");
                } catch (MissingResourceException e2) {
                    return "All Files";
                }
            }
        }));
    }

    @Override // java.awt.peer.FileDialogPeer
    public void setFilenameFilter(FilenameFilter filenameFilter) {
        this.fileFilter = filenameFilter;
    }

    boolean checkFilenameFilter(String str) {
        if (this.fileFilter == null) {
            return true;
        }
        File file = new File(str);
        return this.fileFilter.accept(new File(file.getParent()), file.getName());
    }

    WFileDialogPeer(FileDialog fileDialog) {
        super(fileDialog);
        this.blockedWindows = new Vector<>();
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void create(WComponentPeer wComponentPeer) {
        this.parent = wComponentPeer;
    }

    @Override // sun.awt.windows.WComponentPeer
    protected void checkCreation() {
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WPanelPeer, sun.awt.windows.WCanvasPeer, sun.awt.windows.WComponentPeer
    void initialize() {
        setFilenameFilter(((FileDialog) this.target).getFilenameFilter());
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer, sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        WToolkit.targetDisposedPeer(this.target, this);
        _dispose();
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    public void show() {
        new Thread(new Runnable() { // from class: sun.awt.windows.WFileDialogPeer.1
            @Override // java.lang.Runnable
            public void run() {
                WFileDialogPeer.this._show();
            }
        }).start();
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer
    void hide() {
        _hide();
    }

    void setHWnd(long j2) {
        if (this.hwnd == j2) {
            return;
        }
        this.hwnd = j2;
        Iterator<WWindowPeer> it = this.blockedWindows.iterator();
        while (it.hasNext()) {
            WWindowPeer next = it.next();
            if (j2 != 0) {
                next.modalDisable((Dialog) this.target, j2);
            } else {
                next.modalEnable((Dialog) this.target);
            }
        }
    }

    void handleSelected(char[] cArr) {
        String strSubstring;
        String strSubstring2;
        File[] fileArr;
        String[] strArrSplit = new String(cArr).split(Localizable.NOT_LOCALIZABLE);
        if (strArrSplit.length > 1) {
            strSubstring = strArrSplit[0];
            int length = strArrSplit.length - 1;
            fileArr = new File[length];
            for (int i2 = 0; i2 < length; i2++) {
                fileArr[i2] = new File(strSubstring, strArrSplit[i2 + 1]);
            }
            strSubstring2 = strArrSplit[1];
        } else {
            int iLastIndexOf = strArrSplit[0].lastIndexOf(File.separatorChar);
            if (iLastIndexOf == -1) {
                strSubstring = "." + File.separator;
                strSubstring2 = strArrSplit[0];
            } else {
                strSubstring = strArrSplit[0].substring(0, iLastIndexOf + 1);
                strSubstring2 = strArrSplit[0].substring(iLastIndexOf + 1);
            }
            fileArr = new File[]{new File(strSubstring, strSubstring2)};
        }
        final FileDialog fileDialog = (FileDialog) this.target;
        AWTAccessor.FileDialogAccessor fileDialogAccessor = AWTAccessor.getFileDialogAccessor();
        fileDialogAccessor.setDirectory(fileDialog, strSubstring);
        fileDialogAccessor.setFile(fileDialog, strSubstring2);
        fileDialogAccessor.setFiles(fileDialog, fileArr);
        WToolkit.executeOnEventHandlerThread(fileDialog, new Runnable() { // from class: sun.awt.windows.WFileDialogPeer.2
            @Override // java.lang.Runnable
            public void run() {
                fileDialog.setVisible(false);
            }
        });
    }

    void handleCancel() {
        final FileDialog fileDialog = (FileDialog) this.target;
        AWTAccessor.getFileDialogAccessor().setFile(fileDialog, null);
        AWTAccessor.getFileDialogAccessor().setFiles(fileDialog, null);
        AWTAccessor.getFileDialogAccessor().setDirectory(fileDialog, null);
        WToolkit.executeOnEventHandlerThread(fileDialog, new Runnable() { // from class: sun.awt.windows.WFileDialogPeer.3
            @Override // java.lang.Runnable
            public void run() {
                fileDialog.setVisible(false);
            }
        });
    }

    void blockWindow(WWindowPeer wWindowPeer) {
        this.blockedWindows.add(wWindowPeer);
        if (this.hwnd != 0) {
            wWindowPeer.modalDisable((Dialog) this.target, this.hwnd);
        }
    }

    void unblockWindow(WWindowPeer wWindowPeer) {
        this.blockedWindows.remove(wWindowPeer);
        if (this.hwnd != 0) {
            wWindowPeer.modalEnable((Dialog) this.target);
        }
    }

    @Override // java.awt.peer.DialogPeer
    public void blockWindows(List<Window> list) {
        Iterator<Window> it = list.iterator();
        while (it.hasNext()) {
            WWindowPeer wWindowPeer = (WWindowPeer) AWTAccessor.getComponentAccessor().getPeer(it.next());
            if (wWindowPeer != null) {
                blockWindow(wWindowPeer);
            }
        }
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateAlwaysOnTopState() {
    }

    @Override // java.awt.peer.FileDialogPeer
    public void setDirectory(String str) {
    }

    @Override // java.awt.peer.FileDialogPeer
    public void setFile(String str) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.DialogPeer
    public void setTitle(String str) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.DialogPeer
    public void setResizable(boolean z2) {
    }

    @Override // sun.awt.windows.WComponentPeer
    void enable() {
    }

    @Override // sun.awt.windows.WComponentPeer
    void disable() {
    }

    @Override // sun.awt.windows.WComponentPeer
    public void reshape(int i2, int i3, int i4, int i5) {
    }

    public boolean handleEvent(Event event) {
        return false;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setForeground(Color color) {
    }

    @Override // sun.awt.windows.WWindowPeer, sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setBackground(Color color) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setFont(Font font) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateMinimumSize() {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateIconImages() {
    }

    public boolean requestFocus(boolean z2, boolean z3) {
        return false;
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public boolean requestFocus(Component component, boolean z2, boolean z3, long j2, CausedFocusEvent.Cause cause) {
        return false;
    }

    @Override // sun.awt.windows.WComponentPeer
    void start() {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ContainerPeer
    public void beginValidate() {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ContainerPeer
    public void endValidate() {
    }

    void invalidate(int i2, int i3, int i4, int i5) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.dnd.peer.DropTargetPeer
    public void addDropTarget(DropTarget dropTarget) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.dnd.peer.DropTargetPeer
    public void removeDropTarget(DropTarget dropTarget) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void updateFocusableWindowState() {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void setZOrder(ComponentPeer componentPeer) {
    }

    @Override // sun.awt.windows.WComponentPeer, java.awt.peer.ComponentPeer
    public void applyShape(Region region) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void setOpacity(float f2) {
    }

    @Override // sun.awt.windows.WWindowPeer, java.awt.peer.WindowPeer
    public void setOpaque(boolean z2) {
    }

    public void updateWindow(BufferedImage bufferedImage) {
    }

    @Override // sun.awt.windows.WComponentPeer
    public void createScreenSurface(boolean z2) {
    }

    @Override // sun.awt.windows.WComponentPeer
    public void replaceSurfaceData() {
    }

    public boolean isMultipleMode() {
        return AWTAccessor.getFileDialogAccessor().isMultipleMode((FileDialog) this.target);
    }
}
