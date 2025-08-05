package java.awt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.SinglePixelPackedSampleModel;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.awt.image.SunWritableRaster;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/SplashScreen.class */
public final class SplashScreen {
    private BufferedImage image;
    private final long splashPtr;
    private static boolean wasClosed;
    private URL imageURL;
    private static SplashScreen theInstance;
    private static final PlatformLogger log;
    static final /* synthetic */ boolean $assertionsDisabled;

    private static native void _update(long j2, int[] iArr, int i2, int i3, int i4, int i5, int i6);

    private static native boolean _isVisible(long j2);

    private static native Rectangle _getBounds(long j2);

    private static native long _getInstance();

    private static native void _close(long j2);

    private static native String _getImageFileName(long j2);

    private static native String _getImageJarName(long j2);

    private static native boolean _setImageData(long j2, byte[] bArr);

    private static native float _getScaleFactor(long j2);

    static {
        $assertionsDisabled = !SplashScreen.class.desiredAssertionStatus();
        wasClosed = false;
        theInstance = null;
        log = PlatformLogger.getLogger("java.awt.SplashScreen");
    }

    SplashScreen(long j2) {
        this.splashPtr = j2;
    }

    public static SplashScreen getSplashScreen() {
        SplashScreen splashScreen;
        synchronized (SplashScreen.class) {
            if (GraphicsEnvironment.isHeadless()) {
                throw new HeadlessException();
            }
            if (!wasClosed && theInstance == null) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.SplashScreen.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        System.loadLibrary("splashscreen");
                        return null;
                    }
                });
                long j_getInstance = _getInstance();
                if (j_getInstance != 0 && _isVisible(j_getInstance)) {
                    theInstance = new SplashScreen(j_getInstance);
                }
            }
            splashScreen = theInstance;
        }
        return splashScreen;
    }

    public void setImageURL(URL url) throws IllegalStateException, IOException, NullPointerException {
        checkVisible();
        URLConnection uRLConnectionOpenConnection = url.openConnection();
        uRLConnectionOpenConnection.connect();
        int contentLength = uRLConnectionOpenConnection.getContentLength();
        InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
        byte[] bArr = new byte[contentLength];
        int i2 = 0;
        while (true) {
            int i3 = i2;
            int iAvailable = inputStream.available();
            if (iAvailable <= 0) {
                iAvailable = 1;
            }
            if (i3 + iAvailable > contentLength) {
                contentLength = i3 * 2;
                if (i3 + iAvailable > contentLength) {
                    contentLength = iAvailable + i3;
                }
                byte[] bArr2 = bArr;
                bArr = new byte[contentLength];
                System.arraycopy(bArr2, 0, bArr, 0, i3);
            }
            int i4 = inputStream.read(bArr, i3, iAvailable);
            if (i4 < 0) {
                break;
            } else {
                i2 = i3 + i4;
            }
        }
        synchronized (SplashScreen.class) {
            checkVisible();
            if (!_setImageData(this.splashPtr, bArr)) {
                throw new IOException("Bad image format or i/o error when loading image");
            }
            this.imageURL = url;
        }
    }

    private void checkVisible() {
        if (!isVisible()) {
            throw new IllegalStateException("no splash screen available");
        }
    }

    public URL getImageURL() throws IllegalStateException {
        URL url;
        synchronized (SplashScreen.class) {
            checkVisible();
            if (this.imageURL == null) {
                try {
                    String str_getImageFileName = _getImageFileName(this.splashPtr);
                    String str_getImageJarName = _getImageJarName(this.splashPtr);
                    if (str_getImageFileName != null) {
                        if (str_getImageJarName != null) {
                            this.imageURL = new URL("jar:" + new File(str_getImageJarName).toURL().toString() + "!/" + str_getImageFileName);
                        } else {
                            this.imageURL = new File(str_getImageFileName).toURL();
                        }
                    }
                } catch (MalformedURLException e2) {
                    if (log.isLoggable(PlatformLogger.Level.FINE)) {
                        log.fine("MalformedURLException caught in the getImageURL() method", e2);
                    }
                }
                url = this.imageURL;
            } else {
                url = this.imageURL;
            }
        }
        return url;
    }

    public Rectangle getBounds() throws IllegalStateException {
        Rectangle rectangle_getBounds;
        synchronized (SplashScreen.class) {
            checkVisible();
            float f_getScaleFactor = _getScaleFactor(this.splashPtr);
            rectangle_getBounds = _getBounds(this.splashPtr);
            if (!$assertionsDisabled && f_getScaleFactor <= 0.0f) {
                throw new AssertionError();
            }
            if (f_getScaleFactor > 0.0f && f_getScaleFactor != 1.0f) {
                rectangle_getBounds.setSize((int) (rectangle_getBounds.getWidth() / f_getScaleFactor), (int) (rectangle_getBounds.getHeight() / f_getScaleFactor));
            }
        }
        return rectangle_getBounds;
    }

    public Dimension getSize() throws IllegalStateException {
        return getBounds().getSize();
    }

    public Graphics2D createGraphics() throws IllegalStateException {
        Graphics2D graphics2DCreateGraphics;
        synchronized (SplashScreen.class) {
            checkVisible();
            if (this.image == null) {
                Dimension size = _getBounds(this.splashPtr).getSize();
                this.image = new BufferedImage(size.width, size.height, 2);
            }
            float f_getScaleFactor = _getScaleFactor(this.splashPtr);
            graphics2DCreateGraphics = this.image.createGraphics();
            if (!$assertionsDisabled && f_getScaleFactor <= 0.0f) {
                throw new AssertionError();
            }
            if (f_getScaleFactor <= 0.0f) {
                f_getScaleFactor = 1.0f;
            }
            graphics2DCreateGraphics.scale(f_getScaleFactor, f_getScaleFactor);
        }
        return graphics2DCreateGraphics;
    }

    public void update() throws IllegalStateException {
        BufferedImage bufferedImage;
        synchronized (SplashScreen.class) {
            checkVisible();
            bufferedImage = this.image;
        }
        if (bufferedImage == null) {
            throw new IllegalStateException("no overlay image available");
        }
        DataBuffer dataBuffer = bufferedImage.getRaster().getDataBuffer();
        if (!(dataBuffer instanceof DataBufferInt)) {
            throw new AssertionError((Object) ("Overlay image DataBuffer is of invalid type == " + dataBuffer.getClass().getName()));
        }
        int numBanks = dataBuffer.getNumBanks();
        if (numBanks != 1) {
            throw new AssertionError((Object) ("Invalid number of banks ==" + numBanks + " in overlay image DataBuffer"));
        }
        if (!(bufferedImage.getSampleModel() instanceof SinglePixelPackedSampleModel)) {
            throw new AssertionError((Object) ("Overlay image has invalid sample model == " + bufferedImage.getSampleModel().getClass().getName()));
        }
        int scanlineStride = ((SinglePixelPackedSampleModel) bufferedImage.getSampleModel()).getScanlineStride();
        Rectangle bounds = bufferedImage.getRaster().getBounds();
        int[] iArrStealData = SunWritableRaster.stealData((DataBufferInt) dataBuffer, 0);
        synchronized (SplashScreen.class) {
            checkVisible();
            _update(this.splashPtr, iArrStealData, bounds.f12372x, bounds.f12373y, bounds.width, bounds.height, scanlineStride);
        }
    }

    public void close() throws IllegalStateException {
        synchronized (SplashScreen.class) {
            checkVisible();
            _close(this.splashPtr);
            this.image = null;
            markClosed();
        }
    }

    static void markClosed() {
        synchronized (SplashScreen.class) {
            wasClosed = true;
            theInstance = null;
        }
    }

    public boolean isVisible() {
        boolean z2;
        synchronized (SplashScreen.class) {
            z2 = !wasClosed && _isVisible(this.splashPtr);
        }
        return z2;
    }
}
