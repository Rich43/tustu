package sun.awt;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;

/* loaded from: rt.jar:sun/awt/CustomCursor.class */
public abstract class CustomCursor extends Cursor {
    protected Image image;

    protected abstract void createNativeCursor(Image image, int[] iArr, int i2, int i3, int i4, int i5);

    public CustomCursor(Image image, Point point, String str) throws IndexOutOfBoundsException, HeadlessException {
        super(str);
        this.image = image;
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Canvas canvas = new Canvas();
        MediaTracker mediaTracker = new MediaTracker(canvas);
        mediaTracker.addImage(image, 0);
        try {
            mediaTracker.waitForAll();
        } catch (InterruptedException e2) {
        }
        int width = image.getWidth(canvas);
        int height = image.getHeight(canvas);
        if (mediaTracker.isErrorAny() || width < 0 || height < 0) {
            point.f12371y = 0;
            point.f12370x = 0;
        }
        Dimension bestCursorSize = defaultToolkit.getBestCursorSize(width, height);
        if ((bestCursorSize.width != width || bestCursorSize.height != height) && bestCursorSize.width != 0 && bestCursorSize.height != 0) {
            image = image.getScaledInstance(bestCursorSize.width, bestCursorSize.height, 1);
            width = bestCursorSize.width;
            height = bestCursorSize.height;
        }
        if (point.f12370x >= width || point.f12371y >= height || point.f12370x < 0 || point.f12371y < 0) {
            throw new IndexOutOfBoundsException("invalid hotSpot");
        }
        int[] iArr = new int[width * height];
        try {
            new PixelGrabber(image.getSource(), 0, 0, width, height, iArr, 0, width).grabPixels();
        } catch (InterruptedException e3) {
        }
        createNativeCursor(this.image, iArr, width, height, point.f12370x, point.f12371y);
    }
}
