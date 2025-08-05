package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.Size;
import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import com.sun.prism.Image;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javafx.geometry.Dimension2D;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/CursorUtils.class */
final class CursorUtils {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CursorUtils.class.desiredAssertionStatus();
    }

    private CursorUtils() {
    }

    public static Cursor getPlatformCursor(CursorFrame cursorFrame) {
        Cursor cachedPlatformCursor = (Cursor) cursorFrame.getPlatformCursor(Cursor.class);
        if (cachedPlatformCursor != null) {
            return cachedPlatformCursor;
        }
        Cursor platformCursor = createPlatformCursor(cursorFrame);
        cursorFrame.setPlatforCursor(Cursor.class, platformCursor);
        return platformCursor;
    }

    public static Dimension2D getBestCursorSize(int preferredWidth, int preferredHeight) {
        Size size = Cursor.getBestSize(preferredWidth, preferredHeight);
        return new Dimension2D(size.width, size.height);
    }

    private static Cursor createPlatformCursor(CursorFrame cursorFrame) {
        Application app = Application.GetApplication();
        switch (cursorFrame.getCursorType()) {
            case CROSSHAIR:
                return app.createCursor(3);
            case TEXT:
                return app.createCursor(2);
            case WAIT:
                return app.createCursor(14);
            case DEFAULT:
                return app.createCursor(1);
            case OPEN_HAND:
                return app.createCursor(5);
            case CLOSED_HAND:
                return app.createCursor(4);
            case HAND:
                return app.createCursor(6);
            case H_RESIZE:
                return app.createCursor(11);
            case V_RESIZE:
                return app.createCursor(12);
            case MOVE:
                return app.createCursor(19);
            case DISAPPEAR:
                return app.createCursor(13);
            case SW_RESIZE:
                return app.createCursor(15);
            case SE_RESIZE:
                return app.createCursor(16);
            case NW_RESIZE:
                return app.createCursor(17);
            case NE_RESIZE:
                return app.createCursor(18);
            case N_RESIZE:
            case S_RESIZE:
                return app.createCursor(12);
            case W_RESIZE:
            case E_RESIZE:
                return app.createCursor(11);
            case NONE:
                return app.createCursor(-1);
            case IMAGE:
                return createPlatformImageCursor((ImageCursorFrame) cursorFrame);
            default:
                System.err.println("unhandled Cursor: " + ((Object) cursorFrame.getCursorType()));
                return app.createCursor(1);
        }
    }

    private static Cursor createPlatformImageCursor(ImageCursorFrame imageCursorFrame) {
        return createPlatformImageCursor(imageCursorFrame.getPlatformImage(), (float) imageCursorFrame.getHotspotX(), (float) imageCursorFrame.getHotspotY());
    }

    private static Cursor createPlatformImageCursor(Object platformImage, float hotspotX, float hotspotY) {
        if (platformImage == null) {
            throw new IllegalArgumentException("QuantumToolkit.createImageCursor: no image");
        }
        if (!$assertionsDisabled && !(platformImage instanceof Image)) {
            throw new AssertionError();
        }
        Image prismImage = (Image) platformImage;
        int iheight = prismImage.getHeight();
        int iwidth = prismImage.getWidth();
        Dimension2D d2 = getBestCursorSize(iwidth, iheight);
        float bestWidth = (float) d2.getWidth();
        float bestHeight = (float) d2.getHeight();
        if (bestWidth <= 0.0f || bestHeight <= 0.0f) {
            return Application.GetApplication().createCursor(1);
        }
        switch (prismImage.getPixelFormat()) {
            case INT_ARGB_PRE:
                return createPlatformImageCursor((int) hotspotX, (int) hotspotY, iwidth, iheight, prismImage.getPixelBuffer());
            case BYTE_RGB:
            case BYTE_BGRA_PRE:
            case BYTE_GRAY:
                ByteBuffer buf = (ByteBuffer) prismImage.getPixelBuffer();
                float xscale = bestWidth / iwidth;
                float yscale = bestHeight / iheight;
                int scaledHotSpotX = (int) (hotspotX * xscale);
                int scaledHotSpotY = (int) (hotspotY * yscale);
                PushbroomScaler scaler = ScalerFactory.createScaler(iwidth, iheight, prismImage.getBytesPerPixelUnit(), (int) bestWidth, (int) bestHeight, true);
                byte[] bytes = new byte[buf.limit()];
                int scanlineStride = prismImage.getScanlineStride();
                for (int z2 = 0; z2 < iheight; z2++) {
                    buf.position(z2 * scanlineStride);
                    buf.get(bytes, 0, scanlineStride);
                    if (scaler != null) {
                        scaler.putSourceScanline(bytes, 0);
                    }
                }
                buf.rewind();
                Image img = prismImage.iconify(scaler.getDestination(), (int) bestWidth, (int) bestHeight);
                return createPlatformImageCursor(scaledHotSpotX, scaledHotSpotY, img.getWidth(), img.getHeight(), img.getPixelBuffer());
            default:
                throw new IllegalArgumentException("QuantumToolkit.createImageCursor: bad image format");
        }
    }

    private static Cursor createPlatformImageCursor(int x2, int y2, int width, int height, Object buffer) {
        Application app = Application.GetApplication();
        return app.createCursor(x2, y2, app.createPixels(width, height, (IntBuffer) buffer));
    }
}
