package sun.awt.windows;

import java.awt.AWTEvent;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.awt.peer.TrayIconPeer;
import sun.awt.SunToolkit;
import sun.awt.image.IntegerComponentRaster;

/* loaded from: rt.jar:sun/awt/windows/WTrayIconPeer.class */
final class WTrayIconPeer extends WObjectPeer implements TrayIconPeer {
    static final int TRAY_ICON_WIDTH = 16;
    static final int TRAY_ICON_HEIGHT = 16;
    static final int TRAY_ICON_MASK_SIZE = 32;
    IconObserver observer = new IconObserver();
    boolean firstUpdate = true;
    Frame popupParent = new Frame("PopupMessageWindow");
    PopupMenu popup;

    @Override // java.awt.peer.TrayIconPeer
    public native void setToolTip(String str);

    native void create();

    native synchronized void _dispose();

    native void updateNativeIcon(boolean z2);

    native void setNativeIcon(int[] iArr, byte[] bArr, int i2, int i3, int i4);

    native void _displayMessage(String str, String str2, String str3);

    @Override // sun.awt.windows.WObjectPeer
    protected void disposeImpl() {
        if (this.popupParent != null) {
            this.popupParent.dispose();
        }
        this.popupParent.dispose();
        _dispose();
        WToolkit.targetDisposedPeer(this.target, this);
    }

    WTrayIconPeer(TrayIcon trayIcon) {
        this.target = trayIcon;
        this.popupParent.addNotify();
        create();
        updateImage();
    }

    @Override // java.awt.peer.TrayIconPeer
    public void updateImage() {
        Image image = ((TrayIcon) this.target).getImage();
        if (image != null) {
            updateNativeImage(image);
        }
    }

    @Override // java.awt.peer.TrayIconPeer
    public synchronized void showPopupMenu(final int i2, final int i3) {
        if (isDisposed()) {
            return;
        }
        SunToolkit.executeOnEventHandlerThread(this.target, new Runnable() { // from class: sun.awt.windows.WTrayIconPeer.1
            @Override // java.lang.Runnable
            public void run() {
                PopupMenu popupMenu = ((TrayIcon) WTrayIconPeer.this.target).getPopupMenu();
                if (WTrayIconPeer.this.popup != popupMenu) {
                    if (WTrayIconPeer.this.popup != null) {
                        WTrayIconPeer.this.popupParent.remove(WTrayIconPeer.this.popup);
                    }
                    if (popupMenu != null) {
                        WTrayIconPeer.this.popupParent.add(popupMenu);
                    }
                    WTrayIconPeer.this.popup = popupMenu;
                }
                if (WTrayIconPeer.this.popup != null) {
                    ((WPopupMenuPeer) WTrayIconPeer.this.popup.getPeer()).show(WTrayIconPeer.this.popupParent, new Point(i2, i3));
                }
            }
        });
    }

    @Override // java.awt.peer.TrayIconPeer
    public void displayMessage(String str, String str2, String str3) {
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        _displayMessage(str, str2, str3);
    }

    synchronized void updateNativeImage(Image image) {
        if (isDisposed()) {
            return;
        }
        boolean zIsImageAutoSize = ((TrayIcon) this.target).isImageAutoSize();
        BufferedImage bufferedImage = new BufferedImage(16, 16, 2);
        Graphics2D graphics2DCreateGraphics = bufferedImage.createGraphics();
        if (graphics2DCreateGraphics != null) {
            try {
                graphics2DCreateGraphics.setPaintMode();
                graphics2DCreateGraphics.drawImage(image, 0, 0, zIsImageAutoSize ? 16 : image.getWidth(this.observer), zIsImageAutoSize ? 16 : image.getHeight(this.observer), this.observer);
                createNativeImage(bufferedImage);
                updateNativeIcon(!this.firstUpdate);
                if (this.firstUpdate) {
                    this.firstUpdate = false;
                }
            } finally {
                graphics2DCreateGraphics.dispose();
            }
        }
    }

    void createNativeImage(BufferedImage bufferedImage) {
        WritableRaster raster = bufferedImage.getRaster();
        byte[] bArr = new byte[32];
        int[] data = ((DataBufferInt) raster.getDataBuffer()).getData();
        int length = data.length;
        int width = raster.getWidth();
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2 / 8;
            int i4 = 1 << (7 - (i2 % 8));
            if ((data[i2] & (-16777216)) == 0 && i3 < bArr.length) {
                bArr[i3] = (byte) (bArr[i3] | i4);
            }
        }
        if (raster instanceof IntegerComponentRaster) {
            width = ((IntegerComponentRaster) raster).getScanlineStride();
        }
        setNativeIcon(((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData(), bArr, width, raster.getWidth(), raster.getHeight());
    }

    void postEvent(AWTEvent aWTEvent) {
        WToolkit.postEvent(WToolkit.targetToAppContext(this.target), aWTEvent);
    }

    /* loaded from: rt.jar:sun/awt/windows/WTrayIconPeer$IconObserver.class */
    class IconObserver implements ImageObserver {
        IconObserver() {
        }

        @Override // java.awt.image.ImageObserver
        public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
            if (image != ((TrayIcon) WTrayIconPeer.this.target).getImage() || WTrayIconPeer.this.isDisposed()) {
                return false;
            }
            if ((i2 & 51) != 0) {
                WTrayIconPeer.this.updateNativeImage(image);
            }
            return (i2 & 32) == 0;
        }
    }
}
