package java.awt;

import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.awt.peer.RobotPeer;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;
import sun.awt.ComponentFactory;
import sun.awt.SunToolkit;
import sun.awt.image.SunWritableRaster;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;
import sun.security.util.SecurityConstants;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/awt/Robot.class */
public class Robot {
    private static final int MAX_DELAY = 60000;
    private RobotPeer peer;
    private static int LEGAL_BUTTON_MASK = 0;
    private transient RobotDisposer disposer;
    private boolean isAutoWaitForIdle = false;
    private int autoDelay = 0;
    private DirectColorModel screenCapCM = null;
    private transient Object anchor = new Object();

    public Robot() throws AWTException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new AWTException("headless environment");
        }
        init(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
    }

    public Robot(GraphicsDevice graphicsDevice) throws AWTException {
        checkIsScreenDevice(graphicsDevice);
        init(graphicsDevice);
    }

    private void init(GraphicsDevice graphicsDevice) throws AWTException {
        checkRobotAllowed();
        Object defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof ComponentFactory) {
            this.peer = ((ComponentFactory) defaultToolkit).createRobot(this, graphicsDevice);
            this.disposer = new RobotDisposer(this.peer);
            Disposer.addRecord(this.anchor, this.disposer);
        }
        initLegalButtonMask();
    }

    private static synchronized void initLegalButtonMask() {
        if (LEGAL_BUTTON_MASK != 0) {
            return;
        }
        int maskForButton = 0;
        if (Toolkit.getDefaultToolkit().areExtraMouseButtonsEnabled() && (Toolkit.getDefaultToolkit() instanceof SunToolkit)) {
            int numberOfButtons = ((SunToolkit) Toolkit.getDefaultToolkit()).getNumberOfButtons();
            for (int i2 = 0; i2 < numberOfButtons; i2++) {
                maskForButton |= InputEvent.getMaskForButton(i2 + 1);
            }
        }
        LEGAL_BUTTON_MASK = maskForButton | 7196;
    }

    private void checkRobotAllowed() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.CREATE_ROBOT_PERMISSION);
        }
    }

    private void checkIsScreenDevice(GraphicsDevice graphicsDevice) {
        if (graphicsDevice == null || graphicsDevice.getType() != 0) {
            throw new IllegalArgumentException("not a valid screen device");
        }
    }

    /* loaded from: rt.jar:java/awt/Robot$RobotDisposer.class */
    static class RobotDisposer implements DisposerRecord {
        private final RobotPeer peer;

        public RobotDisposer(RobotPeer robotPeer) {
            this.peer = robotPeer;
        }

        @Override // sun.java2d.DisposerRecord
        public void dispose() {
            if (this.peer != null) {
                this.peer.dispose();
            }
        }
    }

    public synchronized void mouseMove(int i2, int i3) {
        this.peer.mouseMove(i2, i3);
        afterEvent();
    }

    public synchronized void mousePress(int i2) {
        checkButtonsArgument(i2);
        this.peer.mousePress(i2);
        afterEvent();
    }

    public synchronized void mouseRelease(int i2) {
        checkButtonsArgument(i2);
        this.peer.mouseRelease(i2);
        afterEvent();
    }

    private void checkButtonsArgument(int i2) {
        if ((i2 | LEGAL_BUTTON_MASK) != LEGAL_BUTTON_MASK) {
            throw new IllegalArgumentException("Invalid combination of button flags");
        }
    }

    public synchronized void mouseWheel(int i2) {
        this.peer.mouseWheel(i2);
        afterEvent();
    }

    public synchronized void keyPress(int i2) {
        checkKeycodeArgument(i2);
        this.peer.keyPress(i2);
        afterEvent();
    }

    public synchronized void keyRelease(int i2) {
        checkKeycodeArgument(i2);
        this.peer.keyRelease(i2);
        afterEvent();
    }

    private void checkKeycodeArgument(int i2) {
        if (i2 == 0) {
            throw new IllegalArgumentException("Invalid key code");
        }
    }

    public synchronized Color getPixelColor(int i2, int i3) {
        checkScreenCaptureAllowed();
        return new Color(this.peer.getRGBPixel(i2, i3));
    }

    public synchronized BufferedImage createScreenCapture(Rectangle rectangle) {
        checkScreenCaptureAllowed();
        checkValidRect(rectangle);
        if (this.screenCapCM == null) {
            this.screenCapCM = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
        }
        Toolkit.getDefaultToolkit().sync();
        int[] rGBPixels = this.peer.getRGBPixels(rectangle);
        DataBufferInt dataBufferInt = new DataBufferInt(rGBPixels, rGBPixels.length);
        WritableRaster writableRasterCreatePackedRaster = Raster.createPackedRaster(dataBufferInt, rectangle.width, rectangle.height, rectangle.width, new int[]{this.screenCapCM.getRedMask(), this.screenCapCM.getGreenMask(), this.screenCapCM.getBlueMask()}, (Point) null);
        SunWritableRaster.makeTrackable(dataBufferInt);
        return new BufferedImage((ColorModel) this.screenCapCM, writableRasterCreatePackedRaster, false, (Hashtable<?, ?>) null);
    }

    private static void checkValidRect(Rectangle rectangle) {
        if (rectangle.width <= 0 || rectangle.height <= 0) {
            throw new IllegalArgumentException("Rectangle width and height must be > 0");
        }
    }

    private static void checkScreenCaptureAllowed() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.READ_DISPLAY_PIXELS_PERMISSION);
        }
    }

    private void afterEvent() {
        autoWaitForIdle();
        autoDelay();
    }

    public synchronized boolean isAutoWaitForIdle() {
        return this.isAutoWaitForIdle;
    }

    public synchronized void setAutoWaitForIdle(boolean z2) {
        this.isAutoWaitForIdle = z2;
    }

    private void autoWaitForIdle() {
        if (this.isAutoWaitForIdle) {
            waitForIdle();
        }
    }

    public synchronized int getAutoDelay() {
        return this.autoDelay;
    }

    public synchronized void setAutoDelay(int i2) {
        checkDelayArgument(i2);
        this.autoDelay = i2;
    }

    private void autoDelay() {
        delay(this.autoDelay);
    }

    public synchronized void delay(int i2) {
        checkDelayArgument(i2);
        try {
            Thread.sleep(i2);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    private void checkDelayArgument(int i2) {
        if (i2 < 0 || i2 > MAX_DELAY) {
            throw new IllegalArgumentException("Delay must be to 0 to 60,000ms");
        }
    }

    public synchronized void waitForIdle() {
        checkNotDispatchThread();
        try {
            SunToolkit.flushPendingEvents();
            EventQueue.invokeAndWait(new Runnable() { // from class: java.awt.Robot.1
                @Override // java.lang.Runnable
                public void run() {
                }
            });
        } catch (InterruptedException e2) {
            System.err.println("Robot.waitForIdle, non-fatal exception caught:");
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            System.err.println("Robot.waitForIdle, non-fatal exception caught:");
            e3.printStackTrace();
        }
    }

    private void checkNotDispatchThread() {
        if (EventQueue.isDispatchThread()) {
            throw new IllegalThreadStateException("Cannot call method from the event dispatcher thread");
        }
    }

    public synchronized String toString() {
        return getClass().getName() + "[ " + ("autoDelay = " + getAutoDelay() + ", autoWaitForIdle = " + isAutoWaitForIdle()) + " ]";
    }
}
