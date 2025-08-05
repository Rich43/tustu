package sun.awt.windows;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.InvocationEvent;
import java.awt.image.BufferedImage;
import java.awt.peer.ComponentPeer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.awt.EmbeddedFrame;
import sun.awt.image.ByteInterleavedRaster;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/awt/windows/WEmbeddedFrame.class */
public class WEmbeddedFrame extends EmbeddedFrame {
    private long handle;
    private int bandWidth;
    private int bandHeight;
    private int imgWid;
    private int imgHgt;
    private static int pScale;
    private static final int MAX_BAND_SIZE = 30720;
    private boolean isEmbeddedInIE;
    private static String printScale;

    private native boolean isPrinterDC(long j2);

    private native void printBand(long j2, byte[] bArr, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    private static native void initIDs();

    native void notifyModalBlockedImpl(WEmbeddedFramePeer wEmbeddedFramePeer, WWindowPeer wWindowPeer, boolean z2);

    static {
        initIDs();
        pScale = 0;
        printScale = (String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.print.pluginscalefactor"));
    }

    public WEmbeddedFrame() {
        this(0L);
    }

    @Deprecated
    public WEmbeddedFrame(int i2) {
        this(i2);
    }

    public WEmbeddedFrame(long j2) {
        this.bandWidth = 0;
        this.bandHeight = 0;
        this.imgWid = 0;
        this.imgHgt = 0;
        this.isEmbeddedInIE = false;
        this.handle = j2;
        if (j2 != 0) {
            addNotify();
            show();
        }
    }

    @Override // sun.awt.EmbeddedFrame, java.awt.Frame, java.awt.Window, java.awt.Container, java.awt.Component
    public void addNotify() {
        if (getPeer() == null) {
            setPeer(((WToolkit) Toolkit.getDefaultToolkit()).createEmbeddedFrame(this));
        }
        super.addNotify();
    }

    public long getEmbedderHandle() {
        return this.handle;
    }

    void print(long j2) {
        BufferedImage bufferedImage = null;
        int i2 = 1;
        int i3 = 1;
        if (isPrinterDC(j2)) {
            int printScaleFactor = getPrintScaleFactor();
            i3 = printScaleFactor;
            i2 = printScaleFactor;
        }
        int height = getHeight();
        if (0 == 0) {
            this.bandWidth = getWidth();
            if (this.bandWidth % 4 != 0) {
                this.bandWidth += 4 - (this.bandWidth % 4);
            }
            if (this.bandWidth <= 0) {
                return;
            }
            this.bandHeight = Math.min(MAX_BAND_SIZE / this.bandWidth, height);
            this.imgWid = this.bandWidth * i2;
            this.imgHgt = this.bandHeight * i3;
            bufferedImage = new BufferedImage(this.imgWid, this.imgHgt, 5);
        }
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(Color.white);
        Graphics2D graphics2D = (Graphics2D) bufferedImage.getGraphics();
        graphics2D.translate(0, this.imgHgt);
        graphics2D.scale(i2, -i3);
        byte[] dataStorage = ((ByteInterleavedRaster) bufferedImage.getRaster()).getDataStorage();
        int i4 = 0;
        while (true) {
            int i5 = i4;
            if (i5 < height) {
                graphics.fillRect(0, 0, this.bandWidth, this.bandHeight);
                printComponents(graphics2D);
                int i6 = 0;
                int i7 = this.bandHeight;
                int i8 = this.imgHgt;
                if (i5 + this.bandHeight > height) {
                    i7 = height - i5;
                    i8 = i7 * i3;
                    i6 = this.imgWid * (this.imgHgt - i8) * 3;
                }
                printBand(j2, dataStorage, i6, 0, 0, this.imgWid, i8, 0, i5, this.bandWidth, i7);
                graphics2D.translate(0, -this.bandHeight);
                i4 = i5 + this.bandHeight;
            } else {
                return;
            }
        }
    }

    protected static int getPrintScaleFactor() {
        if (pScale != 0) {
            return pScale;
        }
        if (printScale == null) {
            printScale = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: sun.awt.windows.WEmbeddedFrame.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public String run2() {
                    return System.getenv("JAVA2D_PLUGIN_PRINT_SCALE");
                }
            });
        }
        int i2 = 4;
        if (printScale != null) {
            try {
                i2 = Integer.parseInt(printScale);
                if (i2 > 8 || i2 < 1) {
                    i2 = 4;
                }
            } catch (NumberFormatException e2) {
            }
        }
        pScale = i2;
        return pScale;
    }

    public void activateEmbeddingTopLevel() {
    }

    @Override // sun.awt.EmbeddedFrame
    public void synthesizeWindowActivation(boolean z2) {
        if (!z2 || EventQueue.isDispatchThread()) {
            ((WFramePeer) getPeer()).emulateActivation(z2);
        } else {
            WToolkit.postEvent(WToolkit.targetToAppContext(this), new InvocationEvent(this, new Runnable() { // from class: sun.awt.windows.WEmbeddedFrame.2
                @Override // java.lang.Runnable
                public void run() {
                    ((WFramePeer) WEmbeddedFrame.this.getPeer()).emulateActivation(true);
                }
            }));
        }
    }

    @Override // sun.awt.EmbeddedFrame
    public void registerAccelerator(AWTKeyStroke aWTKeyStroke) {
    }

    @Override // sun.awt.EmbeddedFrame
    public void unregisterAccelerator(AWTKeyStroke aWTKeyStroke) {
    }

    @Override // sun.awt.EmbeddedFrame
    public void notifyModalBlocked(Dialog dialog, boolean z2) {
        try {
            notifyModalBlockedImpl((WEmbeddedFramePeer) ((ComponentPeer) WToolkit.targetToPeer(this)), (WWindowPeer) ((ComponentPeer) WToolkit.targetToPeer(dialog)), z2);
        } catch (Exception e2) {
            e2.printStackTrace(System.err);
        }
    }
}
