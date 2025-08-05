package javafx.embed.swing;

import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit;
import java.awt.AlphaComposite;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.SecondaryLoop;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javax.swing.SwingUtilities;
import sun.awt.AWTAccessor;
import sun.awt.FwDispatcher;
import sun.awt.image.IntegerComponentRaster;

/* loaded from: jfxrt.jar:javafx/embed/swing/SwingFXUtils.class */
public class SwingFXUtils {
    private static final Set<Object> eventLoopKeys = new HashSet();

    private SwingFXUtils() {
    }

    public static WritableImage toFXImage(BufferedImage bimg, WritableImage wimg) {
        PixelFormat<IntBuffer> pf;
        int bw2 = bimg.getWidth();
        int bh2 = bimg.getHeight();
        switch (bimg.getType()) {
            case 2:
            case 3:
                break;
            default:
                BufferedImage converted = new BufferedImage(bw2, bh2, 3);
                Graphics2D g2d = converted.createGraphics();
                g2d.drawImage(bimg, 0, 0, (ImageObserver) null);
                g2d.dispose();
                bimg = converted;
                break;
        }
        if (wimg != null) {
            int iw = (int) wimg.getWidth();
            int ih = (int) wimg.getHeight();
            if (iw < bw2 || ih < bh2) {
                wimg = null;
            } else if (bw2 < iw || bh2 < ih) {
                int[] empty = new int[iw];
                PixelWriter pw = wimg.getPixelWriter();
                PixelFormat<IntBuffer> pf2 = PixelFormat.getIntArgbPreInstance();
                if (bw2 < iw) {
                    pw.setPixels(bw2, 0, iw - bw2, bh2, pf2, empty, 0, 0);
                }
                if (bh2 < ih) {
                    pw.setPixels(0, bh2, iw, ih - bh2, pf2, empty, 0, 0);
                }
            }
        }
        if (wimg == null) {
            wimg = new WritableImage(bw2, bh2);
        }
        PixelWriter pw2 = wimg.getPixelWriter();
        IntegerComponentRaster icr = (IntegerComponentRaster) bimg.getRaster();
        int[] data = icr.getDataStorage();
        int offset = icr.getDataOffset(0);
        int scan = icr.getScanlineStride();
        if (bimg.isAlphaPremultiplied()) {
            pf = PixelFormat.getIntArgbPreInstance();
        } else {
            pf = PixelFormat.getIntArgbInstance();
        }
        pw2.setPixels(0, 0, bw2, bh2, pf, data, offset, scan);
        return wimg;
    }

    private static int getBestBufferedImageType(PixelFormat<?> fxFormat, BufferedImage bimg) {
        int bimgType;
        if (bimg != null && ((bimgType = bimg.getType()) == 2 || bimgType == 3)) {
            return bimgType;
        }
        switch (fxFormat.getType()) {
            case BYTE_BGRA_PRE:
            case INT_ARGB_PRE:
            default:
                return 3;
            case BYTE_BGRA:
            case INT_ARGB:
                return 2;
            case BYTE_RGB:
                return 1;
            case BYTE_INDEXED:
                return fxFormat.isPremultiplied() ? 3 : 2;
        }
    }

    private static WritablePixelFormat<IntBuffer> getAssociatedPixelFormat(BufferedImage bimg) {
        switch (bimg.getType()) {
            case 1:
            case 3:
                return PixelFormat.getIntArgbPreInstance();
            case 2:
                return PixelFormat.getIntArgbInstance();
            default:
                throw new InternalError("Failed to validate BufferedImage type");
        }
    }

