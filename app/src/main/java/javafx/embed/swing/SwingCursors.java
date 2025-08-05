package javafx.embed.swing;

import com.sun.javafx.cursor.CursorFrame;
import com.sun.javafx.cursor.ImageCursorFrame;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:javafx/embed/swing/SwingCursors.class */
class SwingCursors {
    SwingCursors() {
    }

    private static Cursor createCustomCursor(ImageCursorFrame cursorFrame) throws HeadlessException {
        Toolkit awtToolkit = Toolkit.getDefaultToolkit();
        double imageWidth = cursorFrame.getWidth();
        double imageHeight = cursorFrame.getHeight();
        Dimension nativeSize = awtToolkit.getBestCursorSize((int) imageWidth, (int) imageHeight);
        double scaledHotspotX = (cursorFrame.getHotspotX() * nativeSize.getWidth()) / imageWidth;
        double scaledHotspotY = (cursorFrame.getHotspotY() * nativeSize.getHeight()) / imageHeight;
        Point hotspot = new Point((int) scaledHotspotX, (int) scaledHotspotY);
        BufferedImage awtImage = SwingFXUtils.fromFXImage(Image.impl_fromPlatformImage(cursorFrame.getPlatformImage()), null);
        return awtToolkit.createCustomCursor(awtImage, hotspot, null);
    }

    static Cursor embedCursorToCursor(CursorFrame cursorFrame) {
        switch (cursorFrame.getCursorType()) {
            case DEFAULT:
                return Cursor.getPredefinedCursor(0);
            case CROSSHAIR:
                return Cursor.getPredefinedCursor(1);
            case TEXT:
                return Cursor.getPredefinedCursor(2);
            case WAIT:
                return Cursor.getPredefinedCursor(3);
            case SW_RESIZE:
                return Cursor.getPredefinedCursor(4);
            case SE_RESIZE:
                return Cursor.getPredefinedCursor(5);
            case NW_RESIZE:
                return Cursor.getPredefinedCursor(6);
            case NE_RESIZE:
                return Cursor.getPredefinedCursor(7);
            case N_RESIZE:
            case V_RESIZE:
                return Cursor.getPredefinedCursor(8);
            case S_RESIZE:
                return Cursor.getPredefinedCursor(9);
            case W_RESIZE:
            case H_RESIZE:
                return Cursor.getPredefinedCursor(10);
            case E_RESIZE:
                return Cursor.getPredefinedCursor(11);
            case OPEN_HAND:
            case CLOSED_HAND:
            case HAND:
                return Cursor.getPredefinedCursor(12);
            case MOVE:
                return Cursor.getPredefinedCursor(13);
            case DISAPPEAR:
                return Cursor.getPredefinedCursor(0);
            case NONE:
                return null;
            case IMAGE:
                return createCustomCursor((ImageCursorFrame) cursorFrame);
            default:
                return Cursor.getPredefinedCursor(0);
        }
    }

    static javafx.scene.Cursor embedCursorToCursor(Cursor cursor) {
        if (cursor == null) {
            return javafx.scene.Cursor.DEFAULT;
        }
        switch (cursor.getType()) {
        }
        return javafx.scene.Cursor.DEFAULT;
    }
}