    public static BufferedImage fromFXImage(Image img, BufferedImage bimg) {
        PixelReader pr = img.getPixelReader();
        if (pr == null) {
            return null;
        }
        int iw = (int) img.getWidth();
        int ih = (int) img.getHeight();
        int prefBimgType = getBestBufferedImageType(pr.getPixelFormat(), bimg);
        if (bimg != null) {
            int bw2 = bimg.getWidth();
            int bh2 = bimg.getHeight();
            if (bw2 < iw || bh2 < ih || bimg.getType() != prefBimgType) {
                bimg = null;
            } else if (iw < bw2 || ih < bh2) {
                Graphics2D g2d = bimg.createGraphics();
                g2d.setComposite(AlphaComposite.Clear);
                g2d.fillRect(0, 0, bw2, bh2);
                g2d.dispose();
            }
        }
        if (bimg == null) {
            bimg = new BufferedImage(iw, ih, prefBimgType);
        }
        IntegerComponentRaster icr = (IntegerComponentRaster) bimg.getRaster();
        int offset = icr.getDataOffset(0);
        int scan = icr.getScanlineStride();
        int[] data = icr.getDataStorage();
        WritablePixelFormat<IntBuffer> pf = getAssociatedPixelFormat(bimg);
        pr.getPixels(0, 0, iw, ih, pf, data, offset, scan);
        return bimg;
    }

    static void runOnFxThread(Runnable runnable) {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    static void runOnEDT(Runnable r2) {
        if (SwingUtilities.isEventDispatchThread()) {
            r2.run();
        } else {
            SwingUtilities.invokeLater(r2);
        }
    }

    static void runOnEDTAndWait(Object nestedLoopKey, Runnable r2) {
        Toolkit.getToolkit().checkFxUserThread();
        if (SwingUtilities.isEventDispatchThread()) {
            r2.run();
            return;
        }
        eventLoopKeys.add(nestedLoopKey);
        SwingUtilities.invokeLater(r2);
        Toolkit.getToolkit().enterNestedEventLoop(nestedLoopKey);
    }

    static void leaveFXNestedLoop(Object nestedLoopKey) {
        if (eventLoopKeys.contains(nestedLoopKey)) {
            if (Platform.isFxApplicationThread()) {
                Toolkit.getToolkit().exitNestedEventLoop(nestedLoopKey, null);
            } else {
                Platform.runLater(() -> {
                    Toolkit.getToolkit().exitNestedEventLoop(nestedLoopKey, null);
                });
            }
            eventLoopKeys.remove(nestedLoopKey);
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingFXUtils$FwSecondaryLoop.class */
    private static class FwSecondaryLoop implements SecondaryLoop {
        private final AtomicBoolean isRunning;

        private FwSecondaryLoop() {
            this.isRunning = new AtomicBoolean(false);
        }

        @Override // java.awt.SecondaryLoop
        public boolean enter() {
            if (this.isRunning.compareAndSet(false, true)) {
                PlatformImpl.runAndWait(() -> {
                    Toolkit.getToolkit().enterNestedEventLoop(this);
                });
                return true;
            }
            return false;
        }

        @Override // java.awt.SecondaryLoop
        public boolean exit() {
            if (this.isRunning.compareAndSet(true, false)) {
                PlatformImpl.runAndWait(() -> {
                    Toolkit.getToolkit().exitNestedEventLoop(this, null);
                });
                return true;
            }
            return false;
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingFXUtils$FXDispatcher.class */
    private static class FXDispatcher implements FwDispatcher {
        private FXDispatcher() {
        }

        @Override // sun.awt.FwDispatcher
        public boolean isDispatchThread() {
            return Platform.isFxApplicationThread();
        }

        @Override // sun.awt.FwDispatcher
        public void scheduleDispatch(Runnable runnable) {
            Platform.runLater(runnable);
        }

        @Override // sun.awt.FwDispatcher
        public SecondaryLoop createSecondaryLoop() {
            return new FwSecondaryLoop();
        }
    }

    private static EventQueue getEventQueue() {
        return (EventQueue) AccessController.doPrivileged(() -> {
            return java.awt.Toolkit.getDefaultToolkit().getSystemEventQueue();
        });
    }

    private static void installFwEventQueue() {
        AWTAccessor.getEventQueueAccessor().setFwDispatcher(getEventQueue(), new FXDispatcher());
    }

    private static void removeFwEventQueue() {
        AWTAccessor.getEventQueueAccessor().setFwDispatcher(getEventQueue(), null);
    }
}
